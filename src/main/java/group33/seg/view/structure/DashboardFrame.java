package group33.seg.view.structure;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import group33.seg.controller.events.GraphHandler;
import group33.seg.controller.events.StatisticHandler;
import group33.seg.controller.persistence.DashboardSettings;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.Accessibility.Appearance;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;

public class DashboardFrame extends JFrame {
  private static final long serialVersionUID = 5064629396099335312L;

  private JSplitPane sppMain;

  /** Test Main: Launch the Dashboard frame. */
  public static void main(String[] args) {
    EventQueue.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            try {
              Accessibility.setAppearance(Appearance.PLATFORM);
              Accessibility.scaleDefaultUIFontSize(1);
              // Show frame
              DashboardFrame frame = new DashboardFrame();
              frame.setVisible(true);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  /** Create the Dashboard frame. */
  public DashboardFrame() {
    this.setTitle("Ad-Dashboard");
    this.setBounds(100, 100, 1280, 720);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    double scaling =
        DashboardSettings.cur.prefs.getDouble(
            DashboardSettings.FONT_SCALING, Accessibility.DEFAULT_SCALING);
    Accessibility.scaleDefaultUIFontSize(scaling);

    initGUI();
  }

  private void initGUI() {
    // Initialise menu bar and its respective items
    JMenuBar menuBar = new DashboardMenuBar(this);
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
    ControlsPanel pnlControls = new ControlsPanel();
    pnlControls.setMinimumSize(pnlControls.getPreferredSize());
    sppMain.setLeftComponent(pnlControls);

    // Use RHS as single panel
    GraphPanel pnlGraph = new GraphPanel();
    sppMain.setRightComponent(pnlGraph);

    // Configure global event handlers
    StatisticHandler statisticHandler = new StatisticHandler();
    pnlControls.getPnlStatisticViewer().setStatisticHandler(statisticHandler);

    GraphHandler graphHandler = new GraphHandler();
    pnlControls.getPnlGraphGenerator().setGraphHandler(graphHandler);
    pnlGraph.setGraphHandler(graphHandler);
  }
}
