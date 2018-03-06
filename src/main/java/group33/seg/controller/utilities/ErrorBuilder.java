package group33.seg.controller.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ErrorBuilder used for keeping track of complex validation where multiple errors have
 * occurred that could not easily be kept track of by a single string. The use of a list allows for
 * simple comment tracking in time that can be interpreted as required.
 */
public class ErrorBuilder {
  /** List of comments held */
  private final ArrayList<String> comments = new ArrayList<>();
  /** Whether an error has occurred */
  private boolean error = false;

  /**
   * Indicate an error without adding a comment.
   */
  public void addError() {
    error = true;
  }

  /**
   * Adds a comment to an array list that is defined as an error.
   *
   * @param newError Error to add
   */
  public void addError(String newError) {
    addComment(newError, true);
  }

  /**
   * Add a comment to an array list. This does not have to be an error.
   *
   * @param newComment Comment to add
   * @param causeError Whether comment is an error
   */
  public void addComment(String newComment, Boolean causeError) {
    comments.add(newComment);
    if (causeError) {
      error = true;
    }
  }

  /**
   * @return List of comments held
   */
  public List<String> getComments() {
    return comments;
  }

  /**
   * Lists comments as points preceeded by a title.
   *
   * @param title Title of list
   * @return Title string of held list
   */
  public String listComments(String title) {
    final StringBuilder sb = new StringBuilder();
    sb.append(title).append(":");
    for (final String comment : comments) {
      sb.append("\r\n- ").append(comment);
    }
    return sb.toString();
  }

  /**
   * @return Whether error builder contains an error
   */
  public boolean isError() {
    return error;
  }

  /**
   * Append another error builder to this instance of error builder. Appending adds all comments
   * from other error builder to end of this error builder. If an error exists in other, copy error.
   *
   * @param other Other error builder, ignored if null
   */
  public void append(ErrorBuilder other) {
    if (other != null) {
      // Copy comments
      for (final String comment : other.getComments()) {
        comments.add(comment);
      }
      // Check if error has occurred
      if (!error && other.isError()) {
        addError();
      }
    }
  }

}
