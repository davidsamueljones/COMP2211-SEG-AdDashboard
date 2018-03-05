package group33.seg.controller.utilities;

public interface ProgressListener {
  
  public default void start() {};

  public default void progressUpdate(int progress) {};

  public default void finish(boolean success) {};

  public default void cancelled() {};
  
}