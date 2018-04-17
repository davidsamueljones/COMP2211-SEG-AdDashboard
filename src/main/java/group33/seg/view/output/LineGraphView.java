package group33.seg.view.output;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.configs.LineGraphConfig.Mode;
import group33.seg.model.types.Pair;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.CustomChartPanel;
import group33.seg.view.utilities.CustomDateAxis;

public class LineGraphView extends JPanel {
  private static final long serialVersionUID = -7920465975957290150L;

  public static float MIN_THICKNESS = 1.0f;
  public static float MAX_THICKNESS = 5.0f;
 
  private CustomChartPanel pnlChart;

  private JFreeChart chart;

  private XYPlot plot;

  /** Domain axis (main one that is displayed only) */
  private CustomDateAxis timeAxis;

  /** Range axis for all modes */
  private NumberAxis valueAxis;

  /** Dataset used for NORMAL mode (OVERLAY mode uses as reference) */
  private TimeSeriesCollection dataset;

  /** Renderer used for NORMAL mode */
  private XYLineAndShapeRenderer renderer;

  /** Configurations for each line as reference */
  private List<LineConfig> configs;

  /** Whether legend should be enabled */
  private boolean legendEnabled;

  /** The current graphing mode */
  private Mode mode;

  /**
   * Fully configure an empty chart and its controls.
   */
  public LineGraphView(boolean useBuffer) {
    initChart();
    initGUI(useBuffer);
    initControlScheme();
  }

  /**
   * Initialise the GUI and any event listeners.
   */
  private void initGUI(boolean useBuffer) {
    setLayout(new BorderLayout(0, 0));

    // Chart panel
    // Configure chart with:
    // * No preferred size, let view control
    // * Apply low minimum and high maximum draw sizing to avoid distortion
    // * Buffer used if indicated (otherwise not)
    // * Disable property changing menu item, allow the rest
    pnlChart = new CustomChartPanel(chart, 0, 0, 10, 10, Integer.MAX_VALUE, Integer.MAX_VALUE,
        useBuffer, false, true, true, true, true, true);

    this.add(pnlChart, BorderLayout.CENTER);

    // Chart controls
    JToolBar tlbControls = new JToolBar();
    tlbControls.setRollover(true);
    add(tlbControls, BorderLayout.SOUTH);

    JButton btnPan = new JButton();
    btnPan.setToolTipText("<html>Enable panning controls for the current chart."
        + "<br><br><b>Scheme controls:</b><p>- Mouse Drag: Pan the view</p>"
        + "<p>- Scroll Wheel: Zoom in/out the range and domain</p>"
        + "<p>- Ctrl/Cmd + Scroll Wheel: Zoom in/out the the range</p>"
        + "<p>- Alt + Scroll Wheel: Zoom in/out the the domain</p>"
        + "<p>- Double Mouse Click: Reset axis");
    btnPan.setIcon(new ImageIcon(getClass().getResource("/icons/hand-empty.png")));
    btnPan.setSelected(true);
    tlbControls.add(btnPan);

    JButton btnZoom = new JButton();
    btnZoom.setToolTipText(
        "<html>Enable zooming controls for the current chart.<br><br><b>Scheme controls:</b>"
            + "<p>- Mouse Drag: Zoom axis to match created box</p>"
            + "<p>- Alt + Mouse Drag: Pan the view</p><p>- Double Mouse Click: Reset axis");
    btnZoom.setIcon(new ImageIcon(getClass().getResource("/icons/magnifying-glass.png")));
    tlbControls.add(btnZoom);

    tlbControls.addSeparator();

    JButton btnSave = new JButton();
    btnSave.setToolTipText("Export the currently displayed graph as an image.");
    btnSave.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
    tlbControls.add(btnSave);

    JButton btnPrint = new JButton();
    btnPrint.setToolTipText("Print the currently displayed graph.");
    btnPrint.setIcon(new ImageIcon(getClass().getResource("/icons/print.png")));
    tlbControls.add(btnPrint);

    tlbControls.addSeparator();

    // Enable pan mode
    btnPan.addActionListener(e -> {
      if (!btnPan.isSelected()) {
        btnPan.setSelected(true);
        btnZoom.setSelected(false);
        enablePanMode();
      }
    });

    // Enable zoom box mode
    btnZoom.addActionListener(e -> {
      if (!btnZoom.isSelected()) {
        btnPan.setSelected(false);
        btnZoom.setSelected(true);
        enableBoxZoom();
      }
    });

    // Display user save option
    btnSave.addActionListener(e -> {
      try {
        pnlChart.doSaveAs();
      } catch (IOException e1) {
        System.err.println("Unable to save image");
      }
    });

    // Display user print option
    btnPrint.addActionListener(e -> {
      pnlChart.createChartPrintJob();
    });
  }

