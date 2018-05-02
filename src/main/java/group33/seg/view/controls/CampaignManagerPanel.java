package group33.seg.view.controls;

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
import group33.seg.model.configs.CampaignConfig;
import group33.seg.view.campaignselection.CampaignSelectionDialog;

public class CampaignManagerPanel extends JPanel {
  private static final long serialVersionUID = 8138446932363054396L;

  private DashboardController controller;

  private JList<CampaignConfig> lstCampaigns;
  private DefaultListModel<CampaignConfig> mdl_lstCampaigns;

  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public CampaignManagerPanel(DashboardController controller) {
    this.controller = controller;

    initGUI();
    // Load the current workspace's campaigns
    refreshCampaigns();
  }

  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0};
    setLayout(gridBagLayout);

    JButton btnAdd = new JButton("Add Campaign");
    GridBagConstraints gbc_btnAdd = new GridBagConstraints();
    gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
    gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnAdd.gridx = 0;
    gbc_btnAdd.gridy = 0;
    add(btnAdd, gbc_btnAdd);

    JPanel pnlExisting = new JPanel();
    pnlExisting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
        "Workspace Campaigns", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    GridBagConstraints gbc_pnlExisting = new GridBagConstraints();
    gbc_pnlExisting.fill = GridBagConstraints.BOTH;
    gbc_pnlExisting.gridx = 0;
    gbc_pnlExisting.gridy = 1;
    add(pnlExisting, gbc_pnlExisting);
    GridBagLayout gbl_pnlExisting = new GridBagLayout();
    gbl_pnlExisting.columnWeights = new double[] {1.0, 1.0};
    gbl_pnlExisting.rowWeights = new double[] {1.0, 0.0};
    pnlExisting.setLayout(gbl_pnlExisting);

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
          setToolTipText(String.format("<html>%s</html>", config.uid));
        }
        return comp;
      }
    });

    JScrollPane scrCampaigns = new JScrollPane(lstCampaigns);
    scrCampaigns.setPreferredSize(new Dimension(0, 75));
    scrCampaigns.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstCampaigns = new GridBagConstraints();
    gbc_lstCampaigns.gridwidth = 2;
    gbc_lstCampaigns.insets = new Insets(5, 5, 5, 5);
    gbc_lstCampaigns.fill = GridBagConstraints.BOTH;
    gbc_lstCampaigns.gridx = 0;
    gbc_lstCampaigns.gridy = 0;
    pnlExisting.add(scrCampaigns, gbc_lstCampaigns);

    JButton btnDelete = new JButton("Delete");
    btnDelete.setEnabled(false);
    GridBagConstraints gbc_btnDelete = new GridBagConstraints();
    gbc_btnDelete.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnDelete.insets = new Insets(0, 5, 5, 2);
    gbc_btnDelete.gridx = 0;
    gbc_btnDelete.gridy = 1;
    pnlExisting.add(btnDelete, gbc_btnDelete);

    JButton btnReplace = new JButton("Replace");
    btnReplace.setEnabled(false);
    GridBagConstraints gbc_btnReplace = new GridBagConstraints();
    gbc_btnReplace.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnReplace.insets = new Insets(0, 2, 5, 5);
    gbc_btnReplace.gridx = 1;
    gbc_btnReplace.gridy = 1;
    pnlExisting.add(btnReplace, gbc_btnReplace);


    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Load import campaign dialog and update view
    btnAdd.addActionListener(e -> {
      // Use current panels form as parent
      Window frmCurrent = SwingUtilities.getWindowAncestor(CampaignManagerPanel.this);
      // Show dialog
      CampaignSelectionDialog cid = new CampaignSelectionDialog(frmCurrent, controller);
      cid.setModalityType(ModalityType.APPLICATION_MODAL);
      cid.setVisible(true);
    });

    btnReplace.addActionListener(e -> {
      // Use current panels form as parent
      Window frmCurrent = SwingUtilities.getWindowAncestor(CampaignManagerPanel.this);
      // Show dialog
      CampaignSelectionDialog cid =
          new CampaignSelectionDialog(frmCurrent, controller, lstCampaigns.getSelectedValue());
      cid.setModalityType(ModalityType.APPLICATION_MODAL);
      cid.setVisible(true);
    });

    // Delete the selected graph
    btnDelete.addActionListener(e -> {
      int res = JOptionPane.showConfirmDialog(CampaignManagerPanel.this,
          "Are you sure you want to delete the selected campaign?\r\n"
              + "Any objects that use this campaign will need to be updated with a new campaign.",
          "Delete Campaign", JOptionPane.YES_NO_OPTION);
      if (res != JOptionPane.YES_OPTION) {
        return;
      }
      controller.workspace.removeCampaign(lstCampaigns.getSelectedValue());
    });

    lstCampaigns.addListSelectionListener(e -> {
      boolean selected = lstCampaigns.getSelectedValue() != null;
      btnDelete.setEnabled(selected);
      btnReplace.setEnabled(selected);
    });

    // Watch for changes in workspace campaign to update current campaign display
    controller.workspace.addListener(type -> {
      if (type == WorkspaceListener.Type.WORKSPACE || type == WorkspaceListener.Type.CAMPAIGNS) {
        refreshCampaigns();
      }
    });
  }

  /**
   * Refresh the campaigns, using the currently selected campaign as the refresh object to select.
   */
  public void refreshCampaigns() {
    refreshCampaigns(lstCampaigns.getSelectedValue());
  }

  /**
   * Fetch the workspace campaigns, replacing those displayed in the list. If the given campaign
   * exists in the list it is selected.
   * 
   * @param selected Campaign to select
   */
  public void refreshCampaigns(CampaignConfig selected) {
    mdl_lstCampaigns.clear();
    List<CampaignConfig> workspaceCampaigns = controller.workspace.getCampaigns();
    if (workspaceCampaigns != null) {
      for (CampaignConfig campaign : workspaceCampaigns) {
        mdl_lstCampaigns.addElement(campaign);
      }
      lstCampaigns.setSelectedValue(selected, true);
    } else {
      lstCampaigns.setSelectedValue(null, true);
    }
  }

}
