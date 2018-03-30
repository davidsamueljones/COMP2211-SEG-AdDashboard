package group33.seg.view.controls;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import group33.seg.model.configs.BounceConfig;
import group33.seg.model.configs.BounceConfig.Type;

public class BounceDefinitionPanel extends JPanel {
  private static final long serialVersionUID = -4113181723451098077L;

  private JRadioButton radTime;
  private JSpinner nudTime;
  private JRadioButton radPageCount;
  private JSpinner nudPageCount;

  /**
   * Create the panel.
   */
  public BounceDefinitionPanel() {

    initGUI();
  }

  private void initGUI() {
    // Add border to panel
    this.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bounce Definition"));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWeights = new double[] {0.0, 0.0, 1.0};
    gridBagLayout.rowWeights = new double[] {0.0, 0.0};
    setLayout(gridBagLayout);

    ButtonGroup bgBounceRateType = new ButtonGroup();
    radTime = new JRadioButton("", true);
    bgBounceRateType.add(radTime);
    GridBagConstraints gbc_radTime = new GridBagConstraints();
    gbc_radTime.anchor = GridBagConstraints.WEST;
    gbc_radTime.insets = new Insets(5, 5, 5, 5);
    gbc_radTime.gridx = 0;
    gbc_radTime.gridy = 0;
    add(radTime, gbc_radTime);

    JLabel lblTime = new JLabel("Time (/s):");
    GridBagConstraints gbc_lblTime = new GridBagConstraints();
    gbc_lblTime.insets = new Insets(5, 0, 5, 5);
    gbc_lblTime.gridx = 1;
    gbc_lblTime.gridy = 0;
    add(lblTime, gbc_lblTime);

    nudTime = new JSpinner();
    GridBagConstraints gbc_nudTime = new GridBagConstraints();
    gbc_nudTime.fill = GridBagConstraints.HORIZONTAL;
    gbc_nudTime.insets = new Insets(5, 0, 5, 5);
    gbc_nudTime.gridx = 2;
    gbc_nudTime.gridy = 0;
    add(nudTime, gbc_nudTime);

    radPageCount = new JRadioButton("");
    bgBounceRateType.add(radPageCount);
    GridBagConstraints gbc_radPageCount = new GridBagConstraints();
    gbc_radPageCount.anchor = GridBagConstraints.WEST;
    gbc_radPageCount.insets = new Insets(0, 5, 0, 5);
    gbc_radPageCount.gridx = 0;
    gbc_radPageCount.gridy = 1;
    add(radPageCount, gbc_radPageCount);

    JLabel lblPageCount = new JLabel("Page Count:");
    GridBagConstraints gbc_lblPageCount = new GridBagConstraints();
    gbc_lblPageCount.insets = new Insets(0, 0, 0, 5);
    gbc_lblPageCount.gridx = 1;
    gbc_lblPageCount.gridy = 1;
    add(lblPageCount, gbc_lblPageCount);

    nudPageCount = new JSpinner();
    GridBagConstraints gbc_nudPageCount = new GridBagConstraints();
    gbc_nudPageCount.fill = GridBagConstraints.HORIZONTAL;
    gbc_nudPageCount.insets = new Insets(0, 0, 5, 5);
    gbc_nudPageCount.gridx = 2;
    gbc_nudPageCount.gridy = 1;
    add(nudPageCount, gbc_nudPageCount);


    lblPageCount.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        radPageCount.setSelected(true);
      }
    });
    nudPageCount.addChangeListener(e -> radPageCount.setSelected(true));

    lblTime.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        radTime.setSelected(true);
      }
    });
    nudTime.addChangeListener(e -> radTime.setSelected(true));

  }

  public void loadDef(BounceConfig bounceDef) {
    if (bounceDef == null || bounceDef.type == null) {
      clear();
      return;
    }
    switch (bounceDef.type) {
      case PAGES:
        nudPageCount.setValue(bounceDef.value);
        break;
      case TIME:
        nudTime.setValue(bounceDef.value);
        break;
      default:
        break;
    }   
  }
  
  public void clear() {
    nudTime.setValue(0);
    nudPageCount.setValue(0);
  }

  public BounceConfig getBounceDef() {
    BounceConfig config = new BounceConfig();   
    if (radPageCount.isSelected()) {
      config.type = Type.PAGES;
      config.value = (Integer) nudPageCount.getValue();
    } else if (radTime.isSelected()) {
      config.type = Type.TIME;
      config.value = (Integer) nudTime.getValue();
    }
    return config;
  }

}
