package group33.seg.controller.handlers;

import group33.seg.controller.DashboardController;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;
import group33.seg.view.output.Graph;

public class StatisticHandler {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;
  
  /**
   * Instantiate a statistic handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public StatisticHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }
  
  public Integer impressionRequest() {
    DatabaseHandler db = new DatabaseHandler(null); // TODO: REMOVE ASAP
    MetricQuery queryImpressions = new MetricQuery(null, Metric.IMPRESSIONS, null, null);
    MetricQueryResponse resImpressions = db.getQueryResponse(queryImpressions);
    if (!resImpressions.getResult().isEmpty()) {
      return resImpressions.getResult().get(0).value;
    } else {
      return null;
    }
  }
}
