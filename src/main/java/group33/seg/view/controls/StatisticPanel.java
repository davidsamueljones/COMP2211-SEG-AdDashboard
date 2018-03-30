package group33.seg.view.controls;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class StatisticPanel extends JPanel {
  private JTextField txtIdentifier;
  private JButton btnDeleteStatistic;

  /**
   * Create the panel.
   */
  public StatisticPanel() {

    initGUI();
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0, 1.0};
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

    JPanel pnlFilterView = new FilterViewPanel();
    GridBagConstraints gbc_pnlFilterView = new GridBagConstraints();
    gbc_pnlFilterView.gridwidth = 2;
    gbc_pnlFilterView.insets = new Insets(0, 0, 5, 0);
    gbc_pnlFilterView.fill = GridBagConstraints.BOTH;
    gbc_pnlFilterView.gridx = 0;
    gbc_pnlFilterView.gridy = 1;
    add(pnlFilterView, gbc_pnlFilterView);

    JPanel pnlBounceDefinition = new BounceDefinitionPanel();
    GridBagConstraints gbc_pnlBounceDefinition = new GridBagConstraints();
    gbc_pnlBounceDefinition.gridwidth = 2;
    gbc_pnlBounceDefinition.insets = new Insets(0, 0, 5, 0);
    gbc_pnlBounceDefinition.fill = GridBagConstraints.BOTH;
    gbc_pnlBounceDefinition.gridx = 0;
    gbc_pnlBounceDefinition.gridy = 2;
    add(pnlBounceDefinition, gbc_pnlBounceDefinition);

    JCheckBox chckbxHideStatistic = new JCheckBox("Hide from Campaign Statistics");
    GridBagConstraints gbc_chckbxHideStatistic = new GridBagConstraints();
    gbc_chckbxHideStatistic.insets = new Insets(0, 0, 5, 0);
    gbc_chckbxHideStatistic.anchor = GridBagConstraints.WEST;
    gbc_chckbxHideStatistic.gridwidth = 2;
    gbc_chckbxHideStatistic.gridx = 0;
    gbc_chckbxHideStatistic.gridy = 3;
    add(chckbxHideStatistic, gbc_chckbxHideStatistic);

    btnDeleteStatistic = new JButton("Delete Statistic");
    GridBagConstraints gbc_btnDeleteStatistic = new GridBagConstraints();
    gbc_btnDeleteStatistic.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnDeleteStatistic.gridwidth = 2;
    gbc_btnDeleteStatistic.gridx = 0;
    gbc_btnDeleteStatistic.gridy = 4;
    add(btnDeleteStatistic, gbc_btnDeleteStatistic);
  }

}