package group33.seg.controller.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener.Type;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.GraphVisitor;
import group33.seg.controller.utilities.SerializationUtils;
import group33.seg.lib.Pair;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.model.configs.WorkspaceConfig;
import group33.seg.model.configs.WorkspaceInstance;
import group33.seg.model.types.Metric;

public class WorkspaceHandler {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;


  /** Listeners for workspace changes */
  private List<WorkspaceListener> listeners = new ArrayList<>();

  /**
   * Instantiate a workspace handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public WorkspaceHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }


  /**
   * @return The current workspace
   */
  public WorkspaceInstance getWorkspaceInstance() {
    return mvc.model.getWorkspaceInstance();
  }


  /**
   * @return The current workspace name
   */
  public String getWorkspaceName() {
    WorkspaceInstance wsi = mvc.model.getWorkspaceInstance();
    return (wsi == null ? null : wsi.name);
  }

  /**
   * @return The current workspace directory
   */
  public String getWorkspaceDirectory() {
    WorkspaceInstance wsi = mvc.model.getWorkspaceInstance();
    return (wsi == null ? null : wsi.directory);
  }

  /**
   * Load a workspace from a given file. If the load is successful, the model is updated.
   * 
   * @param strPath Path to file to load
   * @return Error builder indicating if store was successful
   */
  public ErrorBuilder loadWorkspace(String strPath) {
    ErrorBuilder eb = new ErrorBuilder();
    Path path = Paths.get(strPath);
    if (!Files.exists(path)) {
      eb.addError("Workspace file does not exist");
    } else {
      FileInputStream fis = null;
      try {
        // Open file input stream using identifiers respective file
        fis = new FileInputStream(path.toString());
        // Get object from file input stream
        Object object = SerializationUtils.deserialize(fis);
        if (object instanceof WorkspaceConfig) {
          WorkspaceInstance wsi = new WorkspaceInstance(path);
          wsi.workspace = (WorkspaceConfig) object;
          setWorkspace(wsi);
        } else {
          eb.addError("Loaded file is not a workspace configuration");
        }
      } catch (IOException e) {
        eb.addError("Unable to load workspace file");
      } finally {
        try {
          if (fis != null) {
            fis.close();
          }
        } catch (IOException e) {
          // close failed, ignore
        }
      }
    }
    return eb;
  }

  /**
   * Update the model's workspace. Alerting listeners that the workspace has changed.
   * 
   * @param wsi New workspace to use
   */
  public void setWorkspace(WorkspaceInstance wsi) {
    mvc.model.setWorkspace(wsi);
    notifyListeners(Type.WORKSPACE);
  }

  /**
   * Store the currently handled workspace to its instance's defined location.
   * 
   * @param overwrite Whether to overwrite an existing file if it exists
   * @return Error builder indicating if store was successful
   */
  public ErrorBuilder storeCurrentWorkspace(boolean overwrite) {
    WorkspaceInstance wsi = mvc.model.getWorkspaceInstance();
    return storeWorkspace(wsi, overwrite);
  }

