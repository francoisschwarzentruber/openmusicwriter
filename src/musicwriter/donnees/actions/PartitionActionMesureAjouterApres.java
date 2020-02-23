/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Moment;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */

public class PartitionActionMesureAjouterApres extends PartitionActionMesureAjouter {

    public PartitionActionMesureAjouterApres(final PartitionDonnees partitionDonnees,
                                             final Moment moment,
                                             final int nombre)
    {
        super(partitionDonnees, partitionDonnees.getMesureSuivanteMomentDebut(moment), nombre);
        
    }


}

