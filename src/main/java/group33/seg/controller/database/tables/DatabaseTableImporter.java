package group33.seg.controller.database.tables;

import com.opencsv.CSVReader;
import group33.seg.controller.utilities.DashboardUtilities;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTableImporter {
  private final int batchSize = 500;
  
  /** Current progress of import */
  private volatile int progress;
  
  /** Whether import was successful */
  private volatile boolean successful;
  
  /**
   * Convert a CSV file into batches of insert statements. Execute these batches to fully import the
   * file into the given database.
   * 
   * @param c Database connection
   * @param path Path to CSV file
   * @throws InterruptedException 
   */
  public void importCSV(DatabaseTable table, Connection c, String path, int campaignID) throws SQLException {
    // Find total number of lines for progress feedback (remove header)
    int availableRecords = DashboardUtilities.countFileLines(path) - 1;
    successful = false;
    
    CSVReader reader = null;
    try {
      reader = new CSVReader(new FileReader(path));

      // Skip CSV header
      reader.readNext();

      // Generate statements, executing when batch limit hit
      String[] record;
      int count = 0;
      PreparedStatement ps = c.prepareStatement(table.getInsertTemplate());
      for (int i = 1; i <= availableRecords; i++) {
        // Create insert statement
        record = reader.readNext();
        table.prepareInsert(ps, record, campaignID);
        // Handle batching
        ps.addBatch();
        count += 1;
        if (count == batchSize || i == availableRecords) {
          ps.executeBatch();
          updateProgress((int) (100 * i / (double) availableRecords));
          count = 0;
          if (i == availableRecords) {
            successful = true;
          }
        }     
        // Stop import if thread is interrupted
        if (Thread.currentThread().isInterrupted()) {
          break;
        }
      }
      // Close prepared statement
      ps.close();

    } catch (IOException e) {
      // ignore
    } finally {
      // Close CSV reader
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }
  
  /**
   * Update progress value.
   * 
   * @param progress Current progress of import
   */
  private void updateProgress(int progress) {
    this.progress = progress;
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
