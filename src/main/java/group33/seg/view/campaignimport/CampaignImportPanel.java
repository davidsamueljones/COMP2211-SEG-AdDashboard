package group33.seg.view.campaignimport;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import group33.seg.controller.campaignimport.CampaignImportHandler;
import group33.seg.controller.utilities.ProgressListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;

public class CampaignImportPanel extends JPanel {
  private static final long serialVersionUID = 845431833540626100L;

  private CardLayout cl_Panel;

  public final CampaignImportHandler importHandler;
  private JTextField txtCampaignName;

  private JTextField txtCSVFolder;
  private JButton btnBrowseCSVFolder;

  private JTextField txtClickLog;
  private JTextField txtImpressionLog;
  private JTextField txtServerLog;
  private JButton btnBrowseClickLog;
  private JButton btnBrowseImpressionLog;
  private JButton btnBrowseServerLog;

  private JButton btnImportCampaign;
  private JButton btnCancelImport;


  /**
   * Create the panel.
   */
  public CampaignImportPanel() {
    this(null);
  }

  public CampaignImportPanel(CampaignImportHandler importHandler) {
    if (importHandler == null) {
      importHandler = new CampaignImportHandler();
    }
    this.importHandler = importHandler;

    initGUI();

    // Set the current view
    showView(View.CONTROLS);
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {

    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************
    cl_Panel = new CardLayout();
    setLayout(cl_Panel);

    // Panel for import setup
    JPanel pnlControls = new JPanel();
    GridBagLayout gbl_pnlControls = new GridBagLayout();
    gbl_pnlControls.rowHeights = new int[] {0, 0, 0, 0, 0};
    gbl_pnlControls.columnWeights = new double[] {0.0, 1.0};
    gbl_pnlControls.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
    pnlControls.setLayout(gbl_pnlControls);
    add(pnlControls, View.CONTROLS.toString());

    {
      JLabel lblHelp = new JLabel();
      lblHelp.setText(
          "<html>Import a campaign's data into the current workspace using its respective CSV files "
              + "(a click log, an impression log and a server log):</html>");
      GridBagConstraints gbc_lblHelp = new GridBagConstraints();
      gbc_lblHelp.gridwidth = 2;
      gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
      gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
      gbc_lblHelp.gridx = 0;
      gbc_lblHelp.gridy = 0;
      pnlControls.add(lblHelp, gbc_lblHelp);

      JLabel lblCampaignName = new JLabel("Campaign Name:");
      GridBagConstraints gbc_lblCampaignName = new GridBagConstraints();
      gbc_lblCampaignName.insets = new Insets(0, 0, 5, 5);
      gbc_lblCampaignName.anchor = GridBagConstraints.EAST;
      gbc_lblCampaignName.gridx = 0;
      gbc_lblCampaignName.gridy = 1;
      pnlControls.add(lblCampaignName, gbc_lblCampaignName);

      txtCampaignName = new JTextField();
      GridBagConstraints gbc_txtCampaignName = new GridBagConstraints();
      gbc_txtCampaignName.insets = new Insets(0, 0, 5, 0);
      gbc_txtCampaignName.fill = GridBagConstraints.HORIZONTAL;
      gbc_txtCampaignName.gridx = 1;
      gbc_txtCampaignName.gridy = 1;
      pnlControls.add(txtCampaignName, gbc_txtCampaignName);
      txtCampaignName.setColumns(10);


      JTabbedPane tabsPathModes = new JTabbedPane(SwingConstants.TOP);
      GridBagConstraints gbc_tabsPathModes = new GridBagConstraints();
      gbc_tabsPathModes.insets = new Insets(0, 0, 5, 0);
      gbc_tabsPathModes.gridwidth = 2;
      gbc_tabsPathModes.fill = GridBagConstraints.BOTH;
      gbc_tabsPathModes.gridx = 0;
      gbc_tabsPathModes.gridy = 2;
      pnlControls.add(tabsPathModes, gbc_tabsPathModes);

      JPanel pnlSimple = new JPanel();
      pnlSimple.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      tabsPathModes.addTab("Simple", null, pnlSimple, null);
      GridBagLayout gbl_pnlSimple = new GridBagLayout();
      gbl_pnlSimple.rowHeights = new int[] {0, 0, 0};
      gbl_pnlSimple.columnWeights = new double[] {1.0, 0.0};
      gbl_pnlSimple.rowWeights = new double[] {0.0, 0.0, 1.0};
      pnlSimple.setLayout(gbl_pnlSimple);

      {
        JLabel lblSimple = new JLabel();
        lblSimple
            .setText("<html>Select a folder that contains log files named exactly 'click_log.csv', "
                + "'impression_log.csv' and 'server_log.csv':</html>");
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
      }


      JPanel pnlAdvanced = new JPanel();
      pnlAdvanced.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      tabsPathModes.addTab("Advanced", null, pnlAdvanced, null);
      GridBagLayout gbl_pnlAdvanced = new GridBagLayout();
      gbl_pnlAdvanced.rowHeights = new int[] {0, 0};
      gbl_pnlAdvanced.columnWeights = new double[] {1.0, 0.0};
      gbl_pnlAdvanced.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
      pnlAdvanced.setLayout(gbl_pnlAdvanced);

      {
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

        JLabel lblImpressionLog = new JLabel("Impression Log:");
        GridBagConstraints gbc_lblImpressionLog = new GridBagConstraints();
        gbc_lblImpressionLog.insets = new Insets(0, 0, 5, 0);
        gbc_lblImpressionLog.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblImpressionLog.gridx = 0;
        gbc_lblImpressionLog.gridy = 3;
        pnlAdvanced.add(lblImpressionLog, gbc_lblImpressionLog);

        txtImpressionLog = new JTextField();
        GridBagConstraints gbc_txtImpressionLog = new GridBagConstraints();
        gbc_txtImpressionLog.insets = new Insets(0, 0, 5, 5);
        gbc_txtImpressionLog.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtImpressionLog.gridx = 0;
        gbc_txtImpressionLog.gridy = 4;
        pnlAdvanced.add(txtImpressionLog, gbc_txtImpressionLog);

        btnBrowseImpressionLog = new JButton("Browse");
        GridBagConstraints gbc_btnBrowseImpressionLog = new GridBagConstraints();
        gbc_btnBrowseImpressionLog.insets = new Insets(0, 0, 5, 0);
        gbc_btnBrowseImpressionLog.gridx = 1;
        gbc_btnBrowseImpressionLog.gridy = 4;
        pnlAdvanced.add(btnBrowseImpressionLog, gbc_btnBrowseImpressionLog);

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

      }

      btnImportCampaign = new JButton("Import Campaign");
      GridBagConstraints gbc_btnImportCampaign = new GridBagConstraints();
      gbc_btnImportCampaign.anchor = GridBagConstraints.EAST;
      gbc_btnImportCampaign.gridx = 1;
      gbc_btnImportCampaign.gridy = 3;
      pnlControls.add(btnImportCampaign, gbc_btnImportCampaign);

    }

    // Panel for importing screen
    JPanel pnlImporting = new JPanel();
    GridBagLayout gbl_pnlImporting = new GridBagLayout();
    gbl_pnlImporting.columnWidths = new int[] {0, 0, 0};
    gbl_pnlImporting.rowHeights = new int[] {0, 0, 0, 0, 0};
    gbl_pnlImporting.columnWeights = new double[] {0.1, 0.8, 0.1};
    gbl_pnlImporting.rowWeights = new double[] {1.0, 0.0, 0.0, 0.0, 1.0};
    pnlImporting.setLayout(gbl_pnlImporting);
    add(pnlImporting, "IMPORTING");

    JLabel lblImportProgress = new JLabel();
    GridBagConstraints gbc_lblImportProgress = new GridBagConstraints();
    gbc_lblImportProgress.insets = new Insets(0, 0, 5, 0);
    gbc_lblImportProgress.gridx = 1;
    gbc_lblImportProgress.gridy = 1;
    pnlImporting.add(lblImportProgress, gbc_lblImportProgress);

    JProgressBar pbarImportProgress = new JProgressBar();
    GridBagConstraints gbc_pbarImportProgress = new GridBagConstraints();
    gbc_pbarImportProgress.insets = new Insets(0, 0, 5, 0);
    gbc_pbarImportProgress.fill = GridBagConstraints.HORIZONTAL;
    gbc_pbarImportProgress.gridx = 1;
    gbc_pbarImportProgress.gridy = 2;
    pnlImporting.add(pbarImportProgress, gbc_pbarImportProgress);

    btnCancelImport = new JButton("Cancel Import");
    GridBagConstraints gbc_btnCancelImport = new GridBagConstraints();
    gbc_btnCancelImport.gridx = 1;
    gbc_btnCancelImport.gridy = 3;
    pnlImporting.add(btnCancelImport, gbc_btnCancelImport);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Single folder browser
    FileActionListener actFolderBrowse = new FileActionListener(JFileChooser.DIRECTORIES_ONLY);
    btnBrowseCSVFolder.addActionListener(actFolderBrowse);
    actFolderBrowse.addMapping(btnBrowseCSVFolder, txtCSVFolder);

    // Single file browsers
    FileActionListener actFileBrowse = new FileActionListener(JFileChooser.FILES_ONLY);
    btnBrowseClickLog.addActionListener(actFileBrowse);
    actFileBrowse.addMapping(btnBrowseClickLog, txtClickLog);
    btnBrowseImpressionLog.addActionListener(actFileBrowse);
    actFileBrowse.addMapping(btnBrowseImpressionLog, txtImpressionLog);
    btnBrowseServerLog.addActionListener(actFileBrowse);
    actFileBrowse.addMapping(btnBrowseServerLog, txtServerLog);

    // Handle import trigger
    btnImportCampaign.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        boolean started =
            importHandler.doImport(importHandler.new CampaignImportConfig("Test", "", "", ""));
        if (started) {
          showView(View.IMPORTING);
        } else {
          // show error
        }
      }
    });

    // Create a progress listener to handle any updates from the import handler
    importHandler.addProgressListener(new ProgressListener() {

      @Override
      public void progressUpdate(int progress) {
        EventQueue.invokeLater(new Runnable() {
          @Override
          public void run() {
            lblImportProgress.setText(String.format("Import progress %d%%...", progress));
            pbarImportProgress.setValue(progress);
          }
        });
      }

      @Override
      public void finish(boolean success) {
        Window frmCurrent = SwingUtilities.getWindowAncestor(CampaignImportPanel.this);
        frmCurrent.setVisible(false);
        frmCurrent.dispose();
      }

      @Override
      public void cancelled() {
        showView(View.CONTROLS);
      }

    });

    // Handle cancellation request
    btnCancelImport.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: Add are you sure?
        importHandler.cancelImport(false);
      }
    });

  }


  /**
   * Flip between layout cards and apply specific behaviour.
   * 
   * @param view View to switch to
   */
  private void showView(View view) {
    cl_Panel.show(CampaignImportPanel.this, view.toString());
    switch (view) {
      case CONTROLS:
        focusRequest(txtCampaignName);
        break;
      case IMPORTING:
        focusRequest(btnCancelImport);
        break;
      default:
        break;
    }
  }

  /**
   * Available views for CampaignImportPanel
   */
  private enum View {
    CONTROLS, IMPORTING
  }

  // TODO: Move to helper class
  public void focusRequest(Component component) {
    // Invoke the focus request later so components have a chance to initialise
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        component.requestFocusInWindow();
      }
    });
  }


  // TODO: Move to helper class
  public class FileActionListener implements ActionListener {
    private Map<Object, JTextField> mappings = new HashMap<>();
    private JFileChooser fileChooser = new JFileChooser();

    public FileActionListener(int selectionMode) {
      fileChooser.setFileSelectionMode(selectionMode);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JTextField txt = mappings.get(e.getSource());
      if (txt == null) {
        System.err.print("Object does not have a corresponding field to update");
        return;
      }

      int res = fileChooser.showOpenDialog(CampaignImportPanel.this);
      // fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      if (res == JFileChooser.APPROVE_OPTION) {
        txt.setText(fileChooser.getSelectedFile().getAbsolutePath());
      }
    }

    public void addMapping(Object object, JTextField txt) {
      mappings.put(object, txt);
    }

    public JFileChooser getFileChooser() {
      return fileChooser;
    }

  }

}
