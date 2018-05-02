package group33.seg.view.graphwizards.histogram;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import group33.seg.controller.DashboardController;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.view.graphwizards.GraphWizardInterface;

public class HistogramWizardDialog extends JDialog
    implements GraphWizardInterface<HistogramConfig> {

  private static final long serialVersionUID = -2529642040023886708L;

  private DashboardController controller;

  private HistogramPropertiesPanel pnlGraphProperties;
  private HistogramDataPanel pnlData;

  /** Last loaded (or updated) graph */
  private HistogramConfig base;

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public HistogramWizardDialog(Window parent, DashboardController controller) {
    this(parent, controller, null);
  }

  /**
   * Create the dialog, loading an initial configuration.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   * @param graph Configuration to load into view
   */
  public HistogramWizardDialog(Window parent, DashboardController controller,
      HistogramConfig graph) {
    super(parent, "Histogram Wizard");

    this.controller = controller;
    // Initialise GUI
    initGUI();
    loadGraph(graph);

    // Determine positioning
    setSize(new Dimension(800, 600));
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    JPanel pnlContent = new JPanel();
    pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setContentPane(pnlContent);
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWeights = new double[] {1.0, 0.0, 0.0};
    gbl_pnlContent.rowWeights = new double[] {1.0, 0.0};
    pnlContent.setLayout(gbl_pnlContent);

    JTabbedPane tabsProperties = new JTabbedPane(SwingConstants.TOP);
    GridBagConstraints gbc_tabsProperties = new GridBagConstraints();
    gbc_tabsProperties.gridwidth = 3;
    gbc_tabsProperties.insets = new Insets(0, 0, 5, 0);
    gbc_tabsProperties.fill = GridBagConstraints.BOTH;
    gbc_tabsProperties.gridx = 0;
    gbc_tabsProperties.gridy = 0;
    pnlContent.add(tabsProperties, gbc_tabsProperties);

    pnlGraphProperties = new HistogramPropertiesPanel(controller);
    tabsProperties.add("Graph Properties", pnlGraphProperties);

    pnlData = new HistogramDataPanel(controller);
    tabsProperties.add("Data", pnlData);

    JButton btnClose = new JButton("Close");
    GridBagConstraints gbc_btnClose = new GridBagConstraints();
    gbc_btnClose.anchor = GridBagConstraints.EAST;
    gbc_btnClose.insets = new Insets(0, 0, 0, 5);
    gbc_btnClose.gridx = 0;
    gbc_btnClose.gridy = 1;
    pnlContent.add(btnClose, gbc_btnClose);

    JButton btnApply = new JButton("Apply");
    GridBagConstraints gbc_btnApply = new GridBagConstraints();
    gbc_btnApply.insets = new Insets(0, 0, 0, 5);
    gbc_btnApply.gridx = 1;
    gbc_btnApply.gridy = 1;
    pnlContent.add(btnApply, gbc_btnApply);

    JButton btnApplyClose = new JButton("Apply & Close");
    GridBagConstraints gbc_btnApplyClose = new GridBagConstraints();
    gbc_btnApplyClose.gridx = 2;
    gbc_btnApplyClose.gridy = 1;
    pnlContent.add(btnApplyClose, gbc_btnApplyClose);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Close the wizard, do not do any updates
    btnClose.addActionListener(e -> {
      int res = JOptionPane.showConfirmDialog(HistogramWizardDialog.this,
          "Are you sure you want to close the wizard, any unapplied changes will be lost?", "Close",
          JOptionPane.YES_NO_OPTION);
      if (res != JOptionPane.YES_OPTION) {
        return;
      }
      setVisible(false);
      dispose();
    });

    // Handle apply behaviour, no other effects
    btnApply.addActionListener(e -> apply());

    // Handle apply behaviour and then close the wizard
    btnApplyClose.addActionListener(e -> {
      boolean success = apply();
      if (success) {
        setVisible(false);
        dispose();
      }
    });

  }

  @Override
  public void loadGraph(HistogramConfig graph) {
    this.base = graph;
    pnlGraphProperties.loadGraph(graph);
    pnlData.loadGraph(graph);
  }

  /**
   * Handle behaviour for cementing changes made in the graph wizard. This involves updating the
   * current workspace and displaying the graph.
   * 
   * @return Whether apply was successful
   */
  private boolean apply() {
    HistogramConfig config = makeGraphConfig();
    ErrorBuilder eb = config.validate();
    if (eb.isError()) {
      JOptionPane.showMessageDialog(null, eb.listComments("Configuration Error"),
          "Configuration Error", JOptionPane.ERROR_MESSAGE);
      return false;
    } else {
      // Reload graph in the wizard
      loadGraph(config);
      // Update graph in the workspace
      controller.workspace.putGraph(config);
      controller.workspace.setCurrentGraph(config);
      return true;
    }
  }

  /**
   * Generate a graph configuration using the current wizard configuration. The current base will be
   * used as a reference point if it exists.
   * 
   * @return Generated graph
   */
  private HistogramConfig makeGraphConfig() {
    HistogramConfig config;
    if (base != null) {
      // Copy the uuid to identify the new instance as being a modification
      config = new HistogramConfig(base.uuid);
    } else {
      config = new HistogramConfig();
    }
    // Get updated configuration from wizard
    pnlGraphProperties.updateConfig(config);
    pnlData.updateConfig(config);
    return config;
  }

  @Override
  public HistogramConfig getGraph() {
    return base;
  }

}
