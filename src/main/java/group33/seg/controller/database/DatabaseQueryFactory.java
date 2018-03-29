package group33.seg.controller.database;

import group33.seg.model.configs.BounceConfig;
import group33.seg.model.configs.FilterConfig;
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

  /**
   * Define and store templates for every graph metric type. generate_series is used to avoid
   * straight lines on graphs, when at a certain time the statistic value is 0.
   */
  private static void createGraphQueries() {

    // Total number of impressions over time
    graphQueries.put(
        Metric.IMPRESSIONS,
        "SELECT xaxis, s.yaxis FROM"
            + " (SELECT date_trunc('<interval>', min(date)) AS start FROM impression_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(date)) AS final FROM impression_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis"
            + " LEFT JOIN"
            + " (SELECT date_trunc('<interval>', date) AS dates, count(*) AS yaxis FROM impression_log WHERE <campaign> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Total number of conversions over time - conversions are calculated per entry_date
    graphQueries.put(
        Metric.CONVERSIONS,
        "SELECT xaxis, s.yaxis FROM"
            + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>,'1 <interval>') AS xaxis"
            + " LEFT JOIN"
            + " (SELECT date_trunc('<interval>', entry_date) AS dates, count(*) AS yaxis FROM server_log WHERE <campaign> AND conversion = TRUE GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Total number of clicks over time
    graphQueries.put(
        Metric.CLICKS,
        "SELECT xaxis, s.yaxis FROM"
            + " (SELECT date_trunc('<interval>', min(date)) AS start FROM click_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(date)) AS final FROM click_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis"
            + " LEFT JOIN"
            + " (SELECT date_trunc('<interval>', date) AS dates, count(*) AS yaxis FROM click_log WHERE <campaign> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Total cost over time
    graphQueries.put(
        Metric.TOTAL_COST,
        "SELECT xaxis, al.yaxis FROM"
            + " (SELECT date_trunc('<interval>', min(date)) AS start FROM impression_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(date)) AS final FROM impression_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
            + " (SELECT dates, SUM(cost) AS yaxis FROM"
            + " (SELECT date_trunc('<interval>', date) AS dates, impression_cost AS cost FROM impression_log WHERE <campaign>"
            + " UNION ALL"
            + " SELECT date_trunc('<interval>', date) AS dates, click_cost AS cost FROM click_log WHERE <campaign>) AS t"
            + " GROUP BY dates) AS al"
            + " ON xaxis = al.dates;");

    // Number of uniques over time
    graphQueries.put(
        Metric.UNIQUES,
        "SELECT xaxis, s.yaxis FROM"
            + " (SELECT date_trunc('<interval>', min(date)) AS start FROM click_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(date)) AS final FROM click_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
            + " (SELECT date_trunc('<interval>', date) AS dates, count(DISTINCT user_id) AS yaxis FROM click_log WHERE <campaign> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Number of bounces over time
    graphQueries.put(
        Metric.BOUNCES,
        "SELECT xaxis, s.yaxis FROM"
            + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
            + " (SELECT date_trunc('<interval>', entry_date) AS dates, count(*) AS yaxis FROM server_log WHERE <bounce> AND <campaign> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Bounces per click over time
    graphQueries.put(
        Metric.BOUNCE_RATE,
        "SELECT xaxis, ("
            + " SELECT sl.bounces::DECIMAL / cl.clicks FROM"
            + " (SELECT count(*) as bounces FROM server_log WHERE <bounce> AND <campaign> AND date_trunc('<interval>', entry_date) = xaxis) as sl,"
            + " (SELECT count(*) as clicks FROM click_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as cl) as yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE campaign_id = 3) AS min,"
            + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE campaign_id = 3) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // TODO: All below this line
    // Average amount of money spent per conversion over time
    graphQueries.put(
        Metric.CPA,
        "SELECT conversions.xaxis AS xaxis, (cost.impression_cost + cost.click_cost) / NULLIF(conversions.yaxis,0) AS yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', entry_date) AS xaxis, sum(conversion::int) AS yaxis FROM server_log GROUP BY xaxis) AS conversions"
            + " FULL OUTER JOIN"
            + " ((SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS impression_cost FROM impression_log GROUP BY xaxis) AS impression_cost"
            + " FULL OUTER JOIN"
            + " (SELECT date_trunc('<interval>', date) AS xaxis2, sum(click_cost) AS click_cost FROM click_log GROUP BY xaxis2) AS click_cost"
            + " ON impression_cost.xaxis = click_cost.xaxis2) AS cost"
            + " ON conversions.xaxis = cost.xaxis");

    // Average amount of money spent per click over time
    graphQueries.put(
        Metric.CPC,
        "SELECT date_trunc('<interval>', date) AS xaxis, avg(click_cost) AS yaxis FROM click_log GROUP BY xaxis ORDER BY xaxis;");

    // Average amount of money spent per 1000 impressions over time
    graphQueries.put(
        Metric.CPM,
        "SELECT impression_cost.xaxis AS xaxis, (click_cost.click_cost + impression_cost.impression_cost) / impressions * 1000 AS yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', date) AS xaxis, sum(impression_cost) AS impression_cost, count(*) AS impressions FROM impression_log GROUP BY xaxis) AS impression_cost"
            + " FULL OUTER JOIN"
            + " (SELECT date_trunc('<interval>', date) AS xaxis2, sum(click_cost) AS click_cost FROM click_log GROUP BY xaxis2) AS click_cost"
            + " ON impression_cost.xaxis = click_cost.xaxis2;");

    // Average number of clicks per impression over time
    graphQueries.put(
        Metric.CTR,
        "SELECT il.xaxis AS xaxis, (cl.count::float) / (il.count::float) AS yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS count FROM impression_log GROUP BY xaxis) AS il"
            + " LEFT JOIN"
            + " (SELECT date_trunc('<interval>', date) AS xaxis, count(*) AS count FROM click_log GROUP BY xaxis) AS cl"
            + " ON il.xaxis = cl.xaxis;");
  }

  /** Define and store templates for every statistic metric type. */
  private static void createStatisticTemplates() {

    // Total number of impressions
    statisticQueries.put(
        Metric.IMPRESSIONS,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM impression_log WHERE (<campaign>);");

    // Total number of clicks
    statisticQueries.put(
        Metric.CLICKS,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM click_log WHERE (<campaign>);");

    // Total number of conversions
    statisticQueries.put(
        Metric.CONVERSIONS,
        "SELECT 'all' AS xaxis, sum(conversion::int) AS yaxis FROM server_log WHERE <campaign>;");

    // Total cost - includes both click and impression cost
    statisticQueries.put(
        Metric.TOTAL_COST,
        "SELECT 'all' AS xaxis, il.cost + cl.cost AS yaxis FROM"
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE <campaign>) AS il,"
            + " (SELECT sum(click_cost) AS cost FROM click_log WHERE <campaign>) AS cl;");

    // Total number of uniques
    statisticQueries.put(
        Metric.UNIQUES,
        "SELECT 'all' AS xaxis, count(DISTINCT user_id) AS yaxis FROM click_log WHERE <campaign>;");

    // Total number of bounces - the <bounce> placeholder handles the 2 type of bounce definition
    // and specific values for them
    statisticQueries.put(
        Metric.BOUNCES,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM server_log WHERE <bounce> AND <campaign>;");

    // Bounce rate - the <bounce> placeholder handles the 2 type of bounce definition and specific
    // values for them
    statisticQueries.put(
        Metric.BOUNCE_RATE,
        "SELECT 'all' AS xaxis, bounces / clicks AS yaxis FROM"
            + " (SELECT count(*) AS bounces FROM server_log WHERE <bounce> AND <campaign>) AS sl,"
            + " (SELECT count(*)::DECIMAL AS clicks FROM click_log WHERE <campaign>) AS cl;");

    // Average amount of money spent on a campaign for each conversion (CPA)
    statisticQueries.put(
        Metric.CPA,
        "SELECT 'all' AS xaxis, (il.cost + cl.cost) / conversions AS yaxis FROM "
            + " (SELECT sum(impression_cost) as cost FROM impression_log WHERE <campaign>) as il,"
            + " (SELECT sum(click_cost) as cost FROM click_log WHERE <campaign>) as cl,"
            + " (SELECT count(*) as conversions FROM server_log WHERE conversion=true AND <campaign>) as sl;");

    // The average amount of money spent for each click (CPC)
    statisticQueries.put(
        Metric.CPC,
        "SELECT 'all' AS xaxis, (il.cost + cl.cost) / clicks AS yaxis FROM "
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE <campaign>) AS il,"
            + " (SELECT  sum(click_cost) AS cost FROM click_log WHERE <campaign>) AS cl,"
            + " (SELECT count(*) AS clicks FROM click_log WHERE <campaign>) AS ccl;");

    // The average amount of money spent per 1000 impressions (CPM)
    statisticQueries.put(
        Metric.CPM,
        "SELECT 'all' AS xaxis, (il.cost) / impressions * 1000 AS yaxis FROM"
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE <campaign>) as il,"
            + " (SELECT count(*) AS impressions FROM impression_log WHERE <campaign>) AS iil;");

    // The average amount of clicks per impression (CTR)
    statisticQueries.put(
        Metric.CTR,
        "SELECT 'all' AS xaxis, (clicks::DECIMAL) / impressions AS yaxis FROM"
            + " (SELECT count(*) AS clicks FROM click_log WHERE <campaign>) AS cl,"
            + " (SELECT count(*) AS impressions FROM impression_log WHERE <campaign>) AS il;");
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

    // Data for which campaign should be queried
    if (request.campaignConfig != null) {
      sql = sql.replace("<campaign>", "campaign_id = " + request.campaignConfig.uid);
    }

    // If no campaign_id is specified, fetch all data
    else {
      sql.replace("<campaign>", "1 = 1");
    }

    // TODO add rest of filtering options
    if (request.filterConfig != null) {
      /**
       * Checking if there is a chosen date range. If there is no date range set, the query will
       * default to using the minimum and maximum dates in the table
       */
      if (request.filterConfig.dates != null) {
        sql =
            sql.replace("<start>", "'" + request.filterConfig.dates.min + "'")
                .replace("<final>", "'" + request.filterConfig.dates.max + "'");
      } else sql = sql.replace("<start>", "start").replace("<final>", "final");
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
