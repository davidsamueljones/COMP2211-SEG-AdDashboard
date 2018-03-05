package group33.seg.view.campaignimport;

import javax.swing.JPanel;
import java.awt.Insets;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

public class CampaignImportPanel extends JPanel {
  private static final long serialVersionUID = 845431833540626100L;

  private JTextField txtCampaignName;

  private JTextField txtCSVFolder;
  private JButton btnBrowseCSVFolder;

  private JTextField txtClickLog;
  private JTextField txtImpressionsLog;
  private JTextField txtServerLog;
  private JButton btnBrowseClickLog;
  private JButton btnBrowseImpressionsLog;
  private JButton btnBrowseServerLog;

  private JButton btnImportCampaign;

  /**
   * Create the panel.
   */
  public CampaignImportPanel() {

    initGUI();
  }

  private void initGUI() {
    GridBagLayout gbl_pnlControls = new GridBagLayout();
    gbl_pnlControls.rowHeights = new int[] {0, 0, 0, 0, 0};
    gbl_pnlControls.columnWeights = new double[] {0.0, 1.0};
    gbl_pnlControls.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
    setLayout(gbl_pnlControls);

    JLabel lblHelp = new JLabel();
    lblHelp.setText(
        "<html>Import a campaign's data into the current workspace using its respective CSV files "
            + "(a click log, an impressions log and a server log):</html>");
    GridBagConstraints gbc_lblHelp = new GridBagConstraints();
    gbc_lblHelp.gridwidth = 2;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
    gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 0;
    add(lblHelp, gbc_lblHelp);

    JLabel lblCampaignName = new JLabel("Campaign Name:");
    GridBagConstraints gbc_lblCampaignName = new GridBagConstraints();
    gbc_lblCampaignName.insets = new Insets(0, 0, 5, 5);
    gbc_lblCampaignName.anchor = GridBagConstraints.EAST;
    gbc_lblCampaignName.gridx = 0;
    gbc_lblCampaignName.gridy = 1;
    add(lblCampaignName, gbc_lblCampaignName);

    txtCampaignName = new JTextField();
    GridBagConstraints gbc_txtCampaignName = new GridBagConstraints();
    gbc_txtCampaignName.insets = new Insets(0, 0, 5, 0);
    gbc_txtCampaignName.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtCampaignName.gridx = 1;
    gbc_txtCampaignName.gridy = 1;
    add(txtCampaignName, gbc_txtCampaignName);
    txtCampaignName.setColumns(10);


    JTabbedPane tabsPathModes = new JTabbedPane(SwingConstants.TOP);
    GridBagConstraints gbc_tabsPathModes = new GridBagConstraints();
    gbc_tabsPathModes.insets = new Insets(0, 0, 5, 0);
    gbc_tabsPathModes.gridwidth = 2;
    gbc_tabsPathModes.fill = GridBagConstraints.BOTH;
    gbc_tabsPathModes.gridx = 0;
    gbc_tabsPathModes.gridy = 2;
    add(tabsPathModes, gbc_tabsPathModes);

    JPanel pnlSimple = new JPanel();
    pnlSimple.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabsPathModes.addTab("Simple", null, pnlSimple, null);
    GridBagLayout gbl_pnlSimple = new GridBagLayout();
    gbl_pnlSimple.rowHeights = new int[] {0, 0, 0};
    gbl_pnlSimple.columnWeights = new double[] {1.0, 0.0};
    gbl_pnlSimple.rowWeights = new double[] {0.0, 0.0, 1.0};
    pnlSimple.setLayout(gbl_pnlSimple);

    JLabel lblSimple = new JLabel();
    lblSimple.setText(
        "<html>Select a folder that contains log files named exactly 'click_log.csv', 'impressions_log.csv' and 'server_log.csv':</html>");
    GridBagConstraints gbc_lblSimple = new GridBagConstraints();
    gbc_lblSimple.gridwidth = 2;
    gbc_lblSimple.insets = new Insets(0, 0, 5, 0);
    gbc_lblSimple.fill = GridBagConstraints.BOTH;
    gbc_lblSimple.gridx = 0;
    gbc_lblSimple.gridy = 0;
    pnlSimple.add(lblSimple, gbc_lblSimple);

    txtCSVFolder = new JTextField();
    GridBagConstraints gbc_txtCSVFolder = new GridBagConstraints();
    gbc_txtCSVFolder.insets = new Insets(0, 0, 0, 5);
    gbc_txtCSVFolder.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtCSVFolder.gridx = 0;
    gbc_txtCSVFolder.gridy = 1;
    pnlSimple.add(txtCSVFolder, gbc_txtCSVFolder);

    btnBrowseCSVFolder = new JButton("Browse");
    GridBagConstraints gbc_btnBrowseCSVFolder = new GridBagConstraints();
    gbc_btnBrowseCSVFolder.gridx = 1;
    gbc_btnBrowseCSVFolder.gridy = 1;
    pnlSimple.add(btnBrowseCSVFolder, gbc_btnBrowseCSVFolder);

    JPanel pnlAdvanced = new JPanel();
    pnlAdvanced.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabsPathModes.addTab("Advanced", null, pnlAdvanced, null);
    GridBagLayout gbl_pnlAdvanced = new GridBagLayout();
    gbl_pnlAdvanced.rowHeights = new int[] {0, 0};
    gbl_pnlAdvanced.columnWeights = new double[] {1.0, 0.0};
    gbl_pnlAdvanced.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
    pnlAdvanced.setLayout(gbl_pnlAdvanced);

    JLabel lblAdvanced = new JLabel();
    lblAdvanced.setText("Select indvidual file paths to CSV files:");
    GridBagConstraints gbc_lblAdvanced = new GridBagConstraints();
    gbc_lblAdvanced.gridwidth = 2;
    gbc_lblAdvanced.insets = new Insets(0, 0, 5, 0);
    gbc_lblAdvanced.fill = GridBagConstraints.BOTH;
    gbc_lblAdvanced.gridx = 0;
    gbc_lblAdvanced.gridy = 0;
    pnlAdvanced.add(lblAdvanced, gbc_lblAdvanced);

    JLabel lblClickLog = new JLabel("Click Log:");
    GridBagConstraints gbc_lblClickLog = new GridBagConstraints();
    gbc_lblClickLog.insets = new Insets(0, 0, 5, 0);
    gbc_lblClickLog.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblClickLog.gridx = 0;
    gbc_lblClickLog.gridy = 1;
    pnlAdvanced.add(lblClickLog, gbc_lblClickLog);

    txtClickLog = new JTextField();
    GridBagConstraints gbc_txtClickLog = new GridBagConstraints();
    gbc_txtClickLog.insets = new Insets(0, 0, 5, 5);
    gbc_txtClickLog.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtClickLog.gridx = 0;
    gbc_txtClickLog.gridy = 2;
    pnlAdvanced.add(txtClickLog, gbc_txtClickLog);

    btnBrowseClickLog = new JButton("Browse");
    GridBagConstraints gbc_btnBrowseClickLog = new GridBagConstraints();
    gbc_btnBrowseClickLog.insets = new Insets(0, 0, 5, 0);
    gbc_btnBrowseClickLog.gridx = 1;
    gbc_btnBrowseClickLog.gridy = 2;
    pnlAdvanced.add(btnBrowseClickLog, gbc_btnBrowseClickLog);

    JLabel lblImpressionsLog = new JLabel("Impressions Log:");
    GridBagConstraints gbc_lblImpressionsLog = new GridBagConstraints();
    gbc_lblImpressionsLog.insets = new Insets(0, 0, 5, 0);
    gbc_lblImpressionsLog.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblImpressionsLog.gridx = 0;
    gbc_lblImpressionsLog.gridy = 3;
    pnlAdvanced.add(lblImpressionsLog, gbc_lblImpressionsLog);

    txtImpressionsLog = new JTextField();
    GridBagConstraints gbc_txtImpressionsLog = new GridBagConstraints();
    gbc_txtImpressionsLog.insets = new Insets(0, 0, 5, 5);
    gbc_txtImpressionsLog.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtImpressionsLog.gridx = 0;
    gbc_txtImpressionsLog.gridy = 4;
    pnlAdvanced.add(txtImpressionsLog, gbc_txtImpressionsLog);

    btnBrowseImpressionsLog = new JButton("Browse");
    GridBagConstraints gbc_btnBrowseImpressionsLog = new GridBagConstraints();
    gbc_btnBrowseImpressionsLog.insets = new Insets(0, 0, 5, 0);
    gbc_btnBrowseImpressionsLog.gridx = 1;
    gbc_btnBrowseImpressionsLog.gridy = 4;
    pnlAdvanced.add(btnBrowseImpressionsLog, gbc_btnBrowseImpressionsLog);

    JLabel lblServerLog = new JLabel("Server Log:");
    GridBagConstraints gbc_lblServerLog = new GridBagConstraints();
    gbc_lblServerLog.insets = new Insets(0, 0, 5, 0);
    gbc_lblServerLog.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblServerLog.gridx = 0;
    gbc_lblServerLog.gridy = 5;
    pnlAdvanced.add(lblServerLog, gbc_lblServerLog);

    txtServerLog = new JTextField();
    GridBagConstraints gbc_txtServerLog = new GridBagConstraints();
    gbc_txtServerLog.insets = new Insets(0, 0, 0, 5);
    gbc_txtServerLog.fill = GridBagConstraints.HORIZONTAL;
    gbc_txtServerLog.gridx = 0;
    gbc_txtServerLog.gridy = 6;
    pnlAdvanced.add(txtServerLog, gbc_txtServerLog);

    btnBrowseServerLog = new JButton("Browse");
    GridBagConstraints gbc_btnBrowseServerLog = new GridBagConstraints();
    gbc_btnBrowseServerLog.gridx = 1;
    gbc_btnBrowseServerLog.gridy = 6;
    pnlAdvanced.add(btnBrowseServerLog, gbc_btnBrowseServerLog);

    btnImportCampaign = new JButton("Import Campaign");
    GridBagConstraints gbc_btnImportCampaign = new GridBagConstraints();
    gbc_btnImportCampaign.anchor = GridBagConstraints.EAST;
    gbc_btnImportCampaign.gridx = 1;
    gbc_btnImportCampaign.gridy = 3;
    add(btnImportCampaign, gbc_btnImportCampaign);

    // tabsPathModes.addChangeListener(new ChangeListener() {
    //
    // @Override
    // public void stateChanged(ChangeEvent e) {
    // Component p = ((JTabbedPane) e.getSource()).getSelectedComponent();
    // Dimension panelDim = p.getPreferredSize();
    // Dimension nd = new Dimension(0, originalTabsDim.height + panelDim.height + 50);
    // System.out.println(originalTabsDim.height);
    // System.out.println(pnlAdvanced.getPreferredSize().height);
    // System.out.println(panelDim.height);
    // tabsPathModes.setPreferredSize(nd);
    // revalidate();
    // }
    // });

  }

}
