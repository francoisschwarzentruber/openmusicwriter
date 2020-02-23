/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

/**
 *
 * @author Ancmin
 */
/** This class represents a curve defined by a sequence of control points */

public abstract class ControlCurve {

  protected Polygon pts;

  public ControlCurve() {
    pts = RegionFactory.createPolygon();
  }


  /** paint this curve into g.
   * @param g 
   */
  abstract public void paint(Graphics g);

  static final int EPSILON = 36;  /* square of distance for picking */



  // square of an int
  static int sqr(int x) {
    return x*x;
  }

  /** add a control point, return index of new control point */
  public void addPoint(int x, int y) {
    pts.addPoint(x,y);
  }



  abstract public Area getArea();
  
}

