--STATISTIC QUERIES
--Number of impressions (bash: awk 'END {print NR}' impression_log.csv (-1 for the header))
SELECT
  'all'    AS xaxis,
  COUNT(*) AS yaxis
FROM impression_log;

--Number of conversions
SELECT
  'all'                  AS xaxis,
  sum(conversion :: INT) AS yaxis
FROM server_log;

--Number of clicks (bash: awk 'END {print NR}' click_log.csv (-1 for the header))
SELECT
  'all'    AS xaxis,
  COUNT(*) AS yaxis
FROM click_log;

--Total cost
SELECT
  'all'             AS xaxis,
  il.cost + cl.cost AS yaxis
FROM
  (SELECT sum(impression_cost) AS cost
   FROM impression_log
   WHERE campaign_id = 1) AS il,
  (SELECT sum(click_cost) AS cost
   FROM click_log
   WHERE campaign_id = 1) AS cl;

--Number of uniques
SELECT
  'all'                   AS xaxis,
  count(DISTINCT user_id) AS yaxis
FROM click_log;

--Number of bounces (pages viewed)
SELECT
  'all'    AS xaxis,
  count(*) AS yaxis
FROM server_log
WHERE (pages_viewed <= 1);

--Number of bounces (time spent)
SELECT
  'all'    AS xaxis,
  count(*) AS yaxis
FROM server_log
WHERE EXTRACT(EPOCH FROM (exit_date - entry_date)) <= 2;

--Bounce rate (pages viewed)
SELECT
  'all'            AS xaxis,
  bounces / clicks AS yaxis
FROM
  (SELECT count(*) AS bounces
   FROM server_log
   WHERE (pages_viewed <= 1)) AS sl,
  (SELECT count(*) :: DECIMAL AS clicks
   FROM click_log) AS cl;

--Bounce rate (time spent)
SELECT
  'all'            AS xaxis,
  bounces / clicks AS yaxis
FROM
  (SELECT count(*) AS bounces
   FROM server_log
   WHERE (EXTRACT(EPOCH FROM (exit_date - entry_date))) <= 2) AS sl,
  (SELECT count(*) :: DECIMAL AS clicks
   FROM click_log) AS cl;

--Avg amount of money per conversion (CPA)
SELECT
  'all'                             AS xaxis,
  (il.cost + cl.cost) / conversions AS yaxis
FROM
  (SELECT sum(impression_cost) AS cost
   FROM impression_log) AS il,
  (SELECT sum(click_cost) AS cost
   FROM click_log) AS cl,
  (SELECT count(*) AS conversions
   FROM server_log
   WHERE conversion = TRUE) AS sl;

--Avg amount of money per click (CPC)
SELECT
  'all'                        AS xaxis,
  (il.cost + cl.cost) / clicks AS yaxis
FROM
  (SELECT sum(impression_cost) AS cost
   FROM impression_log) AS il,
  (SELECT sum(click_cost) AS cost
   FROM click_log) AS cl,
  (SELECT count(*) AS clicks
   FROM click_log) AS ccl;

--Avg amount of money per 1000 impressions (CPM)
SELECT
  'all'                                     AS xaxis,
  (il.cost :: DECIMAL / impressions) * 1000 AS yaxis
FROM
  (SELECT sum(impression_cost) AS cost
   FROM impression_log) AS il,
  (SELECT count(*) AS impressions
   FROM impression_log) AS iil;

--Avg number of clicks per impression(CTR)
SELECT
  'all'                             AS xaxis,
  (clicks :: DECIMAL) / impressions AS yaxis
FROM
  (SELECT count(*) AS clicks
   FROM click_log) AS cl,
  (SELECT count(*) AS impressions
   FROM impression_log) AS il;

--GRAPH QUERIES
--Number of impressions (hourly) - default if date range is not set
SELECT
  xaxis,
  s.yaxis
FROM
  (SELECT date_trunc('hour', min(date)) AS start
   FROM impression_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(date)) AS final
   FROM impression_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis LEFT JOIN
  (SELECT
     date_trunc('hour', date) AS dates,
     COUNT(*)                 AS yaxis
   FROM impression_log
   WHERE campaign_id = 1
   GROUP BY dates) AS s
    ON xaxis = s.dates;

--Number of conversions (hourly)
SELECT
  xaxis,
  s.yaxis