  /**
   * Initialise an empty chart.
   */
  private void initChart() {
    // Initialise reference data structures
    this.configs = new ArrayList<>();
    this.dataset = new TimeSeriesCollection();
    // Create X Axis
    timeAxis = new CustomDateAxis();
    timeAxis.setLowerMargin(0.02);
    timeAxis.setUpperMargin(0.02);
    // Create Y Axis
    valueAxis = new NumberAxis();
    valueAxis.setAutoRangeIncludesZero(false);
    // Create a new renderer
    this.renderer = new XYLineAndShapeRenderer(true, false);
    // Create plot and attach it to a chart
    this.plot = new XYPlot(dataset, timeAxis, valueAxis, renderer);
    this.chart = new JFreeChart(null, null, plot, true);
  }

  /**
   * Initialise the default control scheme of the chart.
   */
  private void initControlScheme() {
    enableGlobalBehaviour();
    // Enable zoom using scroll as default
    enablePanMode();
  }

  /**
   * Handle any behaviour that must occur after any graph content change or any property changes.
   */
  private void refresh() {
    switch (mode) {
      case NORMAL:
        enableNormalMode();
        break;
      case OVERLAY:
        enableOverlayMode();
        break;
      default:
        throw new IllegalArgumentException("Invalid graph mode");
    }
  }

  /**
   * Set the graph's properties based off the given configuration.
   * 
   * @param graph Configuration to use for set
   */
  public void setGraphProperties(LineGraphConfig graph) {
    pnlChart.setChart(chart);
    chart.setTitle(graph.title);
    plot.getDomainAxis().setLabel(graph.xAxisTitle);
    plot.getRangeAxis().setLabel(graph.yAxisTitle);
    setLegendEnabled(graph.showLegend);
    this.mode = graph.mode;
    refresh();
  }

  /**
   * Register a new line with the view, the line's respective plot will be empty until data is set.
   * Nothing will happen if the line already exists.
   * 
   * @param line Configuration to create line plot for
   * @return Whether line could be added
   */
  public boolean addLine(LineConfig line) {
    TimeSeries ex = getLineSeries(line);
    if (ex == null) {
      TimeSeries ts = new TimeSeries(line.uuid);
      configs.add(line);
      dataset.addSeries(ts);
      return true;
    }
    return false;
  }

  /**
   * For a given line configuration, update its respective line plot's data, using the provided data
   * set. The line must have been added using {@link addLine} for this to be successful.
   * 
   * @param line Line configuration for which to modify
   * @param data Dataset to replace existing with
   * @return Whether the data was updated successfully
   */
  public boolean setLineData(LineConfig line, List<Pair<String, Number>> data) {
    TimeSeries ts = getLineSeries(line);
    if (ts == null) {
      return false;
    }
    int idx = dataset.indexOf(ts);
    configs.set(idx, line);
    setSeriesData(ts, data);
    return true;
  }

  /**
   * For a given line configuration, update its respective line plot's properties, using the
   * configuration properties.
   * 
   * @param line Line configuration for which to modify
   */
  public void setLineProperties(LineConfig line) {
    TimeSeries ts = getLineSeries(line);
    if (ts != null) {
      int idx = dataset.indexOf(ts);
      configs.set(idx, line);
      setSeriesStyle(idx, line.color, getLineStroke(line.thickness), line.hide);
    }
  }

  /**
   * Remove the given line configuration from the dataset. This will be reflected in the graph view.
   * 
   * @param line Line to remove
   * @return Whether the line was removed
   */
  public boolean removeLine(LineConfig line) {
    TimeSeries ex = getLineSeries(line);
    if (ex != null) {
      configs.remove(line);
      dataset.removeSeries(ex);
      applySeriesStyles();
      return true;
    }
    return false;
  }

