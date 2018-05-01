import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.lib.Pair;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.FilterConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QueryTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CompareTest {

  private static DatabaseHandler databaseHandler;
  private static CampaignConfig campaign;

  /** Initialise tables and populate before running the tests */
  @BeforeClass
  public static void init() {
    DashboardController controller = QueryTestUtil.setUp();
    assertNotNull(controller);
    CampaignConfig campaign = controller.imports.getImportedCampaign();
    databaseHandler = controller.database;
  }

  /** Delete tables after testing */
  @AfterClass
  public static void cleanupDb() {
    QueryTestUtil.tearDown(databaseHandler);
  }

  @Test
  public void compareAges() {
      // Count impressions of users of age <25
      FilterConfig minFilter = new FilterConfig();
      minFilter.ages = new ArrayList<>();
      minFilter.ages.add(FilterConfig.Age.LESS_25);

      MetricQuery lowerBound = new MetricQuery(Metric.IMPRESSIONS, null, minFilter);

      List<Pair<String, Number>> minResponse = databaseHandler.getQueryResponse(lowerBound).getResult();
      Number actualMinResult = minResponse.get(0).value;

      // Count impressions of users of age 25-34
      FilterConfig secondRange = new FilterConfig();
      secondRange.ages = new ArrayList<>();
      secondRange.ages.add(FilterConfig.Age.BETWEEN_25_34);

      MetricQuery secondRangeQuery = new MetricQuery(Metric.IMPRESSIONS, null, secondRange);

      List<Pair<String, Number>> secondRangeResponse = databaseHandler.getQueryResponse(secondRangeQuery).getResult();
      Number actualSecondResult = secondRangeResponse.get(0).value;


      // Count impressions of users of age 35-44
      FilterConfig thirdRange = new FilterConfig();
      thirdRange.ages = new ArrayList<>();
      thirdRange.ages.add(FilterConfig.Age.BETWEEN_35_44);

      MetricQuery thirdRangeQuery = new MetricQuery(Metric.IMPRESSIONS, null, thirdRange);

      List<Pair<String, Number>> thirdRangeResponse = databaseHandler.getQueryResponse(thirdRangeQuery).getResult();
      Number actualThirdResult = thirdRangeResponse.get(0).value;

      // Count impressions of users of age 45-54
      FilterConfig fourthRange = new FilterConfig();
      fourthRange.ages = new ArrayList<>();
      fourthRange.ages.add(FilterConfig.Age.BETWEEN_45_54);

      MetricQuery fourthRangeQuery = new MetricQuery(Metric.IMPRESSIONS, null, fourthRange);

      List<Pair<String, Number>> fourthRangeResponse = databaseHandler.getQueryResponse(fourthRangeQuery).getResult();
      Number actualFourthResult = fourthRangeResponse.get(0).value;

      // Count impressions of users of age >54
      FilterConfig maxFilter = new FilterConfig();
      maxFilter.ages = new ArrayList<>();
      maxFilter.ages.add(FilterConfig.Age.MORE_54);

      MetricQuery upperBound = new MetricQuery(Metric.IMPRESSIONS, null, maxFilter);

      List<Pair<String, Number>> maxResponse = databaseHandler.getQueryResponse(upperBound).getResult();
      Number actualMaxResult = maxResponse.get(0).value;

      assertTrue(actualMinResult.longValue() == 13 && actualSecondResult.longValue() == 27 && actualThirdResult.longValue() == 30 && actualFourthResult.longValue() == 51 && actualMaxResult.longValue() == 22);
  }

  @Test
  public void compareIncome() {

      // Count impressions of users with low income
      FilterConfig lowFilter = new FilterConfig();
      lowFilter.incomes = new ArrayList<>();
      lowFilter.incomes.add(FilterConfig.Income.LOW);

      MetricQuery low = new MetricQuery(Metric.IMPRESSIONS, null, lowFilter);

      List<Pair<String, Number>> lowResponse = databaseHandler.getQueryResponse(low).getResult();
      Number actualLowResult = lowResponse.get(0).value;

      // Count impressions of users with medium income
      FilterConfig mediumFilter = new FilterConfig();
      mediumFilter.incomes = new ArrayList<>();
      mediumFilter.incomes.add(FilterConfig.Income.MEDIUM);

      MetricQuery medium = new MetricQuery(Metric.IMPRESSIONS, null, mediumFilter);

      List<Pair<String, Number>> mediumResponse = databaseHandler.getQueryResponse(medium).getResult();
      Number actualMediumResult = mediumResponse.get(0).value;

      // Count impressions of users with high income
      FilterConfig highFilter = new FilterConfig();
      highFilter.incomes = new ArrayList<>();
      highFilter.incomes.add(FilterConfig.Income.HIGH);

      MetricQuery high = new MetricQuery(Metric.IMPRESSIONS, null, highFilter);

      List<Pair<String, Number>> highResponse = databaseHandler.getQueryResponse(high).getResult();
      Number actualHighResult = highResponse.get(0).value;

      assertTrue(actualLowResult.longValue() == 15 && actualMediumResult.longValue() == 61 && actualHighResult.longValue() == 67);
  }

  @Test
  public void compareContext() {
      // Count impressions of users with Shopping context
      FilterConfig shoppingFilter = new FilterConfig();
      shoppingFilter.contexts = new ArrayList<>();
      shoppingFilter.contexts.add(FilterConfig.Context.SHOPPING);

      MetricQuery shopping = new MetricQuery(Metric.IMPRESSIONS, null, shoppingFilter);

      List<Pair<String, Number>> shoppingResponse = databaseHandler.getQueryResponse(shopping).getResult();
      Number actualShoppingResult = shoppingResponse.get(0).value;

      // Count impressions of users with News context
      FilterConfig newsFilter = new FilterConfig();
      newsFilter.contexts = new ArrayList<>();
      newsFilter.contexts.add(FilterConfig.Context.NEWS);

      MetricQuery news = new MetricQuery(Metric.IMPRESSIONS, null, newsFilter);

      List<Pair<String, Number>> newsResponse = databaseHandler.getQueryResponse(news).getResult();
      Number actualNewsResult = newsResponse.get(0).value;

      // Count impressions of users with Hobbies context
      FilterConfig hobbiesFilter = new FilterConfig();
      hobbiesFilter.contexts = new ArrayList<>();
      hobbiesFilter.contexts.add(FilterConfig.Context.HOBBIES);

      MetricQuery hobbies = new MetricQuery(Metric.IMPRESSIONS, null, hobbiesFilter);

      List<Pair<String, Number>> hobbiesResponse = databaseHandler.getQueryResponse(hobbies).getResult();
      Number actualHobbiesResult = hobbiesResponse.get(0).value;

      // Count impressions of users with Travel context
      FilterConfig travelFilter = new FilterConfig();
      travelFilter.contexts = new ArrayList<>();
      travelFilter.contexts.add(FilterConfig.Context.TRAVEL);

      MetricQuery travel = new MetricQuery(Metric.IMPRESSIONS, null, travelFilter);

      List<Pair<String, Number>> travelResponse = databaseHandler.getQueryResponse(travel).getResult();
      Number actualTravelResult = travelResponse.get(0).value;

      // Count impressions of users with Travel context
      FilterConfig socialFilter = new FilterConfig();
      socialFilter.contexts = new ArrayList<>();
      socialFilter.contexts.add(FilterConfig.Context.SOCIAL_MEDIA);

      MetricQuery socialMedia = new MetricQuery(Metric.IMPRESSIONS, null, socialFilter);

      List<Pair<String, Number>> socialResponse = databaseHandler.getQueryResponse(socialMedia).getResult();
      Number actualSocialResponse = socialResponse.get(0).value;

      assertTrue(actualShoppingResult.longValue() == 27 && actualNewsResult.longValue() == 36 && actualHobbiesResult.longValue() == 0 && actualTravelResult.longValue() == 2 && actualSocialResponse.longValue() == 53);
  }

  @Test
  public void compareGender() {
      // Count impressions of female
      FilterConfig femaleFilter = new FilterConfig();
      femaleFilter.genders = new ArrayList<>();
      femaleFilter.genders.add(FilterConfig.Gender.FEMALE);

      MetricQuery femaleImpressions = new MetricQuery(Metric.IMPRESSIONS, null, femaleFilter);

      List<Pair<String, Number>> femaleResponse = databaseHandler.getQueryResponse(femaleImpressions).getResult();
      Number actualFResult = femaleResponse.get(0).value;

      // Count impressions of male users
      FilterConfig maleFilter = new FilterConfig();
      maleFilter.genders = new ArrayList<>();
      maleFilter.genders.add(FilterConfig.Gender.MALE);

      MetricQuery maleImpressions = new MetricQuery(Metric.IMPRESSIONS, null, maleFilter);

      List<Pair<String, Number>> maleResponse = databaseHandler.getQueryResponse(maleImpressions).getResult();
      Number actualMResult = maleResponse.get(0).value;

      assertTrue(actualFResult.longValue() == 61 && actualMResult.longValue() == 82);

  }

  @Test
    public void nonExistentCampaign() {
      CampaignConfig config = new CampaignConfig(8);
      MetricQuery impressions = new MetricQuery(Metric.IMPRESSIONS, null, null,null, config);

      List<Pair<String, Number>> queryResponse = databaseHandler.getQueryResponse(impressions).getResult();
      Number actualImpressions = queryResponse.get(0).value;

      assertTrue(actualImpressions.longValue() == 0);
  }
}
