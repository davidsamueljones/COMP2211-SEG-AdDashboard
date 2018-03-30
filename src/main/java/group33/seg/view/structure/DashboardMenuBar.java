package group33.seg.view.structure;

import group33.seg.controller.DashboardController;
import group33.seg.view.preferences.PreferencesDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class DashboardMenuBar extends JMenuBar {
  private static final long serialVersionUID = 7553179515259733852L;
  public int CMD_MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

  private final DashboardController controller;

  
  /**
   * Initialise the menu bar.
   * 
   * @param controller Controller for this view object
   */
  public DashboardMenuBar(DashboardController controller) {
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
    JMenuItem mntmImportCampaign = new JMenuItem("Import Campaign");
    mntmImportCampaign.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, CMD_MODIFIER));
    mnFile.add(mntmImportCampaign);

    mnFile.addSeparator();

    JMenuItem mntmExit = new JMenuItem("Exit");
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
    mnView.add(mntmControls);

    JCheckBoxMenuItem mntmGraph = new JCheckBoxMenuItem("Graph");
    mntmGraph.setSelected(true);
    mnView.add(mntmGraph);
  }

  private void initMenuBarItemHelp() {
    // Create menu bar item
    JMenu mnHelp = new JMenu("Help");
    this.add(mnHelp);

    // Create menu bar item children
    JMenuItem mntmAbout = new JMenuItem("About");
    mnHelp.add(mntmAbout);

    mnHelp.addSeparator();

    JMenuItem mntmPreferences = new JMenuItem("Preferences");
    mntmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, CMD_MODIFIER));
    mnHelp.add(mntmPreferences);

    mntmPreferences.addActionListener(
            e -> {
              // Use current panel's form as parent
              Window frmCurrent = SwingUtilities.getWindowAncestor(DashboardMenuBar.this);
              PreferencesDialog preferences = new PreferencesDialog(frmCurrent, controller);
              preferences.setModal(true);
              preferences.setVisible(true);
              if (controller.display.isUIFontScalingOutdated()) {
                controller.display.reloadDashboard();
              }
            });
  }
}
