package group33.seg.model.configs;

/**
 * Structure-like class for constructing a campaign configuration. All variables are public to
 * allow for easy structure access.
 */
public class CampaignConfig {

  /** Unique identifier of campaign from database */
  public final int uid;

  /** Name of campaign */
  public String name;

  public CampaignConfig(int uid) {
    this.uid = uid;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + uid;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof CampaignConfig))
      return false;
    CampaignConfig other = (CampaignConfig) obj;
    if (uid != other.uid)
      return false;
    return true;
  }
  
}
