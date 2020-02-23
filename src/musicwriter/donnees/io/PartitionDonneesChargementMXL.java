/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionDonneesChargementMXL implements PartitionDonneesChargement {

    @Override
    public PartitionDonnees getPartitionDonneesDuFichier(String nomFichier) throws IOException {
        ZipFile zf = new ZipFile(nomFichier);
        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
              ZipEntry ze = (ZipEntry) e.nextElement();
              if (!ze.getName().contains("/")) {
                  InputStream in = zf.getInputStream(ze);
                  return PartitionDonneesChargementMusicXML.getPartitionDonneesFromInputStream(in);
              }
        }
          
        return null;

    }
    
}
