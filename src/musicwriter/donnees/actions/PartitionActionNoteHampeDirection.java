/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Note;
import musicwriter.donnees.Note.HampeDirection;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionActionNoteHampeDirection implements PartitionAction {
    private final Note note;
    private final HampeDirection hampeDirection;
    private final HampeDirection ancienneHampeDirection;

    PartitionActionNoteHampeDirection(Note note, Note.HampeDirection hampeDirection)
    {
        this.note = note;
        this.hampeDirection = hampeDirection;
        this.ancienneHampeDirection = note.getHampeDirection();
    }

    public void executer(PartitionDonnees partitionDonnees) {
        note.setHampeDirection(hampeDirection);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        note.setHampeDirection(ancienneHampeDirection);
    }

    
}
