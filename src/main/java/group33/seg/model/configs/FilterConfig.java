package group33.seg.model.configs;

import java.util.Date;
import java.util.Set;
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


  enum Age {
    LESS_25, BETWEEN_25_34, BETWEEN_35_44, BETWEEN_45_54, MORE_54;
  }

  enum Income {
    LOW, MEDIUM, HIGH,
  }

  enum Context {
    SHOPPING, BLOG, SOCIAL_MEDIA, NEWS,
  }

  enum Gender {
    MALE, FEMALE;
  }

}
