package group33.seg.view.graphwizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.configs.LineGraphConfig.Mode;

public class PlottingModePanel extends JPanel {
  private static final long serialVersionUID = 4139382753869737135L;

  protected JRadioButton radOverlaid;
  protected JRadioButton radNormal;

  /**
   * Initialise the simple panel, no external controllers required.
   */
  public PlottingModePanel() {
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Plotting Mode"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0};
    gridBagLayout.rowHeights = new int[] {0, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0};
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

  /**
   * @param config Configuration to load into the view object
   */
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

  /**
   * Apply reset state to the view object.
   */
  public void clear() {
    radNormal.setSelected(true);
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public void updateConfig(LineGraphConfig config) {
    if (radNormal.isSelected()) {
      config.mode = Mode.NORMAL;
    } else {
      config.mode = Mode.OVERLAY;
    }
  }

}
