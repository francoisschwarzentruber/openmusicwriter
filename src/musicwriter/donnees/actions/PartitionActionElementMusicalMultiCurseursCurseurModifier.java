/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.Curseur;
import musicwriter.donnees.ElementMusicalMultiCurseurs;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionActionElementMusicalMultiCurseursCurseurModifier implements PartitionAction {
    private final ElementMusicalMultiCurseurs elementMusical;
    private final int numeroCurseur;
    private final Curseur nouveauCurseur;
    private final Curseur ancienCurseur;

    
    public PartitionActionElementMusicalMultiCurseursCurseurModifier(ElementMusicalMultiCurseurs elementMusical, int numeroCurseur, Curseur nouveauCurseur)
    {
        this.elementMusical = elementMusical;
        this.numeroCurseur = numeroCurseur;
        this.nouveauCurseur = nouveauCurseur;
        ancienCurseur = elementMusical.getCurseur(numeroCurseur);
    }


    public void executer(PartitionDonnees partitionDonnees) {
        partitionDonnees.elementMusicalSupprimer(elementMusical);
        elementMusical.setCurseur(numeroCurseur, nouveauCurseur);
        partitionDonnees.elementMusicalAjouter(elementMusical);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        partitionDonnees.elementMusicalSupprimer(elementMusical);
        elementMusical.setCurseur(numeroCurseur, ancienCurseur);
        partitionDonnees.elementMusicalAjouter(elementMusical);
    }

}
