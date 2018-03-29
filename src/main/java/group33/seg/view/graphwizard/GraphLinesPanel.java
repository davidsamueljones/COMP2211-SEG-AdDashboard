package group33.seg.view.graphwizard;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import group33.seg.controller.utilities.DashboardUtilities;
import group33.seg.model.configs.LineConfig;

public class GraphLinesPanel extends JPanel {
  private static final long serialVersionUID = -1169530766129778297L;
  private final static int MAX_TAB_TITLE_LEN = 15;
  
  /** Randomiser for line colours */
  private Random random = new Random();
  
  protected JTabbedPane tabsLines;
  protected List<LinePanel> linePanels;

  /**
   * Initialise the panel.
   */
  public GraphLinesPanel() {
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    // ************************************************************************************
    // * GUI HANDLING
    // ************************************************************************************

    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlMain = new GridBagLayout();
    gbl_pnlMain.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    gbl_pnlMain.columnWeights = new double[] {0.0, 1.0};
    setLayout(gbl_pnlMain);

    JButton btnNew = new JButton("New Line");
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

    JButton btnRemove = new JButton("Remove Line");
    GridBagConstraints gbc_btnRemove = new GridBagConstraints();
    gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnRemove.insets = new Insets(0, 0, 0, 5);
    gbc_btnRemove.gridx = 0;
    gbc_btnRemove.gridy = 2;
    add(btnRemove, gbc_btnRemove);

    // ************************************************************************************
    // * EVENT HANDLING
    // ************************************************************************************

    // Create a new line in the graph
    btnNew.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LinePanel pnlLine = new LinePanel();
        pnlLine.pnlLineProperties.txtIdentifier
            .setText(String.format("Line %d", tabsLines.getTabCount() + 1));
        pnlLine.pnlLineProperties.setColor(randomLineColor());
        addLinePanel("Line ", pnlLine);
        tabsLines.setSelectedComponent(pnlLine);
      }
    });

    // Remove currently selected line from graph
    btnRemove.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int res = JOptionPane.showConfirmDialog(GraphLinesPanel.this,
            "Are you sure you want to remove the current line?", "Remove Line",
            JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) {
          return;
        }
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
      Integer idx = getLineTabIndex(line);
      if (idx == -1) {
        // New line behaviour
        LinePanel pnlLine = new LinePanel(line);
        addLinePanel(line.identifier, pnlLine, i);
      } else {
        // Update behaviour
        LinePanel pnlLine = linePanels.get(idx);
        pnlLine.loadLine(line);
        setTabProperties(idx, line.identifier);
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
    if (linePanels != null) {
      for (LinePanel pnlLine : linePanels) {
        lines.add(pnlLine.getLine());
      }
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
  private void addLinePanel(String identifier, LinePanel pnlLine, int idx) {
    // Enforce panel behaviour when part of hierarchy
    JTextField txtIdentifier = pnlLine.pnlLineProperties.txtIdentifier;
    txtIdentifier.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setTabProperties(Math.min(linePanels.size(), idx), txtIdentifier.getText());
      }
    });
    DashboardUtilities.focusRequest(txtIdentifier);

    // Insert panel into hierarchy
    if (linePanels == null) {
      linePanels = new ArrayList<>();
    }
    if (idx > linePanels.size()) {
      // Insert behaviour
      linePanels.add(idx, pnlLine);
      tabsLines.insertTab(null, null, pnlLine, null, idx);
    } else {
      // Append behaviour
      linePanels.add(pnlLine);
      tabsLines.add(pnlLine);
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
   * For a given line configuration find it's corresponding line panel if it exists using this as a
   * tab index.
   * 
   * @param line Line configuration to search for
   * @return Corresponding panel, null if not found
   */
  private int getLineTabIndex(LineConfig line) {
    if (linePanels != null) {
      for (int i = 0; i < linePanels.size(); i++) {
        LinePanel pnlLine = linePanels.get(i);
        if (line.equals(pnlLine.getLine())) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Remove handling for all current lines.
   */
  public void clearLines() {
    linePanels = null;
    tabsLines.removeAll();
  }

  /**
   * @return A random, bright non-saturated colour that can be used as the default for a line.
   */
  private Color randomLineColor() {
    float hue = random.nextFloat();
    float saturation = 0.9f;
    float luminance = 0.9f;
    return Color.getHSBColor(hue, saturation, luminance);
  }
  
}
