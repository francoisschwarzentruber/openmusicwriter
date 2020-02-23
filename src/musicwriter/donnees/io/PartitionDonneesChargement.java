package musicwriter.donnees.io;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import musicwriter.donnees.PartitionDonnees;

/**
 * Tout objet capable de charger une partition hérite de cette interface.
 * @author Ancmin
 */
public interface PartitionDonneesChargement {
    
    
    
    public PartitionDonnees getPartitionDonneesDuFichier(String nomFichier) throws IOException;
}
