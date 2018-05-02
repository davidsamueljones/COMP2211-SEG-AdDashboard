package group33.seg.view.workspace;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.handlers.WorkspaceHandler;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.lib.Pair;
import group33.seg.model.configs.WorkspaceConfig;
import group33.seg.model.configs.WorkspaceInstance;
import group33.seg.view.utilities.FileActionListener;
import group33.seg.view.utilities.JDynamicScrollPane;

public class CreateWorkspacePanel extends JPanel {
  private static final long serialVersionUID = 4736478828855312395L;

  private DashboardController controller;

  protected JLabel lblHelp;
  protected JTextField txtWorkspaceName;
  protected JTextField txtWorkspacePath;

  protected JTabbedPane tabsServerDetails;
  protected JPanel pnlSimple;
  protected JPanel pnlAdvanced;

  protected JTextField txtServerConfig;
  protected JTextField txtServer;
  protected JTextField txtUsername;
  protected JPasswordField txtPassword;

  protected JButton btnApply;

  private WorkspaceInstance base;

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   */
  public CreateWorkspacePanel(DashboardController controller) {
    this(controller, null);
  }

  /**
   * Create the panel.
   *
   * @param controller Controller for this view object
   * @param base Workspace instance to use as base
   */
  public CreateWorkspacePanel(DashboardController controller, WorkspaceInstance base) {
    this.controller = controller;

    initGUI();
    loadWorkspaceInstance(base);
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {

    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************
    setLayout(new BorderLayout());

    JPanel pnlContent = new JPanel();
    pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0, 0};
    gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 1.0, 0.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
    pnlContent.setLayout(gridBagLayout);

    JDynamicScrollPane scrContent = new JDynamicScrollPane();
    scrContent.setViewportView(pnlContent);
    add(scrContent, BorderLayout.CENTER);

    lblHelp = new JLabel();
    GridBagConstraints gbc_lblHelp = new GridBagConstraints();
    gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblHelp.gridwidth = 3;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 5);
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 0;
    pnlContent.add(lblHelp, gbc_lblHelp);

    JLabel lblWorkspaceName = new JLabel("Workspace Name:");
    GridBagConstraints gbc_lblWorkspaceName = new GridBagConstraints();
    gbc_lblWorkspaceName.insets = new Insets(0, 0, 5, 5);
    gbc_lblWorkspaceName.anchor = GridBagConstraints.EAST;
    gbc_lblWorkspaceName.gridx = 0;
    gbc_lblWorkspaceName.gridy = 1;
    pnlContent.add(lblWorkspaceName, gbc_lblWorkspaceName);

    txtWorkspaceName = new JTextField();
    GridBagConstraints gbc_txtWorkspaceName = new GridBagConstraints();
    gbc_txtWorkspaceName.gridwidth = 2;
    gbc_txtWorkspaceName.insets = new Insets(0, 0, 5, 0);
    gbc_txtWorkspaceName.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtWorkspaceName.gridx = 1;
    gbc_txtWorkspaceName.gridy = 1;
    pnlContent.add(txtWorkspaceName, gbc_txtWorkspaceName);

    JLabel lblWorkspaceFolderPath = new JLabel("Workspace Storage Directory:");
    GridBagConstraints gbc_lblWorkspaceFolderPath = new GridBagConstraints();
    gbc_lblWorkspaceFolderPath.gridwidth = 3;
    gbc_lblWorkspaceFolderPath.anchor = GridBagConstraints.WEST;
    gbc_lblWorkspaceFolderPath.insets = new Insets(0, 0, 5, 5);
    gbc_lblWorkspaceFolderPath.gridx = 0;
    gbc_lblWorkspaceFolderPath.gridy = 2;
    pnlContent.add(lblWorkspaceFolderPath, gbc_lblWorkspaceFolderPath);

    txtWorkspacePath = new JTextField();
    txtWorkspacePath
        .setToolTipText("The folder where the workspace's 'adw' file will be saved to.");
    GridBagConstraints gbc_txtWorkspacePath = new GridBagConstraints();
    gbc_txtWorkspacePath.gridwidth = 2;
    gbc_txtWorkspacePath.insets = new Insets(0, 0, 5, 5);
    gbc_txtWorkspacePath.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtWorkspacePath.gridx = 0;
    gbc_txtWorkspacePath.gridy = 3;
    pnlContent.add(txtWorkspacePath, gbc_txtWorkspacePath);

