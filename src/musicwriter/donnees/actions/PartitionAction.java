/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.PartitionDonnees;

/**
 * Cette interface spécifie une action du logiciel.
 * Par exemple, elle peut s'hériter pour faire une action comme "ajout d'une note", "transposition" etc.
 * Elle a deux méthodes :
 * executer exécute l'action
 * exécuterInverse annule l'action.
 * @author proprietaire
 */
public interface PartitionAction {
    public void executer(PartitionDonnees partitionDonnees);
    public void executerInverse(PartitionDonnees partitionDonnees);
}
