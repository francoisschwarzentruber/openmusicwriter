/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.Selection;
import musicwriter.donnees.ElementMusical;
import musicwriter.gui.*;
import java.util.HashSet;

/**
 * affichage d'un ensemble d'éléments musicaux
 * @author proprietaire
 */
public class AffichageEnsemble extends HashSet<Affichage> implements Affichage  {
    
    private double x;
    
    AffichageEnsemble(double x)
    {
        this.x = x;
    }
    
    AffichageEnsemble()
    {
        this.x = 0;
    }
    
    
    
    public double getX()
    {
        return x;
    }
    
    
    
    public void setX(double x)
    {
        double ancienx = this.x;
        this.x = x;
        
        for(Affichage a : this)
        {
            a.setX(a.getX() + x - ancienx);
        }
    }
    
    public void draw(Graphics g) {
        for(Affichage a : this)
        {
            a.draw(g);
        }
    }

    

    public Selection getSelection(Area area) {
        Selection selection = new Selection();
        for(Affichage a : this)
        {
            selection.elementsMusicauxAjouter(a.getSelection(area));
        }
        return selection;
    }



    public Area getArea() {
        Area area = RegionFactory.createEmptyRegion();
        
        for(Affichage a : this)
        {
            area.add(a.getArea());
        }
        return area;
    }



    
    
    @Override
    public Rectangle getRectangle() {
        Rectangle rectangle = null;
        
        for(Affichage a : this)
        {
            if(rectangle == null)
            {
                rectangle = a.getRectangle();
            }
            else
                rectangle.add(a.getRectangle());
        }
        return rectangle;
    }
    
    /**
     * 
     * @param point
     * @return Cette fonction retourne l'élément musical sous le point ou null s'il n'y
     * en a pas.
     */
    public ElementMusical getElementMusical(Point point)
    {
        for(Affichage a : this)
        {
            ElementMusical el = a.getElementMusical(point);
            if(el != null)
                return el;
        }

        return null;
    }

    public double getXFin() {
        double xfin = x;
        for(Affichage a : this)
        {
            if(xfin < a.getXFin())
                xfin = a.getXFin();
        }
        return xfin;
    }


}
