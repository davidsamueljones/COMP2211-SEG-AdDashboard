package group33.seg.view.graphwizard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import group33.seg.model.configs.LineConfig;

public class LinePropertiesPanel extends JPanel {
  private static final long serialVersionUID = -5431944440857799069L;
  private static int DEFAULT_THICKNESS = 25;
  
  private JTextField txtIdentifier;
  private JSlider sldThickness;
  private JCheckBox chckbxHideFromPlot;
  private JPanel pnlPreview;
  
  private BasicStroke lineStroke = new BasicStroke(1);
  private Color color = Color.BLACK;

  public LinePropertiesPanel() {
    this(null);
  }

  public LinePropertiesPanel(LineConfig line) {
    initGUI();
    loadLine(line);
  }

  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Properties"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0};
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
    gbc_txtIdentifier.gridwidth = 3;
    gbc_txtIdentifier.insets = new Insets(0, 0, 5, 0);
    gbc_txtIdentifier.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtIdentifier.gridx = 1;
    gbc_txtIdentifier.gridy = 0;
    add(txtIdentifier, gbc_txtIdentifier);
    txtIdentifier.setColumns(10);

    JLabel lblThickness = new JLabel("Thickness:");
    GridBagConstraints gbc_lblThickness = new GridBagConstraints();
    gbc_lblThickness.insets = new Insets(0, 0, 5, 5);
    gbc_lblThickness.gridx = 0;
    gbc_lblThickness.gridy = 1;
    add(lblThickness, gbc_lblThickness);

    sldThickness = new JSlider();
    sldThickness.setMajorTickSpacing(100);
    sldThickness.setMinorTickSpacing(10);
    sldThickness.setPaintTicks(true);
    GridBagConstraints gbc_sldThickness = new GridBagConstraints();
    gbc_sldThickness.fill = GridBagConstraints.HORIZONTAL;
    gbc_sldThickness.insets = new Insets(0, 0, 5, 5);
    gbc_sldThickness.gridx = 1;
    gbc_sldThickness.gridy = 1;
    add(sldThickness, gbc_sldThickness);

    JButton btnSetColor = new JButton("Set Colour");
    GridBagConstraints gbc_btnSetColor = new GridBagConstraints();
    gbc_btnSetColor.insets = new Insets(0, 0, 5, 0);
    gbc_btnSetColor.gridx = 2;
    gbc_btnSetColor.gridy = 1;
    add(btnSetColor, gbc_btnSetColor);

    JLabel lblPreview = new JLabel("Preview:");
    GridBagConstraints gbc_lblPreview = new GridBagConstraints();
    gbc_lblPreview.insets = new Insets(0, 0, 5, 5);
    gbc_lblPreview.gridx = 0;
    gbc_lblPreview.gridy = 2;
    add(lblPreview, gbc_lblPreview);

    pnlPreview = new JPanel() {
      private static final long serialVersionUID = 6567928294691513381L;

      /**
       * Draws a line using current properties
       */
      @Override
      public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setStroke(lineStroke);
        g.setColor(color);
        g.setRenderingHints(
            new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        // Draw line
        g.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2);
      }
    };
    pnlPreview.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc_pnlPreview = new GridBagConstraints();
    gbc_pnlPreview.insets = new Insets(0, 0, 5, 0);
    gbc_pnlPreview.fill = GridBagConstraints.BOTH;
    gbc_pnlPreview.ipady = 20;
    gbc_pnlPreview.gridx = 1;
    gbc_pnlPreview.gridy = 2;
    add(pnlPreview, gbc_pnlPreview);


    chckbxHideFromPlot = new JCheckBox("Hide from plot");
    GridBagConstraints gbc_chckbxHideFromPlot = new GridBagConstraints();
    gbc_chckbxHideFromPlot.insets = new Insets(0, 0, 0, 5);
    gbc_chckbxHideFromPlot.anchor = GridBagConstraints.WEST;
    gbc_chckbxHideFromPlot.gridx = 1;
    gbc_chckbxHideFromPlot.gridy = 3;
    add(chckbxHideFromPlot, gbc_chckbxHideFromPlot);

    sldThickness.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        lineStroke = getLineStroke(sldThickness.getValue());
        updatePreview();
      }
    });

    btnSetColor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Use JColorChooser, null returned on cancel
        Color colour =
            JColorChooser.showDialog(null, "Line Colour Chooser", LinePropertiesPanel.this.color);
        if (colour != null) {
          LinePropertiesPanel.this.color = colour;
          updatePreview();
        }
      }
    });
  }

  public void loadLine(LineConfig line) {
    if (line == null) {
      clear();
      return;
    }
    
    txtIdentifier.setText(line.identifier);
    sldThickness.setValue(line.thickness);
    this.color = line.color;
    chckbxHideFromPlot.setSelected(line.hide);
    updatePreview();
  }
  
  public void clear() {
    txtIdentifier.setText("");
    sldThickness.setValue(DEFAULT_THICKNESS);
    this.color = Color.BLACK;
    chckbxHideFromPlot.setSelected(false);
    updatePreview();
  }
  
  private static float MIN_THICKNESS = 0.1f;
  private static float MAX_THICKNESS = 5.0f;

  private void updatePreview() {
    pnlPreview.repaint();
  }

  private static BasicStroke getLineStroke(int scale) {
    float dif = MAX_THICKNESS - MIN_THICKNESS;
    float thickness = MIN_THICKNESS + dif * scale / 100.0f;
    return new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  }

  public void updateConfig(LineConfig config) {
    config.identifier = txtIdentifier.getText();
    config.color = color;
    config.thickness = sldThickness.getValue();
    config.hide = chckbxHideFromPlot.isSelected();
  }

}
