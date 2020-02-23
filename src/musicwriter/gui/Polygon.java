/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

/**
 *
 * @author Ancmin
 */
public abstract class Polygon {

    abstract public void addPoint(float x, float y);

    abstract public boolean intersects(Rectangle rectangle);

    abstract public int getNbPoints();
    abstract public float getX(int i);
    abstract public float getY(int i);

    abstract public int[] getXs();
    abstract public int[] getYs();

    abstract public boolean contains(Point pt);
    
}
