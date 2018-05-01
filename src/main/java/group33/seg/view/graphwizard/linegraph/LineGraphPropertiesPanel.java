package group33.seg.view.graphwizard.linegraph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.view.graphwizards.GeneralGraphPropertiesPanel;

public class LineGraphPropertiesPanel extends JScrollPane {
  private static final long serialVersionUID = 3622228245996396838L;

  private DashboardController controller;
  
  protected GeneralGraphPropertiesPanel pnlGeneralProperties;
  protected LineGraphModePanel pnlPlottingMode;

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public LineGraphPropertiesPanel(DashboardController controller) {
    this.controller = controller;
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    JPanel pnlMain = new JPanel();
    pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlMain = new GridBagLayout();
    gbl_pnlMain.rowHeights = new int[] {0, 0, 0, 0};
    gbl_pnlMain.columnWeights = new double[] {1.0};
    gbl_pnlMain.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    pnlMain.setLayout(gbl_pnlMain);

    pnlGeneralProperties = new GeneralGraphPropertiesPanel(controller);
    GridBagConstraints gbc_pnlGeneralProperties = new GridBagConstraints();
    gbc_pnlGeneralProperties.fill = GridBagConstraints.BOTH;
    gbc_pnlGeneralProperties.gridx = 0;
    gbc_pnlGeneralProperties.gridy = 0;
    pnlMain.add(pnlGeneralProperties, gbc_pnlGeneralProperties);

    pnlPlottingMode = new LineGraphModePanel();
    GridBagConstraints gbc_pnlPlottingMode = new GridBagConstraints();
    gbc_pnlPlottingMode.fill = GridBagConstraints.BOTH;
    gbc_pnlPlottingMode.gridx = 0;
    gbc_pnlPlottingMode.gridy = 1;
    pnlMain.add(pnlPlottingMode, gbc_pnlPlottingMode);

    this.setViewportView(pnlMain);
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.getVerticalScrollBar().setUnitIncrement(10);
  }

  /**
   * @param config Configuration to load into the view object
   */
  public void loadGraph(LineGraphConfig config) {
    pnlGeneralProperties.loadGraph(config);
    pnlPlottingMode.loadGraph(config);
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public void updateConfig(LineGraphConfig config) {
    pnlGeneralProperties.updateConfig(config);
    pnlPlottingMode.updateConfig(config);
  }

}
