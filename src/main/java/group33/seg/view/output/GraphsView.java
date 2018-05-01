package group33.seg.view.output;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.SettingsHandler;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener.Type;
import group33.seg.controller.utilities.GraphVisitor;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineGraphConfig;

public class GraphsView extends JPanel {
  private static final long serialVersionUID = 6541885932864334941L;
  
  public static Color DEFAULT_BACKGROUND = Color.getHSBColor(0, 0, (float) 0.9);
  
  private static final String NO_GRAPH_VIEW = "NO_GRAPH";
  private static final String LINE_GRAPH_VIEW = "LINE_GRAPH";
  private static final String HISTOGRAM_VIEW = "HISTOGRAM";

  private DashboardController controller;

  private CardLayout cardLayout;

  private LineGraphView lineGraph;
  private HistogramView histogram;


  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public GraphsView(DashboardController controller) {
    this.controller = controller;

    initGUI();

    // Update controllers knowledge of view
    controller.graphs.setGraphsView(this);
    // Update controllers knowledge of graph to update in view
    controller.graphs.setLineGraphView(lineGraph);
    controller.graphs.setHistogramView(histogram);

    // Load empty view
    loadView(null);
  }

  private void initGUI() {
    boolean useBuffer = controller.settings.prefs.getBoolean(SettingsHandler.BUFFERED_GRAPH, true);
    cardLayout = new CardLayout();
    setLayout(cardLayout);

    // No graph view
    JPanel pnlNoGraph = new JPanel();
    add(pnlNoGraph, NO_GRAPH_VIEW);
    GridBagLayout gbl_pnlNoGraph = new GridBagLayout();
    gbl_pnlNoGraph.columnWidths = new int[] {10, 0, 10};
    gbl_pnlNoGraph.rowHeights = new int[] {0, 0, 0};
    gbl_pnlNoGraph.columnWeights = new double[] {0.0, 1.0, 0.0};
    gbl_pnlNoGraph.rowWeights = new double[] {0.5, 0.0, 0.5};
    pnlNoGraph.setLayout(gbl_pnlNoGraph);

    String strNoGraph = "<html>There is no graph loaded.<br>"
        + "Create or load a graph using the 'Graph Manager'.</html>";
    JLabel lblNoGraph = new JLabel(strNoGraph);
    lblNoGraph.setEnabled(false);
    GridBagConstraints gbc_lblNoGraph = new GridBagConstraints();
    gbc_lblNoGraph.gridx = 1;
    gbc_lblNoGraph.gridy = 1;
    pnlNoGraph.add(lblNoGraph, gbc_lblNoGraph);

    // Create line graph view
    lineGraph = new LineGraphView(useBuffer);
    add(lineGraph, LINE_GRAPH_VIEW);

    // Create histogram
    histogram = new HistogramView();
    add(histogram, HISTOGRAM_VIEW);
  }

  /**
   * Load the view required for the given graph type. If a null graph is provided the view will
   * provide instructions on how to load a graph.
   * 
   * @param graph Graph type to load
   */
  public void loadView(GraphConfig graph) {
    if (graph == null) {
      cardLayout.show(this, NO_GRAPH_VIEW);
    } else {
      graph.accept(new GraphVisitor() {

        @Override
        public void visit(LineGraphConfig graph) {
          cardLayout.show(GraphsView.this, LINE_GRAPH_VIEW);
        }

        @Override
        public void visit(HistogramConfig graph) {
          cardLayout.show(GraphsView.this, HISTOGRAM_VIEW);
        }
      });
    }
  }
  
  /**
   * Get the gridline colour that a view instance would use for the given background colour.
   * 
   * @param bg Background colour
   * @return Gridline colour corresponding to background colour
   */
  public static Color getGridlineColor(Color bg) {
    float[] bgHSB = Color.RGBtoHSB(bg.getRed(), bg.getGreen(), bg.getBlue(), null);
    float bgBrightness = bgHSB[2];
    final float crossOver = (float) 0.5;
    final float fgMinBrightness = (float) 0.2;
    final float fgMaxBrightness = (float) 1.0;
    float fgBrightness;
    if (bgBrightness > crossOver) {
      fgBrightness = Math.min(fgMaxBrightness,
          fgMinBrightness + (float) Math.pow(crossOver - bgBrightness, 2));
    } else {
      fgBrightness = Math.max(fgMinBrightness,
          fgMaxBrightness - (float) Math.pow(crossOver + bgBrightness, 2));
    }
    return Color.getHSBColor(bgHSB[0], bgHSB[1], fgBrightness);
  }
  
}
