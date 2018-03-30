package group33.seg.controller.database.tables;

import java.sql.*;

public class ImpressionLogTable extends DatabaseTable {

  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS impression_log (date TIMESTAMP,"
            + "user_id BIGINT NOT NULL, female BOOLEAN, age age, "
            + "income income, context context, impression_cost DOUBLE PRECISION, campaign_id integer,"
            + "CONSTRAINT impression_log_campaign_id_fkey FOREIGN KEY (campaign_id)"
            + "REFERENCES campaign (id) MATCH SIMPLE)");
    st.close();
  }

  @Override
  public void createIndexes(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS il_date ON impression_log(date, campaign_id);");
    st.execute("CREATE INDEX CONCURRENTLY IF NOT EXISTS il_dayDateTrunc ON impression_log(date_trunc('day', date));");
    st.close();
  }

  @Override
  public void prepareInsert(PreparedStatement ps, String[] params, int campaignID) throws SQLException {
    if (params.length != 7) {
      throw new IllegalArgumentException("Incorrect number of parameters for prepared statement");
    }
    // Create prepared statement
    ps.setTimestamp(1, Timestamp.valueOf(params[0])); // date
    ps.setLong(2, Long.valueOf(params[1])); // user_id
    ps.setBoolean(3, params[2].equals("female")); // female
    ps.setString(4, params[3]); // age
    ps.setString(5, params[4]); // income
    ps.setString(6, params[5]); // context
    ps.setDouble(7, Double.valueOf(params[6])); // impression_cost
    ps.setInt(8, campaignID); // campaign_id
  }

  @Override
  public String getInsertTemplate() {
    return "INSERT INTO impression_log (date, user_id, female, age, income, context, impression_cost, campaign_id) "
        + "values (?, ?, ?, ?::age, ?::income, ?::context, ?, ?)";
  }

  /** Initialise the required enums for the impression log table. */
  public static void initEnums(Connection c) {
    final String enumAge = "CREATE TYPE age AS ENUM ('<25' , '25-34' , '35-44' ,  '45-54' , '>54')";
    final String enumType =
        "CREATE TYPE context AS ENUM ('Social Media', 'News', 'Shopping', 'Blog', 'Hobbies', 'Travel' )";
    final String enumIncome = "CREATE TYPE income AS ENUM ('Low' , 'Medium' , 'High')";

    Statement cs;
    try {
      cs = c.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }
    try {
      cs.execute(enumAge);
    } catch (SQLException e) {
      // ignore type already exists
    }
    try {
      cs.execute(enumType);
    } catch (SQLException e) {
      // ignore type already exists
    }
    try {
      cs.execute(enumIncome);
    } catch (SQLException e) {
      // ignore type already exists
    }
  }

  @Override
  public String getTableName() {
    return "impression_log";
  }
}
