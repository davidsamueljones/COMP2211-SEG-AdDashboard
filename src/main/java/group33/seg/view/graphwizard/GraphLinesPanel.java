package group33.seg.view.graphwizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import group33.seg.model.configs.LineConfig;

public class GraphLinesPanel extends JPanel {
  private static final long serialVersionUID = -1169530766129778297L;
  
  private JTabbedPane tabsLines;
  private Collection<LinePanel> linePanels;
  
  public GraphLinesPanel() {
    this(null);
  }
  
  public GraphLinesPanel(Collection<LineConfig> lines) {
    initGUI();
    loadLines(lines);
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
    
    tabsLines = new JTabbedPane(SwingConstants.TOP);
    GridBagConstraints gbc_tabsLines = new GridBagConstraints();
    gbc_tabsLines.gridheight = 5;
    gbc_tabsLines.insets = new Insets(0, 0, 5, 0);
    gbc_tabsLines.fill = GridBagConstraints.BOTH;
    gbc_tabsLines.gridx = 1;
    gbc_tabsLines.gridy = 0;
    add(tabsLines, gbc_tabsLines);
      
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
    
    btnNew.addActionListener(new ActionListener() {     
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: Check if new line already exists
        LinePanel pnlLine = new LinePanel();
        addLinePanel("New Line", pnlLine);
        tabsLines.setSelectedComponent(pnlLine);
      }
    });
  }

  public void loadLines(Collection<LineConfig> lines) {
    // Clear existing lines from tabs
    clearLines();
    if (lines != null) {
      // Load new lines as tabs
      for (LineConfig line : lines) {
        LinePanel pnlLine = new LinePanel(line);
        addLinePanel(line.identifier, pnlLine);
      }
    }
  }
  
  private final static int MAX_TAB_TITLE_LEN = 15;
  public void addLinePanel(String identifier, LinePanel panel) {
    String title = identifier;
    if (title.length() >= MAX_TAB_TITLE_LEN) {
      title = identifier.substring(0, MAX_TAB_TITLE_LEN - 3) + "...";
    }
    linePanels.add(panel);
    tabsLines.addTab(title, null, panel, identifier); 
  }
  
  public void clearLines() {
    linePanels = new ArrayList<>();
    tabsLines.removeAll();
  }
  
  public List<LineConfig> getLines() {
    List<LineConfig> lines = new ArrayList<>();
    for (LinePanel pnlLine : linePanels) {
      lines.add(pnlLine.getLine());
    }
    return lines;
  }
  
}
