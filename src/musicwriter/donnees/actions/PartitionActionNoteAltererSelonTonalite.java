/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Note;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Tonalite;

/**
 *
 * @author Ancmin
 */
public class PartitionActionNoteAltererSelonTonalite implements PartitionAction {

    private Hauteur.Alteration alterationAncienne;
    private Hauteur.Alteration alterationNouvelle;

    public Note note = null;

    public PartitionActionNoteAltererSelonTonalite(Note note, Tonalite tonalite)
    {
        this.alterationAncienne = note.getHauteur().getAlteration();
        this.alterationNouvelle = tonalite.getAlteration(note.getHauteur());

        this.note = note;

    }


    public void executer(PartitionDonnees partitionDonnees) {
        note.getHauteur().setAlteration(alterationNouvelle);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        note.getHauteur().setAlteration(alterationAncienne);
    }

}
