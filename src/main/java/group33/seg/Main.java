package group33.seg;

import group33.seg.database.DatabaseConfig;
import group33.seg.database.DatabaseConnection;
import group33.seg.database.ServerLog;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConfig config = new DatabaseConfig("config.properties");
        DatabaseConnection conn = new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
        conn.connectDatabase();
    }
}


