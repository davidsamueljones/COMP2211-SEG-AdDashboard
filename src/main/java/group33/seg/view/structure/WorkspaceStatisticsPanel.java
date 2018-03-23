package group33.seg.view.structure;

import javax.swing.JPanel;
import group33.seg.controller.DashboardController;

public class WorkspaceStatisticsPanel extends JPanel {
  private static final long serialVersionUID = 7755954237883396302L;

  private DashboardController controller;

  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public WorkspaceStatisticsPanel(DashboardController controller) {
    this.controller = controller;

    initGUI();
  }

  private void initGUI() {

  }

}