  /**
   * Remove all plotted data from the graph and reset the title and axis labels.
   */
  public void clearGraph() {
    clearLines();
    chart.setTitle("");
    plot.getDomainAxis().setLabel("");
    plot.getRangeAxis().setLabel("");
  }

  /**
   * Remove all plotted data from the graph.
   */
  public void clearLines() {
    configs.clear();
    dataset.removeAllSeries();
  }

  /**
   * For the given time series, clear the current dataset and replace it with the given data.
   * 
   * @param ts Time series for which to set data
   * @param data Replacement data
   */
  private void setSeriesData(TimeSeries ts, List<Pair<String, Number>> data) {
    ts.clear();
    for (Pair<String, Number> d : data) {
      Second x = Second.parseSecond(d.key);
      if (d.value != null) {
        ts.add(x, d.value);
      } else {
        ts.add(x, 0);
      }
    }
    refresh();
  }

  /**
   * Update the renderer style for a single series.
   * 
   * @param idx Index of series in renderer
   * @param color Colour to use for series
   * @param stroke Stroke to use for series
   */
  private void setSeriesStyle(int idx, Color color, Stroke stroke, boolean hidden) {
    renderer.setSeriesPaint(idx, color);
    renderer.setSeriesStroke(idx, stroke);
    renderer.setSeriesVisible(idx, !hidden);
    refresh();
  }

  /**
   * Find the time series of a given line configuration.
   * 
   * @param line Line for which to find a configuration
   * @return Corresponding time series if it exists, otherwise null
   */
  private TimeSeries getLineSeries(LineConfig line) {
    List<TimeSeries> tss = getSeries();
    for (TimeSeries ts : tss) {
      if (ts.getKey().equals(line.uuid)) {
        return ts;
      }
    }
    return null;
  }

  /**
   * Get all time series in the current dataset safely casted.
   * 
   * @return List of time series in dataset
   */
  private List<TimeSeries> getSeries() {
    List<TimeSeries> series = new ArrayList<>();
    for (Object obj : dataset.getSeries()) {
      if (obj instanceof TimeSeries) {
        TimeSeries ts = (TimeSeries) obj;
        series.add(ts);
      }
    }
    return series;
  }

  /**
   * Apply all line configuration settings to the renderer in their current order. This has the
   * effect of reapplying all line rendering properties to make renderer in line with plot.
   */
  private void applySeriesStyles() {
    int idx = 0;
    for (LineConfig config : configs) {
      setSeriesStyle(idx, config.color, getLineStroke(config.thickness), config.hide);
      idx++;
    }
  }

  /**
   * Set whether legend is enabled, redrawing automatically to reflect this.
   */
  private void setLegendEnabled(boolean enabled) {
    legendEnabled = enabled;
    redrawLegend();
  }

  /**
   * Handle whether a legend should be displayed and the generation of it if required.
   */
  private void redrawLegend() {
    if (legendEnabled) {
      plot.setFixedLegendItems(generateLegend());
    } else {
      plot.setFixedLegendItems(new LegendItemCollection());
    }
  }

  /**
   * Using the current lines in the view, create a corresponding legend using their configuration.
   * 
   * @return A set of legend items to be used as a legend
   */
  private LegendItemCollection generateLegend() {
    LegendItemCollection legend = new LegendItemCollection();
    // Generate full legend
    for (LineConfig config : configs) {
      if (!config.hide) {
        LegendItem item = new LegendItem(config.identifier, config.color);
        item.setToolTipText(String.format("<html>%s</html>", config.inText()));
        legend.add(item);
      }
    }
    return legend;
  }

  /**
   * Handle behaviour for normal mode, this uses a single dataset, renderer and a single normal time
   * series axis. All other mode behaviour is first cleared.
   */
  private void enableNormalMode() {
    int i = 0;
    while (plot.getDataset(i) != null) {
      plot.setDataset(i, null);
      plot.setDomainAxis(i, null);
      plot.setRenderer(i, null);
      i++;
    }
    plot.setDomainAxis(timeAxis);
    plot.setDataset(dataset);
    plot.setRenderer(renderer);
    pnlChart.setFixedAutoRanges(null);
    timeAxis.setMode(Mode.NORMAL);
  }

