/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Intervalle;
import musicwriter.donnees.Note;

/**
 *
 * @author proprietaire
 */
public class PartitionActionSelectionTransposer extends PartitionActionActionComposee
                                                implements PartitionAction {

    public PartitionActionSelectionTransposer(Selection selection, Intervalle intervalle)
    {
        for(Note note : selection.getNotes())
        {
            actionAjouter(new PartitionActionNoteTransposer(note, intervalle));
        }
        
    }
    

}
