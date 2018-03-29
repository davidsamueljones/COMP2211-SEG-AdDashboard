package group33.seg.model.configs;

import java.util.UUID;
import group33.seg.controller.types.GraphVisitor;
import group33.seg.controller.utilities.ErrorBuilder;

/**
 * Structure-like class for constructing a generic graph. All variables are public to allow for easy
 * structure access.
 */
public abstract class GraphConfig {

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
  
  public GraphConfig() {
    this(null);
  }

  public GraphConfig(String uuid) {
    if (uuid == null) {
      this.uuid = UUID.randomUUID().toString();
    } else {
      this.uuid = uuid;
    }
  }
 
  public abstract void accept(GraphVisitor visitor);

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
