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
  private ArrayDeque<Interval> intervals;

  /**
   * Initialise tables and data for test
   */
  public QueryCorrectnessTest() {
    try{
      //setup intervals
      intervals = new ArrayDeque<>();
      intervals.add(Interval.DAY);
      intervals.add(Interval.HOUR);
      intervals.add(Interval.MONTH);
      intervals.add(Interval.WEEK);
      intervals.add(Interval.YEAR);

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
  }
}