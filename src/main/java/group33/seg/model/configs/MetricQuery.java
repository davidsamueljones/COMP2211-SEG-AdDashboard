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
  public BounceRateConfig bouncerateDef;
  
  /** Instantiate an empty query. */
  public MetricQuery() {
    this(null, null, null);
  }

  /** 
   * Instantiate a query with no special definitions.
   * 
   * @param metric Metric type being requested
   * @param time Interval to group by
   * @param filter Filter to apply on query
   * */
  public MetricQuery(Metric metric, Interval time, FilterConfig filter) {
    this(metric, time, filter, null);
  }
  
  /** 
   * Instantiate a fully defined query. 
   * 
   * @param metric Metric type being requested
   * @param time Interval to group by
   * @param filter Filter to apply on query
   * @param bouncerateDef Definition to use for bouncerate if applicable
   * */
  public MetricQuery(Metric metric, Interval time, FilterConfig filter, BounceRateConfig bouncerateDef) {
    this.metric = metric;
    this.interval = time;
    this.filter = filter;
    this.bouncerateDef = bouncerateDef;
  }
  
}
