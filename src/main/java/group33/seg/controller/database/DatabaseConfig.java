package group33.seg.controller.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConfig {

  private String host;
  private String user;
  private String password;

  
  /**
   * Create an instance using local configuration information.
   * 
   * @param host Host to target
   * @param user Username to use to access target
   * @param password User's respective password (plaintext)
   */
  public DatabaseConfig(String host, String user, String password) {
    this.host = host;
    this.user = user;
    this.password = password;
  }
  
  /** 
   * Create an instance using information from a configuration to connect to a database.
   *
   * @param file File containing authorisation information for db connection
   */
  public DatabaseConfig(String file) throws FileNotFoundException {
    if (file == null || !Files.exists(Paths.get(file))) {
      throw new FileNotFoundException("Configuration file does not exist");
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

  /**
   * @return Configurations host
   */
  public String getHost() {
    return host;
  }

  /**
   * @return Configurations user
   */
  public String getUser() {
    return user;
  }

  /**
   * @return Configurations password
   */
  public String getPassword() {
    return password;
  }
  
}
