package group33.seg.view.increment1;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import group33.seg.view.utilities.CollapsiblePanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import java.awt.Insets;

public class ControlsPanel extends JScrollPane {
  private static final long serialVersionUID = 335036489710020302L;

  /**
   * Create the panel.
   */
  public ControlsPanel() {
    initGUI();
  }

  private void initGUI() {

    // Store control groups on a seperate panel
    JPanel pnlControls = new JPanel();
    GridBagLayout gbl_pnlControls = new GridBagLayout();
    gbl_pnlControls.rowHeights = new int[] {0, 0, 0, 0, 0};
    gbl_pnlControls.columnWeights = new double[] {1.0};
    gbl_pnlControls.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
    pnlControls.setLayout(gbl_pnlControls);

    {
      // Campaign manager
      CollapsiblePanel colpnlCampaignManager = new CollapsiblePanel("Campaign Manager");
      CampaignManagerPanel pnlCampaignManager = new CampaignManagerPanel();
      colpnlCampaignManager.setContentPane(pnlCampaignManager);
      GridBagConstraints gbc_colpnlCampaignManager = new GridBagConstraints();
      gbc_colpnlCampaignManager.fill = GridBagConstraints.BOTH;
      gbc_colpnlCampaignManager.insets = new Insets(5, 5, 5, 5);
      gbc_colpnlCampaignManager.gridx = 0;
      gbc_colpnlCampaignManager.gridy = 0;
      pnlControls.add(colpnlCampaignManager, gbc_colpnlCampaignManager);
    }

    // Configure scroll pane
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    this.setViewportView(pnlControls);
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
  }

}
