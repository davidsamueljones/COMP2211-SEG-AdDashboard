package group33.seg.view.campaignimport;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.Accessibility.Appearance;

public class CampaignImportDialog extends JDialog {
  private static final long serialVersionUID = -8083386947121993055L;
  private JButton btnImportNew;
  private JButton btnCancel;

  /**
   * Launch the test application.
   */
  public static void main(String[] args) {
    try {
      Accessibility.scaleDefaultUIFontSize(1);
      Accessibility.setAppearance(Appearance.PLATFORM);
      CampaignImportDialog dialog = new CampaignImportDialog();
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
      dialog.setTitle("Campaign Importer");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   */
  public CampaignImportDialog() {
    this(null);
  }

  public CampaignImportDialog(Frame parent) {
    super(parent, "Campaign Importer", true);

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
    setBounds(loc.x, loc.y, 700, 400);
  }

  private void initGUI() {
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
    gbl_pnlModes.rowHeights = new int[] {0, 0, 0};
    gbl_pnlModes.columnWeights = new double[] {1.0};
    gbl_pnlModes.rowWeights = new double[] {0.0, 0.0, 1.0};
    pnlNavigation.setLayout(gbl_pnlModes);

    // Create 'Large' JButton
    btnImportNew = new JButton("Import New");
    Accessibility.scaleJComponentFontSize(btnImportNew, 1.5);
    btnImportNew.setHorizontalAlignment(SwingConstants.LEFT);
    btnImportNew.setIconTextGap(Math.min(btnImportNew.getFont().getSize(), 20));
    btnImportNew.setIcon(new ImageIcon(getClass().getResource("/icons/file.png")));
    btnImportNew.setMargin(new Insets(5, 5, 5, 20));
    btnImportNew.setEnabled(false);
    GridBagConstraints gbc_btnImportNew = new GridBagConstraints();
    gbc_btnImportNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnImportNew.gridx = 0;
    gbc_btnImportNew.gridy = 0;
    pnlNavigation.add(btnImportNew, gbc_btnImportNew);
    pnlNavigation.setMinimumSize(btnImportNew.getPreferredSize());

    btnCancel = new JButton("Cancel");
    Accessibility.scaleJComponentFontSize(btnCancel, 1.5);
    btnCancel.setHorizontalAlignment(SwingConstants.LEFT);
    btnCancel.setIconTextGap(Math.min(btnCancel.getFont().getSize(), 20));
    btnCancel.setIcon(new ImageIcon(getClass().getResource("/icons/times-circle.png")));
    btnCancel.setMargin(new Insets(5, 5, 5, 20));
    GridBagConstraints gbc_btnCancel = new GridBagConstraints();
    gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnCancel.gridx = 0;
    gbc_btnCancel.gridy = 1;
    pnlNavigation.add(btnCancel, gbc_btnCancel);

    // Controls Panel
    CampaignImportPanel pnlCampaignImport = new CampaignImportPanel();
    pnlCampaignImport.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    // JScrollPane scr_pnlControls =
    // new JScrollPane(pnlControls, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
    // ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    // scr_pnlControls.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_pnlCampaignImport = new GridBagConstraints();
    gbc_pnlCampaignImport.fill = GridBagConstraints.BOTH;
    gbc_pnlCampaignImport.insets = new Insets(5, 3, 5, 5);
    gbc_pnlCampaignImport.gridx = 1;
    gbc_pnlCampaignImport.gridy = 0;
    pnlDialog.add(pnlCampaignImport, gbc_pnlCampaignImport);
  }

}
