package utils;

import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.tables.CampaignTable;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.model.configs.CampaignImportConfig;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

public class QueryTestUtil {
  private static final String CAMPAIGN_NAME = "queryTest";
  private static final String PATH_TO_DATASET = "src/test/resources/example-dataset/";
  private static final String PATH_CLICK_LOG = PATH_TO_DATASET + "click_log.csv";
  private static final String PATH_IMPRESSION_LOG = PATH_TO_DATASET + "impression_log.csv";
  private static final String PATH_SERVER_LOG = PATH_TO_DATASET + "server_log.csv";
  private static final String DATABASE_CREDENTIALS = "src/test/resources/test.properties";

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
}
