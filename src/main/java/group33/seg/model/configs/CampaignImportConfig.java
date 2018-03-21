package group33.seg.model.configs;

import java.nio.file.Files;
import java.nio.file.Paths;
import group33.seg.controller.utilities.ErrorBuilder;

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

  /**
   * Do local validation of configuration. For import configurations this
   * includes validation of import paths.
   * 
   * @return Any issues with validation
   */
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