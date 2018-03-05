package group33.seg.view.utilities;

import java.awt.Font;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * General utility class holding methods to set application-wide accessibility options or helper
 * methods to apply localised accessibility.
 */
public class Accessibility {

  /**
   * Set system wide look and feel options using an appearance selection. OS specific rules/flags
   * should be applied here.
   * 
   * @param appearance Appearance selection to use
   */
  public static void setAppearance(Appearance appearance) {
    // Get corresponding appearances LaF
    String className = null;
    if (appearance != null) {
      switch (appearance) {
        case NIMBUS:
          // Font scaling does not work
          className = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
          break;
        case PLATFORM:
          // Natural across platforms (no good HiDPI Windows support)
          className = UIManager.getSystemLookAndFeelClassName();
          break;
      }
    }

    // Attempt to set look and feel
    try {
      UIManager.setLookAndFeel(className);
    } catch (Exception e) {
      // List look and feels for debug
      System.err.println("Error setting LaF - Available LaFs:");
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        System.err.println(info);
      }
    }

  }

  /**
   * Scale the font sizes of all objects contained in the UIManager (that utilise a font). This will
   * only affect objects instantiated in the future and hence should be executed before any
   * component generation.
   * 
   * @param scale Scaling factor to apply
   */
  public static void scaleDefaultUIFontSize(double scale) {
    // Iterate over all UI object types
    Set<Object> objects = UIManager.getLookAndFeelDefaults().keySet();
    for (Object object : objects) {

      // Ignore scale request if object doesn't contain a font
      if (object == null || !object.toString().toLowerCase().contains("font")) {
        continue;
      }
      // Rescale font
      Font font = UIManager.getDefaults().getFont(object);
      if (font != null) {
        // Update object in UI manager
        UIManager.put(object, scaleFont(font, scale));
      }
    }
  }

  /**
   * Scale a JComponent's text by scaling its current font.
   * 
   * @param component JComponent to scale font of
   * @param scale Scaling factor to apply
   */
  public static void scaleJComponentFontSize(JComponent component, double scale) {
    Font font = component.getFont();
    if (font != null) {
      font = scaleFont(component.getFont(), scale);
      // Strip UIManager LaF data to avoid font scaling not being enforced 
      font = new Font(font.getAttributes());
      component.setFont(font);
    }
  }

  /**
   * Scale a font by a given scaling factor.
   * 
   * @param font Font to create a scaled version of
   * @param scale Scaling factor to apply
   * @return Scaled font using original font properties
   */
  public static Font scaleFont(Font font, double scale) {
    return font.deriveFont((float) (font.getSize() * scale));
  }

  /**
   * Enumeration of valid appearance options.
   */
  public enum Appearance {
    PLATFORM, NIMBUS
  }

}
