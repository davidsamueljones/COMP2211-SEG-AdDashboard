package group33.seg.view.controls;

import javax.swing.JPanel;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.SettingsHandler;
import group33.seg.model.configs.CampaignConfig;
import group33.seg.view.campaignimport.CampaignImportDialog;
import group33.seg.view.utilities.Accessibility;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CampaignManagerPanel extends JPanel {
  private static final long serialVersionUID = 8138446932363054396L;

  private DashboardController controller;

  private JTextField txtCurrentCampaign;
  private JButton btnChangeCampaign;

  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public CampaignManagerPanel(DashboardController controller) {
    this.controller = controller;

    initGUI();
    setCurrentCampaign(controller.settings.prefs.get(SettingsHandler.CUR_CAMPAIGN, null));
  }

  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.rowHeights = new int[] {0, 0, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 1.0};
    setLayout(gridBagLayout);

    JLabel lblCampaign = new JLabel("Campaign:");
    GridBagConstraints gbc_lblCampaign = new GridBagConstraints();
    gbc_lblCampaign.anchor = GridBagConstraints.EAST;
    gbc_lblCampaign.insets = new Insets(0, 0, 5, 5);
    gbc_lblCampaign.gridx = 0;
    gbc_lblCampaign.gridy = 0;
    add(lblCampaign, gbc_lblCampaign);

    txtCurrentCampaign = new JTextField();
    txtCurrentCampaign.setColumns(15);
    txtCurrentCampaign.setEnabled(false);
    GridBagConstraints gbc_txtCurrentCampaign = new GridBagConstraints();
    gbc_txtCurrentCampaign.insets = new Insets(0, 0, 5, 0);
    gbc_txtCurrentCampaign.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtCurrentCampaign.gridx = 1;
    gbc_txtCurrentCampaign.gridy = 0;
    add(txtCurrentCampaign, gbc_txtCurrentCampaign);

    btnChangeCampaign = new JButton("Change Campaign");
    GridBagConstraints gbc_btnChangeCampaign = new GridBagConstraints();
    gbc_btnChangeCampaign.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnChangeCampaign.gridwidth = 2;
    gbc_btnChangeCampaign.insets = new Insets(0, 0, 5, 0);
    gbc_btnChangeCampaign.gridx = 0;
    gbc_btnChangeCampaign.gridy = 1;
    add(btnChangeCampaign, gbc_btnChangeCampaign);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Load import campaign dialog and update view
    btnChangeCampaign.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // Use current panels form as parent
        Window frmCurrent = SwingUtilities.getWindowAncestor(CampaignManagerPanel.this);
        // Show dialog
        CampaignImportDialog cid = new CampaignImportDialog(frmCurrent, controller);
        cid.setModal(true);
        cid.setVisible(true);
        // Handle dialog result TODO: Determine if cancelled before import or not
        CampaignConfig campaign = controller.imports.getImportedCampaign();
        if (campaign != null) {
          setCurrentCampaign(campaign.name);
        }
      }
    });
  }

  // TODO: Change to use campaign
  private void setCurrentCampaign(String campaign) {
    if (campaign != null) {
      controller.settings.prefs.put(SettingsHandler.CUR_CAMPAIGN, campaign);
      txtCurrentCampaign.setText(campaign);
    } else {
      txtCurrentCampaign.setText("No Campaign Set");
    }
  }
}
