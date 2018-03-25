package group33.seg.view.graphwizard;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

public class GraphLinesPanel extends JPanel {
  private static final long serialVersionUID = -1169530766129778297L;

  public GraphLinesPanel() {
    initGUI();
  }

  private void initGUI() {
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlMain = new GridBagLayout();
    gbl_pnlMain.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
    gbl_pnlMain.columnWeights = new double[]{0.0, 1.0};
    setLayout(gbl_pnlMain);
    
    JButton btnNew = new JButton("New");
    GridBagConstraints gbc_btnNew = new GridBagConstraints();
    gbc_btnNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNew.insets = new Insets(0, 0, 5, 5);
    gbc_btnNew.gridx = 0;
    gbc_btnNew.gridy = 0;
    add(btnNew, gbc_btnNew);
    
    JTabbedPane tabsLines = new JTabbedPane(JTabbedPane.TOP);
    GridBagConstraints gbc_tabsLines = new GridBagConstraints();
    gbc_tabsLines.gridheight = 5;
    gbc_tabsLines.insets = new Insets(0, 0, 5, 0);
    gbc_tabsLines.fill = GridBagConstraints.BOTH;
    gbc_tabsLines.gridx = 1;
    gbc_tabsLines.gridy = 0;
    add(tabsLines, gbc_tabsLines);
    tabsLines.add("L1", new LinePanel());
    
    JButton btnImport = new JButton("Import");
    GridBagConstraints gbc_btnImport = new GridBagConstraints();
    gbc_btnImport.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnImport.insets = new Insets(0, 0, 5, 5);
    gbc_btnImport.gridx = 0;
    gbc_btnImport.gridy = 1;
    add(btnImport, gbc_btnImport);
    
    JSeparator separator = new JSeparator();
    GridBagConstraints gbc_separator = new GridBagConstraints();
    gbc_separator.insets = new Insets(0, 5, 5, 5);
    gbc_separator.fill = GridBagConstraints.BOTH;
    gbc_separator.gridx = 0;
    gbc_separator.gridy = 2;
    add(separator, gbc_separator);
    
    JButton btnRemove = new JButton("Remove");
    GridBagConstraints gbc_btnRemove = new GridBagConstraints();
    gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnRemove.insets = new Insets(0, 0, 0, 5);
    gbc_btnRemove.gridx = 0;
    gbc_btnRemove.gridy = 3;
    add(btnRemove, gbc_btnRemove);
  }
  
}
