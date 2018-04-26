package group33.seg.model.types;

/**
 * Enumeration of available metrics types.
 */
public enum Metric {
  IMPRESSIONS("Number of Impressions", Definitions.IMPRESSIONS),
  CLICKS("Number of Clicks", Definitions.CLICKS),
  UNIQUES("Number of Uniques", Definitions.UNIQUES),
  BOUNCES("Number of Bounces", Definitions.BOUNCES),
  CONVERSIONS("Number of Conversions", Definitions.CONVERSIONS),
  TOTAL_COST("Total Cost", Definitions.TOTAL_COST),
  TOTAL_COST_HISTOGRAM("Total cost histogram", null),
  CTR("CTR", Definitions.CTR),
  CPA("CPA", Definitions.CPA),
  CPC("CPC", Definitions.CPC),
  CPM("CPM", Definitions.CPM),
  BOUNCE_RATE("Bounce Rate", Definitions.BOUNCE_RATE);
  
  /* Class configuration */
  private final String string;
  public final String definition;

  /**
   * Initialise enum type.
   *
   * @param string String to use for toString representation
   * @param definition The metric's definition/explanation
   */
  private Metric(String string, String definition) {
    this.string = string;
    this.definition = definition;
  }

  @Override
  public String toString() {
    return string;
  }
  
  /**
   * Class to hold metric definition strings.
   */
  class Definitions {
    static final String IMPRESSIONS = "Count of when an ad is shown to a user, regardless of whether they click on it.";
    static final String CLICKS = "Count of when a user clicks on an ad that is shown to them.";
    static final String UNIQUES = "Count of unique users that click on an ad during the course of a campaign.";
    static final String BOUNCES = "Count of occurences where a user clicks on an ad, but then fails to interact with the "
        + "website (typically detected when a user navigates away from the website after a short time, "
        + "or when only a single page has been viewed; this definition can be set).";
    static final String CONVERSIONS = "A conversion, or acquisition, occurs when a user clicks and then acts on an ad. "
        + "The specific definition of an action depends on the campaign (e.g., buying a product, registering as a new "
        + "customer or joining a mailing list).";
    static final String TOTAL_COST = "Sum of impression costs and click costs";
    static final String CTR = "Click-through-rate is the average number of clicks per impression.";
    static final String CPA = "Cost-per-acquisition is the average amount of money spent on an advertising campaign" + 
        "for each acquisition (i.e., conversion).";
    static final String CPC = "Cost-per-click is the average amount of money spent on an advertising campaign for each click.";
    static final String CPM = "Cost-per-thousand impressions is the average amount of money spent on an advertising campaign "
        + "for every one thousand impressions.";
    static final String BOUNCE_RATE = "The average number of bounces per click.";       
  }
  
}
