package group33.seg.controller.handlers;

import java.awt.EventQueue;
import com.rits.cloning.Cloner;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.view.output.HistogramView;
import group33.seg.view.output.LineGraphView;

public class HistogramHandler implements GraphHandlerInterface<HistogramConfig> {

  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Graph being controlled */
  protected final HistogramView view;

  /** Currently loaded graph */
  protected HistogramConfig graph = null;

  /**
   * Instantiate a histogram handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   * @param view View being handled
   */
  public HistogramHandler(DashboardMVC mvc, HistogramView view) {
    this.mvc = mvc;
    this.view = view;
  }

  @Override
  public void reloadGraph() {
    displayGraph(this.graph);
  }

  @Override
  public void displayGraph(HistogramConfig graph) {
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
    updateGraphData(graph);
    this.graph = graph;
  }

  @Override
  public void clearGraph() {
    mvc.controller.graphs.updateProgress("Clearing graph...");
    this.graph = null;
    EventQueue.invokeLater(() -> view.clearGraph());
  }

  /**
   * Update the graph's view's properties, this includes information such as title and axis labels.
   * No data fetches should occur here.
   * 
   * @param graph Graph properties to load
   */
  private void setGraphProperties(HistogramConfig graph) {
    EventQueue.invokeLater(() -> view.setGraphProperties(graph));
  }

  /**
   * Update the view's displayed data.
   * 
   * @param graph Graph for which to load data
   */
  private void updateGraphData(HistogramConfig graph) {

  }


}
