/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;

/**
 * Les indices doivent être entre 0 et nombre de parties - 1
 * @author Ancmin
 */
public class PartitionActionPartieDeplacer implements PartitionAction {

    final int indexSource;
    final int indexDestination;
    private final Partie partieADeplacer;

    public PartitionActionPartieDeplacer(Partie partie, int indexDestination)
    {
        this.indexSource = partie.getNumero();
        this.indexDestination = indexDestination;
        this.partieADeplacer = partie;
    }


    
    public void executer(PartitionDonnees partitionDonnees) {
            partitionDonnees.partieSupprimer(indexSource);
            partitionDonnees.partieAjouter(indexDestination, partieADeplacer);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        partitionDonnees.partieSupprimer(indexDestination);
        partitionDonnees.partieAjouter(indexSource, partieADeplacer);
    }

}
