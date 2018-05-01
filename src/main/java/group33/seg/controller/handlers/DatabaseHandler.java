package group33.seg.controller.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.database.DatabaseQueryFactory;
import group33.seg.controller.database.DatabaseQueryFactory.MalformedFilterException;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.controller.utilities.GraphVisitor;
import group33.seg.lib.Pair;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.model.configs.WorkspaceConfig;
import group33.seg.model.types.Metric;

public class DatabaseHandler {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Managed connection pool */
  private final Map<Connection, Boolean> connections = new HashMap<>();

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
   * @throws SQLException If a connection could not be created
   */
  public void refreshConnections(DatabaseConfig config, int count) throws SQLException {
    synchronized (connections) {
      // Close any existing connections
      closeConnections();
      connections.clear();
      // Open new connections with the given configuration
      DatabaseConnection connection =
          new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
      for (int i = 0; i < count; i++) {
        connections.put(connection.connectDatabase(), true);
      }
      connections.notify();
    }
  }

  /**
   * Close all connections currently in the pool, if a connection could not be closed it is ignored
   * but the return code will reflect this.
   * 
   * @return True if all connections are closed, otherwise false
   */
  public boolean closeConnections() {
    boolean allClosed = true;
    synchronized (connections) {
      // Close any existing connections
      for (Connection conn : connections.keySet()) {
        try {
          conn.close();
        } catch (SQLException e) {
          allClosed = false;
          System.err.println(
              "Unable to close connection to database, connection may have been left open");
        }
      }
    }
    return allClosed;
  }

  /**
   * Get an available database connection, if no database connection is available it will wait for a
   * new connection to be available. This call will not timeout and thus must be interrupted to
   * return if no connection gets freed.
   * 
   * @return Connection to database from pool
   */
  public Connection getConnection() {
    synchronized (connections) {
      while (true) {
        // Check to see if existing connections available
        for (Connection conn : connections.keySet()) {
          if (connections.get(conn)) {
            connections.put(conn, false);
            return conn;
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
   * @param conn Connection to return to pool
   */
  public void returnConnection(Connection conn) {
    synchronized (connections) {
      connections.put(conn, true);
      connections.notify();
    }
  }



  public MetricQueryResponse getQueryResponse(MetricQuery request) {
    return new MetricQueryResponse(request, pool.submit(() -> getGraphData(request)));
  }

  private List<Pair<String, Number>> getGraphData(MetricQuery request) {
    Connection conn = getConnection();

    List<Pair<String, Number>> result = new LinkedList<>();

    try {
      String sql = DatabaseQueryFactory.generateSQL(request);

      Statement cs = conn.createStatement();
      ResultSet rs = cs.executeQuery(sql);

      int i = 0;

      while (rs.next()) {
        try {
          result.add(new Pair<>(rs.getString("xaxis"), (Number) rs.getObject("yaxis")));
        } catch (SQLException e) {
          result.add(new Pair<>(i + "", (Number) rs.getObject("yaxis")));
          i++;
        }
      }
    } catch (MalformedFilterException | SQLException e) {
      e.printStackTrace();
    }
    returnConnection(conn);

    return result;
  }

  /**
   * Fetch data for a given statistic (pre-configured with a metric). Attempt to use the cache if
   * indicated otherwise query new data.
   * 
   * @param statistic Statistic to query with
   * @param cache Whether to use cache
   * @return Single statistic value, null if error
   */
  public Double doStatisticQuery(StatisticConfig statistic, boolean cache) {
    // If statistic is not valid, do not attempt to query
    if (statistic == null || statistic.validate().isError()) {
      return null;
    }
    // Generate query string
    String sql = null;
    try {
      sql = DatabaseQueryFactory.generateSQL(statistic.query);
    } catch (MalformedFilterException e) {
      e.printStackTrace();
    }
    // Check for caches
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    List<Pair<String, Number>> result = null;
    if (workspace != null && workspace.caches.containsKey(sql)) {
      result = workspace.caches.get(sql);
      cache = false;
    }
    if (result == null) {
      // No cache exists so query
      MetricQueryResponse response = mvc.controller.database.getQueryResponse(statistic.query);
      result = response.getResult();
    }
    // Acknowledge results that are as expected
    Number value = 0;
    if (result.size() == 1) {
      if ((value = result.get(0).value) == null) {
        value = 0;
      }
    }
    if (cache) {
      // Cache result in workspace
      workspace.caches.put(sql, result);
    }
    return value.doubleValue();
  }

  /**
   * Request that the database server cancels the given process as defined by its process ID (PID).
   * Default behaviour is a cancel request whereas forced behaviour is termination.
   * 
   * @param pid Process ID to cancel
   * @param force Whether to force cancellation
   */
  public void cancelProcessRequest(int pid, boolean force) {
    if (pid > 0) {
      try {
        // Create an independent server connection
        Connection conn = getConnection();
        // Request cancellation of current transaction
        PreparedStatement ps = conn.prepareStatement(String.format("SELECT %s(%d)",
            force ? "pg_terminate_backend" : "pg_cancel_backend", pid));
        ps.execute();
        returnConnection(conn);
      } catch (Exception e) {
        System.err.println("Unable to request process cancellation");
      }
    }
  }

  /**
   * Get the process ID of the next transaction for a given connection.
   * 
   * @param conn Connection to use
   * @return PID if connection is valid, otherwise -1
   */
  public static int getPID(Connection conn) {
    PreparedStatement ps;
    try {
      ps = conn.prepareStatement("SELECT pg_backend_pid()");
      ps.executeQuery();
      ResultSet rs = ps.getResultSet();
      rs.next();
        return rs.getInt(1);
    } catch (SQLException e) {
      return -1;
    }
  }

}
