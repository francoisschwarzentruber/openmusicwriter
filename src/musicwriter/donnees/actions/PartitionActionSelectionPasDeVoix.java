/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalSurVoix;
import musicwriter.donnees.Voix;

/**
 * Place tous les éléments musicaux, chacun dans une voix distincte
 * @author proprietaire
 */
public class PartitionActionSelectionPasDeVoix extends PartitionActionActionComposee
                                                implements PartitionAction {
    public PartitionActionSelectionPasDeVoix(Selection selection)
    {
        for(ElementMusical element : selection.getElementsMusicaux())
        {
            if(element instanceof ElementMusicalSurVoix)
            {
                actionAjouter(new PartitionActionElementVoixSet(((ElementMusicalSurVoix) element), new Voix()));
            }
            
        }
        
    }
}
