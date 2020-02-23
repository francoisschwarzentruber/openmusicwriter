/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import musicwriter.gui.Area;
import musicwriter.gui.Point;
import musicwriter.gui.Polygon;
import musicwriter.gui.Rectangle;

/**
 *
 * @author Ancmin
 */
class PolygonSwing extends Polygon {

    final java.awt.Polygon polygonSwing;
    
    public PolygonSwing() {
        polygonSwing = new java.awt.Polygon();
        
    }

    public java.awt.Polygon getPolygonSwing() {
        return polygonSwing;
    }

    @Override
    public void addPoint(float x, float y) {
        polygonSwing.addPoint((int) x, (int) y);
    }


    public boolean contains(Point point) {
        return polygonSwing.contains(point.x, point.y);
    }

    @Override
    public boolean intersects(Rectangle rectangle) {
        return polygonSwing.intersects(((RectangleSwing) rectangle).rectangle);
    }

    @Override
    public int getNbPoints() {
        return polygonSwing.npoints;
    }

    @Override
    public float getX(int i) {
        return polygonSwing.xpoints[i];
    }

    @Override
    public float getY(int i) {
        return polygonSwing.ypoints[i];
    }

    @Override
    public int[] getXs() {
        return polygonSwing.xpoints;
    }

    @Override
    public int[] getYs() {
        return polygonSwing.ypoints;
    }

    


    
    
    
    
}