    JButton btnWorkspaceBrowse = new JButton("Browse");
    GridBagConstraints gbc_btnWorkspaceBrowse = new GridBagConstraints();
    gbc_btnWorkspaceBrowse.insets = new Insets(0, 0, 5, 5);
    gbc_btnWorkspaceBrowse.anchor = GridBagConstraints.SOUTH;
    gbc_btnWorkspaceBrowse.gridx = 2;
    gbc_btnWorkspaceBrowse.gridy = 3;
    pnlContent.add(btnWorkspaceBrowse, gbc_btnWorkspaceBrowse);

    tabsServerDetails = new JTabbedPane(SwingConstants.TOP);
    GridBagConstraints gbc_tabsServerDetails = new GridBagConstraints();
    gbc_tabsServerDetails.gridwidth = 3;
    gbc_tabsServerDetails.insets = new Insets(0, 0, 5, 0);
    gbc_tabsServerDetails.fill = GridBagConstraints.BOTH;
    gbc_tabsServerDetails.gridx = 0;
    gbc_tabsServerDetails.gridy = 4;
    pnlContent.add(tabsServerDetails, gbc_tabsServerDetails);

    pnlSimple = new JPanel();
    pnlSimple.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabsServerDetails.addTab("Simple", null, pnlSimple, null);
    GridBagLayout gbl_pnlSimple = new GridBagLayout();
    gbl_pnlSimple.columnWidths = new int[] {0, 0, 0};
    gbl_pnlSimple.rowHeights = new int[] {0, 0, 0};
    gbl_pnlSimple.columnWeights = new double[] {1.0, 0.0, Double.MIN_VALUE};
    gbl_pnlSimple.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
    pnlSimple.setLayout(gbl_pnlSimple);

    JLabel lblServerConnectionConfiguration =
        new JLabel("Server connection configuration file path (.scc):");
    GridBagConstraints gbc_lblServerConnectionConfiguration = new GridBagConstraints();
    gbc_lblServerConnectionConfiguration.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblServerConnectionConfiguration.gridwidth = 2;
    gbc_lblServerConnectionConfiguration.insets = new Insets(0, 0, 5, 5);
    gbc_lblServerConnectionConfiguration.gridx = 0;
    gbc_lblServerConnectionConfiguration.gridy = 0;
    pnlSimple.add(lblServerConnectionConfiguration, gbc_lblServerConnectionConfiguration);

    txtServerConfig = new JTextField();
    GridBagConstraints gbc_txtServerConfig = new GridBagConstraints();
    gbc_txtServerConfig.insets = new Insets(0, 0, 0, 5);
    gbc_txtServerConfig.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtServerConfig.gridx = 0;
    gbc_txtServerConfig.gridy = 1;
    pnlSimple.add(txtServerConfig, gbc_txtServerConfig);

    JButton btnConfigBrowse = new JButton("Browse");
    GridBagConstraints gbc_btnConfigBrowse = new GridBagConstraints();
    gbc_btnConfigBrowse.gridx = 1;
    gbc_btnConfigBrowse.gridy = 1;
    pnlSimple.add(btnConfigBrowse, gbc_btnConfigBrowse);

    pnlAdvanced = new JPanel();
    pnlAdvanced.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabsServerDetails.addTab("Advanced", null, pnlAdvanced, null);
    GridBagLayout gbl_pnlAdvanced = new GridBagLayout();
    gbl_pnlAdvanced.columnWidths = new int[] {0, 0, 0};
    gbl_pnlAdvanced.rowHeights = new int[] {0, 0, 0, 0};
    gbl_pnlAdvanced.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
    gbl_pnlAdvanced.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
    pnlAdvanced.setLayout(gbl_pnlAdvanced);

    JLabel lblServer = new JLabel("Server:");
    GridBagConstraints gbc_lblServer = new GridBagConstraints();
    gbc_lblServer.anchor = GridBagConstraints.EAST;
    gbc_lblServer.insets = new Insets(0, 0, 5, 5);
    gbc_lblServer.gridx = 0;
    gbc_lblServer.gridy = 0;
    pnlAdvanced.add(lblServer, gbc_lblServer);

