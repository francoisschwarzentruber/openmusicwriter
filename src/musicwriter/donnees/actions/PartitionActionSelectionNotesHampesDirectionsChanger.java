/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Note;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionNotesHampesDirectionsChanger extends PartitionActionActionComposee {

    public PartitionActionSelectionNotesHampesDirectionsChanger(Selection selection) {
        for(Note note : selection.getNotes())
        {
            if(note.getHampeDirection().equals(Note.HampeDirection.AUTOMATIC) ||
               note.getHampeDirection().equals(Note.HampeDirection.HAUT))
                actionAjouter(new PartitionActionNoteHampeDirection(note, Note.HampeDirection.BAS));
            else
                actionAjouter(new PartitionActionNoteHampeDirection(note, Note.HampeDirection.HAUT));
        }
    }
}
