/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Intervalle;
import musicwriter.donnees.Note;
import musicwriter.donnees.Hauteur;

/**
 *
 * @author proprietaire
 */
public class PartitionActionNoteTransposer implements PartitionAction {

    Note note = null;
    Hauteur hauteurPassee = null;
    Hauteur hauteurFuture = null;
    
    
    PartitionActionNoteTransposer(Note note, Intervalle intervalle)
    {
        this.note = note;
        hauteurPassee = note.getHauteur();
        hauteurFuture = intervalle.getHauteur2(hauteurPassee);
    }
    
    public void executer(PartitionDonnees partitionDonnees) {
        note.setHauteur(hauteurFuture);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        note.setHauteur(hauteurPassee);
    }

}