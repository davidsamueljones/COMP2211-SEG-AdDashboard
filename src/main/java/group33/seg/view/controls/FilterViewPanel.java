package group33.seg.view.controls;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;

public class FilterViewPanel extends JPanel {
  private static final long serialVersionUID = 8739821651130799927L;
  private JButton btnModifyFilter;

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

    JLabel lblCampaign = new JLabel("Campaign:");
    GridBagConstraints gbc_lblCampaign = new GridBagConstraints();
    gbc_lblCampaign.insets = new Insets(0, 0, 5, 5);
    gbc_lblCampaign.anchor = GridBagConstraints.EAST;
    gbc_lblCampaign.gridx = 0;
    gbc_lblCampaign.gridy = 0;
    add(lblCampaign, gbc_lblCampaign);

    // TODO: Chose type for JComboBox (String or Campaign)
    JComboBox cboCampaign = new JComboBox();
    GridBagConstraints gbc_cboCampaign = new GridBagConstraints();
    gbc_cboCampaign.insets = new Insets(0, 0, 5, 0);
    gbc_cboCampaign.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboCampaign.gridx = 1;
    gbc_cboCampaign.gridy = 0;
    add(cboCampaign, gbc_cboCampaign);

    // TODO: Chose type for JList (String or Filter)
    JList lstFilter = new JList();
    lstFilter.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstFilter = new GridBagConstraints();
    gbc_lstFilter.gridwidth = 2;
    gbc_lstFilter.insets = new Insets(0, 0, 5, 0);
    gbc_lstFilter.fill = GridBagConstraints.BOTH;
    gbc_lstFilter.gridx = 0;
    gbc_lstFilter.gridy = 1;
    add(lstFilter, gbc_lstFilter);

    btnModifyFilter = new JButton("Modify Filter");
    GridBagConstraints gbc_btnModifyFilter = new GridBagConstraints();
    gbc_btnModifyFilter.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnModifyFilter.gridwidth = 2;
    gbc_btnModifyFilter.gridx = 0;
    gbc_btnModifyFilter.gridy = 2;
    add(btnModifyFilter, gbc_btnModifyFilter);
  }

}