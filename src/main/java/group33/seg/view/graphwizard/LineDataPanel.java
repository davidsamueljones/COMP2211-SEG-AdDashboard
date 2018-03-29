package group33.seg.view.graphwizard;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.view.controls.BounceDefinitionPanel;
import group33.seg.view.controls.FilterViewPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class LineDataPanel extends JPanel {
  private static final long serialVersionUID = -7116368239383924369L;

  protected JComboBox<Metric> cboMetric;
  protected JComboBox<Interval> cboInterval;
  protected FilterViewPanel pnlFilter;
  protected BounceDefinitionPanel pnlBounceRate;

  /**
   * Initialise the panel.
   */
  public LineDataPanel() {
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
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0};
    setLayout(gridBagLayout);

    JLabel lblMetricType = new JLabel("Metric Type:");
    GridBagConstraints gbc_lblMetricType = new GridBagConstraints();
    gbc_lblMetricType.anchor = GridBagConstraints.EAST;
    gbc_lblMetricType.insets = new Insets(0, 0, 5, 5);
    gbc_lblMetricType.gridx = 0;
    gbc_lblMetricType.gridy = 0;
    add(lblMetricType, gbc_lblMetricType);

    cboMetric = new JComboBox<Metric>();
    for (Metric metric : Metric.values()) {
      cboMetric.addItem(metric);
    }
    GridBagConstraints gbc_cboMetric = new GridBagConstraints();
    gbc_cboMetric.insets = new Insets(0, 0, 5, 5);
    gbc_cboMetric.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboMetric.gridx = 1;
    gbc_cboMetric.gridy = 0;
    add(cboMetric, gbc_cboMetric);

    JButton btnMetricHelp = new JButton("?");
    GridBagConstraints gbc_btnMetricHelp = new GridBagConstraints();
    gbc_btnMetricHelp.insets = new Insets(0, 0, 5, 0);
    gbc_btnMetricHelp.gridx = 2;
    gbc_btnMetricHelp.gridy = 0;
    add(btnMetricHelp, gbc_btnMetricHelp);

    JLabel lblInterval = new JLabel("Interval:");
    GridBagConstraints gbc_lblInterval = new GridBagConstraints();
    gbc_lblInterval.insets = new Insets(0, 0, 5, 5);
    gbc_lblInterval.anchor = GridBagConstraints.EAST;
    gbc_lblInterval.fill = GridBagConstraints.VERTICAL;
    gbc_lblInterval.gridx = 0;
    gbc_lblInterval.gridy = 1;
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
    gbc_cboInterval.gridy = 1;
    add(cboInterval, gbc_cboInterval);

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
    cboMetric.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        pnlBounceRate.setVisible(Metric.BOUNCE_RATE.equals(cboMetric.getSelectedItem()));
      }
    });

  }

  /**
   * @param config Configuration to load into the view object
   */
  public void loadLine(LineConfig line) {
    if (line == null || line.query == null) {
      clear();
      return;
    }
    cboMetric.setSelectedItem(line.query.metric);
    cboInterval.setSelectedItem(line.query.interval);
    pnlFilter.loadFilter(line.query.filter);
    pnlBounceRate.loadDef(line.query.bounceDef);
    pnlBounceRate.setVisible(Metric.BOUNCE_RATE.equals(line.query.metric));
  }

  /**
   * Apply reset state to the view object.
   */
  public void clear() {
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
    query.filter = pnlFilter.getFilter();
    query.bounceDef = pnlBounceRate.getBounceDef();
    config.query = query;
  }

}
