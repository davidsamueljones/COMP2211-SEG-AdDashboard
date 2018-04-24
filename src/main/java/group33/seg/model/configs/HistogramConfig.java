package group33.seg.model.configs;

import java.awt.Color;
import group33.seg.controller.utilities.GraphVisitor;

public class HistogramConfig extends GraphConfig {

  /** Colour of bars plotted */
  public Color barColor;
  
  /** Query to fetch data with */
  public MetricQuery query;
  
  /**
   * Instantiate a histogram configuration with a random UUID.
   */
  public HistogramConfig() {
    this(null);
  }

  /**
   * Instantiate a histogram with a given UUID.
   * 
   * @param uuid Unique identifier for histogram
   */
  public HistogramConfig(String uuid) {
    super(uuid);
  }
  
  @Override
  public void accept(GraphVisitor visitor) {
    visitor.visit(this);
  }

}
