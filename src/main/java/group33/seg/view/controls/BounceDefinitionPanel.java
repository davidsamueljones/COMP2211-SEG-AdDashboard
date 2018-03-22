package group33.seg.view.controls;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSpinner;

public class BounceDefinitionPanel extends JPanel {
  private static final long serialVersionUID = -4113181723451098077L;

  /**
   * Create the panel.
   */
  public BounceDefinitionPanel() {

    initGUI();
  }

  private void initGUI() {
    // Add border to panel
    this.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bounce Definition"));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0};
    setLayout(gridBagLayout);

    ButtonGroup bgBounceRateType = new ButtonGroup();
    JRadioButton radTime = new JRadioButton("", true);
    bgBounceRateType.add(radTime);
    GridBagConstraints gbc_radTime = new GridBagConstraints();
    gbc_radTime.anchor = GridBagConstraints.WEST;
    gbc_radTime.insets = new Insets(5, 5, 5, 5);
    gbc_radTime.gridx = 0;
    gbc_radTime.gridy = 0;
    add(radTime, gbc_radTime);

    JLabel lblTime = new JLabel("Time (/s):");
    GridBagConstraints gbc_lblTime = new GridBagConstraints();
    gbc_lblTime.insets = new Insets(5, 0, 5, 5);
    gbc_lblTime.gridx = 1;
    gbc_lblTime.gridy = 0;
    add(lblTime, gbc_lblTime);

    JSpinner nudTime = new JSpinner();
    GridBagConstraints gbc_nudTime = new GridBagConstraints();
    gbc_nudTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_nudTime.insets = new Insets(5, 0, 5, 5);
    gbc_nudTime.gridx = 2;
    gbc_nudTime.gridy = 0;
    add(nudTime, gbc_nudTime);

    JRadioButton radPageCount = new JRadioButton("");
    bgBounceRateType.add(radPageCount);
    GridBagConstraints gbc_radPageCount = new GridBagConstraints();
    gbc_radPageCount.anchor = GridBagConstraints.WEST;
    gbc_radPageCount.insets = new Insets(0, 5, 0, 5);
    gbc_radPageCount.gridx = 0;
    gbc_radPageCount.gridy = 1;
    add(radPageCount, gbc_radPageCount);

    JLabel lblPageCount = new JLabel("Page Count:");
    GridBagConstraints gbc_lblPageCount = new GridBagConstraints();
    gbc_lblPageCount.insets = new Insets(0, 0, 0, 5);
    gbc_lblPageCount.gridx = 1;
    gbc_lblPageCount.gridy = 1;
    add(lblPageCount, gbc_lblPageCount);

    JSpinner nudPageCount = new JSpinner();
    GridBagConstraints gbc_nudPageCount = new GridBagConstraints();
    gbc_nudPageCount.fill = GridBagConstraints.HORIZONTAL;
    gbc_nudPageCount.insets = new Insets(0, 0, 5, 5);
    gbc_nudPageCount.gridx = 2;
    gbc_nudPageCount.gridy = 1;
    add(nudPageCount, gbc_nudPageCount);
  }

}