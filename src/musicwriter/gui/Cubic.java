/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

/**
 *
 * @author Ancmin
 */
/** this class represents a cubic polynomial */

public class Cubic {

  float a,b,c,d;         /* a + b*u + c*u^2 +d*u^3 */

  public Cubic(float a, float b, float c, float d){
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }


  /** evaluate cubic
   * @param u
   * @return  
   */
  public float eval(float u) {
    return (((d*u) + c)*u + b)*u + a;
  }
}

