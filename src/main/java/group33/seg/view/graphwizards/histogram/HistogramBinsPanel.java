package group33.seg.view.graphwizards.histogram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.view.output.HistogramView;

public class HistogramBinsPanel extends JPanel {
  private static final long serialVersionUID = -998060022279065856L;

  private JTabbedPane tabsBinModes;
  private JPanel pnlSimple;
  private JPanel pnlAdvanced;

  private JLabel lblSelectedBinColour;

  private JSpinner nudBinCount;

  private JButton btnResetBins;
  private JButton btnUseDefaults;
  private JPanel pnlBins;

  private List<BinPanel> bins = new ArrayList<>();

  private HistogramConfig base = null;


  /**
   * Create the panel.
   */
  public HistogramBinsPanel() {
    initGUI();
  }

  /**
   * Initialise GUI and any event listeners.
   */
  private void initGUI() {
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Histogram Bins"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0};
    gridBagLayout.rowHeights = new int[] {0, 0};
    gridBagLayout.columnWeights = new double[] {1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 1.0};
    setLayout(gridBagLayout);

    JPanel pnlColour = new JPanel();
    GridBagConstraints gbc_pnlColour = new GridBagConstraints();
    gbc_pnlColour.insets = new Insets(0, 0, 5, 5);
    gbc_pnlColour.fill = GridBagConstraints.BOTH;
    gbc_pnlColour.gridx = 0;
    gbc_pnlColour.gridy = 0;
    add(pnlColour, gbc_pnlColour);
    GridBagLayout gbl_pnlColour = new GridBagLayout();
    gbl_pnlColour.columnWidths = new int[] {0, 0, 0, 0};
    gbl_pnlColour.rowHeights = new int[] {0, 0};
    gbl_pnlColour.columnWeights = new double[] {0.0, 0.0, 0.0, 1.0};
    gbl_pnlColour.rowWeights = new double[] {0.0, Double.MIN_VALUE};
    pnlColour.setLayout(gbl_pnlColour);

    JLabel lblBinColour = new JLabel("Bin Colour:");
    GridBagConstraints gbc_lblBinColour = new GridBagConstraints();
    gbc_lblBinColour.anchor = GridBagConstraints.EAST;
    gbc_lblBinColour.insets = new Insets(0, 0, 5, 5);
    gbc_lblBinColour.gridx = 0;
    gbc_lblBinColour.gridy = 0;
    pnlColour.add(lblBinColour, gbc_lblBinColour);

    lblSelectedBinColour = new JLabel("Preview");
    lblSelectedBinColour.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)));
    lblSelectedBinColour.setOpaque(true);
    GridBagConstraints gbc_lblSelectedBackgroundColour = new GridBagConstraints();
    gbc_lblSelectedBackgroundColour.insets = new Insets(0, 0, 0, 5);
    gbc_lblSelectedBackgroundColour.gridx = 1;
    gbc_lblSelectedBackgroundColour.gridy = 0;
    pnlColour.add(lblSelectedBinColour, gbc_lblSelectedBackgroundColour);

    JButton btnSetColor = new JButton("Set");
    GridBagConstraints gbc_btnSetColor = new GridBagConstraints();
    gbc_btnSetColor.gridx = 2;
    gbc_btnSetColor.gridy = 0;
    pnlColour.add(btnSetColor, gbc_btnSetColor);


    JLabel lblHelp = new JLabel("Bins are configured as percentages of the full data range.");
    // FIXME: HTML + Resizing Panel does not play well (even with JDynamicScrollPane) so have to
    // have quite a simple
    // message...
    // JLabel lblHelp = new JLabel("<html>When displaying a histogram, bins are configured "
    // + "as percentages of the full data range. Add the required number of bins and "
    // + "configure the weights appropriately.</html>");
    GridBagConstraints gbc_lblHelp = new GridBagConstraints();
    gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 1;
    add(lblHelp, gbc_lblHelp);

    tabsBinModes = new JTabbedPane(SwingConstants.TOP);
    GridBagConstraints gbc_tabsBinModes = new GridBagConstraints();
    gbc_tabsBinModes.insets = new Insets(0, 0, 5, 5);
    gbc_tabsBinModes.fill = GridBagConstraints.BOTH;
    gbc_tabsBinModes.gridx = 0;
    gbc_tabsBinModes.gridy = 2;
    add(tabsBinModes, gbc_tabsBinModes);

    pnlSimple = new JPanel();
    pnlSimple.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabsBinModes.addTab("Simple", null, pnlSimple, null);
    GridBagLayout gbl_pnlSimple = new GridBagLayout();
    gbl_pnlSimple.columnWidths = new int[] {0, 0, 0};
    gbl_pnlSimple.rowHeights = new int[] {0, 0, 0};
    gbl_pnlSimple.columnWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
    gbl_pnlSimple.rowWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
    pnlSimple.setLayout(gbl_pnlSimple);

    JLabel lblBinCount = new JLabel("Bin Count:");
    GridBagConstraints gbc_lblBinCount = new GridBagConstraints();
    gbc_lblBinCount.insets = new Insets(0, 0, 5, 5);
    gbc_lblBinCount.gridx = 0;
    gbc_lblBinCount.gridy = 0;
    pnlSimple.add(lblBinCount, gbc_lblBinCount);

    SpinnerNumberModel model = new SpinnerNumberModel(20, 1, 1000, 1);
    nudBinCount = new JSpinner(model);
    GridBagConstraints gbc_nudBinCount = new GridBagConstraints();
    gbc_nudBinCount.insets = new Insets(0, 0, 5, 0);
    gbc_nudBinCount.gridx = 1;
    gbc_nudBinCount.gridy = 0;
    pnlSimple.add(nudBinCount, gbc_nudBinCount);

    pnlAdvanced = new JPanel();
    pnlAdvanced.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabsBinModes.addTab("Advanced", null, pnlAdvanced, null);
    GridBagLayout gbl_pnlAdvanced = new GridBagLayout();
    gbl_pnlAdvanced.columnWidths = new int[] {0, 0, 0};
    gbl_pnlAdvanced.rowHeights = new int[] {0, 0, 0};
    gbl_pnlAdvanced.columnWeights = new double[] {1.0, 1.0, Double.MIN_VALUE};
    gbl_pnlAdvanced.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
    pnlAdvanced.setLayout(gbl_pnlAdvanced);

    JButton btnAddBin = new JButton("Add Bin");
    GridBagConstraints gbc_btnAddBin = new GridBagConstraints();
    gbc_btnAddBin.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnAddBin.gridwidth = 2;
    gbc_btnAddBin.insets = new Insets(0, 0, 5, 0);
    gbc_btnAddBin.gridx = 0;
    gbc_btnAddBin.gridy = 0;
    pnlAdvanced.add(btnAddBin, gbc_btnAddBin);

    btnResetBins = new JButton("Reset Bins");
    GridBagConstraints gbc_btnResetBins = new GridBagConstraints();
    gbc_btnResetBins.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnResetBins.insets = new Insets(0, 0, 0, 5);
    gbc_btnResetBins.gridx = 0;
    gbc_btnResetBins.gridy = 1;
    pnlAdvanced.add(btnResetBins, gbc_btnResetBins);
    btnResetBins.setToolTipText("Reset the bins to use the last loaded values");

    btnUseDefaults = new JButton("Use Defaults");
    GridBagConstraints gbc_btnUseDefaults = new GridBagConstraints();
    gbc_btnUseDefaults.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnUseDefaults.gridx = 1;
    gbc_btnUseDefaults.gridy = 1;
    pnlAdvanced.add(btnUseDefaults, gbc_btnUseDefaults);
    btnUseDefaults.setToolTipText("Reset the bins to use the default bin allocations");

    pnlBins = new JPanel();
    pnlBins.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
    JScrollPane scrBins = new JScrollPane(pnlBins);
    scrBins.setPreferredSize(new Dimension(0, 150));
    GridBagConstraints gbc_pnlBins = new GridBagConstraints();
    gbc_pnlBins.insets = new Insets(5, 5, 5, 5);
    gbc_pnlBins.gridwidth = 2;
    gbc_pnlBins.fill = GridBagConstraints.BOTH;
    gbc_pnlBins.gridx = 0;
    gbc_pnlBins.gridy = 2;
    pnlAdvanced.add(scrBins, gbc_pnlBins);
    pnlBins.setLayout(new BoxLayout(pnlBins, BoxLayout.Y_AXIS));

    // Allow resetting of bins to their defaults
    btnUseDefaults.addActionListener(e -> {
      loadDefaultBins();
    });

    // Allow resetting of bins to last state
    btnResetBins.addActionListener(e -> {
      loadBins(base.bins);
    });

    // Allow addition of a new bin
    btnAddBin.addActionListener(e -> {
      if (bins.size() >= 20) {
        JOptionPane.showMessageDialog(null,
            "Unable to add new bin.\r\n" + "Maximum number of advanced bins is 20", "Add Bin Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        addBin(1);
      }
    });

    // Allow selection of a new colour
    btnSetColor.addActionListener(e -> {
      // Use JColorChooser, null returned on cancel
      Color color =
          JColorChooser.showDialog(null, "Bin Colour Chooser", lblBinColour.getBackground());
      if (color != null) {
        loadBinColour(color);
      }
    });

  }

  /**
   * @param config Configuration to load into the view object
   */
  public void loadGraph(HistogramConfig config) {
    if (config == null) {
      reset();
    } else {
      loadBinColour(config.barColor);
      if (config.bins == null) {
        tabsBinModes.setSelectedComponent(pnlSimple);
        nudBinCount.setValue(config.binCount);
      } else {
        tabsBinModes.setSelectedComponent(pnlAdvanced);
        loadBins(config.bins);
      }
      this.base = config;
      btnResetBins.setEnabled(this.base != null && config.bins != null);
    }
  }

  /**
   * Apply reset state to the view object.
   */
  public void reset() {
    loadBinColour(null);
    nudBinCount.setValue(20);
    loadDefaultBins();
    btnResetBins.setEnabled(false);
  }

  /**
   * Update corresponding fields of a given configuration using the view's respective field objects.
   * 
   * @param config Configuration to update
   */
  public void updateConfig(HistogramConfig config) {
    config.barColor = lblSelectedBinColour.getBackground();
    if (tabsBinModes.getSelectedComponent().equals(pnlSimple)) {
      config.binCount = (int) nudBinCount.getValue();
      config.bins = null;
    } else {
      config.bins = getBinValues();
    }
  }

  /**
   * Load the given selected colour into the colour handling object. This should generate an
   * appropriate preview.
   * 
   * @param bg Colour to use, if null use default
   */
  private void loadBinColour(Color bg) {
    if (bg == null) {
      bg = HistogramView.DEFAULT_FOREGROUND;
    }
    lblSelectedBinColour.setBackground(bg);
    lblSelectedBinColour.setForeground(bg);
  }

  /**
   * @param binVals Set of bins to load
   */
  private void loadBins(List<Integer> binVals) {
    clearBins();
    if (binVals != null) {
      for (Integer val : binVals) {
        addBin(val);
      }
    }
  }

  /**
   * Load the default set of bins.
   */
  private void loadDefaultBins() {
    clearBins();
    for (int i = 0; i < 4; i++) {
      addBin(1);
    }
  }

  /**
   * Clear all displayed bins.
   */
  private void clearBins() {
    while (!bins.isEmpty()) {
      removeBin(bins.get(0));
    }
  }

  /**
   * Add a new bin to the gui.
   */
  private void addBin(int weight) {
    BinPanel pnlBin = new BinPanel();
    pnlBin.nudValue.setValue(weight);
    bins.add(pnlBin);
    pnlBins.add(pnlBin);
    updateBins();
    pnlBin.nudValue.addChangeListener(e -> {
      updateBins();
    });
    pnlBin.btnRemove.addActionListener(e -> {
      if (bins.size() == 1) {
        JOptionPane.showMessageDialog(null,
            "Unable to remove bin.\r\n" + "Must have at least one bin.", "Remove Bin Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        removeBin(pnlBin);
      }
    });
    pnlBins.revalidate();
  }

  /**
   * Remove the given bin from the gui.
   * 
   * @param pnlBin Bin panel to remove
   */
  private void removeBin(BinPanel pnlBin) {
    bins.remove(pnlBin);
    pnlBins.remove(pnlBin);
    updateBins();
    pnlBins.revalidate();
  }

  /**
   * Update corresponding bin panel state values to be in line with the current number of bins and
   * selected weight.
   */
  private void updateBins() {
    List<Integer> binVals = getBinValues();
    List<Double> binNormalised = HistogramConfig.getNormalisedBins(binVals);
    int i = 0;
    for (BinPanel bin : bins) {
      bin.setBinNumber(i + 1);
      bin.setBinPercentage(binNormalised.get(i));
      i++;
    }
  }

  /**
   * @return The current weights in all displayed bins
   */
  private List<Integer> getBinValues() {
    List<Integer> binVals = new ArrayList<>();
    for (BinPanel bin : bins) {
      binVals.add(bin.getValue());
    }
    return binVals;
  }

  /**
   * Panel for configuring a single bin.
   */
  public class BinPanel extends JPanel {
    private static final long serialVersionUID = 6721751870896252173L;

    private JLabel lblBin;
    private JSpinner nudValue;
    private JLabel lblPercentage;
    private JButton btnRemove;

    /**
     * Initialise the panel.
     */
    public BinPanel() {
      initGUI();
    }

    /**
     * Initialise the panel GUI.
     */
    private void initGUI() {
      setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5),
          (BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
              BorderFactory.createEmptyBorder(2, 5, 2, 5)))));
      GridBagLayout gridBagLayout = new GridBagLayout();
      gridBagLayout.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
      gridBagLayout.rowHeights = new int[] {0};
      gridBagLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
      gridBagLayout.rowWeights = new double[] {0.0};
      setLayout(gridBagLayout);

      lblBin = new JLabel();
      lblBin.setFont(lblBin.getFont().deriveFont(Font.BOLD));
      GridBagConstraints gbc_lblBin = new GridBagConstraints();
      gbc_lblBin.insets = new Insets(0, 0, 0, 5);
      gbc_lblBin.gridx = 0;
      gbc_lblBin.gridy = 0;
      add(lblBin, gbc_lblBin);

      JLabel lblWeight = new JLabel("Weight:");
      GridBagConstraints gbc_lblWeight = new GridBagConstraints();
      gbc_lblWeight.insets = new Insets(0, 0, 0, 5);
      gbc_lblWeight.gridx = 1;
      gbc_lblWeight.gridy = 0;
      add(lblWeight, gbc_lblWeight);

      SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 1000, 1);
      nudValue = new JSpinner(model);
      Component nudValueEditor = nudValue.getEditor();
      JFormattedTextField valueFTF = ((JSpinner.DefaultEditor) nudValueEditor).getTextField();
      valueFTF.setColumns(3);

      GridBagConstraints gbc_nudValue = new GridBagConstraints();
      gbc_nudValue.insets = new Insets(0, 0, 0, 5);
      gbc_nudValue.gridx = 2;
      gbc_nudValue.gridy = 0;
      add(nudValue, gbc_nudValue);

      lblPercentage = new JLabel("");
      GridBagConstraints gbc_lblPercentage = new GridBagConstraints();
      gbc_lblPercentage.insets = new Insets(0, 0, 0, 5);
      gbc_lblPercentage.anchor = GridBagConstraints.EAST;
      gbc_lblPercentage.gridx = 3;
      gbc_lblPercentage.gridy = 0;
      add(lblPercentage, gbc_lblPercentage);

      btnRemove = new JButton("Remove");
      GridBagConstraints gbc_btnRemove = new GridBagConstraints();
      gbc_btnRemove.gridx = 4;
      gbc_btnRemove.gridy = 0;
      add(btnRemove, gbc_btnRemove);
    }

    /**
     * @return The currently selected weight for the bin
     */
    public int getValue() {
      try {
        return (Integer) nudValue.getValue();
      } catch (Exception e) {
        return 1;
      }
    }

    /**
     * @param percentage New percentage value
     */
    public void setBinPercentage(double percentage) {
      lblPercentage
          .setText(String.format("%-10s", String.format("(%d%%)", (int) (percentage * 100))));
    }

    /**
     * Update the displayed bin number
     * 
     * @param num New bin number
     */
    public void setBinNumber(int num) {
      lblBin.setText(String.format("Bin %2d | ", num));
    }

  }

}
