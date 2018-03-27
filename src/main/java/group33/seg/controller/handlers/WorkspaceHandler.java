package group33.seg.controller.handlers;

import java.util.Iterator;
import java.util.List;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.model.configs.GraphConfig;

public class WorkspaceHandler {
  
  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;
  
  /**
   * Instantiate a workspace handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public WorkspaceHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }
 
  public boolean addGraph(GraphConfig graph) {
    List<GraphConfig> graphs = getGraphs();
    for (GraphConfig exGraph : graphs) {
      if (exGraph.identifier.equals(graph.identifier)) {
        return false;
      }
    }
    graphs.add(graph);
    return true;
  }
  
  public boolean removeGraph(String identifier) {
    boolean removed = false;
    Iterator<GraphConfig> itrGraphs = getGraphs().iterator(); 
    while (itrGraphs.hasNext()) {
      GraphConfig graph = itrGraphs.next();
      if (graph.identifier.equals(identifier)) {
        itrGraphs.remove();
        removed = true;
      }
    }
    return removed;
  }
  
  public List<GraphConfig> getGraphs() {
    return mvc.model.getWorkspace().graphs;
  }
  
}
