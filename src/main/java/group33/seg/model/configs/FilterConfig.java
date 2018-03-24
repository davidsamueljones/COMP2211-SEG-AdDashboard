package group33.seg.model.configs;

import java.util.Date;
import java.util.Set;
import org.apache.commons.text.WordUtils;
import group33.seg.lib.Utilities;
import group33.seg.model.utilities.Range;

/**
 * Structure-like class for constructing a filter configuration. All variables are public to allow
 * for easy structure access.
 */
public class FilterConfig {

  /** Campaign to target (should not be null but target all if is) */
  public CampaignConfig campaign;

  /** Age Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Set<Age> ages;

  /** Income Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Set<Income> incomes;

  /** Context Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Set<Context> contexts;

  /** Gender Filter (filtered to those in set, ignored if null, acknowledge if empty) */
  public Set<Gender> genders;

  /**
   * Date Filter (ignore full range if set to null or if not null but individual limit is null
   * ignore the respective individual limit)
   */
  public Range<Date> dates;


  /**
   * Enumeration of possible ages for filtering.
   */
  public enum Age {
    LESS_25("< 25"), BETWEEN_25_34("25-34"), BETWEEN_35_44("35-44"), BETWEEN_45_54(
        "45-54"), MORE_54("> 54");

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
    SHOPPING, BLOG, SOCIAL_MEDIA, NEWS;

    @Override
    public String toString() {
      return Utilities.getTitleCase(super.toString());
    }
  }



}
