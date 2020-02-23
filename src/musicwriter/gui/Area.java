/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

/**
 *
 * @author Ancmin
 */
public interface Area {
    
    public boolean contains(Point point);

    public void add(Area area);

    public void intersect(Area intersectedWithThis);

    public boolean contains(Rectangle rectangle);

    public boolean isEmpty();

    public Rectangle getBounds();

    public boolean intersects(Rectangle rectangle);
}
