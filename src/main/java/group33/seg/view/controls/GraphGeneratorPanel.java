package group33.seg.view.controls;

import javax.swing.JPanel;
import javax.swing.JTextField;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.GraphHandler;
import group33.seg.model.types.Interval;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class GraphGeneratorPanel extends JPanel {
  private DashboardController controller;;
  private JButton btnGenerateImpressionGraph;
  private JButton btnClearGraph;

  /**
   * Create the panel.
   */
  public GraphGeneratorPanel(DashboardController controller) {
    this.controller = controller;
    initGUI();
  }


  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0};
    gridBagLayout.rowHeights = new int[] {0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);

    JLabel lblInterval = new JLabel("Interval:");
    GridBagConstraints gbc_lblInterval = new GridBagConstraints();
    gbc_lblInterval.insets = new Insets(0, 0, 5, 5);
    gbc_lblInterval.anchor = GridBagConstraints.EAST;
    gbc_lblInterval.gridx = 0;
    gbc_lblInterval.gridy = 0;
    add(lblInterval, gbc_lblInterval);

    JComboBox<Interval> cboInterval = new JComboBox<>();
    GridBagConstraints gbc_cboInterval = new GridBagConstraints();
    gbc_cboInterval.insets = new Insets(0, 0, 5, 0);
    gbc_cboInterval.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboInterval.gridx = 1;
    gbc_cboInterval.gridy = 0;
    add(cboInterval, gbc_cboInterval);

    for (Interval interval : Interval.values()) {
      cboInterval.addItem(interval);
    }

    btnGenerateImpressionGraph = new JButton("Generate Impression Graph");
    GridBagConstraints gbc_btnGenerateImpressionGraph = new GridBagConstraints();
    gbc_btnGenerateImpressionGraph.insets = new Insets(0, 0, 5, 0);
    gbc_btnGenerateImpressionGraph.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnGenerateImpressionGraph.gridwidth = 2;
    gbc_btnGenerateImpressionGraph.gridx = 0;
    gbc_btnGenerateImpressionGraph.gridy = 1;
    add(btnGenerateImpressionGraph, gbc_btnGenerateImpressionGraph);

    btnGenerateImpressionGraph.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.graphs.generateImpressionGraph((Interval) cboInterval.getSelectedItem());
      }
    });

    btnClearGraph = new JButton("Clear Graph");
    btnClearGraph.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.graphs.clearGraph();
      }
    });
    GridBagConstraints gbc_btnClearGraph = new GridBagConstraints();
    gbc_btnClearGraph.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnClearGraph.gridwidth = 2;
    gbc_btnClearGraph.gridx = 0;
    gbc_btnClearGraph.gridy = 2;
    add(btnClearGraph, gbc_btnClearGraph);
  }

}
