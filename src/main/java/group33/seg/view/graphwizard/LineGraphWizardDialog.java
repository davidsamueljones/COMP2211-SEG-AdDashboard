package group33.seg.view.graphwizard;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.GraphsHandler;
import group33.seg.model.configs.BounceConfig;
import group33.seg.model.configs.FilterConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.LineGraphConfig.Mode;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.view.campaignimport.CampaignImportPanel;
import group33.seg.model.configs.MetricQuery;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LineGraphWizardDialog extends JDialog {
  private static final long serialVersionUID = -2529642040023886708L;

  private DashboardController controller;

  private GraphPropertiesPanel pnlGraphProperties;
  private GraphLinesPanel pnlLines;
  private LineGraphConfig base;


  public LineGraphWizardDialog(Window parent, DashboardController controller) {
    this(parent, controller, null);
  }

  public LineGraphWizardDialog(Window parent, DashboardController controller,
      LineGraphConfig graph) {
    super(parent, "Line Graph Wizard");

    this.controller = controller;
    // Initialise GUI
    initGUI();
    loadGraph(graph);

    // Determine positioning
    setSize(new Dimension(800, 600));
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  private void initGUI() {

    JPanel pnlContent = new JPanel();
    pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setContentPane(pnlContent);
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWeights = new double[] {1.0, 0.0, 0.0};
    gbl_pnlContent.rowWeights = new double[] {1.0, 0.0};
    pnlContent.setLayout(gbl_pnlContent);

    JTabbedPane tabsProperties = new JTabbedPane(JTabbedPane.TOP);
    GridBagConstraints gbc_tabsProperties = new GridBagConstraints();
    gbc_tabsProperties.gridwidth = 3;
    gbc_tabsProperties.insets = new Insets(0, 0, 5, 0);
    gbc_tabsProperties.fill = GridBagConstraints.BOTH;
    gbc_tabsProperties.gridx = 0;
    gbc_tabsProperties.gridy = 0;
    pnlContent.add(tabsProperties, gbc_tabsProperties);

    pnlGraphProperties = new GraphPropertiesPanel();
    tabsProperties.add("Graph Properties", pnlGraphProperties);

    pnlLines = new GraphLinesPanel();
    tabsProperties.add("Lines", pnlLines);

    JButton btnClose = new JButton("Close");
    GridBagConstraints gbc_btnClose = new GridBagConstraints();
    gbc_btnClose.anchor = GridBagConstraints.EAST;
    gbc_btnClose.insets = new Insets(0, 0, 0, 5);
    gbc_btnClose.gridx = 0;
    gbc_btnClose.gridy = 1;
    pnlContent.add(btnClose, gbc_btnClose);

    JButton btnApply = new JButton("Apply");
    GridBagConstraints gbc_btnApply = new GridBagConstraints();
    gbc_btnApply.insets = new Insets(0, 0, 0, 5);
    gbc_btnApply.gridx = 1;
    gbc_btnApply.gridy = 1;
    pnlContent.add(btnApply, gbc_btnApply);

    JButton btnApplyClose = new JButton("Apply & Close");
    GridBagConstraints gbc_btnApplyClose = new GridBagConstraints();
    gbc_btnApplyClose.gridx = 2;
    gbc_btnApplyClose.gridy = 1;
    pnlContent.add(btnApplyClose, gbc_btnApplyClose);

    btnClose.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int res = JOptionPane.showConfirmDialog(LineGraphWizardDialog.this,
            "Are you sure you want to close the wizard, any unapplied changes will be lost?",
            "Close", JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) {
          return;
        }
        setVisible(false);
        dispose();
      }
    });

    btnApply.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        apply();
      }
    });

    btnApplyClose.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        apply();
        setVisible(false);
        dispose();
      }
    });

  }

  public void loadGraph(LineGraphConfig graph) {
    this.base = graph;
    pnlGraphProperties.loadGraph(graph);
    pnlLines.updateLines((graph == null ? null : graph.lines));
  }

  private void apply() {
    LineGraphConfig config = makeGraphConfig();
    controller.workspace.putGraph(config);
    controller.graphs.displayGraph(config);
    loadGraph(config);
  }

  private LineGraphConfig makeGraphConfig() {
    LineGraphConfig config;
    if (base != null) {
      // Copy the uuid to identify the new instance as being a modification
      config = new LineGraphConfig(base.uuid);
    } else {
      config = new LineGraphConfig();
    }
    // Get updated configuration from wizard
    pnlGraphProperties.updateConfig(config);
    config.lines = pnlLines.getLines();
    return config;
  }

  public LineGraphConfig getGraph() {
    return base;
  }

}
