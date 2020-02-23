/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.Accord;
import musicwriter.donnees.Note;
import musicwriter.donnees.ElementMusical;
import musicwriter.gui.Point;

/**
 *
 * @author proprietaire
 */
public class AffichageAccordFin extends AffichageEnsemble implements Affichage {

    public AffichageAccordFin(Systeme systeme, Accord accord) {
        super(0);

        for(Note note : accord)
        {
            add(new AffichageNoteFin(systeme, note));
        }
    }

    @Override
    public ElementMusical getElementMusical(Point point) {
        return null;
    }

    @Override
    public void setX(double x) {}


    
    
    
     
}
