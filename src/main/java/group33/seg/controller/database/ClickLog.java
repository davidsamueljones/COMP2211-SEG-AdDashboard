package group33.seg.controller.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ClickLog implements DatabaseTable {

  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS click_log (date TIMESTAMP,"
            + " user_id BIGINT NOT NULL, "
            + "click_cost REAL)");
    st.close();
  }

  @Override
  public void importFile(Connection c, String filepath) throws SQLException {
    Statement st = c.createStatement();
    st.execute("COPY click_log FROM '" + filepath + "' WITH DELIMITER ',' CSV HEADER");
    st.close();
  }
}
