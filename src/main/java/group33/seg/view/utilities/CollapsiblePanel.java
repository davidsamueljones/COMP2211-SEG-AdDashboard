package group33.seg.view.utilities;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

public class CollapsiblePanel extends JPanel {
  private static final long serialVersionUID = -7454210705099631961L;

  private JXCollapsiblePane cp;
  private JButton btnToggle;

  public CollapsiblePanel() {
    this("Toggle Panel");
  }

  public CollapsiblePanel(String toggleText) {
    this(toggleText, null);
  }

  public CollapsiblePanel(String toggleText, Container pane) {
    initGUI(toggleText, pane);
  }

  private void initGUI(String toggleText, Container pane) {
    if (pane != null) {
      setContentPane(pane);
    }
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0};
    gridBagLayout.rowHeights = new int[] {0, 0};
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0};
    setLayout(gridBagLayout);

    // Configure toggle button using action
    btnToggle = new JButton();
    btnToggle.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_btnToggle = new GridBagConstraints();
    gbc_btnToggle.anchor = GridBagConstraints.NORTH;
    gbc_btnToggle.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnToggle.insets = new Insets(0, 0, 5, 0);
    gbc_btnToggle.gridx = 0;
    gbc_btnToggle.gridy = 0;
    add(btnToggle, gbc_btnToggle);

    cp = new JXCollapsiblePane();
    cp.setAnimated(false);
    GridBagConstraints gbc_cp = new GridBagConstraints();
    gbc_cp.insets = new Insets(0, 5, 0, 5);
    gbc_cp.fill = GridBagConstraints.BOTH;
    gbc_cp.gridx = 0;
    gbc_cp.gridy = 1;
    add(cp, gbc_cp);

    // Get toggle collapse action from panel
    Action toggleAction = cp.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
    // Apply collapse/expand icons to the action
    toggleAction.putValue(JXCollapsiblePane.COLLAPSE_ICON, UIManager.getIcon("Tree.expandedIcon"));
    toggleAction.putValue(JXCollapsiblePane.EXPAND_ICON, UIManager.getIcon("Tree.collapsedIcon"));

    btnToggle.setAction(toggleAction);
    btnToggle.setText(toggleText);
  }

  public JButton getToggleButton() {
    return btnToggle;
  }

  public JXCollapsiblePane getCollapsiblePane() {
    return cp;
  }

  public Container getContentPane() {
    return cp.getContentPane();
  }

  public void setContentPane(Container pane) {
    cp.setContentPane(pane);
  }
}
