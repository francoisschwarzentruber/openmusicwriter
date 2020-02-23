/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.actions;

import musicwriter.donnees.Duree;
import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Moment;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Silence;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSilencesCompleter extends PartitionActionActionComposee {
    
    public PartitionActionSilencesCompleter(PartitionDonnees partitionDonnees)
    {
        
        for(Portee portee : partitionDonnees.getPortees())
        {
            Moment momentMesureDebut = partitionDonnees.getMomentDebut();
            while(momentMesureDebut.isStrictementAvant(partitionDonnees.getFinMomentAvecDuree()))
            {
                Moment momentFin = partitionDonnees.getMesureSuivanteMomentDebut(momentMesureDebut);
                traiter(partitionDonnees, portee, momentMesureDebut, momentFin);
                momentMesureDebut = momentFin;
            }
            
        }
        
    }

    private void traiter(PartitionDonnees partitionDonnees, Portee portee, Moment momentMesureDebut, Moment momentFin) {
        Moment moment = momentMesureDebut;
        
        while(moment.isStrictementAvant(momentFin))
        {
            final Moment momentSuivant = partitionDonnees.getMomentSuivant(moment, portee);
            
            if(momentSuivant == null)
                return;
            
            
            if(partitionDonnees.isMomentVideSurPortee(moment, portee))
            {
                actionAjouter(new PartitionActionElementMusicalAjouter(
                        new Silence(moment,
                                    new Duree(moment, momentSuivant),
                                    portee,
                                    portee.getHauteurSilenceStandard(moment))));
            }
            moment = momentSuivant;
        }
    }
    
    
}
