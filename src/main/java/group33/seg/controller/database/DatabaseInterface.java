package group33.seg.controller.database;

import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseInterface {
  private ConcurrentHashMap<DatabaseConnection, Boolean> connections;
  private ConcurrentHashMap<MetricQuery, MetricQueryResponse> previous;
  private final ExecutorService pool = Executors.newFixedThreadPool(10);
  private CampaignConfig campaignConfig;

  public DatabaseInterface(CampaignConfig campaignConfig) {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    this.campaignConfig = campaignConfig;

    previous = new ConcurrentHashMap<MetricQuery, MetricQueryResponse>();
    connections = new ConcurrentHashMap<DatabaseConnection, Boolean>();
  }

  private synchronized DatabaseConnection getConnection() {
    for (DatabaseConnection c : connections.keySet()) {
      //true if available
      if (connections.get(c)) {
        connections.put(c, false);
        return c;
      }
    }

    DatabaseConfig config = new DatabaseConfig("config.properties");
    DatabaseConnection connection = new DatabaseConnection(config.getHost() + campaignConfig.getDatabase(),
        config.getUser(), config.getPassword());

    connections.put(connection, false);
    return connection;
  }

  private void returnConnection(DatabaseConnection c) {
    connections.put(c, true);
  }

  public MetricQueryResponse call(MetricQuery request) {
    if (previous.get(request) != null) {
      return previous.get(request);
    } else {
      MetricQueryResponse response = new MetricQueryResponse(
        request,
        pool.submit(new Callable<List<Pair<String, Integer>>>() {
          public List<Pair<String, Integer>> call() {
            return getNewHistogram(request);
          }
      }));

      previous.put(request, response);
      return response;
    }
  }

  private List<Pair<String, Integer>> getNewHistogram(MetricQuery request) {
    DatabaseConnection connection = getConnection();

    String sql = getSql(request);
    List<Pair<String, Integer>> result = new LinkedList<Pair<String, Integer>>();

    try {
      Statement cs = connection.connectDatabase().createStatement();
      ResultSet rs = cs.executeQuery(sql);

      while (rs.next()) {
        result.add(new Pair<String, Integer>(rs.getString("xaxis"), rs.getInt("yaxis")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    returnConnection(connection);

    return result;
  }

  //needs implementation
  private String getSql(MetricQuery request) {
    return "";
  }
}