package group33.seg.model.configs;

import group33.seg.model.types.Metric;
import group33.seg.model.types.Interval;

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
   * */
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
   * */
  public MetricQuery(Metric metric, Interval time, FilterConfig filter, BounceConfig bounceDef, CampaignConfig campaignConfig) {
    this.metric = metric;
    this.interval = time;
    this.filter = filter;
    this.bounceDef = bounceDef;
    this.campaign = campaignConfig;
  }
  
}
