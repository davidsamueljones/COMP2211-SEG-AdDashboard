package group33.seg.model.configs;

import java.awt.Color;
import java.io.Serializable;
import java.util.UUID;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.GraphVisitor;

/**
 * Structure-like class for constructing a generic graph. All variables are public to allow for easy
 * structure access.
 */
public abstract class GraphConfig implements Serializable {
  private static final long serialVersionUID = -5797687291992849934L;

  /**
   * Sequence that can uniquely identify a graph. Required as all properties of a graph may change
   * yet behaviour may desire two graphs with different properties to represent different instances
   * of the same graph.
   */
  public final String uuid;

  /** Named identifier of graph (not necessarily unique) */
  public String identifier = null;

  /** Main chart title */
  public String title = null;

  /** Title of X Axis */
  public String xAxisTitle = null;

  /** Title of Y Axis */
  public String yAxisTitle = null;

  /** Colour for graph background */
  public Color background = null;
  
  /** Whether the graph legend should be shown on the graph */
  public boolean showLegend = true;
  
  /**
   * Instantiate a graph configuration with a random UUID.
   */
  public GraphConfig() {
    this(null);
  }

  /**
   * Instantiate a graph with a given UUID.
   * 
   * @param uuid Unique identifier for line
   */
  public GraphConfig(String uuid) {
    if (uuid == null) {
      this.uuid = UUID.randomUUID().toString();
    } else {
      this.uuid = uuid;
    }
  }

  /**
   * Execute graph visitor behaviour with this instance. Use subclassing so the true type is the one
   * calling the graph visitor.
   * 
   * @param visitor Visitor object for which to execute behaviour
   */
  public abstract void accept(GraphVisitor visitor);

  /**
   * Create a human readable string (with html formatting) representing the graph configuration.
   * 
   * @return Graph configuration as generated text
   */
  public String inText() {
    StringBuilder builder = new StringBuilder();
    builder.append("<b>Identfier: </b>" + identifier);
    builder.append("<br><b>Title:</b> " + (title == null || title.isEmpty() ? "None" : title));
    builder.append("<br><b>X-Axis:</b> ");
    builder.append(xAxisTitle == null || xAxisTitle.isEmpty() ? "None" : xAxisTitle);
    builder.append("<br><b>Y-Axis:</b> ");
    builder.append(yAxisTitle == null || yAxisTitle.isEmpty() ? "None" : yAxisTitle);
    builder.append("<br><b>Legend:</b> " + (showLegend ? "Enabled" : "Disabled"));
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GraphConfig other = (GraphConfig) obj;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }

  /**
   * Do local validation of configuration.
   * 
   * @return Any issues with validation
   */
  public ErrorBuilder validate() {
    ErrorBuilder eb = new ErrorBuilder();
    if (identifier == null || identifier.isEmpty()) {
      eb.addError("Graph must have an identifier");
    }
    return eb;
  }

}
