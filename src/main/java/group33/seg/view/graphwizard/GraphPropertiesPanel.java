package group33.seg.view.graphwizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import group33.seg.model.configs.LineGraphConfig;

public class GraphPropertiesPanel extends JScrollPane {
  private static final long serialVersionUID = 3622228245996396838L;


  private GeneralGraphPropertiesPanel pnlGeneralProperties;
  private PlottingModePanel pnlPlottingMode;
  
  public GraphPropertiesPanel() {
    this(null);
  }
  
  public GraphPropertiesPanel(LineGraphConfig config) {
    initGUI();
    loadGraph(config);
  }

  private void initGUI() {
    JPanel pnlMain = new JPanel();
    pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlMain = new GridBagLayout();
    gbl_pnlMain.rowHeights = new int[] {0, 0, 0, 0};
    gbl_pnlMain.columnWeights = new double[] {1.0};
    gbl_pnlMain.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    pnlMain.setLayout(gbl_pnlMain);

    pnlGeneralProperties = new GeneralGraphPropertiesPanel();
    GridBagConstraints gbc_pnlGeneralProperties = new GridBagConstraints();
    gbc_pnlGeneralProperties.fill = GridBagConstraints.BOTH;
    gbc_pnlGeneralProperties.gridx = 0;
    gbc_pnlGeneralProperties.gridy = 0;
    pnlMain.add(pnlGeneralProperties, gbc_pnlGeneralProperties);

    pnlPlottingMode = new PlottingModePanel();
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

  public void loadGraph(LineGraphConfig config) {
    pnlGeneralProperties.loadGraph(config);
    pnlPlottingMode.loadGraph(config);
  }

  public void updateConfig(LineGraphConfig config) {
    pnlGeneralProperties.updateConfig(config);
    pnlPlottingMode.updateConfig(config);
  }

}
