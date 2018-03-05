package group33.seg;

import java.sql.SQLException;

import group33.seg.controller.database.Database;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;

public class Main {
  public static void main(String[] args) throws SQLException {
    DatabaseConfig config = new DatabaseConfig("config.properties");
    DatabaseConnection dbconn =
        new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
    dbconn.connectDatabase();

    /** Query for fetching total number of impressions data */
    // TODO move somewhere else, once the rest of the classes are finished
    MetricQuery mq = new MetricQuery(null, Metric.IMPRESSIONS, null, null);
    Database db = new Database(null);
    System.out.println(
        "Total number of impressions " + db.getResponse(mq).getResult().get(0).value);
  }
}
