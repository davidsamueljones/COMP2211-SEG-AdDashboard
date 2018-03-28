package group33.seg.controller.database.tables;

import java.sql.*;

public class ClickLogTable extends DatabaseTable {

  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS click_log (date TIMESTAMP, "
            + "user_id BIGINT NOT NULL, click_cost REAL, campaign_id integer,"
            + "CONSTRAINT click_log_campaign_id_fkey FOREIGN KEY (campaign_id)"
            + "REFERENCES campaign (id) MATCH SIMPLE);");
    st.close();
  }

  @Override
  public void createIndexes(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS cl_date ON click_log(date);");
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
