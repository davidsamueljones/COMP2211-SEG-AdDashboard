package group33.seg.view.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.LineGraphHandler;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.GraphVisitor;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.view.graphwizard.linegraph.LineGraphWizardDialog;
import group33.seg.view.graphwizards.GraphWizardInterface;
import group33.seg.view.graphwizards.WizardSelectorDialog;
import group33.seg.view.graphwizards.histogram.HistogramWizardDialog;
import group33.seg.view.utilities.ProgressDialog;

public class GraphManagerPanel extends JPanel {
  private static final long serialVersionUID = 6541885932864334941L;

  private DashboardController controller;

  private JList<GraphConfig> lstGraphs;
  private DefaultListModel<GraphConfig> mdl_lstGraphs;

  /**
   * Create the panel, loading the workspace's current graphs.
   * 
   * @param controller Controller for this view object
   */
  public GraphManagerPanel(DashboardController controller) {
    this.controller = controller;

    // Initialise the GUI
    initGUI();
    // Load the current workspace's graphs
    refreshGraphs();
  }

  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0};
    setLayout(gridBagLayout);

    JButton btnNew = new JButton("New Graph");
    GridBagConstraints gbc_btnNew = new GridBagConstraints();
    gbc_btnNew.insets = new Insets(0, 0, 5, 0);
    gbc_btnNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNew.gridx = 0;
    gbc_btnNew.gridy = 0;
    add(btnNew, gbc_btnNew);

    JPanel pnlExisting = new JPanel();
    pnlExisting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
        "Workspace Graphs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    GridBagConstraints gbc_pnlExisting = new GridBagConstraints();
    gbc_pnlExisting.fill = GridBagConstraints.BOTH;
    gbc_pnlExisting.gridx = 0;
    gbc_pnlExisting.gridy = 1;
    add(pnlExisting, gbc_pnlExisting);
    GridBagLayout gbl_pnlExisting = new GridBagLayout();
    gbl_pnlExisting.columnWeights = new double[] {1.0, 1.0};
    gbl_pnlExisting.rowWeights = new double[] {1.0, 0.0, 0.0};
    pnlExisting.setLayout(gbl_pnlExisting);

    mdl_lstGraphs = new DefaultListModel<>();
    lstGraphs = new JList<>(mdl_lstGraphs);
    lstGraphs.setCellRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 4349332453062368120L;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        Component comp =
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof GraphConfig) {
          GraphConfig config = (GraphConfig) value;
          ErrorBuilder eb = config.validate();
          if (eb.isError()) {
            setBackground(new Color(250, 128, 114));
          }
          setText(config.identifier);
          setToolTipText(String.format("<html>%s</html>", config.inText()));
        }
        return comp;
      }
    });
    JScrollPane scrGraphs = new JScrollPane(lstGraphs);
    scrGraphs.setPreferredSize(new Dimension(0, 75));
    scrGraphs.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstGraphs = new GridBagConstraints();
    gbc_lstGraphs.gridwidth = 2;
    gbc_lstGraphs.insets = new Insets(5, 5, 5, 5);
    gbc_lstGraphs.fill = GridBagConstraints.BOTH;
    gbc_lstGraphs.gridx = 0;
    gbc_lstGraphs.gridy = 0;
    pnlExisting.add(scrGraphs, gbc_lstGraphs);

    JButton btnDelete = new JButton("Delete");
    btnDelete.setEnabled(false);
    GridBagConstraints gbc_btnDelete = new GridBagConstraints();
    gbc_btnDelete.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnDelete.insets = new Insets(0, 5, 5, 2);
    gbc_btnDelete.gridx = 0;
    gbc_btnDelete.gridy = 1;
    pnlExisting.add(btnDelete, gbc_btnDelete);

    JButton btnViewModify = new JButton("View/Modify");
    btnViewModify.setEnabled(false);
    GridBagConstraints gbc_btnViewModify = new GridBagConstraints();
    gbc_btnViewModify.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnViewModify.insets = new Insets(0, 2, 5, 5);
    gbc_btnViewModify.gridx = 1;
    gbc_btnViewModify.gridy = 1;
    pnlExisting.add(btnViewModify, gbc_btnViewModify);

    JButton btnLoad = new JButton("Load");
    btnLoad.setEnabled(false);
    GridBagConstraints gbc_btnLoad = new GridBagConstraints();
    gbc_btnLoad.insets = new Insets(0, 0, 5, 0);
    gbc_btnLoad.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnLoad.gridwidth = 2;
    gbc_btnLoad.gridx = 0;
    gbc_btnLoad.gridy = 2;
    pnlExisting.add(btnLoad, gbc_btnLoad);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Allow a new graph to be created using a fresh wizard
    btnNew.addActionListener(e -> displayNewWizard());

    // Listen for changes in workspace graphs, updating list if required
    controller.workspace.addListener(type -> {
      if (type == WorkspaceListener.Type.WORKSPACE || type == WorkspaceListener.Type.GRAPHS) {
        SwingUtilities.invokeLater(() -> refreshGraphs());
      }
    });

    // Disable buttons when selection invalidates them
    lstGraphs.addListSelectionListener(e -> {
      boolean isSelection = lstGraphs.getSelectedIndex() != -1;
      btnLoad.setEnabled(isSelection);
      btnDelete.setEnabled(isSelection);
      btnViewModify.setEnabled(isSelection);
    });
    lstGraphs.setSelectedIndex(-1);

    // Delete the selected graph
    btnDelete.addActionListener(e -> {
      int res = JOptionPane.showConfirmDialog(GraphManagerPanel.this,
          "Are you sure you want to delete the selected graph?", "Delete Graph",
          JOptionPane.YES_NO_OPTION);
      if (res != JOptionPane.YES_OPTION) {
        return;
      }
      GraphConfig graph = lstGraphs.getSelectedValue();
      controller.workspace.removeGraph(graph);
      if (graph.equals(controller.workspace.getCurrentGraph())) {
        controller.workspace.setCurrentGraph(null);
      }
    });

    // Open wizard for selected graph
    btnViewModify.addActionListener(e -> displayWizard(lstGraphs.getSelectedValue(), true));

    // Load the selected graph into the view
    btnLoad.addActionListener(e -> {
      controller.workspace.setCurrentGraph(lstGraphs.getSelectedValue());
    });
  }

  /**
   * Display graph type selector, on selection load appropriate wizard.
   */
  public void displayNewWizard() {
    Window frmCurrent = SwingUtilities.getWindowAncestor(GraphManagerPanel.this);
    WizardSelectorDialog selector = new WizardSelectorDialog(frmCurrent);
    selector.setModalityType(ModalityType.APPLICATION_MODAL);
    selector.setVisible(true);
    GraphConfig config = selector.getSelectedType();
    if (config != null) {
      displayWizard(config, false);
    }
  }

  /**
   * Using the graph visitor pattern display an appropriate wizard for the given graph type. A null
   * graph type will be ignored.
   * 
   * @param config Configuration to load in wizard if indicated
   * @param load Whether to load the configuration
   */
  public void displayWizard(GraphConfig config, boolean load) {
    if (config != null) {
      // Use graph visitor pattern to open appropriate wizard
      config.accept(new GraphVisitor() {

        @Override
        public void visit(LineGraphConfig graph) {
          displayLineWizard(load ? graph : null);
        }

        @Override
        public void visit(HistogramConfig graph) {
          displayHistogramWizard(load ? graph : null);
        }

      });

    }
  }

  /**
   * Display a line graph wizard, loading the given configuration.
   * 
   * @param config Configuration to load
   */
  public void displayLineWizard(LineGraphConfig config) {
    Window frmCurrent = SwingUtilities.getWindowAncestor(GraphManagerPanel.this);
    LineGraphWizardDialog wizard = new LineGraphWizardDialog(frmCurrent, controller, config);
    wizard.setModalityType(ModalityType.APPLICATION_MODAL);
    wizard.setVisible(true);
    handleWizardOutput(wizard);
  }



  /**
   * Display a histogram wizard, loading the given configuration.
   * 
   * @param config Configuration to load
   */
  private void displayHistogramWizard(HistogramConfig config) {
    Window frmCurrent = SwingUtilities.getWindowAncestor(GraphManagerPanel.this);
    HistogramWizardDialog wizard = new HistogramWizardDialog(frmCurrent, controller, config);
    wizard.setModalityType(ModalityType.APPLICATION_MODAL);
    wizard.setVisible(true);
    handleWizardOutput(wizard);
  }

  /**
   * Get the created graph from the given wizard and handle behaviour appropriately.
   * 
   * @param wizard Wizard interface to use
   */
  public void handleWizardOutput(GraphWizardInterface<?> wizard) {
    GraphConfig newConfig = wizard.getGraph();
    // Select new graph in list
    if (newConfig != null) {
      SwingUtilities.invokeLater(() -> lstGraphs.setSelectedValue(newConfig, true));
    }
  }

  /**
   * Refresh the graphs, using the currently selected graph as the refresh object to select.
   */
  public void refreshGraphs() {
    refreshGraphs(lstGraphs.getSelectedValue());
  }

  /**
   * Fetch the workspace graphs, replacing those displayed in the list. If the given graph exists in
   * the list it is selected.
   * 
   * @param selected Graph to select
   */
  public void refreshGraphs(GraphConfig selected) {
    mdl_lstGraphs.clear();
    List<GraphConfig> workspaceGraphs = controller.workspace.getGraphs();
    if (workspaceGraphs != null) {
      for (GraphConfig graph : workspaceGraphs) {
        mdl_lstGraphs.addElement(graph);
      }
      lstGraphs.setSelectedValue(selected, true);
    } else {
      lstGraphs.setSelectedValue(null, true);
    }
  }

}
