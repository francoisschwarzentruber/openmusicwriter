/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Color;
import musicwriter.Erreur;
import musicwriter.donnees.ElementMusicalImage;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Selection;
import musicwriter.gui.*;
/**
 *
 * @author Ancmin
 */
public class AffichageElementMusicalImage extends AffichageElementMusical {


    public AffichageElementMusicalImage(Systeme systeme, ElementMusicalImage elementMusicalImage) {
        super(systeme, elementMusicalImage);

    }





    private ElementMusicalImage getElementMusicalImage()
    {
           return (ElementMusicalImage) getElementMusical();
    }


    private Image getImage()
    {
        return getElementMusicalImage().getImage();
    }


    public double getWidth()
    {
        return getRectangle().getWidth();
    }

    public double getXFin() {
        return getX() + getXFin();
    }






    
    public void draw(Graphics g) {
       Rectangle r = getRectangle();

        if(!g.getColor().equals(Color.BLACK))
        {
            g.fillRect(r);
        }



        g.drawImage(getImage(), 
                    r);
    }

    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }


    public int getImageWidth()
    {
        return getImage().getWidth();
        
    }



    public int getImageHeight()
    {
        return getImage().getHeight();

    }

    @Override
    public Rectangle getRectangle() {
        int yCoordonneeVerticale = (int) getSysteme().getY(getElementMusicalImage().getCurseur());
        Portee portee = getElementMusicalImage().getCurseur().getPortee();
        double width =  getImageWidth()*getSysteme().getInterLigne(portee)/(50*8);
        double height =  getImageHeight()*getSysteme().getInterLigne(portee)/(50*8);
        return RegionFactory.createRectangle((int) (getX() - width/2),
                    (int) (yCoordonneeVerticale -  height/2),
                    (int) width,
                    (int) height);
    }

    @Override
    public Selection getSelection(Area area) {
         if(area.contains(getRectangle()))
             return new Selection(getElementMusical());
         else
             return new Selection();
    }

    public double getX() {
        return getSysteme().getXNotes(getElementMusicalImage().getDebutMoment());
    }

    public void setX(double x) {
        Erreur.message("Pas de SetX");
    }

}
