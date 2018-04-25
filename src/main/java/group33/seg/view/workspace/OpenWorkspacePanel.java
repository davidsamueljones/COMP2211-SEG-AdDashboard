package group33.seg.view.workspace;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import group33.seg.controller.DashboardController;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.model.configs.WorkspaceInstance;
import group33.seg.view.utilities.FileActionListener;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class OpenWorkspacePanel extends JPanel {
  private static final long serialVersionUID = -2588808491016545205L;

  private DashboardController controller;

  protected JTextField txtWorkspacePath;
  protected JButton btnBrowse;

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public OpenWorkspacePanel(DashboardController controller) {
    this.controller = controller;
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.rowHeights = new int[] {0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[] {1.0, 0.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    setLayout(gridBagLayout);
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    String strHelp = "Locate an Ad-Dashboard Workspace (.adw) on your system:";
    JLabel lblHelp = new JLabel(String.format("<html>%s</html>", strHelp));
    GridBagConstraints gbc_lblHelp = new GridBagConstraints();
    gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblHelp.gridwidth = 2;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 0;
    add(lblHelp, gbc_lblHelp);

    txtWorkspacePath = new JTextField();
    GridBagConstraints gbc_txtWorkspacePath = new GridBagConstraints();
    gbc_txtWorkspacePath.insets = new Insets(0, 0, 5, 5);
    gbc_txtWorkspacePath.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtWorkspacePath.gridx = 0;
    gbc_txtWorkspacePath.gridy = 1;
    add(txtWorkspacePath, gbc_txtWorkspacePath);
    txtWorkspacePath.setColumns(10);

    btnBrowse = new JButton("Browse");
    GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
    gbc_btnBrowse.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnBrowse.insets = new Insets(0, 0, 5, 0);
    gbc_btnBrowse.gridx = 1;
    gbc_btnBrowse.gridy = 1;
    add(btnBrowse, gbc_btnBrowse);

    JButton btnOpen = new JButton("Open");
    GridBagConstraints gbc_btnOpen = new GridBagConstraints();
    gbc_btnOpen.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnOpen.gridx = 1;
    gbc_btnOpen.gridy = 2;
    add(btnOpen, gbc_btnOpen);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    FileActionListener actFileBrowse = new FileActionListener(this, JFileChooser.FILES_ONLY);
    actFileBrowse.addMapping(btnBrowse, txtWorkspacePath);
    btnBrowse.addActionListener(actFileBrowse);

    btnOpen.addActionListener(e -> {
      loadWorkspace();
    });
  }

  private void loadWorkspace() {
    ErrorBuilder eb = controller.workspace.loadWorkspace(txtWorkspacePath.getText());
    if (eb.isError()) {
      JOptionPane.showMessageDialog(null, eb.listComments("Open Workspace"), "Open Workspace",
          JOptionPane.ERROR_MESSAGE);
    } else {
      Window frmCurrent = SwingUtilities.getWindowAncestor(OpenWorkspacePanel.this);
      frmCurrent.setVisible(false);
      frmCurrent.dispose();
    }
  }

}
