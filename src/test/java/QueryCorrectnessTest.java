import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.DatabaseConnection;
import group33.seg.controller.handlers.CampaignImportHandler;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.model.configs.CampaignImportConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;

/**
 * Tests for correctness of queries on a small set of example data
 */
public class QueryCorrectnessTest {
  private static final String campaignName = "queryTest";
  private static final String path = "exampleDataset/";
  private static final String pathClickLog = path + "click_log.csv";
  private static final String pathImpressionLog = path + "impression_log.csv";
  private static final String pathServerLog = path + "server_log.csv";

  private static final String databaseCredentials = "test.properties";

  private DatabaseHandler databaseHandler;

  /**
   * Initialise tables and data for test
   */
  public QueryCorrectnessTest() {
    try{
      //get database connection
      DatabaseConfig config = new DatabaseConfig(databaseCredentials);
      Connection conn = new DatabaseConnection(config.getHost(), config.getUser(), config.getPassword()).connectDatabase();

      //init tables
      CampaignImportConfig importConfig = new CampaignImportConfig(campaignName, pathClickLog, pathImpressionLog, pathServerLog, databaseCredentials);
      CampaignImportHandler importHandler = new CampaignImportHandler(null);
      boolean importComplete = importHandler.doImport(importConfig);

      while(importHandler.isOngoing()){
        Thread.sleep(10);
      }

      //setup database handler
      if(importComplete){
        databaseHandler = new DatabaseHandler(null, databaseCredentials);
      } else {
        databaseHandler = null;
      }
    } catch (InterruptedException | FileNotFoundException e) {
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

    List<Pair<String,Integer>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 1));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 3));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 4));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 5));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 5));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 8));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 7));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 8));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 9));

    MetricQuery graphQuery = new MetricQuery(Metric.IMPRESSIONS, Interval.WEEK, null);
    List<Pair<String, Integer>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for impression metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void clicksTest () {
    List<Pair<String,Integer>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 50));

    MetricQuery statQuery = new MetricQuery(Metric.CLICKS, null, null);
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for clicks, should have been 50 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Integer>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 2));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 2));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 7));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 4));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 10));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 6));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 11));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 8));

    MetricQuery graphQuery = new MetricQuery(Metric.CLICKS, Interval.WEEK, null);
    List<Pair<String, Integer>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();
    
    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }

  @Test
  public void conversionTest () {
    List<Pair<String,Integer>> expectedResponse = new LinkedList<>();
    expectedResponse.add(new Pair<>("all", 3));

    MetricQuery statQuery = new MetricQuery(Metric.CONVERSIONS, null, null);
    List<Pair<String, Integer>> response = databaseHandler.getQueryResponse(statQuery).getResult();
    
    assertTrue("Statistic wrong for clicks, should have been 3 but was " + response.get(0).value, response.equals(expectedResponse));

    List<Pair<String,Integer>> expectedWeekResponse = new LinkedList<>();
    expectedWeekResponse.add(new Pair<>("2014-12-29 00:00:00", 1));
    expectedWeekResponse.add(new Pair<>("2015-01-05 00:00:00", 0));
    expectedWeekResponse.add(new Pair<>("2015-01-12 00:00:00", 0));
    expectedWeekResponse.add(new Pair<>("2015-01-19 00:00:00", 0));
    expectedWeekResponse.add(new Pair<>("2015-01-26 00:00:00", 0));
    expectedWeekResponse.add(new Pair<>("2015-02-02 00:00:00", 1));
    expectedWeekResponse.add(new Pair<>("2015-02-09 00:00:00", 0));
    expectedWeekResponse.add(new Pair<>("2015-02-16 00:00:00", 0));
    expectedWeekResponse.add(new Pair<>("2015-02-23 00:00:00", 1));

    MetricQuery graphQuery = new MetricQuery(Metric.CONVERSIONS, Interval.WEEK, null);
    List<Pair<String, Integer>> weekResponse = databaseHandler.getQueryResponse(graphQuery).getResult();

    assertTrue("Time interval test wrong for clicks metric", weekResponse.equals(expectedWeekResponse));
  }
}