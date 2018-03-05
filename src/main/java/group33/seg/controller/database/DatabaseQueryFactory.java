package group33.seg.controller.database;

import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;
import java.util.HashMap;

public class DatabaseQueryFactory {
  private HashMap<Metric, String> metricQueries = new HashMap<>();
  private HashMap<Metric, String> statisticQueries = new HashMap<>();

  public DatabaseQueryFactory() {
    metricQueries = new HashMap<>();
    statisticQueries = new HashMap<>();

    // add sql statistic templates
    statisticQueries.put(
        Metric.IMPRESSIONS,
        "SELECT 'all' as xaxis, count(*) as yaxis from impression_log group by xaxis;");

    // add sql metric templates
    metricQueries.put(
        Metric.IMPRESSIONS,
        "SELECT date_trunc('<interval>', date) as xaxis, count(*) as yaxis from impression_log group by xaxis;");
  }

  /**
   * @param request MetricQuery request object
   * @return SQL String representing given MetricQuery
   */
  public String generateSql(MetricQuery request) {
    String sql;

    if (request.getInterval() != null) {
      sql = metricQueries.get(request.getMetric());
      sql = sql.replace("<interval>", request.getInterval().getValue());
    } else {
      sql = statisticQueries.get(request.getMetric());
    }

    // todo add filters
    return sql;
  }
}
