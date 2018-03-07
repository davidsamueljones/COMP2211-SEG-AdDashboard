package group33.seg.controller.events;

import group33.seg.controller.database.Database;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;

public class StatisticHandler {

  public Integer impressionRequest() {
    Database db = new Database();
    MetricQuery queryImpressions = new MetricQuery(null, Metric.IMPRESSIONS, null, null);
    MetricQueryResponse resImpressions = db.getQueryResponse(queryImpressions);
    if (!resImpressions.getResult().isEmpty()) {
      return resImpressions.getResult().get(0).value;
    } else {
      return null;
    }
  }
}
