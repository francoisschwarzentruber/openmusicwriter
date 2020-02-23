/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionRemplacer extends PartitionActionActionComposee {

    public PartitionActionSelectionRemplacer(Selection selectionASupprimer, Selection selectionAMettre)
    {
        actionAjouter(new PartitionActionSelectionSupprimer(selectionASupprimer));
        actionAjouter(new PartitionActionSelectionInserer(selectionAMettre));

    }
}
