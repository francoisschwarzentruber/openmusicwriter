/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Note;
import musicwriter.donnees.Hauteur;

/**
 *
 * @author proprietaire
 */
public class PartitionActionSelectionAlterer extends PartitionActionActionComposee implements PartitionAction {

    public PartitionActionSelectionAlterer(Selection selection, Hauteur.Alteration alteration)
    {
        for(Note note : selection.getNotes())
        {
                actionAjouter(new PartitionActionNoteAlterer(note, alteration));
        }
        
    }
    
}
