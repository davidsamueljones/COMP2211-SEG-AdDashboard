package group33.seg.view.structure;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener;
import group33.seg.view.output.GraphsView;
import group33.seg.view.output.StatisticsView;
import group33.seg.view.utilities.ProgressDialog;
import group33.seg.view.workspace.WorkspaceSelectionDialog;

public class DashboardFrame extends JFrame {
  private static final long serialVersionUID = 5064629396099335312L;

  private static final String VIEW_DASHBOARD = "VIEW_DASHBOARD";
  private static final String VIEW_NO_WORKSPACE = "NO_WORKSPACE";

  private static final String DASHBOARD_TITLE = "Ad-Dashboard";
  private DashboardController controller;

  private JPanel pnlContent;
  private CardLayout cl_pnlContent;

  private JSplitPane sppMain;
  private JSplitPane sppView;

  private ControlsPanel pnlControls;
  private GraphsView pnlGraph;
  private StatisticsView pnlStatistics;

  /** Create the Dashboard frame. */
  public DashboardFrame(DashboardController controller) {
    // Record the object's controller interface
    this.controller = controller;

    this.setBounds(100, 100, 1280, 720);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initGUI();
    initViewUpdaters();
  }

  private void initGUI() {
    // Initialise menu bar and its respective items
    JMenuBar menuBar = new DashboardMenuBar(this, controller);
    setJMenuBar(menuBar);

    cl_pnlContent = new CardLayout();
    pnlContent = new JPanel(cl_pnlContent);
    pnlContent.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(pnlContent);

    // Create panel to hide main dashboard when not fully loaded
    JPanel pnlNoWorkspace = new JPanel();
    pnlNoWorkspace.setLayout(new GridLayout());
    pnlContent.add(pnlNoWorkspace, VIEW_NO_WORKSPACE);
    GridBagLayout gbl_pnlNoWorkspace = new GridBagLayout();
    gbl_pnlNoWorkspace.columnWidths = new int[] {10, 0, 10};
    gbl_pnlNoWorkspace.rowHeights = new int[] {0, 0, 0};
    gbl_pnlNoWorkspace.columnWeights = new double[] {0.0, 1.0, 0.0};
    gbl_pnlNoWorkspace.rowWeights = new double[] {0.5, 0.0, 0.5};
    pnlNoWorkspace.setLayout(gbl_pnlNoWorkspace);

    String strNoWorkspace = "<html>There is no workspace loaded.<br>"
        + "Open a prior workspace or create a new workspace to start using the Ad-Dashboard.</html>";
    JLabel lblNoWorkspace = new JLabel(strNoWorkspace);
    lblNoWorkspace.setEnabled(false);
    GridBagConstraints gbc_lblNoWorkspace = new GridBagConstraints();
    gbc_lblNoWorkspace.gridx = 1;
    gbc_lblNoWorkspace.gridy = 1;
    pnlNoWorkspace.add(lblNoWorkspace, gbc_lblNoWorkspace);

    // Create main dashboard panel
    JPanel pnlDashboard = new JPanel();
    pnlDashboard.setLayout(new GridLayout());
    pnlContent.add(pnlDashboard, VIEW_DASHBOARD);

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

    pnlGraph = new GraphsView(controller);
    sppView.setLeftComponent(pnlGraph);

    pnlStatistics = new StatisticsView(controller);
    sppView.setRightComponent(pnlStatistics);

    EventQueue.invokeLater(() -> {
      sppView.setDividerLocation(getDefaultRHSSplit());
      // If no workspace is loaded, allow one to be selected
      if (controller.workspace.getWorkspaceName() == null) {
        WorkspaceSelectionDialog changeWorkspace = new WorkspaceSelectionDialog(this, controller);
        changeWorkspace.setModalityType(ModalityType.APPLICATION_MODAL);
        changeWorkspace.setVisible(true);
      }
    });
  }


  private void loadTitle() {
    String workspace = controller.workspace.getWorkspaceName();
    String title = DASHBOARD_TITLE;
    if (workspace != null) {
      title = String.format("%s - [%s]", title, workspace);
    }
    this.setTitle(title);
  }

  /**
   * Initialise workspace listeners that will update corresponding views depending on the workspace
   * update type.
   */
  private void initViewUpdaters() {
    // Handle workspace change behaviour
    controller.workspace.addListener(type -> {
      if (type == WorkspaceListener.Type.WORKSPACE) {
        // Update the view to reflect if a workspace has been loaded
        String workspace = controller.workspace.getWorkspaceName();
        loadTitle();
        cl_pnlContent.show(pnlContent, workspace == null ? VIEW_NO_WORKSPACE : VIEW_DASHBOARD);
        controller.database.closeConnections();
        // Update controller behaviour
        if (workspace != null) {
          boolean connections = controller.workspace.updateDatabaseConnections();
          if (!connections) {
            controller.workspace.setWorkspace(null);
            JOptionPane.showMessageDialog(null, "Unable to create database connections.\r\n"
                + "Check that the server, username and password are all correct in the workspace configuration",
                "Workspace Error", JOptionPane.ERROR_MESSAGE);
          } else {
            // Update view objects to reflect data changes
            refreshGraphs();
            refreshStatistics();
          }
        }
      }
    });

    // Update the graph view whenever there are changes in the workspace
    controller.workspace.addListener(type -> {
      boolean reload =
          type == WorkspaceListener.Type.CAMPAIGN || type == WorkspaceListener.Type.CURRENT_GRAPH;
      if (reload) {
        refreshGraphs();
      }
    });

    // Update the statistic view whenever there are changes in the workspace
    controller.workspace.addListener(type -> {
      boolean invalidate = type == WorkspaceListener.Type.CAMPAIGN;
      boolean reload = invalidate || type == WorkspaceListener.Type.STATISTICS;
      if (invalidate) {
        controller.workspace.clearStatisticCaches();
      }
      if (reload) {
        refreshStatistics();
      }
    });
  }

  private void refreshGraphs() {
    SwingUtilities.invokeLater(() -> {
      ProgressDialog progressDialog = new ProgressDialog("Loading Graph...", this, false, true);
      controller.graphs.addProgressListener(progressDialog.listener);
      controller.graphs.displayGraph(controller.workspace.getCurrentGraph());
      progressDialog.setVisible(true);
      controller.graphs.removeProgressListener(progressDialog.listener);
    });
  }

  private void refreshStatistics() {
    SwingUtilities.invokeLater(() -> {
      ProgressDialog progressDialog =
          new ProgressDialog("Loading Statistics...", this, false, true);
      controller.statistics.addProgressListener(progressDialog.listener);
      controller.statistics.loadStatistics(controller.workspace.getStatistics());
      progressDialog.setVisible(true);
      controller.statistics.removeProgressListener(progressDialog.listener);
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
