package group33.seg.controller.events;

import java.util.List;
import group33.seg.controller.database.Database;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;
import group33.seg.view.increment1.Graph;

public class GraphHandler {
  Graph graph;
  
  public void setGraph(Graph graph) {
    this.graph = graph;
  }
  
  public void generateImpressionGraph(Interval interval) {
    Database db = new Database();
    MetricQuery query = new MetricQuery(null, Metric.IMPRESSIONS, interval, null);
    List<Pair<String, Integer>> data = db.getQueryResponse(query).getResult();
    
    graph.addLine(data);
  }
  
}
