package group33.seg.controller.database;

import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import java.util.HashMap;

public class DatabaseQueryFactory {
  private HashMap<Metric, String> metricQueries = new HashMap<Metric, String>();
  private HashMap<Interval, String> intervalValues = new HashMap<Interval, String>();

  public DatabaseQueryFactory() {
    metricQueries = new HashMap<Metric, String>();
    intervalValues = new HashMap<Interval, String>();

    //add interval templates
    intervalValues.put(Interval.HOUR, "hour");
    intervalValues.put(Interval.DAY, "day");
    intervalValues.put(Interval.WEEK, "week");
    intervalValues.put(Interval.MONTH, "month");
    intervalValues.put(Interval.QUARTER, "quarter");
    intervalValues.put(Interval.YEAR, "year");

    //add sql templates
    metricQueries.put(Metric.IMPRESSIONS, "SELECT date_trunc('<interval>', date) as xaxis, count(*) as yaxis from impression_log group by xaxis;");
  }

  public String generateSql(MetricQuery request) {
    String sql = metricQueries.get(request.getMetric());

    if (request.getInterval() != null) {
      sql.replace("<interval>", intervalValues.get(request.getInterval()));
    } else {
      sql.replace("<interval>", "millennium");
    }

    //todo add filters

    return sql;
  }
}