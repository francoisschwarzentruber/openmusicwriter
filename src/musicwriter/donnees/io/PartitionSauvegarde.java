/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public interface PartitionSauvegarde {
    public void sauvegarder(PartitionDonnees partitionDonnees, String nomFichier)  throws java.io.IOException;
    
}
