package group33.seg.view.utilities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import group33.seg.controller.events.CampaignImportHandler;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.view.campaignimport.CampaignImportPanel;
import group33.seg.view.utilities.Accessibility;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class PreferencesDialog extends JDialog {
  private static final long serialVersionUID = -8083386947121993055L;
  private JButton btnApplyConfirm;
  private JButton btnCancel;

  /** Create the dialog. */
  public PreferencesDialog() {
    this(null);
  }

  /**
   * Create the dialog with a given parent window.
   *
   * @param parent Window to treat as a parent
   */
  public PreferencesDialog(Window parent) {
    super(parent, "Preferences");

    // Determine positioning
    Point loc;
    if (parent != null) {
      loc = parent.getLocation();
      loc.x += 80;
      loc.y += 80;
    } else {
      loc = new Point(100, 100);
    }

    // Initialise GUI
    initGUI();
    setLocation(loc);
    pack();
    setResizable(false);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  /** Initialise GUI and any event listeners. */
  private void initGUI() {

    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    JPanel pnlDialog = new JPanel();
    pnlDialog.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlDialog = new GridBagLayout();
    gbl_pnlDialog.columnWeights = new double[] {1.0, 1.0};
    gbl_pnlDialog.rowWeights = new double[] {1.0, 1.0, 0.0};
    pnlDialog.setLayout(gbl_pnlDialog);
    setContentPane(pnlDialog);

    JPanel pnlAccessibility = new JPanel();
    pnlAccessibility.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Accessibility"));
    GridBagConstraints gbc_pnlAccessibility = new GridBagConstraints();
    gbc_pnlAccessibility.gridwidth = 2;
    gbc_pnlAccessibility.insets = new Insets(0, 0, 5, 0);
    gbc_pnlAccessibility.fill = GridBagConstraints.BOTH;
    gbc_pnlAccessibility.gridx = 0;
    gbc_pnlAccessibility.gridy = 0;
    pnlDialog.add(pnlAccessibility, gbc_pnlAccessibility);
    GridBagLayout gbl_pnlAccessibility = new GridBagLayout();
    gbl_pnlAccessibility.columnWidths = new int[] {0, 0};
    gbl_pnlAccessibility.rowHeights = new int[] {0, 0};
    gbl_pnlAccessibility.columnWeights = new double[] {1.0};
    gbl_pnlAccessibility.rowWeights = new double[] {0.0, 1.0};
    pnlAccessibility.setLayout(gbl_pnlAccessibility);

    FontSizePanel pnlFontSize = new FontSizePanel();
    pnlFontSize.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Font Size"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagConstraints gbc_pnlFontSize = new GridBagConstraints();
    gbc_pnlFontSize.insets = new Insets(5, 5, 5, 5);
    gbc_pnlFontSize.fill = GridBagConstraints.BOTH;
    gbc_pnlFontSize.gridx = 0;
    gbc_pnlFontSize.gridy = 0;
    pnlAccessibility.add(pnlFontSize, gbc_pnlFontSize);

    JPanel pnlOther = new JPanel();
    pnlOther.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Other"));
    GridBagConstraints gbc_pnlOther = new GridBagConstraints();
    gbc_pnlOther.anchor = GridBagConstraints.NORTHWEST;
    gbc_pnlOther.gridwidth = 2;
    gbc_pnlOther.insets = new Insets(0, 0, 5, 0);
    gbc_pnlOther.fill = GridBagConstraints.BOTH;
    gbc_pnlOther.gridx = 0;
    gbc_pnlOther.gridy = 1;
    pnlDialog.add(pnlOther, gbc_pnlOther);

    JLabel lblNA = new JLabel("N/A");
    lblNA.setEnabled(false);
    pnlOther.add(lblNA);

    btnCancel = new JButton("Cancel");
    GridBagConstraints gbc_btnCancel = new GridBagConstraints();
    gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
    gbc_btnCancel.gridx = 0;
    gbc_btnCancel.gridy = 2;
    pnlDialog.add(btnCancel, gbc_btnCancel);

    btnApplyConfirm = new JButton("Apply & Confirm");
    GridBagConstraints gbc_btnApplyConfirm = new GridBagConstraints();
    gbc_btnApplyConfirm.fill = GridBagConstraints.BOTH;
    gbc_btnApplyConfirm.insets = new Insets(0, 5, 0, 0);
    gbc_btnApplyConfirm.gridx = 1;
    gbc_btnApplyConfirm.gridy = 2;
    pnlDialog.add(btnApplyConfirm, gbc_btnApplyConfirm);

    btnCancel.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
          }
        });

    btnApplyConfirm.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            if (pnlFontSize.hasChanged()) {
              int res =
                  JOptionPane.showConfirmDialog(
                      PreferencesDialog.this,
                      "Updating the font size requires any open windows to refresh\r\n"
                          + "This may clear any workspaces, are you sure you wish to continue?",
                      "Apply & Confirm",
                      JOptionPane.YES_NO_OPTION);
              if (res != JOptionPane.YES_OPTION) {
                return;
              }
              pnlFontSize.updateSettingsScale();
            }
            setVisible(false);
            dispose();
          }
        });

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

  }
}
