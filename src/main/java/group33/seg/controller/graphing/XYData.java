package group33.seg.controller.graphing;

public class XYData<Date,Double> {
  
  public final Date date;
  public final double value;

  public XYData(Date date, double value) {
    this.date = date;
    this.value = value;
  }

  public Date getDate() { return date; }
  public double getValue() { return value; }

  
}
