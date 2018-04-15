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

  /** Campaign to target (should not be null but target all if is) */
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
   * Create a human readable string (with html formatting) representing the query.
   * 
   * @return Query as generated text
   */
  public String inText() {
    StringBuilder builder = new StringBuilder();
    builder.append("<b>Campaign:</b> " + (campaign == null ? "All" : campaign.name));
    builder.append("<br><b>Metric:</b> " + (metric == null ? "All" : metric));
    builder.append(interval == null ? "" : "<br><b>Interval:</b> " +  interval);
    builder.append("<br><b>Filter:</b><br>"
        + (filter == null ? FilterConfig.NO_FILTER_TEXT : filter.inText()));
    if (needBounceDef(metric)) {
      builder.append("<br><b>Bounce Definition:</b> ");
      builder.append((bounceDef == null ? "None" : bounceDef.inText()));
    }
    return builder.toString();
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
    equal &= (campaign == null ? (other.campaign == null) : campaign.equals(other.campaign));
    equal &= (metric == null ? (other.metric == null) : metric.equals(other.metric));
    equal &= (interval == null ? (other.interval == null) : interval.equals(other.interval));
    equal &= (filter == null ? (other.filter == null) : filter.isEquals(other.filter));
    if (MetricQuery.needBounceDef(metric)) {
      equal &= (bounceDef == null ? (other.bounceDef == null) : bounceDef.isEqual(other.bounceDef));
    }
    return equal;
  }

  /**
   * @return Whether the currently selected metric requires the bounce rate definition panel
   */
  public static boolean needBounceDef(Metric metric) {
    return metric == null || Metric.BOUNCES == metric || Metric.BOUNCE_RATE == metric;
  }

}
