package group33.seg.view.graphwizards.histogram;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import group33.seg.model.configs.HistogramConfig;
import group33.seg.view.output.GraphsView;
import group33.seg.view.output.HistogramView;
import group33.seg.view.output.LineGraphView;
import group33.seg.view.utilities.JDynamicScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;

public class HistogramBinsPanel extends JPanel {
  private static final long serialVersionUID = -998060022279065856L;

  private JLabel lblSelectedBinColour;
  private JPanel pnlBins;
  private JButton btnResetBins;
  private JButton btnUseDefaults;

  private List<BinPanel> bins = new ArrayList<BinPanel>();

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
    gridBagLayout.columnWidths = new int[] {0, 0};
    gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[] {1.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
    setLayout(gridBagLayout);

    JPanel pnlColour = new JPanel();
    GridBagConstraints gbc_pnlColour = new GridBagConstraints();
    gbc_pnlColour.insets = new Insets(0, 0, 5, 0);
    gbc_pnlColour.fill = GridBagConstraints.BOTH;
    gbc_pnlColour.gridwidth = 2;
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
    lblSelectedBinColour.setToolTipText(
        "<html>The background colour is the main background colour of the graph.<br>"
            + "The text colour is the guideline colour.</html>");
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
    gbc_lblHelp.gridwidth = 2;
    gbc_lblHelp.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblHelp.insets = new Insets(0, 0, 5, 0);
    gbc_lblHelp.gridx = 0;
    gbc_lblHelp.gridy = 1;
    add(lblHelp, gbc_lblHelp);

    JButton btnAddBin = new JButton("Add Bin");
    GridBagConstraints gbc_btnAddBin = new GridBagConstraints();
    gbc_btnAddBin.gridwidth = 2;
    gbc_btnAddBin.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnAddBin.insets = new Insets(0, 0, 5, 0);
    gbc_btnAddBin.gridx = 0;
    gbc_btnAddBin.gridy = 2;
    add(btnAddBin, gbc_btnAddBin);

    btnResetBins = new JButton("Reset Bins");
    btnResetBins.setToolTipText("Reset the bins to use the last loaded values");
    GridBagConstraints gbc_btnResetBins = new GridBagConstraints();
    gbc_btnResetBins.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnResetBins.insets = new Insets(0, 0, 5, 5);
    gbc_btnResetBins.gridx = 0;
    gbc_btnResetBins.gridy = 3;
    add(btnResetBins, gbc_btnResetBins);

    btnUseDefaults = new JButton("Use Defaults");
    btnUseDefaults.setToolTipText("Reset the bins to use the default bin allocations");
    GridBagConstraints gbc_btnUseDefaults = new GridBagConstraints();
    gbc_btnUseDefaults.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnUseDefaults.insets = new Insets(0, 0, 5, 0);
    gbc_btnUseDefaults.gridx = 1;
    gbc_btnUseDefaults.gridy = 3;
    add(btnUseDefaults, gbc_btnUseDefaults);

    pnlBins = new JPanel();
    // pnlBins.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
    // BorderFactory.createEmptyBorder(2, 0, 2, 0)));
    GridBagConstraints gbc_pnlBins = new GridBagConstraints();
    gbc_pnlBins.gridwidth = 2;
    gbc_pnlBins.fill = GridBagConstraints.BOTH;
    gbc_pnlBins.gridx = 0;
    gbc_pnlBins.gridy = 4;
    add(pnlBins, gbc_pnlBins);
    pnlBins.setLayout(new BoxLayout(pnlBins, BoxLayout.Y_AXIS));


    // Allow selection of a new colour
    btnSetColor.addActionListener(e -> {
      // Use JColorChooser, null returned on cancel
      Color color =
          JColorChooser.showDialog(null, "Bin Colour Chooser", lblBinColour.getBackground());
      if (color != null) {
        loadBinColour(color);
      }
    });

    // Allow addition of a new bin
    btnAddBin.addActionListener(e -> {
      if (bins.size() >= 20) {
        JOptionPane.showMessageDialog(null,
            "Unable to add new bin.\r\n" + "Maximum number of bins is 20 bins", "Add Bin Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        addBin(1);
      }
    });

    // Allow resetting of bins to last state
    btnResetBins.addActionListener(e -> {
      loadBins(base.bins);
    });

    // Allow resetting of bins to their defaults
    btnUseDefaults.addActionListener(e -> {
      loadDefaultBins();
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
      this.base = config;
      btnResetBins.setEnabled(this.base != null);
    }
  }

  /**
   * Apply reset state to the view object.
   */
  public void reset() {
    loadBinColour(null);
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
    config.bins = getBinValues();
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
    Color fg = GraphsView.getGridlineColor(bg);
    lblSelectedBinColour.setBackground(bg);
    lblSelectedBinColour.setForeground(fg);
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
      removeBin(pnlBin);
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
    pnlBins.revalidate();
    updateBins();
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
