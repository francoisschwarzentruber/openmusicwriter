/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Note;
import musicwriter.donnees.Selection;
import musicwriter.donnees.Tonalite;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionAltererSelonTonalite extends PartitionActionActionComposee implements PartitionAction {

    public PartitionActionSelectionAltererSelonTonalite(Selection selection, Tonalite tonalite)
    {
        for(Note note : selection.getNotes())
        {
                actionAjouter(new PartitionActionNoteAltererSelonTonalite(note, tonalite));
        }

    }

}

