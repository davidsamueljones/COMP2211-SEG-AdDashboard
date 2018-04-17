package group33.seg.view.campaignimport;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.controller.utilities.ErrorBuilder;
import group33.seg.model.configs.CampaignConfig;

public class AvailableCampaignsPanel extends JPanel {
  private static final long serialVersionUID = 410986964221436793L;


  private DashboardController controller;

  private JList<CampaignConfig> lstCampaigns;
  private DefaultListModel<CampaignConfig> mdl_lstCampaigns;

  /**
   * Create the panel, loading the workspace's current graphs.
   * 
   * @param controller Controller for this view object
   */
  public AvailableCampaignsPanel(DashboardController controller) {
    this.controller = controller;
    initGUI();
  }

  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0, 0.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0, 0.0};
    setLayout(gridBagLayout);

    JLabel lblHelp = new JLabel();
    String msgHelp = "Load a campaign that is available on the current server, "
        + "if the desired campaign is not available it must be imported:";
    lblHelp.setText(String.format("<html>%s</html>", msgHelp));
    GridBagConstraints gbc_lblHelp = new GridBagConstraints();
    gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblHelp.gridwidth = 2;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 0;
    add(lblHelp, gbc_lblHelp);

    mdl_lstCampaigns = new DefaultListModel<>();
    lstCampaigns = new JList<>(mdl_lstCampaigns);
    lstCampaigns.setCellRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 4349332453062368120L;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        Component comp =
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof CampaignConfig) {
          CampaignConfig config = (CampaignConfig) value;
          setText(config.name);
        }
        return comp;
      }
    });
    JScrollPane scrGraphs = new JScrollPane(lstCampaigns);
    scrGraphs.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstCampaigns = new GridBagConstraints();
    gbc_lstCampaigns.gridwidth = 2;
    gbc_lstCampaigns.insets = new Insets(5, 5, 5, 5);
    gbc_lstCampaigns.fill = GridBagConstraints.BOTH;
    gbc_lstCampaigns.gridx = 0;
    gbc_lstCampaigns.gridy = 1;
    add(scrGraphs, gbc_lstCampaigns);

    JButton btnRefresh = new JButton("Refresh");
    GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
    gbc_btnRefresh.anchor = GridBagConstraints.WEST;
    gbc_btnRefresh.insets = new Insets(0, 0, 0, 5);
    gbc_btnRefresh.gridx = 0;
    gbc_btnRefresh.gridy = 2;
    add(btnRefresh, gbc_btnRefresh);

    JButton btnLoadCampaign = new JButton("Load Campaign");
    GridBagConstraints gbc_btnLoadCampaign = new GridBagConstraints();
    gbc_btnLoadCampaign.anchor = GridBagConstraints.EAST;
    gbc_btnLoadCampaign.gridx = 1;
    gbc_btnLoadCampaign.gridy = 2;
    add(btnLoadCampaign, gbc_btnLoadCampaign);

    SwingUtilities.invokeLater(this::refreshCampaigns);
    
    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    btnRefresh.addActionListener(arg0 -> refreshCampaigns());
    btnLoadCampaign.addActionListener(arg0 -> {
      CampaignConfig campaign = lstCampaigns.getSelectedValue();
      if (campaign != null) {
        Window frmCurrent = SwingUtilities.getWindowAncestor(AvailableCampaignsPanel.this);
        frmCurrent.setVisible(false);
        frmCurrent.dispose();
        controller.workspace.setCampaign(campaign); 
      }
    });
  }

  private void refreshCampaigns() {
    controller.imports.clearErrors();
    try {
      List<CampaignConfig> campaigns = controller.imports.getAvailableCampaigns();
      Object selected = lstCampaigns.getSelectedValue();
      mdl_lstCampaigns.clear();
      for (CampaignConfig campaign : campaigns) {
        mdl_lstCampaigns.addElement(campaign);
      }
      lstCampaigns.setSelectedValue(selected, true);
    } catch (Exception e) {
      ErrorBuilder eb = controller.imports.getErrors();
      JOptionPane.showMessageDialog(null, eb.listComments("Campaign Fetch Error"),
          "Campaign Fetch Error", JOptionPane.ERROR_MESSAGE);
    }
  }

}