FROM
  (SELECT date_trunc('day', min(entry_date)) AS start
   FROM server_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('day', max(entry_date)) AS final
   FROM server_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 day') AS xaxis LEFT JOIN
  (SELECT
     date_trunc('day', entry_date) AS dates,
     COUNT(*)                      AS yaxis
   FROM server_log
   WHERE campaign_id = 1 AND conversion = TRUE
   GROUP BY dates) AS s
    ON xaxis = s.dates;

--Number of clicks (hourly)
SELECT
  xaxis,
  s.yaxis
FROM
  (SELECT date_trunc('week', min(date)) AS start
   FROM click_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('week', max(date)) AS final
   FROM click_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 week') AS xaxis LEFT JOIN
  (SELECT
     date_trunc('week', date) AS dates,
     COUNT(*)                 AS yaxis
   FROM click_log
   WHERE campaign_id = 1
   GROUP BY dates) AS s
    ON xaxis = s.dates;

--Total cost
SELECT
  xaxis,
  al.yaxis
FROM
  (SELECT date_trunc('hour', min(date)) AS start
   FROM impression_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(date)) AS final
   FROM impression_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis LEFT JOIN
  (SELECT
     dates,
     SUM(cost) AS yaxis
   FROM
     (SELECT
        date_trunc('hour', date) AS dates,
        impression_cost          AS cost
      FROM impression_log
      WHERE campaign_id = 1
      UNION ALL
      SELECT
        date_trunc('hour', date) AS dates,
        click_cost               AS cost
      FROM click_log
      WHERE campaign_id = 1) AS t
   GROUP BY dates
  ) AS al
    ON xaxis = al.dates;

--Number of uniques
SELECT
  xaxis,
  s.yaxis
FROM
  (SELECT date_trunc('hour', min(date)) AS start
   FROM click_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(date)) AS final
   FROM click_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis LEFT JOIN
  (SELECT
     date_trunc('hour', date) AS dates,
     count(DISTINCT user_id)  AS yaxis
   FROM click_log
   WHERE campaign_id = 1
   GROUP BY dates) AS s
    ON xaxis = s.dates;

--Number of bounces
SELECT
  xaxis,
  s.yaxis
FROM
  (SELECT date_trunc('hour', min(entry_date)) AS start
   FROM server_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(entry_date)) AS final
   FROM server_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis LEFT JOIN
  (SELECT
     date_trunc('hour', entry_date) AS dates,
     count(*)                       AS yaxis
   FROM server_log
   WHERE pages_viewed < 2 AND campaign_id = 1
   GROUP BY dates) AS s
    ON xaxis = s.dates;

--Bounce rate - bounces over clicks
SELECT
  xaxis,
  (
    SELECT sl.bounces :: DECIMAL / cl.clicks * 100
    FROM
      (SELECT count(*) AS bounces
       FROM server_log
       WHERE pages_viewed <= 2 AND campaign_id = 1 AND date_trunc('year', entry_date) = xaxis) AS sl,
      (SELECT count(*) AS clicks
       FROM click_log
       WHERE campaign_id = 1 AND date_trunc('year', date) = xaxis) AS cl) AS yaxis
FROM
  (SELECT date_trunc('year', min(entry_date)) AS start
   FROM server_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('year', max(entry_date)) AS final
   FROM server_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 year') AS xaxis;

--CPA - money spent per conversion (total cost over number of conversions)
SELECT
  xaxis,
  (
    SELECT CASE conversions
           WHEN 0
             THEN 0
           ELSE (icost + ccost) / conversions END
    FROM
      (SELECT SUM(impression_cost) AS icost
       FROM impression_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS il,
      (SELECT SUM(click_cost) AS ccost
       FROM click_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS cl,
      (SELECT count(*) AS conversions
       FROM server_log
       WHERE conversion = TRUE AND campaign_id = 1 AND date_trunc('hour', entry_date) = xaxis) AS iil
  ) AS yaxis
FROM
  (SELECT date_trunc('hour', min(entry_date)) AS start
   FROM server_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(entry_date)) AS final
   FROM server_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis;

--CPC - avg amount of money spent per click (total cost over number of clicks)
SELECT
  xaxis,
  (
    SELECT CASE clicks
           WHEN 0
             THEN 0
           ELSE (icost + ccost) / clicks END
    FROM
      (SELECT SUM(impression_cost) AS icost
       FROM impression_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS il,
      (SELECT SUM(click_cost) AS ccost
       FROM click_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS cl,
      (SELECT count(*) AS clicks
       FROM click_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS iil
  ) AS yaxis
FROM
  (SELECT date_trunc('hour', min(date)) AS start
   FROM click_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(date)) AS final
   FROM click_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis;

--CPM - average amount of money pent per 1000 impressions (impression_cost over number of impressions *1000)
SELECT
  xaxis,
  (
    SELECT (cost / impressions) * 1000
    FROM
      (SELECT SUM(impression_cost) AS cost
       FROM impression_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS il,
      (SELECT count(*) AS impressions
       FROM impression_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS iil
  ) AS yaxis
FROM
  (SELECT date_trunc('hour', min(entry_date)) AS start
   FROM server_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(entry_date)) AS final
   FROM server_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis;

--CTR - average amount of clicks per impression (number of clicks over number of impressions)
SELECT
  xaxis,
  (
    SELECT cl.clicks :: DECIMAL / il.impressions
    FROM
      (SELECT count(*) AS clicks
       FROM click_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS cl,
      (SELECT count(*) AS impressions
       FROM impression_log
       WHERE campaign_id = 1 AND date_trunc('hour', date) = xaxis) AS il) AS yaxis
FROM
  (SELECT date_trunc('hour', min(date)) AS start
   FROM impression_log
   WHERE campaign_id = 1) AS min,
  (SELECT date_trunc('hour', max(date)) AS final
   FROM impression_log
   WHERE campaign_id = 1) AS max,
      generate_series(start, final, '1 hour') AS xaxis;

--DELETE TABLES
DROP TABLE campaign;
DROP TABLE click_log;
DROP TABLE impression_log;
DROP TABLE server_log;

