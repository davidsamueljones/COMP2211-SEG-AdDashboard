package group33.seg.model;

import group33.seg.model.configs.WorkspaceConfig;
import group33.seg.model.configs.WorkspaceInstance;

public class DashboardModel {

  /** The workspace instance stored by the model */
  private WorkspaceInstance workspace;

  /**
   * @return The model's workspace instance's workspace
   */
  public WorkspaceConfig getWorkspace() {
    return (workspace == null ? null : workspace.workspace);
  }

  /**
   * @return The model's workspace instance's
   */
  public WorkspaceInstance getWorkspaceInstance() {
    return workspace;
  }

  /**
   * Set the model's workspace configuration using default fields for identifying the workspace
   * instance.
   * 
   * @param workspace Workspace configuration
   */
  public void setWorkspace(WorkspaceConfig workspace) {
    setWorkspace(new WorkspaceInstance(null, null, workspace));
  }

  /**
   * Set the model's fully defined workspace instance.
   * 
   * @param workspace Workspace instance
   */
  public void setWorkspace(WorkspaceInstance workspace) {
    this.workspace = workspace;
  }

}
