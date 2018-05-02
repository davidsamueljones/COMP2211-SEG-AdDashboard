package group33.seg.controller.handlers;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import com.rits.cloning.Cloner;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.types.MetricQueryResponse;
import group33.seg.lib.Pair;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.view.output.HistogramView;

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
    boolean queryChanged = !sameQuery(this.graph, graph);
    if (!sameBins(this.graph, graph)) {
      List<Integer> bins = graph.bins;
      if (bins == null) {
        bins = new ArrayList<>(graph.binCount);
        for (int i = 0; i < graph.binCount; i++) {
          bins.add(1);
        }
      }
      List<Double> normalisedBins = HistogramConfig.getNormalisedBins(bins);
      EventQueue.invokeLater(() -> view.makeBins(normalisedBins, queryChanged));
    }

    if (!sameQuery(this.graph, graph)) {
      MetricQueryResponse mqr = mvc.controller.database.getQueryResponse(graph.query);
      // Wait for result outside of EDT
      List<Pair<String, Number>> results = mqr.getResult();
      List<Double> values = new ArrayList<>(results.size());
      for (Pair<String, Number> result : results) {
        values.add(result.value.doubleValue());
      }
      EventQueue.invokeLater(() -> view.setGraphData(values));
    }
  }


  /**
   * Determine whether two configurations require the same query.
   * 
   * @param original Configuration to compare against
   * @param updated Configuration to compare
   * @return Whether the queries the configurations use are the same
   */
  private static boolean sameQuery(HistogramConfig original, HistogramConfig updated) {
    if (original == null && updated != null || original != null && updated == null) {
      return false;
    } else if (original == null && updated == null) {
      return true;
    }
    return (original.query == null ? (updated.query == null)
        : original.query.isEqual(updated.query));
  }

  /**
   * Determine whether two configurations use the same bin setup.
   * 
   * @param original Configuration to compare against
   * @param updated Configuration to compare
   * @return Whether the bin method, and bin values are the same
   */
  private static boolean sameBins(HistogramConfig original, HistogramConfig updated) {
    if (original == null && updated != null || original != null && updated == null) {
      return false;
    } else if (original == null && updated == null) {
      return true;
    }
    boolean same = true;
    if (original.bins == null && updated.bins == null) {
      if (original.binCount != updated.binCount) {
        same = false;
      }
    } else {
      same &= (original.bins == null ? (updated.bins == null) : original.bins.equals(updated.bins));
    }
    return same;
  }

}
