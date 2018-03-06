package group33.seg.view.increment1;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class DashboardMenuBar extends JMenuBar {
  private static final long serialVersionUID = 7553179515259733852L;
  public int CMD_MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

  /**
   * Initialise the menu bar.
   */
  public DashboardMenuBar() {
    super();
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
  }

}