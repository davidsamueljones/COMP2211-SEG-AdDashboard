package group33.seg.controller.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private String host;
  private String user;
  private String password;

  public DatabaseConnection(String host, String user, String password) {
    this.host = host;
    this.user = user;
    this.password = password;
  }

  public Connection connectDatabase() {
    if (host == null || user == null) {
      throw new IllegalArgumentException("Host and user cannot be null!");
    }

    try {
      Class.forName("org.postgresql.Driver");
      return DriverManager.getConnection(host, user, password);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
