package group33.seg.controller.database;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.opencsv.CSVReader;
import group33.seg.controller.utilities.DashboardUtilities;

public abstract class DatabaseTable {
  private volatile int importProgress;
  
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
  public abstract String getInsertTemplate();

}


