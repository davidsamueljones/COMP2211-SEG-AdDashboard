package group33.seg.controller.utilities;

import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineGraphConfig;

public interface GraphVisitor {

  /**
   * Handle visitor behaviour for a line graph.
   * 
   * @param graph Graph configuration
   */
  void visit(LineGraphConfig graph);

  /**
   * Handle visitor behaviour for a histogram.
   * 
   * @param graph Graph configuration
   */
  void visit(HistogramConfig graph);

}
