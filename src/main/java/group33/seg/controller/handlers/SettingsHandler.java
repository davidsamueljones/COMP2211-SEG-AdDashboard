package group33.seg.controller.handlers;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import group33.seg.view.utilities.Accessibility;

public class SettingsHandler {
  public static final String CUR_CAMPAIGN = "cur_campaign";
  public static final String FONT_SCALING = "font_scaling";

  public final Preferences prefs;

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

  public void clear() {
    try {
      prefs.removeNode();
    } catch (BackingStoreException e) {
      System.err.println("Unable to clear preferences");
    }
  }

  public void setDefaults() {
    // Assign default preferences
    prefs.remove(CUR_CAMPAIGN);
    prefs.putDouble(FONT_SCALING, Accessibility.DEFAULT_SCALING);
  }

}
