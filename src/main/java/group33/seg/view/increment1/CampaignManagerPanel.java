package group33.seg.view.increment1;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import group33.seg.view.campaignimport.CampaignImportDialog;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CampaignManagerPanel extends JPanel {
  private static final long serialVersionUID = 8138446932363054396L;
  private JButton btnChangeCampaign;
  private JTextField txtCurrentCampaign;

  /**
   * Create the panel.
   */
  public CampaignManagerPanel() {

    initGUI();
  }

  private void initGUI() {
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

    btnChangeCampaign.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        Frame frmCurrent = (Frame) SwingUtilities.getWindowAncestor(CampaignManagerPanel.this);
        CampaignImportDialog cid = new CampaignImportDialog(frmCurrent);
        cid.setVisible(true);
      }
    });

  }

}