    txtServer = new JTextField();
    GridBagConstraints gbc_txtServer = new GridBagConstraints();
    gbc_txtServer.insets = new Insets(0, 0, 5, 0);
    gbc_txtServer.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtServer.gridx = 1;
    gbc_txtServer.gridy = 0;
    pnlAdvanced.add(txtServer, gbc_txtServer);

    JLabel lblUsername = new JLabel("Username:");
    GridBagConstraints gbc_lblUsername = new GridBagConstraints();
    gbc_lblUsername.anchor = GridBagConstraints.EAST;
    gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
    gbc_lblUsername.gridx = 0;
    gbc_lblUsername.gridy = 1;
    pnlAdvanced.add(lblUsername, gbc_lblUsername);

    txtUsername = new JTextField();
    GridBagConstraints gbc_txtUsername = new GridBagConstraints();
    gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
    gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtUsername.gridx = 1;
    gbc_txtUsername.gridy = 1;
    pnlAdvanced.add(txtUsername, gbc_txtUsername);

    JLabel lblPassword = new JLabel("Password:");
    GridBagConstraints gbc_lblPassword = new GridBagConstraints();
    gbc_lblPassword.anchor = GridBagConstraints.EAST;
    gbc_lblPassword.insets = new Insets(0, 0, 0, 5);
    gbc_lblPassword.gridx = 0;
    gbc_lblPassword.gridy = 2;
    pnlAdvanced.add(lblPassword, gbc_lblPassword);

    txtPassword = new JPasswordField();
    GridBagConstraints gbc_txtPassword = new GridBagConstraints();
    gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtPassword.gridx = 1;
    gbc_txtPassword.gridy = 2;
    pnlAdvanced.add(txtPassword, gbc_txtPassword);

    btnApply = new JButton();
    GridBagConstraints gbc_btnApply = new GridBagConstraints();
    gbc_btnApply.insets = new Insets(0, 0, 0, 0);
    gbc_btnApply.gridx = 2;
    gbc_btnApply.gridy = 5;
    pnlContent.add(btnApply, gbc_btnApply);

    // Attach dynamic components
    scrContent.addDynamicComponent(lblHelp);
    // Trigger resize
    scrContent.doResize();

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    FileActionListener actFolderBrowse =
        new FileActionListener(this, JFileChooser.DIRECTORIES_ONLY);
    actFolderBrowse.addMapping(btnWorkspaceBrowse, txtWorkspacePath);
    btnWorkspaceBrowse.addActionListener(actFolderBrowse);

    FileActionListener actFileBrowse = new FileActionListener(this, JFileChooser.FILES_ONLY);
    actFileBrowse.addMapping(btnConfigBrowse, txtServerConfig);
    btnConfigBrowse.addActionListener(actFileBrowse);

