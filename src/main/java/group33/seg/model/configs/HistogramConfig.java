package group33.seg.model.configs;

import group33.seg.controller.utilities.GraphVisitor;

public class HistogramConfig extends GraphConfig {

  @Override
  public void accept(GraphVisitor visitor) {
    visitor.visit(this);
  }

}
