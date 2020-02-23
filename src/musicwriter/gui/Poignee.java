/*/
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

import java.awt.Color;
import java.awt.Cursor;
import musicwriter.gui.Point;
import java.awt.Rectangle;
import musicwriter.controller.Controller;
import musicwriter.donnees.Curseur;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Histoire;
import musicwriter.gui.partitionaffichage.Systeme;


/**
 * Une poignée est un petit carré rouge qu'on peut prendre avec la souris
 * pour modifier un objet.
 * @author Ancmin
 */
public abstract class Poignee {


    /**
     * systeme = ligne de portées où se trouve la poignée
     */
    private final Systeme systeme;
    private final int grosseur = 3;

    /**
     * curseur = position dans la partition (temps, portée,  hauteur etc.) où
     * se trouve la poignée
     */
    private final Curseur curseur;



    /**
     *
     * @return le système sur lequel se trouve la poignée.
     * système = une ligne de plusieurs portées où on écrit de la musique.
     */
    protected Systeme getSysteme() {
        return systeme;
    }
    

    /**
     * crée une poignée c'est à dire un "petit carré" qu'on peut déplacer
     * à la souris pour modeler un élément musical.
     * systeme désigne le système (= ligne de portées) où la poignée est
     * curseur désigne la position dans la partition d'où on est
     * @param systeme
     * @param curseur
     */
    public Poignee(Systeme systeme, Curseur curseur) {
        this.systeme = systeme;
        this.curseur = curseur;

    }


    /**
     *
     * @return l'absisse du point central de la poignée
     */
    protected int getX() {
        return (int) systeme.getPoint(curseur).x;
    }

/**
 *
 * @return l'ordonnée du point central de la poignée
 */
    protected int getY() {
        return (int) systeme.getPoint(curseur).y;
    }



    /**
     * dessine la poignée (un petit carré) sur l'objet graphique g
     * @param g
     */
    public void draw(Graphics g)
    {
         final Color colorPoigneeContour = new Color(0.7f, 0.0f, 0.5f);
         final Color colorPoigneeFond = new Color(1.0f, 0.9f, 1.0f);
         
         g.setColor(colorPoigneeFond);
         g.fillOval(getX() - grosseur, getY() - grosseur, 2*grosseur, 2*grosseur);
         g.setPenWidth(1.0f);
         g.setColor(colorPoigneeContour);
         g.drawOval(getX() - grosseur, getY() - grosseur, 2*grosseur, 2*grosseur);
    }


    

/**
 *
 * @param p
 * @return vrai ssi le point p est sur la poignée
 */
    public boolean isContientPoint(Point p)
    {
        return RegionFactory.createRectangle(getX() - grosseur, getY() - grosseur,
                2*grosseur, 2*grosseur).contains(p);
    }



    /**
     * implémente ce qui se passe quand on déplace la poignée.
     * newCurseur = le curseur où on se trouve actuellement
     * @param newCurseur
     * @param panel
     */
    public abstract void mouseDrag(Curseur newCurseur, Controller panel);



    /**
     * implémente ce qui se passe quand on lâche la pognée.
     * newCurseur = le curseur où la poignée est lâchée
     * histoire = permet d'enregistrer quelles actions sont effectuées
     * @param newCurseur
     * @param histoire
     */
    public abstract void mouseFinish(Curseur newCurseur, Histoire histoire);



    public abstract ElementMusical getElementMusicalResultat();

    public abstract Cursor getCursor();

}
