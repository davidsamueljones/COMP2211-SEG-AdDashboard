import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileNotFoundException;
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
    List<Pair<String, Integer>> expectedResponse = response("all", 50);

    MetricQuery statQuery = new MetricQuery(Metric.IMPRESSIONS, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    assertTrue("Statistic wrong for total Number of Impressions, should have been 50 but was "
        + response.get(0).value, response.equals(expectedResponse));
  }

  @Test
  public void clicksTest() {
    List<Pair<String, Integer>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 50));

    MetricQuery statQuery = new MetricQuery(Metric.CLICKS, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for total number of clicks, should have been 50 but was "
        + response.get(0).value, response.equals(expectedResponse));
  }

  @Test
  public void conversionsTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 1);

    MetricQuery statQuery = new MetricQuery(Metric.CONVERSIONS, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for total number of conversions, should have been 1 but was "
        + response.get(0).value, response.equals(expectedResponse));
  }

  @Test
  public void uniquesTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 50);

    MetricQuery statQuery = new MetricQuery(Metric.UNIQUES, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for total number of uniques, should have been 50 but was "
        + response.get(0).value, response.equals(expectedResponse));
  }

  // FIXME the actual result should be 291.828175
  @Test
  public void totalCostTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 291);

    MetricQuery statQuery = new MetricQuery(Metric.TOTAL_COST, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for total cost, should have been 291.828175 but was "
        + response.get(0).value, response.equals(expectedResponse));
  }

  @Test
  public void ctrTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 1);

    MetricQuery statQuery = new MetricQuery(Metric.CTR, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for CTR, should have been 1 but was " + response.get(0).value,
        response.equals(expectedResponse));
  }

  @Test
  public void cpaTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 291);

    MetricQuery statQuery = new MetricQuery(Metric.CPA, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for CPA, should have been 291 but was " + response.get(0).value,
        response.equals(expectedResponse));
  }

  // FIXME the actual result should be 5.8365635
  @Test
  public void cpcTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 5);
    MetricQuery statQuery = new MetricQuery(Metric.CPC, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for CPC, should have been 1.1915 but was " + response.get(0).value,
        response.equals(expectedResponse));
  }

  // FIXME the actual result should be 1.1915
  @Test
  public void cpmTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 1);
    MetricQuery statQuery = new MetricQuery(Metric.CPM, null, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for CPM, should have been 1.1915 but was " + response.get(0).value,
        response.equals(expectedResponse));
  }

  @Test
  public void pagesViewedTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 23);

    BounceConfig b = new BounceConfig();
    b.type = BounceConfig.Type.PAGES;
    b.value = 2;
    MetricQuery statQuery = new MetricQuery(Metric.BOUNCES, null, null, b, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for bounces, should have been 23 but was " + response.get(0).value,
        response.equals(expectedResponse));
  }

  @Test
  public void timeSpentTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 3);

    BounceConfig b = new BounceConfig();
    b.type = BounceConfig.Type.TIME;
    b.value = 1;
    MetricQuery statQuery = new MetricQuery(Metric.BOUNCES, null, null, b, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue("Statistic wrong for bounces, should have been 3 but was " + response.get(0).value,
        response.equals(expectedResponse));
  }

  @Test
  public void bounceRateTest() {
    List<Pair<String, Integer>> expectedResponse = response("all", 46);

    BounceConfig b = new BounceConfig();
    b.type = BounceConfig.Type.PAGES;
    b.value = 1;
    MetricQuery statQuery = new MetricQuery(Metric.BOUNCE_RATE, null, null, b, null);
    statQuery.campaign = this.campaign;
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();

    assertTrue(
        "Statistic wrong for bounce rate, should have been 46 but was " + response.get(0).value,
        response.equals(expectedResponse));

  }

  private List<Pair<String, Integer>> response(String key, int value) {
    List<Pair<String, Integer>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>(key, value));
    return expectedResponse;
  }
}
