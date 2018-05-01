package group33.seg.view.graphwizards.histogram;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.view.graphwizards.GeneralGraphPropertiesPanel;
import group33.seg.view.utilities.JDynamicScrollPane;

public class HistogramPropertiesPanel extends JScrollPane {
  private static final long serialVersionUID = -7939485328852855457L;
  
  private DashboardController controller;

  protected GeneralGraphPropertiesPanel pnlGeneralProperties;
  protected HistogramBinsPanel pnlBins;

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public HistogramPropertiesPanel(DashboardController controller) {
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
    gbl_pnlMain.rowWeights = new double[] {0.0, 0.0, 1.0};
    pnlMain.setLayout(gbl_pnlMain);
    this.setViewportView(pnlMain);
    
    pnlGeneralProperties = new GeneralGraphPropertiesPanel(controller);
    pnlGeneralProperties.setDefaults(null, "Values (binned)", "Frequency");
    pnlGeneralProperties.chckbxShowLegend.setVisible(false);
    
    GridBagConstraints gbc_pnlGeneralProperties = new GridBagConstraints();
    gbc_pnlGeneralProperties.fill = GridBagConstraints.BOTH;
    gbc_pnlGeneralProperties.gridx = 0;
    gbc_pnlGeneralProperties.gridy = 0;
    pnlMain.add(pnlGeneralProperties, gbc_pnlGeneralProperties);

    pnlBins = new HistogramBinsPanel();
    GridBagConstraints gbc_pnlBins = new GridBagConstraints();
    gbc_pnlBins.fill = GridBagConstraints.BOTH;
    gbc_pnlBins.gridx = 0;
    gbc_pnlBins.gridy = 1;
    pnlMain.add(pnlBins, gbc_pnlBins);
    
    this.getVerticalScrollBar().setUnitIncrement(10);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
  }

  /**
   * @param config Configuration to load into the view object
   */
  public void loadGraph(HistogramConfig config) {
    pnlGeneralProperties.loadGraph(config);
    pnlBins.loadGraph(config);
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public void updateConfig(HistogramConfig config) {
    pnlGeneralProperties.updateConfig(config);
    pnlBins.updateConfig(config);
  }
  
}
