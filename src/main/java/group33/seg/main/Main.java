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
    // Create MMC components and link interactions
    DashboardModel model = new DashboardModel();
    DashboardView view = new DashboardView();
    DashboardController controller = new DashboardController(model, view);
    
    // Start view's dashboard
    controller.display.openDashboard();
  }

}
