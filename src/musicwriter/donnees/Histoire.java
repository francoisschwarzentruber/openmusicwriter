/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Stack;
import musicwriter.donnees.actions.PartitionAction;
import musicwriter.gui.PartitionVueEcran;

/**
 * L'histoire est une classe qui regroupe toutes les informations qui
 * se sont passés. Si on rajoute une note à la partition, en fait, on rajoute
 * un objet PartitionActionElementMusicalAjouter à l'histoire via la fonction
 * executer. L'objet histoire s'en souvient.
 * On pourra ensuite utilisé les fonctions annuler() et refaire().
 * @author proprietaire
 */
public class Histoire {
    private final Stack<PartitionAction> actionsPassees = new Stack<PartitionAction>();
    private final Stack<PartitionAction> actionsFutures = new Stack<PartitionAction>();
    private final PartitionDonnees partitionDonnees;
    private final PartitionVueEcran partitionVue;
    
    public Histoire(PartitionDonnees partitionDonnees,
             PartitionVueEcran partitionVue)
    {
        this.partitionDonnees = partitionDonnees;
        this.partitionVue = partitionVue;
    }
    

    /**
     * Exécute l'action passée en paramètres.
     * @param action
     */
    public void executer(PartitionAction action)
    {
        action.executer(partitionDonnees);
        actionsPassees.push(action);
        actionsFutures.clear();
    }
    
    
    /**
     *
     * @return true ssi il est possible d'annuler (équivalent à
     * il s'est passé quelque chose)s
     */
    public boolean isAnnulerPossible()
    {
        return !actionsPassees.isEmpty();
    }
    
    
    /**
     *
     * @return true s'il y a quelque chose à refaire (c'est à dire qu'on a annulé
     * quelque chose)
     */
    public boolean isRefairePossible()
    {
        return !actionsFutures.isEmpty();
    }
    
    
    /**
     * annule la dernière action qui a été effectuée
     */
    public void annulerLaDerniereAction()
    {
        PartitionAction action = actionsPassees.pop();
        action.executerInverse(partitionDonnees);
        actionsFutures.push(action);
        //partitionVue.miseEnPageCalculer();
        partitionVue.miseEnPageCalculerSansThread(partitionDonnees.getMomentDebut(), partitionDonnees.getMomentFin());
    }
    
    /**
     * refait la dernière action qui a été annulée
     */
    public void refaireLaDerniereAction()
    {
        PartitionAction action = actionsFutures.pop();
        action.executer(partitionDonnees);
        actionsPassees.push(action);
        //partitionVue.miseEnPageCalculer();
        partitionVue.miseEnPageCalculerSansThread(partitionDonnees.getMomentDebut(), partitionDonnees.getMomentFin());
        
    }
    

}
