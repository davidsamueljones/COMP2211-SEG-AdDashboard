package group33.seg.view.utilities;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

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
    pnlContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setContentPane(pnlContent);
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWidths = new int[] {0};
    gbl_pnlContent.rowHeights = new int[] {0, 0};
    gbl_pnlContent.columnWeights = new double[] {1.0};
    gbl_pnlContent.rowWeights = new double[] {0.0, 1.0};
    pnlContent.setLayout(gbl_pnlContent);

    JPanel pnlAbout = new AboutPanel();
    GridBagConstraints gbc_pnlAbout = new GridBagConstraints();
    gbc_pnlAbout.insets = new Insets(0, 0, 5, 0);
    gbc_pnlAbout.fill = GridBagConstraints.BOTH;
    gbc_pnlAbout.gridx = 0;
    gbc_pnlAbout.gridy = 0;
    pnlContent.add(pnlAbout, gbc_pnlAbout);

    JTextPane txtLicenses = new JTextPane();
    txtLicenses.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    txtLicenses.setContentType("text/html");
    txtLicenses.setEditable(false);
    txtLicenses.setText(getLicensesText());
    JScrollPane scrLicenses = new JScrollPane(txtLicenses);
    scrLicenses.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrLicenses.setPreferredSize(new Dimension(scrLicenses.getPreferredSize().width, 200));
    GridBagConstraints gbc_txtLicenses = new GridBagConstraints();
    gbc_txtLicenses.insets = new Insets(0, 0, 5, 0);
    gbc_txtLicenses.fill = GridBagConstraints.BOTH;
    gbc_txtLicenses.gridx = 0;
    gbc_txtLicenses.gridy = 1;
    pnlContent.add(scrLicenses, gbc_txtLicenses);
    EventQueue.invokeLater(() -> scrLicenses.getVerticalScrollBar().setValue(0));
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
