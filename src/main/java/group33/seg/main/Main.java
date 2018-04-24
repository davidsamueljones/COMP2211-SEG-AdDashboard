package group33.seg.main;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.model.DashboardModel;
import group33.seg.model.configs.WorkspaceConfig;
import group33.seg.model.configs.WorkspaceInstance;
import group33.seg.view.DashboardView;
import group33.seg.view.utilities.Accessibility;

import javax.swing.JOptionPane;

public class Main {

  public static void main(String[] args) {
    // Initialise a test workspace
    WorkspaceConfig workspace = new WorkspaceConfig();
    WorkspaceInstance wsi = new WorkspaceInstance("Test", "", workspace);
    
    // Create MMC components and link interactions
    DashboardModel model = new DashboardModel();
    model.setWorkspace(workspace);
    DashboardView view = new DashboardView();
    DashboardController controller = new DashboardController(model, view);
    // Ensure cross-platform appearance is enabled
    Accessibility.setAppearance(Accessibility.Appearance.PLATFORM);

    final String configuration = "config.properties";
    try {
      workspace.database = new DatabaseConfig(configuration);
      controller.database.refreshConnections(workspace.database, 10);
      // Start view's dashboard
      controller.display.openDashboard();
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(null, "Unable to open database connection configuration file, '" + configuration
              + "', check that it exists", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "Unable to create database connections, check that hostname, "
          + "port, username and password are all correct in configuration file", "Error", JOptionPane.ERROR_MESSAGE);
    }

  }

}
