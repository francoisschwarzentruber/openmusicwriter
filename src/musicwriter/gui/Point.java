/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

/**
 *
 * @author Ancmin
 */
public class Point {
    final public float x;
    final public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double distance(Point point) {
        return Math.sqrt(distanceSq(point));
    }

    public double distanceSq(Point point) {
        return (x - point.x) * (x - point.x) + (y - point.y) * (y - point.y);
    }
    
     
          
}
