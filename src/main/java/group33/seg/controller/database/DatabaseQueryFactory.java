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
        "SELECT date_trunc('<interval>', entry_date) AS xaxis, sum(conversion::int) AS yaxis FROM server_log GROUP BY xaxis ORDER BY xaxis;");

    graphQueries.put(
        Metric.TOTAL_COST,
        "SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS yaxis FROM impression_log GROUP BY xaxis;");

    graphQueries.put(
        Metric.CPA,
        new StringBuilder().append("SELECT conversions.xaxis AS xaxis, (cost.impression_cost + cost.click_cost) / NULLIF(conversions.yaxis,0) AS yaxis")
        .append(" FROM")
        .append(" (SELECT date_trunc('<interval>', entry_date) AS xaxis, sum(conversion::int) AS yaxis FROM server_log GROUP BY xaxis) AS conversions")
        .append(" FULL OUTER JOIN")
        .append(" ((SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS impression_cost FROM impression_log GROUP BY xaxis) AS impression_cost")
        .append(" FULL OUTER JOIN")
        .append(" (SELECT date_trunc('<interval>', date) AS xaxis2, sum(click_cost) AS click_cost FROM click_log GROUP BY xaxis2) AS click_cost")
        .append(" ON impression_cost.xaxis = click_cost.xaxis2) AS cost")
        .append(" ON conversions.xaxis = cost.xaxis").toString());

    graphQueries.put(
        Metric.CPC,
        "SELECT date_trunc('<interval>', date) AS xaxis, avg(click_cost) AS yaxis FROM click_log GROUP BY xaxis ORDER BY xaxis;");

    graphQueries.put(
        Metric.CPM,
        new StringBuilder().append("SELECT impression_cost.xaxis AS xaxis, (click_cost.click_cost + impression_cost.impression_cost) / impressions * 1000 AS yaxis")
        .append(" FROM")
        .append(" (SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS impression_cost, count(*) AS impressions FROM impression_log GROUP BY xaxis) AS impression_cost")
        .append(" FULL OUTER JOIN")
        .append(" (SELECT date_trunc('<interval>', date) AS xaxis2, sum(click_cost) AS click_cost FROM click_log GROUP BY xaxis2) AS click_cost")
        .append(" ON impression_cost.xaxis = click_cost.xaxis2;").toString());

    graphQueries.put(
        Metric.CLICKS,
        "SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS yaxis FROM click_log GROUP BY xaxis;");

    graphQueries.put(
        Metric.UNIQUES,
        "SELECT date_trunc('<interval>', date) AS xaxis, count(DISTINCT user_id) AS yaxis FROM click_log GROUP BY xaxis;");
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
        new StringBuilder().append("SELECT 'all' AS xaxis, (sum(impression_cost) + sum(click_cost)) / sum(conversion::int) AS yaxis")
        .append(" FROM impression_log AS il")
        .append(" LEFT JOIN")
        .append(" click_log AS cl")
        .append(" ON il.ctid = cl.ctid")
        .append(" LEFT JOIN")
        .append(" server_log AS sl")
        .append(" ON il.ctid = sl.ctid;").toString());

    statisticQueries.put(
        Metric.CPC,
        "SELECT 'all' AS xaxis, avg(click_cost) AS yaxis FROM click_log;");
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
