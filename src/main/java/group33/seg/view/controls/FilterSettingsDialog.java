package group33.seg.view.controls;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.model.configs.FilterConfig;

public class FilterSettingsDialog extends JDialog {
  private static final long serialVersionUID = -6395675568821301037L;

  private FilterConfig filter;

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   */
  public FilterSettingsDialog(Window parent) {
    this(parent, null);
  }

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param filter Filter to load into dialog
   */
  public FilterSettingsDialog(Window parent, FilterConfig filter) {
    super(parent, "Filter Modifier");
    this.filter = filter;

    // Initialise GUI
    initGUI();

    // Determine positioning
    setSize(new Dimension(500, 500));
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
  }

  private void initGUI() {
    JPanel pnlMain = new JPanel();
    pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlMain = new GridBagLayout();
    gbl_pnlMain.columnWeights = new double[] {1.0, 1.0};
    gbl_pnlMain.rowWeights = new double[] {1.0, 0.0};
    pnlMain.setLayout(gbl_pnlMain);
    setContentPane(pnlMain);

    FilterSettingsPanel pnlFilterSettings = new FilterSettingsPanel(filter);
    GridBagConstraints gbc_pnlFilterSettings = new GridBagConstraints();
    gbc_pnlFilterSettings.gridwidth = 2;
    gbc_pnlFilterSettings.insets = new Insets(5, 5, 5, 5);
    gbc_pnlFilterSettings.fill = GridBagConstraints.BOTH;
    gbc_pnlFilterSettings.gridx = 0;
    gbc_pnlFilterSettings.gridy = 0;
    pnlMain.add(pnlFilterSettings, gbc_pnlFilterSettings);

    JButton btnCancel = new JButton("Cancel");
    GridBagConstraints gbc_btnCancel = new GridBagConstraints();
    gbc_btnCancel.anchor = GridBagConstraints.EAST;
    gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
    gbc_btnCancel.gridx = 0;
    gbc_btnCancel.gridy = 1;
    pnlMain.add(btnCancel, gbc_btnCancel);

    JButton btnConfirm = new JButton("Confirm");
    GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
    gbc_btnConfirm.anchor = GridBagConstraints.WEST;
    gbc_btnConfirm.gridx = 1;
    gbc_btnConfirm.gridy = 1;
    pnlMain.add(btnConfirm, gbc_btnConfirm);

    // Ignore any changes
    btnCancel.addActionListener(e -> {
      setVisible(false);
      dispose();
    });

    // Apply changes if valid
    btnConfirm.addActionListener(e -> {
      FilterConfig filter = pnlFilterSettings.generateFilter();
      ErrorBuilder eb = filter.validate();
      if (eb.isError()) {
        JOptionPane.showMessageDialog(null, eb.listComments("Configuration Error"),
            "Configuration Error", JOptionPane.ERROR_MESSAGE);
      } else {
        this.filter = filter;
        setVisible(false);
        dispose();
      }
    });

  }

  /**
   * @return The last successfully generated filter
   */
  public FilterConfig getFilter() {
    return filter;
  }

}
