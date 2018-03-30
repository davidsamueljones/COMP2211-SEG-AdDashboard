package group33.seg.view.controls;

import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JPanel;
import group33.seg.model.configs.FilterConfig;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
    btnCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
      }
    });
    GridBagConstraints gbc_btnCancel = new GridBagConstraints();
    gbc_btnCancel.anchor = GridBagConstraints.EAST;
    gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
    gbc_btnCancel.gridx = 0;
    gbc_btnCancel.gridy = 1;
    pnlMain.add(btnCancel, gbc_btnCancel);

    JButton btnConfirm = new JButton("Confirm");
    btnConfirm.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        filter = pnlFilterSettings.generateFilter();
        setVisible(false);
        dispose();
      }
    });
    GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
    gbc_btnConfirm.anchor = GridBagConstraints.WEST;
    gbc_btnConfirm.gridx = 1;
    gbc_btnConfirm.gridy = 1;
    pnlMain.add(btnConfirm, gbc_btnConfirm);

  }

  public FilterConfig getFilter() {
    return filter;
  }

}
