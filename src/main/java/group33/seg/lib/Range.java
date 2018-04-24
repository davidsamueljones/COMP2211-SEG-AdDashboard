package group33.seg.lib;

import java.io.Serializable;

/** 
 * Helper class for storing data ranges. 
 */
public class Range<T> implements Serializable {
  private static final long serialVersionUID = 2602820261310555424L;
  
  public final T min;
  public final T max;

  /**
   * Initialise a new range object.
   *
   * @param min Minimum value of range
   * @param max Maximum value of range
   */
  public Range(T min, T max) {
    this.min = min;
    this.max = max;
  }
  
  @Override
  public String toString() {
    String strMin = (min == null ? "[]" : min.toString());
    String strMax = (max == null ? "[]" : max.toString());
    return String.format("%s - %s", strMin, strMax);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((max == null) ? 0 : max.hashCode());
    result = prime * result + ((min == null) ? 0 : min.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Range))
      return false;
    Range<?> other = (Range<?>) obj;
    if (max == null) {
      if (other.max != null)
        return false;
    } else if (!max.equals(other.max))
      return false;
    if (min == null) {
      if (other.min != null)
        return false;
    } else if (!min.equals(other.min))
      return false;
    return true;
  }
  
}
