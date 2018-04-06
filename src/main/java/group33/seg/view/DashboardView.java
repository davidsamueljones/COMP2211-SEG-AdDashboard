package group33.seg.view;

import group33.seg.view.structure.DashboardFrame;
import group33.seg.view.utilities.DefinitionFrame;

public class DashboardView {
  private DashboardFrame dashboard = null;
  private DefinitionFrame definitions = null;
   
  public DashboardFrame getDashboard() {
    return dashboard;
  }
  
  public void setDashboard(DashboardFrame dashboard) {
    this.dashboard = dashboard;
  }

  public DefinitionFrame getDefinitions() {
    return definitions;
  }

  public void setDefinitions(DefinitionFrame definitions) {
    this.definitions = definitions;
  }
  
}
