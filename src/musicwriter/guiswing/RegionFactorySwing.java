/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import musicwriter.gui.Area;
import musicwriter.gui.Polygon;
import musicwriter.gui.Rectangle;
import musicwriter.gui.RegionFactory;

/**
 *
 * @author Ancmin
 */
public class RegionFactorySwing extends RegionFactory {

    
    static public void createInstance()
    {
        regionFactory = new RegionFactorySwing();
    }
        
        
    @Override
    protected Polygon createPolygonImpl() {
        return new PolygonSwing();
    }

    @Override
    protected Rectangle createRectangleImpl(float xleft, float ytop, float width, float height) {
        return new RectangleSwing(xleft, ytop, width, height);
    }

    @Override
    protected Area createEmptyRegionImpl() {
        return new RegionSwing();
    }

    @Override
    protected Area createRegionImpl(Rectangle rectangle) {
        return new RegionSwing(((RectangleSwing) rectangle).getRectangleSwing());
    }

    @Override
    protected Area createRegionImpl(Polygon polygon) {
        return new RegionSwing(((PolygonSwing) polygon).getPolygonSwing());
    }

    @Override
    protected Area createEllipseImpl(float xleft, float ytop, float width, float height) {
        return new RegionSwing(new Ellipse2D.Float(xleft, ytop, width, height));
    }


    
    
    
    
    
}
