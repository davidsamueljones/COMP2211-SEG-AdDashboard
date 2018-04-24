package group33.seg.controller.utilities;

public interface ProgressListener {

  default void start() {}

  default void progressUpdate(int progress) {}
  
  default void progressUpdate(String update) {}

  default void finish(boolean success) {}

  default void cancelled() {}
}
