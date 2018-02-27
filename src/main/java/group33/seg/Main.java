package group33.seg;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        connectDatabase();
    }

    public static Connection connectDatabase() {
        Connection c = null;

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
            c = DriverManager.getConnection(host, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("Database loaded successfully");
        return c;
    }
}
