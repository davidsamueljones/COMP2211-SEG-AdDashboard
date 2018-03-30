package group33.seg.model.configs;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Structure-like class for constructing a bounce rate configuration. All variables are public to
 * allow for easy structure access.
 */
public class BounceConfig {

  /** Type of bounce rate */
  public Type type;

  /** Value type uses as constraint */
  public int value;

  /**
   * Equality check between this instance and another instance. This equality check compares all
   * fields including non-final.
   * 
   * @param other Other instance to compare against
   * @return Whether instances are the same
   */
  public boolean isEquals(BounceConfig other) {
    boolean equal = true;
    equal &= (type == other.type);
    equal &= (value == other.value);
    return equal;
  }

  /**
   * Enumeration of possible bounce types.
   */
  public enum Type {
    TIME, PAGES,
  }

}
