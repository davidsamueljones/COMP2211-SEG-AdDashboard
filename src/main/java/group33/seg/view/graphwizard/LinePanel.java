package group33.seg.view.graphwizard;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LinePanel extends JScrollPane {

  public LinePanel() {
    initGUI();
  }
  
  public void initGUI() {
    JPanel pnlMain = new JPanel();
    pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagLayout gbl_pnlMain = new GridBagLayout();
    gbl_pnlMain.rowHeights = new int[] {0, 0, 0, 0};
    gbl_pnlMain.columnWeights = new double[] {1.0};
    gbl_pnlMain.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    pnlMain.setLayout(gbl_pnlMain);
    
    this.setViewportView(pnlMain);
    
    LinePropertiesPanel pnlLineProperties = new LinePropertiesPanel();
    GridBagConstraints gbc_pnlLineProperties = new GridBagConstraints();
    gbc_pnlLineProperties.fill = GridBagConstraints.BOTH;
    gbc_pnlLineProperties.insets = new Insets(0, 0, 5, 0);
    gbc_pnlLineProperties.gridx = 0;
    gbc_pnlLineProperties.gridy = 0;
    pnlMain.add(pnlLineProperties, gbc_pnlLineProperties);
    
    LineDataPanel pnlLineData = new LineDataPanel();
    GridBagConstraints gbc_pnlLineData = new GridBagConstraints();
    gbc_pnlLineData.fill = GridBagConstraints.BOTH;
    gbc_pnlLineData.insets = new Insets(0, 0, 0, 0);
    gbc_pnlLineData.gridx = 0;
    gbc_pnlLineData.gridy = 1;
    pnlMain.add(pnlLineData, gbc_pnlLineData);
    
    
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.getVerticalScrollBar().setUnitIncrement(10);
  }
  
}
