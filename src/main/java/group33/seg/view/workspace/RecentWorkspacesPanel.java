package group33.seg.view.workspace;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import group33.seg.model.configs.WorkspaceConfig;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RecentWorkspacesPanel extends JPanel {
  private static final long serialVersionUID = -8972763800588468558L;

  public RecentWorkspacesPanel() {
    initGUI();
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

    JList<WorkspaceConfig> lstWorkspaces = new JList<WorkspaceConfig>();
    JScrollPane scrWorkspaces = new JScrollPane(lstWorkspaces);
    scrWorkspaces.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstWorkspaces = new GridBagConstraints();
    gbc_lstWorkspaces.insets = new Insets(0, 5, 5, 5);
    gbc_lstWorkspaces.fill = GridBagConstraints.BOTH;
    gbc_lstWorkspaces.gridx = 0;
    gbc_lstWorkspaces.gridy = 1;
    add(scrWorkspaces, gbc_lstWorkspaces);

    JButton btnOpen = new JButton("Open");
    GridBagConstraints gbc_btnOpen = new GridBagConstraints();
    gbc_btnOpen.anchor = GridBagConstraints.EAST;
    gbc_btnOpen.gridx = 0;
    gbc_btnOpen.gridy = 2;
    add(btnOpen, gbc_btnOpen);
  }

}
