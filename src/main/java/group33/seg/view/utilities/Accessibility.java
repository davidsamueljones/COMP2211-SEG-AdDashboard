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
  public static final double MAX_SCALING = 2;
  public static final double MIN_SCALING = 0.5;
  public static final double DEFAULT_SCALING = 1;

  public static double currentScaling = DEFAULT_SCALING;

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
   * component generation. Scalings apply to defaults and do not stack.
   *
   * @param scale Scaling factor to apply to default
   */
  public static void scaleDefaultUIFontSize(double scale) {
    // Normalise scale, tracking new
    double temp = currentScaling;
    currentScaling = scale;
    scale /= temp;

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
      component.setFont(stripLaF(font));
    }
  }

  /**
   * Strip LaF information to avoid font attributes not being enforced.
   * 
   * @param font Font to strip information for
   * @return Font with same attributes as input but no LaF data
   */
  public static Font stripLaF(Font font) {
    return new Font(font.getAttributes());
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
   * Get a a font reduced by a given scaling factor.
   *
   * @param font Font to create a scaled version of
   * @param currentScaling Scaling factor to reverse
   * @return Scaled font using original font properties
   */
  public static Font unscaleFont(Font font, double currentScaling) {
    return Accessibility.scaleFont(font, 1 / currentScaling);
  }

  /** Enumeration of valid appearance options. */
  public enum Appearance {
    PLATFORM, NIMBUS
  }

}
