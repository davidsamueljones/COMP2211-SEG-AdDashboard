import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import group33.seg.controller.database.tables.CampaignTable;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import group33.seg.model.configs.*;
import group33.seg.model.utilities.Range;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
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

  private static final double MARGIN_OF_ERROR = 0.01;

  private static final String DATABASE_CREDENTIALS = "src/test/resources/test.properties";

  private static DatabaseHandler databaseHandler;
  private static CampaignConfig campaign;

  /** Initialise tables and data before running all the tests */
  @BeforeClass
  public static void init() throws FileNotFoundException {
    try {
      // Initialise a controller for these tests (no model or view needed)
      DashboardController controller = new DashboardController(null, null);
      try {
        DatabaseConfig database = new DatabaseConfig(DATABASE_CREDENTIALS);
        controller.database.refreshConnections(database, 10);
      } catch (FileNotFoundException e) {
        System.err.println("Unable to test database connection configuration file");
      } catch (SQLException e) {
        System.err.println("Unable to create database connections, check that hostname, "
            + "port, username and password are all correct in the configuration file");
      }

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

  /**
   * Delete the tables after testing
   */
  @AfterClass
  public static void cleanupDb() throws SQLException {
    CampaignTable cmp = new CampaignTable();
    ServerLogTable sl = new ServerLogTable();
    ClickLogTable cl = new ClickLogTable();
    ImpressionLogTable il = new ImpressionLogTable();
    Connection c = databaseHandler.getConnection();
    sl.dropTable(c);
    cl.dropTable(c);
    il.dropTable(c);
    cmp.dropTable(c);
    databaseHandler.returnConnection(c);
  }

  /**
   * Method for calculating whether a test result is within a certain percentage of the actual value
   *
   * @param expected expected value for this test result
   * @param actual actual value for this test result
   *
   * @return True if the actual value is within the margin of error
   */
  private boolean withinMarginOfError(Number expected, Number actual) {
    return expected.doubleValue() == 0.0 & actual.doubleValue() == 0.0 || Math.abs(1.0 - (actual.doubleValue() / expected.doubleValue())) < MARGIN_OF_ERROR;
  }

  /**
   * Method for comparing two lists with a margin of error
   * @param expectedList list of expected test results
   * @param actualList list of actual test results
   *
   * @return True if the lists are within margin of error
   */
  private boolean listsWithinMarginOfError (List<Pair<String, Number>> expectedList,
                                              List<Pair<String, Number>> actualList) {
    //check lengths
    if (expectedList.size() != actualList.size()) {
      return false;
    }

    //actually compare values
    for (int i = 0; i < expectedList.size(); i++) {
      Pair<String, Number> expected = expectedList.get(i);
      Pair<String, Number> actual = actualList.get(i);

      //comparing keys
      if (!expected.key.equals(actual.key)) {
        return false;
      }

      //only check if not technically equal
      if (!Objects.equals(expected.value, actual.value)) {

        //catch null exceptions before they happen
        if (expected.value == null | actual.value == null) {
          return false;
        }

        //comparing values
        if (!withinMarginOfError(expected.value, actual.value)) {
          return false;
        }
      }
    }

    return true;
  }

  @Test
  public void checkDatabaseHandler() {
    if (databaseHandler == null) {
      fail("Database setup failed");
    }
  }

  @Test
  public void impressionsTest() {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(50L);

    MetricQuery statQuery = new MetricQuery(Metric.IMPRESSIONS, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for impressions, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));

    //Graph test
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse(true, 1L,3L,4L,5L,5L,8L,7L,8L, 9L);

    MetricQuery graphQuery = new MetricQuery(Metric.IMPRESSIONS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for impression metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void clicksTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(50L);

    MetricQuery statQuery = new MetricQuery(Metric.CLICKS, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for clicks, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));

    //Graph test
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse(false, null,2L,2L,7L,4L,10L,6L,11L,8L);

    MetricQuery graphQuery = new MetricQuery(Metric.CLICKS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void conversionTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(3L);

    MetricQuery statQuery = new MetricQuery(Metric.CONVERSIONS, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for conversions, should have been 3 but was " + response.get(0).value, response.equals(expectedResponse));

    //Graph test
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse(true, 1L,null,null,null,null,1L,null,null,1L);

    MetricQuery graphQuery = new MetricQuery(Metric.CONVERSIONS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for conversions metric", weekResponse.equals(expectedWeekResponse));
  }


  @Test
  public void cpaTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(109.51602866666661);

    MetricQuery statQuery = new MetricQuery(Metric.CPA, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPA, should have been 109.51602867 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse(true, null,0.0,0.0,0.0,0.0,58.197251,0.0,0.0,46.678855);

    MetricQuery graphQuery = new MetricQuery(Metric.CPA, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPA metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void cpcTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(6.570961719999997);

    MetricQuery statQuery = new MetricQuery(Metric.CPC, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPC, should have been 6.571 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(false, null, 10.901594999999999, 10.0883385, 4.572583571428572, 4.71900325, 5.8197251, 7.3471485, 7.883889000000002, 5.834856875);

    MetricQuery graphQuery = new MetricQuery(Metric.CPC, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for clicks metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void cpmTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(1.0916200000000003);

    MetricQuery statQuery = new MetricQuery(Metric.CPM, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPM, should have been 1.0916200000000003 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));


    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(true, 2.3449999999999998,  1.1273333333333333, 1.2465, 0.3792, 0.24680000000000005, 1.2938749999999999, 1.584, 0.8975000000000001, 1.3465555555555557);

    MetricQuery graphQuery = new MetricQuery(Metric.CPM, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPM metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void ctrTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(1.0);

    MetricQuery statQuery = new MetricQuery(Metric.CTR, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CTR, should have been 1.0 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(true, 0.0, 0.6666666666666666, 0.5, 1.4, 0.8, 1.25,0.8571428571428571, 1.375, 0.8888888888888888);

    MetricQuery graphQuery = new MetricQuery(Metric.CTR, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CTR metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void totalCostTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(328.54808599999984);

    MetricQuery statQuery = new MetricQuery(Metric.TOTAL_COST, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for total cost, should have been 328.54808599999984 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(true, 0.002345, 21.803190000000004, 20.176677000000005, 32.008085, 18.876013,
            58.197251, 44.082891, 86.722779, 46.678855 );

    MetricQuery graphQuery = new MetricQuery(Metric.TOTAL_COST, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for total cost metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void uniquesTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(50L);

    MetricQuery statQuery = new MetricQuery(Metric.UNIQUES, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for uniques, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(false, null,2L,2L,7L,4L,10L,6L,11L,8L);

    MetricQuery graphQuery = new MetricQuery(Metric.UNIQUES, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for uniques metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void bouncesTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(27L);

    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    MetricQuery statQuery = new MetricQuery(Metric.BOUNCES, null, null, bounceConfig, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for bounces, should have been 27 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(true, 4L,2L, 4L, 5L, 3L, null, 4L, 3L,2L);

    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCES, Interval.WEEK, null, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for bounces metric", weekResponse.equals(expectedWeekResponse));
  }


  @Test
  public void bounceRateTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(54.0);

    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    MetricQuery statQuery = new MetricQuery(Metric.BOUNCE_RATE, null, null, bounceConfig, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for bounce rate, should have been 54.0 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(true, null,100.0,200.0,71.42857142857143, 75.0, 0.0, 66.66666666666666,27.27272727272727, 25.0);


    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCE_RATE, Interval.WEEK, null, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for bounce rate metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  //This was just for date range testing, while I was fixing it; feel free to leave/remove
  @Test
  public void dateRangeTest() throws ParseException {
    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    FilterConfig filter = new FilterConfig();
    filter.dates = new Range<>(f.parse("2014-12-29 00:00:00"), f.parse("2015-02-23 00:00:00"));
    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(true, null,100.0,200.0,71.42857142857143, 75.0, 0.0, 66.66666666666666,27.27272727272727, 25.0);

    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCE_RATE, Interval.WEEK, filter, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    for(Pair<String, Number> p: weekResponse) {
      System.out.println(p.key + " " +p.value);
    }

    assertTrue(listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  /**
   * Create a list and populate it with expected values
   * @param result - The result obtained after manual calculations on the example dataset
   * @return a list with expected result
   */
  private List<Pair<String, Number>> response(Number result) {
    List<Pair<String, Number>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", result));
    return expectedResponse;
  }

  /**
   * Create a list of all weeks generated by date_trunc and generate_series and corresponding expected key metric result
   * From the example-dataset we are using there are only two possible week combinations, the only difference being that some
   * of the queries include an extra week. In case the query doesn't return that week, it is not added to the list.
   * @param w1,w2,w3,w4,w5,w6,w7,w8,w9- expected results in each week;
   * @return a week - result list
   */

  private List<Pair<String,Number>> weeklyResponse(boolean weekOne, Number w1, Number w2, Number w3, Number w4, Number w5, Number w6, Number w7, Number w8, Number w9) {
    List<Pair<String, Number>> expectedWeekResponse = new LinkedList<>();

    if(weekOne)
      expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", w1));

    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", w2));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", w3));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", w4));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", w5));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", w6));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", w7));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", w8));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", w9));
    return expectedWeekResponse;
  }

}
