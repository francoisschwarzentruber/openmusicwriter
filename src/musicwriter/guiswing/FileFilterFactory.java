/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.swing.filechooser.FileFilter;
import musicwriter.donnees.io.PartitionDonneesChargementGestionnaire;
import musicwriter.donnees.io.PartitionSauvegardeGestionnaire;

/**
 *
 * @author Ancmin
 */
public class FileFilterFactory {
    static final private HashMap<String, String> descriptions = getDescriptions();

    static private HashMap<String, String> getDescriptions() {
        HashMap<String, String> myDescriptions = new HashMap<String, String>();
        
        myDescriptions.put("mid", "Fichier MIDI");
        myDescriptions.put("png", "Image Portable Network Graphic");
        myDescriptions.put("mxl", "Fichier MusicXML compressé");
        myDescriptions.put("xml", "Fichier MusicXML");
        
        return myDescriptions;
        
    }
    
    /**
     * 
     * @param extension
     * @return the description of the file extension
     * example: getDescription("mid") returns "MIDI file"
     */
    static private String getDescription(String extension)
    {
        return descriptions.get(extension);
    }
    
    
    
    
    static public class FileFilterWithExtension extends FileFilter {

        final String name;
        final String extension;

        public FileFilterWithExtension(final String extension, final String name) {
            this.name = name;
            this.extension = extension;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            } else {
                return f.getName().endsWith("." + extension);
            }
        }

        @Override
        public String getDescription() {
            return name + " (*." + extension + ")";
        }

        public String getExtension() {
            return extension;
        }
    }
    
    
    static private class FileFilterManyExtensions extends FileFilter {

        final String name;
        final Set<String> extensions;

        FileFilterManyExtensions(final String name, final Set<String> extensions) {
            this.name = name;
            this.extensions = extensions;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            } else {
                for(String extension : extensions)
                {
                    if(f.getName().endsWith("." + extension))
                        return true;
                    
                    
                }
                return false;
            }
        }

        @Override
        public String getDescription() {
            String t = "";
            
            for(String extension : extensions)
                {
                    if(t.equals(""))
                    {
                        t = "*." + extension;
                    }
                    else
                    {
                        t = t + ", *." + extension;
                    }
                     
                }
            
            
            return name + " (" + t + ")";
        }

    }
    
    /**
     * 
     * @return le filtre qui permet d'ouvrir tous les fichiers que Musicwriter peut ouvrir
     */
    static public FileFilter getAllOpenableFormatFilter()
    {
        return new FileFilterManyExtensions("Tous les fichiers de partitions", PartitionDonneesChargementGestionnaire.getExtensions());
    }

    static public ArrayList<FileFilter> getOpenableFileFilters()
    {
        ArrayList<FileFilter> F = new ArrayList<FileFilter>();
        
        for(String ext : PartitionDonneesChargementGestionnaire.getExtensions())
            F.add(createFileFilterWithExtension(ext));
        
        return F;
    }
    
    static private FileFilter createFileFilterWithExtension(String extension)
    {
        return new FileFilterWithExtension(extension, getDescription(extension));
    }
    
    static public ArrayList<FileFilter> getExportableFileFilters()
    {
        ArrayList<FileFilter> F = new ArrayList<FileFilter>();
        
        for(String ext : PartitionSauvegardeGestionnaire.getExtensions())
            F.add(createFileFilterWithExtension(ext));
        
        return F;
    }
    
    static public FileFilter getMusicXMLFileFilter()
    {
        return createFileFilterWithExtension("xml");
    }
    
    static public FileFilter getMusicMXLFileFilter()
    {
        return createFileFilterWithExtension("mxl");
    }

}
