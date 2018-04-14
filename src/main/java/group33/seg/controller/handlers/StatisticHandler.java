package group33.seg.controller.handlers;

import java.util.List;
import com.rits.cloning.Cloner;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.view.structure.WorkspaceStatisticsPanel;

public class StatisticHandler {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Graph being controlled */
  protected WorkspaceStatisticsPanel view;

  /** Currently loaded statistics */
  protected List<StatisticConfig> statistics = null;

  /**
   * Instantiate a statistic handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public StatisticHandler(DashboardMVC mvc) {
    this.mvc = mvc;
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
    // TODO: CLEAR VIEW
  }

  /**
   * Using the list of existing statistics, update or add a set of statistics to the campaign
   * statistics view.
   * 
   * @param statistics Statistics to load
   */
  public void loadStatistics(List<StatisticConfig> statistics) {
    // Create a copy of the input statistics, this allows any changes to the original passed object to
    // be handled by the handler's update structure appropriately on a load
    Cloner cloner = new Cloner();
    statistics = cloner.deepClone(statistics);
    
    // Configure statistic view
    removeOutdatedStatistics(statistics);
    loadStatistics(statistics);
    this.statistics = statistics;
  }


  /**
   * Using the list of existing statistics, remove those from the view that should no longer be displayed
   * based on provided updated statistic information.
   * 
   * @param statistics Updated statistic information
   */
  private void removeOutdatedStatistics(List<StatisticConfig> statistics) {
    // TODO: Handle removing old statistics
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
    // TODO: Handle update behaviour
  }

  /**
   * Remove a statistic from the current view.
   * 
   * @param statistic Statistic to remove
   */
  private void removeStatistic(StatisticConfig statistic) {
    System.out.println("REMOVING: " + statistic.identifier);
    // TODO: REMOVE STATISTIC
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
