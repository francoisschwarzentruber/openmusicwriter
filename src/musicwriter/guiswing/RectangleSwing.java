/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import musicwriter.gui.Point;
import musicwriter.gui.Rectangle;

/**
 *
 * @author Ancmin
 */
class RectangleSwing implements Rectangle {
    
    final Rectangle2D.Float rectangle;
    

    RectangleSwing(float xleft, float ytop, float width, float height) {
        rectangle = new Rectangle2D.Float(xleft, ytop, width, height);
    }

    RectangleSwing(Rectangle2D.Float aRectangle) {
        rectangle = aRectangle;
    }

    @Override
    public boolean contains(Point point) {
        return rectangle.contains(point.x, point.y);
    }

    public Rectangle2D.Float getRectangleSwing() {
        return rectangle;
    }

    @Override
    public float getHeight() {
        return rectangle.height;
    }

    @Override
    public float getWidth() {
        return rectangle.width;
    }

    @Override
    public float getMinX() {
        return rectangle.x;
    }

    @Override
    public float getMaxX() {
        return rectangle.x + rectangle.width;
    }

    @Override
    public float getMaxY() {
        return rectangle.y + rectangle.height;
    }

    @Override
    public float getMinY() {
        return rectangle.y;
    }

    @Override
    public float getCenterX() {
        return rectangle.x + rectangle.width/2;
    }

    @Override
    public float getCenterY() {
        return rectangle.y + rectangle.height/2;
    }

    @Override
    public void add(Rectangle rectangle2) {
        rectangle.add(((RectangleSwing) rectangle2).getRectangleSwing());
    }

    @Override
    public void add(Point point) {
        rectangle.add(new Point2D.Float(point.x, point.y));
    }

    @Override
    public void grow(float dx, float dy) {
        rectangle.x -= dx;
        rectangle.y -= dy;
        rectangle.width += 2*dx;
        rectangle.height += 2*dy;
    }

    @Override
    public void setWidth(float width) {
        rectangle.width = width;
    }
    
}
