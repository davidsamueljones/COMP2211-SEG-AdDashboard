import static org.junit.Assert.assertNotNull;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.lib.Pair;
import group33.seg.lib.Range;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import utils.QueryTestUtil;

/** Tests for correctness of queries on a small set of example data */
public class QueryCorrectnessTest {

  private static final double MARGIN_OF_ERROR = 0.01;
  private static DatabaseHandler databaseHandler;
  private static CampaignConfig campaign;
  /**
   * Initialise tables and populate before running the tests
   */
  @BeforeClass
  public static void init() {
    DashboardController controller = QueryTestUtil.setUp();
    assertNotNull(controller);
    CampaignConfig campaign = controller.imports.getImportedCampaign();
    databaseHandler = controller.database;
  }

  /**
   * Delete tables after testing
   */
  @AfterClass
  public static void cleanupDb() {
    QueryTestUtil.tearDown(databaseHandler);
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
    List<Pair<String, Number>> expectedResponse = response(143L);

    MetricQuery statQuery = new MetricQuery(Metric.IMPRESSIONS, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for impressions, should have been 143 but was " + response.get(0).value, response.equals(expectedResponse));

    //Graph test
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse( 11L, 16L, 18L, 13L, 18L, 15L, 14L, 23L, 15L);

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
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse( 3L,3L,4L,5L,4L,4L,7L,14L,6L);

    MetricQuery graphQuery = new MetricQuery(Metric.CLICKS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void conversionTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(2L);

    MetricQuery statQuery = new MetricQuery(Metric.CONVERSIONS, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for conversions, should have been 2 but was " + response.get(0).value, response.equals(expectedResponse));

    //Graph test
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse( null,null,null,null,null,null,1L,1L,null);

    MetricQuery graphQuery = new MetricQuery(Metric.CONVERSIONS, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for conversions metric", weekResponse.equals(expectedWeekResponse));
  }


  @Test
  public void cpaTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(1.3947624900000002);

    MetricQuery statQuery = new MetricQuery(Metric.CPA, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPA, should have been 1.3947624900000002 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String, Number>> expectedWeekResponse = weeklyResponse( 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 27.615942, 74.506339, 0.0);
    MetricQuery graphQuery = new MetricQuery(Metric.CPA, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPA metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void cpcTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(0.05579049960000001);

    MetricQuery statQuery = new MetricQuery(Metric.CPC, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPC, should have been 0.05579049960000001 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse( 0.004115, 6.270215666666666, 3.0430004999999998, 8.1564606, 6.877498500000001, 6.43325325, 3.9451345714285715, 5.321881357142857, 8.6349855);
    MetricQuery graphQuery = new MetricQuery(Metric.CPC, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for cpc metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void cpmTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(0.01187181818181818);

    MetricQuery statQuery = new MetricQuery(Metric.CPM, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CPM, should have been 0.0187181818181818 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));


    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse( 1.122272727272727, 1.458375, 1.5614444444444442, 0.5916923076923077, 0.6040555555555556, 1.0529333333333333, 1.614142857142857, 1.5486521739130434, 0.8937333333333335);
    MetricQuery graphQuery = new MetricQuery(Metric.CPM, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CPM metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void ctrTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(0.34965034965034963);

    MetricQuery statQuery = new MetricQuery(Metric.CTR, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for CTR, should have been 0.34965034965034963 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse( 0.2727272727272727, 0.1875, 0.2222222222222222, 0.38461538461538464, 0.2222222222222222, 0.26666666666666666, 0.5, 0.6086956521739131, 0.4);
    MetricQuery graphQuery = new MetricQuery(Metric.CTR, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for CTR metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void totalCostTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(2.7895249800000005);

    MetricQuery statQuery = new MetricQuery(Metric.TOTAL_COST, null, null, null, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for total cost, should have been 2.7895249800000005 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse( 1.2345E-4, 0.18810647, 0.12172001999999998, 0.40782303, 0.27509994, 0.25733013, 0.27615942, 0.7450633900000003, 0.5180991300000001);

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
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse(3L,3L,4L,5L,4L,4L,7L,14L,6L);

    MetricQuery graphQuery = new MetricQuery(Metric.UNIQUES, Interval.WEEK, null, null, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for uniques metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void bouncesTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(24L);

    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    MetricQuery statQuery = new MetricQuery(Metric.BOUNCES, null, null, bounceConfig, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for bounces, should have been 24 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse( 2L, 3L, 2L, 2L, 2L, 2L, 3L, 5L, 3L);
    
    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCES, Interval.WEEK, null, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for bounces metric", weekResponse.equals(expectedWeekResponse));
  }


  @Test
  public void bounceRateTest () {

    //Statistic test
    List<Pair<String, Number>> expectedResponse = response(48.0);

    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    MetricQuery statQuery = new MetricQuery(Metric.BOUNCE_RATE, null, null, bounceConfig, campaign);
    List<Pair<String, Number>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for bounce rate, should have been 48.0 but was " + response.get(0).value, listsWithinMarginOfError(expectedResponse, response));

    //Graph test
    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse( 66.66666666666666, 100.0, 50.0, 40.0, 50.0, 50.0, 42.857142857142854, 35.714285714285715, 50.0);

    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCE_RATE, Interval.WEEK, null, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for bounce rate metric", listsWithinMarginOfError(expectedWeekResponse, weekResponse));
  }

  @Test
  public void dateRangeTest() throws ParseException {
    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    FilterConfig filter = new FilterConfig();
    filter.dates = new Range<>(f.parse("2014-12-29 00:00:00"), f.parse("2015-02-23 00:00:00"));
    BounceConfig bounceConfig = new BounceConfig();
    bounceConfig.type = BounceConfig.Type.PAGES;
    bounceConfig.value = 2;

    List<Pair<String,Number>> expectedWeekResponse = weeklyResponse( 66.66666666666666, 100.0, 50.0, 40.0, 50.0, 50.0, 42.857142857142854, 35.714285714285715, null);
    MetricQuery graphQuery = new MetricQuery(Metric.BOUNCE_RATE, Interval.WEEK, filter, bounceConfig, campaign);
    List<Pair<String, Number>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

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

  private List<Pair<String,Number>> weeklyResponse(Number w1, Number w2, Number w3, Number w4, Number w5, Number w6, Number w7, Number w8, Number w9) {
    List<Pair<String, Number>> expectedWeekResponse = new LinkedList<>();

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
