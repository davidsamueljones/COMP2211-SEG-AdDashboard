package group33.seg.controller.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import group33.seg.model.configs.BounceConfig;
import group33.seg.model.configs.FilterConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;

/**
 * This class stores all SQL queries for statistics and graphs data. It makes use of placeholders to
 * adjust user preferences, such as filters (date range, context, income, age, gender) and campaign
 * which the user wants data for. If the user does not have preferences, placeholders are changed to
 * default settings.
 */
public class DatabaseQueryFactory {
  private static final Map<Metric, String> graphQueries = new HashMap<>();
  private static final Map<Metric, String> graphRangeQueries = new HashMap<>();
  private static final Map<Metric, String> statisticQueries = new HashMap<>();
  private static final DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  static {
    createGraphRangeQueries();
    createGraphQueries();
    createStatisticTemplates();
  }

  /**
   * Define and store templates for every graph metric type when user-defined
   * date range filter is present. generate_series is used to avoid
   * straight lines on graphs, when at a certain time the statistic value is 0.
   */
  private static void createGraphRangeQueries() {

    // Total number of impressions over time
    graphRangeQueries.put(
        Metric.IMPRESSIONS,
        "SELECT xaxis, s.yaxis FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis"
            + " LEFT JOIN"
            + " (SELECT date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>)) AS dates, count(*) AS yaxis FROM impression_log WHERE date < <final> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Total number of conversions over time - conversions are calculated per entry_date
    graphRangeQueries.put(
        Metric.CONVERSIONS,
        "SELECT xaxis, s.yaxis FROM"
            + " generate_series(<start>, <final>,'1 <interval>') AS xaxis"
            + " LEFT JOIN"
            + " (SELECT date_trunc('<interval>', entry_date) + (<start> - date_trunc('<interval>', <start>)) AS dates, count(*) AS yaxis FROM <server_log> WHERE entry_date < <final> AND <campaign> AND conversion AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Total number of clicks over time
    graphRangeQueries.put(
        Metric.CLICKS,
        "SELECT xaxis, s.yaxis FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis"
            + " LEFT JOIN"
            + " (SELECT date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>)) AS dates, count(*) AS yaxis FROM <click_log> WHERE date < <final> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Total cost over time
    graphRangeQueries.put(
        Metric.TOTAL_COST,
        "SELECT xaxis, al.yaxis FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
            + " (SELECT dates, SUM(cost) / 100 AS yaxis FROM"
            + " (SELECT date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>)) AS dates, impression_cost AS cost FROM impression_log WHERE date < <final> AND <campaign> AND <filterAge> AND <filterIncome> AND <filterGender>"
            + " UNION ALL"
            + " SELECT date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>)) AS dates, click_cost AS cost FROM <click_log> WHERE date < <final> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS t"
            + " GROUP BY dates) AS al"
            + " ON xaxis = al.dates;");

    // Number of uniques over time
    graphRangeQueries.put(
        Metric.UNIQUES,
        "SELECT xaxis, s.yaxis FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
            + " (SELECT date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>)) AS dates, count(DISTINCT user_id) AS yaxis FROM <click_log> WHERE date < <final> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Number of bounces over time
    graphRangeQueries.put(
        Metric.BOUNCES,
        "SELECT xaxis, s.yaxis FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
            + " (SELECT date_trunc('<interval>', entry_date) + (<start> - date_trunc('<interval>', <start>)) AS dates, count(*) AS yaxis FROM <server_log> WHERE entry_date < <final> AND <bounce> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
            + " ON xaxis = s.dates;");

    // Bounces per click over time
    graphRangeQueries.put(
        Metric.BOUNCE_RATE,
        "SELECT xaxis, ("
            + " SELECT (sl.bounces::double precision) / NULLIF(cl.clicks, 0) * 100 FROM"
            + " (SELECT count(*) as bounces FROM <server_log> WHERE <bounce> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', entry_date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND entry_date < <final>) as sl,"
            + " (SELECT count(*) as clicks FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as cl) as yaxis"
            + " FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per conversion over time
    graphRangeQueries.put(
        Metric.CPA,
        "SELECT xaxis,"
            + " (SELECT CASE conversions WHEN 0 THEN 0 ELSE (icost + ccost) / 100 / conversions END FROM"
            + " (SELECT SUM(impression_cost) as icost FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as il,"
            + " (SELECT SUM(click_cost) as ccost FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as cl,"
            + " (SELECT count(*) as conversions FROM <server_log> WHERE conversion AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', entry_date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND entry_date < <final>) as iil) as yaxis"
            + " FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per click over time
    graphRangeQueries.put(
        Metric.CPC,
        "SELECT xaxis,"
            + " (SELECT CASE clicks WHEN 0 THEN 0 ELSE (icost + ccost) / 100 / clicks END FROM"
            + " (SELECT SUM(impression_cost) as icost FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as il,"
            + " (SELECT SUM(click_cost) as ccost FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>)  as cl,"
            + " (SELECT count(*) as clicks FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as iil) as yaxis"
            + " FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per 1000 impressions over time
    graphRangeQueries.put(
        Metric.CPM,
        "SELECT xaxis,"
            + " (SELECT (cost / 100 / impressions) * 1000 FROM"
            + " (SELECT SUM(impression_cost) as cost FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>)  as il,"
            + " (SELECT count(*) as impressions FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND(date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as iil) as yaxis"
            + " FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average number of clicks per impression over time
    graphRangeQueries.put(
        Metric.CTR,
        "SELECT xaxis, ("
            + " SELECT CASE il.impressions WHEN 0 THEN 0 ELSE ((cl.clicks::double precision) / il.impressions) * 100 END FROM"
            + " (SELECT count(*) as clicks FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as cl,"
            + " (SELECT count(*) as impressions FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND (date_trunc('<interval>', date) + (<start> - date_trunc('<interval>', <start>))) = xaxis AND date < <final>) as il) as yaxis"
            + " FROM"
            + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    graphRangeQueries.put(
            Metric.CLICK_COST,
            "SELECT click_cost as yaxis FROM click_view WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND <filterDate>");
  }

  /**
   * Define and store templates for every graph metric type without date range filter.
   * generate_series is used to avoid
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
                    + " (SELECT date_trunc('<interval>', date) AS dates, count(*) AS yaxis FROM impression_log WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
                    + " ON xaxis = s.dates;");

    // Total number of conversions over time - conversions are calculated per entry_date
    graphQueries.put(
            Metric.CONVERSIONS,
            "SELECT xaxis, s.yaxis FROM"
                    + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>,'1 <interval>') AS xaxis"
                    + " LEFT JOIN"
                    + " (SELECT date_trunc('<interval>', entry_date) AS dates, count(*) AS yaxis FROM <server_log> WHERE <campaign> AND conversion AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
                    + " ON xaxis = s.dates;");

    // Total number of clicks over time
    graphQueries.put(
            Metric.CLICKS,
            "SELECT xaxis, s.yaxis FROM"
                    + " (SELECT date_trunc('<interval>', min(date)) AS start FROM click_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(date)) AS final FROM click_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis"
                    + " LEFT JOIN"
                    + " (SELECT date_trunc('<interval>', date) AS dates, count(*) AS yaxis FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
                    + " ON xaxis = s.dates;");

    // Total cost over time
    graphQueries.put(
            Metric.TOTAL_COST,
            "SELECT xaxis, al.yaxis FROM"
                    + " (SELECT date_trunc('<interval>', min(date)) AS start FROM impression_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(date)) AS final FROM impression_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
                    + " (SELECT dates, SUM(cost) / 100 AS yaxis FROM"
                    + " (SELECT date_trunc('<interval>', date) AS dates, impression_cost AS cost FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender>"
                    + " UNION ALL"
                    + " SELECT date_trunc('<interval>', date) AS dates, click_cost AS cost FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS t"
                    + " GROUP BY dates) AS al"
                    + " ON xaxis = al.dates;");

    // Number of uniques over time
    graphQueries.put(
            Metric.UNIQUES,
            "SELECT xaxis, s.yaxis FROM"
                    + " (SELECT date_trunc('<interval>', min(date)) AS start FROM click_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(date)) AS final FROM click_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
                    + " (SELECT date_trunc('<interval>', date) AS dates, count(DISTINCT user_id) AS yaxis FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
                    + " ON xaxis = s.dates;");

    // Number of bounces over time
    graphQueries.put(
            Metric.BOUNCES,
            "SELECT xaxis, s.yaxis FROM"
                    + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis LEFT JOIN"
                    + " (SELECT date_trunc('<interval>', entry_date) AS dates, count(*) AS yaxis FROM <server_log> WHERE <bounce> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> GROUP BY dates) AS s"
                    + " ON xaxis = s.dates;");

    // Bounces per click over time
    graphQueries.put(
            Metric.BOUNCE_RATE,
            "SELECT xaxis, ("
                    + " SELECT (sl.bounces::double precision) / NULLIF(cl.clicks, 0) * 100 FROM"
                    + " (SELECT count(*) as bounces FROM <server_log> WHERE <bounce> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', entry_date) = xaxis) as sl,"
                    + " (SELECT count(*) as clicks FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as cl) as yaxis"
                    + " FROM"
                    + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per conversion over time
    graphQueries.put(
            Metric.CPA,
            "SELECT xaxis,"
                    + " (SELECT CASE conversions WHEN 0 THEN 0 ELSE (icost + ccost) / 100 / conversions END FROM"
                    + " (SELECT SUM(impression_cost) as icost FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as il,"
                    + " (SELECT SUM(click_cost) as ccost FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as cl,"
                    + " (SELECT count(*) as conversions FROM <server_log> WHERE conversion AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', entry_date) = xaxis) as iil) as yaxis"
                    + " FROM"
                    + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per click over time
    graphQueries.put(
            Metric.CPC,
            "SELECT xaxis,"
                    + " (SELECT CASE clicks WHEN 0 THEN 0 ELSE (icost + ccost) / 100 / clicks END FROM"
                    + " (SELECT SUM(impression_cost) as icost FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as il,"
                    + " (SELECT SUM(click_cost) as ccost FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as cl,"
                    + " (SELECT count(*) as clicks FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as iil) as yaxis"
                    + " FROM"
                    + " (SELECT date_trunc('<interval>', min(date)) AS start FROM click_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(date)) AS final FROM click_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average amount of money spent per 1000 impressions over time
    graphQueries.put(
            Metric.CPM,
            "SELECT xaxis,"
                    + " (SELECT (cost / 100 / impressions) * 1000 FROM"
                    + " (SELECT SUM(impression_cost) as cost FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as il,"
                    + " (SELECT count(*) as impressions FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as iil) as yaxis"
                    + " FROM"
                    + " (SELECT date_trunc('<interval>', min(entry_date)) AS start FROM server_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(entry_date)) AS final FROM server_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // Average number of clicks per impression over time
    graphQueries.put(
            Metric.CTR,
            "SELECT xaxis, ("
                    + " SELECT CASE il.impressions WHEN 0 THEN 0 ELSE ((cl.clicks::double precision) / il.impressions) * 100 END FROM"
                    + " (SELECT count(*) as clicks FROM <click_log> WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as cl,"
                    + " (SELECT count(*) as impressions FROM impression_log WHERE <campaign> AND <filterAge> AND <filterIncome> AND <filterGender> AND date_trunc('<interval>', date) = xaxis) as il) as yaxis"
                    + " FROM"
                    + " (SELECT date_trunc('<interval>', min(date)) AS start FROM impression_log WHERE <campaign>) AS min,"
                    + " (SELECT date_trunc('<interval>', max(date)) AS final FROM impression_log WHERE <campaign>) AS max,"
                    + " generate_series(<start>, <final>, '1 <interval>') AS xaxis;");

    // HISTOGRAM ONLY QUERY
    graphQueries.put(
            Metric.CLICK_COST,
            "SELECT click_cost as yaxis FROM click_view WHERE <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>");
  }

  /** Define and store templates for every statistic metric type. */
  private static void createStatisticTemplates() {

    // Total number of impressions
    statisticQueries.put(
        Metric.IMPRESSIONS,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM impression_log WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>;");

    // Total number of clicks
    statisticQueries.put(
        Metric.CLICKS, "SELECT 'all' AS xaxis, count(*) AS yaxis FROM <click_log> WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>;");

    // Total number of conversions
    statisticQueries.put(
        Metric.CONVERSIONS,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM <server_log> WHERE conversion AND entry_date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>;");

    // Total cost - includes both click and impression cost
    statisticQueries.put(
        Metric.TOTAL_COST,
        "SELECT 'all' AS xaxis, round(((il.cost + cl.cost) / 100)::NUMERIC, 2) AS yaxis FROM"
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS il,"
            + " (SELECT sum(click_cost) AS cost FROM <click_log> WHERE date BETWEEN <range> AND <campaign>) AS cl;");

    // Total number of uniques
    statisticQueries.put(
        Metric.UNIQUES,
        "SELECT 'all' AS xaxis, count(DISTINCT user_id) AS yaxis FROM <click_log> WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>;");

    // Total number of bounces - the <bounce> placeholder handles the 2 type of bounce definition
    // and specific values for them
    statisticQueries.put(
        Metric.BOUNCES,
        "SELECT 'all' AS xaxis, count(*) AS yaxis FROM <server_log> WHERE entry_date BETWEEN <range> AND <bounce> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>;");

    // Bounce rate - the <bounce> placeholder handles the 2 type of bounce definition and specific
    // values for them
    statisticQueries.put(
        Metric.BOUNCE_RATE,
        "SELECT 'all' AS xaxis, round((bounces / NULLIF(clicks, 0) * 100)::NUMERIC, 2) AS yaxis FROM"
            + " (SELECT count(*) AS bounces FROM <server_log> WHERE entry_date BETWEEN <range> AND <bounce> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS sl,"
            + " (SELECT count(*)::double precision AS clicks FROM <click_log> WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS cl;");

    // Average amount of money spent on a campaign for each conversion (CPA)
    statisticQueries.put(
        Metric.CPA,
        "SELECT 'all' AS xaxis, round(((il.cost + cl.cost) / 100 / NULLIF(conversions, 0))::NUMERIC, 2) AS yaxis FROM "
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS il,"
            + " (SELECT sum(click_cost) AS cost FROM <click_log> WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS cl,"
            + " (SELECT count(*) AS conversions FROM <server_log> WHERE entry_date BETWEEN <range> AND conversion AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS sl;");

    // The average amount of money spent for each click (CPC)
    statisticQueries.put(
        Metric.CPC,
        "SELECT 'all' AS xaxis, round(((il.cost + cl.cost) / 100 / NULLIF(clicks, 0))::NUMERIC, 2) AS yaxis FROM "
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS il,"
            + " (SELECT  sum(click_cost) AS cost FROM <click_log> WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS cl,"
            + " (SELECT count(*) AS clicks FROM <click_log> WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS ccl;");

    // The average amount of money spent per 1000 impressions (CPM)
    statisticQueries.put(
        Metric.CPM,
        "SELECT 'all' AS xaxis, round(((il.cost / 100 / impressions) * 1000)::NUMERIC, 2) AS yaxis FROM"
            + " (SELECT sum(impression_cost) AS cost FROM impression_log WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS il,"
            + " (SELECT count(*) AS impressions FROM impression_log WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS iil;");

    // The average amount of clicks per impression (CTR)
    statisticQueries.put(
        Metric.CTR,
        "SELECT 'all' AS xaxis, round(((clicks::double precision) / NULLIF(impressions, 0) * 100)::NUMERIC, 2) AS yaxis FROM"
            + " (SELECT count(*) AS clicks FROM <click_log> WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS cl,"
            + " (SELECT count(*) AS impressions FROM impression_log WHERE date BETWEEN <range> AND <campaign> AND <filterAge> AND <filterContext> AND <filterIncome> AND <filterGender>) AS il;");
  }

  /**
   * Using a metric query request object, generate its corresponding SQL.
   *
   * @param request Query to generate SQL for
   * @return SQL generated from given MetricQuery
   */
  public static String generateSQL(MetricQuery request) throws MalformedFilterException {
    String sql;

    // If the user sets an interval create graph query and apply grouping
    // Otherwise, create statistic query
    if (request.interval != null) {
      if (request.filter == null || request.filter.dates == null) {
        sql = graphQueries.get(request.metric);
      } else {
        sql = graphRangeQueries.get(request.metric);
      }
      sql = applyGrouping(sql, request.interval);
    } else {
      sql = statisticQueries.get(request.metric);
    }

    // Check for invalid filtering
    malformedFilterCheck(request.filter);

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

    //If the user applies a filter, a 'new table' containing user_id, income, context, age and gender is generated.
    //This aims to simplify the filtering as much as possible, by integrating filter information from impression_log into the other two logs.
    if (request.filter != null) {
      sql =
              sql.replace(
                      "<server_log>",
                      "server_view");
      sql =
              sql.replace(
                      "<click_log>",
                      "click_view");
      
      // Apply date range query (if provided by user)
      if (request.filter.dates != null) {
        if (request.filter.dates.min != null) {
          sql = sql.replace("<start>", "'" + f.format(request.filter.dates.min) + "'::TIMESTAMP");
          sql = sql.replace("<range>", "'" + f.format(request.filter.dates.min) + "'::TIMESTAMP AND '" + f.format(request.filter.dates.max) + "'::TIMESTAMP");
          sql = sql.replace("<filterDate>", "date BETWEEN '" + f.format(request.filter.dates.min) + "'::TIMESTAMP and '" + f.format(request.filter.dates.max) + "'::TIMESTAMP" );
        }
        if (request.filter.dates.max != null) {
          sql = sql.replace("<final>", "'" + f.format(request.filter.dates.max) + "'::TIMESTAMP");
        }
      }

      //Add age preference (if provided by user); Also allows for selecting multiple age filters
      if (request.filter.ages != null) {
        StringBuilder ages = new StringBuilder("age IN (");
        for (FilterConfig.Age a : request.filter.ages) {
          ages.append("'").append(a).append("',");
        }
        ages.deleteCharAt(ages.length() - 1);
        ages.append(")");

        sql = sql.replace("<filterAge>", ages.toString());
      }

      //Add context preference (if provided by user); Also allows for selecting multiple context filters
      if (request.filter.contexts != null) {
        StringBuilder contexts = new StringBuilder("context IN (");
        for (FilterConfig.Context cont : request.filter.contexts) {
          contexts.append("'").append(cont).append("',");
        }
        contexts.deleteCharAt(contexts.length() - 1);
        contexts.append(")");

        sql = sql.replace("<filterContext>", contexts.toString());
      }

      if (request.filter.incomes != null) {
        StringBuilder incomes = new StringBuilder("income IN (");
        for (FilterConfig.Income inc : request.filter.incomes) {
          incomes.append("'").append(inc).append("',");
        }
        incomes.deleteCharAt(incomes.length() - 1);
        incomes.append(")");

        sql = sql.replace("<filterIncome>", incomes.toString());
      }

      if (request.filter.genders != null) {
        // Apply gender filter (if provided by user)
        if (!(request.filter.genders.contains(FilterConfig.Gender.FEMALE) && request.filter.genders.contains(FilterConfig.Gender.MALE))) {
          if (request.filter.genders.contains(FilterConfig.Gender.FEMALE))
            sql = sql.replace("<filterGender>", "female");
          if (request.filter.genders.contains(FilterConfig.Gender.MALE))
            sql = sql.replace("<filterGender>", "female = 'false'");
        }
      }
    }
    // Apply default settings, if filters are null
    sql = applyDefaultReplacements(sql);
    return sql;
  }

  /**
   * Apply default filtering, if not specified by the user
   * If there is no campaignID or filtering specified, fetch all the data
   * If there is no date range filter applied, then use the min and max date from the 'date' ('entry_date' for server_log) column in the table
   * @param sql SQL statement with placeholders to be adjusted
   * @return Modified SQL statement
   */
  private static String applyDefaultReplacements(String sql) {
    sql = sql.replace("il.<campaign>", "1 = 1")
          .replace("cl.<campaign>", "1 = 1")
          .replace("sl.<campaign>", "1 =1 ")
          .replace("<campaign>", "1 = 1")
          .replace("<start>", "start")
          .replace("<final>", "final")
          .replace("<bounce>", "1 = 1")
          .replace("<filterAge>", "1 = 1")
          .replace("<filterContext>", "1 = 1")
          .replace("<filterIncome>", "1 = 1")
          .replace("<filterGender>", "1 = 1")
          .replace("<server_log>", "server_log")
          .replace("<click_log>", "click_log")
          .replace("entry_date BETWEEN <range> AND", "")
          .replace("date BETWEEN <range> AND", "");
    return sql;
  }

  /**
   * Helper function to modify template code to apply specific interval grouping.
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

  public static class MalformedFilterException extends Exception {
    private static final long serialVersionUID = 291915740307942812L;
  }

  /**
   * Checks if FilterConfig is invalid.
   * @param filterConfig which is checked for validity
   * @throws MalformedFilterException when the filter is initialised but empty
   */
  private static void malformedFilterCheck(FilterConfig filterConfig) throws MalformedFilterException {
    if (filterConfig == null) {
      return;
    }

    ArrayDeque<Collection<?>> filters = new ArrayDeque<>();
    if (filterConfig.ages != null) {
      filters.add(filterConfig.ages);
    }

    if (filterConfig.contexts != null) {
      filters.add(filterConfig.contexts);
    }

    if (filterConfig.genders != null) {
      filters.add(filterConfig.genders);
    }

    if (filterConfig.incomes != null) {
      filters.add(filterConfig.incomes);
    }

    for (Collection<?> filter : filters) {
      if (filter != null) {
        if (filter.size() == 0) {
          throw new MalformedFilterException();
        }
      }
    }
  }
}