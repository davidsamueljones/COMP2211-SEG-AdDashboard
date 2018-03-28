package group33.seg.main;

import group33.seg.controller.DashboardController;
import group33.seg.model.DashboardModel;
import group33.seg.model.workspace.Workspace;
import group33.seg.view.DashboardView;

public class Main {

  public static void main(String[] args) {
    
    // Create MMC components and link interactions
    DashboardModel model = new DashboardModel();
    model.setWorkspace(new Workspace("", ""));
    DashboardView view = new DashboardView();
    DashboardController controller = new DashboardController(model, view);
    
    // Start view's dashboard
    controller.display.openDashboard();
  }
  
}
