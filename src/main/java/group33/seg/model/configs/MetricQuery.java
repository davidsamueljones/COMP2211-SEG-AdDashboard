package group33.seg.model.configs;

import group33.seg.model.types.Metric;
import group33.seg.model.types.Interval;

public class MetricQuery {
  private CampaignConfig campaignConfig;
  private Metric metric;
  private Interval interval;
  private FilterConfig filterConfig;

  public MetricQuery(CampaignConfig campaignConfig, Metric metric, Interval grouping, FilterConfig filterConfig) {
    this.campaignConfig = campaignConfig;
    this.metric = metric;
    this.interval = grouping;
    this.filterConfig = filterConfig;
  }

  public CampaignConfig getConfig() {
    return campaignConfig;
  }

  public Metric getMetric() {
    return metric;
  }

  public Interval getInterval() {
    return interval;
  }

  public FilterConfig getFilterConfig() {
    return filterConfig;
  }

>>>>>>> Stashed changes
  // metric (null == all metrics)
  // filter
  // bounce rate definition (ignored if not relevant to metric)

  // interval (null == single value returned)
}
