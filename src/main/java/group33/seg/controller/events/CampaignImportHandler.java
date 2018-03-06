package group33.seg.controller.events;

import java.util.Set;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.DatabaseTable;
import group33.seg.controller.database.tables.DatabaseTableImporter;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.ProgressListener;
import group33.seg.model.configs.CampaignConfig;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Controller class handling import of campaign data using a separate thread and the current
 * database connection. Use listening infrastructure to communicate any external updates.
 */
public class CampaignImportHandler {
  /** Thread safety lock for importedCampaign */
  private final Object lockImportedCampaign = new Object();

  /** Last campaign to be imported */
  private CampaignConfig importedCampaign = null;

  /** Current progress of import */
  private volatile int progress;

  /** Reference thread used for importing */
  private Thread threadImport;

  /** Listeners to alert about import progress */
  private final Set<ProgressListener> progressListeners = new HashSet<>();

  /** Error builder to store errors pertaining to the last import */
  private ErrorBuilder eb = new ErrorBuilder();

  /**
   * Import CSV files into tables using the current database connection. The imported data will
   * replace any data residing in the database. Progress listeners are kept updated on status of
   * import. As the import is handled on another thread, {@link cancelImport} must be used to
   * interrupt an ongoing import. Any errors are built up using the instances internal ErrorBuilder.
   * 
   * @param importConfig Campaign information required for import.
   * @return Whether import started successfully
   */
  public boolean doImport(CampaignImportConfig importConfig) {
    // Refresh error builder for this import, whilst validating config
    eb = importConfig.validate();

    // Error in configuration
    if (eb.isError()) {
      return false;
    }
    // Cannot start another import whilst another is ongoing
    if (isOngoing()) {
      eb.addError("An import is already ongoing");
      return false;
    }

    // Handle import on a separate thread
    threadImport = new Thread(new Runnable() {
      @Override
      public void run() {
        boolean finished = false; 
        
        // Alert listeners that import is starting
        alertStart();
        // Reset import progress
        updateProgress(0);

        // TODO: TEMPORARY - Use interface database connection
        DatabaseConfig config = new DatabaseConfig("config.properties");
        DatabaseConnection dbConn =
            new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword());
        dbConn.connectDatabase();
        Connection conn = dbConn.connectDatabase();

        // Create table objects
        ClickLogTable clickLogTable = new ClickLogTable();
        ImpressionLogTable impressionLogTable = new ImpressionLogTable();
        ServerLogTable serverLogTable = new ServerLogTable();
        
        // Remove existing tables data (TODO: Not to be kept)
        clickLogTable.clearTable(conn);
        impressionLogTable.clearTable(conn);
        serverLogTable.clearTable(conn);
        
        try {
          // Import click log
          importTable(clickLogTable, conn, importConfig.pathClickLog, 0.33);
          // Import impression log and ensure enums are set
          ImpressionLogTable.initEnums(conn);
          importTable(impressionLogTable, conn, importConfig.pathImpressionLog, 0.33);
          // Import server log
          importTable(serverLogTable, conn, importConfig.pathServerLog, 0.33);

          // Create campaign configuration (storing as last import)
          setImportedCampaign(new CampaignConfig(importConfig.campaignName));
          // Alert listeners that import is finished
          alertFinished(true);
          finished = true;
        } catch (InterruptedException e) {
          alertCancelled();
        } catch (ImportException e) {
          alertFinished(false);
        } 
        
        // Remove 'corrupted' data if import did not finish successfully
        if (!finished) {
          clickLogTable.clearTable(conn);
          impressionLogTable.clearTable(conn);
          serverLogTable.clearTable(conn);
        }
        threadImport = null;
      }
    });

