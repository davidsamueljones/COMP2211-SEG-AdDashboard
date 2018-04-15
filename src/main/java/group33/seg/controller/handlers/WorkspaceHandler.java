package group33.seg.controller.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener.Type;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;
import group33.seg.model.workspace.Workspace;

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

  public void setCampaign(CampaignConfig campaign) {
    Workspace workspace = mvc.model.getWorkspace();
    workspace.campaign = campaign;
    notifyListeners(Type.CAMPAIGN);
  }

  public CampaignConfig getCampaign() {
    Workspace workspace = mvc.model.getWorkspace();
    return workspace.campaign;
  }

  public void putGraph(GraphConfig graph) {
    List<GraphConfig> graphs = getGraphs();

    int cur = graphs.indexOf(graph);
    if (cur >= 0) {
      graphs.set(cur, graph);
    } else {
      graphs.add(graph);
    }
    notifyListeners(Type.GRAPHS);
    return;
  }

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

  public List<GraphConfig> getGraphs() {
    Workspace workspace = mvc.model.getWorkspace();
    if (workspace != null) {
      return workspace.graphs;
    } else {
      return null;
    }
  }

  public void addListener(WorkspaceListener listener) {
    listeners.add(listener);
  }

  public void notifyListeners(Type type) {
    for (WorkspaceListener listener : listeners) {
      listener.update(type);
    }
  }

  public interface WorkspaceListener {
    public void update(Type type);

    public enum Type {
      WORKSPACE, CAMPAIGN, GRAPHS, STATISTICS;
    }

  }

  public List<StatisticConfig> getStatistics() {
    Workspace workspace = mvc.model.getWorkspace();
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

  public void putStatistic(StatisticConfig statistic) {
    Workspace workspace = mvc.model.getWorkspace();
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

  public boolean removeStatistic(StatisticConfig toRemove) {
    Workspace workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(toRemove);
    if (cur >= 0) {
      workspace.statistics.remove(cur);
      notifyListeners(Type.STATISTICS);
      return true;
    }
    return false;
  }

  public boolean putCache(StatisticConfig statistic, Map<Metric, Double> cache) {
    Workspace workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(statistic);
    if (cur >= 0) {
      workspace.statistics.set(cur, new Pair<>(statistic, cache));
      return true;
    }
    return false;
  }
  
  public Map<Metric, Double> getCache(StatisticConfig statistic) {
    Workspace workspace = mvc.model.getWorkspace();
    int cur = getStatistics().indexOf(statistic);
    if (cur >= 0) {
      return workspace.statistics.get(cur).value;
    }
    return null;
  }



}
