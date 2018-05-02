package group33.seg.model.configs;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.lib.Range;
import group33.seg.lib.Utilities;

/**
 * Structure-like class for constructing a filter configuration. All variables are public to allow
 * for easy structure access.
 */
public class FilterConfig implements Serializable {
  private static final long serialVersionUID = -6199412926155535712L;

  public static String NO_FILTER_TEXT = "* No Filter *";

  /** Age Filter (filtered to those in set, ignored if null) */
  public Collection<Age> ages = null;

  /** Gender Filter (filtered to those in set, ignored if null) */
  public Collection<Gender> genders = null;

  /** Income Filter (filtered to those in set, ignored if null) */
  public Collection<Income> incomes = null;

  /** Context Filter (filtered to those in set, ignored if null) */
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
    // Use a break monitor so only required breaks are written
    boolean br = false;
    // Fields
    br |= appendFilterSection(filter, generateFieldText("Ages", ages), br);
    br |= appendFilterSection(filter, generateFieldText("Genders", genders), br);
    br |= appendFilterSection(filter, generateFieldText("Incomes", incomes), br);
    br |= appendFilterSection(filter, generateFieldText("Contexts", contexts), br);

    // Dates
    if (dates != null) {
      br = appendFilterSection(filter, dates.toString(), br);
    }
    // Check if any filter rules have been found
    if (filter.length() == 0) {
      filter.append(NO_FILTER_TEXT);
    }
    return filter.toString();
  }

  /**
   * Append text to the builder, adding a line break prior if indicated.
   * 
   * @param builder Builder to append to
   * @param text Text to append
   * @param br Whether a break should be placed
   * @return Whether anything was appended
   */
  private boolean appendFilterSection(StringBuilder builder, String text, boolean br) {
    if (text != null && !text.isEmpty()) {
      if (br) {
        builder.append("<br>");
      }
      builder.append(text);
      return true;
    }
    return false;
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
      filter.append("]");
    }
    return filter.toString();
  }

  /**
   * Do local validation of configuration.
   * 
   * @return Any issues with validation
   */
  public ErrorBuilder validate() {
    ErrorBuilder eb = new ErrorBuilder();
    if (ages != null && ages.isEmpty()) {
      eb.addError("No age filter values set");
    }
    if (genders != null && genders.isEmpty()) {
      eb.addError("No gender filter values set");
    }
    if (incomes != null && incomes.isEmpty()) {
      eb.addError("No income filter values set");
    }
    if (contexts != null && contexts.isEmpty()) {
      eb.addError("No context filter values set");
    }
    if (dates != null && dates.min != null && dates.max != null) {
      if (dates.min.compareTo(dates.max) > 0) {
        eb.addError("Start date cannot be after end date");
      }
    }
    return eb;
  }

  /**
   * Equality check between this instance and another instance. This equality check compares all
   * fields including non-final.
   * 
   * @param other Other instance to compare against
   * @return Whether instances are the same
   */
  public boolean isEqual(FilterConfig other) {
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
