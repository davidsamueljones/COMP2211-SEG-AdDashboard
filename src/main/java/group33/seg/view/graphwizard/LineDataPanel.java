package group33.seg.view.graphwizard;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import group33.seg.model.types.Metric;
import group33.seg.view.controls.BounceDefinitionPanel;
import group33.seg.view.controls.FilterViewPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class LineDataPanel extends JPanel {
  private static final long serialVersionUID = -7116368239383924369L;

  public LineDataPanel() {
    initGUI();
  }

  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Data"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
    setLayout(gridBagLayout);
    
    JLabel lblMetricType = new JLabel("Metric Type:");
    GridBagConstraints gbc_lblMetricType = new GridBagConstraints();
    gbc_lblMetricType.anchor = GridBagConstraints.EAST;
    gbc_lblMetricType.insets = new Insets(0, 0, 5, 5);
    gbc_lblMetricType.gridx = 0;
    gbc_lblMetricType.gridy = 0;
    add(lblMetricType, gbc_lblMetricType);
    
    JComboBox<Metric> cboMetric = new JComboBox<Metric>();
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
    
    FilterViewPanel pnlFilter = new FilterViewPanel();
    pnlFilter.setPreferredSize(new Dimension(pnlFilter.getPreferredSize().width, 150));
    GridBagConstraints gbc_pnlFilter = new GridBagConstraints();
    gbc_pnlFilter.fill = GridBagConstraints.BOTH;
    gbc_pnlFilter.gridwidth = 3;
    gbc_pnlFilter.insets = new Insets(0, 0, 0, 5);
    gbc_pnlFilter.gridx = 0;
    gbc_pnlFilter.gridy = 1;
    add(pnlFilter, gbc_pnlFilter);
    
    BounceDefinitionPanel pnlBounceRate = new BounceDefinitionPanel();
    GridBagConstraints gbc_pnlBounceRate = new GridBagConstraints();
    gbc_pnlBounceRate.fill = GridBagConstraints.BOTH;
    gbc_pnlBounceRate.gridwidth = 3;
    gbc_pnlBounceRate.insets = new Insets(0, 0, 0, 0);
    gbc_pnlBounceRate.gridx = 0;
    gbc_pnlBounceRate.gridy = 2;
    add(pnlBounceRate, gbc_pnlBounceRate);
    
  }
}
