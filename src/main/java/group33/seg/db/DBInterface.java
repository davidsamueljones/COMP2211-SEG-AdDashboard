package group33.seg.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DBInterface {
    private ConcurrentHashMap<Integer, HashMap<String, Integer>> previous;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    public DBInterface () {
    }

    public Future<HashMap<String, Integer>> call(DBRequest request) {
        if (previous.get(request.hashCode()) != null) {
            return pool.submit(new Callable<HashMap<String, Integer>>() {
                public HashMap<String, Integer> call(){
                    return previous.get(request.hashCode());
                }
            });
        } else {
            return pool.submit(new Callable<HashMap<String, Integer>>() {
                public HashMap<String, Integer> call(){
                    return getNewHistogram(request);
                }
            });
        }
    }

    private HashMap<String, Integer> getNewHistogram (DBRequest request) {
        Connection connection = null;

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        String host = prop.getProperty("DB_HOST");
        String user = prop.getProperty("DB_USER");
        String password = prop.getProperty("DB_PASSWORD");

        try {
            connection = DriverManager.getConnection(host, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        String sql = request.getSql();
        Statement cs = connection.createStatement();
        ResultSet rs = cs.executeQuery(sql);

        HashMap<String, Integer> result = new HashMap<String, Integer>();
        while (rs.next()) {
            result.put(rs.getString("xAxis"), rs.getInt("yAxis"));
        }

        return result;
    }
}