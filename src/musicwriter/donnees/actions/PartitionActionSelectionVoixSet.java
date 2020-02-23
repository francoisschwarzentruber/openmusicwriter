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
 *
 * @author proprietaire
 */
public class PartitionActionSelectionVoixSet extends PartitionActionActionComposee
                                                implements PartitionAction {
    public PartitionActionSelectionVoixSet(Selection selection, Voix voix)
    {
        for(ElementMusical element : selection.getElementsMusicaux())
        {
            if(element instanceof ElementMusicalSurVoix)
            {
                actionAjouter(new PartitionActionElementVoixSet(((ElementMusicalSurVoix) element), voix));
            }
            
        }
        
    }
}
