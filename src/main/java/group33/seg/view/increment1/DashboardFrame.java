package group33.seg.view.increment1;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.Accessibility.Appearance;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;


public class DashboardFrame extends JFrame {
  private static final long serialVersionUID = 5064629396099335312L;
  public int CMD_MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

  /**
   * Test Main: Launch the Dashboard frame.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
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

  /**
   * Create the Dashboard frame.
   */
  public DashboardFrame() {

    this.setTitle("Ad-Dashboard");
    this.setBounds(100, 100, 1280, 720);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initGUI();
  }

  private void initGUI() {
    // Initialise menu bar and its respective items
    JMenuBar menuBar = new DashboardMenuBar();
    setJMenuBar(menuBar);

    // Use custom content pane
    JPanel pnlDashboard = new JPanel();
    pnlDashboard.setBorder(new EmptyBorder(5, 5, 5, 5));
    pnlDashboard.setLayout(new GridLayout());
    setContentPane(pnlDashboard);

    // Split panel into LHS and RHS
    JSplitPane sppMain = new JSplitPane();
    pnlDashboard.add(sppMain);

    // Use LHS as single panel
    ControlsPanel pnlControls = new ControlsPanel();
    pnlControls.setMinimumSize(pnlControls.getPreferredSize());
    sppMain.setLeftComponent(pnlControls);

    // Use RHS as single panel
    GraphPanel pnlGraph = new GraphPanel();
    sppMain.setRightComponent(pnlGraph);

  }

}
