package group33.seg.view.structure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.StatisticConfig;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;

public class WorkspaceStatisticsPanel extends JPanel {
  private static final long serialVersionUID = 7755954237883396302L;

  private DashboardController controller;

  private JTable tblStatistics;
  private StatisticTableModel model_tblStatistics;

  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public WorkspaceStatisticsPanel(DashboardController controller) {
    this.controller = controller;

    initGUI();
    // Initialise the view
    clearStatistics();
    // Configure so this is the view handled by the StatisticHandler
    controller.statistics.setView(this, true);
  }

  private void initGUI() {
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setLayout(new BorderLayout());
   
    model_tblStatistics = new StatisticTableModel();
    tblStatistics = new JTable(model_tblStatistics);
    tblStatistics.setShowGrid(true);
    tblStatistics.setGridColor(Color.LIGHT_GRAY);
    tblStatistics.getTableHeader().setDefaultRenderer(new ColumnHeaderRenderer());

    // Initialise metrics as row headers for table
    ListModel<Metric> model_rowHeaders = new AbstractListModel<Metric>() {
      private static final long serialVersionUID = 6880290767512330693L;

      @Override
      public int getSize() {
        return Metric.values().length;
      }

      @Override
      public Metric getElementAt(int index) {
        return Metric.values()[index];
      }
    };
    JList<Metric> rowHeaders = new JList<Metric>(model_rowHeaders);
    rowHeaders.setCellRenderer(new RowHeaderRenderer());

    // Ensure row sizing is the same between headers and table
    int fixedHeight = tblStatistics.getRowHeight() + tblStatistics.getRowMargin();
    rowHeaders.setFixedCellHeight(fixedHeight);
    tblStatistics.setRowHeight(fixedHeight);

    // Create scroll pane that also has LHS row headers
    JScrollPane scrStatistics = new JScrollPane(tblStatistics);
    scrStatistics.setRowHeaderView(rowHeaders);
    scrStatistics.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    add(scrStatistics, BorderLayout.CENTER);    
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
  public boolean setStatisticData(StatisticConfig statistic, Map<Metric, Object> data) {
    int idx = model_tblStatistics.getStatisticIndex(statistic);
    if (idx == -1) {
      return false;
    }
    // Update only data field (this may change data but will not change structure)
    model_tblStatistics.statistics.set(idx,
        new Pair<>(model_tblStatistics.statistics.get(idx).key, data));
    model_tblStatistics.fireTableStructureChanged();
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
    // TODO: ADD HIDE FUNCTIONALITY
    model_tblStatistics.fireTableStructureChanged();
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
    return false;
  }

  /**
   * Remove all statistics from the view.
   */
  public void clearStatistics() {
    model_tblStatistics.statistics = new ArrayList<>();
    model_tblStatistics.fireTableStructureChanged();
  }

  /**
   * Model used for displaying statistic list.
   */
  class StatisticTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -6483702839398100367L;

    /** List of statistics and their data displayed by model */
    private List<Pair<StatisticConfig, Map<Metric, Object>>> statistics = null;

    @Override
    public int getRowCount() {
      return Metric.values().length;
    }

    @Override
    public int getColumnCount() {
      return (statistics == null ? 1 : statistics.size());
    }

    @Override
    public String getColumnName(int column) {
      return (statistics == null ? "" : statistics.get(column).key.identifier);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Metric metric = Metric.values()[rowIndex];
      Map<Metric, Object> values = (statistics == null ? null : statistics.get(columnIndex).value);
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
        for (Pair<StatisticConfig, Map<Metric, Object>> pair : statistics) {
          if (pair.key.equals(statistic)) {
            return i;
          }
          i++;
        }
      }
      return -1;
    }
  }

  /**
   * Helper class for rendering list cells as if they were table header cells.
   */
  class RowHeaderRenderer implements ListCellRenderer<Object> {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
      TableCellRenderer header = new JTableHeader().getDefaultRenderer();
      return header.getTableCellRendererComponent(null, value, false, false, 0, 0);
    }
  }

  /**
   * Helper class for rendering table header cells without acknowledging table draw behaviour
   * (messes with LaF).
   */
  class ColumnHeaderRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      TableCellRenderer header = new JTableHeader().getDefaultRenderer();
      return header.getTableCellRendererComponent(null, value, isSelected, hasFocus, row, column);
    }
  }

}
