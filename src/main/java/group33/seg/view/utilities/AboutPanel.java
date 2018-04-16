package group33.seg.view.utilities;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutPanel extends JPanel {
  private static final long serialVersionUID = 1012812580706515903L;

  private static final String CREATORS = "<b>Group 33</b><br>David Jones, Iliana Hadzhiatanasova, "
      + "Harry Brown, Simeon Milev, Kiera Spencer-Hayles, Ina Li";

  private JLabel lblGroup;

  public AboutPanel() {
    initGUI();
  }

  private void initGUI() {
    GridBagLayout gbl_pnlContent = new GridBagLayout();
    gbl_pnlContent.columnWidths = new int[] {0, 0};
    gbl_pnlContent.rowHeights = new int[] {0, 0, 0, 0};
    gbl_pnlContent.columnWeights = new double[] {0.0, 1.0};
    gbl_pnlContent.rowWeights = new double[] {0.5, 0.0, 0.0, 0.5};
    setLayout(gbl_pnlContent);

    JPanel pnlIcon = new JPanel() {
      private static final long serialVersionUID = -7685274924778291165L;
      private Image img = null;
      private Dimension imgSize;

      {
        try {
          final String strImg = "/icons/main/icon128.png";
          BufferedImage bufImg = ImageIO.read(getClass().getResource(strImg));
          imgSize = new Dimension(bufImg.getWidth(), bufImg.getHeight());
          img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(strImg));
          this.setPreferredSize(imgSize);
          this.setMinimumSize(imgSize);
        } catch (Exception e) {
          System.err.println("Unable to load icon");
        }
      }

      @Override
      protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D) g2;
        if (img != null) {
          int x = (this.getSize().width - imgSize.width) / 2;
          int y = (this.getSize().height - imgSize.height) / 2;
          g.drawImage(img, x, y, this);
        }
      }
    };
    pnlIcon.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc_pnlIcon = new GridBagConstraints();
    gbc_pnlIcon.fill = GridBagConstraints.BOTH;
    gbc_pnlIcon.gridheight = 4;
    gbc_pnlIcon.gridx = 0;
    gbc_pnlIcon.gridy = 0;
    add(pnlIcon, gbc_pnlIcon);

    JLabel lblAdDashboard = new JLabel("<html><b>Ad-Dashboard</b></html>");
    Accessibility.scaleJComponentFontSize(lblAdDashboard, 1.25);
    GridBagConstraints gbc_lblAdDashboard = new GridBagConstraints();
    gbc_lblAdDashboard.anchor = GridBagConstraints.SOUTHWEST;
    gbc_lblAdDashboard.insets = new Insets(5, 5, 5, 0);
    gbc_lblAdDashboard.gridx = 1;
    gbc_lblAdDashboard.gridy = 1;
    add(lblAdDashboard, gbc_lblAdDashboard);

    lblGroup = new JLabel();
    GridBagConstraints gbc_lblGroup = new GridBagConstraints();
    gbc_lblGroup.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblGroup.anchor = GridBagConstraints.NORTH;
    gbc_lblGroup.insets = new Insets(0, 5, 5, 0);
    gbc_lblGroup.gridx = 1;
    gbc_lblGroup.gridy = 2;
    add(lblGroup, gbc_lblGroup);

    // Adjust fixed width based off current label width
    lblGroup.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        loadCreatorText();
      }
    });
    loadCreatorText();
  }

  private void loadCreatorText() {
    int width = (int) (lblGroup.getSize().width * 0.7);
    lblGroup.setText(String.format("<html><p width='%dpx'>%s</p></html>", width, CREATORS));
  }

}
