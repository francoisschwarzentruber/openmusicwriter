/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Portee;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Moment;
import java.awt.Color;
import musicwriter.gui.Graphics;
import musicwriter.gui.Point;
import musicwriter.gui.*;

/**
 *
 * @author Ancmin
 */
public class AffichageSilenceSurTouteLaMesureTouteLaPortee implements Affichage {

    final Systeme systeme;
    final Moment debutMesure;
    final Moment finMesure;
    final Portee portee;

    public Portee getPortee() {
        return portee;
    }

    static Image img = ImageLoader.getImage("pause.png");
    


    AffichageSilenceSurTouteLaMesureTouteLaPortee(Systeme systeme, Moment debutMesure, Portee portee) {
        this.systeme = systeme;
        this.debutMesure = debutMesure;
        this.finMesure = systeme.getPartitionDonnees().getMomentMesureFin(debutMesure);
        this.portee = portee;
        
    }



    private double getWidth()
    {
        return (img.getWidth()*systeme.getInterLigne(getPortee())/50);
    }
    
    public double getX() {
       return (systeme.getXNotes(debutMesure) + systeme.getXBarreAuDebut(finMesure)) / 2
                - getWidth();
    }

    public void setX(double x) {
        
    }


    private Systeme getSysteme()
    {
        return systeme;
    }

    public void draw(Graphics g) {
        Rectangle r = getRectangle();

        if(!g.getColor().equals(Color.BLACK))
             g.fillRect(r);

        g.drawImage(img, r);


    }

    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }



    /**
     * @return le y moyen du silence
     */
    public double getY()
    {
        int coordonneVerticale = 2;
        return getSysteme().getY(getPortee(), coordonneVerticale);
    }


    public Rectangle getRectangle() {
        double y =   getY();

        return RegionFactory.createRectangle(
            (int) (getX()),
            (int) (y),//-  (img.getIconHeight() / 2)*getSysteme().getInterLigne(getPortee())/50),
            (int) (img.getWidth()*getSysteme().getInterLigne(getPortee())/50),
            (int) (img.getHeight()*getSysteme().getInterLigne(getPortee())/50));
    }

    public Selection getSelection(Area area) {
        return new Selection();
    }

    public ElementMusical getElementMusical(Point point) {
        return null;
    }

    public double getXFin() {
       return getX();
    }

}
