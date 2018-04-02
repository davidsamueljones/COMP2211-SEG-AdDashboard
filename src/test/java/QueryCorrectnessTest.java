import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import group33.seg.model.configs.*;
import org.junit.Test;

import group33.seg.controller.handlers.CampaignImportHandler;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;

/** Tests for correctness of queries on a small set of example data */
public class QueryCorrectnessTest {

  private static final String CAMPAIGN_NAME = "queryTest";
  private static final String PATH_TO_DATASET = "src/test/resources/example-dataset/";
  private static final String PATH_CLICK_LOG = PATH_TO_DATASET + "click_log.csv";
  private static final String PATH_IMPRESSION_LOG = PATH_TO_DATASET + "impression_log.csv";
  private static final String PATH_SERVER_LOG = PATH_TO_DATASET + "server_log.csv";

  private static final String DATABASE_CREDENTIALS = "src/test/resources/test.properties";

  private DatabaseHandler databaseHandler;

  /** Initialise tables and data for test */
  public QueryCorrectnessTest() throws FileNotFoundException {
    try {

      // init tables
      CampaignImportConfig importConfig =
          new CampaignImportConfig(
              CAMPAIGN_NAME,
              PATH_CLICK_LOG,
              PATH_IMPRESSION_LOG,
              PATH_SERVER_LOG,
              DATABASE_CREDENTIALS);
      CampaignImportHandler importHandler = new CampaignImportHandler(null);
      boolean importComplete = importHandler.doImport(importConfig);

      while (importHandler.isOngoing()) {
        Thread.sleep(10);
      }

      // setup database handler
      if (importComplete) {
        databaseHandler = new DatabaseHandler(null, DATABASE_CREDENTIALS);
      } else {
        databaseHandler = null;
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void checkDatabaseHandler() {
    if (databaseHandler == null) {
      fail("Database setup failed");
    }
  }

  @Test
  public void impressionsTest() {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 50L));

    MetricQuery statQuery = new MetricQuery(Metric.IMPRESSIONS, null, null);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for impressions, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String, Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 1L));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 3L));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 4L));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 5L));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 5L));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 8L));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 7L));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 8L));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 9L));

    MetricQuery graphQuery = new MetricQuery(Metric.IMPRESSIONS, Interval.WEEK, null);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for impression metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void clicksTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 50L));

    MetricQuery statQuery = new MetricQuery(Metric.CLICKS, null, null);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for clicks, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String, Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 2L));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 2L));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 7L));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 4L));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 10L));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 6L));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 11L));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 8L));

    MetricQuery graphQuery = new MetricQuery(Metric.CLICKS, Interval.WEEK, null);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void conversionTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 3L));

    MetricQuery statQuery = new MetricQuery(Metric.CONVERSIONS, null, null);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for conversions, should have been 3 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String, Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 1L));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 1L));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 1L));

    MetricQuery graphQuery = new MetricQuery(Metric.CONVERSIONS, Interval.WEEK, null);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    for(Pair p : weekResponse){
      System.out.println(p.key + " " + p.value);
    }

    assertTrue("Time interval test wrong for conversions metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void cpaTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 109.51602866666661));

    MetricQuery statQuery = new MetricQuery(Metric.CPA, null, null);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPA, should have been 109.51602866666661 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String, Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 58.197251));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 46.678855));

    MetricQuery graphQuery = new MetricQuery(Metric.CPA, Interval.WEEK, null);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPA metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void cpcTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 6.570961719999997));

    MetricQuery statQuery = new MetricQuery(Metric.CPC, null, null);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPC, should have been 6.570961719999997 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 10.901594999999999));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 10.0883385));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 4.572583571428572));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 4.71900325));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 5.8197251));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 7.3471485));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 7.883889000000002));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 5.834856875));

    MetricQuery graphQuery = new MetricQuery(Metric.CPC, Interval.WEEK, null);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void cpmTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 1.0916200000000003));

    MetricQuery statQuery = new MetricQuery(Metric.CPM, null, null);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPM, should have been 1.0916200000000003 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 2.3449999999999998));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 1.1273333333333333));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 1.2465));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 0.3792));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 0.24680000000000005));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 1.2938749999999999));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 1.584));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 0.8975000000000001));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 1.3465555555555557));

    MetricQuery graphQuery = new MetricQuery(Metric.CPM, Interval.WEEK, null);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPM metric", weekResponse.equals(expectedWeekResponse));
  }
}
