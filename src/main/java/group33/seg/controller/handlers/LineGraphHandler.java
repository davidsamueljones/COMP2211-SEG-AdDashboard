package group33.seg.controller.handlers;

import java.util.List;
import com.rits.cloning.Cloner;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.handlers.GraphsHandler.Update;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.view.output.LineGraphView;

public class LineGraphHandler implements GraphHandlerInterface<LineGraphConfig> {
  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Graph being controlled */
  protected final LineGraphView view;

  /** Currently loaded graph */
  protected LineGraphConfig graph = null;

  /**
   * Instantiate a line graph handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   * @param view View being handled
   */
  public LineGraphHandler(DashboardMVC mvc, LineGraphView view) {
    this.mvc = mvc;
    this.view = view;
  }

  @Override
  public void reloadGraph() {
    displayGraph(this.graph);
  }

  /**
   * Update the line graph displayed in the view. As an update, behaviour means that if the
   * currently displayed graph is the same as the input graph, only changes will be reflected. In
   * the case that the current graph does not exist or is different, behaviour is the same as that
   * of loadLineGraph.
   * 
   * @param graph Graph to update the view with
   */
  @Override
  public void displayGraph(LineGraphConfig graph) {
    // Clear graph if not an update
    if (this.graph == null || !this.graph.uuid.equals(graph.uuid)) {
      clearGraph();
    }

    // Create a copy of the input graph, this allows any changes to the original passed object to
    // be handled by the handler's update structure appropriately on a load
    Cloner cloner = new Cloner();
    graph = cloner.deepClone(graph);

    // Configure graph view
    setGraphProperties(graph);
    updateGraphLines(graph.lines);
    this.graph = graph;
  }

  /**
   * Remove any lines from graph.
   */
  @Override
  public void clearGraph() {
    System.out.println("CLEARING GRAPH");
    this.graph = null;
    view.clearGraph();
  }

  /**
   * Update the graph's view's properties, this includes information such as title and axis labels.
   * No data fetches should occur here.
   * 
   * @param graph Graph properties to load
   */
  private void setGraphProperties(LineGraphConfig graph) {
    view.setGraphProperties(graph);
  }

  /**
   * Handle line update, first ensuring any outdated lines are removed before applying updates and
   * adding new lines.
   * 
   * @param lines Lines to use for update
   */
  private void updateGraphLines(List<LineConfig> lines) {
    removeOutdatedLines(lines);
    loadLines(lines);
  }

  /**
   * Using the list of existing lines, update or add a set of lines to the graph view.
   * 
   * @param lines Lines to load
   */
  private void loadLines(List<LineConfig> lines) {
    // Get list of existing lines
    List<LineConfig> exLines = (this.graph == null ? null : graph.lines);
    for (LineConfig line : lines) {
      // Ensure campaign is current with workspace (FIXME: INCREMENT 2 FEATURE)
      line.query.campaign = mvc.controller.workspace.getCampaign();
      // Update line if it exists, otherwise add it
      int idx = (exLines == null ? -1 : exLines.indexOf(line));
      if (idx >= 0) {
        updateLine(line, getLineUpdate(exLines.get(idx), line));
      } else {
        addLine(line);
      }
    }
  }

  /**
   * Using the list of existing lines, remove those from the view that should no longer be displayed
   * based on provided updated lined information.
   * 
   * @param lines Updated line information
   */
  private void removeOutdatedLines(List<LineConfig> lines) {
    // Get list of existing lines
    List<LineConfig> exLines = (this.graph == null ? null : graph.lines);
    if (exLines == null) {
      return;
    }
    // Remove existing lines that no longer exist after update
    for (LineConfig exLine : exLines) {
      int idx = lines.indexOf(exLine);
      if (idx < 0) {
        removeLine(exLine);
      }
    }
  }

  /**
   * Query data for the given configuration and add the line to the current view.
   * 
   * @param line Line configuration to use
   */
  private void addLine(LineConfig line) {
    updateLine(line, Update.FULL);
  }

  /**
   * Update a line in the view using the given update options. On a DATA update the line's query
   * must be executed to fetch up-to-date data first. On a PROPERTIES update only the view must be
   * updated.
   * 
   * @param line Line configuration to use
   * @param update Update options
   */
  private void updateLine(LineConfig line, Update update) {
    // Add line record in view if it doesn't exist
    view.addLine(line);

    // Do required view updates
    if (update == Update.FULL || update == Update.PROPERTIES) {
      System.out.println("UPDATING PROPERTIES: " + line.identifier);
      view.setLineProperties(line);
    }
    if (update == Update.FULL || update == Update.DATA) {
      System.out.println("UPDATING DATA: " + line.identifier);
      // TODO: Improve performance with threading?
      MetricQueryResponse mqr = mvc.controller.database.getQueryResponse(line.query);
      view.setLineData(line, mqr.getResult());
    }

  }

  /**
   * Remove a line from the current view.
   * 
   * @param line Line to remove
   */
  private void removeLine(LineConfig line) {
    System.out.println("REMOVING: " + line.identifier);
    view.removeLine(line);
  }

  /**
   * Given an original and an updated line configuration, determine what type of update is required
   * to update the graph in the least cost-manner. As in, if only line properties have changed it is
   * best to not requery data due to computational cost.
   * 
   * @param original Line configuration to use as base
   * @param updated Line configuration to compare with base
   * @return Update type as represented by Update enumeration
   */
  public static Update getLineUpdate(LineConfig original, LineConfig updated) {
    // Check for any changes in querying that may change data
    boolean data = true;
    data &= (original.query == null ? (original.query == null)
        : original.query.isEquals(updated.query));

    // Check for any changes of properties
    boolean properties = true;
    properties &= (original.identifier == null ? (original.identifier == null)
        : original.identifier.equals(updated.identifier));
    properties &=
        (original.color == null ? (original.color == null) : original.color.equals(updated.color));
    properties &= original.thickness == updated.thickness;
    properties &= original.hide == updated.hide;

    // Determine update required
    if (!(data || properties)) {
      return Update.FULL;
    }
    if (!data) {
      return Update.DATA;
    }
    if (!properties) {
      return Update.PROPERTIES;
    }
    return Update.NOTHING;
  }

}
