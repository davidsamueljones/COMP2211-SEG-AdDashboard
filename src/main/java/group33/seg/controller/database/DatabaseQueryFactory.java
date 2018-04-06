package group33.seg.controller.database;

import java.util.HashMap;
import java.util.Map;

import group33.seg.model.configs.BounceConfig;
import group33.seg.model.configs.FilterConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;

/**
 * This class stores all SQL queries for statistics and graphs data.
 * It makes use of placeholders to adjust user preferences, such as
 * filters (date range, context, income, age, gender) and campaign
 * which the user wants data for.
 * If the user does not have preferences, placeholders are changed to default settings.
 */
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
            + " (SELECT date_trunc('<interval>', entry_date) AS dates, count(*) AS yaxis FROM server_log WHERE <campaign> AND conversion GROUP BY dates) AS s"
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
            + " SELECT (sl.bounces::double precision) / NULLIF(cl.clicks, 0) * 100 FROM"
            + " (SELECT count(*) as bounces FROM server_log WHERE <bounce> AND <campaign> AND date_trunc('<interval>', entry_date) = xaxis) as sl,"
            + " (SELECT count(*) as clicks FROM click_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as cl) as yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per conversion over time
    graphQueries.put(
        Metric.CPA,
        "SELECT xaxis,"
            + " (SELECT CASE conversions WHEN 0 THEN 0 ELSE (icost + ccost) / conversions END FROM"
            + " (SELECT SUM(impression_cost) as icost FROM impression_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as il,"
            + " (SELECT SUM(click_cost) as ccost FROM click_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as cl,"
            + " (SELECT count(*) as conversions FROM server_log WHERE conversion AND <campaign> AND date_trunc('<interval>', entry_date) = xaxis) as iil) as yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per click over time
    graphQueries.put(
        Metric.CPC,
        "SELECT xaxis,"
            + " (SELECT CASE clicks WHEN 0 THEN 0 ELSE (icost + ccost) / clicks END FROM"
            + " (SELECT SUM(impression_cost) as icost FROM impression_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as il,"
            + " (SELECT SUM(click_cost) as ccost FROM click_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as cl,"
            + " (SELECT count(*) as clicks FROM click_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as iil) as yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', min(date)) AS start FROM click_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(date)) AS final FROM click_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per 1000 impressions over time
    graphQueries.put(
        Metric.CPM,
        "SELECT xaxis,"
            + " (SELECT (cost / impressions) * 1000 FROM"
            + " (SELECT SUM(impression_cost) as cost FROM impression_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as il,"
            + " (SELECT count(*) as impressions FROM impression_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as iil) as yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average number of clicks per impression over time
    graphQueries.put(
        Metric.CTR,
        "SELECT xaxis, ("
            + " SELECT (cl.clicks::double precision) / il.impressions FROM"
            + " (SELECT count(*) as clicks FROM click_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as cl,"
            + " (SELECT count(*) as impressions FROM impression_log WHERE <campaign> AND date_trunc('<interval>', date) = xaxis) as il) as yaxis"
            + " FROM"
            + " (SELECT date_trunc('<interval>', min(date)) AS start FROM impression_log WHERE <campaign>) AS min,"
            + " (SELECT date_trunc('<interval>', max(date)) AS final FROM impression_log WHERE <campaign>) AS max,"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");
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
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM server_log WHERE conversion AND <campaign>;");

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

    // Total number of bounces - the <bounce> placeholder handles the 2 type of bounce definition and specific values for them
    statisticQueries.put(
        Metric.BOUNCES,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM server_log WHERE <bounce> AND <campaign>;");

    // Bounce rate - the <bounce> placeholder handles the 2 type of bounce definition and specific values for them
    statisticQueries.put(
        Metric.BOUNCE_RATE,
        "SELECT 'all' AS xaxis, bounces / NULLIF(clicks, 0) * 100 AS yaxis FROM"
            + " (SELECT count(*) AS bounces FROM server_log WHERE <bounce> AND <campaign>) AS sl,"
            + " (SELECT count(*)::double precision AS clicks FROM click_log WHERE <campaign>) AS cl;");

    // Average amount of money spent on a campaign for each conversion (CPA)
    statisticQueries.put(
        Metric.CPA,
        "SELECT 'all' AS xaxis, (il.cost + cl.cost) / conversions AS yaxis FROM "
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE <campaign>) AS il,"
            + " (SELECT sum(click_cost) AS cost FROM click_log WHERE <campaign>) AS cl,"
            + " (SELECT count(*) AS conversions FROM server_log WHERE conversion AND <campaign>) AS sl;");

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
        "SELECT 'all' AS xaxis, (il.cost / impressions) * 1000 AS yaxis FROM"
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE <campaign>) AS il,"
            + " (SELECT count(*) AS impressions FROM impression_log WHERE <campaign>) AS iil;");

    // The average amount of clicks per impression (CTR)
    statisticQueries.put(
        Metric.CTR,
        "SELECT 'all' AS xaxis, (clicks::double precision) / impressions AS yaxis FROM"
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

      // Create  graph query and apply grouping
      sql = graphQueries.get(request.metric);
      sql = applyGrouping(sql, request.interval);
    } else {
      // Create statistic query
      sql = statisticQueries.get(request.metric);
    }

    // Adjust <bounce> placeholder according to bounce definition preference
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

    // Adjust <campaign> placeholder according to the campaign the user wants to see data for
    if (request.campaign != null) {
      sql = sql.replace("<campaign>", "campaign_id = " + request.campaign.uid);
    }

    if (request.filter != null) {
      // Apply date range query (if provided by user)
      if (request.filter.dates != null) {
        if (request.filter.dates.min != null) {
          sql = sql.replace("<start>", "'" + request.filter.dates.min + "'");
        }
        if (request.filter.dates.max != null) {
          sql = sql.replace("<final>", "'" + request.filter.dates.max + "'");
        }
      }

      //FIXME these should be fixed...
      if(request.filter.ages != null) {
        //Apply age filter (if provided by user)
        sql = sql.replace("<filterAge>", "AND age = '" + request.filter.ages + "'");
      }

      //FIXME should be slightly different
      if(request.filter.contexts != null) {
        //Apply context filter (if provided by user)
        sql = sql.replace("<filterContext>", "AND context = '" + request.filter.contexts + "'");
      }

      //FIXME should be slightly different
      if(request.filter.incomes != null) {
        //Apply income filter (if provided by user)
        sql = sql.replace("<filter>", "AND income = '" + request.filter.incomes + "'");
      }

      //FIXME should be slightly different
      if(request.filter.genders != null) {
        //Apply gender filter (if provided by user)
        if(request.filter.genders.contains(FilterConfig.Gender.FEMALE))
        sql = sql.replace("<filter>", "AND female = 'true'");
        if(request.filter.genders.contains(FilterConfig.Gender.MALE))
        sql = sql.replace("<filter>", "AND female = 'false'");
      }
    }

    // Apply default settings, if null
    sql = applyDefaultReplacements(sql);

    return sql;
  }

  /**
   * Apply default filtering, if not specified by the user If there is no campaignID specified,
   * fetch all the data If there is no date range filter applied, then use the min and max date from
   * the 'date' column in the table
   *
   * @param sql SQL statement with placeholders to be adjusted
   * @return Modified SQL statement
   */
  private static String applyDefaultReplacements(String sql) {
    sql =
        sql.replace("<campaign>", "1 = 1")
                .replace("<start>", "start")
                .replace("<final>", "final")
                .replace("<filterAge>", "")
                .replace("<filterContext>", "")
                .replace("<filterIncome>", "")
                .replace("<filterGender>", "");
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
