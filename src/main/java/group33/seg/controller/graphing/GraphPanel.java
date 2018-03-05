package group33.seg.controller.graphing;

import java.awt.Color;

import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;

import org.jfree.chart.axis.NumberAxis;


import org.jfree.data.time.Day;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
// import java.lang.Object.*;

public class GraphPanel extends JFrame {

  TimeSeriesCollection dataset = new TimeSeriesCollection();
  XYPlot plot;
  
  // Sample arrays filled in for testing purposes
  XYData[] points;
  XYData[] points2;
  XYData[] points3;
  XYData[] points4;

  // lines consists of XYLine that make each TimeSerie
  ArrayList<XYLine> lines = new ArrayList<XYLine>();
  ArrayList<TimeSeries> series = new ArrayList<TimeSeries>();

  // renderOrder keeps track of the IDs of each XYLine
  // the same way they are stored in the dataset
  ArrayList<String> renderOrder = new ArrayList<String>();

  public GraphPanel(String applicationTitle, String chartTitle) {
    super(applicationTitle);
    JFreeChart xylineChart = ChartFactory.createTimeSeriesChart(chartTitle, "Date", "Value",
        createDataset(), false, true, false);


    // Creates a panel for the chart
    ChartPanel chartPanel = new ChartPanel(xylineChart);
    chartPanel.setPreferredSize(new java.awt.Dimension(560, 400));
    plot = xylineChart.getXYPlot();

    // Creates the renderer used for colours, thickness and so on
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    // Sets it to plot
    plot.setRenderer(renderer);

    this.add(chartPanel, BorderLayout.CENTER);
    JPanel control = new JPanel();

    // Used for zoom in, out - to be implemented
    NumberAxis range = (NumberAxis) plot.getRangeAxis();
    DateAxis domain = (DateAxis) plot.getDomainAxis();


    // Just adds some random Graphs for testing
    control.add(new JButton(new AbstractAction("Add Graphs") {
      @Override
      public void actionPerformed(ActionEvent e) {

        createSampleData();

      }
    }));

    control.add(new JButton(new AbstractAction("Color change") {

      @Override
      public void actionPerformed(ActionEvent e) {
        setColor(Color.BLACK, "Two");
      }
    }));

    control.add(new JButton(new AbstractAction("Delete") {

      @Override
      public void actionPerformed(ActionEvent e) {
        deleteLine("Two");
      }
    }));



    // Used for testing to make sure the order of the dataset, renderer
    // and lines array are not going rogue for some reason
    control.add(new JButton(new AbstractAction("Stats") {

      @Override
      public void actionPerformed(ActionEvent e) {
        // range.setRange(0.0, 30.0);
        // series.add(new TimeSeries(series.size()));

        System.out.print("Dataset order");
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
          System.out.print(dataset.getSeries(i).getMaxY() + " ");
        }

        System.out.println(" ");

        System.out.print("Render Order: ");
        for (String s : renderOrder)
          System.out.print(s + ",");

        System.out.println(" ");

        System.out.print("Lines Order: ");
        for (XYLine line : lines)
          System.out.print(line.getID() + ",");

      }
    }));

    this.add(control, BorderLayout.SOUTH);
  }

  private TimeSeriesCollection createDataset() {
    return dataset;
  }

  // Creates 4 different line charts
  private void createSampleData() {

    points = new XYData[31];
    for (int j = 0; j < 31; j++) {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, 2017);
      cal.set(Calendar.MONTH, Calendar.FEBRUARY);
      cal.set(Calendar.DAY_OF_MONTH, j);
      Date date = cal.getTime();
      double value = Math.random() * 5;
      points[j] = new XYData<Date, Double>(date, value);
    }
    addLine(new XYLine(points, "One", Color.WHITE));

    points2 = new XYData[31];
    for (int j = 0; j < 31; j++) {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, 2017);
      cal.set(Calendar.MONTH, Calendar.FEBRUARY);
      cal.set(Calendar.DAY_OF_MONTH, j);
      Date date = cal.getTime();
      double value = Math.random() * 35;
      points2[j] = new XYData<Date, Double>(date, value);
    }
    addLine(new XYLine(points2, "Two", Color.GREEN));

    points3 = new XYData[31];
    for (int j = 0; j < 31; j++) {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, 2017);
      cal.set(Calendar.MONTH, Calendar.FEBRUARY);
      cal.set(Calendar.DAY_OF_MONTH, j);
      Date date = cal.getTime();
      double value = Math.random() * 65;
      points3[j] = new XYData<Date, Double>(date, value);
    }
    addLine(new XYLine(points3, "Three", Color.RED));

    points4 = new XYData[31];
    for (int j = 0; j < 31; j++) {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, 2017);
      cal.set(Calendar.MONTH, Calendar.MARCH);
      cal.set(Calendar.DAY_OF_MONTH, j);
      Date date = cal.getTime();
      double value = Math.random() * 95;
      points4[j] = new XYData<Date, Double>(date, value);
    }
    addLine(new XYLine(points4, "Four", Color.BLACK));


  }


  /*
   * The line is added to the array lines, new TimeSeries created with name the line ID, and the ID
   * is add to the renderer Then the Timeseries coresponding to this line is filled with the data
   * from it and charts are updated
   */
  public void addLine(XYLine line) {
    lines.add(line);
    series.add(new TimeSeries(line.getID()));
    renderOrder.add(line.getID());
    for (int i = 0; i < line.getData().length; i++) {
      Date date = (Date) line.getData()[i].getDate();
      series.get(series.size() - 1).add(new Day(date), line.getData()[i].getValue());
    }
    updateCharts();
  }

  // The dataset is is refreshed
  public void updateCharts() {
    ((TimeSeriesCollection) plot.getDataset()).removeAllSeries();
    displayCharts();
  }

  // Adds all timeseries to the dataset and then assigns
  // the initial colour for the lines so the renderer does not
  public void displayCharts() {
    for (TimeSeries series : series) {
      dataset.addSeries(series);
    }
    for (int i = 0; i < renderOrder.size(); i++) {
      setColor(lines.get(i).getColor(), renderOrder.get(i));
    }
    plot.setDataset(dataset);
  }

  public void setColor(Color c, String ID) {
    if (renderOrder.indexOf(ID) >= 0)
      plot.getRenderer().setSeriesPaint(renderOrder.indexOf(ID), c);
  }

  // Line is deleted from all the arraylists and panel is updated
  public void deleteLine(String ID) {
    dataset.removeSeries(renderOrder.indexOf(ID));
    series.remove(renderOrder.indexOf(ID));
    renderOrder.remove(renderOrder.indexOf(ID));
    lines.remove(lines.indexOf(getLine(ID)));
    updateCharts();
  }

  // Returns the XYLine with the corresponding ID
  public XYLine getLine(String ID) {
    XYLine match = new XYLine();
    for (XYLine line : lines) {
      if (line.getID().equals(ID))
        match = line;
    }
    return match;
  }



  public static void main(String[] args) {
    GraphPanel chart = new GraphPanel("App", "Graphs");
    chart.pack();
    chart.setVisible(true);
  }
}
