/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Note;
import musicwriter.donnees.Note.HampeDirection;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionNotesHampesDirections extends PartitionActionActionComposee {

    public PartitionActionSelectionNotesHampesDirections(Selection selection, HampeDirection hampeDirection) {
        for(Note note : selection.getNotes())
        {
                actionAjouter(new PartitionActionNoteHampeDirection(note, hampeDirection));
        }
    }

}
