package group33.seg.view.utilities;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import group33.seg.model.types.Metric;

public class DefinitionPanel extends JDynamicScrollPane {
  private static final long serialVersionUID = -7792003666831327347L;

  public DefinitionPanel() {
    initGUI();
  }

  private void initGUI() {
    JPanel pnlContent = new JPanel();
    this.setViewportView(pnlContent);

    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.rowHeights = new int[] {0, 0};
    gbl_pnlContent.columnWeights = new double[] {1.0};
    gbl_pnlContent.rowWeights = new double[] {0.0, 1.0,};
    pnlContent.setLayout(gbl_pnlContent);

    JLabel lblDefinitions = new JLabel();
    lblDefinitions.setText(generateDefinitionString());
    GridBagConstraints gbc_lblDefinitions = new GridBagConstraints();
    gbc_lblDefinitions.fill = GridBagConstraints.BOTH;
    gbc_lblDefinitions.insets = new Insets(5, 5, 5, 5);
    gbc_lblDefinitions.gridx = 0;
    gbc_lblDefinitions.gridy = 0;
    pnlContent.add(lblDefinitions, gbc_lblDefinitions);

    // Attach dynamic components
    this.addDynamicComponent(lblDefinitions);
    // Trigger resize
    this.doResize();
  }

  /**
   * Present all metric definitions against their respective metric as a formatted html
   * string.
   * 
   * @return All metric definitions as a formatted string
   */
  private static String generateDefinitionString() {
    StringBuilder builder = new StringBuilder();
    builder.append("<html><h2><b>Definitions:</b></h2>");
    // builder.append("<p>");
    for (Metric metric : Metric.values()) {
      builder.append("<p style='margin-top:5'");
      builder.append("<b>" + metric + ":</b> ");
      builder.append(metric.definition);
      builder.append("</p>");
    }
    // builder.append("</p>");
    builder.append("</html>");
    return builder.toString();
  }

}
