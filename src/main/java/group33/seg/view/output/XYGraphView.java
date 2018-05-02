package group33.seg.view.output;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.XYPlot;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.CustomChartPanel;

public abstract class XYGraphView extends JPanel {
  private static final long serialVersionUID = -1616084431265689374L;

  protected CustomChartPanel pnlChart;

  protected JFreeChart chart;
  protected XYPlot plot;

  /**
   * Fully configure an empty chart and its controls.
   */
  public XYGraphView(boolean useBuffer) {
    initChart();
    initGUI(useBuffer);
    initControlScheme();
  }

  /**
   * Initialise the GUI and any event listeners.
   */
  protected void initGUI(boolean useBuffer) {
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
  protected abstract void initChart();

  /**
   * Initialise the default control scheme of the chart.
   */
  protected void initControlScheme() {
    enableGlobalBehaviour();
    // Enable zoom using scroll as default
    enablePanMode();
  }


  /**
   * Enable mouse drag pan and scroll zoom behaviour for the current chart panel.
   */
  protected void enablePanMode() {
    setPanModifier(pnlChart, InputEvent.BUTTON1_MASK);
    useScrollZoom(pnlChart);
  }

  /**
   * Enable box zoom behaviour for the current chart panel.
   */
  protected void enableBoxZoom() {
    setPanModifier(pnlChart, InputEvent.ALT_MASK | InputEvent.BUTTON1_MASK);
    useBoxZoom(pnlChart);
  }

  /**
   * Enable any settings that are consistent throughout all behaviours.
   */
  protected void enableGlobalBehaviour() {
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
  }

  /**
   * For the given chart panel, set it up to use the pointer for zooming using a drag-box. This will
   * reverse effects of using scroll zoom due to incompatibilities between using both at the same
   * time.
   * 
   * @param chartPanel Chart panel to modify zoom behaviour for
   */
  protected static void useBoxZoom(ChartPanel chartPanel) {
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
  protected static void useScrollZoom(ChartPanel chartPanel) {
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
  protected static void setPanModifier(ChartPanel chartPanel, Integer modifier) {
    try {
      Field mask = ChartPanel.class.getDeclaredField("panMask");
      mask.setAccessible(true);
      mask.setInt(chartPanel, (modifier == null ? 0 : modifier));
    } catch (Exception ex) {
      System.err.println("Unable to set pan modifier");
    }
  }

  /**
   * Using a new font scheme apply the given scaling to all textual components of the histogram
   * graph view.
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

}
