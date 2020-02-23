/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

/**
 *
 * @author Ancmin
 */
public abstract class RegionFactory {
    
    static protected RegionFactory regionFactory;
    
    
    static public void createInstance()
    {
        
    }
    
    
    static public Polygon createPolygon()
    {
        return regionFactory.createPolygonImpl();
    }

    public static Area createEmptyRegion() {
        return regionFactory.createEmptyRegionImpl();
    }

    public static Area createRegion(Polygon polygon) {
        return regionFactory.createRegionImpl(polygon);
    }

    public static Area createRegion(Rectangle rectangle) {
        return regionFactory.createRegionImpl(rectangle);
    }

    public static Area createEllipse(float xleft, float ytop, float width, float height) {
        return regionFactory.createEllipseImpl(xleft, ytop, width, height);
    }

    protected abstract Polygon createPolygonImpl();
    
    
    static public Rectangle createRectangle(float xleft, float ytop, float width, float height)
    {
        return regionFactory.createRectangleImpl(xleft, ytop, width, height);
    }

    protected abstract Rectangle createRectangleImpl(float xleft, float ytop, float width, float height);

    protected abstract Area createEmptyRegionImpl();
    protected abstract Area createRegionImpl(Rectangle rectangle);
    protected abstract Area createRegionImpl(Polygon polygon);

    protected abstract Area createEllipseImpl(float xleft, float ytop, float width, float height);
    
}
