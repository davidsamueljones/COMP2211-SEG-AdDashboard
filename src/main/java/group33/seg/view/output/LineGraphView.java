package group33.seg.view.output;

import group33.seg.model.types.Pair;
import group33.seg.view.utilities.Accessibility;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LineGraphView extends JPanel {
  private static final long serialVersionUID = -7920465975957290150L;
  private static float MIN_THICKNESS = 2.0f;
  private static float MAX_THICKNESS = 10.0f;
  
  private double scale;
  private TimeSeriesCollection dataset = new TimeSeriesCollection();
  private XYPlot plot;
  private JFreeChart xylineChart;
  private ArrayList<TimeSeries> series = new ArrayList<TimeSeries>();

  public LineGraphView(String chartTitle, String xAxisLabel, String yAxisLabel) {
    xylineChart =
        ChartFactory.createTimeSeriesChart(
            chartTitle, xAxisLabel, yAxisLabel, dataset, false, true, false);

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
    // chartPanel.setPreferredSize(new Dimension(1680, 1100));
    chartPanel.setZoomTriggerDistance(Integer.MAX_VALUE);
    chartPanel.setFillZoomRectangle(false);
    chartPanel.setZoomOutlinePaint(new Color(0f, 0f, 0f, 0f));
    chartPanel.setZoomAroundAnchor(true);
    //    try {
    //        Field mask = ChartPanel.class.getDeclaredField("panMask");
    //        mask.setAccessible(true);
    //        mask.set(chartPanel, 0);
    //    } catch (NoSuchFieldException e) {
    //        e.printStackTrace();
    //    } catch (IllegalAccessException e) {
    //        e.printStackTrace();
    //    }
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

  public void clearLines() {
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
  
  
  public void applyFontScale() {
    // TODO: Replace with themeing code
    Font title = xylineChart.getTitle().getFont();
    xylineChart.getTitle().setFont(Accessibility.scaleFont(title, scale));
    Font xTitle = plot.getDomainAxis().getLabelFont();
    plot.getDomainAxis().setLabelFont(Accessibility.scaleFont(xTitle, scale));
    Font xTicker = plot.getDomainAxis().getTickLabelFont();
    plot.getDomainAxis().setTickLabelFont(Accessibility.scaleFont(xTicker, scale));
    Font yTitle = plot.getRangeAxis().getLabelFont();
    plot.getRangeAxis().setLabelFont(Accessibility.scaleFont(yTitle, scale));
    Font yTicker = plot.getDomainAxis().getTickLabelFont();
    plot.getRangeAxis().setTickLabelFont(Accessibility.scaleFont(yTicker, scale));
  }
  
  public void setFontScale(double scale) {
    this.scale = scale;
    applyFontScale();
  }


  public static BasicStroke getLineStroke(int scale) {
    float dif = MAX_THICKNESS - MIN_THICKNESS;
    float thickness = MIN_THICKNESS + dif * scale / 100.0f;
    return new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  }
  
}
