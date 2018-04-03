package group33.seg.controller.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

  /** Managed connection pool */
  private final Map<DatabaseConnection, Boolean> connections = new HashMap<>();

  /** Number of query threads */
  private final ExecutorService pool = Executors.newFixedThreadPool(10);

  /**
   * Instantiate a database handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public DatabaseHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }

  /**
   * Using the given configuration, stop handling all old connections and recreate the given number
   * of connections. This pool serves as all managed connections, therefore if a request is made
   * when no connection is available it must wait until a connection is freed.
   * 
   * @param config Database configuration to use for connection creation
   * @param count Number of connections to create
   */
  public void refreshConnections(DatabaseConfig config, int count) {
    synchronized (connections) {
      connections.clear();
      for (int i = 0; i < count; i++) {
        DatabaseConnection connection =
            new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
        connections.put(connection, true);
      }
      connections.notify();
    }
  }

  /**
   * Get an available database connection, if no database connection is available it will wait for a
   * new connection to be available. This call will not timeout and thus must be interrupted to
   * return if no connection gets freed.
   * 
   * @return Connection to database from pool
   */
  public DatabaseConnection getConnection() {
    synchronized (connections) {
      while (true) {
        // Check to see if existing connections available
        for (DatabaseConnection c : connections.keySet()) {
          // true if available
          if (connections.get(c)) {
            connections.put(c, false);
            return c;
          }
        }
        // Wait for notification of possible open connection before trying again
        try {
          connections.wait();
        } catch (InterruptedException e) {
          return null;
        }
      }
    }
  }

  /**
   * Return a database connection to the pool so it can be used again.
   * 
   * @param c Connection to return to pool
   */
  public void returnConnection(DatabaseConnection c) {
    synchronized (connections) {
      connections.put(c, true);
      connections.notify();
    }
  }

  public MetricQueryResponse getQueryResponse(MetricQuery request) {
    return new MetricQueryResponse(request, pool.submit(() -> getGraphData(request)));
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
