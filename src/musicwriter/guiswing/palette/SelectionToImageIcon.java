/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.palette;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import musicwriter.guiswing.GraphicsSwing;
import musicwriter.donnees.Instrument;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Selection;
import musicwriter.gui.partitionaffichage.PartitionVue;

/**
 * Le but de cette classe est de transformer une sélection d'éléments musicaux 
 * (notes, silences...) en une image. Une telle image peut être incorporé dans 
 * un bouton.
 * @author Ancmin
 */
public class SelectionToImageIcon {
    
    static private PartitionVue getPartitionVue(Selection selection)
    {
        final PartitionDonnees p = new PartitionDonnees();

        for(Partie partie : selection.getElementsMusicauxParties())
        {
            p.partieAjouter(partie);
        }

        if(selection.getElementsMusicauxParties().isEmpty())
            p.partieAjouter(new Partie(new Instrument(45)));
        
        p.selectionAjouter(selection);

        return new PartitionVue(p, p.getParties(), p.getMomentDebut(), 1000, 7, 0);
    }
    /**
     * 
     * @param selection
     * @param avecPorteesLignes
     * @return une image qui représente la séletion.
     * si avecPorteesLignes, on affiche en plus les 5 lignes des portées
     */
    static public ImageIcon convert(Selection selection, boolean avecPorteesLignes)
    {
        

        PartitionVue v = getPartitionVue(selection);


        final int decalX = -40;
        final int decalY = -35;
        final int width = (int) v.getSysteme(v.getMomentDebut()).getXFinDernierElementsMusicaux()+ decalX-10;
        final int height = (int) v.getSysteme(v.getMomentDebut()).getYToutEnBas() -10+ decalY;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        // Create an image that does not support transparency
        BufferedImage img = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics2D g = ((Graphics2D) img.createGraphics());

        //BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.translate(decalX, decalY);
        g.setColor(Color.BLACK);

        if(avecPorteesLignes)
            v.afficherPartition(new GraphicsSwing(g));
        else
            v.afficherElementsMusicauxSansLigne(new GraphicsSwing(g));

        g.dispose();


        return new ImageIcon(img);
    }


}
