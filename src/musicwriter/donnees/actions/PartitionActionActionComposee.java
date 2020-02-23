/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.PartitionDonnees;
import java.util.ArrayList;

/**
 *
 * @author proprietaire
 */
public class PartitionActionActionComposee implements PartitionAction {
    private final ArrayList<PartitionAction> actions = new ArrayList<PartitionAction>();


    protected void actionsSupprimerToutes() {
        actions.clear();
    }

    public void actionAjouterDebut(PartitionAction action)
    {
        actions.add(0, action);
    }
    
    
    
    public void actionAjouter(PartitionAction action)
    {
        actions.add(action);
    }
    
    public void executer(PartitionDonnees partitionDonnees) {
        for(int i = 0; i < actions.size(); i++)
        {
            actions.get(i).executer(partitionDonnees);
        }
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        for(int i = actions.size() - 1; i >= 0 ; i--)
        {
            actions.get(i).executerInverse(partitionDonnees);
        }
    }
}
