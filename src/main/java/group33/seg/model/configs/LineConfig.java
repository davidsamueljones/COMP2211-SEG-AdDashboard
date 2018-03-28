package group33.seg.model.configs;

import java.awt.Color;
import java.util.UUID;

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

  /** Thickness of line plotted */
  public int thickness;

  /** Whether to hide plot from the graph */
  public boolean hide;

  /** Query to fetch data with, uses graph grouping */
  public MetricQuery query = null;


  public LineConfig() {
    this(UUID.randomUUID().toString());
  }

  public LineConfig(String uuid) {
    this.uuid = uuid;
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

}
