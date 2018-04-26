package group33.seg.model.configs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.lib.Pair;
import group33.seg.model.types.Metric;

public class WorkspaceConfig implements Serializable {
  private static final long serialVersionUID = 3581124779235508070L;

  /** Database settings workspace utilises */
  public DatabaseConfig database; 
  
  /** Campaign configurations currently loaded in workspace */
  public List<CampaignConfig> campaigns = new ArrayList<>();

  /** Currently loaded graph */
  public GraphConfig graph = null;
  
  /** Graph configurations currently stored in workspace */
  public List<GraphConfig> graphs = new ArrayList<>();

  /** Statistics currently stored in workspace (with a set of cached values) */
  public List<Pair<StatisticConfig, Map<Metric, Double>>> statistics = new ArrayList<>();
  
}
