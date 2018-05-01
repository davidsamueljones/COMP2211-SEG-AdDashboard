package utils;

import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.tables.CampaignTable;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.lib.Pair;
import group33.seg.model.configs.CampaignImportConfig;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class QueryTestUtil {
  private static final String CAMPAIGN_NAME = "queryTest";
  private static final String PATH_TO_DATASET = "src/test/resources/example-dataset/";
  private static final String PATH_CLICK_LOG = PATH_TO_DATASET + "click_log.csv";
  private static final String PATH_IMPRESSION_LOG = PATH_TO_DATASET + "impression_log.csv";
  private static final String PATH_SERVER_LOG = PATH_TO_DATASET + "server_log.csv";
  private static final String DATABASE_CREDENTIALS = "src/test/resources/test.properties";
  private static final double MARGIN_OF_ERROR = 0.01;


  public static DashboardController setUp() {
    try {
      // Initialise a controller for these tests (no model or view needed)
      DashboardController controller = new DashboardController(null, null);
      try {
        DatabaseConfig database = new DatabaseConfig(DATABASE_CREDENTIALS);
        controller.database.refreshConnections(database, 10);
      } catch (FileNotFoundException e) {
        System.err.println("Unable to test database connection configuration file");
      } catch (SQLException e) {
        System.err.println(
            "Unable to create database connections, check that hostname, "
                + "port, username and password are all correct in the configuration file");
      }

      // Do import using custom test configuration
      CampaignImportConfig importConfig =
          new CampaignImportConfig(
              CAMPAIGN_NAME, PATH_CLICK_LOG, PATH_IMPRESSION_LOG, PATH_SERVER_LOG);
      boolean importComplete = controller.imports.doImport(importConfig);

      while (controller.imports.isOngoing()) {
        Thread.sleep(100);
      }

      return controller;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void tearDown(DatabaseHandler databaseHandler) {
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
  private static boolean withinMarginOfError(Number expected, Number actual) {
    return expected.doubleValue() == 0.0 & actual.doubleValue() == 0.0 || Math.abs(1.0 - (actual.doubleValue() / expected.doubleValue())) < MARGIN_OF_ERROR;
  }

  /**
   * Method for comparing two lists with a margin of error
   * @param expectedList list of expected test results
   * @param actualList list of actual test results
   *
   * @return True if the lists are within margin of error
   */
  public static boolean listsWithinMarginOfError (List<Pair<String, Number>> expectedList,
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
}
