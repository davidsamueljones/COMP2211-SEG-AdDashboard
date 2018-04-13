package group33.seg.view.statisticwizard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.view.controls.BounceDefinitionPanel;
import group33.seg.view.controls.FilterViewPanel;

public class StatisticPanel extends JPanel {
  private static final long serialVersionUID = 5602951595852368687L;
  
  protected JTextField txtIdentifier;
  protected JButton btnDeleteStatistic;
  protected FilterViewPanel pnlFilter;
  protected BounceDefinitionPanel pnlBounceRate;
  protected JCheckBox chckbxHideStatistic;
  
  /**
   * Create the panel.
   */
  public StatisticPanel() {
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0, 0.0, 0.0, 0.0};
    setLayout(gridBagLayout);

    JLabel lblIdentifier = new JLabel("Identifier:");
    GridBagConstraints gbc_lblIdentifier = new GridBagConstraints();
    gbc_lblIdentifier.insets = new Insets(0, 0, 5, 5);
    gbc_lblIdentifier.anchor = GridBagConstraints.EAST;
    gbc_lblIdentifier.gridx = 0;
    gbc_lblIdentifier.gridy = 0;
    add(lblIdentifier, gbc_lblIdentifier);

    txtIdentifier = new JTextField();
    GridBagConstraints gbc_txtIdentifier = new GridBagConstraints();
    gbc_txtIdentifier.insets = new Insets(0, 0, 5, 0);
    gbc_txtIdentifier.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtIdentifier.gridx = 1;
    gbc_txtIdentifier.gridy = 0;
    add(txtIdentifier, gbc_txtIdentifier);
    txtIdentifier.setColumns(10);

    pnlFilter = new FilterViewPanel();
    pnlFilter.setPreferredSize(new Dimension(pnlFilter.getPreferredSize().width, 150));
    GridBagConstraints gbc_pnlFilter = new GridBagConstraints();
    gbc_pnlFilter.gridwidth = 2;
    gbc_pnlFilter.insets = new Insets(0, 0, 5, 0);
    gbc_pnlFilter.fill = GridBagConstraints.BOTH;
    gbc_pnlFilter.gridx = 0;
    gbc_pnlFilter.gridy = 1;
    add(pnlFilter, gbc_pnlFilter);

    pnlBounceRate = new BounceDefinitionPanel();
    GridBagConstraints gbc_pnlBounceDefinition = new GridBagConstraints();
    gbc_pnlBounceDefinition.gridwidth = 2;
    gbc_pnlBounceDefinition.insets = new Insets(0, 0, 5, 0);
    gbc_pnlBounceDefinition.fill = GridBagConstraints.BOTH;
    gbc_pnlBounceDefinition.gridx = 0;
    gbc_pnlBounceDefinition.gridy = 2;
    add(pnlBounceRate, gbc_pnlBounceDefinition);

    chckbxHideStatistic = new JCheckBox("Hide from Campaign Statistics");
    GridBagConstraints gbc_chckbxHideStatistic = new GridBagConstraints();
    gbc_chckbxHideStatistic.insets = new Insets(0, 0, 5, 0);
    gbc_chckbxHideStatistic.anchor = GridBagConstraints.WEST;
    gbc_chckbxHideStatistic.gridwidth = 2;
    gbc_chckbxHideStatistic.gridx = 0;
    gbc_chckbxHideStatistic.gridy = 3;
    add(chckbxHideStatistic, gbc_chckbxHideStatistic);
  }

  /**
   * @param config Configuration to load into the view object
   */
  public void loadStatistic(StatisticConfig statistic) {
    if (statistic == null || statistic.query == null) {
      clear();
      return;
    }
    txtIdentifier.setText(statistic.identifier);
    pnlFilter.loadFilter(statistic.query.filter);
    pnlBounceRate.loadDef(statistic.query.bounceDef);
    chckbxHideStatistic.setSelected(statistic.hide);
  }

  /**
   * Apply reset state to the view object.
   */
  private void clear() {
    txtIdentifier.setText("");
    pnlFilter.loadFilter(null);
    pnlBounceRate.loadDef(null);
    chckbxHideStatistic.setSelected(false);
  }

  /**
   * Do update behaviour on a fresh statistic configuration instance.
   * 
   * @return Fully configured statistic
   */
  public StatisticConfig getStatistic() {
    return updateConfig(new StatisticConfig());
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public StatisticConfig updateConfig(StatisticConfig config) {
    config.identifier = txtIdentifier.getText();
    MetricQuery query = new MetricQuery();
    query.metric = null;
    query.interval = null;
    query.filter = pnlFilter.getFilter();
    query.bounceDef = pnlBounceRate.getBounceDef();
    config.query = query;
    config.hide = chckbxHideStatistic.isSelected();
    return config;
  }

}