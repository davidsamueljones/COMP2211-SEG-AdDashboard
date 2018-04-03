package group33.seg.controller.database.tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.Encoding;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.controller.utilities.DashboardUtilities;

public class DatabaseTableImporter {
  /** Buffer size used for imports */
  private static final int BUFFER_SIZE = 1048576;

  /** Database handler to use for import */
  private final DatabaseHandler databaseHandler;

  /** Whether an import is ongoing */
  private volatile boolean ongoing = false;

  /** Current progress of import */
  private volatile int progress;

  /** Whether import was successful */
  private volatile boolean successful;

  /** Whether a cancellation request has occurred */
  private volatile boolean cancelling = false;

  /** Import transaction PID for cancellation requests */
  private volatile int pid = -1;


  /**
   * Create a database importer that uses a database handler as a connection source. This sub
   * controller does not need any other controller objects.
   * 
   * @param databaseHandler
   */
  public DatabaseTableImporter(DatabaseHandler databaseHandler) {
    this.databaseHandler = databaseHandler;
  }

  /**
   * Start a managed import for the given file using the given table type. This has the same
   * behaviour as calling {@link start} on a new thread but interrupting this call thread will cause
   * a cancellation request to occur automatically.
   * 
   * @param path Path to file for import
   * @param table Table type to handle input data as
   * @param campaignID Campaign to use for table manipulation
   */
  public void doImport(String path, DatabaseTable table, int campaignID) {
    // Start import on new thread
    Thread t = new Thread(() -> {
      System.out.println("Starting import...");
      try {
        start(path, table, campaignID);
        successful = true;
      } catch (Exception e) {
        successful = false;
      }      
    });
    t.start();
    // Wait for import to finish, but allow for cancellation through interrupt
    try {
      t.join();
      System.out.println("Finished import...");
    } catch (InterruptedException e) {
      System.out.println("Cancelling import...");
      cancel();
      try {
        t.join();
      } catch (InterruptedException e1) {
        System.err.println("Interrupted when waiting for import to be cancelled");
      }
    }
  }

  /**
   * Start an import for the given file using the given table type. This call will cause an
   * unchecked exception if a import is already ongoing. The PID used for this import is reported to
   * the class instance so cancellations are possible using {@link cancel}.
   * 
   * @param path Path to file for import
   * @param table Table type to handle input data as
   * @param campaignID Campaign to use for table manipulation
   * @return
   */
  private long start(String path, DatabaseTable table, int campaighID) {
    DatabaseConnection db = null;
    Connection conn = null;
    BaseConnection bconn = null;

    if (this.ongoing) {
      throw new IllegalStateException("This instance is already doing an import");
    }
    this.ongoing = true;

    long written = -1;
    try {
      try {
        // Create server connection
        db = databaseHandler.getConnection();
        conn = db.connectDatabase();
        if (conn instanceof BaseConnection) {
          bconn = (BaseConnection) conn;
        } else {
          throw new RuntimeException("Database connection did not generate a BaseConnection type");
        }
      } catch (SQLException e) {
        throw new RuntimeException("Unable to make database connection", e);
      }

      // Track PID for cancellation requests
      this.pid = DatabaseHandler.getPID(bconn);

      try {
        written = write(bconn, path, table, campaighID);
      } catch (SQLException e) {
        if (!cancelling) {
          throw new RuntimeException("Server error when importing", e);
        }
      } catch (IOException e) {
        throw new RuntimeException("Unable to read from import file", e);
      }
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        // unable to close connection, ignore
      }
      databaseHandler.returnConnection(db);
      this.pid = -1;
      this.ongoing = false;
      this.cancelling = false;
    }
    return written;
  }

  /**
   * Write data from an input file to the given connection using bulk copying methods routed through
   * an input stream. This method allows the data to be manipulated in line with table rules before
   * copying occurs. Progress is reported to the class progress field.
   * 
   * @param conn Connection to use for data copy
   * @param path Path to file for import
   * @param table Table type to handle input data as
   * @param campaignID Campaign to use for table manipulation
   * @return How many rows were written
   */
  private long write(BaseConnection conn, String path, DatabaseTable table, int campaignID)
      throws SQLException, IOException {
    BufferedReader in = null;
    CopyIn cp = null;
    try {
      // Initialise copy, routing input file through buffered reader
      in = new BufferedReader(new FileReader(new File(path)));

      int toImport = DashboardUtilities.countFileLines(path) - 1;
      CopyManager copyManager = new CopyManager(conn);
      cp = copyManager.copyIn(table.getCopyTemplate("STDIN"));

      // Allocate buffer for import
      final byte[] buf = new byte[BUFFER_SIZE];
      int payload = 0;

      int imported = 0;
      this.progress = 0;
      while (in.ready()) {
        // Read in line by line, preparing appropriately and converting to bytes
        String line = in.readLine();
        line = table.fromCSV(line, campaignID) + '\n';
        byte[] bline = Encoding.defaultEncoding().encode(line);

        // If line would overflow buffer write the buffer now
        if (payload + bline.length > BUFFER_SIZE) {
          push(cp, buf, 0, payload);
          cp.flushCopy();
          this.progress = (int) (100 * imported / (double) toImport);
          payload = 0;
        }

        // Append line bytes into buffer
        for (byte b : bline) {
          buf[payload] = b;
          payload++;
        }
        imported++;
      }
      // Write any remaining bytes
      push(cp, buf, 0, payload);
      System.out.println("FINISHING");
      long rows = cp.endCopy();
      System.out.println(toImport + " " + rows);
      progress = 100;
      return rows;
    } finally {
      // Cancel any copy that is not finished at this point
      if (cp != null) {
        if (cp.isActive()) {
          cp.cancelCopy();
        }
      }
      if (in != null) {
        in.close();
      }
    }
  }

  /**
   * Write a buffer to an initialised connection set up for copying.
   * 
   * @param cp Initialised data copier
   * @param buf Buffer to copy
   * @param off Offset of data in buffer
   * @param len Length of data in buffer to write
   * @return How many bytes were written
   */
  private long push(CopyIn cp, byte[] buf, int off, int len) throws SQLException {
    cp.writeToCopy(buf, off, len);
    return len;
  }

  /**
   * Cancel an ongoing import. First cancellation attempts will use a cancellation request. If this
   * is not successful, termination requests occur.
   * 
   * @return Whether cancellation occurrence was verifiable
   */
  public boolean cancel() {
    int count = 0;
    try {
      cancelling = true;
      while (ongoing) {
        databaseHandler.cancelProcessRequest(pid, count < 5);
        Thread.sleep(100);
      }
      return true;
    } catch (InterruptedException e) {
      System.err.println("Unable to wait to verify if cancellation was successful");
      return false;
    }
  }

  /**
   * @return Current progress level
   */
  public int getProgress() {
    return progress;
  }

  /**
   * @return Whether the last import was successful
   */
  public boolean isSuccessful() {
    return successful;
  }

}
