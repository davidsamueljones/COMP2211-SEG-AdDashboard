package group33.seg.model.types;

import group33.seg.lib.Utilities;

/** Enumeration of time intervals between data. */
public enum Interval {
  HOUR, DAY, WEEK, MONTH, YEAR;

  @Override
  public String toString() {
    return Utilities.getTitleCase(super.toString());
  }

}
