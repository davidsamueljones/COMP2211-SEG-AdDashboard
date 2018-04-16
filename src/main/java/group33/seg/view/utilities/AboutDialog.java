package group33.seg.view.utilities;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JTextPane;

public class AboutDialog extends JDialog {
  private static final long serialVersionUID = 6431317165687978167L;

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
    gbl_pnlContent.columnWeights = new double[]{0.0, 1.0, 0.0};
    gbl_pnlContent.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
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
    
    JTextPane txtLicenses = new JTextPane();
    txtLicenses.putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    txtLicenses.setContentType("text/html");
    txtLicenses.setEditable(false);
    txtLicenses.setText(getLicensesText());
    JScrollPane scrLicenses = new JScrollPane(txtLicenses);
    scrLicenses.setPreferredSize(new Dimension(scrLicenses.getPreferredSize().width, 200));
    EventQueue.invokeLater(() -> scrLicenses.getVerticalScrollBar().setValue(0));
    GridBagConstraints gbc_txtLicenses = new GridBagConstraints();
    gbc_txtLicenses.insets = new Insets(0, 0, 0, 5);
    gbc_txtLicenses.fill = GridBagConstraints.BOTH;
    gbc_txtLicenses.gridx = 1;
    gbc_txtLicenses.gridy = 3;
    pnlContent.add(scrLicenses, gbc_txtLicenses);
  }

  /**
   * @return License text from class file in formatted html.
   */
  private String getLicensesText() {
    try {
      Path file = new File(getClass().getResource("/licenses.txt").toURI()).toPath();
      StringBuilder builder = new StringBuilder("<html>");
      builder.append("<h1>Licenses</h1>");
      Files.lines(file).forEachOrdered(l -> builder.append(l).append("\r\n"));
      builder.append("</html>");
      return builder.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return "Unable to get license information";
    }   
  }

}
