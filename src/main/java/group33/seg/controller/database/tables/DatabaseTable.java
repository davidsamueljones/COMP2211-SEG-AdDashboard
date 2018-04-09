package group33.seg.controller.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseTable {

  /**
   * Create a table of the required type at the given connection.
   *
   * @param c Database connection
   */
  public abstract void createTable(Connection c) throws SQLException;

  /**
   * Creates required indexes for that table
   *
   * @param c Database connection
   */
  public abstract void createIndexes(Connection c) throws SQLException;

  /**
   * Convert a set of string parameters to their correct format and create a prepared statement.
   *  @param ps Prepared statement to apply params to
   * @param params Params to apply to the prepared statement
   * @param campaignID For primary key reference
   */
  public abstract void prepareInsert(PreparedStatement ps, String[] params, int campaignID) throws SQLException;

  /** 
   * @return Template used for INSERT statements 
   */
  public abstract String getInsertTemplate();

  /**
   * Convert a CSV string to be in table format.
   * 
   * @param input CSV string to convert
   * @param campaignID Common data item, set to -1 if not needed
   * @return Converted string
   */
  public abstract String fromCSV(String input, int campaignID);
  
  /**
   * @param Source for copy, paths must be single quoted
   * @return Template used for COPY statements
   */
  public abstract String getCopyTemplate(String source);
  
  /**
   *  @return Name of table 
   */
  public abstract String getTableName();

  /** 
   * Clear all rows from table. 
   */
  public final void clearTable(Connection c) {
    Statement cs;
    try {
      cs = c.createStatement();
      cs.execute(String.format("TRUNCATE TABLE %s", getTableName()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Delete the table from the database
   *
   */
  public final void dropTable(Connection c) {
    Statement cs;
    try {
      cs = c.createStatement();
      cs.execute(String.format("DROP TABLE %s CASCADE", getTableName()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
}
