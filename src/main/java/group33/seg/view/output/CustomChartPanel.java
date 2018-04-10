package group33.seg.view.output;

import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;
import group33.seg.model.types.Pair;

/**
 * Custom implementation of JFreeChart's chartpanel that can autorange to fixed values when
 * required.
 */
public class CustomChartPanel extends ChartPanel {
  private static final long serialVersionUID = -8183602850437017483L;

  /**
   * Requires use of list as two ValueAxis objects with equal properties are defined as equal by the
   * JFreeChart API. As they are a list and not 1-1 mappings, axis may be set multiple times in a
   * single auto-range, the range latest in the list will be the one applied.
   */
  private List<Pair<ValueAxis, Range>> fixedAutoRanges = null;

  /**
   * Constructs a JFreeChart panel.
   *
   * @param chart The chart.
   * @param width The preferred width of the panel.
   * @param height The preferred height of the panel.
   * @param minimumDrawWidth The minimum drawing width.
   * @param minimumDrawHeight The minimum drawing height.
   * @param maximumDrawWidth The maximum drawing width.
   * @param maximumDrawHeight The maximum drawing height.
   * @param useBuffer A flag that indicates whether to use the off-screen buffer to improve
   *        performance (at the expense of memory).
   * @param properties A flag indicating whether or not the chart property editor should be
   *        available via the popup menu.
   * @param copy A flag indicating whether or not a copy option should be available via the popup
   *        menu.
   * @param save A flag indicating whether or not save options should be available via the popup
   *        menu.
   * @param print A flag indicating whether or not the print option should be available via the
   *        popup menu.
   * @param zoom A flag indicating whether or not zoom options should be added to the popup menu.
   * @param tooltips A flag indicating whether or not tooltips should be enabled for the chart.
   */
  public CustomChartPanel(JFreeChart chart, int width, int height, int minimumDrawWidth,
      int minimumDrawHeight, int maximumDrawWidth, int maximumDrawHeight, boolean useBuffer,
      boolean properties, boolean copy, boolean save, boolean print, boolean zoom,
      boolean tooltips) {
    super(chart, width, height, minimumDrawWidth, minimumDrawHeight, maximumDrawWidth,
        maximumDrawHeight, useBuffer, properties, copy, save, print, zoom, tooltips);
  }

  /**
   * @return The current fixed ranges, null if not set
   */
  public List<Pair<ValueAxis, Range>> getFixedAutoRanges() {
    return fixedAutoRanges;
  }

  /**
   * @param fixedAutoRanges Axis paired with ranges for which to set their bounds
   */
  public void setFixedAutoRanges(List<Pair<ValueAxis, Range>> fixedAutoRanges) {
    this.fixedAutoRanges = fixedAutoRanges;
    restoreAutoBounds();
  }

  /**
   * Restores the auto-range calculation on both axis and if the fixed auto ranges are set, set the
   * specific axis bounds to the fixed ranges (make use of margins like autorange would).
   */
  @Override
  public void restoreAutoBounds() {
    super.restoreAutoBounds();
    if (fixedAutoRanges != null) {
      for (Pair<ValueAxis, Range> pair : fixedAutoRanges) {
        pair.key.setRangeWithMargins(pair.value);
      }
    }
  }

}
