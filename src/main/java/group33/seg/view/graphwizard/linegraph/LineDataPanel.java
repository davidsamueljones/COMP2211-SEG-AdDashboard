package group33.seg.view.graphwizard.linegraph;

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
import javax.swing.ListCellRenderer;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.view.controls.BounceDefinitionPanel;
import group33.seg.view.controls.FilterViewPanel;

public class LineDataPanel extends JPanel {
  private static final long serialVersionUID = -7116368239383924369L;

  private DashboardController controller;

  protected JComboBox<CampaignConfig> cboCampaign;
  protected JComboBox<Metric> cboMetric;
  protected JComboBox<Interval> cboInterval;
  protected FilterViewPanel pnlFilter;
  protected BounceDefinitionPanel pnlBounceRate;

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public LineDataPanel(DashboardController controller) {
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

    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Data"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 1.0, 0.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0};
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
    for (Metric metric : Metric.getLineGraphTypes()) {
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

    JLabel lblInterval = new JLabel("Interval:");
    GridBagConstraints gbc_lblInterval = new GridBagConstraints();
    gbc_lblInterval.insets = new Insets(0, 0, 5, 5);
    gbc_lblInterval.anchor = GridBagConstraints.EAST;
    gbc_lblInterval.fill = GridBagConstraints.VERTICAL;
    gbc_lblInterval.gridx = 0;
    gbc_lblInterval.gridy = 2;
    add(lblInterval, gbc_lblInterval);

    cboInterval = new JComboBox<>();
    for (Interval interval : Interval.values()) {
      cboInterval.addItem(interval);
    }
    GridBagConstraints gbc_cboInterval = new GridBagConstraints();
    gbc_cboInterval.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboInterval.gridwidth = 2;
    gbc_cboInterval.insets = new Insets(0, 0, 5, 0);
    gbc_cboInterval.gridx = 1;
    gbc_cboInterval.gridy = 2;
    add(cboInterval, gbc_cboInterval);

    pnlFilter = new FilterViewPanel();
    pnlFilter.setPreferredSize(new Dimension(pnlFilter.getPreferredSize().width, 150));
    GridBagConstraints gbc_pnlFilter = new GridBagConstraints();
    gbc_pnlFilter.fill = GridBagConstraints.BOTH;
    gbc_pnlFilter.gridwidth = 3;
    gbc_pnlFilter.insets = new Insets(0, 0, 5, 0);
    gbc_pnlFilter.gridx = 0;
    gbc_pnlFilter.gridy = 3;
    add(pnlFilter, gbc_pnlFilter);

    pnlBounceRate = new BounceDefinitionPanel();
    GridBagConstraints gbc_pnlBounceRate = new GridBagConstraints();
    gbc_pnlBounceRate.fill = GridBagConstraints.BOTH;
    gbc_pnlBounceRate.gridwidth = 3;
    gbc_pnlBounceRate.insets = new Insets(0, 0, 0, 0);
    gbc_pnlBounceRate.gridx = 0;
    gbc_pnlBounceRate.gridy = 4;
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
   * @param line Line configuration to load into the view object
   */
  public void loadLine(LineConfig line) {
    if (line == null || line.query == null) {
      clear();
      return;
    }
    cboCampaign.setSelectedItem(line.query.campaign);
    cboMetric.setSelectedItem(line.query.metric);
    cboInterval.setSelectedItem(line.query.interval);
    pnlFilter.loadFilter(line.query.filter);
    pnlBounceRate.loadDef(line.query.bounceDef);
    pnlBounceRate.setVisible(MetricQuery.needBounceDef((Metric) cboMetric.getSelectedItem()));
  }

  /**
   * Apply reset state to the view object.
   */
  public void clear() {
    cboCampaign.setSelectedIndex(cboCampaign.getItemCount() == 1 ? 0 : -1);
    cboMetric.setSelectedItem(null);
    cboInterval.setSelectedItem(null);
    pnlFilter.loadFilter(null);
    pnlBounceRate.setVisible(false);
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public void updateConfig(LineConfig config) {
    MetricQuery query = new MetricQuery();
    query.metric = (Metric) cboMetric.getSelectedItem();
    query.interval = (Interval) cboInterval.getSelectedItem();
    query.campaign = (CampaignConfig) cboCampaign.getSelectedItem();
    query.filter = pnlFilter.getFilter();
    query.bounceDef = pnlBounceRate.getBounceDef();
    config.query = query;
  }

}
