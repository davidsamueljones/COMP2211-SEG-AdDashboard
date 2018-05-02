package group33.seg.view.campaignselection;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
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
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.view.utilities.Accessibility;

public class CampaignSelectionDialog extends JDialog {
  private static final long serialVersionUID = -8083386947121993055L;

  private DashboardController controller;

  private JButton btnAvailable;
  private JButton btnImportNew;
  private JButton btnClose;

  private CampaignConfig base;

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public CampaignSelectionDialog(Window parent, DashboardController controller) {
    this(parent, controller, null);
  }

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   * @param campaign Campaign to replace in workspace
   */
  public CampaignSelectionDialog(Window parent, DashboardController controller,
      CampaignConfig campaign) {
    super(parent, "Campaign Selector");

    this.controller = controller;
    this.base = campaign;

    // Initialise GUI
    initGUI();

    // Determine positioning
    setSize(new Dimension(800, 450));
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }

    // Apply dialog properties
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
  }

  /** Initialise GUI and any event listeners. */
  private void initGUI() {

    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    JPanel pnlDialog = new JPanel();
    pnlDialog.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlDialog = new GridBagLayout();
    gbl_pnlDialog.columnWeights = new double[] {0.0, 1.0};
    gbl_pnlDialog.rowWeights = new double[] {1.0};
    pnlDialog.setLayout(gbl_pnlDialog);
    setContentPane(pnlDialog);

    // Modes Panel
    JPanel pnlNavigation = new JPanel();
    GridBagConstraints gbc_pnlNavigation = new GridBagConstraints();
    gbc_pnlNavigation.fill = GridBagConstraints.BOTH;
    gbc_pnlNavigation.insets = new Insets(5, 5, 5, 3);
    gbc_pnlNavigation.gridx = 0;
    gbc_pnlNavigation.gridy = 0;
    pnlDialog.add(pnlNavigation, gbc_pnlNavigation);

    GridBagLayout gbl_pnlModes = new GridBagLayout();
    gbl_pnlModes.rowHeights = new int[] {0, 0, 0, 0};
    gbl_pnlModes.columnWeights = new double[] {1.0};
    gbl_pnlModes.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    pnlNavigation.setLayout(gbl_pnlModes);

    // Create JButton for selecting AvailableCampaignsPanel
    btnAvailable = new JButton("Available");
    Accessibility.scaleJComponentFontSize(btnAvailable, 1.5);
    btnAvailable.setHorizontalAlignment(SwingConstants.LEFT);
    btnAvailable.setIconTextGap(Math.min(btnAvailable.getFont().getSize(), 20));
    btnAvailable.setIcon(new ImageIcon(getClass().getResource("/icons/folder-open.png")));
    btnAvailable.setMargin(new Insets(5, 5, 5, 20));
    GridBagConstraints gbc_btnAvailable = new GridBagConstraints();
    gbc_btnAvailable.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnAvailable.gridx = 0;
    gbc_btnAvailable.gridy = 0;
    pnlNavigation.add(btnAvailable, gbc_btnAvailable);
    pnlNavigation.setMinimumSize(btnAvailable.getPreferredSize());

    // Create JButton for selecting CampaignImportPanel
    btnImportNew = new JButton("Import New");
    Accessibility.scaleJComponentFontSize(btnImportNew, 1.5);
    btnImportNew.setHorizontalAlignment(SwingConstants.LEFT);
    btnImportNew.setIconTextGap(Math.min(btnImportNew.getFont().getSize(), 20));
    btnImportNew.setIcon(new ImageIcon(getClass().getResource("/icons/file.png")));
    btnImportNew.setMargin(new Insets(5, 5, 5, 20));
    GridBagConstraints gbc_btnImportNew = new GridBagConstraints();
    gbc_btnImportNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnImportNew.gridx = 0;
    gbc_btnImportNew.gridy = 1;
    pnlNavigation.add(btnImportNew, gbc_btnImportNew);
    pnlNavigation.setMinimumSize(btnImportNew.getPreferredSize());

    // Create JButton for exiting dialog
    btnClose = new JButton("Close");
    Accessibility.scaleJComponentFontSize(btnClose, 1.5);
    btnClose.setHorizontalAlignment(SwingConstants.LEFT);
    btnClose.setIconTextGap(Math.min(btnClose.getFont().getSize(), 20));
    btnClose.setIcon(new ImageIcon(getClass().getResource("/icons/times-circle.png")));
    btnClose.setMargin(new Insets(5, 5, 5, 20));
    GridBagConstraints gbc_btnClose = new GridBagConstraints();
    gbc_btnClose.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnClose.gridx = 0;
    gbc_btnClose.gridy = 2;
    pnlNavigation.add(btnClose, gbc_btnClose);

    // Controls Panel
    CardLayout cl_pnlControls = new CardLayout();
    JPanel pnlControls = new JPanel(cl_pnlControls);

    pnlControls.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagConstraints gbc_pnlControls = new GridBagConstraints();
    gbc_pnlControls.fill = GridBagConstraints.BOTH;
    gbc_pnlControls.insets = new Insets(5, 5, 5, 5);
    gbc_pnlControls.gridx = 1;
    gbc_pnlControls.gridy = 0;
    pnlDialog.add(pnlControls, gbc_pnlControls);

    // Available Panel
    AvailableCampaignsPanel pnlAvailable = new AvailableCampaignsPanel(controller, base);
    pnlControls.add(pnlAvailable);
    cl_pnlControls.addLayoutComponent(pnlAvailable, View.AVAILABLE.toString());

    // Import Panel
    CampaignImportPanel pnlCampaignImport = new CampaignImportPanel(controller, base);
    pnlControls.add(pnlCampaignImport);
    cl_pnlControls.addLayoutComponent(pnlCampaignImport, View.IMPORTING.toString());

    // Select initial screen
    btnAvailable.setEnabled(false);
    cl_pnlControls.show(pnlControls, View.AVAILABLE.toString());

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Allow switching between two panels
    btnAvailable.addActionListener(e -> {
      if (controller.imports.isOngoing()) {
        JOptionPane.showMessageDialog(null,
            "Cannot change view whilst importing, either cancel or wait for import to finish.",
            "View Error", JOptionPane.ERROR_MESSAGE);
      } else {
        cl_pnlControls.show(pnlControls, View.AVAILABLE.toString());
        btnAvailable.setEnabled(false);
        btnImportNew.setEnabled(true);
      }
    });
    btnImportNew.addActionListener(e -> {
      cl_pnlControls.show(pnlControls, View.IMPORTING.toString());
      btnAvailable.setEnabled(true);
      btnImportNew.setEnabled(false);
    });

    // Close the dialog if an import is not ongoing
    btnClose.addActionListener(e -> closeDialog());

    // Listen for any other window close event so close behaviour is controlled
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        closeDialog();
      }
    });
  }

  /** Available views for CampaignSelectionDialog */
  private enum View {
    AVAILABLE, IMPORTING
  }

  /**
   * Check if dialog can be closed before closing it.
   */
  private void closeDialog() {
    if (controller.imports.isOngoing()) {
      JOptionPane.showMessageDialog(null,
          "Cannot close whilst importing, either cancel or wait for import to finish.",
          "Close Error", JOptionPane.ERROR_MESSAGE);
    } else {
      setVisible(false);
      dispose();
    }
  }

}
