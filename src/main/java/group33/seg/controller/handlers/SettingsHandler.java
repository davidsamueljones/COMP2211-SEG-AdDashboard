package group33.seg.controller.handlers;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import group33.seg.view.utilities.Accessibility;

public class SettingsHandler {
  public static final String FONT_SCALING = "font_scaling";
  public static final String BUFFERED_GRAPH = "buffered_graph";

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
    }
  }

}
