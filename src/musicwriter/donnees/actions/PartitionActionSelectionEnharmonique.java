/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Note;

/**
 *
 * @author proprietaire
 */
public class PartitionActionSelectionEnharmonique extends PartitionActionActionComposee implements PartitionAction {

    public PartitionActionSelectionEnharmonique(Selection selection)
    {
        for(Note note : selection.getNotes())
        {
            actionAjouter(new PartitionActionNoteEnharmonique(note));
        }
        
    }
    

}
