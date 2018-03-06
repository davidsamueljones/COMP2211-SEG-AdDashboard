package group33.seg.controller.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.opencsv.CSVReader;

public abstract class DatabaseTable {
  
  /**
   * Create a table of the required type at the given connection.
   * 
   * @param c Database connection
   */
  public abstract void createTable(Connection c) throws SQLException;
  
  /**
   * Convert a set of string parameters to their correct format and create a prepared statement.
   * 
   * @param ps Prepared statement to apply params to
   * @param params Params to apply to the prepared statement 
   */
  public abstract void prepareInsert(PreparedStatement ps, String[] params) throws SQLException;
  
  /**
   * @return Template used for INSERT statements
   */
  protected abstract String getInsertTemplate();
  
  
  /**
   * Convert a CSV file into batches of insert statements. Execute these batches to fully
   * import the file into the given database.
   * 
   * @param c Database connection
   * @param path Path to CSV file
   */
  public void importCSV(Connection c, String path) throws SQLException {
    PreparedStatement ps = c.prepareStatement(getInsertTemplate());
    final int batchSize = 1000;
    int count = 0;  
    // TODO: Add progress feedback
    
    CSVReader reader = null;
    try {
      reader = new CSVReader(new FileReader(path));

      // Skip header
      reader.readNext();

      // Generate statements, executing when batch limit hit
      String[] record;
      while ((record = reader.readNext()) != null) {
        prepareInsert(ps, record);
        // Add to the batch
        ps.addBatch();

        count += 1;
        if (count == batchSize) {
          ps.executeBatch();
        }
      }

      // Execute any final statements
      ps.executeBatch();
      ps.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}


