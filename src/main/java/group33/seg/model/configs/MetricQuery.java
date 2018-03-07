package group33.seg.model.configs;

import group33.seg.model.types.Metric;
import group33.seg.model.types.Interval;

/**
 * Structure-like class for constructing a MetricQuery. All variables are public to allow for easy
 * structure access.
 */
public class MetricQuery {
  /** Campaign to target */
  public CampaignConfig campaign;

  /** Metric to fetch */
  public Metric metric;

  /** Interval to group by (null == single return value) */
  public Interval interval;

  /** Filtering configuration to use (null == ignored) */
  public FilterConfig filter;

  // add bounce rate definition (ignored if not relevant to metric)

  /** Instantiate an empty query. */
  public MetricQuery() {
    this(null, null, null, null);
  }

  /** Instantiate a fully defined query. */
  public MetricQuery(CampaignConfig campaign, Metric metric, Interval time, FilterConfig filter) {
    this.campaign = campaign;
    this.metric = metric;
    this.interval = time;
    this.filter = filter;
  }
}
