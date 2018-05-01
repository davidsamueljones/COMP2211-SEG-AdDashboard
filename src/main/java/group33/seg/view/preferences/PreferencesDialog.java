package group33.seg.view.preferences;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.SettingsHandler;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

public class PreferencesDialog extends JDialog {
  private static final long serialVersionUID = -8083386947121993055L;

  private DashboardController controller;

  private FontSizePanel pnlFontSize;
  private JCheckBox chckbxToolTipsEnabled;

  private JRadioButton radGraphFast;
  private JRadioButton radQuality;

  private JButton btnApplyConfirm;
  private JButton btnCancel;


  /**
   * Create the dialog with a given parent window.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public PreferencesDialog(Window parent, DashboardController controller) {
    super(parent, "Preferences");

    this.controller = controller;

    // Initialise GUI
    initGUI();

    // Set sizing
    pack();
    setResizable(false);
    // Set positioning
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
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
    gbl_pnlAccessibility.columnWidths = new int[] {0};
    gbl_pnlAccessibility.rowHeights = new int[] {0, 0, 0};
    gbl_pnlAccessibility.columnWeights = new double[] {1.0};
    gbl_pnlAccessibility.rowWeights = new double[] {0.0, 0.0, 1.0};
    pnlAccessibility.setLayout(gbl_pnlAccessibility);

    pnlFontSize = new FontSizePanel(controller);
    pnlFontSize.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Font Size"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagConstraints gbc_pnlFontSize = new GridBagConstraints();
    gbc_pnlFontSize.insets = new Insets(5, 5, 5, 5);
    gbc_pnlFontSize.fill = GridBagConstraints.BOTH;
    gbc_pnlFontSize.gridx = 0;
    gbc_pnlFontSize.gridy = 0;
    pnlAccessibility.add(pnlFontSize, gbc_pnlFontSize);

    chckbxToolTipsEnabled = new JCheckBox("Tool Tips Enabled");
    GridBagConstraints gbc_chckbxToolTipsEnabled = new GridBagConstraints();
    gbc_chckbxToolTipsEnabled.anchor = GridBagConstraints.WEST;
    gbc_chckbxToolTipsEnabled.insets = new Insets(0, 0, 5, 0);
    gbc_chckbxToolTipsEnabled.gridx = 0;
    gbc_chckbxToolTipsEnabled.gridy = 1;
    pnlAccessibility.add(chckbxToolTipsEnabled, gbc_chckbxToolTipsEnabled);
    chckbxToolTipsEnabled
        .setSelected(controller.settings.prefs.getBoolean(SettingsHandler.TOOL_TIPS_ENABLED, true));

    JPanel pnlOther = new JPanel();
    pnlOther
        .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Other"));
    GridBagConstraints gbc_pnlOther = new GridBagConstraints();
    gbc_pnlOther.anchor = GridBagConstraints.NORTHWEST;
    gbc_pnlOther.gridwidth = 2;
    gbc_pnlOther.insets = new Insets(0, 0, 5, 0);
    gbc_pnlOther.fill = GridBagConstraints.BOTH;
    gbc_pnlOther.gridx = 0;
    gbc_pnlOther.gridy = 1;
    pnlDialog.add(pnlOther, gbc_pnlOther);
    GridBagLayout gbl_pnlOther = new GridBagLayout();
    gbl_pnlOther.rowHeights = new int[] {0, 0};
    gbl_pnlOther.columnWeights = new double[] {1.0};
    gbl_pnlOther.rowWeights = new double[] {0.0, 1.0};
    pnlOther.setLayout(gbl_pnlOther);

    JPanel pnlGraphSettings = new JPanel();
    pnlGraphSettings
        .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Graphs"));
    GridBagConstraints gbc_pnlGraphSettings = new GridBagConstraints();
    gbc_pnlGraphSettings.anchor = GridBagConstraints.NORTHWEST;
    gbc_pnlGraphSettings.fill = GridBagConstraints.BOTH;
    gbc_pnlGraphSettings.insets = new Insets(5, 5, 5, 5);
    gbc_pnlGraphSettings.gridx = 0;
    gbc_pnlGraphSettings.gridy = 0;

    pnlOther.add(pnlGraphSettings, gbc_pnlGraphSettings);
    GridBagLayout gbl_pnlGraphSettings = new GridBagLayout();
    gbl_pnlGraphSettings.columnWidths = new int[] {0, 0, 0, 0};
    gbl_pnlGraphSettings.columnWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    gbl_pnlGraphSettings.rowWeights = new double[] {0.0};
    pnlGraphSettings.setLayout(gbl_pnlGraphSettings);

    JLabel lblRenderingMode = new JLabel("Rendering Mode:");
    GridBagConstraints gbc_lblRenderingMode = new GridBagConstraints();
    gbc_lblRenderingMode.insets = new Insets(0, 5, 5, 5);
    gbc_lblRenderingMode.gridx = 0;
    gbc_lblRenderingMode.gridy = 0;
    pnlGraphSettings.add(lblRenderingMode, gbc_lblRenderingMode);

    ButtonGroup bgRenderingMode = new ButtonGroup();
    radGraphFast = new JRadioButton("Fast");
    bgRenderingMode.add(radGraphFast);
    GridBagConstraints gbc_radGraphFast = new GridBagConstraints();
    gbc_radGraphFast.insets = new Insets(0, 0, 5, 5);
    gbc_radGraphFast.gridx = 1;
    gbc_radGraphFast.gridy = 0;
    pnlGraphSettings.add(radGraphFast, gbc_radGraphFast);

    radQuality = new JRadioButton("Quality");
    bgRenderingMode.add(radQuality);
    GridBagConstraints gbc_radQuality = new GridBagConstraints();
    gbc_radQuality.insets = new Insets(0, 0, 5, 0);
    gbc_radQuality.gridx = 2;
    gbc_radQuality.gridy = 0;
    pnlGraphSettings.add(radQuality, gbc_radQuality);
    setGraphsUseBuffer();

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

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Do not apply changes on cancel
    btnCancel.addActionListener(e -> {
      setVisible(false);
      dispose();
    });

    // Apply changes
    btnApplyConfirm.addActionListener(e -> {
      boolean reloadNeeded;
      if (reloadNeeded = reloadNeeded()) {
        int res = JOptionPane.showConfirmDialog(PreferencesDialog.this,
            "Selected changes require any open windows to refresh.\r\n"
                + "This may clear any loaded views (no data will be lost), "
                + "are you sure you wish to continue?",
            "Apply & Confirm", JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) {
          return;
        }
        updateSettings();
      }
      setVisible(false);
      dispose();
      // Reload dashboard if required
      if (reloadNeeded) {
        controller.display.reloadDashboard();
      }
    });

  }

  /**
   * @return Whether current setting changes require the dashboard to be reloaded.
   */
  private boolean reloadNeeded() {
    boolean reloadNeeded = false;
    reloadNeeded |= pnlFontSize.hasChanged();
    reloadNeeded |= chckbxToolTipsEnabled.isSelected() != controller.settings.prefs
        .getBoolean(SettingsHandler.TOOL_TIPS_ENABLED, true);
    reloadNeeded |= getGraphsUseBuffer() != controller.settings.prefs
        .getBoolean(SettingsHandler.BUFFERED_GRAPH, true);
    return reloadNeeded;
  }

  /**
   * Apply changed settings to settings handler.
   */
  private void updateSettings() {
    pnlFontSize.updateSettings();
    controller.settings.prefs.putBoolean(SettingsHandler.TOOL_TIPS_ENABLED,
        chckbxToolTipsEnabled.isSelected());
    controller.settings.prefs.putBoolean(SettingsHandler.BUFFERED_GRAPH, getGraphsUseBuffer());
  }

  /**
   * Load the setting refering to whether graphs should use a buffer.
   */
  private void setGraphsUseBuffer() {
    if (controller.settings.prefs.getBoolean(SettingsHandler.BUFFERED_GRAPH, true)) {
      radGraphFast.setSelected(true);
    } else {
      radQuality.setSelected(true);
    }
  }

  /**
   * @return Whether the current setting selections indicate graphs should use a buffer
   */
  private boolean getGraphsUseBuffer() {
    return radGraphFast.isSelected();
  }

}
