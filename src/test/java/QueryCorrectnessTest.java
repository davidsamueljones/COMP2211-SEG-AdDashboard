import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.handlers.CampaignImportHandler;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.model.configs.CampaignImportConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for correctness of queries on a small set of example data
 */
@RunWith(MockitoJUnitRunner.class)
public class QueryCorrectnessTest {

  @Mock
  DatabaseConnection conn;

  @Mock
  DatabaseConfig config;

  private static final String CAMPAIGN_NAME = "queryTest";
  private static final String PATH_TO_DATASET = "src/test/resources/example-dataset/";
  private static final String PATH_CLICK_LOG = PATH_TO_DATASET + "click_log.csv";
  private static final String PATH_IMPRESSION_LOG = PATH_TO_DATASET + "impression_log.csv";
  private static final String PATH_SERVER_LOG = PATH_TO_DATASET + "server_log.csv";

  private static final String DATABASE_CREDENTIALS = "src/test/resources/test.properties";

  private DatabaseHandler databaseHandler;
  private ArrayList<Interval> intervals;

  /**
   * Initialise tables and data for test
   */
  public QueryCorrectnessTest() {
    try{
      //setup intervals
      intervals = new ArrayList<>();
      intervals.add(Interval.DAY);
      intervals.add(Interval.HOUR);
      intervals.add(Interval.MONTH);
      intervals.add(Interval.WEEK);
      intervals.add(Interval.YEAR);


      //init tables
      CampaignImportConfig importConfig = new CampaignImportConfig(CAMPAIGN_NAME, PATH_CLICK_LOG, PATH_IMPRESSION_LOG, PATH_SERVER_LOG, DATABASE_CREDENTIALS);
      CampaignImportHandler importHandler = new CampaignImportHandler(null);
      boolean importComplete = importHandler.doImport(importConfig);

      while(importHandler.isOngoing()){
        Thread.sleep(10);
      }

      //setup database handler
      if(importComplete){
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
  public void impressionsTest () {
    List<Pair<String,Integer>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 50));

    MetricQuery statQuery = new MetricQuery(Metric.IMPRESSIONS, null, null);
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for impressions, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));
  }

  @Test
  public void clicksTest() {

  }

  @Test
  public void conversionsTest() {

  }

  @Test
  public void uniquesTest() {

  }

  @Test
  public void totalCostTest() {

  }

  @Test
  public void ctrTest() {

  }

  @Test
  public void cpaTest() {

  }

  @Test
  public void cpmTest() {

  }

  @Test
  public void pagesViewedTest() {

  }

  @Test
  public void timeSpentTest() {

  }

  @Test
  public void bounceRateTest() {

  }
}