/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import musicwriter.gui.Area;
import musicwriter.gui.Point;
import musicwriter.gui.Rectangle;

/**
 *
 * @author Ancmin
 */
class RegionSwing implements Area {

    private final java.awt.geom.Area area;
    
    

    
    public RegionSwing(java.awt.Shape e) {
        area = new java.awt.geom.Area(e);
    }

    RegionSwing() {
        area = new java.awt.geom.Area();
    }

    @Override
    public boolean contains(Point point) {
        return area.contains(point.x, point.y);
    }

    @Override
    public void add(Area area2) {
        area.add(((RegionSwing) area2).getAreaSwing());
    }

    @Override
    public void intersect(Area area2) {
        area.intersect(((RegionSwing) area2).getAreaSwing());
    }

    @Override
    public boolean contains(Rectangle rectangle) {
        return area.contains(((RectangleSwing) rectangle).getRectangleSwing());
    }

    @Override
    public boolean isEmpty() {
        return area.isEmpty();
    }

    @Override
    public Rectangle getBounds() {
        java.awt.geom.Rectangle2D r = area.getBounds();
        return new RectangleSwing((float) r.getX(), (float) r.getY(), (float) r.getWidth(), (float) r.getHeight());
    }

    @Override
    public boolean intersects(Rectangle rectangle) {
        return area.intersects(((RectangleSwing) rectangle).getRectangleSwing());
    }

    private java.awt.geom.Area getAreaSwing() {
        return area;
    }
    
}
