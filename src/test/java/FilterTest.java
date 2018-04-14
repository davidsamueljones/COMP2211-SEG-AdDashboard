import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import group33.seg.controller.DashboardController;
import group33.seg.controller.database.DatabaseConfig;
import group33.seg.controller.database.tables.CampaignTable;
import group33.seg.controller.database.tables.ClickLogTable;
import group33.seg.controller.database.tables.ImpressionLogTable;
import group33.seg.controller.database.tables.ServerLogTable;
import group33.seg.controller.handlers.DatabaseHandler;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.model.configs.CampaignImportConfig;
import group33.seg.model.configs.FilterConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;

public class FilterTest {

  private static final String CAMPAIGN_NAME = "queryTest";
  private static final String PATH_TO_DATASET = "src/test/resources/example-dataset/";
  private static final String PATH_CLICK_LOG = PATH_TO_DATASET + "click_log.csv";
  private static final String PATH_IMPRESSION_LOG = PATH_TO_DATASET + "impression_log.csv";
  private static final String PATH_SERVER_LOG = PATH_TO_DATASET + "server_log.csv";

  private static final double MARGIN_OF_ERROR = 0.01;

  private static final String DATABASE_CREDENTIALS = "src/test/resources/test.properties";

  private static DatabaseHandler databaseHandler;
  private static CampaignConfig campaign;

