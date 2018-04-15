package group33.seg.view.structure;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.SettingsHandler;
import group33.seg.view.output.LineGraphView;

public class GraphPanel extends JPanel {
  private static final long serialVersionUID = 6541885932864334941L;

  private DashboardController controller;

  private LineGraphView graph;


  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public GraphPanel(DashboardController controller) {
    this.controller = controller;

    initGUI();

    // Update controllers knowledge of graph to update in view
    controller.graphs.setLineGraphView(graph);
  }

  private void initGUI() {
    setLayout(new BorderLayout(0, 0));

    boolean useBuffer = controller.settings.prefs.getBoolean(SettingsHandler.BUFFERED_GRAPH, true);
    graph = new LineGraphView(useBuffer);
    add(graph, BorderLayout.CENTER);

  }

}
