import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import group33.seg.model.configs.*;
import org.junit.Test;
import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.handlers.CampaignImportHandler;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;
import group33.seg.model.workspace.Workspace;

/** Tests for correctness of queries on a small set of example data */
public class QueryCorrectnessTest {

  private static final String CAMPAIGN_NAME = "queryTest";
  private static final String PATH_TO_DATASET = "src/test/resources/example-dataset/";
  private static final String PATH_CLICK_LOG = PATH_TO_DATASET + "click_log.csv";
  private static final String PATH_IMPRESSION_LOG = PATH_TO_DATASET + "impression_log.csv";
  private static final String PATH_SERVER_LOG = PATH_TO_DATASET + "server_log.csv";

  private static final String DATABASE_CREDENTIALS = "src/test/resources/test.properties";

  private DashboardController controller;
  private DatabaseHandler databaseHandler;
  private CampaignConfig campaign;

  /** Initialise tables and data for test */
  public QueryCorrectnessTest() throws FileNotFoundException {
    try {
      // Initialise a controller for these tests (no model or view needed)
      controller = new DashboardController(null, null);
      controller.database.refreshConnections(new DatabaseConfig(DATABASE_CREDENTIALS), 5);

      // Do import using custom test configuration
      CampaignImportConfig importConfig = new CampaignImportConfig(CAMPAIGN_NAME, PATH_CLICK_LOG,
          PATH_IMPRESSION_LOG, PATH_SERVER_LOG);
      boolean importComplete = controller.imports.doImport(importConfig);

      while (controller.imports.isOngoing()) {
        Thread.sleep(100);
      }
      campaign = controller.imports.getImportedCampaign();

      // setup database handler
      if (importComplete) {
        databaseHandler = controller.database;
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

    MetricQuery statQuery = new MetricQuery(Metric.IMPRESSIONS, null, null, null, campaign);
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

    MetricQuery graphQuery = new MetricQuery(Metric.IMPRESSIONS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for impression metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void clicksTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 50L));

    MetricQuery statQuery = new MetricQuery(Metric.CLICKS, null, null, null, campaign);
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

    MetricQuery graphQuery = new MetricQuery(Metric.CLICKS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void conversionTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 3L));

    MetricQuery statQuery = new MetricQuery(Metric.CONVERSIONS, null, null, null, campaign);
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

    MetricQuery graphQuery = new MetricQuery(Metric.CONVERSIONS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for conversions metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void cpaTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 109.51602866666661));

    MetricQuery statQuery = new MetricQuery(Metric.CPA, null, null, null, campaign);
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

    MetricQuery graphQuery = new MetricQuery(Metric.CPA, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPA metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void cpcTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 6.570961719999997));

    MetricQuery statQuery = new MetricQuery(Metric.CPC, null, null, null, campaign);
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

    MetricQuery graphQuery = new MetricQuery(Metric.CPC, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void cpmTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 1.0916200000000003));

    MetricQuery statQuery = new MetricQuery(Metric.CPM, null, null, null, campaign);
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

    MetricQuery graphQuery = new MetricQuery(Metric.CPM, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPM metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void ctrTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 1.0));

    MetricQuery statQuery = new MetricQuery(Metric.CTR, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CTR, should have been 1.0 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 0.6666666666666666));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 0.5));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 1.4));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 0.8));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 1.25));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 0.8571428571428571));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 1.375));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 0.8888888888888888));

    MetricQuery graphQuery = new MetricQuery(Metric.CTR, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CTR metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void totalCostTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 328.54808599999984));

    MetricQuery statQuery = new MetricQuery(Metric.TOTAL_COST, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for total cost, should have been 328.54808599999984 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 0.002345));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 21.803190000000004));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 20.176677000000005));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 32.008085));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 18.876013));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 58.197251));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 44.082891));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 86.722779));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 46.678855));

    MetricQuery graphQuery = new MetricQuery(Metric.TOTAL_COST, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for total cost metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void uniquesTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 50L));

    MetricQuery statQuery = new MetricQuery(Metric.UNIQUES, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for uniques, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 2L));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 2L));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 7L));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 4L));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 10L));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 6L));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 11L));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 8L));

    MetricQuery graphQuery = new MetricQuery(Metric.UNIQUES, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for uniques metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void bouncesTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 27L));

    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    MetricQuery statQuery = new MetricQuery(Metric.BOUNCES, null, null, bounceConfig, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for bounces, should have been 27 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 4L));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 2L));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 4L));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 5L));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 3L));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 4L));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 3L));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 2L));

    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCES, Interval.WEEK, null, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for bounces metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void bounceRateTest () {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 54.0));

    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    MetricQuery statQuery = new MetricQuery(Metric.BOUNCE_RATE, null, null, bounceConfig, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for bounce rate, should have been 54.0 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", null));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 100.0));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 200.0));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 71.42857142857143));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 75.0));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 0.0));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 66.66666666666666));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 27.27272727272727));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 25.0));

    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCE_RATE, Interval.WEEK, null, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for bounce rate metric", weekResponse.equals(expectedWeekResponse));
  }
}
