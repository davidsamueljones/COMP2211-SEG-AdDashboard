package group33.seg.model.utilities;

/** 
 * Helper class for storing data ranges. 
 */
public class Range<T> {
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
  
}
