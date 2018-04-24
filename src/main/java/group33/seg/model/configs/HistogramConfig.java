package group33.seg.model.configs;

import java.awt.Color;
import group33.seg.controller.utilities.GraphVisitor;

public class HistogramConfig extends GraphConfig {
  private static final long serialVersionUID = -3528347690642461216L;

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
