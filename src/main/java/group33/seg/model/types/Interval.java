package group33.seg.model.types;

/** Enumeration of time intervals between data. */
public enum Interval {
  HOUR("hour"),
  DAY("day"),
  WEEK("week"),
  MONTH("month"),
  YEAR("year");

  private String value;

  Interval(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