  /**
   * Store a workspace to its instance's defined location.
   * 
   * @param wsi Workspace to save
   * @param overwrite Whether to overwrite an existing file if it exists
   * @return Error builder indicating if store was successful
   */
  public static ErrorBuilder storeWorkspace(WorkspaceInstance wsi, boolean overwrite) {
    ErrorBuilder eb = new ErrorBuilder();
    if (wsi == null || wsi.workspace == null) {
      eb.addError("There is no workspace to store");
    } else {
      Path path = Paths.get(wsi.getWorkspaceFile().toURI());
      if (Files.exists(path) && !overwrite) {
        eb.addError("Workspace already exists in this location");
      } else {
        FileOutputStream fos = null;
        try {
          Files.createDirectories(path.getParent());
          fos = new FileOutputStream(path.toString());
          SerializationUtils.serialize(wsi.workspace, fos);
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
    }
    return eb;
  }

  /**
   * Save encrypted database config and clear password
   *
   * @param saveLocation file to save to
   * @param password     to use for encryption
   */
  public ErrorBuilder saveDatabaseConfig(String saveLocation, char[] password) {
    ErrorBuilder eb = new ErrorBuilder();

    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace != null) {
      DatabaseConfig databaseConfig = workspace.database;
      if (databaseConfig != null) {
        FileOutputStream fos = null;
        try {
          fos = new FileOutputStream(new File(saveLocation));
          SerializationUtils.serializeEncrypted(workspace, fos, password);
        } catch (IOException e) {
          eb.addError("File not available");
        } finally {
          try {
            if (fos != null) {
              fos.close();
            }
          } catch (IOException e) {
            // close failed, ignore
          }
        }
      } else {
        eb.addError("DatabaseConfig was null");
      }
    } else {
      eb.addError("WorkspaceConfig was null");
    }

    return eb;
  }

  /**
   * Using the currently loaded workspace, update the database handlers connections. Will close
   * connections if no valid database configuration exists.
   * 
   * @return Whether the update was successful
   */
  public boolean updateDatabaseConnections() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace != null && workspace.database != null) {
      DatabaseConfig config = workspace.database;
      try {
        mvc.controller.database.refreshConnections(config, 10);
        return true;
      } catch (SQLException e) {
        // falls through to return false
      }
    }
    mvc.controller.database.closeConnections();
    return false;
  }

  /**
   * @return The list of campaigns stored in the workspace
   */
  public List<CampaignConfig> getCampaigns() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    return workspace == null ? null : workspace.campaigns;
  }

  /**
   * Add a campaign to the workspace campaign list or if it already exists, update it. Alert
   * listeners that the campaign list has changed.
   * 
   * @param campaign Campaign to place in workspace
   */
  public void putCampaign(CampaignConfig campaign) {
    List<CampaignConfig> campaigns = getCampaigns();
    if (campaigns != null) {
      int cur = campaigns.indexOf(campaign);
      if (cur >= 0) {
        campaigns.set(cur, campaign);
      } else {
        campaigns.add(campaign);
      }
      notifyListeners(Type.CAMPAIGNS);
    }
  }

  /**
   * Replace a campaign in the workspace campaign list. This will also update any references in
   * workspace statistics/graphs that use this graph.
   * 
   * @param toReplace Campaign to replace in workspace (will be removed)
   * @param campaign Campaign to put in place
   * @return Whether campaign was replaced
   */
  public boolean replaceCampaign(CampaignConfig toReplace, CampaignConfig campaign) {
    List<CampaignConfig> campaigns = getCampaigns();
    if (campaigns != null) {
      int cur = campaigns.indexOf(toReplace);
      if (cur >= 0) {
        if (toReplace.equals(campaign) || !campaigns.contains(campaign)) {
          campaigns.set(cur, campaign);
        } else {
          campaigns.remove(cur);
        }
        notifyListeners(Type.CAMPAIGNS);
        replaceQueryCampaigns(toReplace, campaign);
        return true;
      }
    }
    return false;
  }

  /**
   * Replace the campaign used in queries with a different campaign.
   * 
   * @param toReplace Campaign to replace
   * @param campaign Campaign to put as replacement
   */
  private void replaceQueryCampaigns(CampaignConfig toReplace, CampaignConfig campaign) {
    // Replace campaign in graphs
    List<GraphConfig> graphs = getGraphs();
    for (GraphConfig graph : graphs) {
      graph.accept(new GraphVisitor() {

        @Override
        public void visit(HistogramConfig graph) {
          // TODO
        }

        @Override
        public void visit(LineGraphConfig graph) {
          for (LineConfig line : graph.lines) {
            if (line.query != null && line.query.campaign != null
                && line.query.campaign.equals(toReplace)) {
              line.query.campaign = campaign;
              if (graph.equals(getCurrentGraph())) {
                notifyListeners(Type.CURRENT_GRAPH);
              }
            }
          }
        }
      });
    }
    notifyListeners(Type.GRAPHS);

    // Replace campaign in statistics
    for (StatisticConfig statistic : getStatistics()) {
      if (statistic.query != null && statistic.query.campaign != null
          && statistic.query.campaign.equals(toReplace)) {
        statistic.query.campaign = campaign;
      }
    }
    notifyListeners(Type.STATISTICS);
  }

  /**
   * Remove the given campaign from the workspace campaigns. If a campaign is removed alert
   * listeners that the campaign list has changed.
   * 
   * @param toRemove Campaign to remove from workspace
   * @return Whether campaign was removed (if it existed)
   */
  public boolean removeCampaign(CampaignConfig toRemove) {
    boolean removed = false;
    List<CampaignConfig> campaigns = getCampaigns();
    if (campaigns != null) {
      Iterator<CampaignConfig> itrCampaigns = campaigns.iterator();
      while (itrCampaigns.hasNext()) {
        CampaignConfig campaign = itrCampaigns.next();
        if (campaign.equals(toRemove)) {
          itrCampaigns.remove();
          notifyListeners(Type.CAMPAIGNS);
          replaceQueryCampaigns(toRemove, null);
          removed = true;
        }
      }
    }
    return removed;
  }

  /**
   * @return The list of graphs stored in the workspace
   */
  public List<GraphConfig> getGraphs() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    return workspace == null ? null : workspace.graphs;
  }

  /**
   * Set the current graph for the workspace. Alert listeners that the current graph has changed.
   * There is no restriction that the current graph must belong to the workspace's graph list.
   * 
   * @param config Configuration to use as current graph
   */
  public void setCurrentGraph(GraphConfig config) {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace != null) {
      workspace.graph = config;
      notifyListeners(Type.CURRENT_GRAPH);
    }
  }

  /**
   * @return The workspace's current graph
   */
  public GraphConfig getCurrentGraph() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    return workspace == null ? null : workspace.graph;
  }

  /**
   * Add a graph to the workspace graph list or if it already exists, update it. Alert listeners
   * that the graph list has changed.
   * 
   * @param graph Graph to place in workspace
   */
  public void putGraph(GraphConfig graph) {
    List<GraphConfig> graphs = getGraphs();
    if (graphs != null) {
      int cur = graphs.indexOf(graph);
      if (cur >= 0) {
        graphs.set(cur, graph);
      } else {
        graphs.add(graph);
      }
      notifyListeners(Type.GRAPHS);
    }
  }

  /**
   * Remove the given graph from the workspace graphs. If a graph is removed alert listeners that
   * the graph list has changed.
   * 
   * @param toRemove Graph to remove from workspace
   * @return Whether graph was removed (if it existed)
   */
  public boolean removeGraph(GraphConfig toRemove) {
    boolean removed = false;
    List<GraphConfig> graphs = getGraphs();
    if (graphs != null) {
      Iterator<GraphConfig> itrGraphs = graphs.iterator();
      while (itrGraphs.hasNext()) {
        GraphConfig graph = itrGraphs.next();
        if (graph.equals(toRemove)) {
          itrGraphs.remove();
          removed = true;
          notifyListeners(Type.GRAPHS);
        }
      }
    }
    return removed;
  }

  /**
   * @return The list of statistics stored in the workspace (without caches)
   */
  public List<StatisticConfig> getStatistics() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace != null) {
      List<StatisticConfig> statistics = new ArrayList<>();
      for (Pair<StatisticConfig, Map<Metric, Double>> pair : workspace.statistics) {
        statistics.add(pair.key);
      }
      return statistics;
    } else {
      return null;
    }
  }

  /**
   * Add a statistic to the workspace statistic list with an empty cache. If statistic already
   * exists, update it, if it has changed, also clear the cache. Alert listeners that the statistic
   * list has changed.
   * 
   * @param statistic Statistic to place in workspace
   */
  public void putStatistic(StatisticConfig statistic) {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(statistic);
    Map<Metric, Double> cache = null;
    if (cur >= 0) {
      StatisticConfig exStatistic = workspace.statistics.get(cur).key;
      // Keep cache if query is unchanged
      if (exStatistic.query.isEquals(statistic.query)) {
        cache = workspace.statistics.get(cur).value;
      }
      workspace.statistics.set(cur, new Pair<>(statistic, cache));
    } else {
      workspace.statistics.add(new Pair<>(statistic, cache));
    }
    notifyListeners(Type.STATISTICS);
    return;
  }

  /**
   * Remove the given statistic from the workspace statistics. If a statistic is removed alert
   * listeners that the statistic list has changed.
   * 
   * @param toRemove Statistic to remove from workspace
   * @return Whether Statistic was removed (if it existed)
   */
  public boolean removeStatistic(StatisticConfig toRemove) {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(toRemove);
    if (cur >= 0) {
      workspace.statistics.remove(cur);
      notifyListeners(Type.STATISTICS);
      return true;
    }
    return false;
  }

  /**
   * For the given statistic, update its cache. Do not update the stored statistic, use
   * {@link #putStatistic(statistic)} first to do this. A cache is a mappings of metrics to the
   * cached values.
   * 
   * @param statistic Statistic to update cache for
   * @param cache Cache to store (can be null)
   * @return Whether the cache was updated (whether the statistic exists)
   */
  public boolean putCache(StatisticConfig statistic, Map<Metric, Double> cache) {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(statistic);
    if (cur >= 0) {
      StatisticConfig exStatistic = workspace.statistics.get(cur).key;
      workspace.statistics.set(cur, new Pair<>(exStatistic, cache));
      return true;
    }
    return false;
  }

  /**
   * Get the cache for a given statistic. A cache is a mappings of metrics to the cached values.
   * 
   * @param statistic Statistic to get cache for
   * @return Cache for given statistic, null if statistic not found (or if no cache)
   */
  public Map<Metric, Double> getCache(StatisticConfig statistic) {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(statistic);
    if (cur >= 0) {
      return workspace.statistics.get(cur).value;
    }
    return null;
  }

  /**
   * Clear all statistic caches.
   */
  public void clearStatisticCaches() {
    for (StatisticConfig statistic : getStatistics()) {
      putCache(statistic, null);
    }
  }

  /**
   * @param listener New listener to listen to workspace state updates.
   */
  public void addListener(WorkspaceListener listener) {
    listeners.add(listener);
  }

  /**
   * @param listener Listener to stop listening for workspace state updates.
   */
  public void removeListener(WorkspaceListener listener) {
    listeners.remove(listener);
  }


  /**
   * Notify all listeners that a workspace state update has occurred.
   * 
   * @param type Type of state update
   */
  public void notifyListeners(Type type) {
    for (WorkspaceListener listener : listeners) {
      listener.update(type);
    }
  }

  /**
   * Interface for handling updates from the workspace handler.
   */
  public interface WorkspaceListener {

    /**
     * Called by the workspace handler if its state is updated.
     * 
     * @param type The type of state update
     */
    public void update(Type type);

    /**
     * Enumeration of types of workspace state update.
     */
    public enum Type {
      WORKSPACE, CURRENT_GRAPH, CAMPAIGNS, GRAPHS, STATISTICS;
    }

  }

}
