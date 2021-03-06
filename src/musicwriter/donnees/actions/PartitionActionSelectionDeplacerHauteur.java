/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.*;

/**
 *
 * @author proprietaire
 */
public class PartitionActionSelectionDeplacerHauteur extends PartitionActionActionComposee
                                                implements PartitionAction {

    public PartitionActionSelectionDeplacerHauteur(Selection selection, Intervalle intervalle)
    {
        for(ElementMusical element : selection.getElementsMusicaux())
        {
            if(element instanceof Note)
                 actionAjouter(new PartitionActionNoteTransposer((Note) element, intervalle));
            
            if(element instanceof Silence)
                 actionAjouter(new PartitionActionSilenceDeplacerHauteur((Silence) element, intervalle));
        }
        
    }
    

}
