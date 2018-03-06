package group33.seg.view.output;

import group33.seg.model.types.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Graph extends JPanel {

  private TimeSeriesCollection dataset = new TimeSeriesCollection();
  private XYPlot plot;

  private ArrayList<TimeSeries> series = new ArrayList<TimeSeries>();

  public Graph(String chartTitle, String xAxisLabel, String yAxisLabel) {

    JFreeChart xylineChart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel,
        dataset, false, true, false);

    // Creates a panel for the chart
    ChartPanel chartPanel = new ChartPanel(xylineChart);
    this.plot = xylineChart.getXYPlot();

    this.setLayout(new GridLayout(1, 1));
    this.add(chartPanel);
    
    
    // FIXME: TEMPORARY CODE TO MAKE GRAPH MORE USABLE [REPLACE ASAP]
    // https://stackoverflow.com/questions/31193099/pan-chart-using-mouse-jfreechart
    chartPanel.setMouseZoomable(false);
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setDomainZoomable(true);
    chartPanel.setRangeZoomable(false);
    chartPanel.setPreferredSize(new Dimension(1680, 1100));
    chartPanel.setZoomTriggerDistance(Integer.MAX_VALUE);
    chartPanel.setFillZoomRectangle(false);
    chartPanel.setZoomOutlinePaint(new Color(0f, 0f, 0f, 0f));
    chartPanel.setZoomAroundAnchor(true);
    try {
        Field mask = ChartPanel.class.getDeclaredField("panMask");
        mask.setAccessible(true);
        mask.set(chartPanel, 0);
    } catch (NoSuchFieldException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    }
    chartPanel.addMouseWheelListener(arg0 -> chartPanel.restoreAutoRangeBounds());
    
  }

  public void addLine(List<Pair<String, Integer>> data) {
    TimeSeries ts = new TimeSeries("" + Math.random());
    series.add(ts);

    for (Pair<String, Integer> d : data) {
      ts.add(Second.parseSecond(d.key), d.value);
    }

    updateCharts();
  }
  
  public void clearLines()  {
    series.clear();
    updateCharts();
  }

  // The dataset is refreshed
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
