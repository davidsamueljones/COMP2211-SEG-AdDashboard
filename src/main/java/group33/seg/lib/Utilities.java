package group33.seg.lib;
import org.apache.commons.text.WordUtils;

public class Utilities {
  
  /**
   * Split string into words using any underscores and capitalise the first letter of each word to
   * make title case. TODO: Move generic utility somewhere more appropriate
   * 
   * @param string String to convert to sentence case
   * @return Formatted string
   */
  public static String getTitleCase(String string) {
    string = string.replace("_", " ");
    string = string.toLowerCase();
    return WordUtils.capitalize(string);
  }
  
}
