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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseQueryFactory;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener.Type;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.GraphVisitor;
import group33.seg.controller.utilities.SerializationUtils;
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

  /** FIXME: Not production code */
  public static final String PRIVATE_KEY = "TEST_KEY";

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Listeners for workspace changes */
  private List<WorkspaceListener> listeners = new ArrayList<>();

  private boolean unstoredChanges = false;

  /**
   * Instantiate a workspace handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public WorkspaceHandler(DashboardMVC mvc) {
    this.mvc = mvc;

    addListener(type -> {
      if (type == Type.WORKSPACE) {
        unstoredChanges = false;
      } else {
        unstoredChanges = true;
      }
    });
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
   * Update the model's workspace. Alerting listeners that the workspace has changed.
   * 
   * @param wsi New workspace to use
   */
  public void setWorkspace(WorkspaceInstance wsi) {
    mvc.model.setWorkspace(wsi);
    notifyListeners(Type.WORKSPACE);
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
        Object object = SerializationUtils.deserializeEncrypted(fis,
            WorkspaceHandler.PRIVATE_KEY.toCharArray());
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
   * Store the currently handled workspace to its instance's defined location.
   * 
   * @param overwrite Whether to overwrite an existing file if it exists
   * @return Error builder indicating if store was successful
   */
  public ErrorBuilder storeCurrentWorkspace(boolean overwrite) {
    WorkspaceInstance wsi = mvc.model.getWorkspaceInstance();
    cleanCaches();
    ErrorBuilder eb = storeWorkspace(wsi, WorkspaceHandler.PRIVATE_KEY.toCharArray(), overwrite);
    unstoredChanges &= eb.isError();
    return eb;
  }

  /**
   * Store a workspace to its instance's defined location.
   * 
   * @param wsi Workspace to save
   * @param password Password to use for encryption
   * @param overwrite Whether to overwrite an existing file if it exists
   * @return Error builder indicating if store was successful
   */
  public static ErrorBuilder storeWorkspace(WorkspaceInstance wsi, char[] password,
      boolean overwrite) {
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
          SerializationUtils.serializeEncrypted(wsi.workspace, fos, password);
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
   * Encrypt and store the current databse configuration.
   * 
   * @param saveLocation file to save to
   * @param password Password to use for encryption
   * @param overwrite Whether to overwrite an existing file if it exists
   * @return Error builder indicating if store was successful
   */
  public ErrorBuilder storeDatabaseConfig(String saveLocation, char[] password, boolean overwrite) {
    ErrorBuilder eb = new ErrorBuilder();
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace == null) {
      eb.addError("No workspace loaded");
    } else {
      DatabaseConfig config = workspace.database;
      if (config == null) {
        eb.addError("No workspace database configuration set");
      } else {
        eb.append(DatabaseConfig.storeDatabaseConfig(config, saveLocation, password, overwrite));
      }
    }
    return eb;
  }

  /**
   * @return Whether 'store' has been called since changes
   */
  public boolean hasUnstoredChanges() {
    return unstoredChanges;
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
   * @return The list of statistics stored in the workspace
   */
  public List<StatisticConfig> getStatistics() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace != null) {
      return workspace.statistics;
    } else {
      return null;
    }
  }

  /**
   * Add a statistic to the workspace statistic list. If statistic already exists, update it. Alert
   * listeners that the statistic list has changed.
   * 
   * @param statistic Statistic to place in workspace
   */
  public void putStatistic(StatisticConfig statistic) {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(statistic);
    if (cur >= 0) {
      workspace.statistics.set(cur, statistic);
    } else {
      workspace.statistics.add(statistic);
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
   * Clear all caches.
   */
  public void clearCaches() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace != null) {
      workspace.caches = new HashMap<>();
    }
  }

  /**
   * Remove any cache not used by the workspace.
   */
  public void cleanCaches() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    Set<String> used = new HashSet<>();
    // Get list of queries used by graphs
    for (GraphConfig graph : workspace.graphs) {
      graph.accept(new GraphVisitor() {

        @Override
        public void visit(HistogramConfig graph) {
          // TODO Auto-generated method stub

        }

        @Override
        public void visit(LineGraphConfig graph) {
          for (LineConfig line : graph.lines) {
            used.add(DatabaseQueryFactory.generateSQL(line.query));
          }
        }
      });
    }
    // Get list of queries used by statistics
    for (StatisticConfig statistic : workspace.statistics) {
      for (Metric metric : Metric.getTypes()) {
        statistic.query.metric = metric;
        used.add(DatabaseQueryFactory.generateSQL(statistic.query));
      }
      statistic.query.metric = null;
    }

    // Remove any cache that is not used
    Iterator<String> itrCaches = workspace.caches.keySet().iterator();
    while (itrCaches.hasNext()) {
      String cache = itrCaches.next();
      if (!used.contains(cache)) {
        itrCaches.remove();
      }
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
