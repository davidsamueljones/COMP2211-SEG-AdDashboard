package group33.seg.controller.handlers;

import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.types.GraphVisitor;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.view.output.LineGraphView;

/**
 * Handler class for processing graph configurations updating relevant views. Makes use of the
 * visitor pattern for a uniform interface handling the superclass of GraphConfig. It is expected
 * that only a single graph view instance owned by this handler is visible at one time.
 */
public class GraphsHandler {
  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Line graph handler */
  private LineGraphHandler lineGraph;

  /** Handler currently in use */
  private GraphHandlerInterface<?> currentHandler = null;

  /** Font scaling to apply to textual elements in charts */
  private double scale = 1;

  /**
   * Instantiate a graph handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public GraphsHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }

  /**
   * Create a new line handler making use of the given view output.
   */
  public void setLineGraphView(LineGraphView view) {
    view.applyFontScale(scale);
    lineGraph = new LineGraphHandler(mvc, view);
  }

  /**
   * Reloads the graph using the current handler.
   */
  public void reloadGraph() {
    if (currentHandler != null) {
      currentHandler.reloadGraph();
    }
  }

  /**
   * Display the given graph configuration with the appropriate graph handler. Default behaviour of
   * updating without forcing recreation is applied.
   * 
   * @param graph Graph to configure and display
   */
  public void displayGraph(GraphConfig graph) {
    displayGraph(graph, false);
  }

  /**
   * Display the given graph configuration with the appropriate graph handler. If the graph handler
   * required is different from the current graph handler, the contents of the existing handler will
   * be cleared to avoid partial updates on later calls. If the clear flag is set the graph handler
   * about to be used will be cleared to force full recreation otherwise an incremental update may
   * be applied.
   * 
   * @param graph Graph to display
   * @param clear Whether full recreation should be enforced
   */
  public void displayGraph(GraphConfig graph, boolean clear) {
    // Handle display behaviour depending on the type of graph
    graph.accept(new GraphVisitor() {

      @Override
      public void visit(LineGraphConfig graph) {
        handleUpdate(lineGraph, clear);
        lineGraph.displayGraph(graph);
      }

      /**
       * Handle graph clearing behaviour and update the graph handler.
       * 
       * @param next The graph handler about to be used
       * @param clear Whether to clear the graph handler that is about to be used
       */
      private void handleUpdate(GraphHandlerInterface<?> next, boolean clear) {
        if (currentHandler != null && currentHandler != next) {
          currentHandler.clearGraph();
        } else if (clear) {
          next.clearGraph();
        }
        currentHandler = lineGraph;
      }
    });

  }

  /**
   * Set font scale to apply to all current and future charts.
   * 
   * @param scale Scale to use
   */
  public void setFontScale(double scale) {
    this.scale = scale;
    if (lineGraph != null) {
      lineGraph.view.applyFontScale(scale);
    }
  }

  /**
   * Enumeration of what type of update has occurred to a graph or graph element.
   */
  public enum Update {
    FULL, /* Both properties and data have changed */
    PROPERTIES, /* Just properties have changed */
    DATA, /* Just data has changed */
    NOTHING; /* No changes */
  }

}
