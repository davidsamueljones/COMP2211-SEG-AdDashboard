package group33.seg.controller.handlers;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.rits.cloning.Cloner;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.controller.utilities.ProgressListener;
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

  /** Listeners to alert about progress */
  private final Set<ProgressListener> progressListeners = new HashSet<>();

  /**
   * Instantiate a statistic handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public StatisticHandler(DashboardMVC mvc) {
    this.mvc = mvc;
    // Print messages to stdout for debug
    addProgressListener(new ProgressListener() {
      @Override
      public void progressUpdate(String update) {
        System.out.println(update);
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
    this.statistics = null;
    if (view != null) {
      EventQueue.invokeLater(() -> view.clearStatistics());
    }
  }

  /**
   * Using the list of existing statistics, update or add a set of statistics to the campaign
   * statistics view.
   * 
   * @param statistics Statistics to load
   */
  public void loadStatistics(List<StatisticConfig> inpStatistics) {
    // Do load on worker thread, updating progress listeners appropriately
    Thread workerThread = new Thread(() -> {
      try {
        updateProgress("Loading statistics into view...");
        alertStart();

        if (view != null) {
          // Create a copy of the input statistics, this allows any changes to the original passed
          // object to be handled by the handler's update structure appropriately on a load
          Cloner cloner = new Cloner();
          List<StatisticConfig> statistics = cloner.deepClone(inpStatistics);

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
          StatisticHandler.this.statistics = statistics;
        }
        updateProgress("Finished loading statistics into view");
      } finally {
        alertFinished();
      }
    });

    workerThread.start();
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
    EventQueue.invokeLater(() -> view.addStatistic(statistic));

    // Do required view updates
    if (update != Update.NOTHING) {
      updateProgress(
          String.format("Updating properties of statistic '%s'...", statistic.identifier));
      EventQueue.invokeLater(() -> view.setStatisticProperties(statistic));
    }
    if (update == Update.FULL || update == Update.DATA) {
      updateProgress(String.format("Updating data of statistic '%s'...", statistic.identifier));
      Map<Metric, Double> results = doStatisticQuery(statistic);
      EventQueue.invokeLater(() -> view.setStatisticData(statistic, results));
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
      updateProgress(
          String.format("Updating '%s' data of statistic '%s'...", metric, statistic.identifier));
      query.metric = metric;
      MetricQueryResponse res = mvc.controller.database.getQueryResponse(query);
      // Only acknowledge results that are as expected
      Number value = 0;
      if (res.getResult() != null && res.getResult().size() == 1) {
        if ((value = res.getResult().get(0).value) == null) {
          value = 0;
        }
      }
      results.put(metric, value.doubleValue());
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
    updateProgress(String.format("Removing statistic '%s'...", statistic.identifier));
    EventQueue.invokeLater(() -> view.removeStatistic(statistic));
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
   * Helper function to alert all listeners that a load has started.
   */
  private void alertStart() {
    for (ProgressListener listener : progressListeners) {
      listener.start();
    }
  }

  /**
   * Helper function to alert all listeners that a load finished.
   */
  private void alertFinished() {
    for (ProgressListener listener : progressListeners) {
      listener.finish(true);
    }
  }

  /**
   * Helper function to alert all listeners of a progress update.
   *
   * @param update Textual update on progress
   */
  private void updateProgress(String update) {
    for (ProgressListener listener : progressListeners) {
      listener.progressUpdate(update);
    }
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
