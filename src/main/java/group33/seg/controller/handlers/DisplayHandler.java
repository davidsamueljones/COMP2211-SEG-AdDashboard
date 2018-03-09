package group33.seg.controller.handlers;

import java.awt.EventQueue;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.view.DashboardView;
import group33.seg.view.structure.DashboardFrame;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.Accessibility.Appearance;

// TODO: Not a fan of this name but functionality doesn't fall under View
public class DisplayHandler {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  private boolean fontScalingOutdated = false;
  
  
  /**
   * Instantiate a display handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public DisplayHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }

  /**
   * If the view does not have a dashboard instance, create a new one and show it.
   */
  public void openDashboard() {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        if (mvc.view.getDashboard() != null) {
          System.err.println("Dashboard already open");
          return;
        }

        try {
          // Ensure view settings are enforced
          applyViewSettings();
          // Create and show a new dashboard
          DashboardFrame newDashboard = new DashboardFrame(mvc.controller);
          mvc.view.setDashboard(newDashboard);
          newDashboard.setVisible(true);
          fontScalingOutdated = false;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * If the view has a dashboard instance, close it and remove it.
   */
  public void closeDashboard() {
    DashboardFrame dashboard = mvc.view.getDashboard();

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        if (dashboard == null) {
          System.err.println("Dashboard not open");
          return;
        }

        dashboard.setVisible(false);
        dashboard.dispose();
        mvc.view.setDashboard(null);
      }
    });

  }

  /**
   * Do close and open behaviour.
   */
  public void reloadDashboard() {
    closeDashboard();
    openDashboard();
  }

  /**
   * Use the current user preferences to enforce view settings.
   */
  public void applyViewSettings() {
    // Ensure cross-platform appearance is enabled
    Accessibility.setAppearance(Appearance.PLATFORM);
    // Apply font scaling
    applyUIFontScaling();
  }

  public void applyUIFontScaling() {
    double scaling = mvc.controller.settings.prefs.getDouble(
        SettingsHandler.FONT_SCALING, Accessibility.DEFAULT_SCALING);
    applyUIFontScaling(scaling);
  }
  
  public void applyUIFontScaling(double scaling) {
    Accessibility.scaleDefaultUIFontSize(scaling);
    fontScalingOutdated = true;
  }
  
  public void setUIFontScaling(double newScaling) {
    double currentScaling = mvc.controller.settings.prefs.getDouble(
            SettingsHandler.FONT_SCALING, Accessibility.DEFAULT_SCALING);

    if (currentScaling != newScaling) {
      mvc.controller.settings.prefs.putDouble(SettingsHandler.FONT_SCALING, newScaling);
      fontScalingOutdated = true;
    }
    
  }
  
  public boolean isUIFontScalingOutdated() {
    return fontScalingOutdated;
  }

}
