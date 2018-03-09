package group33.seg.view.structure;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.DisplayHandler;
import group33.seg.controller.handlers.GraphHandler;
import group33.seg.controller.handlers.StatisticHandler;
import group33.seg.view.DashboardView;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;

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

    // Use RHS as single panel
    GraphPanel pnlGraph = new GraphPanel(controller);
    sppMain.setRightComponent(pnlGraph);

  }
  
}
