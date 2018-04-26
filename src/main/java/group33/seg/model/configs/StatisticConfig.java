package group33.seg.model.configs;

import java.io.Serializable;
import java.util.UUID;
import group33.seg.controller.utilities.ErrorBuilder;

/**
 * Structure-like class for constructing a statistic configuration. All variables are public to
 * allow for easy structure access.
 */
public class StatisticConfig implements Serializable {
  private static final long serialVersionUID = 6272012825897221682L;

  /**
   * Sequence that can uniquely identify a statistic. Required as all properties of a statistic may
   * change yet behaviour may desire two statistic with different properties to represent different
   * instances of the same statistic.
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

  /**
   * Create a human readable string (with html formatting) representing the statistic configuration.
   * 
   * @return Statistic as generated text
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
    StatisticConfig other = (StatisticConfig) obj;
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
      eb.addError("Statistic must have an identifier");
    }
    if (query == null) {
      eb.addError("A query must be provided");
    } else {
      if (query.campaign == null) {
        eb.addError("A campaign must be selected");
      }
    }
    return eb;
  }

}
