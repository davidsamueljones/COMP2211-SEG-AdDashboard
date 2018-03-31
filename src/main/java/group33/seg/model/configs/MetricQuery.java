package group33.seg.model.configs;

import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;

/**
 * Structure-like class for constructing a MetricQuery. All variables are public to allow for easy
 * structure access.
 */
public class MetricQuery {

  /** Metric to fetch */
  public Metric metric;

  /** Interval to group by (null == single return value) */
  public Interval interval;

  /** Filtering configuration to use (null == ignored) */
  public FilterConfig filter;

  /** Bounce rate definition (ignored if not relevant to metric) */
  public BounceConfig bounceDef;

  /** Campaign configuration for selecting a campaign through ID */
  public CampaignConfig campaign;

  /** Instantiate an empty query. */
  public MetricQuery() {
    this(null, null, null, null, null);
  }

  /**
   * Instantiate a query with no special definitions.
   * 
   * @param metric Metric type being requested
   * @param time Interval to group by
   * @param filter Filter to apply on query
   */
  public MetricQuery(Metric metric, Interval time, FilterConfig filter) {
    this(metric, time, filter, null, null);
  }

  /**
   * Instantiate a fully defined query.
   * 
   * @param metric Metric type being requested
   * @param time Interval to group by
   * @param filter Filter to apply on query
   * @param bounceDef Definition to use for bounce if applicable
   * @param campaign Campaign to use
   */
  public MetricQuery(Metric metric, Interval time, FilterConfig filter, BounceConfig bounceDef,
      CampaignConfig campaign) {
    this.metric = metric;
    this.interval = time;
    this.filter = filter;
    this.bounceDef = bounceDef;
    this.campaign = campaign;
  }

  /**
   * Equality check between this instance and another instance. This equality check compares all
   * fields including non-final.
   * 
   * @param other Other instance to compare against
   * @return Whether instances are the same
   */
  public boolean isEquals(MetricQuery other) {
    boolean equal = true;
    equal &= (metric == null ? (other.metric == null) : metric.equals(other.metric));
    equal &= (interval == null ? (other.interval == null) : interval.equals(other.interval));
    equal &= (filter == null ? (other.filter == null) : filter.isEquals(other.filter));
    if (Metric.BOUNCE_RATE.equals(metric)) {
      equal &=
          (bounceDef == null ? (other.bounceDef == null) : bounceDef.isEqual(other.bounceDef));
    }
    return equal;
  }

  /**
   * Do local validation of configuration.
   * 
   * @return Any issues with validation
   */
  public ErrorBuilder validate() {
    ErrorBuilder eb = new ErrorBuilder();
    if (metric == null) {
      eb.addError("A metric must be selected");
    }
    if (interval == null) {
      eb.addError("An interval must be selected");
    }
    return eb;
  }

}
