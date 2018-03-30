package group33.seg.view.controls;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXDatePicker;
import org.scijava.swing.checkboxtree.CheckBoxNodeData;
import org.scijava.swing.checkboxtree.CheckBoxNodeEditor;
import org.scijava.swing.checkboxtree.CheckBoxNodeRenderer;
import group33.seg.model.configs.FilterConfig;
import group33.seg.model.utilities.Range;
import java.awt.Component;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FilterSettingsPanel extends JPanel {
  private static final long serialVersionUID = -6373337720735697409L;

  private JTree tree;
  private JXDatePicker dtpStartDate;
  private JXDatePicker dtpEndDate;

  // Associations of filtering types to corresponding tree nodes
  private Map<FilterConfig.Age, DefaultMutableTreeNode> ages;
  private Map<FilterConfig.Gender, DefaultMutableTreeNode> genders;
  private Map<FilterConfig.Income, DefaultMutableTreeNode> incomes;
  private Map<FilterConfig.Context, DefaultMutableTreeNode> contexts;
  private JButton btnStartDateClear;
  private JButton btnEndDateClear;

  public FilterSettingsPanel() {
    this(null);
  }

  public FilterSettingsPanel(FilterConfig filter) {
    initGUI();
    initialiseTree(tree, filter);
    if (filter != null && filter.dates != null) {
      dtpStartDate.setDate(filter.dates.min);
      dtpEndDate.setDate(filter.dates.max);
    }
  }

  private void initGUI() {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.rowWeights = new double[] {1.0, 0.0, 0.0};
    gridBagLayout.columnWeights = new double[] {0.0, 1.0, 0.0};
    setLayout(gridBagLayout);

    tree = new JTree();
    JScrollPane scrTree = new JScrollPane(tree);
    scrTree.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    GridBagConstraints gbc_tree = new GridBagConstraints();
    gbc_tree.gridwidth = 3;
    gbc_tree.insets = new Insets(0, 0, 5, 5);
    gbc_tree.fill = GridBagConstraints.BOTH;
    gbc_tree.gridy = 0;
    gbc_tree.gridx = 0;
    add(scrTree, gbc_tree);

    JLabel lblStartDate = new JLabel("Start Date:");
    GridBagConstraints gbc_lblStartDate = new GridBagConstraints();
    gbc_lblStartDate.insets = new Insets(0, 0, 5, 5);
    gbc_lblStartDate.gridx = 0;
    gbc_lblStartDate.gridy = 1;
    add(lblStartDate, gbc_lblStartDate);

    dtpStartDate = new JXDatePicker();
    GridBagConstraints gbc_dtpStartDate = new GridBagConstraints();
    gbc_dtpStartDate.fill = GridBagConstraints.HORIZONTAL;
    gbc_dtpStartDate.insets = new Insets(0, 0, 5, 5);
    gbc_dtpStartDate.gridx = 1;
    gbc_dtpStartDate.gridy = 1;
    add(dtpStartDate, gbc_dtpStartDate);

    btnStartDateClear = new JButton("Clear");
    btnStartDateClear.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dtpStartDate.setDate(null);
      }
    });
    GridBagConstraints gbc_btnStartDateClear = new GridBagConstraints();
    gbc_btnStartDateClear.insets = new Insets(0, 0, 5, 0);
    gbc_btnStartDateClear.gridx = 2;
    gbc_btnStartDateClear.gridy = 1;
    add(btnStartDateClear, gbc_btnStartDateClear);

    JLabel lblEndDate = new JLabel("End Date:");
    GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
    gbc_lblEndDate.insets = new Insets(0, 0, 0, 5);
    gbc_lblEndDate.gridx = 0;
    gbc_lblEndDate.gridy = 2;
    add(lblEndDate, gbc_lblEndDate);

    dtpEndDate = new JXDatePicker();
    GridBagConstraints gbc_dtpEndDate = new GridBagConstraints();
    gbc_dtpEndDate.insets = new Insets(0, 0, 0, 5);
    gbc_dtpEndDate.fill = GridBagConstraints.HORIZONTAL;
    gbc_dtpEndDate.gridx = 1;
    gbc_dtpEndDate.gridy = 2;
    add(dtpEndDate, gbc_dtpEndDate);

    btnEndDateClear = new JButton("Clear");
    btnEndDateClear.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dtpEndDate.setDate(null);
      }
    });
    GridBagConstraints gbc_btnEndDateClear = new GridBagConstraints();
    gbc_btnEndDateClear.gridx = 2;
    gbc_btnEndDateClear.gridy = 2;
    add(btnEndDateClear, gbc_btnEndDateClear);
  }

  /**
   * Load filterConfig enumerations into tree. Check values based on filterConfig config.
   * 
   * @param tree Tree to configure/initialise
   * @param filter Current filterConfig to load
   */
  private void initialiseTree(JTree tree, FilterConfig filter) {
    // Sub-methods expect non-null config
    if (filter == null) {
      filter = new FilterConfig();
    }
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    // Create and fill filterConfig categories
    DefaultMutableTreeNode tnAge = new DefaultMutableTreeNode("Age");
    ages = addTreeCheckboxes(tnAge, FilterConfig.Age.class,
        Arrays.asList(FilterConfig.Age.values()), filter.ages);
    root.add(tnAge);
    
    DefaultMutableTreeNode tnGender = new DefaultMutableTreeNode("Gender");
    genders = addTreeCheckboxes(tnGender, FilterConfig.Gender.class,
        Arrays.asList(FilterConfig.Gender.values()), filter.genders);
    root.add(tnGender);

    DefaultMutableTreeNode tnIncome = new DefaultMutableTreeNode("Income");
    incomes = addTreeCheckboxes(tnIncome, FilterConfig.Income.class,
        Arrays.asList(FilterConfig.Income.values()), filter.incomes);
    root.add(tnIncome);

    DefaultMutableTreeNode tnContext = new DefaultMutableTreeNode("Context");
    contexts = addTreeCheckboxes(tnContext, FilterConfig.Context.class,
        Arrays.asList(FilterConfig.Context.values()), filter.contexts);
    root.add(tnContext);

    // Create tree model
    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    tree.setModel(treeModel);
    // Fully expand tree
    tree.expandRow(0);
    tree.expandPath(new TreePath(tnAge.getPath()));
    tree.expandPath(new TreePath(tnGender.getPath()));
    tree.expandPath(new TreePath(tnIncome.getPath()));
    tree.expandPath(new TreePath(tnContext.getPath()));
    // Set tree properties
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.setCellEditor(new CheckBoxNodeEditor(tree));
    tree.setEditable(true);

    // Create custom renderer that does not display folder icons on parents
    tree.setCellRenderer(new CheckBoxNodeRenderer() {
      DefaultTreeCellRenderer parentRenderer = new DefaultTreeCellRenderer();
      {
        parentRenderer.setClosedIcon(null);
        parentRenderer.setOpenIcon(null);
      }

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
          boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp;
        if (leaf) {
          comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row,
              hasFocus);
        } else {
          comp = parentRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf,
              row, hasFocus);
        }
        return comp;
      }
    });

  }

  /**
   * Add many checkbox leafs to a parent node. If a checked collection is not provided all
   * checkboxes will be unchecked, otherwise a respective leaf will be checked if it exists in the
   * collection.
   * 
   * @param parent Parent to add leaves to
   * @param type Class type of values
   * @param values Values to add as leaves
   * @param checked Values to set as selected, ignored if null
   * @return Mapping of values and their corresponding checkbox node
   */
  private static <T> Map<T, DefaultMutableTreeNode> addTreeCheckboxes(DefaultMutableTreeNode parent,
      Class<T> type, Collection<T> values, Collection<T> checked) {
    Map<T, DefaultMutableTreeNode> mappings = new HashMap<>();
    for (T value : values) {
      boolean isChecked = (checked == null ? true : checked.contains(value));
      DefaultMutableTreeNode node = addTreeCheckbox(parent, value.toString(), isChecked);
      mappings.put(value, node);
    }
    return mappings;
  }

  /**
   * Add a checkbox leaf to a parent node using the given text and state.
   * 
   * @param parent Parent to add leaf to
   * @param data Data to store with respective node
   * @param checked Whether checkbox should be selected
   * @return Generated node for data
   */
  private static DefaultMutableTreeNode addTreeCheckbox(DefaultMutableTreeNode parent, String text,
      boolean checked) {
    CheckBoxNodeData stored = new CheckBoxNodeData(text, checked);
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(stored);
    parent.add(node);
    return node;
  }

  /**
   * Using the current tree configuration and date components, create a corresponding filterConfig
   * configuration.
   * 
   * @return Generated filterConfig
   */
  public FilterConfig generateFilter() {
    FilterConfig filter = new FilterConfig();
    // Process tree
    filter.ages = processFilterCategory(FilterConfig.Age.class, ages);
    filter.genders = processFilterCategory(FilterConfig.Gender.class, genders);
    filter.incomes = processFilterCategory(FilterConfig.Income.class, incomes);
    filter.contexts = processFilterCategory(FilterConfig.Context.class, contexts);
    // Process dates
    Date start = dtpStartDate.getDate();
    Date end = dtpEndDate.getDate();
    if (start != null || end != null) {
      filter.dates = new Range<>(start, end);
    }
    
    return filter;
  }

  /**
   * For a given set of mappings, find values that should be included in a filterConfig.
   * 
   * @param type Type of values associated with nodes
   * @param mappings Values and their corresponding nodes
   * @return Values which have a node that is checked, null if all nodes are checked
   */
  private <T> Collection<T> processFilterCategory(Class<T> type, Map<T, DefaultMutableTreeNode> mappings) {
    // Create a set of checked values in the filterConfig
    Collection<T> inFilter = new ArrayList<>();
    for (Entry<T, DefaultMutableTreeNode> entry : mappings.entrySet()) {
      if (isNodeChecked(entry.getValue())) {
        inFilter.add(entry.getKey());
      }
    }
    // No filterConfig need be applied
    if (inFilter.size() == mappings.size()) {
      return null;
    }
    // A filterConfig has been applied
    return inFilter;
  }

  /**
   * Check for a given tree node (provided that it is in fact a checkbox node), whether or not it is
   * in a checked state.
   * 
   * @param node Node to check
   * @return Whether node is checked
   */
  private boolean isNodeChecked(DefaultMutableTreeNode node) {
    Object obj = node.getUserObject();
    if (obj instanceof CheckBoxNodeData) {
      CheckBoxNodeData cnd = (CheckBoxNodeData) obj;
      return cnd.isChecked();
    } else {
      return false;
    }
  }

}
