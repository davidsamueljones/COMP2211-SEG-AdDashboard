package group33.seg.controller.database;

import group33.seg.model.configs.BounceConfig;
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

    // Total number of impressions over time
    graphQueries.put(
        Metric.IMPRESSIONS,
        "SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS yaxis FROM impression_log GROUP BY xaxis;");

    // Total number of conversions over time
    graphQueries.put(
        Metric.CONVERSIONS,
        "SELECT date_trunc('<interval>', entry_date) AS xaxis, sum(conversion::int) AS yaxis FROM server_log GROUP BY xaxis ORDER BY xaxis;");

    // Total number of clicks over time
    graphQueries.put(
        Metric.CLICKS,
        "SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS yaxis FROM click_log GROUP BY xaxis;");

    // Total cost over time
    graphQueries.put(
        Metric.TOTAL_COST,
        "SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS yaxis FROM impression_log GROUP BY xaxis;");

    // Number of uniques over time
    graphQueries.put(
        Metric.UNIQUES,
        "SELECT date_trunc('<interval>', date) AS xaxis, count(DISTINCT user_id) AS yaxis FROM click_log GROUP BY xaxis;");

    graphQueries.put(
            Metric.BOUNCES,
            "SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS yaxis FROM server_log WHERE (<bounce>);");

    // Bounces per click over time
    graphQueries.put(
            Metric.BOUNCE_RATE,
            new StringBuilder().append("SELECT bounces.xaxis AS xaxis, (bounces.bounce::float) / (clicks.click::float) AS yaxis")
            .append(" FROM")
            .append(" (SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS bounce FROM server_log WHERE (<bounce>)) AS bounces")
            .append(" FULL OUTER JOIN")
            .append(" (SELECT date_trunc('<interval>', date) AS xaxis2, count(*) AS click FROM click_log GROUP BY xaxis2) AS clicks")
            .append(" ON bounces.xaxis = clicks.xaxis2;").toString());
    
    // Average amount of money spent per conversion over time
    graphQueries.put(
        Metric.CPA,
        new StringBuilder()
            .append("SELECT conversions.xaxis AS xaxis, (cost.impression_cost + cost.click_cost) / NULLIF(conversions.yaxis,0) AS yaxis")
            .append(" FROM")
            .append(" (SELECT date_trunc('<interval>', entry_date) AS xaxis, sum(conversion::int) AS yaxis FROM server_log GROUP BY xaxis) AS conversions")
            .append(" FULL OUTER JOIN")
            .append(" ((SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS impression_cost FROM impression_log GROUP BY xaxis) AS impression_cost")
            .append(" FULL OUTER JOIN")
            .append(" (SELECT date_trunc('<interval>', date) AS xaxis2, sum(click_cost) AS click_cost FROM click_log GROUP BY xaxis2) AS click_cost")
            .append(" ON impression_cost.xaxis = click_cost.xaxis2) AS cost")
            .append(" ON conversions.xaxis = cost.xaxis")
            .toString());

    // Average amount of money spent per click over time
    graphQueries.put(
        Metric.CPC,
        "SELECT date_trunc('<interval>', date) AS xaxis, avg(click_cost) AS yaxis FROM click_log GROUP BY xaxis ORDER BY xaxis;");

    // Average amount of money spent per 1000 impressions over time
    graphQueries.put(
        Metric.CPM,
        new StringBuilder()
            .append("SELECT impression_cost.xaxis AS xaxis, (click_cost.click_cost + impression_cost.impression_cost) / impressions * 1000 AS yaxis")
            .append(" FROM")
            .append(" (SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS impression_cost, count(*) AS impressions FROM impression_log GROUP BY xaxis) AS impression_cost")
            .append(" FULL OUTER JOIN")
            .append(" (SELECT date_trunc('<interval>', date) AS xaxis2, sum(click_cost) AS click_cost FROM click_log GROUP BY xaxis2) AS click_cost")
            .append(" ON impression_cost.xaxis = click_cost.xaxis2;")
            .toString());

    // Average number of clicks per impression over time
    graphQueries.put(
        Metric.CTR,
        new StringBuilder()
            .append("SELECT il.xaxis AS xaxis, (cl.count::float) / (il.count::float) AS yaxis")
            .append(" FROM")
            .append(" (SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS count FROM impression_log GROUP BY xaxis) AS il")
            .append(" LEFT JOIN")
            .append(" (SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS count FROM click_log GROUP BY xaxis) AS cl")
            .append(" ON il.xaxis = cl.xaxis;")
            .toString());
  }

  /** Define and store templates for every statistic metric type. */
  private static void createStatisticTemplates() {

    // Total number of impressions
    statisticQueries.put(
        Metric.IMPRESSIONS, "SELECT 'all' AS xaxis, count(*) AS yaxis FROM impression_log;");

    // Total number of clicks
    statisticQueries.put(Metric.CLICKS, "SELECT 'all' AS xaxis, count(*) AS yaxis FROM click_log;");

    // Total number of conversions
    statisticQueries.put(
        Metric.CONVERSIONS,
        "SELECT 'all' AS xaxis, sum(conversion::int) AS yaxis FROM server_log;");

    // Total cost
    statisticQueries.put(
        Metric.TOTAL_COST,
        "SELECT 'all' AS xaxis, sum(impression_cost) AS yaxis FROM impression_log;");

    // Total number of uniques
    statisticQueries.put(
        Metric.UNIQUES, "SELECT 'all' AS xaxis, count(DISTINCT user_id) AS yaxis FROM click_log;");

    // Total number of bounces
    // The <bounce> placeholder handles the 2 type of bounce definition and specific values for them
    statisticQueries.put(
        Metric.BOUNCES,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM server_log WHERE (<bounce>);");

    //TODO add bounce rate query

    // Average amount of money spent on a campaign for each conversion
    statisticQueries.put(
        Metric.CPA,
        new StringBuilder()
            .append("SELECT 'all' AS xaxis, (sum(impression_cost) + sum(click_cost)) / sum(conversion::int) AS yaxis")
            .append("FROM impression_log AS il")
            .append("FULL OUTER JOIN")
            .append("click_log AS cl")
            .append("ON il.ctid = cl.ctid")
            .append("FULL OUTER JOIN")
            .append("server_log AS sl")
            .append("ON il.ctid = sl.ctid;")
            .toString());

    // The average amount of money spent for each click
    statisticQueries.put(
        Metric.CPC, "SELECT 'all' AS xaxis, avg(click_cost) AS yaxis FROM click_log;");


    // TODO Add CPM query
    // TODO add CTR query
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

      // Adjust placeholders according to bounce definition preference
      if (request.bounceDef != null) {
        if (request.bounceDef.type == BounceConfig.Type.TIME) {
          sql =
              sql.replace(
                  "<bounce>",
                  "EXTRACT(EPOCH FROM (exit_date - entry_date)) <= " + request.bounceDef.value);
        } else {
          sql = sql.replace("<bounce>", "pages_viewed <= " + request.bounceDef.value);
        }
      }
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
