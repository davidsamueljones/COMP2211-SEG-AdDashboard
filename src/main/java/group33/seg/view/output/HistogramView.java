package group33.seg.view.output;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import group33.seg.lib.Range;
import group33.seg.model.configs.HistogramConfig;

public class HistogramView extends XYGraphView {
  private static final long serialVersionUID = 794738588952920667L;

  public static Color DEFAULT_FOREGROUND = Color.getHSBColor(0.583f, 1, 0.8f);

  private XYBarRenderer renderer;

  private SimpleHistogramDataset dataset;

  private List<Double> values;
  private List<Double> bins;
  private Range<Double> range;

  /**
   * Fully configure an empty chart and its controls.
   */
  public HistogramView(boolean useBuffer) {
    super(useBuffer);
  }

  @Override
  protected void initChart() {
    dataset = new SimpleHistogramDataset("Bins");
    dataset.setAdjustForBinSize(false);
    NumberAxis frequencyAxis = new NumberAxis();
    frequencyAxis.setNumberFormatOverride(new DecimalFormat("0"));
    NumberAxis valueAxis = new NumberAxis();
    valueAxis.setAutoRangeIncludesZero(false);
    
    renderer = new XYBarRenderer();
    plot = new XYPlot(dataset, valueAxis, frequencyAxis, renderer);
    plot.setRenderer(renderer);
    plot.setDomainZeroBaselineVisible(true);
    plot.setRangeZeroBaselineVisible(true);

    chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
  }

  /**
   * Set the graph's properties based off the given configuration.
   * 
   * @param graph Configuration to use for set
   */
  public void setGraphProperties(HistogramConfig graph) {
    pnlChart.setChart(chart);
    chart.setTitle(graph.title);
    plot.setBackgroundPaint(graph.background);
    Color colGridlines = XYGraphView.getGridlineColor(graph.background);
    plot.setDomainGridlinePaint(colGridlines);
    plot.setRangeGridlinePaint(colGridlines);
    plot.getDomainAxis().setLabel(graph.xAxisTitle);
    plot.getRangeAxis().setLabel(graph.yAxisTitle);
    renderer.setBarPainter(new StandardXYBarPainter());
    renderer.setSeriesPaint(0, graph.barColor);
    renderer.setSeriesOutlinePaint(0, XYGraphView.getGridlineColor(graph.barColor));
    renderer.setDrawBarOutline(true);
  }

  /**
   * Remove all plotted data from the graph and reset the title and axis labels.
   */
  public void clearGraph() {
    chart.setTitle("");
    plot.getDomainAxis().setLabel("");
    plot.getRangeAxis().setLabel("");
    plot.setBackgroundPaint(XYGraphView.DEFAULT_BACKGROUND);
    dataset.clearObservations();
  }

  /**
   * Load the given data into the graph. Update bins if required due to a change of range.
   * 
   * @param data Data to load
   */
  public void setGraphData(List<Double> data) {
    dataset.clearObservations();
    Range<Double> range = null;
    if (data != null) {
      range = new Range<>(Collections.min(data), Collections.max(data));
      if (!range.equals(this.range)) {
        this.range = range;
        makeBins(bins, true);
      }
      for (Double value : data) {
        try {
          dataset.addObservation(value);
        } catch (Exception e) {
          System.err.println("No bin for " + value);
        }
      }
    }
    this.range = range;
    this.values = data;
  }

  /**
   * Set the graphs bins using a set of weights. This clears all data so it will reload it from a
   * reference set. This can optionally be turned off.
   * 
   * @param bins Weights of bins
   * @param clear Whether to keep data cleared
   */
  public void makeBins(List<Double> bins, boolean clear) {
    dataset.removeAllBins();
    dataset.setAdjustForBinSize(true);
    this.bins = bins;
    if (this.bins != null && this.range != null) {
      double width = range.max - range.min;
      double lower = range.min;
      for (Double weight : bins) {
        double upper = lower + weight * width;
        SimpleHistogramBin bin = new SimpleHistogramBin(lower, upper, true, true);
        dataset.addBin(bin);
        lower = upper + 0.000000001;
      }
      if (!clear) {
        setGraphData(values);
      }
    }
  }

}
