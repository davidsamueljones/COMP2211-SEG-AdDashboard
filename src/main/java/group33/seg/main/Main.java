package group33.seg.main;

import java.io.FileNotFoundException;
import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.model.DashboardModel;
import group33.seg.model.workspace.Workspace;
import group33.seg.view.DashboardView;

public class Main {

  public static void main(String[] args) {
    // Initialise a test workspace
    Workspace workspace = new Workspace("Test", "");
    try {
      workspace.database = new DatabaseConfig("config.properties");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
        
    // Create MMC components and link interactions
    DashboardModel model = new DashboardModel();
    model.setWorkspace(workspace);

    DashboardView view = new DashboardView();
    DashboardController controller = new DashboardController(model, view);
    controller.database.refreshConnections(workspace.database, 10);
    
    // Start view's dashboard
    controller.display.openDashboard();
  }
  
}
