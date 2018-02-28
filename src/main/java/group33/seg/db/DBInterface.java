package group33.seg.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DBInterface {
    private ConcurrentHashMap<Connection,Boolean> connections;
    private ConcurrentHashMap<DBRequest, LinkedHashMap<String, Integer>> previous;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    public DBInterface () {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        previous = new ConcurrentHashMap<DBRequest, LinkedHashMap<String, Integer>>();
        connections = new ConcurrentHashMap<Connection, Boolean>();
    }

    private synchronized Connection getConnection() {
        for (Connection c : connections.keySet()) {
            //true if available
            if(connections.get(c)) {
                connections.put(c, false);
                return c;
            }
        }

        Connection connection = null;

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.properties"));
        
            String host = prop.getProperty("DB_HOST");
            String user = prop.getProperty("DB_USER");
            String password = prop.getProperty("DB_PASSWORD");

            connection = DriverManager.getConnection(host, user, password);

            connections.put(connection, false);
            return connection;
        } catch (SQLException | IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void returnConnection(Connection c) {
        connections.put(c, true);
    }

    public Future<LinkedHashMap<String, Integer>> call(DBRequest request) {
        if (previous.get(request) != null) {
            return pool.submit(new Callable<LinkedHashMap<String, Integer>>() {
                public LinkedHashMap<String, Integer> call(){
                    return previous.get(request);
                }
            });
        } else {
            return pool.submit(new Callable<LinkedHashMap<String, Integer>>() {
                public LinkedHashMap<String, Integer> call(){
                    return getNewHistogram(request);
                }
            });
        }
    }

    private LinkedHashMap<String, Integer> getNewHistogram (DBRequest request) {
            Connection connection = getConnection();
            
            String sql = request.getSql();
            LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();

            try{
                Statement cs = connection.createStatement();
                ResultSet rs = cs.executeQuery(sql);
                
                while (rs.next()) {
                    result.put(rs.getString("xaxis"), rs.getInt("yaxis"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            returnConnection(connection);

            request.fixResult(result);

            previous.put(request, result);
            return result;
    }
}