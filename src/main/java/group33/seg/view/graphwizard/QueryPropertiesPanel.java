package group33.seg.view.graphwizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import group33.seg.model.types.Interval;

public class QueryPropertiesPanel extends JPanel {



  public QueryPropertiesPanel() {
    initGUI();
  }

  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Query Properties"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {1.0};
    setLayout(gridBagLayout);
    
    JLabel lblInterval = new JLabel("Interval:");
    GridBagConstraints gbc_lblInterval = new GridBagConstraints();
    gbc_lblInterval.insets = new Insets(0, 0, 0, 5);
    gbc_lblInterval.anchor = GridBagConstraints.EAST;
    gbc_lblInterval.fill = GridBagConstraints.VERTICAL;
    gbc_lblInterval.gridx = 0;
    gbc_lblInterval.gridy = 0;
    add(lblInterval, gbc_lblInterval);

    JComboBox<Interval> cboInterval = new JComboBox<>();
    for (Interval interval : Interval.values()) {
      cboInterval.addItem(interval);   
    }
    GridBagConstraints gbc_cboInterval = new GridBagConstraints();
    gbc_cboInterval.fill = GridBagConstraints.HORIZONTAL;
    gbc_cboInterval.insets = new Insets(0, 0, 0, 0);
    gbc_cboInterval.gridx = 1;
    gbc_cboInterval.gridy = 0;
    add(cboInterval, gbc_cboInterval);
  }


}
