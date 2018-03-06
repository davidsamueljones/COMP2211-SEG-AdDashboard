package group33.seg.controller.graphing;

import group33.seg.model.types.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Graph extends JPanel {

  private TimeSeriesCollection dataset = new TimeSeriesCollection();
  private XYPlot plot;

  private ArrayList<TimeSeries> series = new ArrayList<TimeSeries>();

  public Graph(
      String chartTitle, String xAxisLabel, String yAxisLabel, List<Pair<String, Integer>> data) {
    JFreeChart xylineChart =
        ChartFactory.createTimeSeriesChart(
            chartTitle, xAxisLabel, yAxisLabel, dataset, false, true, false);

    // Creates a panel for the chart
    ChartPanel chartPanel = new ChartPanel(xylineChart);

    this.plot = xylineChart.getXYPlot();

    this.addLine(data);
    this.add(chartPanel);
  }

  public void addLine(List<Pair<String, Integer>> data) {
    TimeSeries ts = new TimeSeries("" + Math.random());
    series.add(ts);

    for (Pair<String, Integer> d : data) {
      ts.add(Second.parseSecond(d.key), d.value);
    }

    updateCharts();
  }

  // The dataset is is refreshed
  public void updateCharts() {
    ((TimeSeriesCollection) plot.getDataset()).removeAllSeries();
    displayCharts();
  }

  // Adds all timeseries to the dataset
  public void displayCharts() {
    for (TimeSeries series : series) {
      dataset.addSeries(series);
    }
  }
}
