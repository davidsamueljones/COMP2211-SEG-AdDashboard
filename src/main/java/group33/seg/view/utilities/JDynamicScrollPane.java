package group33.seg.view.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Extension of a normal JScrollPane that can track objects which may change height if the width of
 * the content pane were to change. Listeners update the content pane's preferred dimensions in line
 * with these changes. Can be adapted to work horizontally as well if required.
 */
public class JDynamicScrollPane extends JScrollPane {
  private static final long serialVersionUID = -8637590249351476767L;

  /** List of components that change size */
  private Set<Component> dynamicComponents = new HashSet<>();
  private Set<DynamicComponentWatcher> watchers = new HashSet<>();

  private volatile boolean initialised = false;
  private volatile boolean deltaApplied = true;
  private volatile int lastHeight = 0;

  /**
   * Initialise the dynamic scroll pane.
   */
  public JDynamicScrollPane() {
    setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
  }

  /**
   * Create a listener on self to see when scroll area has changed. When this occurs the content
   * pane may need to be adjusted so trigger that here.
   */
  private void initListeners() {

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        doResize();
        // ` TODO: Maybe avoid doing all resize events
      }
    });
  }

  /**
   * Start tracking a new component. Size deltas between changes will be applied to content pane.
   * 
   * @param component Component to track
   */
  public void addDynamicComponent(Component component) {
    dynamicComponents.add(component);
    DynamicComponentWatcher watcher = new DynamicComponentWatcher(component.getPreferredSize());
    component.addComponentListener(watcher);
    watchers.add(watcher);
  }

  /**
   * First resize for width and then resize for height using deltas gather from width change. As
   * width change must first occur there may be graphical bugs before height is applied so hide the
   * scroll bar.
   */
  public void doResize() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {

        // Set width of content to that of view
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        setWidth();
        // Apply height deltas
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            applyHeightDelta();
            setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            // If this was the first time doResize was called, enable listeners
            if (!initialised) {
              initListeners();
              initialised = true;
              doResize();
            }
          }
        });
      }
    });
  }

  /**
   * Update content pane to have width of the scroll pane. The height will not be correct when this
   * function terminates.
   */
  public void setWidth() {
    if (deltaApplied) {
      // Record height before size change
      lastHeight = getViewport().getPreferredSize().height;
      deltaApplied = false;
    }
    final int indent = (((Integer) UIManager.get("ScrollBar.width")).intValue());
    int newWidth = getWidth() - indent;
    // Update width, being lenient on height
    // Height must be set to desired value with delta change
    viewport.getView().setPreferredSize(new Dimension(newWidth, Integer.MAX_VALUE));
  }

  /**
   * Using any delta information and the last true height. Set a new true height of the content
   * pane.
   */
  public void applyHeightDelta() {
    // Determine new height
    int dif = 0;
    for (DynamicComponentWatcher watcher : watchers) {
      dif += watcher.getDelta().height;
      watcher.clearDelta();
    }
    final int newHeight = lastHeight + dif;
    // Apply resize
    viewport.getView()
        .setPreferredSize(new Dimension(viewport.getView().getPreferredSize().width, newHeight));
    // Notify of cleared deltas
    deltaApplied = true;
  }

  /**
   * Class that record a component size changes as a delta. Should be added as a component listener.
   */
  public class DynamicComponentWatcher extends ComponentAdapter {
    Dimension delta = new Dimension(0, 0);
    Dimension last = null;

    /**
     * Instantiate a new component watcher.
     * 
     * @param initial Initial size to use as last
     */
    public DynamicComponentWatcher(Dimension initial) {
      last = initial;
    }

    @Override
    public synchronized void componentResized(ComponentEvent e) {
      Dimension temp = last;
      last = e.getComponent().getSize();
      if (temp == null) {
        return;
      }
      // Calculate difference and apply to delta
      Dimension dif = new Dimension(last.width - temp.width, last.height - temp.height);
      delta = new Dimension(delta.width + dif.width, delta.height + dif.height);
      // System.out.println(delta);
    }

    /**
     * @return Current delta
     */
    public synchronized Dimension getDelta() {
      return delta;
    }

    /**
     * Clears the delta value
     */
    public synchronized void clearDelta() {
      delta = new Dimension(0, 0);
    }

  }

}
