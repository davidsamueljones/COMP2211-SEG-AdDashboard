package group33.seg.controller.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConfig {

  private String host;
  private String user;
  private String password;

  public DatabaseConfig(String file) throws FileNotFoundException {
    if (file == null || !Files.exists(Paths.get(file))) {
      throw new FileNotFoundException("Configuration file doesn't exist!");
    }
    
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.host = prop.getProperty("DB_HOST");
    this.user = prop.getProperty("DB_USER");
    this.password = prop.getProperty("DB_PASSWORD");
  }

  public String getHost() {
    return host;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }
}