  /**
   * Handle behaviour for overlay mode. This plots all dataset series with their first data value
   * displayed at the origin and the succeeding values successive to this. This is handled by
   * splitting the main dataset into many smaller datasets (one series per dataset). Each dataset
   * has an invisible axis synchronised with the main axis to allow for this behaviour. The main
   * axis is put into overlay mode so that it can use relative date formatting. By setting its base
   * to its respective datasets lowest value, the displayed axis is indexed at 0d0h0m0s.
   */
  private void enableOverlayMode() {
    // Ensure normal mode is set to clear any odd state
    enableNormalMode();

    // Change the plot to use a different dataset for each series, keeping track of created axis
    // (including the main axis)
    List<TimeSeries> series = getSeries();
    List<ValueAxis> axis = new ArrayList<>();
    axis.add(timeAxis);
    int n = 0; // index for created axis
    for (int i = 0; i < series.size(); i++) {
      TimeSeries s = series.get(i);
      LineConfig l = configs.get(i);


      // We must disregard any hidden datasets so they don't distort auto ranging
      if (!l.hide) {
        TimeSeriesCollection d = new TimeSeriesCollection(s);
        plot.setDataset(n, d);
        if (n > 0) {
          // Configure another axis for the series (hidden)
          ValueAxis a = new DateAxis();
          a.setVisible(false);
          plot.setDomainAxis(n, a);
          axis.add(a);
        }
        // Map the series to this axis
        plot.mapDatasetToDomainAxis(n, n);
        // Configure and set a dataset specific renderer
        XYLineAndShapeRenderer r = new XYLineAndShapeRenderer(true, false);
        r.setSeriesPaint(0, renderer.getSeriesPaint(i));
        r.setSeriesStroke(0, renderer.getSeriesStroke(i));
        plot.setRenderer(n, r);
        n++;
      }
    }

    // Track any data that gets cleared and needs to be reapplied
    final double lowerMargin = timeAxis.getLowerMargin();
    final double upperMargin = timeAxis.getUpperMargin();
    // Ensure no margin is set as it makes calculations simpler
    for (ValueAxis a : axis) {
      a.setLowerMargin(0);
      a.setUpperMargin(0);
    }
    // Set all axis bounds to fit their data
    pnlChart.restoreAutoBounds();

    // Find the largest dataset to determine the synchronised autorange limits
    double longest = 0;
    for (ValueAxis a : axis) {
      double len = a.getUpperBound() - a.getLowerBound();
      if (len > longest) {
        longest = len;
      }
    }

    // Synchronise autorange limits and ensure margins are reset to their originals
    List<Pair<ValueAxis, Range>> ranges = new ArrayList<>();
    for (ValueAxis a : axis) {
      double lower = a.getLowerBound();
      double upper = lower + longest;
      ranges.add(new Pair<>(a, new Range(lower, upper)));
    }
    pnlChart.setFixedAutoRanges(ranges);

    // Determine and set the relative offset required for basing the main axis off zero
    timeAxis.setBaseMillis((long) timeAxis.getLowerBound());
    // Enable overlay mode on the custom axis so relative formatting is used
    timeAxis.setMode(Mode.OVERLAY);

    // Enforce the old margins again
    for (ValueAxis a : axis) {
      a.setLowerMargin(lowerMargin);
      a.setUpperMargin(upperMargin);
    }
    // Set all axis bounds to fit their data
    pnlChart.restoreAutoBounds();
  }

  /**
   * Enable mouse drag pan and scroll zoom behaviour for the current chart panel.
   */
  public void enablePanMode() {
    setPanModifier(pnlChart, InputEvent.BUTTON1_MASK);
    useScrollZoom(pnlChart);
  }

  /**
   * Enable box zoom behaviour for the current chart panel.
   */
  public void enableBoxZoom() {
    setPanModifier(pnlChart, InputEvent.ALT_MASK | InputEvent.BUTTON1_MASK);
    useBoxZoom(pnlChart);
  }