    // Import can start successfully
    this.importedCampaign = null;
    threadImport.start();
    return true;
  }

  /**
   * Use a worker thread to import a given database type with the given file.
   * 
   * @param table Table type to import to
   * @param conn Database connection
   * @param path Path to file to import
   * @param weight Weighting for progress updates
   */
  private void importTable(DatabaseTable table, Connection conn, String path, double weight)
      throws InterruptedException {
    DatabaseTableImporter importer = new DatabaseTableImporter();
    final int curProgress = progress;
  
    // Ensure table is created 
    try {
      table.createTable(conn);
    } catch (SQLException e) {
      eb.addError("Database error, consult your administrator");
      throw new ImportException();
    }
    
    // Create worker thread handling import
    Thread worker = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          importer.importCSV(table, conn, path);
        } catch (Exception e) {
          e.printStackTrace();
          // do nothing, let main import thread handle
        }
      }
    });

    // Start worker
    worker.start();
    // Wait for worker to finish
    while (worker.isAlive()) {
      Thread.sleep(100);
      // Check for interrupts
      if (Thread.interrupted()) {
        worker.interrupt();
        worker.join(0);
        throw new InterruptedException();
      }
      // Update progress
      updateProgress(curProgress + (int) (importer.getProgress() * weight));
    }
    // Check if import was successful
    if (!importer.isSuccessful()) {
      eb.addError(String.format(
          "Error importing file '%s', check that the file is of the correct format", path));
      throw new ImportException();
    }
  }

  /**
   * Cancel an ongoing import (if one is ongoing). Let importer control how cancellation occurs.
   * 
   * @param wait Whether to halt the current thread until cancellation is finished
   * @return Whether import was cancelled
   */
  public boolean cancelImport(boolean wait) {
    if (isOngoing()) {
      threadImport.interrupt();
      if (wait) {
        try {
          threadImport.wait();
          return true;
        } catch (InterruptedException e) {
          return false;
        }
      }
    }
    return false;
  }


  /**
   * @return Whether an import is ongoing
   */
  public boolean isOngoing() {
    return (threadImport != null && threadImport.isAlive());
  }

  /**
   * @return Current progress level
   */
  public int getProgress() {
    return progress;
  }

  /**
   * Thread safe access to internal campaign.
   * 
   * @return Last imported campaign, null if none or unsuccessful
   */
  public CampaignConfig getImportedCampaign() {
    synchronized (lockImportedCampaign) {
      return importedCampaign;
    }
  }

  /**
   * Thread safe setting of internal imported campaign set.
   * 
   * @param importedCampaign Campaign to store
   */
  private void setImportedCampaign(CampaignConfig importedCampaign) {
    synchronized (lockImportedCampaign) {
      this.importedCampaign = importedCampaign;
    }
  }

  /**
   * @param progressListener Progress listener to start sending alerts to
   */
  public void addProgressListener(ProgressListener progressListener) {
    progressListeners.add(progressListener);
  }

  /**
   * @param progressListener Progress listener to no longer alert
   */
  public void removeProgressListener(ProgressListener progressListener) {
    progressListeners.remove(progressListener);
  }

  /**
   * Helper function to alert all listeners that an import has started.
   */
  private void alertStart() {
    for (ProgressListener listener : progressListeners) {
      listener.start();
    }
  }

  /**
   * Helper function to alert all listeners that an import has been cancelled.
   */
  private void alertCancelled() {
    for (ProgressListener listener : progressListeners) {
      listener.cancelled();
    }
  }

  /**
   * Helper function to alert all listeners that an import finished.
   * 
   * @param success Whether the import finished successfully
   */
  private void alertFinished(boolean success) {
    for (ProgressListener listener : progressListeners) {
      listener.finish(success);
    }
  }

  /**
   * Update progress value, notifying all listeners.
   * 
   * @param progress Current progress of import
   */
  private void updateProgress(int progress) {
    this.progress = progress;
    for (ProgressListener listener : progressListeners) {
      listener.progressUpdate(progress);
    }
  }

  /**
   * @return Error builder pertaining to last import
   */
  public ErrorBuilder getErrors() {
    return eb;
  }

  /**
   * Configuration used to configure a campaign import.
   */
  public class CampaignImportConfig {
    public final String campaignName;
    public final String pathClickLog;
    public final String pathImpressionLog;
    public final String pathServerLog;

    /**
     * Initialise a campaign import configuration.
     * 
     * @param campaignName Name for campaign
     * @param pathClickLog Path to click log csv
     * @param pathImpressionLog Path to impression log csv
     * @param pathServerLog Path to server log csv
     */
    public CampaignImportConfig(String campaignName, String pathClickLog, String pathImpressionLog,
        String pathServerLog) {
      this.campaignName = campaignName;
      this.pathClickLog = pathClickLog;
      this.pathImpressionLog = pathImpressionLog;
      this.pathServerLog = pathServerLog;
    }


    public ErrorBuilder validate() {
      ErrorBuilder eb = new ErrorBuilder();
      if (campaignName.isEmpty()) {
        eb.addError("Campaign name is empty");
      }
      if (!Files.exists(Paths.get(pathClickLog))) {
        eb.addError("Click log path does not exist");
      }
      if (!Files.exists(Paths.get(pathImpressionLog))) {
        eb.addError("Impression log path does not exist");
      }
      if (!Files.exists(Paths.get(pathServerLog))) {
        eb.addError("Server log path does not exist");
      }
      return eb;
    }
  }
  
  /**
   * Unchecked exception for importing failures
   */
  public class ImportException extends RuntimeException {
    private static final long serialVersionUID = -3767480036135704125L;
  }

}
