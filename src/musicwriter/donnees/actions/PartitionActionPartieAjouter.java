/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Partie;

/**
 *
 * @author proprietaire
 */
public class PartitionActionPartieAjouter implements PartitionAction {

    /**
     * Quand on ajoute une partie, la partie est vide.
     */
    private final Partie partie;
    private final int position;
    
    public PartitionActionPartieAjouter(int position, Partie partie)
    {
        this.position = position;
        this.partie = partie;
    }

    public void executer(PartitionDonnees partitionDonnees) {
        partitionDonnees.partieAjouter(position, partie);
        partitionDonnees.partieClefsInstaller(partie);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        partitionDonnees.partieSupprimer(partie);
    }




}
