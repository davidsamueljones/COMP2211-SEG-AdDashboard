package group33.seg.controller.database;

import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;
import java.util.HashMap;

public class DatabaseQueryFactory {
  private HashMap<Metric, String> metricQueries = new HashMap<Metric, String>();

  public DatabaseQueryFactory() {
    metricQueries = new HashMap<>();

    // add sql templates
    metricQueries.put(
        Metric.IMPRESSIONS,
        "SELECT date_trunc('<interval>', date) as xaxis, count(*) as yaxis from impression_log group by xaxis;");
  }

  public String generateSql(MetricQuery request) {
    String sql = metricQueries.get(request.getMetric());

    if (request.getInterval() != null) {
      sql = sql.replace("<interval>", request.getInterval().getValue());
    } else {
      sql = sql.replace("<interval>", "millennium");
    }

    // todo add filters
    return sql;
  }
}
