package musicwriter.donnees.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import musicwriter.donnees.PartitionDonnees;
import org.jdom.JDOMException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Cet objet est une façade qui permet de charger une partition depuis un fichier dans
 * n'importe quel format.
 * 
 * @author Ancmin
 */
public class PartitionDonneesChargementGestionnaire {   
   
    static final private HashMap<String, PartitionDonneesChargement> partitionChargements = getPartitionChargements();
    
    static private HashMap<String, PartitionDonneesChargement> getPartitionChargements() {
        HashMap<String, PartitionDonneesChargement> myPartitionChargements = new HashMap<String, PartitionDonneesChargement>();
       
        myPartitionChargements.put("mid", new PartitionDonneesChargementMIDI());
        myPartitionChargements.put("mxl", new PartitionDonneesChargementMXL());
        myPartitionChargements.put("xml", new PartitionDonneesChargementMusicXML());
        
        return myPartitionChargements;
    }
    
    
    
    
    static private String getExtension(String filename)
    {
        return filename.substring(filename.indexOf("." ) + 1); 
    }

    
    
    /**
     * 
     * @param nomFichier
     * @return a score that represents the content of the file nomFichier (where the 
     * type of this file is given by its extension)
     * @throws JDOMException
     * @throws IOException
     * @throws InvalidMidiDataException 
     */
    public static PartitionDonnees getPartitionDonneesDuFichier(String nomFichier) throws JDOMException, IOException, InvalidMidiDataException
    {
        final PartitionDonneesChargement chargement = partitionChargements.get(getExtension(nomFichier));
        
        if(chargement != null)
            return chargement.getPartitionDonneesDuFichier(nomFichier);
        else
            return (new PartitionDonneesChargementMusicXML()).getPartitionDonneesDuFichier(nomFichier);
    }

    static public Set<String> getExtensions()
    {
        return partitionChargements.keySet();
    }

    
    
    

}
