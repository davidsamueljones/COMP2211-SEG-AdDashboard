package group33.seg.model.types;

/**
 * Enumeration of available metrics types.
 */
public enum Metric {
  IMPRESSIONS("Number of Impressions"),
  CLICKS("Number of Clicks"),
  UNIQUES("Number of Uniques"),
  BOUNCES("Number of Bounces"),
  CONVERSIONS("Number of Conversions"),
  TOTAL_COST("Total Cost"),
  CTR("CTR"),
  CPA("CPA"),
  CPC("CPC"),
  CPM("CPM"),
  BOUNCE_RATE("Bounce Rate");

  /* Class configuration */
  private String string;

  /**
   * Initialise enum type.
   *
   * @param string String to use for toString representation
   */
  private Metric(String string) {
    this.string = string;
  }
  
  @Override
  public String toString() {
    return string;
  }
}
