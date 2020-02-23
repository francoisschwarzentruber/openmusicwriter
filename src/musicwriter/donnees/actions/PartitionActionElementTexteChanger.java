/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.ElementMusicalTexte;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionActionElementTexteChanger implements PartitionAction {


    final String ancienTexte;
    final String newTexte;
    private final ElementMusicalTexte el;



    public PartitionActionElementTexteChanger(ElementMusicalTexte el, String newText)
    {
        this.el = el;
        this.ancienTexte = el.getTexte();
        this.newTexte = newText;
    }



    
    public void executer(PartitionDonnees partitionDonnees) {
        el.setTexte(newTexte);
    }

    public void executerInverse(PartitionDonnees partitionDonnees) {
        el.setTexte(ancienTexte);
    }



}
