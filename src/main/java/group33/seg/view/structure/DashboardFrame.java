package group33.seg.view.structure;

import group33.seg.controller.DashboardController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {
  private static final long serialVersionUID = 5064629396099335312L;

  private DashboardController controller;
  private JSplitPane sppMain;

  /** Create the Dashboard frame. */
  public DashboardFrame(DashboardController controller) {
    // Record the object's controller interface
    this.controller = controller;
    
    this.setTitle("Ad-Dashboard");
    this.setBounds(100, 100, 1280, 720);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initGUI();
  }

  private void initGUI() {
    // Initialise menu bar and its respective items
    JMenuBar menuBar = new DashboardMenuBar(controller);
    setJMenuBar(menuBar);

    // Use custom content pane
    JPanel pnlDashboard = new JPanel();
    pnlDashboard.setBorder(new EmptyBorder(5, 5, 5, 5));
    pnlDashboard.setLayout(new GridLayout());
    setContentPane(pnlDashboard);

    // Split panel into LHS and RHS
    sppMain = new JSplitPane();
    pnlDashboard.add(sppMain);

    // Use LHS as single panel
    ControlsPanel pnlControls = new ControlsPanel(controller);
    pnlControls.setMinimumSize(pnlControls.getPreferredSize());
    sppMain.setLeftComponent(pnlControls);
    
    // Split RHS into graph and table panels
    JSplitPane sppView = new JSplitPane();
    sppView.setResizeWeight(0.65);
    sppView.setOrientation(JSplitPane.VERTICAL_SPLIT);
    sppView.setOneTouchExpandable(true);
    sppMain.setRightComponent(sppView);

    GraphPanel pnlGraph = new GraphPanel(controller);
    sppView.setLeftComponent(pnlGraph);
    
    WorkspaceStatisticsPanel pnlStatistics = new WorkspaceStatisticsPanel(controller);
    sppView.setRightComponent(pnlStatistics);

  }
  
}
