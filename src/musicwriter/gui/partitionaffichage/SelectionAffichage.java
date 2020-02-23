/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.util.HashSet;
import java.util.Set;
import musicwriter.controller.PartitionPanelModeSelection;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Selection;
import musicwriter.gui.*;

/**
 * Cette classe gère l'affichage d'une sélection.
 * @author Ancmin
 */
public class SelectionAffichage {

    private final PartitionVue partitionVue;
    private final Selection selection;
    private final Set<Polygon> selectionPolygones;


    /**
     *
     * @param selection
     * @return la liste des polygones (hexagones) qui sont derrière les éléments
     * sélectionnées.
     */
    private Set<Polygon> getSelectionArea(Selection selection)
    {
        Set<Polygon> polygons = new HashSet<Polygon>();

        if(selection != null)
            for(ElementMusical el : selection)
            {
                AffichageElementMusical aem = partitionVue.getAffichageElementMusical(el);

                if(aem == null)
                {
                    System.out.println("getSelectionArea : partitionVue.getAffichageElementMusical(el) == null");
                    return polygons;
                }

                Rectangle r = aem.getRectangle();
                r.grow(12, 8);
                Polygon p = RegionFactory.createPolygon();
                p.addPoint((int) r.getMinX(), (int) r.getMinY());
                p.addPoint((int) r.getCenterX(), (int) r.getMinY() - 8);
                p.addPoint((int) r.getMaxX(), (int) r.getMinY());
                p.addPoint((int) r.getMaxX(), (int) r.getMaxY());
                p.addPoint((int) r.getCenterX(), (int) r.getMaxY() + 8);
                p.addPoint((int) r.getMinX(), (int) r.getMaxY());

                polygons.add(p);
            }

        return polygons;

    }



    /**
     * crée un objet permettant l'affichage de la sélection.
     * @param partitionVue
     * @param selection
     */
    public SelectionAffichage(PartitionVue partitionVue, Selection selection)
    {
        this.partitionVue = partitionVue;
        this.selection = selection;
        this.selectionPolygones = getSelectionArea(selection);
    }

    /**
     * affiche les polygones derrière... les hexagones derrière les éléments
     * sélectionnées.
     * Les polygones sont dessinés sur le contexte graphique g.
     *
     * @param g
     */
    public void drawSelectionFond(Graphics g, boolean sousCurseur) {
//        if(sousCurseur)
//        {
//            //g.setColor(new Color(1.0f,0.7f,0.7f));

//        }
//        else
//        {
//        //    g.setColor(new Color(0.9f,0.8f,0.8f));
//        }
       g.setColor(PartitionPanelModeSelection.getColorSelection()
                    .brighter().brighter().brighter().brighter());

        if(selectionPolygones != null)
        {
            for(Polygon p : selectionPolygones)
            {
                g.fillPolygon(p);
            }
        }
    }



/**
 *
 * @param pt
 * @return true ssi le point pt se trouve bien sur l'un des hexagones.
 */
    public boolean isPointInSelectionPolygones(Point pt)
    {
        if(selectionPolygones == null)
        {
            return false;
        }
        else
        {
            for(Polygon p : selectionPolygones)
            {
                if(p.contains(pt))
                    return true;
            }
            return false;
        }
    }

}
