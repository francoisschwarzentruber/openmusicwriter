/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.ElementMusical;

/**
 *
 * @author Ancmin
 */
public class PartitionActionElementMusicalRemplacer extends PartitionActionActionComposee {

    public PartitionActionElementMusicalRemplacer(ElementMusical elAvant, ElementMusical elFutur)
    {
        actionAjouter(new PartitionActionElementMusicalSupprimer(elAvant));
        actionAjouter(new PartitionActionElementMusicalAjouter(elFutur));

    }



}
