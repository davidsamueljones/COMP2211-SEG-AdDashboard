package group33.seg.view.structure;

import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.WorkspaceHandler;
import group33.seg.view.about.AboutDialog;
import group33.seg.view.preferences.PreferencesDialog;

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
    initMenuBarTools();
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
      dashboard.showWorkspaceSelector();
    });
    mntmChangeWorkspace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, CMD_MODIFIER));
    mnFile.add(mntmChangeWorkspace);

    JMenuItem mntmModifyWorkspace = new JMenuItem("Modify Workspace");
    mntmModifyWorkspace.addActionListener(e -> {
      dashboard.showWorkspaceModifier();
    });
    mntmModifyWorkspace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, CMD_MODIFIER));
    mnFile.add(mntmModifyWorkspace);

    JMenuItem mtnmSaveWorkspace = new JMenuItem("Save Workspace");
    mtnmSaveWorkspace.addActionListener(e -> {
      controller.workspace.storeCurrentWorkspace(true);
    });
    mtnmSaveWorkspace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, CMD_MODIFIER));
    mnFile.add(mtnmSaveWorkspace);

    mnFile.addSeparator();

    JMenuItem mntmExit = new JMenuItem("Exit");
    mntmExit.addActionListener(e -> {
      dashboard.dispatchEvent(new WindowEvent(dashboard, WindowEvent.WINDOW_CLOSING));
    });
    mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, CMD_MODIFIER));
    mnFile.add(mntmExit);

    // Menu bar click behaviour
    mnFile.addMenuListener(new MenuListener() {

      @Override
      public void menuSelected(MenuEvent e) {
        boolean workspaceLoaded = controller.workspace.getWorkspaceName() != null;
        mntmModifyWorkspace.setEnabled(workspaceLoaded);
        mtnmSaveWorkspace.setEnabled(workspaceLoaded);
      }

      @Override
      public void menuDeselected(MenuEvent e) {}

      @Override
      public void menuCanceled(MenuEvent e) {}

    });
  }

  private void initMenuBarTools() {
    // Create menu bar item
    JMenu mnTools = new JMenu("Tools");
    this.add(mnTools);

    JMenuItem mtnmClearGraph = new JMenuItem("Clear Graph");
    mtnmClearGraph.addActionListener(e -> controller.workspace.setCurrentGraph(null));
    mnTools.add(mtnmClearGraph);

    mnTools.addSeparator();

    JMenu mnAdvanced = new JMenu("Advanced");
    mnTools.add(mnAdvanced);

    JMenuItem mtnmCleanCaches = new JMenuItem("Clean Caches");
    mtnmCleanCaches.addActionListener(e -> controller.workspace.cleanCaches());
    mnAdvanced.add(mtnmCleanCaches);

    JMenuItem mtnmClearCaches = new JMenuItem("Clear Caches");
    mtnmClearCaches.addActionListener(e -> controller.workspace.clearCaches());
    mnAdvanced.add(mtnmClearCaches);

    mnAdvanced.addSeparator();

    JMenuItem mntmExportSCC = new JMenuItem("Export SCC");
    mntmExportSCC.setToolTipText("Export Server Connection Configuration (.scc)");
    mntmExportSCC.addActionListener(e -> {
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int res = fc.showSaveDialog(this);
      if (res == JFileChooser.APPROVE_OPTION) {
        File file = new File(fc.getSelectedFile() + ".scc");
        if (Files.exists(Paths.get(file.toString()))) {
          res = JOptionPane.showConfirmDialog(null,
              "The file specified already exists, would you like to overwrite it?", "Overwrite",
              JOptionPane.YES_NO_OPTION);
          if (res != JOptionPane.YES_OPTION) {
            return;
          }
        }
        controller.workspace.storeDatabaseConfig(file.toString(),
            WorkspaceHandler.PRIVATE_KEY.toCharArray(), true);
      }
    });
    mnAdvanced.add(mntmExportSCC);

    // Menu bar click behaviour
    mnTools.addMenuListener(new MenuListener() {

      @Override
      public void menuSelected(MenuEvent e) {
        mtnmClearGraph.setEnabled(controller.workspace.getCurrentGraph() != null);
        boolean workspaceLoaded = controller.workspace.getWorkspaceName() != null;
        mtnmCleanCaches.setEnabled(workspaceLoaded);
        mtnmClearCaches.setEnabled(workspaceLoaded);
        mntmExportSCC.setEnabled(workspaceLoaded);
      }

      @Override
      public void menuDeselected(MenuEvent e) {}

      @Override
      public void menuCanceled(MenuEvent e) {}

    });

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

    JMenuItem mtnmUserManual = new JMenuItem("User Manual");
    mnHelp.add(mtnmUserManual);
    mtnmUserManual.addActionListener(e -> {
      int res =
          JOptionPane.showConfirmDialog(null,
              "The user manual will open using your default PDF application.\r\n"
                  + "Are you sure you want to open it?",
              "Open User Manual", JOptionPane.YES_NO_OPTION);
      if (res != JOptionPane.YES_OPTION) {
        return;
      }

      // Use current panel's form as parent
      try {
        Desktop.getDesktop()
            .open(new File(getClass().getResource("/ad_dashboard_usermanual.pdf").toURI()));
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Could not find user manual", "Error",
            JOptionPane.ERROR_MESSAGE);
      }
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
