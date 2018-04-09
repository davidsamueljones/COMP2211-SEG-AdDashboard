package group33.seg.view.output;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.util.RelativeDateFormat;
import group33.seg.model.configs.LineGraphConfig.Mode;

public class CustomDateAxis extends DateAxis {
  private static final long serialVersionUID = 7564109459443901236L;

  private Mode mode = Mode.NORMAL;
  private long baseMillis = 0;

  /**
   * @param mode Mode for date axis to utilise
   */
  public void setMode(Mode mode) {
    this.mode = mode;
    fireChangeEvent();
  }

  @Override
  public List<?> refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea,
      RectangleEdge edge) {

    // Configure the override format in line with the mode
    RelativeDateFormat format;
    switch (mode) {
      case NORMAL:
        format = null;
        break;
      case OVERLAY:
        // Determine the correct tick unit to use now
        if (isAutoTickUnitSelection()) {
          selectAutoTickUnit(g2, dataArea, edge);
        }
        // Create the overlay format using this unit
        DateTickUnitType unit = getTickUnit().getUnitType();
        format = getRelativeFormat(unit);
        format.setBaseMillis(baseMillis);
        break;
      default:
        throw new IllegalStateException("Unsupported date axis mode");
    }

    // Apply format using reflection to avoid re-firing refresh using setter method or disabling
    // events (not ideal but blame restrictive APIs...)
    try {
      Field overrideFormat = DateAxis.class.getDeclaredField("dateFormatOverride");
      overrideFormat.setAccessible(true);
      overrideFormat.set(this, format);
    } catch (Exception e) {
      System.err.println("Unable to set date override format");
    }

    // Do normal tick behaviour
    return super.refreshTicks(g2, state, dataArea, edge);
  }

  /**
   * Update the base for the relative date format used whilst in overlay mode.
   * 
   * @param baseMillis Base to use (in ms)
   */
  public void setBaseMillis(long baseMillis) {
    this.baseMillis = baseMillis;
    if (mode == Mode.OVERLAY) {
      fireChangeEvent();
    }
  }

  /**
   * Create a relative date format that hides information if the axis does not believe it relevent
   * for the given unit type.
   * 
   * @param unit Unit to use for deciding output format
   * @return Relative date format appropriate for given unit
   */
  private static RelativeDateFormat getRelativeFormat(DateTickUnitType unit) {
    RelativeDateFormat format = new RelativeDateFormat();
    format.setShowZeroDays(true);
    if (unit == DateTickUnitType.YEAR || unit == DateTickUnitType.MONTH
        || unit == DateTickUnitType.DAY) {
      format.setHourFormatter(new HiddenNumberFormat());
      format.setMinuteFormatter(new HiddenNumberFormat());
      format.setSecondFormatter(new HiddenNumberFormat());
      format.setDaySuffix("d");
      format.setMinuteSuffix("");
      format.setSecondSuffix("");
      format.setHourSuffix("");
    } else if (unit == DateTickUnitType.HOUR) {
      format.setMinuteFormatter(new HiddenNumberFormat());
      format.setSecondFormatter(new HiddenNumberFormat());
      format.setDaySuffix("d ");
      format.setHourSuffix("h");
      format.setMinuteSuffix("");
      format.setSecondSuffix("");
    } else if (unit == DateTickUnitType.MINUTE) {
      format.setSecondFormatter(new HiddenNumberFormat());
      format.setDaySuffix("d ");
      format.setHourSuffix("h ");
      format.setMinuteSuffix("m");
      format.setSecondSuffix("");
    } else {
      format.setDaySuffix("d ");
      format.setHourSuffix("h ");
      format.setMinuteSuffix("m ");
      format.setSecondSuffix("s");
    }
    return format;
  }

}

/**
 * Helper class that hides any number input.
 */
class HiddenNumberFormat extends NumberFormat {
  private static final long serialVersionUID = 6570046205767568509L;

  @Override
  public Number parse(String source, ParsePosition parsePosition) {
    return null;
  }

  @Override
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
    return new StringBuffer();
  }

  @Override
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
    return new StringBuffer();
  }
};
