/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;
import musicwriter.donnees.ElementMusical;

/**
 *
 * @author proprietaire
 */
public class PartitionActionSelectionSupprimer extends PartitionActionActionComposee implements PartitionAction {

    public PartitionActionSelectionSupprimer(Selection selection)
    {
        for(ElementMusical element : selection.getElementsMusicaux())
        {
            actionAjouter(new PartitionActionElementMusicalSupprimer(element));
        }
        
    }
    
}
