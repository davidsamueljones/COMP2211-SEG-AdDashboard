package group33.seg.controller.types;

public abstract class MetricQueryResponse<T> {
  // query
  // Map<Metric, T> data

  // Notes:
  // Map so can return multiple metrics (for statistics) 
  // Generic so can return single data values (for statistics) or
  // multiple data values, possibly x,y pairings for graph

}
