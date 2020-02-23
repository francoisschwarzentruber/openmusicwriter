/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class PartitionActionPartieSupprimer implements PartitionAction {

    final Partie partie;
    final Selection selectionParties;

    
    public PartitionActionPartieSupprimer(PartitionDonnees partitionDonnees, int i)
    {
        partie = partitionDonnees.getPartie(i);
        selectionParties = partitionDonnees.getSelectionToutPartie(partie);
    }

    public void executer(PartitionDonnees partitionDonnees) {
        partitionDonnees.selectionSupprimer(selectionParties);
        partitionDonnees.partieSupprimer(partie);

        
        
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        partitionDonnees.partieAjouter(partie);
        partitionDonnees.selectionAjouter(selectionParties);
    }
}
