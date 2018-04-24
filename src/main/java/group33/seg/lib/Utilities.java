package group33.seg.lib;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import org.apache.commons.text.WordUtils;

public class Utilities {

  /**
   * Split string into words using any underscores and capitalise the first letter of each word to
   * make title case.
   * 
   * @param string String to convert to sentence case
   * @return Formatted string
   */
  public static String getTitleCase(String string) {
    string = string.replace("_", " ");
    string = string.toLowerCase();
    return WordUtils.capitalize(string);
  }

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
    EventQueue.invokeLater(() -> component.requestFocusInWindow());
  }

}
