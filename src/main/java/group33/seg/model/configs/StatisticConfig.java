package group33.seg.model.configs;

/**
 * Structure-like class for constructing a statistic configuration. All variables are public to allow
 * for easy structure access.
 */
public class StatisticConfig {
  
  /** Storage identifier of statistic */
  public String identifier;
  
  /** Whether to hide statistic from display */
  public boolean hide;
  
  /** Query to fetch data with, should use no grouping and no specific metric */
  public MetricQuery query;

}
