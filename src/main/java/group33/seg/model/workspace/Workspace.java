package group33.seg.model.workspace;

import java.util.ArrayList;
import java.util.List;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.StatisticConfig;

public class Workspace {
  public final String name;
  public final String path;

  /** Campaigns currently managed by workspace */
  public final List<CampaignConfig> campaigns = new ArrayList<>();

  /** Graph configurations currently stored in workspace */
  public final List<GraphConfig> graphs = new ArrayList<>();

  /** Statistics currently stored in workspace */
  public final List<StatisticConfig> statistics = new ArrayList<>();

  /**
   * Initialise a workspace.
   * 
   * @param name Name (identifier) of workspace
   * @param path Path to configuration file for workspace
   */
  public Workspace(String name, String path) {
    this.name = name;
    this.path = path;
  }

}
