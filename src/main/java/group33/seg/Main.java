package group33.seg;

import java.sql.SQLException;
import java.util.List;

import group33.seg.controller.database.Database;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.graphing.Graph;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;

import javax.swing.*;

public class Main {
  public static void main(String[] args) throws SQLException {
    DatabaseConfig config = new DatabaseConfig("config.properties");
    DatabaseConnection dbconn =
        new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
    dbconn.connectDatabase();

    /** Query for fetching total number of impressions data */
    Database db = new Database(null);
    MetricQuery totalImpressions = new MetricQuery(null, Metric.IMPRESSIONS, null, null);
    MetricQuery weeklyImpressions = new MetricQuery(null, Metric.IMPRESSIONS, Interval.WEEK, null);
    MetricQuery monthlyImpressions =
        new MetricQuery(null, Metric.IMPRESSIONS, Interval.MONTH, null);

    /** Get the query response and pass it as a final param to the Graph view data as a chart */
    List<Pair<String, Integer>> weeklyData = db.getResponse(weeklyImpressions).getResult();
    List<Pair<String, Integer>> monthlyData = db.getResponse(weeklyImpressions).getResult();

    Graph chart = new Graph("Number of Impressions", "Time", "Number of impressions", weeklyData);

    JFrame frame = new JFrame();
    frame.add(chart);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
