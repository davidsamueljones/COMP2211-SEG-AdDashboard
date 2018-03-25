package group33.seg.model.types;

/**
 * Enumeration of available metrics types. Can be fetched in their short and long string
 * representations.
 */
public enum Metric {
  IMPRESSIONS("Impressions", "Number of Impressions"),
  CLICKS("Clicks", "Number of Clicks"),
  UNIQUES("Uniques", "Number of Uniques"),
  BOUNCES("Bounces", "Number of Bounces"),
  CONVERSIONS("Conversions", "Number of Conversions"),
  TOTAL_COST("Total Cost"),
  CTR("CTR"),
  CPA("CPA"),
  CPC("CPC"),
  CPM("CPM"),
  BOUNCE_RATE("Bounce Rate");

  /* Class configuration */
  private String shortStr;
  private String longStr;

  /**
   * Initialise enum type.
   *
   * @param str String to use for long and short representation.
   */
  private Metric(String str) {
    this(str, str);
  }

  /**
   * Initialise enum type.
   *
   * @param shortStr String to use for short representation
   * @param longStr String to use for long representation
   */
  private Metric(String shortStr, String longStr) {
    this.shortStr = shortStr;
    this.longStr = longStr;
  }

  /** @return Short representation */
  public String getShortStr() {
    return shortStr;
  }

  /** @return Long representation */
  public String getLongStr() {
    return longStr;
  }
}
