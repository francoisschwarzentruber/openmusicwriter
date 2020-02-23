/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.ElementMusicalSurPortee;

/**
 *
 * @author proprietaire
 */
public class PartitionActionSelectionPorteeChanger extends PartitionActionActionComposee implements PartitionAction {

    public PartitionActionSelectionPorteeChanger(PartitionDonnees partitionDonnees, Selection selection, int porteeChangement)
    {
        for(ElementMusicalSurPortee note : selection.getElementsMusicauxSurPortee())
        {
            actionAjouter(new PartitionActionElementMusicalPorteeChanger(partitionDonnees, note, porteeChangement));
        }
        
    }

}
