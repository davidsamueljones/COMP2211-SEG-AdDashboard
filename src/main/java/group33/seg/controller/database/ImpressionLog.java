package group33.seg.controller.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class ImpressionLog extends DatabaseTable {

  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute("CREATE TABLE IF NOT EXISTS impression_log (date TIMESTAMP,"
        + "user_id BIGINT NOT NULL, female BOOLEAN, age VARCHAR(10), "
        + "income VARCHAR(7), context VARCHAR(20), impression_cost REAL)");
    st.close();
  }

  @Override
  public void prepareInsert(PreparedStatement ps, String[] params) throws SQLException {
    if (params.length != 7) {
      throw new IllegalArgumentException("Incorrect number of parameters for prepared statement");
    }
    // Create prepared statement
    ps.setTimestamp(1, Timestamp.valueOf(params[0])); // date
    ps.setLong(2, Long.valueOf(params[1])); // user_id
    ps.setBoolean(3, params[2].equals("female") ? true : false); // female
    ps.setString(4, params[3]); // age
    ps.setString(5, params[4]); // income
    ps.setString(6, params[5]); // context
    ps.setDouble(7, Double.valueOf(params[6])); // impression_cost
  }

  @Override
  public String getInsertTemplate() {
    return "INSERT INTO impression_log (date, user_id, female, age, income, context, impression_cost) "
        + "values (?, ?, ?, ?, ?, ?, ?)";
  }

}
