/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.ElementMusicalTempo;
import java.awt.Font;
import musicwriter.gui.Area;
import musicwriter.gui.Graphics;
import musicwriter.gui.Rectangle;
import musicwriter.gui.RegionFactory;
/**
 *
 * @author Ancmin
 */
public class AffichageElementMusicalTempo extends AffichageElementMusical {
    private double x;

    public AffichageElementMusicalTempo(Systeme systeme, double x, ElementMusicalTempo T)
    {
        super(systeme, T);
        this.x = x;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void draw(Graphics g) {
        g.setFont(new Font(g.getFont().getName(), Font.BOLD, getSysteme().getFontSize(16)));
        g.drawString(getElementMusicalTempo().getNom(), (int) x, (int) getY());
    }

    
    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }


    private double getY()
    {
        //return getSysteme().getYHaut() - 30;
        return getSysteme().getYToutEnHaut()+32;
    }

    
    public Rectangle getRectangle() {
        int height = 16;
        return RegionFactory.createRectangle((int) x, (int) getY() - height, getWidth(), height);
    }


    private ElementMusicalTempo getElementMusicalTempo() {
        return (ElementMusicalTempo) getElementMusical();
    }

    
    private int getWidth() {
        return 64;
    }

    
    public double getXFin() {
        return getX() + getWidth();
    }



}
