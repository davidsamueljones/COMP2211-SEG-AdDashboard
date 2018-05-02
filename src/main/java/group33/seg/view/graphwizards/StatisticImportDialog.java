package group33.seg.view.graphwizards;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.StatisticConfig;

public class StatisticImportDialog extends JDialog {
  private static final long serialVersionUID = -5073712222751435008L;

  private DashboardController controller;

  private JList<StatisticConfig> lstStatistics;
  private DefaultListModel<StatisticConfig> mdl_lstStatistics;

  private StatisticConfig selected = null;

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public StatisticImportDialog(Window parent, DashboardController controller) {
    super(parent, "Statistic Import");

    this.controller = controller;

    // Initialise GUI
    initGUI();
    refreshStatisics();

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
    pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWeights = new double[] {0.5, 0.5};
    gbl_pnlContent.rowWeights = new double[] {1.0, 0.0};
    pnlContent.setLayout(gbl_pnlContent);
    setContentPane(pnlContent);

    mdl_lstStatistics = new DefaultListModel<>();
    lstStatistics = new JList<>(mdl_lstStatistics);
    lstStatistics.setCellRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 4349332453062368120L;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        Component comp =
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof StatisticConfig) {
          StatisticConfig config = (StatisticConfig) value;
          setText(config.identifier);
          setToolTipText(String.format("<html>%s</html>", config.inText()));
        }
        return comp;
      }

    });
    JScrollPane scrStatistics = new JScrollPane(lstStatistics);
    scrStatistics.setPreferredSize(new Dimension(0, 150));
    scrStatistics.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstStatistics = new GridBagConstraints();
    gbc_lstStatistics.insets = new Insets(5, 5, 5, 5);
    gbc_lstStatistics.fill = GridBagConstraints.BOTH;
    gbc_lstStatistics.gridwidth = 2;
    gbc_lstStatistics.gridx = 0;
    gbc_lstStatistics.gridy = 0;
    pnlContent.add(scrStatistics, gbc_lstStatistics);

    JButton btnCancel = new JButton("Cancel");
    GridBagConstraints gbc_btnCancel = new GridBagConstraints();
    gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnCancel.insets = new Insets(0, 10, 0, 3);
    gbc_btnCancel.gridx = 0;
    gbc_btnCancel.gridy = 1;
    pnlContent.add(btnCancel, gbc_btnCancel);

    JButton btnSelect = new JButton("Select");
    GridBagConstraints gbc_btnSelect = new GridBagConstraints();
    gbc_btnSelect.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnSelect.insets = new Insets(0, 3, 0, 10);
    gbc_btnSelect.gridx = 1;
    gbc_btnSelect.gridy = 1;
    pnlContent.add(btnSelect, gbc_btnSelect);

    // Hide panel
    btnCancel.addActionListener(e -> {
      setVisible(false);
      dispose();
    });

    // Set the selected
    btnSelect.addActionListener(e -> {
      selected = lstStatistics.getSelectedValue();
      if (selected == null) {
        JOptionPane.showMessageDialog(null, "No statistic selected", "Selection Error",
            JOptionPane.ERROR_MESSAGE);
        return;
      } else {
        setVisible(false);
        dispose();
      }
    });
  }

  /**
   * Fetch the workspace statistics, replacing those displayed in the list.
   */
  public void refreshStatisics() {
    mdl_lstStatistics.clear();
    List<StatisticConfig> workspaceStatistics = controller.workspace.getStatistics();
    if (workspaceStatistics != null) {
      for (StatisticConfig statistic : workspaceStatistics) {
        mdl_lstStatistics.addElement(statistic);
      }
    }
  }

  /**
   * @return The selected statistic
   */
  public StatisticConfig getStatistic() {
    return selected;
  }

}
