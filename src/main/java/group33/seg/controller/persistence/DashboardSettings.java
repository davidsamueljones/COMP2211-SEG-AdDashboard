package group33.seg.controller.persistence;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import group33.seg.view.utilities.Accessibility;

public class DashboardSettings {
  public static final String CUR_CAMPAIGN = "cur_campaign";
  public static final String FONT_SCALING = "font_scaling";
  
  /** Static access to preferences */
  public static final DashboardSettings cur = new DashboardSettings();
 
  public final Preferences prefs;

  public DashboardSettings() {
    boolean newPrefs = false;

    try {
      // Check whether to initialise defaults
      newPrefs = !Preferences.userRoot().nodeExists(this.getClass().getName());
    } catch (BackingStoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // Get preference node
    prefs = Preferences.userRoot().node(this.getClass().getName());

    // Initialise defaults if preference node did not exist
    if (newPrefs) {
      setDefaults();
    }
    
  }

  public void clearPreferences() {
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
