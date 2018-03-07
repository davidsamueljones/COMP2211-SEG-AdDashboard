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
   * Convert a set of string parameters to their correct format and create a prepared statement.
   *
   * @param ps Prepared statement to apply params to
   * @param params Params to apply to the prepared statement
   */
  public abstract void prepareInsert(PreparedStatement ps, String[] params) throws SQLException;

  /** @return Template used for INSERT statements */
  public abstract String getInsertTemplate();

  /** @return Name of table */
  public abstract String getTableName();

  /** Clear all rows from table. */
  public final void clearTable(Connection c) {
    Statement cs;
    try {
      cs = c.createStatement();
      cs.execute(String.format("TRUNCATE TABLE %s", getTableName()));
    } catch (SQLException e) {
      e.printStackTrace();
      // FIXME: Do not ignore this error, throw and handle
    }
  }
}
