/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

/**
 *
 * @author Ancmin
 */
public interface Rectangle {
    
    public float getHeight();
    public float getWidth();
    
    public float getMinX();
    public float getMaxX();
    public float getMaxY();
    public float getMinY();
    
    public float getCenterX();
    public float getCenterY();

    public void add(Rectangle rectangle);
    public void add(Point point);

    public void grow(float dx, float dy);

    public void setWidth(float max);

    public boolean contains(Point point);
    
   
    
    
    
}
