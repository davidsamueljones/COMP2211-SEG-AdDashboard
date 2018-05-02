package group33.seg.view.definitions;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;

public class DefinitionFrame extends JFrame {
  private static final long serialVersionUID = -499578074273448909L;

  /**
   * Create the frame.
   */
  public DefinitionFrame() {
    initGUI();
  }

  private void initGUI() {
    this.setTitle("Definitions");
    this.setBounds(100, 100, 541, 401);
    DefinitionPanel pnlDefinitionPanel = new DefinitionPanel();
    Border border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
        pnlDefinitionPanel.getBorder());
    pnlDefinitionPanel.setBorder(border);
    setContentPane(pnlDefinitionPanel);
  }

}
