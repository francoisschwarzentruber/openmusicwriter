/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Duree;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalDuree;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Rational;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionDureesEtirer extends PartitionActionActionComposee {

    public PartitionActionSelectionDureesEtirer(Selection selection, Rational facteur) {

        
        Moment mDebut = selection.getMomentDebut();

        for(ElementMusical el : selection.getElementsMusicaux())
        {
           
            Duree dDebutEl = new Duree(mDebut, el.getDebutMoment());

            Duree dDebutNouveauEl = dDebutEl.multiplier(facteur);

            Moment mNouveauEl = dDebutNouveauEl.getFinMoment(mDebut);


            actionAjouter(new PartitionActionElementTempsDeplacer(el, new Duree(el.getDebutMoment(), mNouveauEl
                    )));

            if(el instanceof ElementMusicalDuree)
            {
                ElementMusicalDuree eld = (ElementMusicalDuree) el;
                actionAjouter(new PartitionActionElementMusicalDureeChanger(eld, eld.getDuree().multiplier(facteur)));
            }
            


        }
        
        
        
        
        
    }

    



}
