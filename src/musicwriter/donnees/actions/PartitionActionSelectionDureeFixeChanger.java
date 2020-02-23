/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import java.util.Set;
import musicwriter.donnees.*;

/**
 * Change la durée de la sélection
 * modifie les durées de toutes les notes de la sélection à la durée duree
 * 
 * Si les notes sont rapprochés, alors ça forme une suite de notes contigüs
 * 
 * S'il n'y a rien d'autres en même temps dans la partition qui est joué en même
 * temps que les notes sélectionnées, alors ça déplace la suite de la partition pour
 * ne pas insérer de trou etc.
 * 
 * @author proprietaire
 */
public class PartitionActionSelectionDureeFixeChanger extends PartitionActionActionComposee
                                                implements PartitionAction {
    

    public PartitionActionSelectionDureeFixeChanger(PartitionDonnees partitionDonnees, Selection selection, Duree duree)
    {
        
        
        
             
        if(selection.isVide())
            return;
        
        Moment moment = selection.getMomentDebut();
        Moment momentFinNouveau = moment;
        Duree dureeDeplacement = Duree.getDureeNulle();

        
        while(moment != null)
        {
            momentFinNouveau = duree.getFinMoment(momentFinNouveau);
            Set<ElementMusical> els = selection.getElementsMusicauxQuiCommencent(moment);

            Duree dureeMax = Selection.getDureeMaximale(els);

            
             for(ElementMusical element : els)
             {
                if(element instanceof ElementMusicalDuree)
                {
                     actionAjouter(new PartitionActionElementMusicalDureeChanger((ElementMusicalDuree) element,
                                   duree));
                     actionAjouter(new PartitionActionElementTempsDeplacer(element, dureeDeplacement));
                }
             }


             Moment momentSuivant = selection.getMomentSuivantAvecElementsMusicauxQuiDebutent(moment);

             if(momentSuivant == null)
                 break;
             else
             {
                 
                 if((new Duree(moment, momentSuivant)).isInferieur(dureeMax))
                 {
                    dureeDeplacement = dureeDeplacement.moins((new Duree(moment, momentSuivant)).moins(duree));
                 }
                 /**
                  * pas de décalage car à partir de maintenant on est beaucoup
                  * plus loin
                  */
                 else
                 {
                     dureeDeplacement = Duree.getDureeNulle();
                 }
             }


             moment = momentSuivant;


        }
        
        
        
        
        
        /**
         * on s'occupe de décaler un peu s'il y a besoin
         */
        
        
        
        
        if(!partitionDonnees.selectionOtherThingsInTheSameTime(selection))
        {
            PartitionAction decalage =
                    new PartitionActionInsererTemps(selection.getFinMomentAvecDuree(), 
                           momentFinNouveau, partitionDonnees.getMomentMesureFin(selection.getFinMomentElementMusicalDebute()));
            if(selection.getFinMomentAvecDuree().isApres(momentFinNouveau))
                
                actionAjouter(decalage);  
            else
                actionAjouterDebut(decalage);
        }
//        for(ElementMusical element : selection.getElementsMusicaux())
//        {
//
//            if(element instanceof ElementMusicalDuree)
//            {
//                 actionAjouter(new PartitionActionElementMusicalDureeChanger((ElementMusicalDuree) element, duree));
//            }
//        }

    }

}
