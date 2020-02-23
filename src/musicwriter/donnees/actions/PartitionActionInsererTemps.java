/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import java.util.ArrayList;
import musicwriter.donnees.*;

/**
 *
 * @author proprietaire
 */
public class PartitionActionInsererTemps implements PartitionAction {
    private final Moment momentAvant;
    private final Moment momentNouveau;
    
    /**
     * il s'agit du moment à partir du quel on ne décalle plus (valable uniquement
     * si momentNouveau < momentAvant
     * 
     * Si à null, alors c'est ignoré... on déplace tout jusqu'à la fin de la partition
     */
    private final Moment momentLimit;

    /**
     * cette action a pour but de TOUT décaler pour insérer du "vide"
     * Par exemple si moment = 3/1 et nouveauMoment = 8/1.. ça insère 5 temps
     * et la note en 3/1 va en 8/1, la note en 4/1 va en 9/1 etc.
     * 
     * cette action ne marche que si moment <= nouveauMoment
     * ou si moment > nouveauMoment, il faut qu'il n'y ait rien d'écrit
     * entre nouveauMoment et moment.
     * @param moment
     * @param nouveauMoment
     */
    public PartitionActionInsererTemps(Moment moment, Moment nouveauMoment)
    {
        momentAvant = moment;
        momentNouveau = nouveauMoment;
        
        momentLimit = null;
    }


    /**
     * the same. But momentLimit is the moment where we do not change anything if nouveauMoment < moment
     * @param moment
     * @param nouveauMoment
     * @param momentLimit 
     */
    public PartitionActionInsererTemps(Moment moment, Moment nouveauMoment, Moment momentLimit)
    {
        momentAvant = moment;
        momentNouveau = nouveauMoment;
        
        if(nouveauMoment.isAvant(moment))
        {
            this.momentLimit = momentLimit;
        }
        else
        {
            this.momentLimit = null;
        }
    }
    
    
    
    
    
    private boolean isDeplacerSiAuDebut(ElementMusical e)
    {
        return !((e instanceof ElementMusicalClef) ||
                (e instanceof ElementMusicalTempo) ||
               (e instanceof ElementMusicalChangementMesureSignature) ||
                (e instanceof ElementMusicalChangementTonalite))
                ||
                !e.getDebutMoment().equals(momentAvant);
    }

    @Override
    public void executer(PartitionDonnees partitionDonnees) {

        Duree d = new Duree(momentAvant, momentNouveau);
        
        final ArrayList<ElementMusical> els;
        
        if(d.isPositive())
        {
            els = partitionDonnees.getSelectionToutEntreMoment1Moment2ElementsMusicauxALEnvers(momentAvant, momentLimit);
        }
        else
        {
            els = partitionDonnees.getSelectionToutEntreMoment1Moment2ElementsMusicauxALEndroit(momentAvant, momentLimit);
        }
        
        
        
        for(ElementMusical e : els)
        {
            if(isDeplacerSiAuDebut(e))
                 partitionDonnees.elementMusicalDeplacer(e, d.getFinMoment(e.getDebutMoment() ));
        }
    }

    @Override
    public void executerInverse(PartitionDonnees partitionDonnees) {
        Duree d = new Duree(momentAvant, momentNouveau);
        
        final ArrayList<ElementMusical> els;
        
        if(d.isPositive())
        {
            els = partitionDonnees.getSelectionToutEntreMoment1Moment2ElementsMusicauxALEndroit(momentNouveau, momentLimit);
        }
        else
        {
            els = partitionDonnees.getSelectionToutEntreMoment1Moment2ElementsMusicauxALEnvers(momentNouveau, momentLimit);
        }
        
        
        for(ElementMusical e : els)
        {
            if(isDeplacerSiAuDebut(e))
                partitionDonnees.elementMusicalDeplacer(e, d.getDebutMoment(e.getDebutMoment() ));
        }
    }

}
