package group33.seg.view.graphwizard;

import group33.seg.model.configs.LineConfig;

import javax.swing.*;
import java.awt.*;

public class LinePanel extends JScrollPane {
  private static final long serialVersionUID = 5600109072945686314L;

  protected LineDataPanel pnlLineData;
  protected LinePropertiesPanel pnlLineProperties;

  private LineConfig base;

  public LinePanel() {
    this(null);
  }

  public LinePanel(LineConfig line) {
    initGUI();
    loadLine(line);
  }

  /**
   * Initialise GUI and any event listeners.
   */
  public void initGUI() {
    JPanel pnlMain = new JPanel();
    pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlMain = new GridBagLayout();
    gbl_pnlMain.rowHeights = new int[] {0, 0, 0};
    gbl_pnlMain.columnWeights = new double[] {1.0};
    gbl_pnlMain.rowWeights = new double[] {0.0, 0.0, 1.0};
    pnlMain.setLayout(gbl_pnlMain);

    pnlLineProperties = new LinePropertiesPanel();
    GridBagConstraints gbc_pnlLineProperties = new GridBagConstraints();
    gbc_pnlLineProperties.fill = GridBagConstraints.BOTH;
    gbc_pnlLineProperties.insets = new Insets(0, 0, 5, 0);
    gbc_pnlLineProperties.gridx = 0;
    gbc_pnlLineProperties.gridy = 0;
    pnlMain.add(pnlLineProperties, gbc_pnlLineProperties);

    pnlLineData = new LineDataPanel();
    GridBagConstraints gbc_pnlLineData = new GridBagConstraints();
    gbc_pnlLineData.fill = GridBagConstraints.BOTH;
    gbc_pnlLineData.insets = new Insets(0, 0, 0, 0);
    gbc_pnlLineData.gridx = 0;
    gbc_pnlLineData.gridy = 1;
    pnlMain.add(pnlLineData, gbc_pnlLineData);

    this.setViewportView(pnlMain);
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.getVerticalScrollBar().setUnitIncrement(10);
  }

  public void loadLine(LineConfig line) {
    this.base = line;
    pnlLineProperties.loadLine(line);
    pnlLineData.loadLine(line);
  }

  public LineConfig getLine() {
    LineConfig config;
    if (base != null) {
      // Copy the uuid to identify the new instance as being a modification
      config = new LineConfig(base.uuid);
    } else {
      config = new LineConfig();
    }
    base = config;

    // Do updates
    pnlLineProperties.updateConfig(config);
    pnlLineData.updateConfig(config);
    return config;
  }

}
