package group33.seg.controller.graphing;

import java.awt.Color;

public class XYLine {

  public XYData[] data;
  public Color color;
  public String ID;

  public XYLine(){
    
  }
  public XYLine(XYData[] data){
    this.data=data;
  }
  
  public XYLine(XYData[] data,String ID){
    this.data=data;
    this.ID=ID;
  }
  
  public XYLine(XYData[] data,String ID, Color color){
    this.data=data;
    this.ID=ID;
    this.color=color;
  }
  
  public XYData[] getData() {
    return data;
  }

  public Color getColor() {
    return color;
  }

  public String getID() {
    return ID;
  }

}
