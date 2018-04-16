package group33.seg.view.utilities;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import group33.seg.controller.DashboardController;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;

public class AboutDialog extends JDialog {
  
  /**
   * Create the dialog with a given parent window.
   *
   * @param parent Window to treat as a parent
   */
  public AboutDialog(Window parent) {
    super(parent, "About");

    // Initialise GUI
    initGUI();

    // Set sizing
    pack();  
    // Set positioning
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
    gbl_pnlContent.columnWidths = new int[]{0, 0, 0};
    gbl_pnlContent.rowHeights = new int[]{0, 0, 0, 0};
    gbl_pnlContent.columnWeights = new double[]{0.0, 0.0, 0.0};
    gbl_pnlContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
    pnlContent.setLayout(gbl_pnlContent);
    
    JPanel pnlIcon = new JPanel();
    GridBagConstraints gbc_pnlIcon = new GridBagConstraints();
    gbc_pnlIcon.insets = new Insets(0, 0, 5, 5);
    gbc_pnlIcon.fill = GridBagConstraints.BOTH;
    gbc_pnlIcon.gridx = 1;
    gbc_pnlIcon.gridy = 0;
    pnlContent.add(pnlIcon, gbc_pnlIcon);
    GridBagLayout gbl_pnlIcon = new GridBagLayout();
    gbl_pnlIcon.columnWidths = new int[]{0};
    gbl_pnlIcon.rowHeights = new int[]{0};
    gbl_pnlIcon.columnWeights = new double[]{Double.MIN_VALUE};
    gbl_pnlIcon.rowWeights = new double[]{Double.MIN_VALUE};
    pnlIcon.setLayout(gbl_pnlIcon);
    
    JLabel lblAdDashboard = new JLabel("<html><b>Ad-Dashboard</b></html>");
    GridBagConstraints gbc_lblAdDashboard = new GridBagConstraints();
    gbc_lblAdDashboard.insets = new Insets(0, 0, 5, 5);
    gbc_lblAdDashboard.gridx = 1;
    gbc_lblAdDashboard.gridy = 1;
    pnlContent.add(lblAdDashboard, gbc_lblAdDashboard);
    
    JLabel lblGroup = new JLabel("Group 33");
    GridBagConstraints gbc_lblGroup = new GridBagConstraints();
    gbc_lblGroup.insets = new Insets(0, 0, 5, 5);
    gbc_lblGroup.gridx = 1;
    gbc_lblGroup.gridy = 2;
    pnlContent.add(lblGroup, gbc_lblGroup);
  }

}
