/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Note;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionNotesLierAuxSuivantes  extends PartitionActionActionComposee implements PartitionAction {

    public PartitionActionSelectionNotesLierAuxSuivantes(Selection selection, boolean ouinon)
    {
        for(Note note : selection.getNotes())
        {
                actionAjouter(new PartitionActionNoteLierALaSuivante(note, ouinon));
        }

    }

}
