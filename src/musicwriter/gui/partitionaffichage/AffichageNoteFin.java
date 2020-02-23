/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Note;
import musicwriter.donnees.ElementMusical;
import java.awt.Color;
import musicwriter.gui.Graphics;
import musicwriter.gui.Point;
import musicwriter.gui.*;

/**
 *
 * @author proprietaire
 */
public class AffichageNoteFin implements Affichage {

    Systeme systeme = null;
    Note note = null;

    AffichageNoteFin(Systeme systeme, Note note) {
        this.systeme = systeme;
        this.note = note;
        
    }
    
    
    public void draw(Graphics g) {
        double ycentrenote =  systeme.getY(note.getPortee(), note.getCoordonneeVerticaleSurPortee());
        Color ancienneCouleur = g.getColor();
        g.setPenWidth(0.9f);
        g.setColor(new Color(0.8f, 0.8f, 0.8f));
            g.drawLine((int) getX(),
                        (int) (ycentrenote - systeme.getInterLigne(note.getPortee())/3),
                        (int) getX(),
                        (int) (ycentrenote + systeme.getInterLigne(note.getPortee())/3));
            g.setColor(ancienneCouleur);
    }

    public Selection getSelection(Area area) {
        return null;
    }

    
    
    public Rectangle getRectangle() {
        double ycentrenote =  systeme.getY(note.getPortee(), note.getCoordonneeVerticaleSurPortee());

            return RegionFactory.createRectangle((int) getX() - 2,
                        (int) ycentrenote - 2, 
                        (int) getX() + 2,
                        (int) 4);
    }

    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }

    public ElementMusical getElementMusical(Point point) {
        return null;
    }

    public double getX() {return systeme.getXNotes(note.getFinMoment());}

    public double getXFin() {
        return getX();
    }


    public void setX(double x) {}

    
    
}
