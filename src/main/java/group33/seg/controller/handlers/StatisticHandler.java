package group33.seg.controller.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rits.cloning.Cloner;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.model.types.Metric;
import group33.seg.view.output.StatisticsView;

public class StatisticHandler {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Graph being controlled */
  protected StatisticsView view;

  /** Currently loaded statistics */
  protected List<StatisticConfig> statistics = null;

  /**
   * Instantiate a statistic handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public StatisticHandler(DashboardMVC mvc) {
    this.mvc = mvc;
    // Update the view whenever there are changes in the workspace
    mvc.controller.workspace.addListener(new WorkspaceListener() {
      @Override
      public void update(Type type) {
        if (type == WorkspaceListener.Type.WORKSPACE || type == WorkspaceListener.Type.STATISTICS) {
          loadStatistics(mvc.controller.workspace.getStatistics());
        }
      }
    });
  }

  /**
   * Allow external setting of which view is controlled.
   * 
   * @param view View to control
   * @param reload Whether to do a full reload of the currently stored statistics
   */
  public void setView(StatisticsView view, boolean reload) {
    this.view = view;
    if (reload) {
      List<StatisticConfig> statistics = this.statistics;
      this.statistics = null;
      loadStatistics(statistics);
    }
  }

  /**
   * Reloads the statistics using the current handler.
   */
  public void reloadStatistics() {
    loadStatistics(this.statistics);
  }

  /**
   * Remove any statistics from the handled view.
   */
  public void clearStatistics() {
    System.out.println("CLEARING STATISTICS");
    this.statistics = null;
    if (view != null) {
      view.clearStatistics();
    }
  }

  /**
   * Using the list of existing statistics, update or add a set of statistics to the campaign
   * statistics view.
   * 
   * @param statistics Statistics to load
   */
  public void loadStatistics(List<StatisticConfig> statistics) {
    if (view == null) {
      return;
    }

    // Create a copy of the input statistics, this allows any changes to the original passed object
    // to be handled by the handler's update structure appropriately on a load
    Cloner cloner = new Cloner();
    statistics = cloner.deepClone(statistics);

    // Configure statistic view
    removeOutdatedStatistics(statistics);
    if (statistics != null) {
      // Get list of existing statistics
      List<StatisticConfig> exStatistics = this.statistics;
      for (StatisticConfig statistic : statistics) {
        // Ensure campaign is current with workspace (FIXME: INCREMENT 2 FEATURE)
        statistic.query.campaign = mvc.controller.workspace.getCampaign();
        // Update statistic if it exists, otherwise add it
        int idx = (exStatistics == null ? -1 : exStatistics.indexOf(statistic));
        if (idx >= 0) {
          updateStatistic(statistic, getStatisticUpdate(exStatistics.get(idx), statistic));
        } else {
          addStatistic(statistic);
        }
      }
    }
    this.statistics = statistics;
  }

  /**
   * Using the list of existing statistics, remove those from the view that should no longer be
   * displayed based on provided updated statistic information.
   * 
   * @param statistics Updated statistic information
   */
  private void removeOutdatedStatistics(List<StatisticConfig> statistics) {
    // Get list of existing statistics
    List<StatisticConfig> exStatistics = this.statistics;
    if (exStatistics == null) {
      return;
    }
    // Remove existing statistics that no longer exist after update
    for (StatisticConfig exStatistic : exStatistics) {
      int idx = statistics.indexOf(exStatistic);
      if (idx < 0) {
        removeStatistic(exStatistic);
      }
    }
  }

  /**
   * Query data for the given configuration and add the statistic to the current view.
   * 
   * @param statistic Statistic configuration to use
   */
  private void addStatistic(StatisticConfig statistic) {
    updateStatistic(statistic, Update.FULL);
  }

  /**
   * Update a statistic in the view using the given update options. On a DATA update the statistic's
   * query must be executed to fetch up-to-date data first. On a PROPERTIES update only the view
   * must be updated.
   * 
   * @param statistic Statistic configuration to use
   * @param update Update options
   */
  private void updateStatistic(StatisticConfig statistic, Update update) {
    // Add statistic record in view if it doesn't exist
    view.addStatistic(statistic);

    // Do required view updates
    if (update != Update.NOTHING) {
      System.out.println("UPDATING PROPERTIES: " + statistic.identifier);
      view.setStatisticProperties(statistic);
    }
    if (update == Update.FULL || update == Update.DATA) {
      System.out.println("UPDATING DATA: " + statistic.identifier);
      Map<Metric, Double> results = doStatisticQuery(statistic);
      view.setStatisticData(statistic, results);
    }
  }

  /**
   * Do a metric query for every metric type using the statistic query configuration.
   * 
   * @param statistic Statistic configuration to use for query
   * @return Mapping of metrics to returned values
   */
  private Map<Metric, Double> doStatisticQuery(StatisticConfig statistic) {
    Map<Metric, Double> cache = mvc.controller.workspace.getCache(statistic);
    if (cache != null) {
      return cache;
    }
    // Not cached so query it
    Map<Metric, Double> results = new HashMap<>();
    MetricQuery query = statistic.query;
    for (Metric metric : Metric.values()) {
      query.metric = metric;
      MetricQueryResponse res = mvc.controller.database.getQueryResponse(query);
      // Only acknowledge results that are as expected
      Double value = null;
      if (res.getResult() != null && res.getResult().size() == 1) {
        if(res.getResult().get(0).value == null) {
          value = 0d;
        }
        else {
          value = res.getResult().get(0).value.doubleValue();
        }
      }
      results.put(metric, value);
    }
    mvc.controller.workspace.putCache(statistic, results);
    query.metric = null;
    return results;
  }

  /**
   * Remove a statistic from the current view.
   * 
   * @param statistic Statistic to remove
   */
  private void removeStatistic(StatisticConfig statistic) {
    System.out.println("REMOVING: " + statistic.identifier);
    view.removeStatistic(statistic);
  }

  /**
   * Given an original and an updated statistic configuration, determine what type of update is
   * required to update the statistic in the least cost-manner. As in, if only statistic properties
   * have changed it is best to not requery data due to computational cost.
   * 
   * @param original Statistic configuration to use as base
   * @param updated Statistic configuration to compare with base
   * @return Update type as represented by Update enumeration
   */
  private Update getStatisticUpdate(StatisticConfig original, StatisticConfig updated) {

    // Check for any changes in querying that may change data
    boolean data = true;
    data &= (original.query == null ? (original.query == null)
        : original.query.isEquals(updated.query));

    // Check for any changes of properties
    boolean properties = true;
    properties &= (original.identifier == null ? (original.identifier == null)
        : original.identifier.equals(updated.identifier));
    properties &= original.hide == updated.hide;

    // Determine update required
    if (!(data || properties)) {
      return Update.FULL;
    }
    if (!data) {
      return Update.DATA;
    }
    if (!properties) {
      return Update.PROPERTIES;
    }
    return Update.NOTHING;
  }

  /**
   * Enumeration of what type of update has occurred to a statistic.
   */
  public enum Update {
    FULL, /* Both properties and data have changed */
    PROPERTIES, /* Just properties have changed */
    DATA, /* Just data has changed */
    NOTHING; /* No changes */
  }

}
