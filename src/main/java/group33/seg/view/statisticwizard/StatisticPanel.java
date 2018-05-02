package group33.seg.view.statisticwizard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.view.controls.BounceDefinitionPanel;
import group33.seg.view.controls.FilterViewPanel;

public class StatisticPanel extends JPanel {
  private static final long serialVersionUID = 5602951595852368687L;
  
  private DashboardController controller;
  
  protected JTextField txtIdentifier;
  protected JComboBox<CampaignConfig> cboCampaign;
  protected FilterViewPanel pnlFilter;
  protected BounceDefinitionPanel pnlBounceRate;
  protected JCheckBox chckbxHideStatistic;
  
  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public StatisticPanel(DashboardController controller) {
    this.controller = controller;
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
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

    JLabel lblCampaign = new JLabel("Campaign:");
    GridBagConstraints gbc_lblCampaign = new GridBagConstraints();
    gbc_lblCampaign.insets = new Insets(0, 0, 5, 5);
    gbc_lblCampaign.anchor = GridBagConstraints.EAST;
    gbc_lblCampaign.gridx = 0;
    gbc_lblCampaign.gridy = 1;
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
    gbc_cboCampaign.gridy = 1;
    add(cboCampaign, gbc_cboCampaign);
    
    pnlFilter = new FilterViewPanel();
    pnlFilter.setPreferredSize(new Dimension(pnlFilter.getPreferredSize().width, 150));
    GridBagConstraints gbc_pnlFilter = new GridBagConstraints();
    gbc_pnlFilter.gridwidth = 2;
    gbc_pnlFilter.insets = new Insets(0, 0, 5, 0);
    gbc_pnlFilter.fill = GridBagConstraints.BOTH;
    gbc_pnlFilter.gridx = 0;
    gbc_pnlFilter.gridy = 2;
    add(pnlFilter, gbc_pnlFilter);

    pnlBounceRate = new BounceDefinitionPanel();
    GridBagConstraints gbc_pnlBounceDefinition = new GridBagConstraints();
    gbc_pnlBounceDefinition.gridwidth = 2;
    gbc_pnlBounceDefinition.insets = new Insets(0, 0, 5, 0);
    gbc_pnlBounceDefinition.fill = GridBagConstraints.BOTH;
    gbc_pnlBounceDefinition.gridx = 0;
    gbc_pnlBounceDefinition.gridy = 3;
    add(pnlBounceRate, gbc_pnlBounceDefinition);

    chckbxHideStatistic = new JCheckBox("Hide from Campaign Statistics");
    GridBagConstraints gbc_chckbxHideStatistic = new GridBagConstraints();
    gbc_chckbxHideStatistic.insets = new Insets(0, 0, 5, 0);
    gbc_chckbxHideStatistic.anchor = GridBagConstraints.WEST;
    gbc_chckbxHideStatistic.gridwidth = 2;
    gbc_chckbxHideStatistic.gridx = 0;
    gbc_chckbxHideStatistic.gridy = 4;
    add(chckbxHideStatistic, gbc_chckbxHideStatistic);
  }

  /**
   * @param statistic Statistic configuration to load into the view object
   */
  public void loadStatistic(StatisticConfig statistic) {
    if (statistic == null || statistic.query == null) {
      clear();
      return;
    }
    txtIdentifier.setText(statistic.identifier);
    cboCampaign.setSelectedItem(statistic.query.campaign);
    pnlFilter.loadFilter(statistic.query.filter);
    pnlBounceRate.loadDef(statistic.query.bounceDef);
    chckbxHideStatistic.setSelected(statistic.hide);
  }

  /**
   * Apply reset state to the view object.
   */
  private void clear() {
    txtIdentifier.setText(controller.workspace.getNextStatisticIdentifier());
    cboCampaign.setSelectedIndex(cboCampaign.getItemCount() == 1 ? 0 : -1);
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
    query.campaign = (CampaignConfig) cboCampaign.getSelectedItem();
    query.metric = null;
    query.interval = null;
    query.filter = pnlFilter.getFilter();
    query.bounceDef = pnlBounceRate.getBounceDef();
    config.query = query;
    config.hide = chckbxHideStatistic.isSelected();
    return config;
  }

}