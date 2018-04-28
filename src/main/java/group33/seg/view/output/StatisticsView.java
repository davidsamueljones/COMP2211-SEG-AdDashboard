package group33.seg.view.output;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import group33.seg.controller.DashboardController;
import group33.seg.controller.handlers.WorkspaceHandler.WorkspaceListener;
import group33.seg.lib.Pair;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.model.types.Metric;
import group33.seg.view.utilities.ProgressDialog;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class StatisticsView extends JPanel {
  private static final long serialVersionUID = 7755954237883396302L;
  private static final String HAS_STATISTICS = "HAS_STATISTICS";
  private static final String NO_STATISTICS = "NO_STATISTICS";

  private DashboardController controller;

  private CardLayout cardLayout;
  private JTable tblStatistics;
  private StatisticTableModel model_tblStatistics;

  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public StatisticsView(DashboardController controller) {
    this.controller = controller;
    // Configure so this is the view handled by the StatisticHandler
    controller.statistics.setView(this, true);
    initGUI();
    // Initialise the view
    clearStatistics();
    refreshView();
  }

  private void initGUI() {
    cardLayout = new CardLayout();
    setLayout(cardLayout);
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    model_tblStatistics = new StatisticTableModel();
    tblStatistics = new JTable(model_tblStatistics);
    tblStatistics.setShowGrid(true);
    tblStatistics.setGridColor(Color.LIGHT_GRAY);
    tblStatistics.getTableHeader().setDefaultRenderer(new StatisticHeaderRenderer());
    tblStatistics.setDefaultRenderer(Object.class, new ValueRenderer());
    // Initialise metrics as row headers for table
    ListModel<Metric> model_rowHeaders = new AbstractListModel<Metric>() {
      private static final long serialVersionUID = 6880290767512330693L;

      @Override
      public int getSize() {
        return Metric.getTypes().length;
      }

      @Override
      public Metric getElementAt(int index) {
        return Metric.getTypes()[index];
      }
    };
    JList<Metric> rowHeaders = new JList<>(model_rowHeaders);
    rowHeaders.setCellRenderer(new MetricHeaderRenderer());

    // Ensure row sizing is the same between headers and table
    final JLabel lblSize = new JLabel();
    int fixedHeight = (int) (lblSize.getFontMetrics(lblSize.getFont()).getHeight() * 1.1);
    rowHeaders.setFixedCellHeight(fixedHeight);
    tblStatistics.setRowHeight(fixedHeight);

    // Create scroll pane that also has LHS row headers
    JScrollPane scrStatistics = new JScrollPane(tblStatistics);
    scrStatistics.setRowHeaderView(rowHeaders);
    scrStatistics.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    add(scrStatistics, HAS_STATISTICS);

    JPanel pnlNoStatistics = new JPanel();
    add(pnlNoStatistics, NO_STATISTICS);
    GridBagLayout gbl_pnlNoStatistics = new GridBagLayout();
    gbl_pnlNoStatistics.columnWidths = new int[] {10, 0, 10};
    gbl_pnlNoStatistics.rowHeights = new int[] {0, 0, 0};
    gbl_pnlNoStatistics.columnWeights = new double[] {0.0, 1.0, 0.0};
    gbl_pnlNoStatistics.rowWeights = new double[] {0.5, 0.0, 0.5};
    pnlNoStatistics.setLayout(gbl_pnlNoStatistics);

    String strNoStatistics = "<html>There are no statistics to display.<br>"
        + "Create or show statistics using the 'Statistic Manager'.</html>";
    JLabel lblNoStatistics = new JLabel(strNoStatistics);
    lblNoStatistics.setEnabled(false);
    GridBagConstraints gbc_lblNoStatistics = new GridBagConstraints();
    gbc_lblNoStatistics.gridx = 1;
    gbc_lblNoStatistics.gridy = 1;
    pnlNoStatistics.add(lblNoStatistics, gbc_lblNoStatistics);
  }

  /**
   * Display the appropriate view depending on if there is statistics.
   */
  public void refreshView() {
    if (model_tblStatistics.getVisibleStatistics().isEmpty()) {
      cardLayout.show(this, NO_STATISTICS);
    } else {
      cardLayout.show(this, HAS_STATISTICS);
    }
  }

  /**
   * Add a new statistic to the view, if it already exists it will not be added again.
   * 
   * @param statistic Statistic to add
   * @return Whether statistic could be added
   */
  public boolean addStatistic(StatisticConfig statistic) {
    int idx = model_tblStatistics.getStatisticIndex(statistic);
    if (idx != -1) {
      return false;
    }
    // Add record (this will change the structure so redraw whole table)
    model_tblStatistics.statistics.add(new Pair<>(statistic, null));
    model_tblStatistics.fireTableStructureChanged();
    refreshView();
    return true;
  }

  /**
   * For a given statistic configuration, update the data displayed in the table entry.
   * 
   * @param statistic Statistic configuration for which to set the data for
   * @param data Mappings of metrics to values to display, the values used is the toString value so
   *        can be any type.
   * @return Whether the statistic existed in the view
   */
  public boolean setStatisticData(StatisticConfig statistic, Map<Metric, Double> data) {
    int idx = model_tblStatistics.getStatisticIndex(statistic);
    if (idx == -1) {
      return false;
    }
    // Update only data field (this may change data but will not change structure)
    model_tblStatistics.statistics.set(idx,
        new Pair<>(model_tblStatistics.statistics.get(idx).key, data));
    model_tblStatistics.fireTableDataChanged();
    return true;
  }

  /**
   * For a given statistic configuration, update its respective table entry using the configuration
   * properties.
   * 
   * @param statistic Statistic configuration for which to modify
   * @return Whether the statistic existed in the view
   */
  public boolean setStatisticProperties(StatisticConfig statistic) {
    int idx = model_tblStatistics.getStatisticIndex(statistic);
    if (idx == -1) {
      return false;
    }
    // Update only statistic field (this may change column titles so redraw whole table)
    model_tblStatistics.statistics.set(idx,
        new Pair<>(statistic, model_tblStatistics.statistics.get(idx).value));
    model_tblStatistics.fireTableStructureChanged();
    refreshView();
    return true;
  }

  /**
   * Remove the given statistic configuration from the table view.
   * 
   * @param statistic Statistic to remove
   * @return Whether the statistic was removed
   */
  public boolean removeStatistic(StatisticConfig statistic) {
    int idx = model_tblStatistics.getStatisticIndex(statistic);
    if (idx == -1) {
      return false;
    }
    // Remove record (this will change the structure so redraw whole table)
    model_tblStatistics.statistics.remove(idx);
    model_tblStatistics.fireTableStructureChanged();
    refreshView();
    return false;
  }

  /**
   * Remove all statistics from the view.
   */
  public void clearStatistics() {
    model_tblStatistics.statistics = new ArrayList<>();
    model_tblStatistics.fireTableStructureChanged();
    refreshView();
  }

  /**
   * Model used for displaying statistic list.
   */
  class StatisticTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -6483702839398100367L;

    /** List of statistics and their data displayed by model */
    private List<Pair<StatisticConfig, Map<Metric, Double>>> statistics = null;

    @Override
    public int getRowCount() {
      return Metric.getTypes().length;
    }

    @Override
    public int getColumnCount() {
      List<Pair<StatisticConfig, Map<Metric, Double>>> visible = getVisibleStatistics();
      return visible.size();
    }

    @Override
    public String getColumnName(int column) {
      List<Pair<StatisticConfig, Map<Metric, Double>>> visible = getVisibleStatistics();
      return visible.get(column).key.identifier;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Metric metric = Metric.getTypes()[rowIndex];
      Map<Metric, Double> values = getVisibleStatistics().get(columnIndex).value;
      return (values == null ? null : values.get(metric));
    }

    /**
     * Get the storage index for the given statistic.
     * 
     * @param statistic Statistic to find
     * @return Index of statistic's first occurrence or -1 if not found
     */
    public int getStatisticIndex(StatisticConfig statistic) {
      if (statistics != null) {
        int i = 0;
        for (Pair<StatisticConfig, Map<Metric, Double>> pair : statistics) {
          if (pair.key.equals(statistic)) {
            return i;
          }
          i++;
        }
      }
      return -1;
    }

    /**
     * @return All stored statistics that are not hidden
     */
    public List<Pair<StatisticConfig, Map<Metric, Double>>> getVisibleStatistics() {
      List<Pair<StatisticConfig, Map<Metric, Double>>> visible = new ArrayList<>();
      if (statistics != null) {
        for (Pair<StatisticConfig, Map<Metric, Double>> pair : statistics) {
          if (!pair.key.hide) {
            visible.add(pair);
          }
        }
      }
      return visible;
    }

  }

  /**
   * Helper class for rendering list cells as if they were table header cells. Also dispalys metric
   * definitions as tooltips.
   */
  class MetricHeaderRenderer implements ListCellRenderer<Metric> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Metric> list, Metric metric,
        int index, boolean isSelected, boolean cellHasFocus) {
      TableCellRenderer header = new JTableHeader().getDefaultRenderer();
      Component c = header.getTableCellRendererComponent(null, metric, false, false, 0, 0);
      if (c instanceof JComponent) {
        JComponent c1 = (JComponent) c;
        c1.setToolTipText(String.format("<html><p width=\"250\">%s</p></html>", metric.definition));
        return c1;
      }
      return c;
    }
  }

  /**
   * Helper class for rendering table header cells without acknowledging table draw behaviour
   * (messes with LaF). Also displays statistic configurations in text form as tooltips.
   */
  class StatisticHeaderRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      TableCellRenderer header = new JTableHeader().getDefaultRenderer();
      Component c =
          header.getTableCellRendererComponent(null, value, isSelected, hasFocus, row, column);
      if (c instanceof JComponent) {
        JComponent c1 = (JComponent) c;
        StatisticConfig statistic = model_tblStatistics.getVisibleStatistics().get(column).key;
        c1.setToolTipText(String.format("<html>%s</html>", statistic.inText()));
        return c1;
      }
      return c;
    }
  }

  /**
   * Renderer that displays the full value on mouseover, helpful for long values.
   */
  class ValueRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 7893477071189445275L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      Component c =
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      if (c instanceof JComponent) {
        JComponent c1 = (JComponent) c;
        c1.setToolTipText(value == null ? "" : value.toString());
        return c1;
      }
      return c;
    }

  }

}
