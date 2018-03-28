package group33.seg.controller.handlers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.LineGraphConfig;
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
    graph.setFontScale(scale); // FIXME: Instead fetch theme here
  }

  private void loadLineGraph(LineGraphConfig graph) {
    // LineGraphConfig graph = wizard.getGraph();
    // if (graph != null) {
    // refreshGraphs(graph.identifier);
    // }
  }

  private void currentGraph() {

  }



  public void generateImpressionGraph(Interval interval) {
    DatabaseHandler db = new DatabaseHandler(null, "config.properties"); // TODO: REMOVE ASAP
    MetricQuery query = new MetricQuery(Metric.IMPRESSIONS, interval, null);
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

  public static Update getLineUpdate(LineConfig original, LineConfig updated) {
    // Check for any changes in querying that may change data
    boolean data = true;
    data &= original.query.metric.equals(updated.query.metric);
    data &= original.query.interval.equals(updated.query.interval);
    data &= original.query.filter.isEquals(updated.query.filter);
    if (updated.query.metric.equals(Metric.BOUNCE_RATE)) {
      data &= original.query.bounceDef.isEquals(updated.query.bounceDef);
    }
    // Check for any changes of properties
    boolean properties = true;
    properties &= original.identifier.equals(updated.identifier);
    properties &= original.color.equals(updated.color);
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

  public enum Update {
    FULL, PROPERTIES, DATA, NOTHING;
  }

}
