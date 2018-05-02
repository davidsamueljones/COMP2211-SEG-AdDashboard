package group33.seg.controller.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CampaignTable extends DatabaseTable {

  /**
   * Executing the query for creating the table in the database. The id attribute is SERIAL, so that
   * every time a row is inserted in the table the value is automatically incremented. This allows
   * for unique IDs, that represent the connection between a campaign and its logs
   *
   * @param c Database connection
   * @throws SQLException
   */
  @Override
  public void createTable(Connection c) throws SQLException {
    Statement st = c.createStatement();
    st.execute(
        "CREATE TABLE IF NOT EXISTS campaign (id SERIAL NOT NULL, name character varying, CONSTRAINT campaign_pkey PRIMARY KEY (id));");
    st.close();
  }

  @Override
  public void createIndexes(Connection c) throws SQLException {}

  @Override
  public void prepareInsert(PreparedStatement ps, String[] params, int campaignID)
      throws SQLException {
    ps.setString(1, params[0]);
  }

  /**
   * Populating only the id column in the table The name column information will be received as a
   * user input and is handled in {@link group33.seg.controller.handlers.CampaignImportHandler}
   *
   * @return
   */
  @Override
  public String getInsertTemplate() {
    return "INSERT INTO campaign (name) " + "values (?)";
  }

  @Override
  public String fromCSV(String input, int campaignID) {
    throw new IllegalStateException("Campaign table has no expected CSV string format");
  }

  @Override
  public String getCopyTemplate(String source) {
    throw new IllegalStateException("Campaign table has no copy template");
  }

  @Override
  public String getTableName() {
    return "campaign";
  }

}
