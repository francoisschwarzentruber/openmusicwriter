/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import java.util.ArrayList;
import musicwriter.donnees.Note;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionNotesDecorer extends PartitionActionActionComposee {

    public PartitionActionSelectionNotesDecorer(Selection selection, ArrayList<String> articulations, ArrayList<String> ornements) {
        for(Note note : selection.getNotes())
        {
                actionAjouter(new PartitionActionNoteDecorer(note, articulations, ornements));
        }
    }

}
