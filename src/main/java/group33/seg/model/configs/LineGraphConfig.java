package group33.seg.model.configs;

import java.awt.Color;
import java.util.List;
import group33.seg.model.types.Interval;

/**
 * Structure-like class for constructing a line graph configuration. All variables are public to allow
 * for easy structure access.
 */
public class LineGraphConfig extends GraphConfig {

  /** Mode for how lines behave in respect to each other */
  public Mode mode;

  /** Grouping interval applied to all lines */
  public Interval interval;

  /** Set of lines that graph should display */
  public List<Line> lines;

  /**
   * Structure-like class for constructing a single line. All variables are public to allow for easy
   * structure access.
   */
  public class Line {

    /** Identifier shown for line (possibly in legend) */
    public String identifier;

    /** Colour of line plotted */
    public Color color;

    /** Thickness of line plotted */
    public int thickness;

    /** Whether to hide plot from the graph */
    public boolean hide;

    /** Query to fetch data with, uses graph grouping */
    public MetricQuery query;

  }

  /** Enumeration of line drawing modes. */
  public enum Mode {
    NORMAL, /* Indicates data should be plotted at its absolute position */
    OVERLAY /* Indicates data should be plotted at the same index */
  }

}
