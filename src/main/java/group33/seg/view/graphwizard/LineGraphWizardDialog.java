package group33.seg.view.graphwizard;

import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import group33.seg.controller.DashboardController;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LineGraphWizardDialog extends JDialog {
  private static final long serialVersionUID = -2529642040023886708L;

  private DashboardController controller;


  public static void main(String[] args) {
    LineGraphWizardDialog dialog = new LineGraphWizardDialog(null, null);
    dialog.setVisible(true);
  }
  
  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   * @param controller Controller for this view object
   */
  public LineGraphWizardDialog(Window parent, DashboardController controller) {
    super(parent, "Line Graph Wizard");
    this.controller = controller;
    
    // Initialise GUI
    initGUI();

    // Determine positioning
    setSize(new Dimension(800, 450));
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
  }
  
  private void initGUI() {
    
    JPanel pnlContent = new JPanel();
    pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setContentPane(pnlContent);
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWeights = new double[]{1.0, 0.0, 0.0};
    gbl_pnlContent.rowWeights = new double[]{1.0, 0.0};
    pnlContent.setLayout(gbl_pnlContent);
    
    JTabbedPane tabsProperties = new JTabbedPane(JTabbedPane.TOP);
    GridBagConstraints gbc_tabsProperties = new GridBagConstraints();
    gbc_tabsProperties.gridwidth = 3;
    gbc_tabsProperties.insets = new Insets(0, 0, 5, 0);
    gbc_tabsProperties.fill = GridBagConstraints.BOTH;
    gbc_tabsProperties.gridx = 0;
    gbc_tabsProperties.gridy = 0;
    pnlContent.add(tabsProperties, gbc_tabsProperties);
    
    GraphPropertiesPanel pnlGraphProperties = new GraphPropertiesPanel();
    tabsProperties.add("Graph Properties", pnlGraphProperties);
    
    GraphLinesPanel pnlLines = new GraphLinesPanel();
    tabsProperties.add("Lines", pnlLines);
    
    JButton btnCancel = new JButton("Cancel");
    GridBagConstraints gbc_btnCancel = new GridBagConstraints();
    gbc_btnCancel.anchor = GridBagConstraints.EAST;
    gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
    gbc_btnCancel.gridx = 0;
    gbc_btnCancel.gridy = 1;
    pnlContent.add(btnCancel, gbc_btnCancel);
    
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
  }
  

}