  /** Initialise tables and data before running all the tests */
  @BeforeClass
  public static void init() throws FileNotFoundException {
    try {
      // Initialise a controller for these tests (no model or view needed)
      DashboardController controller = new DashboardController(null, null);
      try {
        DatabaseConfig database = new DatabaseConfig(DATABASE_CREDENTIALS);
        controller.database.refreshConnections(database, 10);
      } catch (FileNotFoundException e) {
        System.err.println("Unable to test database connection configuration file");
      } catch (SQLException e) {
        System.err.println("Unable to create database connections, check that hostname, "
            + "port, username and password are all correct in the configuration file");
      }

      // Do import using custom test configuration
      CampaignImportConfig importConfig = new CampaignImportConfig(CAMPAIGN_NAME, PATH_CLICK_LOG,
          PATH_IMPRESSION_LOG, PATH_SERVER_LOG);
      boolean importComplete = controller.imports.doImport(importConfig);

      while (controller.imports.isOngoing()) {
        Thread.sleep(100);
      }
      campaign = controller.imports.getImportedCampaign();

      // setup database handler
      if (importComplete) {
        databaseHandler = controller.database;
      } else {
        databaseHandler = null;
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Delete the tables after testing
   */
  @AfterClass
  public static void cleanupDb() throws SQLException {
    CampaignTable cmp = new CampaignTable();
    ServerLogTable sl = new ServerLogTable();
    ClickLogTable cl = new ClickLogTable();
    ImpressionLogTable il = new ImpressionLogTable();
    Connection c = databaseHandler.getConnection();
    sl.dropTable(c);
    cl.dropTable(c);
    il.dropTable(c);
    cmp.dropTable(c);
    databaseHandler.returnConnection(c);
  }

  public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
    Set<Set<T>> sets = new HashSet<Set<T>>();
    if (originalSet.isEmpty()) {
        sets.add(new HashSet<T>());
        return sets;
    }
    List<T> list = new ArrayList<T>(originalSet);
    T head = list.get(0);
    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
    for (Set<T> set : powerSet(rest)) {
        Set<T> newSet = new HashSet<T>();
        newSet.add(head);
        newSet.addAll(set);
        sets.add(newSet);
        sets.add(set);
    }       
    return sets;
  }

  private static <T> boolean matching(Set<T> combination, HashMap<T, Long> values, Long all, Long result) {
    if (combination.isEmpty()) {
      return result.equals(all);
    }

    Long expected = 0L;
    for (T filter: combination) {
      expected += values.get(filter);
    }

    if (expected.equals(result)) {
      return true;
    } else {
      System.out.println("expected " + expected + " was " + result);
      return false;
    }
  }

  public boolean ageMatches(Set<FilterConfig.Age> ages, Long result) {
    HashMap<FilterConfig.Age, Long> values = new HashMap<>();
    values.put(FilterConfig.Age.LESS_25, 13L);
    values.put(FilterConfig.Age.BETWEEN_25_34, 27L);
    values.put(FilterConfig.Age.BETWEEN_35_44, 30L);
    values.put(FilterConfig.Age.BETWEEN_45_54, 51L);
    values.put(FilterConfig.Age.MORE_54, 22L);

    return matching(ages, values, 143L, result);
  }

  @Test
  public void ageTest(){
    HashSet<FilterConfig.Age> ages = new HashSet<>();
    ages.add(FilterConfig.Age.LESS_25);
    ages.add(FilterConfig.Age.BETWEEN_25_34);
    ages.add(FilterConfig.Age.BETWEEN_35_44);
    ages.add(FilterConfig.Age.BETWEEN_45_54);
    ages.add(FilterConfig.Age.MORE_54);

    Set<Set<FilterConfig.Age>> combinations = powerSet(ages);

    for (Set<FilterConfig.Age> combination : combinations) {
      FilterConfig config = new FilterConfig();
      
      if (!combination.isEmpty()) {
        config.ages = new ArrayList<>();
      }
      
      for (FilterConfig.Age age : combination) {
        config.ages.add(age);
      }
      //test on statistic
      MetricQuery mq = new MetricQuery(Metric.IMPRESSIONS, null, config);

      
      List<Pair<String, Number>> response = databaseHandler.getQueryResponse(mq).getResult();
      assertTrue(ageMatches(combination, (Long) response.get(0).value));
    }
  }

  public boolean contextMatches(Set<FilterConfig.Context> contexts, Long result) {
    HashMap<FilterConfig.Context, Long> values = new HashMap<>();
    values.put(FilterConfig.Context.BLOG, 25L);
    values.put(FilterConfig.Context.HOBBIES, 0L);
    values.put(FilterConfig.Context.NEWS, 36L);
    values.put(FilterConfig.Context.SHOPPING, 27L);
    values.put(FilterConfig.Context.SOCIAL_MEDIA, 53L);
    values.put(FilterConfig.Context.TRAVEL, 2L);

    return matching(contexts, values, 143L, result);
  }

  @Test
  public void contextTest(){
    HashSet<FilterConfig.Context> contexts = new HashSet<>();
    contexts.add(FilterConfig.Context.BLOG);
    //contexts.add(FilterConfig.Context.HOBBIES);
    contexts.add(FilterConfig.Context.NEWS);
    contexts.add(FilterConfig.Context.SHOPPING);
    contexts.add(FilterConfig.Context.SOCIAL_MEDIA);
    contexts.add(FilterConfig.Context.TRAVEL);

    Set<Set<FilterConfig.Context>> combinations = powerSet(contexts);

    for (Set<FilterConfig.Context> combination : combinations) {
      FilterConfig config = new FilterConfig();
      
      if (!combination.isEmpty()) {
        config.contexts = new ArrayList<>();
      }

      StringBuilder testName = new StringBuilder();
      
      for (FilterConfig.Context context : combination) {
        config.contexts.add(context);
        testName.append(context.toString() + ", ");
      }
      //test on statistic
      MetricQuery mq = new MetricQuery(Metric.IMPRESSIONS, null, config);

      
      List<Pair<String, Number>> response = databaseHandler.getQueryResponse(mq).getResult();
      assertTrue(testName.append(" is broken").toString(),contextMatches(combination, (Long) response.get(0).value));
    }
  }

  public boolean genderMatches(Set<FilterConfig.Gender> ages, Long result) {
    HashMap<FilterConfig.Gender, Long> values = new HashMap<>();
    values.put(FilterConfig.Gender.FEMALE, 61L);
    values.put(FilterConfig.Gender.MALE, 82L);

    return matching(ages, values, 143L, result);
  }

  @Test
  public void genderTest(){
    HashSet<FilterConfig.Gender> genders = new HashSet<>();
    genders.add(FilterConfig.Gender.FEMALE);
    genders.add(FilterConfig.Gender.MALE);

    Set<Set<FilterConfig.Gender>> combinations = powerSet(genders);

    for (Set<FilterConfig.Gender> combination : combinations) {
      FilterConfig config = new FilterConfig();
      
      if (!combination.isEmpty()) {
        config.genders = new ArrayList<>();
      }

      StringBuilder testName = new StringBuilder();
      
      for (FilterConfig.Gender gender : combination) {
        config.genders.add(gender);
        testName.append(gender.toString() + ", ");
      }
      //test on statistic
      MetricQuery mq = new MetricQuery(Metric.IMPRESSIONS, null, config);

      
      List<Pair<String, Number>> response = databaseHandler.getQueryResponse(mq).getResult();
      assertTrue(testName.append(" is broken").toString(),genderMatches(combination, (Long) response.get(0).value));
    }
  }

  public boolean incomeMatches(Set<FilterConfig.Income> incomes, Long result) {
    HashMap<FilterConfig.Income, Long> values = new HashMap<>();
    values.put(FilterConfig.Income.HIGH, 67L);
    values.put(FilterConfig.Income.MEDIUM, 61L);
    values.put(FilterConfig.Income.LOW, 15L);

    return matching(incomes, values, 143L, result);
  }

  @Test
  public void incomeTest(){
    HashSet<FilterConfig.Income> incomes = new HashSet<>();
    incomes.add(FilterConfig.Income.HIGH);
    incomes.add(FilterConfig.Income.MEDIUM);
    incomes.add(FilterConfig.Income.LOW);

    Set<Set<FilterConfig.Income>> combinations = powerSet(incomes);

    for (Set<FilterConfig.Income> combination : combinations) {
      FilterConfig config = new FilterConfig();
      
      if (!combination.isEmpty()) {
        config.incomes = new ArrayList<>();
      }

      StringBuilder testName = new StringBuilder();
      
      for (FilterConfig.Income income : combination) {
        config.incomes.add(income);
        testName.append(income.toString() + ", ");
      }
      //test on statistic
      MetricQuery mq = new MetricQuery(Metric.IMPRESSIONS, null, config);

      
      List<Pair<String, Number>> response = databaseHandler.getQueryResponse(mq).getResult();
      assertTrue(testName.append(" is broken").toString(),incomeMatches(combination, (Long) response.get(0).value));
    }
  }
}
