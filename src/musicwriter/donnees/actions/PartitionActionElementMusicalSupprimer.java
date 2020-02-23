/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author proprietaire
 */
public class PartitionActionElementMusicalSupprimer implements PartitionAction {
    private final ElementMusical noteASupprimer;
    
    public PartitionActionElementMusicalSupprimer(ElementMusical noteASupprimer)
    {
        this.noteASupprimer = noteASupprimer;
    }

    public void executer(PartitionDonnees partitionDonnees) {
        partitionDonnees.elementMusicalSupprimer(noteASupprimer);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        partitionDonnees.elementMusicalAjouter(noteASupprimer);
    }
}