package group33.seg.view.structure;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import group33.seg.controller.DashboardController;

public class WorkspaceStatisticsPanel extends JPanel {
  private static final long serialVersionUID = 7755954237883396302L;

  private DashboardController controller;

  /**
   * Create the panel.
   * 
   * @param controller Controller for this view object
   */
  public WorkspaceStatisticsPanel(DashboardController controller) {
    this.controller = controller;

    initGUI();
  }

  private void initGUI() {
	  
	  setLayout(new BorderLayout(0, 0));
	  TableModel dataModel = new AbstractTableModel() {
		  private String [] columnNames = {"Metric Type",
				                           "Age",
				                           "Gender",
				                           "Income",
				                           "Context",
				                           "Start Date",
				                           "End Date",
				                           "Matric"};
		  private Object[][] data = {
				  {"Kathy", "Smith", "Smith", "Smith", "Smith", "Snowboarding", new Integer(5), new Boolean(false)},
				  {"John", "Doe", "Smith", "Smith", "Smith", "Rowing", new Integer(3), new Boolean(true)},		
		                            };
          public int getColumnCount() { return 8; }
          public int getRowCount() { return 11;}

          public String getColumnName(int col) {
              return columnNames[col];
          }
     //     public Object getValueAt(int row, int col) {
     //         return data[row][col];
     //     }
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return null;
		}

      };
      JTable table = new JTable(dataModel);
      JScrollPane scrollpane = new JScrollPane(table);
      table.setValueAt(9, 2, 2);
      add(scrollpane, BorderLayout.CENTER);
  }

}
