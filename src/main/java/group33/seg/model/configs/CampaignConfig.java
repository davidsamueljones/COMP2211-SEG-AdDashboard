package group33.seg.model.configs;

public class CampaignConfig {
  private String name;
  private String database;

  public CampaignConfig(String name, String database) {
    this.name = name;
    this.database = database;
  }

  public String getName() {
    return name;
  }

  public String getDatabase() {
    return database;
  }
}
