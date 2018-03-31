package group33.seg.controller.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

public class ServerLogTable extends DatabaseTable {
  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS server_log (entry_date TIMESTAMP, user_id BIGINT NOT NULL,"
            + "exit_date TIMESTAMP, pages_viewed INTEGER, conversion BOOLEAN,  campaign_id integer,"
            + "CONSTRAINT server_log_campaign_id_fkey FOREIGN KEY (campaign_id)"
            + "REFERENCES campaign (id) MATCH SIMPLE)");
    st.close();
  }

  @Override
  public void createIndexes(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS sl_date ON server_log(entry_date,campaign_id);");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS sl_dayDateTrunc ON server_log(date_trunc('day', entry_date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS sl_hourDateTrunc ON server_log(date_trunc('hour', entry_date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS sl_monthDateTrunc ON server_log(date_trunc('month', entry_date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS sl_weekDateTrunc ON server_log(date_trunc('week', entry_date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS sl_yearDateTrunc ON server_log(date_trunc('year', entry_date));");
    st.close();
  }

  @Override
  public void prepareInsert(PreparedStatement ps, String[] params, int campaignID) throws SQLException {
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
      ps.setTimestamp(3, Timestamp.valueOf(params[2]));
    }
    ps.setInt(4, Integer.valueOf(params[3])); // pages_viewed
    ps.setBoolean(5, params[4].equals("Yes")); // conversion
    ps.setInt(6, campaignID); //campaign_id
  }

  @Override
  public String getInsertTemplate() {
    return "INSERT INTO server_log (entry_date, user_id, exit_date, pages_viewed, conversion, campaign_id) "
        + "values (?, ?, ?, ?, ?, ?)";
  }

  @Override
  public String getTableName() {
    return "server_log";
  }
}
