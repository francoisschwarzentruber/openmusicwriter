/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;


import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Selection;
import musicwriter.gui.Area;
import musicwriter.gui.Graphics;
import musicwriter.gui.Point;
import musicwriter.gui.Rectangle;

/**
 * Cette interface sert à représenter toute zone à l'écran qui sera affiché.
 *
 * Exemple : une note à l'écran, une barre de mesure, un changement de tonalité etc.
 * @author François Schwarzentruber
 */
interface Affichage {

    /**
     *
     * @return l'absisse de l'objet affiché.
     */
    public double getX();

    /**
     *
     * @return l'absisse de l'objet affiché.
     */
    public double getXFin();


    /**
     * définit l'abscisse de l'objet à afficher
     * @param x
     */
    void setX(double x);
    
    /**
     * Cette fonction dessine l'objet dans l'objet g.
     * @param g
     */
    void draw(Graphics g);


    
    /**
     *
     * @return la région où il y a affiché quelque chose.
     */
    Area getArea();


    /**
     *
     * @return le plus petit rectangle contenant la région où il y a affiché quelque chose.
     */
    Rectangle getRectangle();

    /**
     *
     * @param area
     * @return la sélection (ensemble d'éléments musicaux) qui intersecte la région area.
     */
    public Selection getSelection(Area area);
    

    /**
     *
     * @param point
     * @return l'élément musical présent sous le point et géré par cet objet Affichage
     * retourne null si il n'y en a pas
     */
    ElementMusical getElementMusical(Point point);
    
}
