package group33.seg.controller.handlers;

import java.util.HashSet;
import java.util.Set;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.controller.types.GraphVisitor;
import group33.seg.controller.utilities.ProgressListener;
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
  private LineGraphHandler lineGraphHandler;

  /** Handler currently in use */
  private GraphHandlerInterface<?> currentHandler = null;

  /** Listeners to alert about progress */
  private final Set<ProgressListener> progressListeners = new HashSet<>();
  
  /** Font scaling to apply to textual elements in charts */
  private double scale = 1;

  /**
   * Instantiate a graph handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public GraphsHandler(DashboardMVC mvc) {
    this.mvc = mvc;
    // Print messages to stdout for debug
    addProgressListener(new ProgressListener() {
      @Override
      public void progressUpdate(String update) {
        System.out.println(update);
      }
    });
  }

  /**
   * Create a new line graph handler making use of the given view output.
   */
  public void setLineGraphView(LineGraphView view) {
    if (view == null) {
      lineGraphHandler = null;
    } else {
      view.applyFontScale(scale);
      lineGraphHandler = new LineGraphHandler(mvc, view);
    }
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
    // Do load on worker thread, updating progress listeners appropriately
    Thread workerThread = new Thread(() -> {
      updateProgress("Loading graph into view...");
      alertStart();
      
      // Handle display behaviour depending on the type of graph
      graph.accept(new GraphVisitor() {

        @Override
        public void visit(LineGraphConfig graph) {
          handleUpdate(lineGraphHandler, clear);
          lineGraphHandler.displayGraph(graph);
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
          currentHandler = lineGraphHandler;
        }
      });
      
      updateProgress("Finished loading graph into view");
      alertFinished();
    });

    workerThread.start();
  }

  /**
   * Set font scale to apply to all current and future charts.
   * 
   * @param scale Scale to use
   */
  public void setFontScale(double scale) {
    this.scale = scale;
    if (lineGraphHandler != null) {
      lineGraphHandler.view.applyFontScale(scale);
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

  /**
   * @param progressListener Progress listener to start sending alerts to
   */
  public void addProgressListener(ProgressListener progressListener) {
    progressListeners.add(progressListener);
  }

  /**
   * @param progressListener Progress listener to no longer alert
   */
  public void removeProgressListener(ProgressListener progressListener) {
    progressListeners.remove(progressListener);
  }

  /**
   * Helper function to alert all listeners that a load has started.
   */
  protected void alertStart() {
    for (ProgressListener listener : progressListeners) {
      listener.start();
    }
  }

  /**
   * Helper function to alert all listeners that a load finished.
   */
  protected void alertFinished() {
    for (ProgressListener listener : progressListeners) {
      listener.finish(true);
    }
  }
  
  /**
   * Helper function to alert all listeners of a progress update.
   *
   * @param update Textual update on progress
   */
  protected void updateProgress(String update) {
    for (ProgressListener listener : progressListeners) {
      listener.progressUpdate(update);
    }
  }
  
}
