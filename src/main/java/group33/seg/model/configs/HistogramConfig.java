package group33.seg.model.configs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.controller.utilities.GraphVisitor;

public class HistogramConfig extends GraphConfig {
  private static final long serialVersionUID = -3528347690642461216L;

  /** Query to fetch data with */
  public MetricQuery query; 
  
  /** List of bins weightings (use sum of all weightings to normalise) */
  public List<Integer> bins;
  
  /** Colour of bars plotted */
  public Color barColor;
  
  /**
   * Instantiate a histogram configuration with a random UUID.
   */
  public HistogramConfig() {
    this(null);
  }

  /**
   * Instantiate a histogram with a given UUID.
   * 
   * @param uuid Unique identifier for histogram
   */
  public HistogramConfig(String uuid) {
    super(uuid);
  }
  
  @Override
  public void accept(GraphVisitor visitor) {
    visitor.visit(this);
  }
  
  /**
   * Get the bin weightings so that the sum of all bin weightings is 1.
   * 
   * @return Normalised bin weightings
   */
  public List<Double> getNormalisedBins() {
    
    List<Double> normalised = new ArrayList<Double>();
    if (bins != null) {
      int sum = 0;
      for (Integer weight : bins) {
        sum += weight;
      }
      for (Integer weight : bins) {
        normalised.add(weight / (double) sum);
      }
    }
    return normalised;
  }
  
  @Override
  public String inText() {
    StringBuilder builder = new StringBuilder(super.inText());
    builder.append("<br><b>Bins:</b><br>");
    List<Double> bins = getNormalisedBins();
    if (bins == null || bins.isEmpty()) {
      builder.append("* No Bins *");
    } else {
      Iterator<Double> itrBins = bins.iterator();
      int count = 0;
      while (itrBins.hasNext()) {
        count++;
        builder.append(String.format("Bin %d [%.2d]", count, itrBins.next()));
        if (itrBins.hasNext()) {
          builder.append(", ");
        }
      }
    }
    return builder.toString();
  }
  
  @Override
  public ErrorBuilder validate() {
    ErrorBuilder eb = super.validate();

    if (query == null) {
      eb.addError("Histogram must have a query");
    }
    if (barColor == null) {
      eb.addError("No bar colour set");
    }
    if (barColor == null) {
      eb.addError("No bar colour set");
    }    
    if (bins == null || bins.isEmpty()) {
      eb.addError("Must have at least one bin");
    }

    return eb;
  }

}
