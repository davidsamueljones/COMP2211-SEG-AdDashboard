package group33.seg.view.structure;

import group33.seg.controller.DashboardController;
import group33.seg.view.output.LineGraphView;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {
  private static final long serialVersionUID = 6541885932864334941L;

  private DashboardController controller;
  
  private LineGraphView graph;

  private JButton btnPointer;
  private JButton btnPan;
  private JButton btnZoom;

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

    graph = new LineGraphView("Number of Impressions", "Time", "Number of impressions");
    add(graph, BorderLayout.CENTER);

    JToolBar tlbControls = new JToolBar();
    tlbControls.setRollover(true);
    add(tlbControls, BorderLayout.SOUTH);

    btnPointer = new JButton("Pointer");
    tlbControls.add(btnPointer);

    btnPan = new JButton("Pan");
    tlbControls.add(btnPan);

    btnZoom = new JButton("Zoom");
    tlbControls.add(btnZoom);

    tlbControls.addSeparator();

    // FIXME: Hidden controls until functionality implemented
    tlbControls.setVisible(false);
  }

}
