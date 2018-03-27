package group33.seg.view.graphwizard;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JRadioButton;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.configs.LineGraphConfig.Mode;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class PlottingModePanel extends JPanel {
  private static final long serialVersionUID = 4139382753869737135L;

  private JRadioButton radOverlaid;
  private JRadioButton radNormal;
  
  public PlottingModePanel() {
    initGUI();
  }
  
  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Plotting Mode"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0};
    setLayout(gridBagLayout);
      
    ButtonGroup group = new ButtonGroup();
    radNormal = new JRadioButton("Normal");
    group.add(radNormal);
    GridBagConstraints gbc_radNormal = new GridBagConstraints();
    gbc_radNormal.insets = new Insets(0, 0, 0, 5);
    gbc_radNormal.gridx = 0;
    gbc_radNormal.gridy = 0;
    add(radNormal, gbc_radNormal);
    
    radOverlaid = new JRadioButton("Overlaid Time Period");
    group.add(radOverlaid);
    GridBagConstraints gbc_radOverlaid = new GridBagConstraints();
    gbc_radOverlaid.gridx = 1;
    gbc_radOverlaid.gridy = 0;
    add(radOverlaid, gbc_radOverlaid);
  }
 
  public void loadGraph(LineGraphConfig config) {
    if (config == null) {
      clear();
      return;
    }
    
    switch (config.mode) {
      case NORMAL:
        radNormal.setSelected(true);
        break;
      case OVERLAY:
        radOverlaid.setSelected(true);
        break;
    }

  }
  
  public void clear() {
    radNormal.setSelected(true);
  }

  public void updateConfig(LineGraphConfig config) {
    if (radNormal.isSelected()) {
      config.mode = Mode.NORMAL;
    } else {
      config.mode = Mode.OVERLAY;
    }
  }
  
}
