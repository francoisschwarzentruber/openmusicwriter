/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import musicwriter.donnees.PartitionDonnees;

/**
 * Cette classe est une interface pour la sauvegarde d'une partition
 * @author proprietaire
 */
public class PartitionSauvegardeGestionnaire {
    
    static final private HashMap<String, PartitionSauvegarde> partitionSauvegardes = getPartitionSauvegardes();
    

    static HashMap<String, PartitionSauvegarde> getPartitionSauvegardes()
            
    {
        HashMap<String, PartitionSauvegarde> myPartitionSauvegardes = new HashMap<String, PartitionSauvegarde>();
        myPartitionSauvegardes = new HashMap<String, PartitionSauvegarde>();
        myPartitionSauvegardes.put("mid", new PartitionSauvegardeMIDI());
        myPartitionSauvegardes.put("mxl", new PartitionSauvegardeMusicMXL());
        myPartitionSauvegardes.put("png", new PartitionSauvegardePNG());
        myPartitionSauvegardes.put("xml", new PartitionSauvegardeMusicXML());
        
        return myPartitionSauvegardes;
    }
    
    
    static private String getExtension(String filename)
    {
        return filename.substring(filename.indexOf("." ) + 1); 
    }

    
    static public Set<String> getExtensions()
    {
        return partitionSauvegardes.keySet();
    }

    public static void sauvegarder(PartitionDonnees partitionDonnees, String fichierNom) throws IOException
    {
        final PartitionSauvegarde partitionSauvegarde = partitionSauvegardes.get(getExtension(fichierNom));
        if(partitionSauvegarde != null)
            partitionSauvegarde.sauvegarder(partitionDonnees, fichierNom);
        else if(fichierNom.endsWith(".mxl"))
            (new PartitionSauvegardeMusicXML()).sauvegarder(partitionDonnees, fichierNom);
        
    }
    
    
    

}
