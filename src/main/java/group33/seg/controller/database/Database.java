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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO think of a better class name that is not DatabaseInterface, since it's not an interface
public class Database {
  private ConcurrentHashMap<DatabaseConnection, Boolean> connections;
  private ConcurrentHashMap<MetricQuery, MetricQueryResponse> previous;
  private final ExecutorService pool = Executors.newFixedThreadPool(10);
  private CampaignConfig campaignConfig;

  public Database(CampaignConfig campaignConfig) {
    this.campaignConfig = campaignConfig;
    previous = new ConcurrentHashMap<>();
    connections = new ConcurrentHashMap<>();
  }

  private synchronized DatabaseConnection getConnection() {
    for (DatabaseConnection c : connections.keySet()) {
      // true if available
      if (connections.get(c)) {
        connections.put(c, false);
        return c;
      }
    }
    DatabaseConfig config = new DatabaseConfig("config.properties");
    DatabaseConnection connection =
        new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
    connections.put(connection, false);
    return connection;
  }

  private void returnConnection(DatabaseConnection c) {
    connections.put(c, true);
  }

  public MetricQueryResponse getResponse(MetricQuery request) {
    if (previous.containsKey(request)) {
      return previous.get(request);
    } else {
      MetricQueryResponse response =
          new MetricQueryResponse(request, pool.submit(() -> getGraphData(request)));

      previous.put(request, response);
      return response;
    }
  }

  private List<Pair<String, Integer>> getGraphData(MetricQuery request) {
    DatabaseConnection connection = getConnection();
    String sql = new DatabaseQueryFactory().generateSql(request);
    List<Pair<String, Integer>> result = new LinkedList<>();

    try {
      Statement cs = connection.connectDatabase().createStatement();
      ResultSet rs = cs.executeQuery(sql);

      while (rs.next()) {
        result.add(new Pair<>(rs.getString("xaxis"), rs.getInt("yaxis")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    returnConnection(connection);

    return result;
  }
}
