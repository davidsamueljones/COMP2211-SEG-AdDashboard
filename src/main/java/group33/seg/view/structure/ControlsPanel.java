package group33.seg.view.structure;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.view.DashboardView;
import group33.seg.view.controls.CampaignManagerPanel;
import group33.seg.view.controls.GraphGeneratorPanel;
import group33.seg.view.controls.StatisticViewer;
import group33.seg.view.utilities.CollapsiblePanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import java.awt.Insets;

public class ControlsPanel extends JScrollPane {
  private static final long serialVersionUID = 335036489710020302L;
 
  private final DashboardController controller;
  
  private CampaignManagerPanel pnlCampaignManager;
  private StatisticViewer pnlStatisticViewer;
  private GraphGeneratorPanel pnlGraphGenerator;

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
      pnlCampaignManager = new CampaignManagerPanel(controller);
      colpnlCampaignManager.setContentPane(pnlCampaignManager);
      GridBagConstraints gbc_colpnlCampaignManager = new GridBagConstraints();
      gbc_colpnlCampaignManager.fill = GridBagConstraints.BOTH;
      gbc_colpnlCampaignManager.insets = new Insets(5, 5, 5, 5);
      gbc_colpnlCampaignManager.gridx = 0;
      gbc_colpnlCampaignManager.gridy = 0;
      pnlControls.add(colpnlCampaignManager, gbc_colpnlCampaignManager);

      // Statistic Viewer
      CollapsiblePanel colpnlStatisticViewer = new CollapsiblePanel("Statistic Viewer");
      pnlStatisticViewer = new StatisticViewer(controller);
      colpnlStatisticViewer.setContentPane(pnlStatisticViewer);
      GridBagConstraints gbc_colpnlStatisticViewer = new GridBagConstraints();
      gbc_colpnlStatisticViewer.fill = GridBagConstraints.BOTH;
      gbc_colpnlStatisticViewer.insets = new Insets(0, 5, 5, 5);
      gbc_colpnlStatisticViewer.gridx = 0;
      gbc_colpnlStatisticViewer.gridy = 1;
      pnlControls.add(colpnlStatisticViewer, gbc_colpnlStatisticViewer);

      // Graph Generator
      CollapsiblePanel colpnlGraphGenerator = new CollapsiblePanel("Graph Generator");
      pnlGraphGenerator = new GraphGeneratorPanel(controller);
      colpnlGraphGenerator.setContentPane(pnlGraphGenerator);
      GridBagConstraints gbc_colpnlGraphGenerator = new GridBagConstraints();
      gbc_colpnlGraphGenerator.fill = GridBagConstraints.BOTH;
      gbc_colpnlGraphGenerator.insets = new Insets(0, 5, 5, 5);
      gbc_colpnlGraphGenerator.gridx = 0;
      gbc_colpnlGraphGenerator.gridy = 2;
      pnlControls.add(colpnlGraphGenerator, gbc_colpnlGraphGenerator);
    }

    // Configure scroll pane
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    this.setViewportView(pnlControls);
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
  }

}
