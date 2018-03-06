package group33.seg.controller.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

public class ServerLog extends DatabaseTable {
  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS server_log (entry_date TIMESTAMP, user_id BIGINT NOT NULL,"
            + "exit_date TIMESTAMP, pages_viewed INTEGER, conversion BOOLEAN)");
    st.close();
  }


  @Override
  public void prepareInsert(PreparedStatement ps, String[] params) throws SQLException {
    if (params.length != 5) {
      throw new IllegalArgumentException("Incorrect number of parameters for prepared statement");
    }
    // Create prepared statement
    ps.setTimestamp(1, Timestamp.valueOf(params[0])); // entry_date
    ps.setLong(2, Long.valueOf(params[1])); // user_id
    // exit_date (can be null equivalent)
    if (params[2].equals("n/a")) {
      ps.setNull(3, Types.TIMESTAMP);
    } else {
      ps.setTimestamp(3,   Timestamp.valueOf(params[2])); 
    }   
    ps.setInt(4, Integer.valueOf(params[3])); // pages_viewed
    ps.setBoolean(5, Boolean.valueOf(params[4])); // Conversion
  }

  @Override
  protected String getInsertTemplate() {
    return "INSERT INTO server_log (entry_date, user_id, exit_date, pages_viewed, conversion) "
        + "values (?, ?, ?, ?, ?)";
  }

}
