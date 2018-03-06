package group33.seg.view.increment1;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import group33.seg.controller.events.GraphHandler;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GraphPanel extends JPanel {
  private static final long serialVersionUID = 6541885932864334941L;

  private GraphHandler graphHandler;
  private Graph graph;
  
  private JButton btnPointer;
  private JButton btnPan;
  private JButton btnZoom;

  /**
   * Create the panel.
   */
  public GraphPanel() {

    initGUI();
  }

  private void initGUI() {
    setLayout(new BorderLayout(0, 0));

    graph = new Graph("Number of Impressions", "Time", "Number of impressions");
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
  }
  
  public void setGraphHandler(GraphHandler graphHandler) {
    this.graphHandler = graphHandler;
    graphHandler.setGraph(graph);
  }


}
