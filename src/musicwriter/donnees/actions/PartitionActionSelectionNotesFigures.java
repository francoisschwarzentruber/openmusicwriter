/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Note;
import musicwriter.donnees.Note.NoteFigure;
import musicwriter.donnees.Selection;



/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionNotesFigures extends PartitionActionActionComposee {

    public PartitionActionSelectionNotesFigures(Selection selection, NoteFigure noteFigure) {
        for(Note note : selection.getNotes())
        {
                actionAjouter(new PartitionActionNoteFigure(note, noteFigure));
        }
    }

}
