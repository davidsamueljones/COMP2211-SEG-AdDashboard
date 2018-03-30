package group33.seg.controller;

import group33.seg.controller.handlers.CampaignImportHandler;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.controller.handlers.DisplayHandler;
import group33.seg.controller.handlers.GraphHandler;
import group33.seg.controller.handlers.SettingsHandler;
import group33.seg.controller.handlers.StatisticHandler;
import group33.seg.controller.handlers.WorkspaceHandler;
import group33.seg.model.DashboardModel;
import group33.seg.view.DashboardView;

public class DashboardController {

  /** MVC that controller is residing over */
  private final DashboardMVC mvc;

  /** Handler for general GUI updates */
  public final DisplayHandler display;

  /** Handler for database access */
  public final DatabaseHandler database;

  /** Handler for campaign importing */
  public final CampaignImportHandler imports;

  /** Handler for graph updates */
  public final GraphHandler graphs;

  /** Handler for statistic updates */
  public final StatisticHandler statistics;
  
  /** Handler for settings */
  public final SettingsHandler settings;
  
  /** Handler for workspace */
  public final WorkspaceHandler workspace;
  
  /**
   * Instantiate a dashboard controller to control interactions between a given view and model. Uses
   * suite of sub-controllers to break down functionality. Controller creates an MVC instance that
   * is kept private to avoid accidental view-model coupling. The sub-controllers have access to
   * this model.
   * 
   * @param model Model to control
   * @param view View to control
   */
  public DashboardController(DashboardModel model, DashboardView view) {
    this.mvc = new DashboardMVC(model, view, this);

    // Create sub-controllers with access to MVC
    display = new DisplayHandler(mvc);
    database = new DatabaseHandler(mvc);
    imports = new CampaignImportHandler(mvc);
    graphs = new GraphHandler(mvc);
    statistics = new StatisticHandler(mvc);
    settings = new SettingsHandler();
    workspace = new WorkspaceHandler(mvc);
  }

  /**
   * Class used by the parent controller (which already has knowledge of the full system) to pass
   * full knowledge of the system to sub-controllers.
   */
  public class DashboardMVC {
    /** MVC model */
    public final DashboardModel model;
    /** MVC view */
    public final DashboardView view;
    /** MVC controller */
    public final DashboardController controller;

    /**
     * Instantiate a MVC with knowledge of the whole system.
     * 
     * @param model Model to use in MVC
     * @param view View to use in MVC
     * @param controller Controller to use in MVC
     */
    public DashboardMVC(DashboardModel model, DashboardView view, DashboardController controller) {
      this.model = model;
      this.view = view;
      this.controller = controller;
    }

  }

}
