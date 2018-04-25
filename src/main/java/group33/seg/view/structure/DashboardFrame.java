package group33.seg.view.structure;

import java.awt.CardLayout;
import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener;
import group33.seg.view.output.GraphsView;
import group33.seg.view.output.StatisticsView;
import group33.seg.view.utilities.ProgressDialog;
import group33.seg.view.workspace.WorkspaceModifyDialog;
import group33.seg.view.workspace.WorkspaceSelectionDialog;

public class DashboardFrame extends JFrame {
  private static final long serialVersionUID = 5064629396099335312L;

  private static final String VIEW_DASHBOARD = "VIEW_DASHBOARD";
  private static final String VIEW_WORKSPACE_ERROR = "VIEW_WORKSPACE_ERROR";

  private static final String DASHBOARD_TITLE = "Ad-Dashboard";
  private DashboardController controller;

  private JPanel pnlContent;
  private CardLayout cl_pnlContent;

  private JSplitPane sppMain;
  private JSplitPane sppView;

  private ControlsPanel pnlControls;
  private GraphsView pnlGraph;
  private StatisticsView pnlStatistics;

  private JLabel lblWorkspaceError;
  private JButton btnSelectWorkspace;
  private JButton btnModifyWorkspace;


  /** 
   * Create the Dashboard frame.
   * 
   * @param controller Controller for this view object
   */
  public DashboardFrame(DashboardController controller) {
    // Record the object's controller interface
    this.controller = controller;

    this.setBounds(100, 100, 1280, 720);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initGUI();
    initViewUpdaters();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {

    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************
    // Initialise menu bar and its respective items
    JMenuBar menuBar = new DashboardMenuBar(this, controller);
    setJMenuBar(menuBar);

    cl_pnlContent = new CardLayout();
    pnlContent = new JPanel(cl_pnlContent);
    pnlContent.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(pnlContent);

    // Create panel to hide main dashboard when workspace not correctly loaded
    JPanel pnlWorkspaceError = new JPanel();
    pnlWorkspaceError.setLayout(new GridLayout());
    pnlContent.add(pnlWorkspaceError, VIEW_WORKSPACE_ERROR);
    GridBagLayout gbl_pnlWorkspaceError = new GridBagLayout();
    gbl_pnlWorkspaceError.columnWidths = new int[] {10, 0, 10};
    gbl_pnlWorkspaceError.rowHeights = new int[] {0, 0, 0, 0};
    gbl_pnlWorkspaceError.columnWeights = new double[] {0.0, 1.0, 0.0};
    gbl_pnlWorkspaceError.rowWeights = new double[] {0.5, 0.0, 0.0, 0.5};
    pnlWorkspaceError.setLayout(gbl_pnlWorkspaceError);

    lblWorkspaceError = new JLabel();
    lblWorkspaceError.setEnabled(false);
    GridBagConstraints gbc_lblWorkspaceError = new GridBagConstraints();
    gbc_lblWorkspaceError.insets = new Insets(0, 0, 5, 0);
    gbc_lblWorkspaceError.gridx = 1;
    gbc_lblWorkspaceError.gridy = 1;
    pnlWorkspaceError.add(lblWorkspaceError, gbc_lblWorkspaceError);

    JPanel pnlWorkspaceButtons = new JPanel(new FlowLayout());
    GridBagConstraints gbc_pnlWorkspaceButtons = new GridBagConstraints();
    gbc_pnlWorkspaceButtons.gridx = 1;
    gbc_pnlWorkspaceButtons.gridy = 2;
    pnlWorkspaceError.add(pnlWorkspaceButtons, gbc_pnlWorkspaceButtons);

    btnSelectWorkspace = new JButton("Change Workspace");
    pnlWorkspaceButtons.add(btnSelectWorkspace);

    btnModifyWorkspace = new JButton("Modify Workspace");
    pnlWorkspaceButtons.add(btnModifyWorkspace);

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


    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    btnModifyWorkspace.addActionListener(e -> {
      showWorkspaceModifier();
    });

    btnSelectWorkspace.addActionListener(e -> showWorkspaceSelector());

    EventQueue.invokeLater(() -> {
      sppView.setDividerLocation(getDefaultRHSSplit());

      // Refresh the workspace
      refreshWorkspace();
      // If no workspace is loaded, allow one to be selected
      if (controller.workspace.getWorkspaceName() == null) {
        showWorkspaceSelector();
      }
    });
  }

  /**
   * Show workspace selector dialog.
   */
  protected void showWorkspaceSelector() {
    WorkspaceSelectionDialog changeWorkspace = new WorkspaceSelectionDialog(this, controller);
    changeWorkspace.setModalityType(ModalityType.APPLICATION_MODAL);
    changeWorkspace.setVisible(true);
  }

  /**
   * Show workspace modifier dialog.
   */
  protected void showWorkspaceModifier() {
    WorkspaceModifyDialog modifyWorkspace = new WorkspaceModifyDialog(this, controller);
    modifyWorkspace.setModalityType(ModalityType.APPLICATION_MODAL);
    modifyWorkspace.setVisible(true);
  }

  /**
   * Initialise workspace listeners that will update corresponding views depending on the workspace
   * update type.
   */
  private void initViewUpdaters() {
    // Handle workspace change behaviour
    controller.workspace.addListener(type -> {
      if (type == WorkspaceListener.Type.WORKSPACE) {
        refreshWorkspace();
      }
    });

    // Update the graph view whenever there are changes in the workspace
    controller.workspace.addListener(type -> {
      boolean reload =
          type == WorkspaceListener.Type.CAMPAIGN || type == WorkspaceListener.Type.CURRENT_GRAPH;
      if (reload) {
        refreshGraph();
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

  /**
   * Refresh the workspace.
   */
  private void refreshWorkspace() {
    // Cleanup current workspace state
    controller.database.closeConnections();

    // Handle the workspace load behaviour
    String workspace = controller.workspace.getWorkspaceName();
    boolean loaded = workspace != null;
    boolean connections = false;
    String error = null;
    if (!loaded) {
      error = "<html>There is no workspace loaded.<br>"
          + "Open a prior workspace or create a new workspace to start using the Ad-Dashboard.</html>";
    } else {
      connections = controller.workspace.updateDatabaseConnections();
      if (!connections) {
        error = "<html>The current workspace cannot connect to the server.<br>"
            + "Check that the server, username and password are all correct in the workspace "
            + "configuration or load a different workspace.</html>";
      }
    }

    // Display relevant view depending on if workspace load was successful
    refreshTitle();
    if (!loaded || !connections) {
      lblWorkspaceError.setText(error);
      btnModifyWorkspace.setVisible(loaded);
      cl_pnlContent.show(pnlContent, VIEW_WORKSPACE_ERROR);
    } else {
      cl_pnlContent.show(pnlContent, VIEW_DASHBOARD);
      // Update view objects to reflect data changes
      refreshGraph();
      refreshStatistics();
    }
  }

  /**
   * Refresh the title of the view.
   */
  private void refreshTitle() {
    String workspace = controller.workspace.getWorkspaceName();
    String title = DASHBOARD_TITLE;
    if (workspace != null) {
      title = String.format("%s - [%s]", title, workspace);
    }
    this.setTitle(title);
  }

  /**
   * Refresh the graph displayed in the view.
   */
  private void refreshGraph() {
    SwingUtilities.invokeLater(() -> {
      ProgressDialog progressDialog = new ProgressDialog("Loading Graph...", this, false, true);
      controller.graphs.addProgressListener(progressDialog.listener);
      controller.graphs.displayGraph(controller.workspace.getCurrentGraph());
      progressDialog.setVisible(true);
      controller.graphs.removeProgressListener(progressDialog.listener);
    });
  }

  /**
   * Refresh the statistics displayed in the view.
   */
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
