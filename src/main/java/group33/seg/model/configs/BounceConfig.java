package group33.seg.model.configs;

/**
 * Structure-like class for constructing a bounce rate configuration. All variables are public to
 * allow for easy structure access.
 */
public class BounceConfig {

  /** Type of bounce rate */
  public Type type;

  /** Value type uses as constraint */
  public int value;

  public enum Type {
    TIME, PAGES,
  }

}
