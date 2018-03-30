package group33.seg.view.controls;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;

public class GraphControlsPanel extends JPanel {
  private static final long serialVersionUID = -2452627668529824145L;

  private JXDatePicker dpFromX;
  private JXDatePicker dpToX;
  private JButton btnResetXNormal;

  private JSpinner nudShiftX;
  private JSpinner nudLengthX;
  private JButton btnResetXOverlaid;

  private JComboBox cboInterval;

  private JButton btnResetY;

  /**
   * Create the panel.
   */
  public GraphControlsPanel() {

    initGUI();
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0};
    setLayout(gridBagLayout);

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // X-Axis Controls
    /////////////////////////////////////////////////////////////////////////////////////////////////

    JPanel pnlXAxis = new JPanel();
    pnlXAxis
        .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "X-Axis"));
    GridBagConstraints gbc_pnlXAxis = new GridBagConstraints();
    gbc_pnlXAxis.insets = new Insets(0, 0, 5, 0);
    gbc_pnlXAxis.fill = GridBagConstraints.BOTH;
    gbc_pnlXAxis.gridx = 0;
    gbc_pnlXAxis.gridy = 0;
    add(pnlXAxis, gbc_pnlXAxis);
    GridBagLayout gbl_pnlXAxis = new GridBagLayout();
    gbl_pnlXAxis.columnWeights = new double[] {0.0, 1.0};
    gbl_pnlXAxis.rowWeights = new double[] {1.0, 0.0};
    pnlXAxis.setLayout(gbl_pnlXAxis);

    JTabbedPane tabsXAxis = new JTabbedPane(JTabbedPane.TOP);
    GridBagConstraints gbc_tabsXAxis = new GridBagConstraints();
    gbc_tabsXAxis.gridwidth = 2;
    gbc_tabsXAxis.fill = GridBagConstraints.BOTH;
    gbc_tabsXAxis.insets = new Insets(5, 5, 5, 5);
    gbc_tabsXAxis.gridx = 0;
    gbc_tabsXAxis.gridy = 0;
    pnlXAxis.add(tabsXAxis, gbc_tabsXAxis);

    // Normal Controls
    {
      JPanel pnlNormal = new JPanel();
      tabsXAxis.addTab("Normal", null, pnlNormal, null);
      GridBagLayout gbl_pnlNormal = new GridBagLayout();
      gbl_pnlNormal.columnWeights = new double[] {0.0, 1.0};
      gbl_pnlNormal.rowWeights = new double[] {0.0, 0.0, 0.0};
      pnlNormal.setLayout(gbl_pnlNormal);

      JLabel lblFromX = new JLabel("From:");
      GridBagConstraints gbc_lblFromX = new GridBagConstraints();
      gbc_lblFromX.anchor = GridBagConstraints.EAST;
      gbc_lblFromX.insets = new Insets(5, 5, 5, 5);
      gbc_lblFromX.gridx = 0;
      gbc_lblFromX.gridy = 0;
      pnlNormal.add(lblFromX, gbc_lblFromX);

      dpFromX = new JXDatePicker();
      GridBagConstraints gbc_dpFromX = new GridBagConstraints();
      gbc_dpFromX.insets = new Insets(5, 0, 5, 5);
      gbc_dpFromX.fill = GridBagConstraints.HORIZONTAL;
      gbc_dpFromX.gridx = 1;
      gbc_dpFromX.gridy = 0;
      pnlNormal.add(dpFromX, gbc_dpFromX);

      JLabel lblToX = new JLabel("To:");
      GridBagConstraints gbc_lblToX = new GridBagConstraints();
      gbc_lblToX.insets = new Insets(0, 5, 5, 5);
      gbc_lblToX.gridx = 0;
      gbc_lblToX.gridy = 1;
      pnlNormal.add(lblToX, gbc_lblToX);

      dpToX = new JXDatePicker();
      GridBagConstraints gbc_dpToX = new GridBagConstraints();
      gbc_dpToX.insets = new Insets(0, 0, 5, 5);
      gbc_dpToX.fill = GridBagConstraints.HORIZONTAL;
      gbc_dpToX.gridx = 1;
      gbc_dpToX.gridy = 1;
      pnlNormal.add(dpToX, gbc_dpToX);

      btnResetXNormal = new JButton("Reset");
      GridBagConstraints gbc_btnResetXNormal = new GridBagConstraints();
      gbc_btnResetXNormal.anchor = GridBagConstraints.EAST;
      gbc_btnResetXNormal.insets = new Insets(0, 5, 5, 5);
      gbc_btnResetXNormal.gridx = 1;
      gbc_btnResetXNormal.gridy = 2;
      pnlNormal.add(btnResetXNormal, gbc_btnResetXNormal);
    }

    // Overlaid Controls
    {
      JPanel pnlOverlaid = new JPanel();
      tabsXAxis.addTab("Overlaid", null, pnlOverlaid, null);
      GridBagLayout gbl_pnlOverlaid = new GridBagLayout();
      gbl_pnlOverlaid.columnWeights = new double[] {0.0, 1.0};
      gbl_pnlOverlaid.rowWeights = new double[] {0.0, 0.0, 0.0};
      pnlOverlaid.setLayout(gbl_pnlOverlaid);

      JLabel lblShiftX = new JLabel("Shift:");
      GridBagConstraints gbc_lblShiftX = new GridBagConstraints();
      gbc_lblShiftX.anchor = GridBagConstraints.EAST;
      gbc_lblShiftX.insets = new Insets(5, 5, 5, 5);
      gbc_lblShiftX.gridx = 0;
      gbc_lblShiftX.gridy = 0;
      pnlOverlaid.add(lblShiftX, gbc_lblShiftX);

      nudShiftX = new JSpinner();
      GridBagConstraints gbc_nudShiftX = new GridBagConstraints();
      gbc_nudShiftX.insets = new Insets(5, 0, 5, 5);
      gbc_nudShiftX.fill = GridBagConstraints.HORIZONTAL;
      gbc_nudShiftX.gridx = 1;
      gbc_nudShiftX.gridy = 0;
      pnlOverlaid.add(nudShiftX, gbc_nudShiftX);

      JLabel lblLengthX = new JLabel("Length:");
      GridBagConstraints gbc_lblLengthX = new GridBagConstraints();
      gbc_lblLengthX.insets = new Insets(0, 5, 5, 5);
      gbc_lblLengthX.gridx = 0;
      gbc_lblLengthX.gridy = 1;
      pnlOverlaid.add(lblLengthX, gbc_lblLengthX);

      nudLengthX = new JSpinner();
      GridBagConstraints gbc_nudLengthX = new GridBagConstraints();
      gbc_nudLengthX.insets = new Insets(0, 0, 5, 5);
      gbc_nudLengthX.fill = GridBagConstraints.HORIZONTAL;
      gbc_nudLengthX.gridx = 1;
      gbc_nudLengthX.gridy = 1;
      pnlOverlaid.add(nudLengthX, gbc_nudLengthX);

      btnResetXOverlaid = new JButton("Reset");
      GridBagConstraints gbc_btnResetXOverlaid = new GridBagConstraints();
      gbc_btnResetXOverlaid.anchor = GridBagConstraints.EAST;
      gbc_btnResetXOverlaid.insets = new Insets(0, 5, 5, 5);
      gbc_btnResetXOverlaid.gridx = 1;
      gbc_btnResetXOverlaid.gridy = 2;
      pnlOverlaid.add(btnResetXOverlaid, gbc_btnResetXOverlaid);
    }

    JLabel lblInterval = new JLabel("Interval:");
    GridBagConstraints gbc_lblInterval = new GridBagConstraints();
    gbc_lblInterval.insets = new Insets(0, 5, 5, 5);
    gbc_lblInterval.anchor = GridBagConstraints.EAST;
    gbc_lblInterval.fill = GridBagConstraints.VERTICAL;
    gbc_lblInterval.gridx = 0;
    gbc_lblInterval.gridy = 1;
    pnlXAxis.add(lblInterval, gbc_lblInterval);

    // TODO: Chose type for JComboBox (String or Interval)
    cboInterval = new JComboBox();
    GridBagConstraints gbc_cboInterval = new GridBagConstraints();
    gbc_cboInterval.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboInterval.insets = new Insets(0, 0, 5, 5);
    gbc_cboInterval.gridx = 1;
    gbc_cboInterval.gridy = 1;
    pnlXAxis.add(cboInterval, gbc_cboInterval);

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Y-Axis Controls
    /////////////////////////////////////////////////////////////////////////////////////////////////

    JPanel pnlYAxis = new JPanel();
    pnlYAxis
        .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Y-Axis"));
    GridBagConstraints gbc_pnlYAxis = new GridBagConstraints();
    gbc_pnlYAxis.fill = GridBagConstraints.BOTH;
    gbc_pnlYAxis.gridx = 0;
    gbc_pnlYAxis.gridy = 1;
    add(pnlYAxis, gbc_pnlYAxis);
    GridBagLayout gbl_pnlYAxis = new GridBagLayout();
    gbl_pnlYAxis.columnWeights = new double[] {0.0, 1.0};
    gbl_pnlYAxis.rowWeights = new double[] {0.0, 0.0, 0.0};
    pnlYAxis.setLayout(gbl_pnlYAxis);

    JLabel lblMinY = new JLabel("Min:");
    GridBagConstraints gbc_lblMinY = new GridBagConstraints();
    gbc_lblMinY.insets = new Insets(5, 5, 5, 5);
    gbc_lblMinY.gridx = 0;
    gbc_lblMinY.gridy = 0;
    pnlYAxis.add(lblMinY, gbc_lblMinY);

    JSpinner spnMinY = new JSpinner();
    GridBagConstraints gbc_spnMinY = new GridBagConstraints();
    gbc_spnMinY.fill = GridBagConstraints.HORIZONTAL;
    gbc_spnMinY.insets = new Insets(5, 0, 5, 5);
    gbc_spnMinY.gridx = 1;
    gbc_spnMinY.gridy = 0;
    pnlYAxis.add(spnMinY, gbc_spnMinY);

    JLabel lblMaxY = new JLabel("Max:");
    GridBagConstraints gbc_lblMaxY = new GridBagConstraints();
    gbc_lblMaxY.insets = new Insets(0, 5, 5, 5);
    gbc_lblMaxY.gridx = 0;
    gbc_lblMaxY.gridy = 1;
    pnlYAxis.add(lblMaxY, gbc_lblMaxY);

    JSpinner spnMaxY = new JSpinner();
    GridBagConstraints gbc_spnMaxY = new GridBagConstraints();
    gbc_spnMaxY.fill = GridBagConstraints.HORIZONTAL;
    gbc_spnMaxY.insets = new Insets(0, 0, 5, 5);
    gbc_spnMaxY.gridx = 1;
    gbc_spnMaxY.gridy = 1;
    pnlYAxis.add(spnMaxY, gbc_spnMaxY);

    btnResetY = new JButton("Reset");
    GridBagConstraints gbc_btnResetY = new GridBagConstraints();
    gbc_btnResetY.insets = new Insets(0, 0, 5, 5);
    gbc_btnResetY.anchor = GridBagConstraints.EAST;
    gbc_btnResetY.gridx = 1;
    gbc_btnResetY.gridy = 2;
    pnlYAxis.add(btnResetY, gbc_btnResetY);
  }

}
