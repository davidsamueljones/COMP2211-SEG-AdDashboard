package group33.seg.view.graphwizard;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

public class GeneralGraphPropertiesPanel extends JPanel {
  public GeneralGraphPropertiesPanel() {
    initGUI();
  }

  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "General Properties"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
    setLayout(gridBagLayout);

    JLabel lblIdentifier = new JLabel("Identifier:");
    GridBagConstraints gbc_lblIdentifier = new GridBagConstraints();
    gbc_lblIdentifier.anchor = GridBagConstraints.EAST;
    gbc_lblIdentifier.insets = new Insets(0, 0, 5, 5);
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

    JLabel lblTitle = new JLabel("Title:");
    GridBagConstraints gbc_lblTitle = new GridBagConstraints();
    gbc_lblTitle.anchor = GridBagConstraints.EAST;
    gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
    gbc_lblTitle.gridx = 0;
    gbc_lblTitle.gridy = 1;
    add(lblTitle, gbc_lblTitle);

    txtTitle = new JTextField();
    GridBagConstraints gbc_txtTitle = new GridBagConstraints();
    gbc_txtTitle.insets = new Insets(0, 0, 5, 0);
    gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtTitle.gridx = 1;
    gbc_txtTitle.gridy = 1;
    add(txtTitle, gbc_txtTitle);
    txtTitle.setColumns(10);

    JLabel lblXaxisTitle = new JLabel("X-Axis Title:");
    GridBagConstraints gbc_lblXaxisTitle = new GridBagConstraints();
    gbc_lblXaxisTitle.anchor = GridBagConstraints.EAST;
    gbc_lblXaxisTitle.insets = new Insets(0, 0, 5, 5);
    gbc_lblXaxisTitle.gridx = 0;
    gbc_lblXaxisTitle.gridy = 2;
    add(lblXaxisTitle, gbc_lblXaxisTitle);

    txtXaxistitle = new JTextField();
    GridBagConstraints gbc_txtXaxistitle = new GridBagConstraints();
    gbc_txtXaxistitle.insets = new Insets(0, 0, 5, 0);
    gbc_txtXaxistitle.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtXaxistitle.gridx = 1;
    gbc_txtXaxistitle.gridy = 2;
    add(txtXaxistitle, gbc_txtXaxistitle);
    txtXaxistitle.setColumns(10);

    JLabel lblYaxisTitle = new JLabel("Y-Axis Title:");
    GridBagConstraints gbc_lblYaxisTitle = new GridBagConstraints();
    gbc_lblYaxisTitle.anchor = GridBagConstraints.EAST;
    gbc_lblYaxisTitle.insets = new Insets(0, 0, 5, 5);
    gbc_lblYaxisTitle.gridx = 0;
    gbc_lblYaxisTitle.gridy = 3;
    add(lblYaxisTitle, gbc_lblYaxisTitle);

    txtYaxistitle = new JTextField();
    GridBagConstraints gbc_txtYaxistitle = new GridBagConstraints();
    gbc_txtYaxistitle.insets = new Insets(0, 0, 5, 0);
    gbc_txtYaxistitle.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtYaxistitle.gridx = 1;
    gbc_txtYaxistitle.gridy = 3;
    add(txtYaxistitle, gbc_txtYaxistitle);
    txtYaxistitle.setColumns(10);

    JCheckBox chckbxShowLegend = new JCheckBox("Show Legend");
    GridBagConstraints gbc_chckbxShowLegend = new GridBagConstraints();
    gbc_chckbxShowLegend.anchor = GridBagConstraints.WEST;
    gbc_chckbxShowLegend.gridx = 1;
    gbc_chckbxShowLegend.gridy = 4;
    add(chckbxShowLegend, gbc_chckbxShowLegend);
  }

  private static final long serialVersionUID = -1585475807433849072L;
  private JTextField txtIdentifier;
  private JTextField txtTitle;
  private JTextField txtXaxistitle;
  private JTextField txtYaxistitle;

}
