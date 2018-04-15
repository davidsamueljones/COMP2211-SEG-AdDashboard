package group33.seg.view.utilities;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import group33.seg.controller.utilities.ProgressListener;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.SwingConstants;

public class ProgressDialog extends JDialog {
  private static final long serialVersionUID = -861297834109653112L;

  private JLabel lblUpdates;

  public final ProgressListener listener = new ProgressListener() {

    @Override
    public void start() {
      SwingUtilities.invokeLater(() -> {
        setVisible(true);
      });
    };

    @Override
    public void progressUpdate(String update) {
      SwingUtilities.invokeLater(() -> {
        lblUpdates.setText(String.format("<html>%s</html>", update));
      });
    }

    @Override
    public void finish(boolean success) {
      SwingUtilities.invokeLater(() -> {
        setVisible(false);
      });
    };

  };

  /**
   * Create the dialog.
   *
   * @param parent Window to treat as a parent
   */
  public ProgressDialog(Window parent) {
    super(parent, "Progress...");


    // Initialise GUI
    initGUI();

    // Determine positioning
    if (parent != null) {
      setLocationRelativeTo(parent);
    } else {
      setLocation(100, 100);
    }
    pack();
    // Apply dialog properties
    setModalityType(ModalityType.APPLICATION_MODAL);
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
  }

  private void initGUI() {
    JPanel pnlContent = new JPanel();
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWidths = new int[] {10, 0, 10};
    gbl_pnlContent.columnWeights = new double[] {0, 1.0, 0};
    gbl_pnlContent.rowWeights = new double[] {0.5, 0.0, 0.0, 0.5};
    pnlContent.setLayout(gbl_pnlContent);
    pnlContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    this.setContentPane(pnlContent);
    
    lblUpdates = new JLabel("...");
    int height = lblUpdates.getFontMetrics(lblUpdates.getFont()).getHeight();
    lblUpdates.setHorizontalAlignment(SwingConstants.CENTER);
    gbl_pnlContent.rowHeights = new int[] {0, height * 3, 0, 15};
    GridBagConstraints gbc_lblUpdates = new GridBagConstraints();
    gbc_lblUpdates.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblUpdates.insets = new Insets(0, 20, 5, 20);
    gbc_lblUpdates.gridx = 1;
    gbc_lblUpdates.gridy = 1;
    pnlContent.add(lblUpdates, gbc_lblUpdates);

    JProgressBar progressBar = new JProgressBar();
    progressBar.setPreferredSize(new Dimension(350, progressBar.getPreferredSize().height));
    progressBar.setIndeterminate(true);
    GridBagConstraints gbc_progressBar = new GridBagConstraints();
    gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
    gbc_progressBar.gridx = 1;
    gbc_progressBar.gridy = 2;
    pnlContent.add(progressBar, gbc_progressBar);
  }

}
