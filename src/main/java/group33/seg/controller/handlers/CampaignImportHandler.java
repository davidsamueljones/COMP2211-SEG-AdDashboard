package group33.seg.controller.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.postgresql.util.PSQLState;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.database.tables.CampaignTable;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.DatabaseTable;
import group33.seg.controller.database.tables.DatabaseTableImporter;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.ProgressListener;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.CampaignImportConfig;

/**
 * Controller class handling import of campaign data using a separate thread and the current
 * database connection. Use listening infrastructure to communicate any external updates.
 */
public class CampaignImportHandler {
  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Thread safety lock for importedCampaign */
  private final Object lockImportedCampaign = new Object();

  /** Last campaign to be imported */
  private CampaignConfig importedCampaign = null;

  /** Current progress of import */
  private volatile int progress;

  /** Reference thread used for importing */
  private Thread threadImport;

  /** Listeners to alert about import progress */
  private final Set<ProgressListener> progressListeners = new HashSet<>();

  /** Error builder to store errors pertaining to the last import */
  private ErrorBuilder eb = new ErrorBuilder();

  /**
   * Instantiate an import handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public CampaignImportHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }

  /**
   * Import CSV files into tables using a pool database connection. Data will be imported as a new
   * campaign with existing data unaffected. Progress listeners are kept updated on status of import
   * and any errors are built up using the instances internal ErrorBuilder. As the import is handled
   * on another thread, {@link CampaignImportHandler#cancelImport} must be used to interrupt an
   * ongoing import.
   *
   * @param importConfig Campaign information required for import
   * @return Whether import started successfully
   */
  public boolean doImport(CampaignImportConfig importConfig) {
    // Refresh error builder for this import, whilst validating config
    eb = importConfig.validate();

    // Error in configuration
    if (eb.isError()) {
      return false;
    }
    // Cannot start another import whilst another is ongoing
    if (isOngoing()) {
      eb.addError("An import is already ongoing");
      return false;
    }

    // Handle import on a separate thread
    threadImport = new Thread(() -> {
      importCampaign(importConfig);
      threadImport = null;
    });

    // Import can start successfully
    this.importedCampaign = null;
    threadImport.start();
    return true;
  }

  /**
   * Import control method, should be called using {@link doImport}.
   * 
   * @param importConfig Campaign information required for import
   */
  private void importCampaign(CampaignImportConfig importConfig) {
    boolean finished = false;

    // Alert listeners that import is starting
    alertStart();
    // Reset import progress
    updateProgress(0);

    // Use a single connection for the entire transaction
    DatabaseConnection db = null;
    Connection conn = null;
    int campaignID = -1;

    try {
      // Create connection
      db = mvc.controller.database.getConnection();
      try {
        conn = db.connectDatabase();
      } catch (SQLException e) {
        if (PSQLState.CONNECTION_UNABLE_TO_CONNECT.getState().equals(e.getSQLState())) {
          eb.addError(
              "Connection to server refused, check hostname, port, username and password are correct");
          throw new ImportException();
        } else {
          throw new RuntimeException(e);
        }
      }

      // Create a new campaign
      campaignID = createNewCampaign(conn, importConfig.campaignName);

      // Use the file size to determine the proportions when importing
      double sizeImpressionLog = new File(importConfig.pathImpressionLog).length();
      double sizeClickLog = new File(importConfig.pathClickLog).length();
      double sizeServerLog = new File(importConfig.pathServerLog).length();
      double totalSize = sizeImpressionLog + sizeClickLog + sizeServerLog;

      // Import click log
      importTable(new ClickLogTable(), conn, importConfig.pathClickLog, sizeClickLog / totalSize,
          campaignID);
      // Import impression log
      importTable(new ImpressionLogTable(), conn, importConfig.pathImpressionLog,
          sizeImpressionLog / totalSize, campaignID);
      // Import server log
      importTable(new ServerLogTable(), conn, importConfig.pathServerLog, sizeServerLog / totalSize,
          campaignID);

      // Create campaign configuration (storing as last import)
      CampaignConfig campaign = new CampaignConfig(campaignID);
      campaign.name = importConfig.campaignName;
      setImportedCampaign(campaign);

      // Alert listeners that import is finished
      alertFinished(true);
      finished = true;
    } catch (InterruptedException e) {
      alertCancelled();
    } catch (ImportException e) {
      alertFinished(false);
    } catch (Exception e) {
      eb.addError("Unknown Error");
      e.printStackTrace();
      alertFinished(false);
    } finally {
      // Remove 'corrupted' data if import did not finish successfully
      if (conn != null && !finished && campaignID != -1) {
        removeCampaign(conn, campaignID);
      }
      try {
        conn.close();
      } catch (SQLException e) {
        // Couldn't close ignore
      } finally {
        // Return database connection
        mvc.controller.database.returnConnection(db);
      }
    }
  }

