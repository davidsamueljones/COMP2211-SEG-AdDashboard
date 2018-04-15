package group33.seg.view.structure;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import group33.seg.controller.DashboardController;
import group33.seg.view.output.StatisticsView;

public class DashboardFrame extends JFrame {
  private static final long serialVersionUID = 5064629396099335312L;

  private DashboardController controller;

  private JSplitPane sppMain;
  private JSplitPane sppView;

  private ControlsPanel pnlControls;
  private GraphPanel pnlGraph;
  private StatisticsView pnlStatistics;

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
    sppMain.setResizeWeight(0);
    pnlDashboard.add(sppMain);

    // Use LHS as single panel
    pnlControls = new ControlsPanel(controller);
    pnlControls.setMinimumSize(pnlControls.getPreferredSize());
    sppMain.setLeftComponent(pnlControls);

    // Split RHS into graph and table panels
    sppView = new JSplitPane();
    sppView.setResizeWeight(1);
    sppView.setOrientation(JSplitPane.VERTICAL_SPLIT);
    sppMain.setRightComponent(sppView);

    pnlGraph = new GraphPanel(controller);
    sppView.setLeftComponent(pnlGraph);

    pnlStatistics = new StatisticsView(controller);
    sppView.setRightComponent(pnlStatistics);

    EventQueue.invokeLater(() -> {
      sppView.setDividerLocation(getDefaultRHSSplit());
    });
    
  }
  
  /**
   * @param show Whether the controls should be shown.
   */
  public void showControls(boolean show) {
    boolean wasShown = pnlControls.isVisible();
    pnlControls.setVisible(show);
    if (!show || (!wasShown && show)) {
      sppMain.setDividerLocation(-1);
    }
  }

  /**
   * @param show Whether the graph view should be shown.
   */
  public void showGraph(boolean show) {
    boolean wasShown = pnlGraph.isVisible();
    pnlGraph.setVisible(show);
    if (!show || (!wasShown && show)) {
      sppView.setDividerLocation(getDefaultRHSSplit());
      sppMain.setDividerLocation(-1);
    }
  }

  /**
   * @param show Whether the statistics view should be shown.
   */
  public void showStatistics(boolean show) {
    boolean wasShown = pnlStatistics.isVisible();
    pnlStatistics.setVisible(show);
    if (!show || (!wasShown && show)) {
      sppView.setDividerLocation(getDefaultRHSSplit());
      sppMain.setDividerLocation(-1);
    }
  }
  
  /**
   * @return Default split between graph and statistic views
   */
  private int getDefaultRHSSplit() {
    return (int) (sppView.getSize().height * 0.75);
  }
  
}
