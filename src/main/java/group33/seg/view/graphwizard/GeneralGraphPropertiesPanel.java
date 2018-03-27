package group33.seg.view.graphwizard;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import group33.seg.model.configs.LineGraphConfig;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

public class GeneralGraphPropertiesPanel extends JPanel {
  private static final long serialVersionUID = -1585475807433849072L;

  private JTextField txtIdentifier;
  private JTextField txtTitle;
  private JTextField txtXAxisTitle;
  private JTextField txtYAxisTitle;
  private JCheckBox chckbxShowLegend;

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

    JLabel lblXAxisTitle = new JLabel("X-Axis Title:");
    GridBagConstraints gbc_lblXAxisTitle = new GridBagConstraints();
    gbc_lblXAxisTitle.anchor = GridBagConstraints.EAST;
    gbc_lblXAxisTitle.insets = new Insets(0, 0, 5, 5);
    gbc_lblXAxisTitle.gridx = 0;
    gbc_lblXAxisTitle.gridy = 2;
    add(lblXAxisTitle, gbc_lblXAxisTitle);

    txtXAxisTitle = new JTextField();
    GridBagConstraints gbc_txtXAxisTitle = new GridBagConstraints();
    gbc_txtXAxisTitle.insets = new Insets(0, 0, 5, 0);
    gbc_txtXAxisTitle.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtXAxisTitle.gridx = 1;
    gbc_txtXAxisTitle.gridy = 2;
    add(txtXAxisTitle, gbc_txtXAxisTitle);
    txtXAxisTitle.setColumns(10);

    JLabel lblYAxisTitle = new JLabel("Y-Axis Title:");
    GridBagConstraints gbc_lblYAxisTitle = new GridBagConstraints();
    gbc_lblYAxisTitle.anchor = GridBagConstraints.EAST;
    gbc_lblYAxisTitle.insets = new Insets(0, 0, 5, 5);
    gbc_lblYAxisTitle.gridx = 0;
    gbc_lblYAxisTitle.gridy = 3;
    add(lblYAxisTitle, gbc_lblYAxisTitle);

    txtYAxisTitle = new JTextField();
    GridBagConstraints gbc_txtYAxisTitle = new GridBagConstraints();
    gbc_txtYAxisTitle.insets = new Insets(0, 0, 5, 0);
    gbc_txtYAxisTitle.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtYAxisTitle.gridx = 1;
    gbc_txtYAxisTitle.gridy = 3;
    add(txtYAxisTitle, gbc_txtYAxisTitle);
    txtYAxisTitle.setColumns(10);

    chckbxShowLegend = new JCheckBox("Show Legend");
    GridBagConstraints gbc_chckbxShowLegend = new GridBagConstraints();
    gbc_chckbxShowLegend.anchor = GridBagConstraints.WEST;
    gbc_chckbxShowLegend.gridx = 1;
    gbc_chckbxShowLegend.gridy = 4;
    add(chckbxShowLegend, gbc_chckbxShowLegend);
  }

  public void loadGraph(LineGraphConfig config) {
    if (config == null) {
      reset();
    } else {
      txtIdentifier.setText(config.identifier);
      txtTitle.setText(config.title);
      txtXAxisTitle.setText(config.xAxisTitle);
      txtYAxisTitle.setText(config.yAxisTitle);
      chckbxShowLegend.setSelected(config.showLegend);
    }
  }

  public void reset() {
    txtIdentifier.setText("");
    txtTitle.setText("");
    txtXAxisTitle.setText("");
    txtYAxisTitle.setText("");
    chckbxShowLegend.setSelected(true);
  }

  public void updateConfig(LineGraphConfig config) {
    config.identifier = txtIdentifier.getText();
    config.title = txtTitle.getText();
    config.xAxisTitle = txtXAxisTitle.getText();
    config.yAxisTitle = txtYAxisTitle.getText();
    config.showLegend = chckbxShowLegend.isSelected();
  }

}
