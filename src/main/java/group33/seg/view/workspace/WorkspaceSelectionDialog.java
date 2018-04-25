package group33.seg.view.workspace;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.view.utilities.Accessibility;

public class WorkspaceSelectionDialog extends JDialog {
  private static final long serialVersionUID = -9007004758635767514L;

  private DashboardController controller;

  private CardLayout cl_pnlControls;
  private JPanel pnlControls;

  private JButton btnOpen;
  private JButton btnNew;
  private JButton btnClose;
  private JButton btnRecent;

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public WorkspaceSelectionDialog(Window parent, DashboardController controller) {
    super(parent, "Workspace Selector");

    this.controller = controller;

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
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    loadView(View.RECENT);
  }

  /**
   * Initialise GUI and any event listeners.
   */
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
    gbl_pnlModes.rowHeights = new int[] {0, 0, 0, 0, 0};
    gbl_pnlModes.columnWeights = new double[] {1.0};
    gbl_pnlModes.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
    pnlNavigation.setLayout(gbl_pnlModes);

    // Create JButton for selecting RecentWorkspacePanel
    btnRecent = new JButton("Recent");
    Accessibility.scaleJComponentFontSize(btnRecent, 1.5);
    btnRecent.setHorizontalAlignment(SwingConstants.LEFT);
    btnRecent.setIconTextGap(Math.min(btnRecent.getFont().getSize(), 20));
    btnRecent.setIcon(new ImageIcon(getClass().getResource("/icons/redo-alt.png")));
    btnRecent.setMargin(new Insets(5, 5, 5, 20));
    GridBagConstraints gbc_btnRecent = new GridBagConstraints();
    gbc_btnRecent.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnRecent.gridx = 0;
    gbc_btnRecent.gridy = 0;
    pnlNavigation.add(btnRecent, gbc_btnRecent);

    // Create JButton for selecting OpenWorkspacePanel
    btnOpen = new JButton("Open");
    Accessibility.scaleJComponentFontSize(btnOpen, 1.5);
    btnOpen.setHorizontalAlignment(SwingConstants.LEFT);
    btnOpen.setIconTextGap(Math.min(btnOpen.getFont().getSize(), 20));
    btnOpen.setIcon(new ImageIcon(getClass().getResource("/icons/folder-open.png")));
    btnOpen.setMargin(new Insets(5, 5, 5, 20));
    GridBagConstraints gbc_btnOpen = new GridBagConstraints();
    gbc_btnOpen.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnOpen.gridx = 0;
    gbc_btnOpen.gridy = 1;
    pnlNavigation.add(btnOpen, gbc_btnOpen);

    // Create JButton for selecting NewWorkspacePanel
    btnNew = new JButton("New");
    Accessibility.scaleJComponentFontSize(btnNew, 1.5);
    btnNew.setHorizontalAlignment(SwingConstants.LEFT);
    btnNew.setIconTextGap(Math.min(btnNew.getFont().getSize(), 20));
    btnNew.setIcon(new ImageIcon(getClass().getResource("/icons/file.png")));
    btnNew.setMargin(new Insets(5, 5, 5, 20));
    GridBagConstraints gbc_btnNew = new GridBagConstraints();
    gbc_btnNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNew.gridx = 0;
    gbc_btnNew.gridy = 2;
    pnlNavigation.add(btnNew, gbc_btnNew);

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
    gbc_btnClose.gridy = 3;
    pnlNavigation.add(btnClose, gbc_btnClose);
    
    // Controls Panel
    cl_pnlControls = new CardLayout();
    pnlControls = new JPanel(cl_pnlControls);

    pnlControls.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagConstraints gbc_pnlControls = new GridBagConstraints();
    gbc_pnlControls.fill = GridBagConstraints.BOTH;
    gbc_pnlControls.insets = new Insets(5, 5, 5, 5);
    gbc_pnlControls.gridx = 1;
    gbc_pnlControls.gridy = 0;
    pnlDialog.add(pnlControls, gbc_pnlControls);

    RecentWorkspacesPanel pnlRecent = new RecentWorkspacesPanel();
    pnlControls.add(pnlRecent, View.RECENT.toString());

    OpenWorkspacePanel pnlOpen = new OpenWorkspacePanel(controller);
    pnlControls.add(pnlOpen, View.OPEN.toString());

    CreateWorkspacePanel pnlNew = new CreateWorkspacePanel(controller);
    pnlControls.add(pnlNew, View.NEW.toString());

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    btnRecent.addActionListener(e -> loadView(View.RECENT));
    btnOpen.addActionListener(e -> loadView(View.OPEN));
    btnNew.addActionListener(e -> loadView(View.NEW));
    btnClose.addActionListener(e -> {
      setVisible(false);
      dispose();
    });
  }

  private void loadView(View view) {
    cl_pnlControls.show(pnlControls, view.toString());
    btnNew.setEnabled(View.NEW != view);
    btnOpen.setEnabled(View.OPEN != view);
    btnRecent.setEnabled(View.RECENT != view);
  }

  /**
   * Enumeration of workspace2 selection views.
   */
  public enum View {
    RECENT, OPEN, NEW;
  }

}