  /**
   * Enable any settings that are consistent throughout all behaviours.
   * 
   * @param chartPanel Chart panel to set behaviour for
   */
  private void enableGlobalBehaviour() {
    // Enable panning
    plot.setRangePannable(true);
    plot.setDomainPannable(true);
    // Modify zoom scroll behaviour depending on held modifiers
    pnlChart.addMouseWheelListener(e -> {
      boolean range = (e.getModifiers() & InputEvent.ALT_MASK) == 0;
      pnlChart.setRangeZoomable(range);
      boolean domain =
          (e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) == 0;
      pnlChart.setDomainZoomable(domain);

      // Re-enable all once this event has been consumed
      SwingUtilities.invokeLater(() -> {
        pnlChart.setDomainZoomable(true);
        pnlChart.setRangeZoomable(true);
      });
    });
    // Enable plot reset on double mouse click
    pnlChart.addChartMouseListener(new ChartMouseListener() {

      @Override
      public void chartMouseMoved(ChartMouseEvent e) {}

      @Override
      public void chartMouseClicked(ChartMouseEvent e) {
        MouseEvent e1 = e.getTrigger();
        if ((e1.getModifiers() & InputEvent.BUTTON1_MASK) != 0 && e1.getClickCount() == 2) {
          pnlChart.restoreAutoBounds();
        }
      }
    });
    // Redraw legends manually for any events that could affect them
    dataset.addChangeListener(arg0 -> redrawLegend());
    renderer.addChangeListener(arg0 -> redrawLegend());
  }

  /**
   * For the given chart panel, set it up to use the pointer for zooming using a drag-box. This will
   * reverse effects of using scroll zoom due to incompatibilities between using both at the same
   * time.
   * 
   * @param chartPanel Chart panel to modify zoom behaviour for
   */
  private static void useBoxZoom(ChartPanel chartPanel) {
    chartPanel.setZoomTriggerDistance(10);
    chartPanel.setMouseZoomable(true);
    chartPanel.setMouseWheelEnabled(false);
    chartPanel.setZoomAroundAnchor(false);
  }

  /**
   * For the given chart panel, set it up to use the scroll wheel for zooming. This will reverse
   * effects of using box zoom due to incompatibilities between using both at the same time.
   * 
   * @param chartPanel Chart panel to modify zoom behaviour for
   */
  private static void useScrollZoom(ChartPanel chartPanel) {
    chartPanel.setZoomTriggerDistance(Integer.MAX_VALUE);
    chartPanel.setMouseZoomable(false);
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setZoomAroundAnchor(true);
  }

  /**
   * Make use of a reflection to set the private pan mask field of the JFreeChart library.
   * 
   * @param chartPanel Chart panel to modify mask for
   * @param modifier Modifier required for panning
   */
  private static void setPanModifier(ChartPanel chartPanel, Integer modifier) {
    try {
      Field mask = ChartPanel.class.getDeclaredField("panMask");
      mask.setAccessible(true);
      mask.setInt(chartPanel, (modifier == null ? 0 : modifier));
    } catch (Exception ex) {
      System.err.println("Unable to set pan modifier");
    }
  }

  /**
   * Using a new font scheme apply the given scaling to all textual components of the line graph
   * view.
   * 
   * @param scale Scale to apply
   */
  public void applyFontScale(double scale) {
    // Create a new theme
    StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
    // Scale theme's fonts
    Font fontXL = Accessibility.scaleFont(theme.getExtraLargeFont(), scale);
    Font fontL = Accessibility.scaleFont(theme.getLargeFont(), scale);
    Font fontR = Accessibility.scaleFont(theme.getRegularFont(), scale);
    Font fontS = Accessibility.scaleFont(theme.getSmallFont(), scale);
    // Update theme's fonts
    theme.setExtraLargeFont(fontXL);
    theme.setLargeFont(fontL);
    theme.setRegularFont(fontR);
    theme.setSmallFont(fontS);
    // Apply theme
    theme.apply(chart);
  }

  /**
   * Create a stroke that can be utilised by a chart's plot. It's thickness is defined as a scaling
   * between a minimum and maximum thickness.
   * 
   * @param scale Scale to use for thickness calculation
   * @return Stroke with calculated thickness
   */
  public static BasicStroke getLineStroke(int scale) {
    float dif = MAX_THICKNESS - MIN_THICKNESS;
    float thickness = MIN_THICKNESS + dif * scale / 100.0f;
    return new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  }

}
