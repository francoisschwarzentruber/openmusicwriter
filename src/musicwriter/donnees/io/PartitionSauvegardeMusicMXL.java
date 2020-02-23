/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import musicwriter.donnees.PartitionDonnees;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 * Cette classe enregistre au format MusicXML compressé.
 * NE MARCHE PAS. IL FAUT REGARDER COMMENT ECRIRE UN TEL FICHIER.
 * @author Ancmin
 */
public class PartitionSauvegardeMusicMXL implements PartitionSauvegarde {

    @Override
    public void sauvegarder(PartitionDonnees partitionDonnees, String nomFichier) throws java.io.IOException {
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        Document document = PartitionSauvegardeMusicXML.sauvegarderMusicXML(partitionDonnees);
        
        document.setDocType(new DocType("score-partwise",
                "-//Recordare//DTD MusicXML 1.1 Partwise//EN",
                "http://www.musicxml.org/dtds/partwise.dtd"));
 

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(nomFichier));
        
        out.putNextEntry(new ZipEntry("myscore.xml"));        
        sortie.output(document, out);
        out.closeEntry();
        
        out.putNextEntry(new ZipEntry("META-INF/container.xml"));
        
//        <?xml version="1.0" encoding="UTF-8">
//        <container>
//          <rootfiles>
//            <rootfile full-path="elite.xml"/>
//          </rootfiles>
//        </container>
            
            
        Document documentContainer = new Document();
        Element rootfile = new Element("rootfile");
        rootfile.setAttribute("full-path", "myscore.xml");
        Element rootfiles = new Element("rootfiles");
        rootfiles.addContent(rootfile);
        Element container = new Element("container");
        container.addContent(rootfiles);
        
        documentContainer.addContent(container);
        sortie.output(documentContainer, out);
        out.closeEntry();
        
        out.close();
        partitionDonnees.setPasDeModification();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
