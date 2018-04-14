package group33.seg.model.configs;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import group33.seg.lib.Utilities;
import group33.seg.model.utilities.Range;

/**
 * Structure-like class for constructing a filter configuration. All variables are public to allow
 * for easy structure access.
 */
public class FilterConfig {
  public static String NO_FILTER_TEXT = "* No Filter *";

  /** Age Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Collection<Age> ages = null;

  /** Gender Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Collection<Gender> genders = null;

  /** Income Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Collection<Income> incomes = null;

  /** Context Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Collection<Context> contexts = null;

  /**
   * Date Filter (ignore full range if set to null or if not null but individual limit is null
   * ignore the respective individual limit)
   */
  public Range<Date> dates = null;

  /**
   * Create a human readable string (with html formatting) representing the filter configuration.
   * 
   * @return Filter as generated text
   */
  public String inText() {
    StringBuilder filter = new StringBuilder();
    // Fields
    filter.append(generateFieldText("Ages", ages));
    filter.append(generateFieldText("Genders", genders));
    filter.append(generateFieldText("Incomes", incomes));
    filter.append(generateFieldText("Contexts", contexts));
    // Dates
    if (dates != null) {
      filter.append(dates.toString());
    }
    // Check if any filter rules have been found
    if (filter.length() == 0) {
      filter.append(NO_FILTER_TEXT);
    }
    return filter.toString();
  }

  /**
   * Generate a human readable string (with html formatting) representing a single fields filter
   * configuration.
   * 
   * @param field Field values correspond to
   * @param values Values to filter by, no filter if null
   * @return Filter as generated text
   */
  private String generateFieldText(String field, Collection<?> values) {
    StringBuilder filter = new StringBuilder();
    // No filter to apply if null
    if (values != null) {
      // Add list title
      filter.append(field);
      // Make string list of input values
      filter.append(" [");
      Iterator<?> itrValues = values.iterator();
      while (itrValues.hasNext()) {
        Object value = itrValues.next();
        filter.append(value.toString());
        if (itrValues.hasNext()) {
          filter.append(", ");
        }
      }
      filter.append("]<br>");
    }
    return filter.toString();
  }

  /**
   * Equality check between this instance and another instance. This equality check compares all
   * fields including non-final.
   * 
   * @param other Other instance to compare against
   * @return Whether instances are the same
   */
  public boolean isEquals(FilterConfig other) {
    boolean equal = true;
    equal &= (ages == null ? (other.ages == null) : ages.equals(other.ages));
    equal &= (genders == null ? (other.genders == null) : genders.equals(other.genders));
    equal &= (incomes == null ? (other.incomes == null) : incomes.equals(other.incomes));
    equal &= (contexts == null ? (other.contexts == null) : contexts.equals(other.contexts));
    equal &= (dates == null ? (other.dates == null) : dates.equals(other.dates));
    return equal;
  }

  /**
   * Enumeration of possible ages for filtering.
   */
  public enum Age {
    LESS_25("<25"), BETWEEN_25_34("25-34"), BETWEEN_35_44("35-44"), BETWEEN_45_54("45-54"), MORE_54(
        ">54");

    private String string;

    Age(String string) {
      this.string = string;
    }

    @Override
    public String toString() {
      return string;
    }
  }

  /**
   * Enumeration of possible genders for filtering.
   */
  public enum Gender {
    MALE, FEMALE;

    @Override
    public String toString() {
      return Utilities.getTitleCase(super.toString());
    }
  }

  /**
   * Enumeration of possible incomes for filtering.
   */
  public enum Income {
    LOW, MEDIUM, HIGH;

    @Override
    public String toString() {
      return Utilities.getTitleCase(super.toString());
    }
  }

  /**
   * Enumeration of possible contexts for filtering.
   */
  public enum Context {
    SHOPPING, BLOG, SOCIAL_MEDIA, NEWS, TRAVEL, HOBBIES;

    @Override
    public String toString() {
      return Utilities.getTitleCase(super.toString());
    }
  }

}
