package group33.seg.view.graphwizard.linegraph;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.view.output.LineGraphView;

public class GeneralGraphPropertiesPanel extends JPanel {
  private static final long serialVersionUID = -1585475807433849072L;

  protected JTextField txtIdentifier;
  protected JTextField txtTitle;
  protected JTextField txtXAxisTitle;
  protected JTextField txtYAxisTitle;
  protected JLabel lblSelectedBackgroundColour;
  protected JCheckBox chckbxShowLegend;

  /**
   * Initialise the simple panel, no external controllers required.
   */
  public GeneralGraphPropertiesPanel() {
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "General Properties"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
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

    JLabel lblBackgroundColour = new JLabel("Background Colour:");
    GridBagConstraints gbc_lblBackgroundColour = new GridBagConstraints();
    gbc_lblBackgroundColour.anchor = GridBagConstraints.EAST;
    gbc_lblBackgroundColour.insets = new Insets(0, 0, 5, 5);
    gbc_lblBackgroundColour.gridx = 0;
    gbc_lblBackgroundColour.gridy = 4;
    add(lblBackgroundColour, gbc_lblBackgroundColour);

    JPanel pnlColour = new JPanel();
    GridBagConstraints gbc_pnlColour = new GridBagConstraints();
    gbc_pnlColour.insets = new Insets(0, 0, 5, 0);
    gbc_pnlColour.fill = GridBagConstraints.BOTH;
    gbc_pnlColour.gridx = 1;
    gbc_pnlColour.gridy = 4;
    add(pnlColour, gbc_pnlColour);
    GridBagLayout gbl_pnlColour = new GridBagLayout();
    gbl_pnlColour.columnWidths = new int[] {0, 0, 0};
    gbl_pnlColour.rowHeights = new int[] {0, 0};
    gbl_pnlColour.columnWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
    gbl_pnlColour.rowWeights = new double[] {0.0, Double.MIN_VALUE};
    pnlColour.setLayout(gbl_pnlColour);

    lblSelectedBackgroundColour = new JLabel("Preview");
    lblSelectedBackgroundColour.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)));
    lblSelectedBackgroundColour.setToolTipText(
        "<html>The background colour is the main background colour of the graph.<br>"
            + "The text colour is the guideline colour.</html>");
    lblSelectedBackgroundColour.setOpaque(true);
    GridBagConstraints gbc_lblSelectedBackgroundColour = new GridBagConstraints();
    gbc_lblSelectedBackgroundColour.insets = new Insets(0, 0, 0, 5);
    gbc_lblSelectedBackgroundColour.gridx = 0;
    gbc_lblSelectedBackgroundColour.gridy = 0;
    pnlColour.add(lblSelectedBackgroundColour, gbc_lblSelectedBackgroundColour);

    JButton btnSetColor = new JButton("Set");
    GridBagConstraints gbc_btnSetColor = new GridBagConstraints();
    gbc_btnSetColor.gridx = 1;
    gbc_btnSetColor.gridy = 0;
    pnlColour.add(btnSetColor, gbc_btnSetColor);

    chckbxShowLegend = new JCheckBox("Show Legend");
    GridBagConstraints gbc_chckbxShowLegend = new GridBagConstraints();
    gbc_chckbxShowLegend.insets = new Insets(0, 0, 5, 0);
    gbc_chckbxShowLegend.anchor = GridBagConstraints.WEST;
    gbc_chckbxShowLegend.gridx = 1;
    gbc_chckbxShowLegend.gridy = 5;
    add(chckbxShowLegend, gbc_chckbxShowLegend);

    // Allow selection of a new colour
    btnSetColor.addActionListener(e -> {
      // Use JColorChooser, null returned on cancel
      Color color = JColorChooser.showDialog(null, "Line Colour Chooser",
          lblSelectedBackgroundColour.getBackground());
      if (color != null) {
        loadBackgroundColor(color);
      }
    });

  }

  /**
   * Load the given background colour into the colour handling object. This should generate an
   * appropriate preview.
   * 
   * @param color Colour to use, if null use default
   */
  private void loadBackgroundColor(Color bg) {
    if (bg == null) {
      bg = LineGraphView.DEFAULT_BACKGROUND;
    }
    Color fg = LineGraphView.getGridlineColor(bg);
    lblSelectedBackgroundColour.setBackground(bg);
    lblSelectedBackgroundColour.setForeground(fg);
  }

  /**
   * @param config Configuration to load into the view object
   */
  public void loadGraph(LineGraphConfig config) {
    if (config == null) {
      reset();
    } else {
      txtIdentifier.setText(config.identifier);
      txtTitle.setText(config.title);
      txtXAxisTitle.setText(config.xAxisTitle);
      txtYAxisTitle.setText(config.yAxisTitle);
      loadBackgroundColor(config.background);
      chckbxShowLegend.setSelected(config.showLegend);
    }
  }

  /**
   * Apply reset state to the view object.
   */
  public void reset() {
    txtIdentifier.setText("");
    txtTitle.setText("");
    txtXAxisTitle.setText("");
    txtYAxisTitle.setText("");
    loadBackgroundColor(null);
    chckbxShowLegend.setSelected(true);
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public void updateConfig(LineGraphConfig config) {
    config.identifier = txtIdentifier.getText();
    config.title = txtTitle.getText();
    config.xAxisTitle = txtXAxisTitle.getText();
    config.yAxisTitle = txtYAxisTitle.getText();
    config.background = lblSelectedBackgroundColour.getBackground();
    config.showLegend = chckbxShowLegend.isSelected();
  }

}
