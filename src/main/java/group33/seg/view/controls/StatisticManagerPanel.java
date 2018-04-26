package group33.seg.view.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.view.statisticwizard.StatisticWizardDialog;

public class StatisticManagerPanel extends JPanel {
  private static final long serialVersionUID = 2590651419460169868L;

  private DashboardController controller;

  private JList<StatisticConfig> lstStatistics;
  private DefaultListModel<StatisticConfig> mdl_lstStatistics;

  /**
   * Create the panel, loading the workspace's current statistics.
   * 
   * @param controller Controller for this view object
   */
  public StatisticManagerPanel(DashboardController controller) {
    this.controller = controller;

    // Initialise the GUI
    initGUI();
    // Load the current workspace's statistics
    refreshStatisics();
  }

  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0};
    setLayout(gridBagLayout);

    JButton btnNew = new JButton("New Statistic");
    GridBagConstraints gbc_btnNew = new GridBagConstraints();
    gbc_btnNew.insets = new Insets(0, 0, 5, 0);
    gbc_btnNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNew.gridx = 0;
    gbc_btnNew.gridy = 0;
    add(btnNew, gbc_btnNew);

    JPanel pnlExisting = new JPanel();
    pnlExisting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
        "Workspace Statistics", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    GridBagConstraints gbc_pnlExisting = new GridBagConstraints();
    gbc_pnlExisting.fill = GridBagConstraints.BOTH;
    gbc_pnlExisting.gridx = 0;
    gbc_pnlExisting.gridy = 1;
    add(pnlExisting, gbc_pnlExisting);
    GridBagLayout gbl_pnlExisting = new GridBagLayout();
    gbl_pnlExisting.columnWeights = new double[] {1.0, 1.0};
    gbl_pnlExisting.rowWeights = new double[] {1.0, 0.0};
    pnlExisting.setLayout(gbl_pnlExisting);

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
          ErrorBuilder eb = config.validate();
          if (eb.isError()) {
            setBackground(new Color(250, 128, 114));
          }
          setText(config.identifier);
          setToolTipText(String.format("<html>%s</html>", config.inText()));
        }
        return comp;
      }
    });
    JScrollPane scrStatistics = new JScrollPane(lstStatistics);
    scrStatistics.setPreferredSize(new Dimension(0, 75));
    scrStatistics.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstStatistics = new GridBagConstraints();
    gbc_lstStatistics.gridwidth = 2;
    gbc_lstStatistics.insets = new Insets(5, 5, 5, 5);
    gbc_lstStatistics.fill = GridBagConstraints.BOTH;
    gbc_lstStatistics.gridx = 0;
    gbc_lstStatistics.gridy = 0;
    pnlExisting.add(scrStatistics, gbc_lstStatistics);

    JButton btnDelete = new JButton("Delete");
    btnDelete.setEnabled(false);
    GridBagConstraints gbc_btnDelete = new GridBagConstraints();
    gbc_btnDelete.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnDelete.insets = new Insets(0, 5, 5, 2);
    gbc_btnDelete.gridx = 0;
    gbc_btnDelete.gridy = 1;
    pnlExisting.add(btnDelete, gbc_btnDelete);

    JButton btnViewModify = new JButton("View/Modify");
    btnViewModify.setEnabled(false);
    GridBagConstraints gbc_btnViewModify = new GridBagConstraints();
    gbc_btnViewModify.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnViewModify.insets = new Insets(0, 2, 5, 5);
    gbc_btnViewModify.gridx = 1;
    gbc_btnViewModify.gridy = 1;
    pnlExisting.add(btnViewModify, gbc_btnViewModify);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Allow a new statistic to be created using a fresh wizard
    btnNew.addActionListener(e -> displayWizard(null));

    // Listen for changes in workspace statistics, updating list if required
    controller.workspace.addListener(type -> {
      if (type == WorkspaceListener.Type.WORKSPACE || type == WorkspaceListener.Type.STATISTICS) {
        SwingUtilities.invokeLater(() -> refreshStatisics());
      }
    });

    // Disable buttons when selection invalidates them
    lstStatistics.addListSelectionListener(e -> {
      boolean isSelection = lstStatistics.getSelectedIndex() != -1;
      btnDelete.setEnabled(isSelection);
      btnViewModify.setEnabled(isSelection);
    });
    lstStatistics.setSelectedIndex(-1);

    // Delete the selected statistic
    btnDelete.addActionListener(e -> {
      int res = JOptionPane.showConfirmDialog(StatisticManagerPanel.this,
          "Are you sure you want to delete the selected statistic?", "Delete Statistic",
          JOptionPane.YES_NO_OPTION);
      if (res != JOptionPane.YES_OPTION) {
        return;
      }
      controller.workspace.removeStatistic(lstStatistics.getSelectedValue());
    });

    // Open wizard for selected statistics
    btnViewModify.addActionListener(e -> displayWizard(lstStatistics.getSelectedValue()));

  }

  /**
   * Open the wizard for the given statistic.
   * 
   * @param statisticConfig Configuration to display in wizard
   */
  public void displayWizard(StatisticConfig config) {   
    Window frmCurrent = SwingUtilities.getWindowAncestor(StatisticManagerPanel.this);
    StatisticWizardDialog wizard = new StatisticWizardDialog(frmCurrent, controller, config);
    wizard.setModalityType(ModalityType.APPLICATION_MODAL);
    wizard.setVisible(true);
    // Select new statistic on wizard close
    StatisticConfig newConfig = wizard.getStatistic();
    if (newConfig != null) {
      SwingUtilities.invokeLater(() -> lstStatistics.setSelectedValue(newConfig, true));
    }
  }

  /**
   * Refresh the statistics, using the currently selected statistic as the refresh object to select.
   */
  public void refreshStatisics() {
    refreshStatisics(lstStatistics.getSelectedValue());
  }

  /**
   * Fetch the workspace statistics, replacing those displayed in the list. If the given statistic
   * exists in the list it is selected.
   * 
   * @param selected Statistic to select
   */
  public void refreshStatisics(StatisticConfig selected) {
    mdl_lstStatistics.clear();
    List<StatisticConfig> workspaceStatistics = controller.workspace.getStatistics();
    if (workspaceStatistics != null) {
      for (StatisticConfig statistic : workspaceStatistics) {
        mdl_lstStatistics.addElement(statistic);
      }
      lstStatistics.setSelectedValue(selected, true);
    } else {
      lstStatistics.setSelectedValue(null, true);
    }
  }

}
