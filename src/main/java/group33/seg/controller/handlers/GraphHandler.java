package group33.seg.controller.handlers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import group33.seg.controller.DashboardController.DashboardMVC;
import group33.seg.model.configs.GraphConfig;
import group33.seg.model.configs.LineConfig;
import group33.seg.model.configs.LineGraphConfig;
import group33.seg.model.configs.MetricQuery;
import group33.seg.model.types.Interval;
import group33.seg.model.types.Metric;
import group33.seg.model.types.Pair;
import group33.seg.view.output.Graph;

public class GraphHandler {
  /** MVC model that sub-controller has knowledge of */
  private final DashboardMVC mvc;

  /** Graph being controlled */
  private Graph graphView;

  /** Currently loaded graph */
  private GraphConfig graph;

  /**
   * Instantiate a graph handler.
   * 
   * @param mvc Knowledge of full system as model view controller
   */
  public GraphHandler(DashboardMVC mvc) {
    this.mvc = mvc;
  }

  /**
   * @param graphView View to update when graph updates occur
   */
  public void setGraphView(Graph graphView) {
    this.graphView = graphView;
    // FIXME: Instead fetch theme here
    graphView.setFontScale(scale);
  }

  /**
   * Update the line graph displayed in the view. As an update, behaviour means that if the
   * currently displayed graph is the same as the input graph, only changes will be reflected. In
   * the case that the current graph does not exist or is different, behaviour is the same as that
   * of loadLineGraph.
   * 
   * @param graph Graph to update the view with
   */
  public void updateLineGraph(LineGraphConfig graph) {
    if (this.graph == null || !this.graph.uuid.equals(graph.uuid)) {
      // Load whole graph freshly
      loadLineGraph(graph);
    } else {
      // Update current graph
      setGraphProperties(graph);
      updateGraphLines(graph.lines);
      this.graph = graph;
    }
  }

  public void loadLineGraph(LineGraphConfig graph) {
    clearGraph();
    setGraphProperties(graph);
    loadGraphLines(graph.lines);
    // LineGraphConfig graph = wizard.getGraph();
    // if (graph != null) {
    // refreshGraphs(graph.identifier);
    // }
    // TODO
  }

  /**
   * Update the graph's view's properties, this includes information such as title and axis labels.
   * No data fetches should occur here.
   * 
   * @param graph Graph properties to load
   */
  public void setGraphProperties(GraphConfig graph) {
    // TODO: CALL VIEW UPDATES FOR TITLE/AXIS LABELS
  }

  /**
   * 
   * @param lines
   */
  public void updateGraphLines(List<LineConfig> lines) {
    // Get existing lines in graph
    List<LineConfig> exLines = null;
    if (this.graph instanceof LineGraphConfig) {
      exLines = ((LineGraphConfig) this.graph).lines;
    }
    if (exLines == null) {
      // No existing found, must load all
      loadGraphLines(lines);
    } else {
      // Remove existing lines that no longer exist or are hidden after update
      for (LineConfig exLine : exLines) {
        boolean remove = false;
        int idx = lines.indexOf(exLine);
        if (idx >= 0) {
          LineConfig line = lines.get(idx);
          if ((exLine.hide != line.hide) && line.hide) {
            remove = true;
          }
        } else {
          remove = true;
        }
        if (remove) {
          removeLine(exLine);
        }
      }
      // Update and add new lines
      for (LineConfig line : lines) {
        int idx = exLines.indexOf(line);
        if (idx >= 0) {
          addLine(line, getLineUpdate(exLines.get(idx), line));
        } else {
          addLine(line);
        }
      }
    }
  }



  public void loadGraphLines(List<LineConfig> lines) {
    for (LineConfig line : lines) {
      addLine(line);
    }

    // LineGraphConfig graph = wizard.getGraph();
    // if (graph != null) {
    // refreshGraphs(graph.identifier);
    // }
    // TODO
  }



  public void clearLines() {
    clearLines(null);
  }

  private void clearLines(List<LineConfig> lines) {

  }


  public void addLine(LineConfig line) {
    addLine(line, Update.FULL);
  }

  public void addLine(LineConfig line, Update update) {
    if (update == Update.FULL || update == Update.DATA) {
      // TODO: Query data and do line data update
    }
    if (update == Update.FULL || update == Update.PROPERTIES) {
      // TODO: Update properties
    }
  }

  public void removeLine(LineConfig line) {
    // TODO: Use the unique identifier to remove line from graph view
  }



  public void generateImpressionGraph(Interval interval) {
    DatabaseHandler db = new DatabaseHandler(null); // TODO: REMOVE ASAP
    MetricQuery query = new MetricQuery(Metric.IMPRESSIONS, interval, null);
    List<Pair<String, Integer>> data = db.getQueryResponse(query).getResult();

    graphView.addLine(data);
  }

  public void clearGraph() {
    graphView.clearLines();
  }

  // FIXME: Can remove when setting by theme
  private double scale = 1;

  public void setFontScale(double scale) {
    this.scale = scale;
  }

  /**
   * Given an original and an updated line configuration, determine what type of update is required
   * to update the graph in the least cost-manner. As in, if only line properties have changed it is
   * best to not requery data due to computational cost.
   * 
   * @param original Line configuration to use as base
   * @param updated Line configuration to compare with base
   * @return Update type as represented by Update enumeration
   */
  public static Update getLineUpdate(LineConfig original, LineConfig updated) {
    // Check for any changes in querying that may change data
    boolean data = true;
    data &= original.query.metric.equals(updated.query.metric);
    data &= original.query.interval.equals(updated.query.interval);
    data &= original.query.filter.isEquals(updated.query.filter);
    if (updated.query.metric.equals(Metric.BOUNCE_RATE)) {
      data &= original.query.bounceDef.isEquals(updated.query.bounceDef);
    }
    // Check for any changes of properties
    boolean properties = true;
    properties &= original.identifier.equals(updated.identifier);
    properties &= original.color.equals(updated.color);
    properties &= original.thickness == updated.thickness;
    properties &= original.hide == updated.hide;

    // Determine update required
    if (!(data || properties)) {
      return Update.FULL;
    }
    if (!data) {
      return Update.DATA;
    }
    if (!properties) {
      return Update.PROPERTIES;
    }
    return Update.NOTHING;
  }

  /**
   * Enumeration of what type of update has occurred to a graph or graph element.
   */
  public enum Update {
    FULL, /* Both properties and data have changed */
    PROPERTIES, /* Just properties have changed */
    DATA, /* Just data has changed */
    NOTHING; /* No changes */
  }

}
