package group33.seg.view.output;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.view.utilities.CustomChartPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

public class HistogramView extends JPanel {
  
  public static Color DEFAULT_FOREGROUND = Color.getHSBColor(0.556f, 1, 0.9f);
  
  private CustomChartPanel pnlChart;

  private JFreeChart chart;
  
  public HistogramView() {
    initGUI();
  }

  private void initGUI() {
    setLayout(new BorderLayout(0, 0));

    // Chart panel
    ChartPanel pnlChart = new ChartPanel(null);
    this.add(pnlChart, BorderLayout.CENTER);
    
    // Chart controls
    JToolBar tlbControls = new JToolBar();
    tlbControls.setRollover(true);
    add(tlbControls, BorderLayout.SOUTH);

    JButton btnSave = new JButton();
    btnSave.setToolTipText("Export the currently displayed graph as an image.");
    btnSave.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
    tlbControls.add(btnSave);

    JButton btnPrint = new JButton();
    btnPrint.setToolTipText("Print the currently displayed graph.");
    btnPrint.setIcon(new ImageIcon(getClass().getResource("/icons/print.png")));
    tlbControls.add(btnPrint);

    tlbControls.addSeparator();

    // Display user save option
    btnSave.addActionListener(e -> {
//      try {
//        pnlChart.doSaveAs();
//      } catch (IOException e1) {
//        System.err.println("Unable to save image");
//      }
    });

    // Display user print option
    btnPrint.addActionListener(e -> {
      //pnlChart.createChartPrintJob();
    });
  }

  private static final long serialVersionUID = 794738588952920667L;

  public void applyFontScale(double scale) {
    // TODO Auto-generated method stub

  }

  public Object clearGraph() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object setGraphProperties(HistogramConfig graph) {
    // TODO Auto-generated method stub
    return null;
  }

}
