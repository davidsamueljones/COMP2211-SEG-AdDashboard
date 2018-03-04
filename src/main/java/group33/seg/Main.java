package group33.seg;

import java.sql.SQLException;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;

public class Main {
  public static void main(String[] args) throws SQLException {
    DatabaseConfig config = new DatabaseConfig("config.properties");
    DatabaseConnection dbconn =
        new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
    dbconn.connectDatabase();

  }
}


