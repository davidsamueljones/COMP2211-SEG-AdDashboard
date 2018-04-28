package group33.seg.controller.handlers;

public interface GraphHandlerInterface<T> {
  void reloadGraph();

  void displayGraph(T config);

  void clearGraph();
}
