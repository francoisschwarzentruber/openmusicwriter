/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.ElementMusicalCrescendoDecrescendo;
import musicwriter.gui.Area;
import musicwriter.gui.Graphics;
import musicwriter.gui.Polygon;
import musicwriter.gui.RegionFactory;
/**
 *
 * @author Ancmin
 */
public class AffichageElementMusicalCrescendoDecrescendo extends AffichageElementMusicalMultiCurseurs {

    public AffichageElementMusicalCrescendoDecrescendo(Systeme systeme,
                                                       ElementMusicalCrescendoDecrescendo element)
    {
        super(systeme, element);
    }

    public double getX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getXFin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

    public void setX(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void draw(Graphics g) {
        g.setPenWidth(1.3f);
        g.drawLine((int) getPoint0().x, (int) getPoint0().y,
                    (int) getPoint1().x, (int) (getPoint1().y - getSysteme().getNoteRayon(getPortee())));
        g.drawLine((int) getPoint0().x, (int) getPoint0().y,
                    (int) getPoint1().x, (int) (getPoint1().y + getSysteme().getNoteRayon(getPortee())));
    }

    @Override
    public Area getArea() {
        Polygon p = RegionFactory.createPolygon();
        p.addPoint((int) getPoint0().x-5, (int) getPoint0().y-5);
        p.addPoint((int) getPoint0().x-5, (int) getPoint0().y+5);
        p.addPoint((int) getPoint1().x+5, (int) getPoint1().y+5);
        p.addPoint((int) getPoint1().x+5, (int) getPoint1().y-5);
        return RegionFactory.createRegion(p);
    }



}
