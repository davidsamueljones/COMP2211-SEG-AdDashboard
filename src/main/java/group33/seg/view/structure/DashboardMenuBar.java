package group33.seg.view.structure;

import java.awt.Dialog.ModalityType;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import group33.seg.controller.DashboardController;
import group33.seg.view.about.AboutDialog;
import group33.seg.view.preferences.PreferencesDialog;
import group33.seg.view.workspace.WorkspaceSelectionDialog;

public class DashboardMenuBar extends JMenuBar {
  private static final long serialVersionUID = 7553179515259733852L;
  public int CMD_MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

  private final DashboardFrame dashboard;
  private final DashboardController controller;

  /**
   * Initialise the menu bar.
   * 
   * @param controller Controller for this view object
   */
  public DashboardMenuBar(DashboardFrame dashboard, DashboardController controller) {
    this.dashboard = dashboard;
    this.controller = controller;

    initMenuBar();
  }

  private void initMenuBar() {
    // Initialise menu bar items
    initMenuBarItemFile();
    initMenuBarItemView();
    initMenuBarItemHelp();
  }

  private void initMenuBarItemFile() {
    // Create menu bar item
    JMenu mnFile = new JMenu("File");
    this.add(mnFile);

    // Create menu bar item children
    JMenuItem mntmChangeWorkspace = new JMenuItem("Change Workspace");
    mntmChangeWorkspace.addActionListener(e -> {
      // Use current panel's form as parent
      Window frmCurrent = SwingUtilities.getWindowAncestor(DashboardMenuBar.this);
      WorkspaceSelectionDialog changeWorkspace =
          new WorkspaceSelectionDialog(frmCurrent, controller);
      changeWorkspace.setModalityType(ModalityType.APPLICATION_MODAL);
      changeWorkspace.setVisible(true);
    });
    mntmChangeWorkspace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, CMD_MODIFIER));
    mnFile.add(mntmChangeWorkspace);

    JMenuItem mtnmSaveWorkspace = new JMenuItem("Save Workspace");
    mtnmSaveWorkspace.addActionListener(e -> {
      controller.workspace.storeCurrentWorkspace(true);
    });
    mtnmSaveWorkspace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, CMD_MODIFIER));
    mnFile.add(mtnmSaveWorkspace);

    mnFile.addSeparator();

    JMenuItem mntmExit = new JMenuItem("Exit");
    mntmExit.addActionListener(e -> controller.display.closeDashboard());
    mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, CMD_MODIFIER));
    mnFile.add(mntmExit);
  }

  private void initMenuBarItemView() {
    // Create menu bar item
    JMenu mnView = new JMenu("View");
    this.add(mnView);

    // Create menu bar item children
    JCheckBoxMenuItem mntmControls = new JCheckBoxMenuItem("Controls");
    mntmControls.setSelected(true);
    mntmControls.addActionListener(e -> dashboard.showControls(mntmControls.isSelected()));
    mnView.add(mntmControls);

    JCheckBoxMenuItem mntmGraph = new JCheckBoxMenuItem("Graph");
    mntmGraph.setSelected(true);
    mntmGraph.addActionListener(e -> dashboard.showGraph(mntmGraph.isSelected()));
    mnView.add(mntmGraph);

    JCheckBoxMenuItem mntmStatistics = new JCheckBoxMenuItem("Statistics");
    mntmStatistics.setSelected(true);
    mntmStatistics.addActionListener(e -> dashboard.showStatistics(mntmStatistics.isSelected()));
    mnView.add(mntmStatistics);

    mnView.addSeparator();

    JCheckBoxMenuItem mntmDefinitions = new JCheckBoxMenuItem("Definitions");
    mntmDefinitions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, CMD_MODIFIER));
    mntmDefinitions.setSelected(false);
    mnView.add(mntmDefinitions);

    // Menu bar click behaviour
    mnView.addMenuListener(new MenuListener() {

      @Override
      public void menuSelected(MenuEvent e) {
        mntmDefinitions.setSelected(controller.display.isDefinitionWindowVisible());
      }

      @Override
      public void menuDeselected(MenuEvent e) {}

      @Override
      public void menuCanceled(MenuEvent e) {}

    });

    // Toggle definitions item click behaviour
    mntmDefinitions.addActionListener(e -> {
      if (mntmDefinitions.isSelected()) {
        controller.display.showDefinitions();
      } else {
        controller.display.hideDefinitions();
      }
    });
  }

  private void initMenuBarItemHelp() {
    // Create menu bar item
    JMenu mnHelp = new JMenu("Help");
    this.add(mnHelp);

    // Create menu bar item children
    JMenuItem mntmAbout = new JMenuItem("About");
    mnHelp.add(mntmAbout);
    mntmAbout.addActionListener(e -> {
      // Use current panel's form as parent
      Window frmCurrent = SwingUtilities.getWindowAncestor(DashboardMenuBar.this);
      AboutDialog about = new AboutDialog(frmCurrent);
      about.setModalityType(ModalityType.APPLICATION_MODAL);
      about.setVisible(true);
    });

    mnHelp.addSeparator();

    JMenuItem mntmPreferences = new JMenuItem("Preferences");
    mntmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, CMD_MODIFIER));
    mnHelp.add(mntmPreferences);
    mntmPreferences.addActionListener(e -> {
      // Use current panel's form as parent
      Window frmCurrent = SwingUtilities.getWindowAncestor(DashboardMenuBar.this);
      PreferencesDialog preferences = new PreferencesDialog(frmCurrent, controller);
      preferences.setModalityType(ModalityType.APPLICATION_MODAL);
      preferences.setVisible(true);
    });
  }
}
