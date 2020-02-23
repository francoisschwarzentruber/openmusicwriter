/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.util.HashSet;
import java.util.Set;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Selection;
import musicwriter.gui.Area;
import musicwriter.gui.Graphics;
import musicwriter.gui.Poignee;
import musicwriter.gui.Point;

/**
 *
 * @author proprietaire
 */
public abstract class AffichageElementMusical implements Affichage {

    private final ElementMusical elementMusical;
    private final Systeme systeme;


    /**
     *
     * @return l'élément musical affiché par l'afficheur
     */
    public ElementMusical getElementMusical() {
        return elementMusical;
    }


/**
 *
 * @return le système sur lequel on dessine l'élement musical.
 * (système = ligne de portées)
 */
    public Systeme getSysteme()
    {
        return systeme;
    }



    AffichageElementMusical(Systeme systeme, ElementMusical element)
    {
        this.elementMusical = element;
        this.systeme = systeme;
    }
    

    /**
     *
     * @param point
     * @return l'élément musical si le point est dessus. Et sinon, retourne null.
     */
    @Override
    public ElementMusical getElementMusical(Point point)
    {
        if(getArea().contains(point))
            return getElementMusical();
        else
            return null;
    }


    /**
     * 
     * @return le moment où l'élément musical commence
     */
    protected Moment getDebutMoment()
    {
        return getElementMusical().getDebutMoment();
    }


    
    static protected void dessinerCercle(Graphics g, double cercleCentreX, double cercleCentreY, double cercleRayon) {
        final double angle = -0.5;
        g.rotate(angle, cercleCentreX, cercleCentreY);
        g.drawOval((int) (cercleCentreX - cercleRayon - 1.5f),
                   (int) (cercleCentreY - cercleRayon),
                   (int) (2 * cercleRayon + 3),
                   (int) (2 * cercleRayon - 1));
        g.rotate(-angle, cercleCentreX, cercleCentreY);
        
    }
    
    static protected void dessinerDisque(Graphics g, double cercleCentreX, double cercleCentreY, double cercleRayon) {
        final double angle = -0.3;
        g.rotate(angle, cercleCentreX, cercleCentreY);
        g.fillOval((int) (cercleCentreX - cercleRayon - 1.5f),
                   (int) (cercleCentreY - cercleRayon),
                   (int) (2 * cercleRayon + 3),
                   (int) (2 * cercleRayon));
        g.rotate(-angle, cercleCentreX, cercleCentreY);
        
    }


    /**
     *
     * @param area
     * @return une sélection qui contient l'élément musical, si area touche
     * l'afficheur. Et sinon, retourne une sélection vide.
     */
    public Selection getSelection(Area area) {
         if(area.contains(getRectangle()))
             return new Selection(getElementMusical());
         else
             return new Selection();
    }


/**
 *
 * @return les poignées. C'est à dire les "petits carrés" où on peut venir avec
 * la souris et qu'on peut bouger pour changer la forme de l'élément musical.
 */
    public Set<Poignee> getPoignees()
    {
        return new HashSet<Poignee>();
    }

}
