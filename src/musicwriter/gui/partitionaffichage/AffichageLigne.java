/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;


import musicwriter.gui.*;
import musicwriter.donnees.ElementMusicalLigne;
import musicwriter.gui.RegionFactory;

/**
 *
 * @author Ancmin
 */
public class AffichageLigne extends AffichageElementMusicalMultiCurseurs {

    public AffichageLigne(Systeme systeme, ElementMusicalLigne element)
    {
        super(systeme, element);
    }


    public ElementMusicalLigne getElementMusicalLigne()
    {
        return (ElementMusicalLigne) getElementMusical();
    }

    public double getX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setX(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getXFin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }






    public void draw(Graphics g) {
        g.setPenWidth(1.5f);
        g.drawLine((int) getPoint0().x, (int) getPoint0().y,
                    (int) getPoint1().x, (int) getPoint1().y);
    }


    
    public Area getArea() {
        Polygon p = RegionFactory.createPolygon();
        p.addPoint((int) getPoint0().x-5, (int) getPoint0().y-5);
        p.addPoint((int) getPoint0().x-5, (int) getPoint0().y+5);
        p.addPoint((int) getPoint1().x+5, (int) getPoint1().y+5);
        p.addPoint((int) getPoint1().x+5, (int) getPoint1().y-5);
        return RegionFactory.createRegion(p);
    }


    






}
