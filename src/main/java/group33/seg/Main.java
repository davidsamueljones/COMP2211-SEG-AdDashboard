package group33.seg;

import group33.seg.database.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConfig config = new DatabaseConfig("config.properties");
        DatabaseConnection dbconn = new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
        Connection conn = dbconn.connectDatabase();
    }
}


