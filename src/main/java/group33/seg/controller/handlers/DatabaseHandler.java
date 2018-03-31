package group33.seg.controller.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.database.DatabaseQueryFactory;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Pair;

public class DatabaseHandler {
  
  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  private final String databaseConfig;
  
  private Map<DatabaseConnection, Boolean> connections = new ConcurrentHashMap<>();

  private Map<MetricQuery, MetricQueryResponse> cachedResponses = new ConcurrentHashMap<>();
  
  /**
   * Instantiate a database handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public DatabaseHandler(DashboardMVC mvc, String databaseConfig) {
    this.mvc = mvc;
    this.databaseConfig = databaseConfig;
  }

  /** Number of query threads */
  private final ExecutorService pool = Executors.newFixedThreadPool(10);

  private synchronized DatabaseConnection getConnection() {
    for (DatabaseConnection c : connections.keySet()) {
      // true if available
      if (connections.get(c)) {
        connections.put(c, false);
        return c;
      }
    }
    
    try {
      DatabaseConfig config = new DatabaseConfig(databaseConfig);
      DatabaseConnection connection =
          new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
      connections.put(connection, false);
      return connection;
    } catch (Exception e) {
      return null;
    }

  }

  private void returnConnection(DatabaseConnection c) {
    connections.put(c, true);
  }

  public MetricQueryResponse getQueryResponse(MetricQuery request) {
    // TODO: MetricQuery does not and will not implement hashcode and equals to do anything other
    // than checking if the same object is being compared, caching must use then converted SQL
    // otherwise the same query will cache multiple times
    // TODO: Caching quickly eats RAM when graph data is considered, this needs to be improved or
    // removed
    //    if (cachedResponses.containsKey(request)) {
    //      return cachedResponses.get(request);
    //    } else {
    MetricQueryResponse response =
        new MetricQueryResponse(request, pool.submit(() -> getGraphData(request)));
    //      cachedResponses.put(request, response);
    return response;
  }

  private List<Pair<String, Integer>> getGraphData(MetricQuery request) {
    DatabaseConnection connection = getConnection();
    String sql = DatabaseQueryFactory.generateSQL(request);
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
