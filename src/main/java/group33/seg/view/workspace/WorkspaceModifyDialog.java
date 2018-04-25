package group33.seg.view.workspace;

import java.awt.Dimension;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import group33.seg.controller.DashboardController;

public class WorkspaceModifyDialog extends JDialog {
  private static final long serialVersionUID = 5491279897585497599L;

  private DashboardController controller;

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public WorkspaceModifyDialog(Window parent, DashboardController controller) {
    super(parent, "Workspace Modifier");

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
  }

  private void initGUI() {
    CreateWorkspacePanel pnlContent =
        new CreateWorkspacePanel(controller, controller.workspace.getWorkspaceInstance());
    pnlContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setContentPane(pnlContent);
  }

}
