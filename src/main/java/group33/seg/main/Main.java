package group33.seg.main;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.model.DashboardModel;
import group33.seg.model.workspace.Workspace;
import group33.seg.view.DashboardView;

public class Main {

  public static void main(String[] args) {
    // Initialise a test workspace
    Workspace workspace = new Workspace("Test", "");

    // Create MMC components and link interactions
    DashboardModel model = new DashboardModel();
    model.setWorkspace(workspace);

    DashboardView view = new DashboardView();
    DashboardController controller = new DashboardController(model, view);
   
    final String configuration = "config.properties";
    try {
      workspace.database = new DatabaseConfig(configuration);
      controller.database.refreshConnections(workspace.database, 10);
    } catch (FileNotFoundException e) {
      System.err.println("Unable to open database connection configuration file, '" + configuration 
          + "', check that it exists");
    } catch (SQLException e) {
      System.err.println("Unable to create database connections, check that hostname, "
          + "port, username and password are all correct in configuration file");
    }

    // Start view's dashboard
    controller.display.openDashboard();
  }

}
