package group33.seg.view.graphwizard;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.types.Pair;
import group33.seg.model.configs.LineConfig;

public class GraphLinesPanel extends JPanel {
  private static final long serialVersionUID = -1169530766129778297L;

  private final static int MAX_TAB_TITLE_LEN = 15;

  private JTabbedPane tabsLines;
  private List<LinePanel> linePanels;

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
    gbl_pnlMain.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
    gbl_pnlMain.columnWeights = new double[] {0.0, 1.0};
    setLayout(gbl_pnlMain);

    JButton btnNew = new JButton("New");
    GridBagConstraints gbc_btnNew = new GridBagConstraints();
    gbc_btnNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNew.insets = new Insets(0, 0, 5, 5);
    gbc_btnNew.gridx = 0;
    gbc_btnNew.gridy = 0;
    add(btnNew, gbc_btnNew);

    tabsLines = new JTabbedPane(JTabbedPane.TOP);
    GridBagConstraints gbc_tabsLines = new GridBagConstraints();
    gbc_tabsLines.gridheight = 5;
    gbc_tabsLines.insets = new Insets(0, 0, 5, 0);
    gbc_tabsLines.fill = GridBagConstraints.BOTH;
    gbc_tabsLines.gridx = 1;
    gbc_tabsLines.gridy = 0;
    add(tabsLines, gbc_tabsLines);

//    JButton btnImport = new JButton("Import");
//    GridBagConstraints gbc_btnImport = new GridBagConstraints();
//    gbc_btnImport.fill = GridBagConstraints.HORIZONTAL;
//    gbc_btnImport.insets = new Insets(0, 0, 5, 5);
//    gbc_btnImport.gridx = 0;
//    gbc_btnImport.gridy = 1;
//    add(btnImport, gbc_btnImport);

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

    btnRemove.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int idx = tabsLines.getSelectedIndex();
        if (idx >= 0) {
          tabsLines.remove(idx);
          linePanels.remove(idx);
        }
      }
    });
  }


  /**
   * Clear lines, loading a fresh set. If the currently selected line exists in both sets it is
   * reselected. TODO: Do as update behaviour to avoid computation and graphical glitches?
   * 
   * @param lines Lines to load into panel
   */
  public void loadLines(Collection<LineConfig> lines) {
    // Track current selected line in case of update
    LineConfig curLine = getSelectedLine();
    int scrPos = getSelectedScrollPosition();

    // Clear existing lines from tabs
    clearLines();
    // Load new lines as tabs
    if (lines != null) {

      for (LineConfig line : lines) {
        LinePanel pnlLine = new LinePanel(line);
        addLinePanel(line.identifier, pnlLine);
      }
    }

    // Reselect current line if it still exists
    showLineTab(curLine, scrPos);
  }

  /**
   * Get configurations for all lines currently being handled by the view.
   * 
   * @return Generated lines in tab order
   */
  public List<LineConfig> getLines() {
    List<LineConfig> lines = new ArrayList<>();
    for (LinePanel pnlLine : linePanels) {
      lines.add(pnlLine.getLine());
    }
    return lines;
  }

  /**
   * Add a new line panel, tracking accordingly for lookup.
   * 
   * @param identifier String identifier to use for panel
   * @param panel Panel to add
   */
  private void addLinePanel(String identifier, LinePanel panel) {
    linePanels.add(panel);
    tabsLines.addTab("", panel);
    setTabProperties(tabsLines.getTabCount() - 1, identifier);
  }

  /**
   * Configure the properties of a tab, specifically enforcing length rules on titles. As this may
   * cause ambiguity the tool tip is set to the full identifier.
   * 
   * @param idx Index of tab to modify
   * @param identifier String identifier to use for properties
   */
  private void setTabProperties(int idx, String identifier) {
    String title = identifier;
    if (title.length() >= MAX_TAB_TITLE_LEN) {
      title = identifier.substring(0, MAX_TAB_TITLE_LEN - 3) + "...";
    }
    tabsLines.setTitleAt(idx, title);
    tabsLines.setToolTipTextAt(idx, identifier);
  }

  /**
   * Display the tab holding the corresponding configuration. If the line configuration does not
   * exist in any tabs then no selection is made. Scroll position setting is also triggered on the
   * desired tab.
   * 
   * @param line Configuration to load corresponding tab for
   * @param scrPos Scroll position for tab if found
   */
  private void showLineTab(LineConfig line, int scrPos) {
    for (int i = 0; i < linePanels.size(); i++) {
      LinePanel pnlLine = linePanels.get(i);
      if (pnlLine.getLine().equals(line)) {
        tabsLines.setSelectedIndex(i);
        // Ensure panel sizing has time to complete for scrolling
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            pnlLine.getVerticalScrollBar().setValue(scrPos);
          }
        });
      }
    }
  }

  /**
   * @return The line generates from the currently displayed LinePanel instance
   */
  private LineConfig getSelectedLine() {
    LinePanel selected = getSelectedLinePanel();
    return (selected == null ? null : selected.getLine());
  }

  /**
   * @return The scroll position of the currently displayed LinePanel instance
   */
  private int getSelectedScrollPosition() {
    LinePanel selected = getSelectedLinePanel();
    return (selected == null ? 0 : selected.getVerticalScrollBar().getValue());
  }

  /**
   * @return The currently displayed tabs corresponding LinePanel instance
   */
  private LinePanel getSelectedLinePanel() {
    int idx = tabsLines.getSelectedIndex();
    return (idx >= 0 ? linePanels.get(idx) : null);
  }


  /**
   * Remove handling for all current lines.
   */
  private void clearLines() {
    linePanels = new ArrayList<>();
    tabsLines.removeAll();
  }

}
