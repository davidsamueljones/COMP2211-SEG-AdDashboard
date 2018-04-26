package group33.seg.controller.handlers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import group33.seg.view.utilities.Accessibility;

public class SettingsHandler {
  public static final String FONT_SCALING = "font_scaling";
  public static final String BUFFERED_GRAPH = "buffered_graph";
  public static final String RECENT_WORKSPACE_COUNT = "recent_workspace_count";
  public static final String RECENT_WORKSPACE = "recent_workspace";

  /** Handled preference node */
  public final Preferences prefs;

  /**
   * Create a new settings handler, fetching or creating the relevent preference node.
   */
  public SettingsHandler() {
    boolean newPrefs = false;

    try {
      // Check whether to initialise defaults
      newPrefs = !Preferences.userRoot().nodeExists(this.getClass().getName());
    } catch (BackingStoreException e) {
      // Do nothing, leaving settings unchanged
    }

    // Get preference node
    prefs = Preferences.userRoot().node(this.getClass().getName());

    // Initialise defaults if preference node did not exist
    if (newPrefs) {
      setDefaults();
    }
  }

  /**
   * Delete current preferences node if it exists
   */
  public void clear() {
    try {
      if (prefs != null) {
        prefs.removeNode();
      }
    } catch (BackingStoreException e) {
      System.err.println("Unable to clear preferences");
    }
  }

  /**
   * Apply default settings to current node.
   */
  public void setDefaults() {
    if (prefs != null) {
      // Assign default preferences
      prefs.putDouble(FONT_SCALING, Accessibility.DEFAULT_SCALING);
      prefs.putBoolean(BUFFERED_GRAPH, true);
      prefs.putInt(RECENT_WORKSPACE_COUNT, 0);
    }
  }

  /**
   * Add a new workspace to be stored as a recent workspace. If the workspace already exists the
   * position will be updated to be most recent.
   * 
   * @param path Path to add as recent
   */
  public void addRecentWorkspace(String path) {
    int count = prefs.getInt(RECENT_WORKSPACE_COUNT, 0);
    prefs.put(RECENT_WORKSPACE + count, path);
    prefs.putInt(RECENT_WORKSPACE_COUNT, count + 1);
    cleanupRecentWorkspaces();
  }

  /**
   * Get a list of recent workspaces that are available, this will not include any workspaces that
   * do not exist.
   * 
   * @return List of workspaces (most recent first)
   */
  public List<String> getRecentWorkspaces() {
    cleanupRecentWorkspaces();

    int count = prefs.getInt(RECENT_WORKSPACE_COUNT, 0);
    List<String> recent = new ArrayList<String>();
    for (int i = 0; i < count; i++) {
      recent.add(prefs.get(RECENT_WORKSPACE + i, null));
    }
    Collections.reverse(recent);
    return recent;
  }

  /**
   * Clean up the stored workspaces, this entails: removing workspace locations that no longer exist
   * and removing duplicates. Duplicate removal behaviour is such that the earlier occurrences are
   * removed. This should be called after any manipulation of stored workspaces.
   */
  private void cleanupRecentWorkspaces() {
    // Get list of current workspaces (removing from preferences)
    int count = prefs.getInt(RECENT_WORKSPACE_COUNT, 0);
    List<String> recent = new ArrayList<String>();
    for (int i = 0; i < count; i++) {
      String workspace = prefs.get(RECENT_WORKSPACE + i, null);
      prefs.remove(RECENT_WORKSPACE + i);
      boolean exists = workspace != null && Files.exists(Paths.get(workspace));
      if (exists) {
        recent.remove(workspace);
        recent.add(workspace);
      }
    }
    // Put workspaces back into preferences
    for (int i = 0; i < recent.size(); i++) {
      prefs.put(RECENT_WORKSPACE + i, recent.get(i));
    }
    prefs.putInt(RECENT_WORKSPACE_COUNT, recent.size());
  }

}
