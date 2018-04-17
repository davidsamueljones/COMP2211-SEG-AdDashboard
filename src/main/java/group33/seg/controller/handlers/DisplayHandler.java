package group33.seg.controller.handlers;

import java.awt.Dialog.ModalExclusionType;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.view.definitions.DefinitionFrame;
import group33.seg.view.structure.DashboardFrame;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.Accessibility.Appearance;

public class DisplayHandler {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  private volatile boolean definitionsVisible = false;

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
    EventQueue.invokeLater(() -> {
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
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * If the view has a dashboard instance, close it and remove it.
   */
  public void closeDashboard() {
    DashboardFrame dashboard = mvc.view.getDashboard();

    EventQueue.invokeLater(() -> {
      if (dashboard == null) {
        System.err.println("Dashboard not open");
        return;
      }
      // Unattach views from controller
      mvc.controller.graphs.setLineGraphView(null);
      mvc.controller.statistics.setView(null, false);
      
      dashboard.setVisible(false);
      dashboard.dispose();
      mvc.view.setDashboard(null);
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
   * @return Whether the controlled definition dialog is visible
   */
  public boolean isDefinitionWindowVisible() {
    return definitionsVisible;
  }

  /**
   * Show the view's floating definitions window, if it does not exist, create it ensuring the
   * correct properties are set.
   */
  public void showDefinitions() {
    EventQueue.invokeLater(() -> {
      DefinitionFrame definitions = mvc.view.getDefinitions();
      if (definitions == null) {
        definitions = new DefinitionFrame();
        definitions.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        definitions.setAlwaysOnTop(true);
        definitions.setFocusableWindowState(false);
        definitions.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        definitions.addComponentListener(new ComponentAdapter() {
          @Override
          public void componentShown(ComponentEvent e) {
            definitionsVisible = true;
          }

          @Override
          public void componentHidden(ComponentEvent e) {
            definitionsVisible = false;
          }
        });
        mvc.view.setDefinitions(definitions);
      }

      definitions.setVisible(true);
    });
  }
  
  /**
   * Hide the controlled definition dialog.
   */
  public void hideDefinitions() {
    EventQueue.invokeLater(() -> {
      DefinitionFrame definitions = mvc.view.getDefinitions();
      if (definitions != null) {
        mvc.view.getDefinitions().setVisible(false);
      }
    });
  }

  /**
   * Use the current user preferences to enforce view settings.
   */
  public void applyViewSettings() {
    // Ensure cross-platform appearance is enabled
    Accessibility.setAppearance(Appearance.PLATFORM);
    // Apply font scaling
    applyUIFontScaling();
    // Apply any other global properties
    ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
  }

  /**
   * Apply the scaling currently stored by the settings handler.
   */
  public void applyUIFontScaling() {
    double scaling = mvc.controller.settings.prefs.getDouble(SettingsHandler.FONT_SCALING,
        Accessibility.DEFAULT_SCALING);
    applyUIFontScaling(scaling);
  }

  /**
   * Apply a given font scale to all GUI elements.
   * 
   * @param scaling Scaling to apply
   */
  public void applyUIFontScaling(double scaling) {
    Accessibility.scaleDefaultUIFontSize(scaling);
    mvc.controller.graphs.setFontScale(scaling);
  }

}
