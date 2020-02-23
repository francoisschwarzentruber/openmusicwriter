/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.actions;

import musicwriter.donnees.Moment;
import musicwriter.donnees.PartitionDonnees;

/**
 * This action enables to insert time in the score.
 * If momentNouveau < momentDebut it removes everthing that is between momentNouveau and momentDebut and delete the interval
 * [momentNouveau, momentDebut[
 * @author Ancmin
 */
public class PartitionActionInsererTempsOuSupprimerTemps extends PartitionActionActionComposee {
    
    public PartitionActionInsererTempsOuSupprimerTemps(PartitionDonnees partitionDonnees, Moment momentDebut, Moment momentNouveau)
    {
        if(momentDebut.isStrictementApres(momentNouveau))
        {
            final Moment moment2 = momentDebut;
            final Moment moment1 = momentNouveau;
            
            
            actionAjouter(new PartitionActionSelectionSupprimer(partitionDonnees.getSelectionPlageStrict(moment1, moment2)));
        }
        
        actionAjouter(new PartitionActionInsererTemps(momentDebut, momentNouveau));
        
    }
}