    btnApply.addActionListener(e -> {
      storeWorkspace();
    });
  }

  /**
   * Load a workspace into the workspace panel.
   * 
   * @param wsi Workspace instance to load
   */
  private void loadWorkspaceInstance(WorkspaceInstance wsi) {
    // Load workspace date into fields
    if (wsi != null) {
      txtWorkspaceName.setText(wsi.name);
      txtWorkspacePath.setText(wsi.directory);
      if (wsi.workspace != null && wsi.workspace.database != null) {
        if (wsi.workspace.database.getReference() != null) {
          tabsServerDetails.setSelectedComponent(pnlSimple);
          txtServerConfig.setText(wsi.workspace.database.getReference());
        } else {
          tabsServerDetails.setSelectedComponent(pnlAdvanced);
          txtServer.setText(wsi.workspace.database.getHost());
          txtUsername.setText(wsi.workspace.database.getUser());
          txtPassword.setText(wsi.workspace.database.getPassword());
        }
      }
      String strHelp = "Modify the currently loaded workspace. Any changes to workspace name"
          + " or location will be reflected on the file system.";
      lblHelp.setText(String.format("<html>%s</html>", strHelp));
      btnApply.setText("Apply Changes");
    } else {
      txtWorkspaceName.setText(null);
      txtWorkspacePath.setText(null);
      tabsServerDetails.setSelectedComponent(pnlSimple);
      txtServerConfig.setText(null);
      txtServer.setText(null);
      txtUsername.setText(null);
      txtPassword.setText(null);

      String strHelp = "Create a new workspace for viewing campaigns. A created "
          + "workspace can be opened again in the future.";
      lblHelp.setText(String.format("<html>%s</html>", strHelp));
      btnApply.setText("Create New");
    }
    // Track workspace as base
    this.base = wsi;
  }


  /**
   * Generate a workspace instance from the current fields and the base if it exists.
   * 
   * @param eb Error builder to update with instance formation errors
   * @return Generated workspace instance
   */
  private WorkspaceInstance getWorkspaceInstance(ErrorBuilder eb) {
    // Create workspace instance
    WorkspaceInstance wsi =
        new WorkspaceInstance(txtWorkspaceName.getText(), txtWorkspacePath.getText());
    // Keep track of existing workspace if it exists (as base) or create a new one
    wsi.workspace = base == null ? new WorkspaceConfig() : base.workspace;

    // Validate workspace instance
    if (wsi.name.isEmpty()) {
      eb.addError("The workspace name must not be empty");
    }
    if (wsi.directory.isEmpty()) {
      eb.addError("The workspace path must not be empty");
    }

    // Update workspace database connection
    if (tabsServerDetails.getSelectedComponent().equals(pnlSimple)) {
      Pair<DatabaseConfig, ErrorBuilder> pair = DatabaseConfig.loadDatabaseConfig(
          txtServerConfig.getText(), WorkspaceHandler.PRIVATE_KEY.toCharArray());
      if (pair.key != null) {
        wsi.workspace.database = pair.key;
      } else {
        eb.append(pair.value);
      }
    } else {
      wsi.workspace.database = new DatabaseConfig(txtServer.getText(), txtUsername.getText(),
          String.valueOf(txtPassword.getPassword()));
    }
    return wsi;
  }

  /**
   * Create a workspace instance using the user filled elements. If there are no errors the
   * workspace will be saved to the file system. If the base is set this will be treated as an
   * update, therefore overwrite checks for the current path are disabled and any changes in file
   * system path will remove the old workspace file.
   * 
   * @return Error builder verifying creation of workspace
   */
  private ErrorBuilder storeWorkspace() {
    // Create workspace instance
    ErrorBuilder eb = new ErrorBuilder();
    WorkspaceInstance wsi = getWorkspaceInstance(eb);

    // Attempt to store workspace, if successful, load it into the model
    if (!eb.isError()) {
      boolean write = true;
      // If this is an update, just overwrite the existing file, otherwise check
      Path newPath = Paths.get(wsi.getWorkspaceFile().toURI());
      Path curPath = base == null ? null : Paths.get(base.getWorkspaceFile().toURI());
      boolean overwriteCheck = !newPath.equals(curPath);
      if (overwriteCheck && Files.exists(newPath)) {
        int res = JOptionPane.showConfirmDialog(null, String.format(
            "The workspace '%s' already exists in this location, would you like to overwrite it?",
            wsi.name), "Overwrite", JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) {
          write = false;
        }
      }
      // Write the created workspace to the file system
      if (write) {
        // Store the workspace
        eb.append(
            WorkspaceHandler.storeWorkspace(wsi, WorkspaceHandler.PRIVATE_KEY.toCharArray(), true));
        // Delete the existing workspace if it was moved
        if (base != null && !newPath.equals(curPath)) {
          try {
            Files.delete(curPath);
          } catch (IOException e) {
            System.err.println("Unable to delete old workspace");
          }
        }
        if (!eb.isError()) {
          Window frmCurrent = SwingUtilities.getWindowAncestor(CreateWorkspacePanel.this);
          frmCurrent.setVisible(false);
          frmCurrent.dispose();
          // Load the workspace
          controller.workspace.setWorkspace(wsi);
        }
      }
    }

    if (eb.isError()) {
      JOptionPane.showMessageDialog(null, eb.listComments("Creating Workspace"),
          "Creating Workspace", JOptionPane.ERROR_MESSAGE);
    }
    return eb;
  }

}
