package group33.seg.view.graphwizards;

import group33.seg.model.configs.GraphConfig;

public interface GraphWizardInterface<T extends GraphConfig> {

  /**
   * Load the given graph configuration into the graph wizard. After a load it should be treated as
   * the base graph so any future changes should be treated as updates/increments of a previous
   * load.
   * 
   * @param graph Configuration to load
   */
  public void loadGraph(T graph);

  /**
   * Return the currently loaded graph without any non-applied changes. This should be the graph
   * modified by the wizard implementation.
   * 
   * @return The currently loaded graph
   */
  public T getGraph();

}
