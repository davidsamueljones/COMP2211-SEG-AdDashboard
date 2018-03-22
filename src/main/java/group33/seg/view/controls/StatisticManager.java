package group33.seg.view.controls;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.GraphHandler;
import group33.seg.controller.handlers.StatisticHandler;
import group33.seg.model.configs.StatisticConfig;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class StatisticManager extends JPanel {
  private static final long serialVersionUID = -4131564432533932331L;

  private DashboardController controller;

  private JButton btnNewStatistic;

  /**
   * Create the panel.
   */
  public StatisticManager(DashboardController controller) {
    this.controller = controller;
    initGUI();
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0};
    setLayout(gridBagLayout);

    btnNewStatistic = new JButton("New Statistic");
    GridBagConstraints gbc_btnNewStatistic = new GridBagConstraints();
    gbc_btnNewStatistic.gridwidth = 2;
    gbc_btnNewStatistic.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNewStatistic.insets = new Insets(0, 0, 5, 0);
    gbc_btnNewStatistic.gridx = 0;
    gbc_btnNewStatistic.gridy = 0;
    add(btnNewStatistic, gbc_btnNewStatistic);

    JLabel lblStatisticSelector = new JLabel("Statistic Select:");
    GridBagConstraints gbc_lblStatisticSelector = new GridBagConstraints();
    gbc_lblStatisticSelector.insets = new Insets(0, 0, 5, 5);
    gbc_lblStatisticSelector.anchor = GridBagConstraints.EAST;
    gbc_lblStatisticSelector.gridx = 0;
    gbc_lblStatisticSelector.gridy = 1;
    add(lblStatisticSelector, gbc_lblStatisticSelector);

    // TODO: Chose type for JComboBox (String or Statistic)
    JComboBox<StatisticConfig> cboStatisticSelector = new JComboBox<>();
    GridBagConstraints gbc_cboStatisticSelector = new GridBagConstraints();
    gbc_cboStatisticSelector.insets = new Insets(0, 0, 5, 0);
    gbc_cboStatisticSelector.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboStatisticSelector.gridx = 1;
    gbc_cboStatisticSelector.gridy = 1;
    add(cboStatisticSelector, gbc_cboStatisticSelector);

    JPanel pnlStatistics = new JPanel();
    CardLayout cl_pnlStatistics = new CardLayout();
    pnlStatistics.setLayout(cl_pnlStatistics);
    pnlStatistics.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Selected Statistic", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    GridBagConstraints gbc_pnlStatistics = new GridBagConstraints();
    gbc_pnlStatistics.gridwidth = 2;
    gbc_pnlStatistics.fill = GridBagConstraints.BOTH;
    gbc_pnlStatistics.gridx = 0;
    gbc_pnlStatistics.gridy = 2;
    add(pnlStatistics, gbc_pnlStatistics);

    // No statistic selected cards
    JLabel lblNoneSelected = new JLabel("No Statistic Selected");
    lblNoneSelected.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    lblNoneSelected.setEnabled(false);
    pnlStatistics.add(lblNoneSelected);
    cl_pnlStatistics.addLayoutComponent(lblNoneSelected, "NO_STATISTIC_SELECTED");
    // Set panel to show that no statistic selected
    cl_pnlStatistics.show(pnlStatistics, "NO_STATISTIC_SELECTED");

    // FIXME: Remove testcode
//    StatisticPanel pnlStatistic = new StatisticPanel();
//    pnlStatistic.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//    // Choose height of JList
//    Dimension d = pnlStatistic.getPreferredSize();
//    d.height += 50;
//    pnlStatistic.setPreferredSize(d);
//    pnlStatistics.add(pnlStatistic);
//    cl_pnlStatistics.addLayoutComponent(pnlStatistic, "TEST");
//    cl_pnlStatistics.show(pnlStatistics, "TEST");
  }

}