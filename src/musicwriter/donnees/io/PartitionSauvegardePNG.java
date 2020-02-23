/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import musicwriter.guiswing.GraphicsSwing;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.guiswing.GraphicsSwing;
import musicwriter.gui.partitionaffichage.PartitionVue;

/**
 * Cette classe est destiné à enregistrer un aperçu de la partition au format PNG.
 * @author Ancmin
 */
public class PartitionSauvegardePNG implements PartitionSauvegarde {


    public void sauvegarder(PartitionDonnees partitionDonnees, String nomFichier) throws IOException {
        PartitionVue partitionVue = new PartitionVue(partitionDonnees, partitionDonnees.getParties(), partitionDonnees.getMomentDebut(), 1000, 10, 0);
        
        BufferedImage img = new BufferedImage(partitionVue.getWidth()+32, partitionVue.getYToutEnBas(), BufferedImage.TYPE_4BYTE_ABGR);
        
        Graphics2D g = img.createGraphics();
        partitionVue.afficherPartition(new GraphicsSwing(g));
        
        File outputfile = new File(nomFichier);
        ImageIO.write(img, "png", outputfile);


       
    }
    
}
