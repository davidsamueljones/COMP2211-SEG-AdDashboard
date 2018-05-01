package group33.seg.view.graphwizards.histogram;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;
import group33.seg.view.controls.BounceDefinitionPanel;
import group33.seg.view.controls.FilterViewPanel;

public class HistogramDataPanel extends JPanel {
  private static final long serialVersionUID = 8629318246093199609L;

  private DashboardController controller;

  protected JComboBox<CampaignConfig> cboCampaign;
  protected JComboBox<Metric> cboMetric;
  protected FilterViewPanel pnlFilter;
  protected BounceDefinitionPanel pnlBounceRate;

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public HistogramDataPanel(DashboardController controller) {
    this.controller = controller;
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.rowHeights = new int[] {0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 1.0, 0.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    setLayout(gridBagLayout);

    JLabel lblCampaign = new JLabel("Campaign:");
    GridBagConstraints gbc_lblCampaign = new GridBagConstraints();
    gbc_lblCampaign.insets = new Insets(0, 0, 5, 5);
    gbc_lblCampaign.anchor = GridBagConstraints.EAST;
    gbc_lblCampaign.gridx = 0;
    gbc_lblCampaign.gridy = 0;
    add(lblCampaign, gbc_lblCampaign);

    cboCampaign = new JComboBox<>();
    for (CampaignConfig campaign : controller.workspace.getCampaigns()) {
      cboCampaign.addItem(campaign);
    }
    cboCampaign.setRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 1L;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        String text = "";
        if (value instanceof CampaignConfig) {
          CampaignConfig config = (CampaignConfig) value;
          text = config.name;
        }
        return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
      }
    });
    GridBagConstraints gbc_cboCampaign = new GridBagConstraints();
    gbc_cboCampaign.gridwidth = 2;
    gbc_cboCampaign.insets = new Insets(0, 0, 5, 0);
    gbc_cboCampaign.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboCampaign.gridx = 1;
    gbc_cboCampaign.gridy = 0;
    add(cboCampaign, gbc_cboCampaign);

    JLabel lblMetricType = new JLabel("Metric Type:");
    GridBagConstraints gbc_lblMetricType = new GridBagConstraints();
    gbc_lblMetricType.anchor = GridBagConstraints.EAST;
    gbc_lblMetricType.insets = new Insets(0, 0, 5, 5);
    gbc_lblMetricType.gridx = 0;
    gbc_lblMetricType.gridy = 1;
    add(lblMetricType, gbc_lblMetricType);

    cboMetric = new JComboBox<>();
    for (Metric metric : Metric.getHistogramTypes()) {
      cboMetric.addItem(metric);
    }
    GridBagConstraints gbc_cboMetric = new GridBagConstraints();
    gbc_cboMetric.insets = new Insets(0, 0, 5, 5);
    gbc_cboMetric.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboMetric.gridx = 1;
    gbc_cboMetric.gridy = 1;
    add(cboMetric, gbc_cboMetric);

    JButton btnMetricHelp = new JButton("?");
    GridBagConstraints gbc_btnMetricHelp = new GridBagConstraints();
    gbc_btnMetricHelp.insets = new Insets(0, 0, 5, 0);
    gbc_btnMetricHelp.gridx = 2;
    gbc_btnMetricHelp.gridy = 1;
    add(btnMetricHelp, gbc_btnMetricHelp);

    pnlFilter = new FilterViewPanel();
    pnlFilter.setPreferredSize(new Dimension(pnlFilter.getPreferredSize().width, 150));
    GridBagConstraints gbc_pnlFilter = new GridBagConstraints();
    gbc_pnlFilter.fill = GridBagConstraints.BOTH;
    gbc_pnlFilter.gridwidth = 3;
    gbc_pnlFilter.insets = new Insets(0, 0, 5, 0);
    gbc_pnlFilter.gridx = 0;
    gbc_pnlFilter.gridy = 2;
    add(pnlFilter, gbc_pnlFilter);

    pnlBounceRate = new BounceDefinitionPanel();
    GridBagConstraints gbc_pnlBounceRate = new GridBagConstraints();
    gbc_pnlBounceRate.fill = GridBagConstraints.BOTH;
    gbc_pnlBounceRate.gridwidth = 3;
    gbc_pnlBounceRate.insets = new Insets(0, 0, 0, 0);
    gbc_pnlBounceRate.gridx = 0;
    gbc_pnlBounceRate.gridy = 3;
    add(pnlBounceRate, gbc_pnlBounceRate);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Make relevant controls visible depending on metric
    cboMetric.addActionListener(e -> {
      // Show bounce rate definition if metric requires it
      Metric item = (Metric) cboMetric.getSelectedItem();
      pnlBounceRate.setVisible(MetricQuery.needBounceDef((Metric) cboMetric.getSelectedItem()));
      // Display appropriate help
      if (item == null) {
        btnMetricHelp.setToolTipText(null);
      } else {
        btnMetricHelp.setToolTipText("<html><p width=\"250\">" + item.definition + "</p></html>");
      }

    });

    // Show definition panel if it is not already displayed
    btnMetricHelp.addActionListener(e -> {
      controller.display.showDefinitions();
    });

  }

  /**
   * @param graph Graph configuration to load into the view object
   */
  public void loadGraph(HistogramConfig graph) {
    if (graph == null || graph.query == null) {
      clear();
      return;
    }
    cboCampaign.setSelectedItem(graph.query.campaign);
    cboMetric.setSelectedItem(graph.query.metric);
    pnlFilter.loadFilter(graph.query.filter);
    pnlBounceRate.loadDef(graph.query.bounceDef);
    pnlBounceRate.setVisible(MetricQuery.needBounceDef((Metric) cboMetric.getSelectedItem()));
  }

  /**
   * Apply reset state to the view object.
   */
  public void clear() {
    cboCampaign.setSelectedIndex(cboCampaign.getItemCount() == 1 ? 0 : -1);
    cboMetric.setSelectedIndex(cboMetric.getItemCount() == 1 ? 0 : -1);
    pnlFilter.loadFilter(null);
    pnlBounceRate.setVisible(false);
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public void updateConfig(HistogramConfig config) {
    MetricQuery query = new MetricQuery();
    query.metric = (Metric) cboMetric.getSelectedItem();
    query.campaign = (CampaignConfig) cboCampaign.getSelectedItem();
    query.filter = pnlFilter.getFilter();
    query.bounceDef = pnlBounceRate.getBounceDef();
    config.query = query;
  }

}
