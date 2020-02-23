/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import java.util.ArrayList;
import musicwriter.donnees.Note;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionActionNoteDecorer implements PartitionAction {

    private final ArrayList<String> articulationsAnciennes;
    private final ArrayList<String> articulations;
    private final ArrayList<String> ornements;
    private final ArrayList<String> OrnementsAnciens;
    private final Note note;

    public PartitionActionNoteDecorer(Note note, ArrayList<String> articulations, ArrayList<String> ornements) {
        this.note = note;
        this.articulations = articulations;
        this.ornements = ornements;
        this.articulationsAnciennes = note.getArticulations();
        this.OrnementsAnciens = note.getOrnements();
    }

    public void executer(PartitionDonnees partitionDonnees) {
        note.setArticulations(articulations);
        note.setOrnements(ornements);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        note.setArticulations(articulationsAnciennes);
        note.setOrnements(OrnementsAnciens);
    }



}
