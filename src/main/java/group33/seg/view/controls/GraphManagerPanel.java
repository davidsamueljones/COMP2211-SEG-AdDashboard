package group33.seg.view.controls;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.border.BevelBorder;
import group33.seg.controller.DashboardController;
import group33.seg.model.configs.GraphConfig;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class GraphManagerPanel extends JPanel {
  private static final long serialVersionUID = 6541885932864334941L;

  private DefaultListModel<GraphConfig> mdl_lstGraphs;

  private JButton btnRemove;
  private JButton btnViewModify;

  private JButton btnLoad;
  private JButton btnNew;

  /**
   * Create the panel.
   * 
   * @param controller
   */
  public GraphManagerPanel(DashboardController controller) {
    initGUI();
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0};
    setLayout(gridBagLayout);

    btnNew = new JButton("New");
    GridBagConstraints gbc_btnNew = new GridBagConstraints();
    gbc_btnNew.insets = new Insets(0, 0, 5, 0);
    gbc_btnNew.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNew.gridx = 0;
    gbc_btnNew.gridy = 0;
    add(btnNew, gbc_btnNew);
    
    JPanel pnlExisting = new JPanel();
    pnlExisting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Existing Graphs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    GridBagConstraints gbc_pnlExisting = new GridBagConstraints();
    gbc_pnlExisting.insets = new Insets(0, 5, 5, 5);
    gbc_pnlExisting.fill = GridBagConstraints.BOTH;
    gbc_pnlExisting.gridx = 0;
    gbc_pnlExisting.gridy = 1;
    add(pnlExisting, gbc_pnlExisting);
    GridBagLayout gbl_pnlExisting = new GridBagLayout();
    gbl_pnlExisting.columnWeights = new double[]{1.0, 1.0};
    gbl_pnlExisting.rowWeights = new double[] {1.0, 0.0, 0.0};
    pnlExisting.setLayout(gbl_pnlExisting);
    
    mdl_lstGraphs = new DefaultListModel<>();
    JList<GraphConfig> lstGraphs = new JList<>(mdl_lstGraphs);
    JScrollPane scrGraphs = new JScrollPane(lstGraphs);
    scrGraphs.setPreferredSize(new Dimension(0, scrGraphs.getPreferredSize().height));
    scrGraphs.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_lstGraphs = new GridBagConstraints();
    gbc_lstGraphs.gridwidth = 2;
    gbc_lstGraphs.insets = new Insets(5, 5, 5, 5);
    gbc_lstGraphs.fill = GridBagConstraints.BOTH;
    gbc_lstGraphs.gridx = 0;
    gbc_lstGraphs.gridy = 0;
    pnlExisting.add(scrGraphs, gbc_lstGraphs);

    btnRemove = new JButton("Remove");
    GridBagConstraints gbc_btnRemove = new GridBagConstraints();
    gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnRemove.insets = new Insets(0, 5, 5, 2);
    gbc_btnRemove.gridx = 0;
    gbc_btnRemove.gridy = 1;
    pnlExisting.add(btnRemove, gbc_btnRemove);

    btnViewModify = new JButton("View/Modify");
    GridBagConstraints gbc_btnViewModify = new GridBagConstraints();
    gbc_btnViewModify.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnViewModify.insets = new Insets(0, 2, 5, 5);
    gbc_btnViewModify.gridx = 1;
    gbc_btnViewModify.gridy = 1;
    pnlExisting.add(btnViewModify, gbc_btnViewModify);

    btnLoad = new JButton("Load");
    GridBagConstraints gbc_btnLoad = new GridBagConstraints();
    gbc_btnLoad.insets = new Insets(0, 0, 0, 0);
    gbc_btnLoad.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnLoad.gridwidth = 2;
    gbc_btnLoad.gridx = 0;
    gbc_btnLoad.gridy = 2;
    pnlExisting.add(btnLoad, gbc_btnLoad);
  }

}
