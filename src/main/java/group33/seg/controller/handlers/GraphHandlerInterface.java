package group33.seg.controller.handlers;

public interface GraphHandlerInterface<T> {
  public void reloadGraph();

  public void displayGraph(T config);

  public void clearGraph();
}
