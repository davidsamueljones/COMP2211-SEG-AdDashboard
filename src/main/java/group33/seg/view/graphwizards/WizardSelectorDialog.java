package group33.seg.view.graphwizards;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.model.configs.LineGraphConfig;

public class WizardSelectorDialog extends JDialog {
  private static final long serialVersionUID = -7965948960906234126L;
  private GraphConfig selected = null;

  public WizardSelectorDialog(Window parent) {
    super(parent, "Graph Type Selector");
    // Initialise GUI
    initGUI();

    // Determine sizing
    pack();
    // Determine positioning
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  private void initGUI() {
    JPanel pnlContent = new JPanel();
    pnlContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setContentPane(pnlContent);
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWeights = new double[] {0.33, 0.33};
    gbl_pnlContent.rowWeights = new double[] {0.0, 0.0};
    pnlContent.setLayout(gbl_pnlContent);

    JLabel lblHelp = new JLabel("Which type of graph would you like to create?");
    GridBagConstraints gbc_lblHelp = new GridBagConstraints();
    gbc_lblHelp.gridwidth = 2;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 0;
    pnlContent.add(lblHelp, gbc_lblHelp);

    JButton btnLineGraph = new JButton("Line Graph");
    GridBagConstraints gbc_btnLineGraph = new GridBagConstraints();
    gbc_btnLineGraph.anchor = GridBagConstraints.EAST;
    gbc_btnLineGraph.insets = new Insets(0, 0, 0, 3);
    gbc_btnLineGraph.gridx = 0;
    gbc_btnLineGraph.gridy = 1;
    pnlContent.add(btnLineGraph, gbc_btnLineGraph);

    JButton btnCPMHistogram = new JButton("CPM Histogram");
    GridBagConstraints gbc_btnCPMHistogram = new GridBagConstraints();
    gbc_btnCPMHistogram.insets = new Insets(0, 0, 0, 3);
    gbc_btnCPMHistogram.anchor = GridBagConstraints.WEST;
    gbc_btnCPMHistogram.gridx = 1;
    gbc_btnCPMHistogram.gridy = 1;
    pnlContent.add(btnCPMHistogram, gbc_btnCPMHistogram);

    // Select line graph type
    btnLineGraph.addActionListener(e -> {
      selected = new LineGraphConfig();
      setVisible(false);
      dispose();
    });
    // Select CPM histogram type
    btnCPMHistogram.addActionListener(e -> {
      selected = new HistogramConfig();
      setVisible(false);
      dispose();
    });
  }

  public GraphConfig getSelectedType() {
    return selected;
  }

}
