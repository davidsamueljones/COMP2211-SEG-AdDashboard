package group33.seg.controller.events;

import group33.seg.model.utilities.Range;

public class GraphHandler {
  // model being used
  // graph object being manipulated

  public void xAxisChange(Range<Double> range) {
    // TODO:
    // 1. Update x axis on graph
  }

  public void yAxisChange(Range<Double> range) {
    // TODO:
    // 1. Update y axis on graph
  }

  public void intervalChange(/* Interval*/ ) {
    // TODO:
    // 1. Requery all line queries with new grouping (update in model)
    // 2. Update data on all lines (update data objects for lines)
    // 3. Update interval in graph (update graph object)
    // 4. Redraw full graph
  }

  public void linePropertyChange(/* Line object? */ ) {
    // TODO:
    // 1. Update line properties (in model)
    // 2. Redraw line on graph
  }

  public void lineQueryChange(/* Line object? */ ) {
    // TODO:
    // 1. Requery line data (update in model)
    // 2. Update line data in graph
    // 3. Redraw line on graph
  }

  public void multiLinePropertyChange(/* Lines list */ ) {
    // TODO:
    // For all lines do: linePropertyChange(line);
  }

  public void multiLineQueryChange() {
    // TODO:
    // For all lines do: lineQueryChange(line);
  }
}
