package group33.seg.controller.campaignimport;

import java.util.Set;
import group33.seg.controller.utilities.ProgressListener;
import group33.seg.model.configs.CampaignConfig;
import java.util.HashSet;

/**
 * Controller class handling import of campaign data using a separate thread and the 
 * current database connection. Use listening infrastructure to communicate any external updates.
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

  /**
   * Import CSV files into tables using the current database connection. The imported data will
   * replace any data residing in the database. Progress listeners are kept updated on status of
   * import. As the import is handled on another thread, {@link}cancelImport must be used to
   * interrupt an ongoing import.
   * 
   * @param importConfig Campaign information required for import.
   * @return Whether import started successfully
   */
  public boolean doImport(CampaignImportConfig importConfig) {
    // Cannot start another import whilst another is ongoing
    if (isOngoing()) {
      return false;
    }
    
    // Handle import on a separate thread
    threadImport = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          // Alert listeners that import is starting
          alertStart();

          updateProgress(0);
          // do click log import
          Thread.sleep(1000);
          updateProgress(33);
          // do impression log import
          Thread.sleep(1000);
          updateProgress(66);
          // do server log import
          Thread.sleep(1000);
          updateProgress(99);
          Thread.sleep(1000);
          // drop old table (do first?)
          updateProgress(100);

          // Create campaign configuration (storing as last import)
          setImportedCampaign(new CampaignConfig(importConfig.campaignName));
          // Alert listeners that import is finished
          alertFinished(true);
        } catch (InterruptedException e) {
          // drop any half imported data
          alertCancelled();
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
  }

}
