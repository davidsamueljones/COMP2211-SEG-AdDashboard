package group33.seg.view.utilities;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import org.jdesktop.swingx.JXTitledSeparator;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class FontSizePanel extends JPanel {
  private static final long serialVersionUID = -2100128317422188877L;

  JXTitledSeparator lblCurrentFontSize;

  private Font unscaledFont;
  private double currentScaling = 1.0; // TODO: Load from file

  /**
   * Create the panel.
   */
  public FontSizePanel() {
    Accessibility.scaleDefaultUIFontSize(currentScaling);
    initGUI();
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0, 1.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0};
    setLayout(gridBagLayout);

    JSlider sldFontSize = new JSlider();
    sldFontSize.setPaintTicks(true);
    sldFontSize.setSnapToTicks(true);
    sldFontSize.setMinimum(50);
    sldFontSize.setMaximum(200);
    sldFontSize.setMinorTickSpacing(10);
    sldFontSize.setMajorTickSpacing(50);
    sldFontSize.setValue((int) (currentScaling * 100));
    sldFontSize.setOrientation(JScrollBar.HORIZONTAL);
    sldFontSize.setPreferredSize(new Dimension(300, sldFontSize.getPreferredSize().height));
    // sldFontSize.setMinimumSize(new Dimension(300, sldFontSize.getPreferredSize().height));
    GridBagConstraints gbc_sldFontSize = new GridBagConstraints();
    gbc_sldFontSize.fill = GridBagConstraints.HORIZONTAL;
    gbc_sldFontSize.gridwidth = 3;
    gbc_sldFontSize.insets = new Insets(0, 0, 5, 0);
    gbc_sldFontSize.gridx = 0;
    gbc_sldFontSize.gridy = 0;
    add(sldFontSize, gbc_sldFontSize);

    JLabel lblSmall = new JLabel("Abc");
    Accessibility.scaleJComponentFontSize(lblSmall, 0.5 / currentScaling);
    GridBagConstraints gbc_lblSmall = new GridBagConstraints();
    gbc_lblSmall.insets = new Insets(0, 0, 5, 0);
    gbc_lblSmall.anchor = GridBagConstraints.WEST;
    gbc_lblSmall.gridx = 0;
    gbc_lblSmall.gridy = 1;
    add(lblSmall, gbc_lblSmall);

    JLabel lblMedium = new JLabel("<html>Abc<br><i>(default)</i></html>");
    Accessibility.scaleJComponentFontSize(lblMedium, 1.0 / currentScaling);
    GridBagConstraints gbc_lblMedium = new GridBagConstraints();
    gbc_lblMedium.anchor = GridBagConstraints.WEST;
    gbc_lblMedium.insets = new Insets(0, 0, 5, 0);
    gbc_lblMedium.gridx = 1;
    gbc_lblMedium.gridy = 1;
    add(lblMedium, gbc_lblMedium);

    JLabel lblLarge = new JLabel("Abc");
    Accessibility.scaleJComponentFontSize(lblLarge, 2.0 / currentScaling);
    GridBagConstraints gbc_lblLarge = new GridBagConstraints();
    gbc_lblLarge.insets = new Insets(0, 0, 5, 0);
    gbc_lblLarge.anchor = GridBagConstraints.EAST;
    gbc_lblLarge.gridx = 2;
    gbc_lblLarge.gridy = 1;
    add(lblLarge, gbc_lblLarge);

    lblCurrentFontSize = new JXTitledSeparator();
    lblCurrentFontSize.setHorizontalAlignment(SwingConstants.CENTER);
    lblCurrentFontSize.setTitle("Current");
    GridBagConstraints gbc_lblCurrentFontSize = new GridBagConstraints();
    gbc_lblCurrentFontSize.gridwidth = 3;
    gbc_lblCurrentFontSize.insets = new Insets(0, 0, 5, 0);
    gbc_lblCurrentFontSize.fill = GridBagConstraints.BOTH;
    gbc_lblCurrentFontSize.gridx = 0;
    gbc_lblCurrentFontSize.gridy = 2;
    add(lblCurrentFontSize, gbc_lblCurrentFontSize);

    // Get unscaled font
    Font initial = lblCurrentFontSize.getFont();
    unscaledFont = Accessibility.scaleFont(initial, 1 / currentScaling);
    lblCurrentFontSize.setFont(unscaledFont);

    // Determine preferred size of displayed font size
    Accessibility.scaleJComponentFontSize(lblCurrentFontSize, Accessibility.MAX_SCALING);
    Dimension preferred = lblCurrentFontSize.getPreferredSize();
    Accessibility.scaleJComponentFontSize(lblCurrentFontSize, 1 / Accessibility.MAX_SCALING);

    // Load current scaling
    setFontScale(currentScaling);
    lblCurrentFontSize.setPreferredSize(preferred);


    sldFontSize.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        setFontScale(sldFontSize.getValue() / 100.0);
      }
    });

  }

  private void loadUnscaledFont() {

  }

  private void setFontScale(double scale) {
    lblCurrentFontSize.setTitle(String.format("Current %.2fx", scale));
    Font scaled = Accessibility.scaleFont(unscaledFont, scale);
    lblCurrentFontSize.setFont(scaled);
  }

}
