/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Font;
import musicwriter.donnees.Selection;
import musicwriter.donnees.ElementMusicalChangementMesureSignature;
import musicwriter.donnees.Portee;
import javax.swing.JPanel;
import musicwriter.gui.*;

/**
 *
 * @author Ancmin
 */
public class AffichageChangementMesureSignature extends AffichageElementMusical implements Affichage {
    private double x;


    AffichageChangementMesureSignature(Systeme systeme, double x, ElementMusicalChangementMesureSignature element)
    {
          super(systeme, element);
          this.x = x;
    }


    private ElementMusicalChangementMesureSignature getChangementMesureSignature()
    {
        return (ElementMusicalChangementMesureSignature) getElementMusical();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }


    public Font getFont()
    {
        return new Font("", Font.BOLD, getSysteme().getFontSize(22));
    }

    public void draw(Graphics g) {
        
        for(Portee p : getSysteme().getPortees())
        {
            g.setFont(getFont());
            
            g.drawString(getChangementMesureSignature().getSignature().getNumerateur(),
                         (int) x,
                         (int) getSysteme().getY(p, 0));
            g.drawString(String.valueOf(getChangementMesureSignature().getSignature().getDenominateur()),
                         (int) x,
                         (int) getSysteme().getY(p, -4));
        }
    }


    public Rectangle getRectangle() {
        return RegionFactory.createRectangle((int) getX(), 
                             (int) getSysteme().getYHaut(),
                             (int) getWidth(),
                             (int) (getSysteme().getYBas() - getSysteme().getYHaut()));
    }

    @Override
    public Selection getSelection(Area area) {
         if(area.contains(getRectangle()))
             return new Selection(getElementMusical());
         else
             return new Selection();
    }

    public Area getArea() {
        Area a = RegionFactory.createEmptyRegion();;
        for(Portee p : getSysteme().getPortees())
        {
            a.add(RegionFactory.createRegion(RegionFactory.createRectangle((int) getX(),
                    (int) getSysteme().getPorteeYHaut(p),
                    (int) getWidth(),
                    (int) getSysteme().getPorteeHeight(p))));
        }
        return a;
    }


    /**
     * TODO : approximatif car pas de FontMetrics
     * @return 
     */
    public double getWidth() {
        return Math.max(getSysteme().getInterLigneStandard() * 1.6,
                getChangementMesureSignature().getSignature().getNumerateur().length());
    }


    public double getXFin() {
        return getX() + getWidth();
    }


}
