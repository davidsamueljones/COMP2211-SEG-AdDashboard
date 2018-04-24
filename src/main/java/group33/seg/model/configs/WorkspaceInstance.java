package group33.seg.model.configs;

import java.io.File;

public class WorkspaceInstance {
  public static final String WORKSPACE_EXT = "adw";

  /** Name of the workspace */
  public final String name;

  /** Where the workspace should be saved to */
  public final String directory;

  /** Workspace this instance relates to */
  public final WorkspaceConfig workspace;

  /**
   * Initialise a workspace.
   * 
   * @param name Name (identifier) of workspace
   * @param directory Path to storage location for workspace
   * @param workspace Workspace instance relates to
   */
  public WorkspaceInstance(String name, String directory, WorkspaceConfig workspace) {
    this.name = name;
    this.directory = directory;
    this.workspace = workspace;
  }

  /**
   * A workspace configuration is normally stored as its identifier with no spaces and the workspace
   * extension. Passing this method an identifier will generate the corresponding filename.
   * 
   * @param identifier Identifier for which to generate a filename for
   * @return Generated filename
   */
  public static String getWorkspaceFilename(String identifier) {
    return String.format("%s.%s", identifier.replace(' ', '_'), WORKSPACE_EXT);
  }
   
  /**
   * Get a file object that maps to the given workspace storage properties.
   * 
   * @param directory Directory for workspace
   * @param identifier Identifier for workspace
   * @return Generated file object
   */
  public static File getWorkspaceFile(String directory, String identifier) {
    String filename = getWorkspaceFilename(identifier);
    File file = new File(directory, filename);
    return file;  
  }

}
