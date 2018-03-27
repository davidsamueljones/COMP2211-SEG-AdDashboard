package group33.seg.model.configs;

import java.util.List;

/**
 * Structure-like class for constructing a line graph configuration. All variables are public to
 * allow for easy structure access.
 */
public class LineGraphConfig extends GraphConfig {

  public LineGraphConfig() {
    this(null);
  }
  
  public LineGraphConfig(String uuid) {
    super(uuid);
  }

  /** Mode for how lines behave in respect to each other */
  public Mode mode;

  /** Set of lines that graph should display */
  public List<LineConfig> lines;

  /** Whether the graph legend should be shown on the graph */
  public boolean showLegend;

  /** Enumeration of line drawing modes. */
  public enum Mode {
    NORMAL, /* Indicates data should be plotted at its absolute position */
    OVERLAY /* Indicates data should be plotted at the same index */
  }

}
