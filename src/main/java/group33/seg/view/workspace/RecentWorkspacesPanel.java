package group33.seg.view.workspace;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.utilities.ErrorBuilder;

public class RecentWorkspacesPanel extends JPanel {
  private static final long serialVersionUID = -8972763800588468558L;

  private DashboardController controller;

  private DefaultListModel<String> model_lstWorkspaces;
  private JList<String> lstWorkspaces;

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public RecentWorkspacesPanel(DashboardController controller) {
    this.controller = controller;
    initGUI();
    refreshRecent();
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0};
    gridBagLayout.rowHeights = new int[] {0, 0, 0};
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0, 0.0};
    setLayout(gridBagLayout);
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    String strHelp = "Select a recently loaded workspace to load:";
    JLabel lblHelp = new JLabel(String.format("<html>%s</html>", strHelp));
    GridBagConstraints gbc_lblHelp = new GridBagConstraints();
    gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 0;
    add(lblHelp, gbc_lblHelp);

    model_lstWorkspaces = new DefaultListModel<>();
    lstWorkspaces = new JList<>(model_lstWorkspaces);
    JScrollPane scrWorkspaces = new JScrollPane(lstWorkspaces);
    scrWorkspaces.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstWorkspaces = new GridBagConstraints();
    gbc_lstWorkspaces.insets = new Insets(0, 5, 5, 5);
    gbc_lstWorkspaces.fill = GridBagConstraints.BOTH;
    gbc_lstWorkspaces.gridx = 0;
    gbc_lstWorkspaces.gridy = 1;
    add(scrWorkspaces, gbc_lstWorkspaces);

    JButton btnOpen = new JButton("Open");
    btnOpen.setEnabled(false);
    GridBagConstraints gbc_btnOpen = new GridBagConstraints();
    gbc_btnOpen.anchor = GridBagConstraints.EAST;
    gbc_btnOpen.gridx = 0;
    gbc_btnOpen.gridy = 2;
    add(btnOpen, gbc_btnOpen);

    btnOpen.addActionListener(e -> loadWorkspace());
    
    lstWorkspaces.addListSelectionListener(e -> {
      btnOpen.setEnabled(lstWorkspaces.getSelectedValue() != null);
    });
  }

  /**
   * Refresh the recently displayed workspaces.
   */
  public void refreshRecent() {
    List<String> recents = controller.settings.getRecentWorkspaces();
    for (String recent : recents) {
      model_lstWorkspaces.addElement(recent);
    }
  }

  /**
   * Load the currently selected recent workspace.
   */
  private void loadWorkspace() {
    String workspace = lstWorkspaces.getSelectedValue();
    ErrorBuilder eb = controller.workspace.loadWorkspace(workspace);
    if (eb.isError()) {
      JOptionPane.showMessageDialog(null, eb.listComments("Open Workspace"), "Open Workspace",
          JOptionPane.ERROR_MESSAGE);
    } else {
      Window frmCurrent = SwingUtilities.getWindowAncestor(RecentWorkspacesPanel.this);
      frmCurrent.setVisible(false);
      frmCurrent.dispose();
    }
  }

}
