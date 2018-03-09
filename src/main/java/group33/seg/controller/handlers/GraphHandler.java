package group33.seg.controller.handlers;

import java.util.List;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;
import group33.seg.view.output.Graph;

public class GraphHandler {
  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;
  
  /** Graph being controlled */
  private Graph graph;
 
  /**
   * Instantiate a graph handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public GraphHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }
  
  public void setGraph(Graph graph) {
    this.graph = graph;
    graph.setFontScale(scale); //FIXME: Instead fetch theme here
  }

  public void generateImpressionGraph(Interval interval) {
    DatabaseHandler db = new DatabaseHandler(null); // TODO: REMOVE ASAP
    MetricQuery query = new MetricQuery(null, Metric.IMPRESSIONS, interval, null);
    List<Pair<String, Integer>> data = db.getQueryResponse(query).getResult();

    graph.addLine(data);
  }

  public void clearGraph() {
    graph.clearLines();
  }
  
  // FIXME: Can remove when setting by theme
  private double scale = 1;
  public void setFontScale(double scale) {
    this.scale = scale;
  }
  
  
  
  
  
}
