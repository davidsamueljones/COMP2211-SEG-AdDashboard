package group33.seg.view.controls;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import group33.seg.model.configs.FilterConfig;

public class FilterViewPanel extends JPanel {
  private static final long serialVersionUID = 8739821651130799927L;
  
  private JTextArea txtFilter;
  private JButton btnModifyFilter;

  private FilterConfig filter = null;
  
  /**
   * Create the panel.
   */
  public FilterViewPanel() {
    this(true);
  }

  /**
   * Create the panel.
   * 
   * @param haveBorder Whether to place a border a border round panel
   */
  public FilterViewPanel(boolean haveBorder) {
    initGUI();

    if (haveBorder) {
      Border border = BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Filter"),
          BorderFactory.createEmptyBorder(5, 5, 5, 5));
      this.setBorder(border);
    }
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0, 0.0};
    setLayout(gridBagLayout);

    // JLabel lblCampaign = new JLabel("Campaign:");
    // GridBagConstraints gbc_lblCampaign = new GridBagConstraints();
    // gbc_lblCampaign.insets = new Insets(0, 0, 5, 5);
    // gbc_lblCampaign.anchor = GridBagConstraints.EAST;
    // gbc_lblCampaign.gridx = 0;
    // gbc_lblCampaign.gridy = 0;
    // add(lblCampaign, gbc_lblCampaign);

    // JComboBox<CampaignConfig> cboCampaign = new JComboBox<>();
    // GridBagConstraints gbc_cboCampaign = new GridBagConstraints();
    // gbc_cboCampaign.insets = new Insets(0, 0, 5, 0);
    // gbc_cboCampaign.fill = GridBagConstraints.HORIZONTAL;
    // gbc_cboCampaign.gridx = 1;
    // gbc_cboCampaign.gridy = 0;
    // add(cboCampaign, gbc_cboCampaign);

    txtFilter = new JTextArea(FilterConfig.NO_FILTER_TEXT);
    txtFilter.setEditable(false);
    JScrollPane scrFilter = new JScrollPane(txtFilter);
    scrFilter.setPreferredSize(new Dimension(0, scrFilter.getPreferredSize().height));
    scrFilter.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_txtFilter = new GridBagConstraints();
    gbc_txtFilter.gridwidth = 2;
    gbc_txtFilter.insets = new Insets(0, 0, 5, 0);
    gbc_txtFilter.fill = GridBagConstraints.BOTH;
    gbc_txtFilter.gridx = 0;
    gbc_txtFilter.gridy = 1;
    add(scrFilter, gbc_txtFilter);

    btnModifyFilter = new JButton("Modify Filter");
    GridBagConstraints gbc_btnModifyFilter = new GridBagConstraints();
    gbc_btnModifyFilter.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnModifyFilter.gridwidth = 2;
    gbc_btnModifyFilter.gridx = 0;
    gbc_btnModifyFilter.gridy = 2;
    add(btnModifyFilter, gbc_btnModifyFilter);

    btnModifyFilter.addActionListener(e -> {
      Window frmCurrent = SwingUtilities.getWindowAncestor(FilterViewPanel.this);
      FilterSettingsDialog dialog = new FilterSettingsDialog(frmCurrent, filter);
      dialog.setModal(true);
      dialog.setVisible(true);
      loadFilter(dialog.getFilter());
    });
  }
  
  public void loadFilter(FilterConfig filter) {
    this.filter = filter;
    if (filter != null) {
      txtFilter.setText(filter.inText());
    }
  }

  public FilterConfig getFilter() {
    return filter;
  }

}
