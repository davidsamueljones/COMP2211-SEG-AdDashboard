package group33.seg.controller.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class ClickLogTable extends DatabaseTable {

  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS click_log (date TIMESTAMP, "
            + "user_id BIGINT NOT NULL, click_cost DOUBLE PRECISION , campaign_id integer,"
            + "CONSTRAINT click_log_campaign_id_fkey FOREIGN KEY (campaign_id)"
            + "REFERENCES campaign (id) MATCH SIMPLE);");
    st.close();
  }

  @Override
  public void createIndexes(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS cl_date ON click_log(date, campaign_id);");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS cl_dayDateTrunc ON click_log(date_trunc('day', date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS cl_hourDateTrunc ON click_log(date_trunc('hour', date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS cl_monthDateTrunc ON click_log(date_trunc('month', date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS cl_weekDateTrunc ON click_log(date_trunc('week', date));");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS cl_yearDateTrunc ON click_log(date_trunc('year', date));");
    st.close();
  }

  @Override
  public void prepareInsert(PreparedStatement ps, String[] params, int campaignID)
      throws SQLException {
    if (params.length != 3) {
      throw new IllegalArgumentException("Incorrect number of parameters for prepared statement");
    }
    // Create prepared statement
    ps.setTimestamp(1, Timestamp.valueOf(params[0])); // date
    ps.setLong(2, Long.valueOf(params[1])); // user_id
    ps.setDouble(3, Double.valueOf(params[2])); // click_cost
    ps.setInt(4, campaignID); //campaign_id
  }

  @Override
  public String getInsertTemplate() {
    return "INSERT INTO click_log (date, user_id, click_cost, campaign_id) values (?, ?, ?, ?)";
  }

  @Override
  public String getTableName() {
    return "click_log";
  }
}
