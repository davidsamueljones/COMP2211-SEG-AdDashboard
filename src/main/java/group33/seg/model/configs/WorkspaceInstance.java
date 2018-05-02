package group33.seg.model.configs;

import java.io.File;
import java.nio.file.Path;

public class WorkspaceInstance {
  public static final String WORKSPACE_EXT = "adw";

  /** Name of the workspace */
  public final String name;

  /** Where the workspace should be saved to */
  public final String directory;

  /** Workspace this instance relates to */
  public WorkspaceConfig workspace;

  /**
   * Initialise a workspace without storing an actual workspace configuration using a raw path.
   * 
   * @param path Path pointing to workspace instance
   */
  public WorkspaceInstance(Path path) {
    this(path.getFileName().toString().replace(".adw", "").replace('_', ' '),
        path.getParent().toString());
  }

  /**
   * Initialise a workspace without storing an actual workspace configuration.
   * 
   * @param name Name (identifier) of workspace
   * @param directory Path to storage location for workspace
   */
  public WorkspaceInstance(String name, String directory) {
    this(name, directory, null);
  }

  /**
   * Initialise a fully defined workspace instance.
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
   * Instance implementation of {@link #getWorkspaceFilename()}.
   * 
   * @return Generated filename
   */
  public String getWorkspaceFilename() {
    return getWorkspaceFilename(name);
  }

  /**
   * Instance implementation of {@link #getWorkspaceFile()}
   * 
   * @return Generated file object
   */
  public File getWorkspaceFile() {
    return getWorkspaceFile(directory, name);
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
