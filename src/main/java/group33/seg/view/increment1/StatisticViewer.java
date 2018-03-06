package group33.seg.view.increment1;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import group33.seg.controller.events.GraphHandler;
import group33.seg.controller.events.StatisticHandler;

public class StatisticViewer extends JPanel {
  private StatisticHandler statisticHandler;

  private JButton btnGenerateImpressions;
  private JTextField txtTotalImpressions;

  /**
   * Create the panel.
   */
  public StatisticViewer() {

    initGUI();
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0};
    gridBagLayout.rowHeights = new int[] {0, 0, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);

    btnGenerateImpressions = new JButton("Generate Impression Total");
    GridBagConstraints gbc_btnGenerateImpressions = new GridBagConstraints();
    gbc_btnGenerateImpressions.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnGenerateImpressions.gridwidth = 2;
    gbc_btnGenerateImpressions.insets = new Insets(0, 0, 5, 0);
    gbc_btnGenerateImpressions.gridx = 0;
    gbc_btnGenerateImpressions.gridy = 0;
    add(btnGenerateImpressions, gbc_btnGenerateImpressions);

    JLabel lblTotalImpressions = new JLabel("Total Impressions:");
    GridBagConstraints gbc_lblTotalImpressions = new GridBagConstraints();
    gbc_lblTotalImpressions.insets = new Insets(0, 0, 0, 5);
    gbc_lblTotalImpressions.anchor = GridBagConstraints.EAST;
    gbc_lblTotalImpressions.gridx = 0;
    gbc_lblTotalImpressions.gridy = 1;
    add(lblTotalImpressions, gbc_lblTotalImpressions);

    txtTotalImpressions = new JTextField();
    txtTotalImpressions.setEnabled(false);
    GridBagConstraints gbc_txtTotalImpressions = new GridBagConstraints();
    gbc_txtTotalImpressions.anchor = GridBagConstraints.NORTH;
    gbc_txtTotalImpressions.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtTotalImpressions.gridx = 1;
    gbc_txtTotalImpressions.gridy = 1;
    add(txtTotalImpressions, gbc_txtTotalImpressions);
    txtTotalImpressions.setColumns(10);

    btnGenerateImpressions.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        txtTotalImpressions.setText(String.valueOf(statisticHandler.impressionRequest()));
      }
    });
  }

  public void setStatisticHandler(StatisticHandler statisticHandler) {
    this.statisticHandler = statisticHandler;
  }

}
