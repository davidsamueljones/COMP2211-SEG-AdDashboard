package group33.seg.controller.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener.Type;
import group33.seg.lib.Pair;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.model.configs.WorkspaceConfig;
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
   * Set the campaign used by the workspace, alerting listeners that a campaign update has occurred.
   * 
   * @param campaign Campaign for workspace to use
   */
  public void setCampaign(CampaignConfig campaign) {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    workspace.campaign = campaign;
    notifyListeners(Type.CAMPAIGN);
  }

  /**
   * @return The current campaign used by the workspace
   */
  public CampaignConfig getCampaign() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    return workspace.campaign;
  }

  /**
   * @return The list of graphs stored in the workspace
   */
  public List<GraphConfig> getGraphs() {
    WorkspaceConfig workspace = mvc.model.getWorkspace();
    if (workspace != null) {
      return workspace.graphs;
    } else {
      return null;
    }
  }

  /**
   * Set the current graph for the workspace. Alert listeners that the current graph has changed.
   * There is no restriction that the current graph must belong to the workspace's graph list.
   * 
   * @param config Configuration to use as current graph
   */
  public void setCurrentGraph(GraphConfig config) {
    mvc.model.getWorkspace().graph = config;
    notifyListeners(Type.CURRENT_GRAPH);
  }

  /**
   * @return The workspace's current graph
   */
  public GraphConfig getCurrentGraph() {
    return mvc.model.getWorkspace().graph;
  }

  /**
   * Add a graph to the workspace graph list or if it already exists, update it. Alert listeners
   * that the graph list has changed.
   * 
   * @param graph Graph to place in workspace
   */
  public void putGraph(GraphConfig graph) {
    List<GraphConfig> graphs = getGraphs();

    int cur = graphs.indexOf(graph);
    if (cur >= 0) {
      graphs.set(cur, graph);
    } else {
      graphs.add(graph);
    }
    notifyListeners(Type.GRAPHS);
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
    Iterator<GraphConfig> itrGraphs = getGraphs().iterator();
    while (itrGraphs.hasNext()) {
      GraphConfig graph = itrGraphs.next();
      if (graph.equals(toRemove)) {
        itrGraphs.remove();
        removed = true;
        notifyListeners(Type.GRAPHS);
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
      WORKSPACE, CAMPAIGN, CURRENT_GRAPH, GRAPHS, STATISTICS;
    }

  }

}
