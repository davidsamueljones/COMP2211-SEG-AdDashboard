package group33.seg.controller.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.SerializationUtils;
import group33.seg.lib.Pair;
import group33.seg.model.configs.WorkspaceConfig;
import group33.seg.model.configs.WorkspaceInstance;

public class DatabaseConfig implements Serializable {
  private static final long serialVersionUID = -1279471354579637690L;

  private String host;
  private String user;
  private String password;

  private String reference;

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
    this.reference = null;
  }

  /**
   * Create an instance using information from a configuration to connect to a database.
   *
   * @param strPath Path to file containing authorisation information for db connection
   */
  public DatabaseConfig(String strPath) throws FileNotFoundException {
    // Verify that the input file exists and is not a directory
    boolean isValid = strPath != null;
    Path path = null;
    if (isValid) {
      path = Paths.get(strPath);
      isValid &= !Files.isDirectory(path);
      isValid &= Files.exists(path);
    }
    if (!isValid) {
      throw new FileNotFoundException("Configuration file does not exist");
    }

    Properties prop = new Properties();
    try {
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(path.toString());
        prop.load(fis);
      } finally {
        try {
          if (fis != null) {
            fis.close();
          }
        } catch (IOException e) {
          // close failed, ignore
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.host = prop.getProperty("DB_HOST");
    this.user = prop.getProperty("DB_USER");
    this.password = prop.getProperty("DB_PASSWORD");
    this.reference = strPath;
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

  /**
   * @return Configurations reference (likely a configuration file)
   */
  public String getReference() {
    return reference;
  }

  /**
   * Decrypt and get a database configuration.
   * 
   * @param saveLocation File to load
   * @param password Password to use for decryption
   * @return Pair of database config if loaded, and error builder maintained during load
   */
  public static Pair<DatabaseConfig, ErrorBuilder> loadDatabaseConfig(String saveLocation,
      char[] password) {
    ErrorBuilder eb = new ErrorBuilder();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(saveLocation);
      Object object = SerializationUtils.deserializeEncrypted(fis, password);
      if (object instanceof DatabaseConfig) {
        DatabaseConfig dbConfig = (DatabaseConfig) object;
        dbConfig.reference = saveLocation;
        return new Pair<>(dbConfig, eb);
      } else {
        eb.addError("Loaded file is not a database configuration");
      }
    } catch (FileNotFoundException e) {
      eb.addError("File does not exist or is a directory");
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
      } catch (IOException e) {
        // close failed, ignore
      }
    }
    return new Pair<>(null, eb);
  }

  /**
   * Encrypt and store a database configuration.
   *
   * @param config Database configuration to save
   * @param saveLocation File to save to
   * @param password Password to use for encryption
   * @param overwrite Whether to overwrite an existing file if it exists
   * @return Error builder indicating if store was successful
   */
  public static ErrorBuilder storeDatabaseConfig(DatabaseConfig config, String saveLocation,
      char[] password, boolean overwrite) {
    ErrorBuilder eb = new ErrorBuilder();
    if (Files.exists(Paths.get(saveLocation)) && !overwrite) {
      eb.addError("File already exists in this location");
    } else {
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(new File(saveLocation));
        SerializationUtils.serializeEncrypted(config, fos, password);
      } catch (IOException e) {
        eb.addError("Unable to store file to location");
      } finally {
        try {
          if (fos != null) {
            fos.close();
          }
        } catch (IOException e) {
          // close failed, ignore
        }
      }
    }
    return eb;
  }

}
