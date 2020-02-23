/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Note;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author proprietaire
 */
public class PartitionActionNoteAlterer implements PartitionAction {

    private Hauteur.Alteration alterationAncienne;
    private Hauteur.Alteration alterationNouvelle;
    
    public Note note = null;
    
    public PartitionActionNoteAlterer(Note note, Hauteur.Alteration alteration)
    {
        this.alterationAncienne = note.getHauteur().getAlteration();
        this.alterationNouvelle = alteration;
        
        this.note = note;
        
    }
    
    
    @Override
    public void executer(PartitionDonnees partitionDonnees) {
        note.getHauteur().setAlteration(alterationNouvelle);
    }

    @Override
    public void executerInverse(PartitionDonnees partitionDonnees) {
        note.getHauteur().setAlteration(alterationAncienne);
    }

}
