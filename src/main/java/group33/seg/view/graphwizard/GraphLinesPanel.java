package group33.seg.view.graphwizard;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import group33.seg.model.configs.LineConfig;

public class GraphLinesPanel extends JPanel {
  private static final long serialVersionUID = -1169530766129778297L;

  private final static int MAX_TAB_TITLE_LEN = 15;

  private JTabbedPane tabsLines;
  private List<LinePanel> linePanels;

  public GraphLinesPanel() {
    this(null);
  }

  public GraphLinesPanel(List<LineConfig> lines) {
    initGUI();
    updateLines(lines);
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

    tabsLines = new JTabbedPane(SwingConstants.TOP);
    GridBagConstraints gbc_tabsLines = new GridBagConstraints();
    gbc_tabsLines.gridheight = 5;
    gbc_tabsLines.insets = new Insets(0, 0, 5, 0);
    gbc_tabsLines.fill = GridBagConstraints.BOTH;
    gbc_tabsLines.gridx = 1;
    gbc_tabsLines.gridy = 0;
    add(tabsLines, gbc_tabsLines);

    // JButton btnImport = new JButton("Import");
    // GridBagConstraints gbc_btnImport = new GridBagConstraints();
    // gbc_btnImport.fill = GridBagConstraints.HORIZONTAL;
    // gbc_btnImport.insets = new Insets(0, 0, 5, 5);
    // gbc_btnImport.gridx = 0;
    // gbc_btnImport.gridy = 1;
    // add(btnImport, gbc_btnImport);

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
   * Handle line update, first ensuring any outdated lines are removed before applying updates and
   * adding new lines. This ensures the contents of the view will have line panels in corresponding
   * indexes to the input lines and only these line panels.
   * 
   * @param lines Lines to load into view
   */
  public void updateLines(List<LineConfig> lines) {
    removeOutdatedLines(lines);
    loadLines(lines);
  }

  /**
   * Do add/update behaviour for a set of line configurations. If a line configuration has an
   * existing line panel the contents of the panel is updated. Otherwise a new panel is created and
   * inserted in the index position.
   * 
   * @param lines Line configuration to use
   */
  private void loadLines(List<LineConfig> lines) {
    if (lines == null) {
      return;
    }
    for (int i = 0; i < lines.size(); i++) {
      LineConfig line = lines.get(i);
      LinePanel pnlLine = getLinePanel(line);
      if (pnlLine == null) {
        // New line behaviour
        pnlLine = new LinePanel(line);
        addLinePanel(line.identifier, pnlLine, i);
      } else {
        // Update behaviour
        pnlLine.loadLine(line);
      }
    }
  }

  /**
   * Remove any lines from the current view that do not exist in the latest configuration.
   * 
   * @param lines Lines in the new configuration
   */
  private void removeOutdatedLines(List<LineConfig> lines) {
    if (linePanels == null) {
      return;
    }
    // Iterate over all existing lines
    Iterator<LinePanel> itrLinePanels = linePanels.iterator();
    int idxTab = 0;
    while (itrLinePanels.hasNext()) {
      LinePanel pnlLine = itrLinePanels.next();
      // If line exists in old set but not in new set, remove it
      if (lines == null || !lines.contains(pnlLine.getLine())) {
        itrLinePanels.remove();
        tabsLines.remove(idxTab);
      }
      idxTab++;
    }
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
   * Add a line panel after all other current line panels, tracking accordingly for lookup.
   * 
   * @param identifier String identifier to use for panel
   * @param panel Panel to add
   */
  private void addLinePanel(String identifier, LinePanel panel) {
    addLinePanel(identifier, panel, tabsLines.getTabCount());
  }

  /**
   * Add a new line panel, tracking accordingly for lookup.
   * 
   * @param identifier String identifier to use for panel
   * @param panel Panel to add
   * @param idx Index for new line panel
   */
  private void addLinePanel(String identifier, LinePanel panel, int idx) {
    if (linePanels == null) {
      linePanels = new ArrayList<>();
    }
    if (idx > linePanels.size()) {
      // Insert behaviour
      linePanels.add(idx, panel);
      tabsLines.insertTab(null, null, panel, null, idx);
    } else {
      // Append behaviour
      linePanels.add(panel);
      tabsLines.add(panel);
    }
    setTabProperties(Math.min(linePanels.size(), idx), identifier);
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
   * For a given line configuration return it's corresponding line panel if it exists.
   * 
   * @param line Line configuration to search for
   * @return Corresponding panel, null if not found
   */
  private LinePanel getLinePanel(LineConfig line) {
    if (linePanels != null) {
      for (LinePanel pnlLine : linePanels) {
        if (line.equals(pnlLine.getLine())) {
          return pnlLine;
        }
      }
    }
    return null;
  }

  /**
   * Remove handling for all current lines.
   */
  private void clearLines() {
    linePanels = new ArrayList<>();
    tabsLines.removeAll();
  }

}
