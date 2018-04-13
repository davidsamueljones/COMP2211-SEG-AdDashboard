package group33.seg.model.configs;

import java.util.UUID;

/**
 * Structure-like class for constructing a statistic configuration. All variables are public to allow
 * for easy structure access.
 */
public class StatisticConfig {
  
  /**
   * Sequence that can uniquely identify a graph. Required as all properties of a graph may change
   * yet behaviour may desire two graphs with different properties to represent different instances
   * of the same graph.
   */
  public final String uuid;

  /** Named identifier of statistic (not necessarily unique) */
  public String identifier = null;

  /** Whether to hide statistic from display */
  public boolean hide;
  
  /** Query to fetch data with, should use no grouping and no specific metric */
  public MetricQuery query;
  
  public StatisticConfig() {
    this(null);
  }
  
  public StatisticConfig(String uuid) {
    if (uuid == null) {
      this.uuid = UUID.randomUUID().toString();
    } else {
      this.uuid = uuid;
    }
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
    StatisticConfig other = (StatisticConfig) obj;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }
  
}