  private void removeCampaign(Connection conn, int campaignID) {
    try {
      Statement st = conn.createStatement();
      // TODO: Enable CASCADE in Campaign table (currently will cause exception)
      st.execute(String.format("DELETE FROM %s WHERE id = %d", (new CampaignTable()).getTableName(),
          campaignID));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create a new campaign on the server. If the campaign table doesn't exist it is created.
   * 
   * @param conn Connection to use
   * @param name Name of campaign to add
   * @return UID from server pertaining to campaign entry (primary key)
   */
  private int createNewCampaign(Connection conn, String name) throws ImportException {
    int campaignID = -1;
    CampaignTable campaignTable = new CampaignTable();
    try {
      // Create campaign table if it doesn't exist
      campaignTable.createTable(conn);
      // Create a new campaign, tracking the generated primary key
      PreparedStatement ps =
          conn.prepareStatement(campaignTable.getInsertTemplate(), Statement.RETURN_GENERATED_KEYS);
      campaignTable.prepareInsert(ps, new String[] {name}, -1);
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      rs.next();
      campaignID = rs.getInt(1);
    } catch (SQLException e) {
      eb.addError("Unable to create new campaign");
      throw new ImportException();
    }
    return campaignID;
  }

  /**
   * Use a worker thread to import a given database type with the given file.
   *
   * @param table Table type to import to
   * @param conn Database connection
   * @param path Path to file to import
   * @param weight Weighting for progress updates
   */
  private void importTable(DatabaseTable table, Connection conn, String path, double weight,
      int campaignID) throws InterruptedException {
    DatabaseTableImporter importer = new DatabaseTableImporter();
    final int curProgress = progress;

    // Ensure table is created
    try {
      table.createTable(conn);
      table.createIndexes(conn);
    } catch (SQLException e) {
      e.printStackTrace();
      eb.addError("Database error, consult your administrator");
      throw new ImportException();
    }

    // Create worker thread handling import
    Thread worker = new Thread(() -> {
      try {
        System.out.println(campaignID);
        importer.importCSV(table, conn, path, campaignID);
      } catch (Exception e) {
        e.printStackTrace();
        // do nothing, let main import thread handle
      }
    });

    // Start worker
    worker.start();
    // Wait for worker to finish
    while (worker.isAlive()) {
      boolean interrupt = false;
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        interrupt = true;
      }
      // Check for interrupts
      if (interrupt || Thread.currentThread().isInterrupted()) {
        worker.interrupt();
        worker.join(0);
        throw new InterruptedException();
      }
      // Update progress
      updateProgress(curProgress + (int) (importer.getProgress() * weight));
    }
    // Check if import was successful
    if (!importer.isSuccessful()) {
      eb.addError(String
          .format("Error importing file '%s', check that the file is of the correct format", path));
      throw new ImportException();
    }
  }

  /**
   * Cancel an ongoing import (if one is ongoing). Let importer control how cancellation occurs.
   *
   * @param wait Whether to halt the current thread until cancellation is finished
   * @return Whether import was cancelled
   */
  public boolean cancelImport(boolean wait) {
    if (isOngoing()) {
      threadImport.interrupt();
      if (wait) {
        try {
          threadImport.join();
          return true;
        } catch (InterruptedException e) {
          return false;
        }
      }
    }
    return false;
  }

  /**
   * @return Whether an import is ongoing
   */
  public boolean isOngoing() {
    return (threadImport != null && threadImport.isAlive());
  }

  /**
   * @return Current progress level
   */
  public int getProgress() {
    return progress;
  }

  /**
   * Thread safe access to internal campaign.
   *
   * @return Last imported campaign, null if none or unsuccessful
   */
  public CampaignConfig getImportedCampaign() {
    synchronized (lockImportedCampaign) {
      return importedCampaign;
    }
  }

  /**
   * Thread safe setting of internal imported campaign set.
   *
   * @param importedCampaign Campaign to store
   */
  private void setImportedCampaign(CampaignConfig importedCampaign) {
    synchronized (lockImportedCampaign) {
      this.importedCampaign = importedCampaign;
    }
  }

  /**
   * @param progressListener Progress listener to start sending alerts to
   */
  public void addProgressListener(ProgressListener progressListener) {
    progressListeners.add(progressListener);
  }

  /**
   * @param progressListener Progress listener to no longer alert
   */
  public void removeProgressListener(ProgressListener progressListener) {
    progressListeners.remove(progressListener);
  }

  /**
   * Helper function to alert all listeners that an import has started.
   */
  private void alertStart() {
    for (ProgressListener listener : progressListeners) {
      listener.start();
    }
  }

  /**
   * Helper function to alert all listeners that an import has been cancelled.
   */
  private void alertCancelled() {
    for (ProgressListener listener : progressListeners) {
      listener.cancelled();
    }
  }

  /**
   * Helper function to alert all listeners that an import finished.
   *
   * @param success Whether the import finished successfully
   */
  private void alertFinished(boolean success) {
    for (ProgressListener listener : progressListeners) {
      listener.finish(success);
    }
  }

  /**
   * Update progress value, notifying all listeners.
   *
   * @param progress Current progress of import
   */
  private void updateProgress(int progress) {
    this.progress = progress;
    for (ProgressListener listener : progressListeners) {
      listener.progressUpdate(progress);
    }
  }

  /**
   * @return Error builder pertaining to last import
   */
  public ErrorBuilder getErrors() {
    return eb;
  }

  /**
   * Unchecked exception for importing failures
   */
  public class ImportException extends RuntimeException {
    private static final long serialVersionUID = -3767480036135704125L;
  }

}
