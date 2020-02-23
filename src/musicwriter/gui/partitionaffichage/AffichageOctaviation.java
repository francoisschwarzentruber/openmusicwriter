/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Font;
import musicwriter.gui.Graphics;
import musicwriter.Erreur;
import musicwriter.donnees.Octaviation;
import musicwriter.donnees.Portee;
import musicwriter.gui.Area;
import musicwriter.gui.Rectangle;
import musicwriter.gui.RegionFactory;

/**
 *
 * @author Ancmin
 */
public class AffichageOctaviation extends AffichageElementMusical {

    public AffichageOctaviation(Systeme systeme, Octaviation octaviation) 
    {
        super(systeme, octaviation);
    }




    public double getX() {
        if(getOctaviation().getNbOctavesEntreEcritureEtSon() == 0)
             return getSysteme().getXNotes(getElementMusical().getDebutMoment()) - (int) (4*getSysteme().getInterLigne(getPortee()));
        else
             return getSysteme().getXNotes(getElementMusical().getDebutMoment());
    }

    public double getXFin() {
        return getX();
    }




    public void setX(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Octaviation getOctaviation()
    {
        return (Octaviation) getElementMusical();
    }


    public Portee getPortee()
    {
        return getOctaviation().getPortee();
    }


    private double getY()
    {
        return getSysteme().getY(getOctaviation().getPortee(), 8);
    }

    public void draw(Graphics g) {
        int nbOctave = getOctaviation().getNbOctavesEntreEcritureEtSon();
        g.setFont(new Font(g.getFont().getName(), 0,  getSysteme().getFontSize(getPortee(), 10)));
        double y = getY();

        switch(nbOctave)
        {
            case 1:
                g.drawString("8va bassa", (int) getX(), (int) y);
                break;
            case -1:
                g.drawString("8va alta", (int) getX(), (int) y);
                break;
            case 0:
                int x1 = (int) getX();
                int x2 = x1 + (int) (2*getSysteme().getInterLigne(getPortee()));
                int y1 = (int) y;
                int y2 = y1 + (int) (2*getSysteme().getInterLigne(getPortee()));
                g.drawLine(x1, y1, x2, y1);
                g.drawLine(x2, y1, x2, y2);
                break;
            default:
                Erreur.message("AffichageOctaviation.draw(g)");
        }
    }

    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }

    public Rectangle getRectangle() {
        int esp = (int) getSysteme().getInterLigne(getPortee());
        if(getOctaviation().getNbOctavesEntreEcritureEtSon() == 0)
        {
            return RegionFactory.createRectangle((int) getX(),
                            (int) getY(),
                            (int) (4*getSysteme().getInterLigne(getPortee())),
                            2*esp);

        }
        else
            return RegionFactory.createRectangle((int) getX(),
                            (int) getY() - 2*esp,
                            (int) (4*getSysteme().getInterLigne(getPortee())),
                            2*esp);
    }

}
