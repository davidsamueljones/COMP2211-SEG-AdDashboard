package group33.seg.controller.utilities;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class DashboardUtilities {

  /**
   * Count the number of lines in a file.
   *
   * @param path File to do line count for
   * @return Number of lines
   */
  public static Integer countFileLines(String path) {
    Integer lineCount = 0;
    LineNumberReader reader = null;

    try {
      reader = new LineNumberReader(new FileReader(path));
      while (reader.readLine() != null) {
        lineCount++;
      }
    } catch (IOException e) {
      lineCount = null;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
    return lineCount;
  }

  /**
   * Invoke a focus request later so that request is queued until all previous events have occurred.
   *
   * @param component Component to request focus
   */
  public static void focusRequest(Component component) {
    EventQueue.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            component.requestFocusInWindow();
          }
        });
  }
}
