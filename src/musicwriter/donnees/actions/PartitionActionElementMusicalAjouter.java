/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.BarreDeMesure;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.ElementMusicalChangementMesureSignature;
import musicwriter.donnees.ElementMusicalTempo;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalClef;
import musicwriter.donnees.ElementMusicalChangementTonalite;
import musicwriter.donnees.Octaviation;

/**
 *
 * @author proprietaire
 */
public class PartitionActionElementMusicalAjouter implements PartitionAction {
    private final ElementMusical element;
    private PartitionActionElementMusicalSupprimer partitionActionElementMusicalSupprimer = null;
    
    public PartitionActionElementMusicalAjouter(ElementMusical noteAAjouter)
    {
        this.element = noteAAjouter;
    }



    private void enregistrerElementMusicalQuOnEcrasePuisSuppression(PartitionDonnees partitionDonnees, ElementMusical elementMusicalEcrase)
    {
        if(elementMusicalEcrase != null)
            {
               partitionActionElementMusicalSupprimer =
                    new PartitionActionElementMusicalSupprimer(elementMusicalEcrase);

               partitionActionElementMusicalSupprimer.executer(partitionDonnees);
            }
    }

    public void executer(PartitionDonnees partitionDonnees) {
        if(element.getClass().equals(ElementMusicalChangementTonalite.class)
           | element.getClass().equals(ElementMusicalChangementMesureSignature.class)
           | element.getClass().equals(ElementMusicalTempo.class)
           | element.getClass().equals(BarreDeMesure.class))
        {
            enregistrerElementMusicalQuOnEcrasePuisSuppression(partitionDonnees,
                    partitionDonnees.getElementMusical(element.getDebutMoment(),
                                element.getClass()));

        }
        else if(element.getClass().equals(ElementMusicalClef.class))
        {
           enregistrerElementMusicalQuOnEcrasePuisSuppression(
                   partitionDonnees,
                   partitionDonnees.getElementMusicalClefPileDessus(element.getDebutMoment(),
                                                         ((ElementMusicalClef) element).getPortee()));
        }
        else if(element.getClass().equals(Octaviation.class))
        {
           enregistrerElementMusicalQuOnEcrasePuisSuppression(
                   partitionDonnees,
                   partitionDonnees.getOctaviationPileDessus(element.getDebutMoment(),
                                                         ((Octaviation) element).getPortee()));
        }


        partitionDonnees.elementMusicalAjouter(element);

    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        partitionDonnees.elementMusicalSupprimer(element);
        if(partitionActionElementMusicalSupprimer != null)
        {
            partitionActionElementMusicalSupprimer.executerInverse(partitionDonnees);
        }
    }
}
