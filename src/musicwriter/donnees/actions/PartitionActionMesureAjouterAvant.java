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
public class PartitionActionMesureAjouterAvant extends PartitionActionMesureAjouter {

    public PartitionActionMesureAjouterAvant(final PartitionDonnees partitionDonnees, final Moment moment, final int nombre)
    {
        super(partitionDonnees, partitionDonnees.getMesureMomentDebut(moment), nombre);
        
       
    }




}
