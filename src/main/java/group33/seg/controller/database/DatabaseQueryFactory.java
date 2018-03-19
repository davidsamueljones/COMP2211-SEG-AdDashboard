package group33.seg.controller.database;

import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import java.util.HashMap;
import java.util.Map;

public class DatabaseQueryFactory {
  private static final Map<Metric, String> graphQueries = new HashMap<>();
  private static final Map<Metric, String> statisticQueries = new HashMap<>();

  static {
    createGraphQueries();
    createStatisticTemplates();
  }

  /** Define and store templates for every graph metric type. */
  private static void createGraphQueries() {
    graphQueries.put(
        Metric.IMPRESSIONS,
        "SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS yaxis FROM impression_log GROUP BY xaxis;");

    graphQueries.put(
        Metric.CONVERSIONS,
        "SELECT date_trunc('<interval>', entry_date) AS xaxis, sum(conversion::int) AS yaxis FROM server_log GROUP BY xaxis;");

    graphQueries.put(
        Metric.TOTAL_COST,
        "SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS yaxis FROM impression_log GROUP BY xaxis;");

    graphQueries.put(
        Metric.CPA,
        new StringBuilder().append("SELECT conversions.xaxis AS xaxis, cost.yaxis / NULLIF(conversions.yaxis,0) AS yaxis")
        .append("FROM")
        .append("(SELECT date_trunc('<interval>', entry_date) AS xaxis, sum(conversion::int) AS yaxis FROM server_log GROUP BY xaxis) AS conversions")
        .append("INNER JOIN")
        .append("(SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS yaxis FROM impression_log GROUP BY xaxis) AS cost")
        .append("ON conversions.xaxis = cost.xaxis;").toString());
  }

  /** Define and store templates for every statistic metric type. */
  private static void createStatisticTemplates() {
    statisticQueries.put(
        Metric.IMPRESSIONS,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM impression_log;");

    statisticQueries.put(
        Metric.CONVERSIONS,
        "SELECT 'all' AS xaxis, sum(conversion::int) AS yaxis FROM server_log;");

    statisticQueries.put(
        Metric.TOTAL_COST,
        "SELECT 'all' AS xaxis, sum(impression_cost) AS yaxis FROM impression_log;");

    statisticQueries.put(
        Metric.CPA,
        new StringBuilder().append("SELECT 'all' AS xaxis, sum(impression_cost) / sum(conversion::int) AS yaxis")
        .append("FROM impression_log AS il")
        .append("LEFT JOIN server_log AS sl")
        .append("ON il.ctid = sl.ctid;").toString());
  }

  /**
   * Using a metric query request object, generate its corresponding SQL.
   *
   * @param request Query to generate SQL for
   * @return SQL generated from given MetricQuery
   */
  public static String generateSQL(MetricQuery request) {
    String sql;

    if (request.interval != null) {
      // Create graph query
      sql = graphQueries.get(request.metric);
      sql = applyGrouping(sql, request.interval);
    } else {
      // Create statistic query
      sql = statisticQueries.get(request.metric);
    }

    return sql;
  }

  /**
   * Helper function to modify template code to apply specific grouping.
   *
   * @param sql SQL to apply grouping to
   * @param interval Interval to apply
   * @return Modified SQL statement
   */
  private static String applyGrouping(String sql, Interval interval) {
    String groupingString;
    switch (interval) {
      case DAY:
        groupingString = "day";
        break;
      case HOUR:
        groupingString = "hour";
        break;
      case MONTH:
        groupingString = "month";
        break;
      case WEEK:
        groupingString = "week";
        break;
      case YEAR:
        groupingString = "year";
        break;
      default:
        return sql;
    }
    return sql.replace("<interval>", groupingString);
  }
}
