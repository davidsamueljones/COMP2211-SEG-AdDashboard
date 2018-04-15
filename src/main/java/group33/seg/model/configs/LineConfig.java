package group33.seg.model.configs;

import java.awt.Color;
import java.util.UUID;
import group33.seg.controller.utilities.ErrorBuilder;

/**
 * Structure-like class for constructing a single line. All variables are public to allow for easy
 * structure access. A line can be uniquely identified with its UUID.
 */
public class LineConfig {

  /**
   * Sequence that can uniquely identify a line. Required as all visible properties of a line may
   * change yet behaviour may desire two lines with different properties to represent different
   * instances of the same line.
   */
  public final String uuid;

  /** Identifier (not necessarily unique) shown for line */
  public String identifier = null;

  /** Colour of line plotted */
  public Color color = null;

  /** Thickness of line plotted (as a scale of graph defaults) */
  public int thickness;

  /** Whether to hide plot from the graph */
  public boolean hide;

  /** Query to fetch data with, uses graph grouping */
  public MetricQuery query = null;

  /**
   * Instantiate a line configuration with a random UUID.
   */
  public LineConfig() {
    this(UUID.randomUUID().toString());
  }

  /**
   * Instantiate a line with a given UUID.
   * 
   * @param uuid Unique identifier for line
   */
  public LineConfig(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Create a human readable string (with html formatting) representing the line configuration.
   * 
   * @return Line as generated text
   */
  public String inText() {
    StringBuilder builder = new StringBuilder();
    builder.append("<b>Identfier: </b>" + identifier);
    builder.append("<br><b>Hidden:</b> " + (hide ? "True" : "False"));
    builder.append("<br><br>" + query.inText());
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
    LineConfig other = (LineConfig) obj;
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
    if (color == null) {
      eb.addError("An colour must be selected");
    }
    if (query == null) {
      eb.addError("A query must be provided");
    } else {
      if (query.metric == null) {
        eb.addError("A metric must be selected");
      }
      if (query.interval == null) {
        eb.addError("An interval must be selected");
      }
    }
    return eb;
  }

}
