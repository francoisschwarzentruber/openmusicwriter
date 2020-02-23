/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Note;
import musicwriter.donnees.Note.NoteFigure;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
class PartitionActionNoteFigure implements PartitionAction {
    private final Note note;
    private final NoteFigure noteFigure;
    private final NoteFigure ancienneNoteFigure;

    PartitionActionNoteFigure(Note note, Note.NoteFigure noteFigure)
    {
        this.note = note;
        this.noteFigure = noteFigure;
        this.ancienneNoteFigure = note.getNoteFigure();
    }

    public void executer(PartitionDonnees partitionDonnees) {
        note.setNoteFigure(noteFigure);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        note.setNoteFigure(ancienneNoteFigure);
    }

}
