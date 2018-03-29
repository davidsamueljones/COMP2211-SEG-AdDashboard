package group33.seg.model.configs;

import java.util.List;
import group33.seg.controller.types.GraphVisitor;
import group33.seg.controller.utilities.ErrorBuilder;

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
  public Mode mode = null;

  /** Set of lines that graph should display */
  public List<LineConfig> lines = null;

  /** Whether the graph legend should be shown on the graph */
  public boolean showLegend = true;

  @Override
  public void accept(GraphVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public ErrorBuilder validate() {
    ErrorBuilder eb = super.validate();
    
    if (mode == null) {
      eb.addError("Line graph mode is unset");
    }
    if (lines != null) {
     //lines.validate();
    }
    return eb;
  }
  
  /** Enumeration of line drawing modes. */
  public enum Mode {
    NORMAL, /* Indicates data should be plotted at its absolute position */
    OVERLAY /* Indicates data should be plotted at the same index */
  }

}
