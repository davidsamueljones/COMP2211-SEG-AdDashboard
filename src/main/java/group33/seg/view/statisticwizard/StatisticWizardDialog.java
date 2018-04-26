package group33.seg.view.statisticwizard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.StatisticHandler;
import group33.seg.controller.handlers.StatisticHandler.Update;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.model.configs.StatisticConfig;

public class StatisticWizardDialog extends JDialog {
  private static final long serialVersionUID = 5283386849072274016L;

  private DashboardController controller;

  private StatisticPanel pnlStatistic;

  /** Last loaded (or updated) statistic */
  private StatisticConfig base;

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public StatisticWizardDialog(Window parent, DashboardController controller) {
    this(parent, controller, null);
  }

  /**
   * Create the dialog, loading an initial configuration.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   * @param statistic Configuration to load into view
   */

  public StatisticWizardDialog(Window parent, DashboardController controller,
      StatisticConfig statistic) {
    super(parent, "Statistic Wizard");

    this.controller = controller;
    // Initialise GUI
    initGUI();
    loadStatistic(statistic);

    // Determine positioning
    setSize(new Dimension(400, 400));
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
  }


  private void initGUI() {
    JPanel pnlContent = new JPanel();
    pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setContentPane(pnlContent);
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWeights = new double[] {1.0, 0.0, 0.0};
    gbl_pnlContent.rowWeights = new double[] {1.0, 0.0};
    pnlContent.setLayout(gbl_pnlContent);

    pnlStatistic = new StatisticPanel(controller);
    pnlStatistic.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    JScrollPane scrStatistic = new JScrollPane(pnlStatistic);
    scrStatistic.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_pnlStatistic = new GridBagConstraints();
    gbc_pnlStatistic.gridwidth = 3;
    gbc_pnlStatistic.insets = new Insets(0, 0, 5, 0);
    gbc_pnlStatistic.fill = GridBagConstraints.BOTH;
    gbc_pnlStatistic.gridx = 0;
    gbc_pnlStatistic.gridy = 0;
    pnlContent.add(scrStatistic, gbc_pnlStatistic);

    JButton btnClose = new JButton("Close");
    GridBagConstraints gbc_btnClose = new GridBagConstraints();
    gbc_btnClose.anchor = GridBagConstraints.EAST;
    gbc_btnClose.insets = new Insets(0, 0, 0, 5);
    gbc_btnClose.gridx = 0;
    gbc_btnClose.gridy = 1;
    pnlContent.add(btnClose, gbc_btnClose);

    JButton btnApply = new JButton("Apply");
    GridBagConstraints gbc_btnApply = new GridBagConstraints();
    gbc_btnApply.insets = new Insets(0, 0, 0, 5);
    gbc_btnApply.gridx = 1;
    gbc_btnApply.gridy = 1;
    pnlContent.add(btnApply, gbc_btnApply);

    JButton btnApplyClose = new JButton("Apply & Close");
    GridBagConstraints gbc_btnApplyClose = new GridBagConstraints();
    gbc_btnApplyClose.gridx = 2;
    gbc_btnApplyClose.gridy = 1;
    pnlContent.add(btnApplyClose, gbc_btnApplyClose);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Close the wizard, do not do any updates
    btnClose.addActionListener(e -> {
      handleClose();
    });

    // Handle apply behaviour, no other effects
    btnApply.addActionListener(e -> apply());

    // Handle apply behaviour and then close the wizard
    btnApplyClose.addActionListener(e -> {
      boolean success = apply();
      if (success) {
        setVisible(false);
        dispose();
      }
    });

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        handleClose();
      }
    });
  }

  /**
   * Handle the close, checking if close was desired.
   */
  private void handleClose() {
    if (base == null
        || StatisticHandler.getStatisticUpdate(base, makeStatisticConfig()) != Update.NOTHING) {
      int res = JOptionPane.showConfirmDialog(StatisticWizardDialog.this,
          "Are you sure you want to close the wizard, any unapplied changes will be lost?", "Close",
          JOptionPane.YES_NO_OPTION);
      if (res != JOptionPane.YES_OPTION) {
        return;
      }
    }
    setVisible(false);
    dispose();
  }

  /**
   * Load the given statistic configuration into the dialog. After a load it shall be treated as the
   * base statistic so any future changes will be treated as updates/increments of a previous load.
   * 
   * @param statistic Configuration to load
   */
  public void loadStatistic(StatisticConfig statistic) {
    pnlStatistic.loadStatistic(statistic);
    this.base = statistic;
  }

  /**
   * Handle behaviour for cementing changes made in the statistic wizard. This involves updating the
   * current workspace and updating the statistic in the campaign statistics.
   * 
   * @return Whether apply was successful
   */
  private boolean apply() {
    StatisticConfig config = makeStatisticConfig();
    ErrorBuilder eb = config.validate();
    if (eb.isError()) {
      JOptionPane.showMessageDialog(null, eb.listComments("Configuration Error"),
          "Configuration Error", JOptionPane.ERROR_MESSAGE);
      return false;
    } else {
      controller.workspace.putStatistic(config);
      loadStatistic(config);
      return true;
    }
  }

  /**
   * Generate a statistic configuration using the current wizard configuration. The current base
   * will be used as a reference point if it exists.
   * 
   * @return Generated statistic
   */
  private StatisticConfig makeStatisticConfig() {
    StatisticConfig config;
    if (base != null) {
      // Copy the uuid to identify the new instance as being a modification
      config = new StatisticConfig(base.uuid);
    } else {
      config = new StatisticConfig();
    }
    // Get updated configuration from wizard
    pnlStatistic.updateConfig(config);
    return config;
  }

  /**
   * @return The currently loaded statistic without any non-applied changes
   */
  public StatisticConfig getStatistic() {
    return base;
  }

}
