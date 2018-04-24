package group33.seg.view.structure;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.view.controls.CampaignManagerPanel;
import group33.seg.view.controls.GraphManagerPanel;
import group33.seg.view.controls.StatisticManagerPanel;
import group33.seg.view.utilities.Accessibility;
import group33.seg.view.utilities.CollapsiblePanel;

public class ControlsPanel extends JScrollPane {
  private static final long serialVersionUID = 335036489710020302L;
 
  private final DashboardController controller;
  
  private CampaignManagerPanel pnlCampaignManager;
  private GraphManagerPanel pnlGraphManager;
  private StatisticManagerPanel pnlStatisticManager;

  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public ControlsPanel(DashboardController controller) {
    this.controller = controller;
    
    initGUI();
  }
  
  private void initGUI() {

    // Store control groups on a separate panel
    JPanel pnlControls = new JPanel();
    GridBagLayout gbl_pnlControls = new GridBagLayout();
    gbl_pnlControls.rowHeights = new int[] {0, 0, 0, 0, 0};
    gbl_pnlControls.columnWeights = new double[] {1.0};
    gbl_pnlControls.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
    pnlControls.setLayout(gbl_pnlControls);

    {
      // Campaign manager
      CollapsiblePanel colpnlCampaignManager = new CollapsiblePanel("Campaign Manager");
      applyCollapsiblePanelStyle(colpnlCampaignManager);
      pnlCampaignManager = new CampaignManagerPanel(controller);
      colpnlCampaignManager.setContentPane(pnlCampaignManager);
      colpnlCampaignManager.getCollapsiblePane().setCollapsed(false);
      GridBagConstraints gbc_colpnlCampaignManager = new GridBagConstraints();
      gbc_colpnlCampaignManager.fill = GridBagConstraints.BOTH;
      gbc_colpnlCampaignManager.insets = new Insets(5, 5, 5, 5);
      gbc_colpnlCampaignManager.gridx = 0;
      gbc_colpnlCampaignManager.gridy = 0;
      pnlControls.add(colpnlCampaignManager, gbc_colpnlCampaignManager);

      // Graph Manager
      CollapsiblePanel colpnlGraphManager = new CollapsiblePanel("Graph Manager");
      applyCollapsiblePanelStyle(colpnlGraphManager);
      pnlGraphManager = new GraphManagerPanel(controller);
      colpnlGraphManager.setContentPane(pnlGraphManager);
      colpnlGraphManager.getCollapsiblePane().setCollapsed(true);
      GridBagConstraints gbc_colpnlGraphManager = new GridBagConstraints();
      gbc_colpnlGraphManager.fill = GridBagConstraints.BOTH;
      gbc_colpnlGraphManager.insets = new Insets(0, 5, 5, 5);
      gbc_colpnlGraphManager.gridx = 0;
      gbc_colpnlGraphManager.gridy = 2;
      pnlControls.add(colpnlGraphManager, gbc_colpnlGraphManager);
      
      // Statistic Viewer
      CollapsiblePanel colpnlStatisticManager = new CollapsiblePanel("Statistic Manager");
      applyCollapsiblePanelStyle(colpnlStatisticManager);
      pnlStatisticManager = new StatisticManagerPanel(controller);
      colpnlStatisticManager.setContentPane(pnlStatisticManager);
      colpnlStatisticManager.getCollapsiblePane().setCollapsed(true);
      GridBagConstraints gbc_colpnlStatisticManager = new GridBagConstraints();
      gbc_colpnlStatisticManager.fill = GridBagConstraints.BOTH;
      gbc_colpnlStatisticManager.insets = new Insets(0, 5, 5, 5);
      gbc_colpnlStatisticManager.gridx = 0;
      gbc_colpnlStatisticManager.gridy = 3;
      pnlControls.add(colpnlStatisticManager, gbc_colpnlStatisticManager);
    }

    // Configure scroll pane
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    this.setViewportView(pnlControls);
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.getVerticalScrollBar().setUnitIncrement(10);
  }
  
  private void applyCollapsiblePanelStyle(CollapsiblePanel colpnl) {
//    Font curFont = colpnl.getToggleButton().getFont();
//    Font newFont = Accessibility.stripLaF(curFont.deriveFont(curFont.getStyle() | Font.BOLD));
//    colpnl.getToggleButton().setFont(newFont);
    Accessibility.scaleJComponentFontSize(colpnl.getToggleButton(), 1.25);
  }

}
