/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ElementsMusicauxPanel.java
 *
 * Created on 22 mars 2010, 18:23:48
 */

package musicwriter.guiswing.palette;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.TransferHandler;
import musicwriter.donnees.Selection;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalImage;
import musicwriter.guiswing.ImageLoaderSwing;
import musicwriter.guiswing.ImageSwing;
import musicwriter.guiswing.dialogs.RoundedBorder;

/**
 * Ce widget permet de dessiner représente un ensemble d'éléments musicaux
 * @author Ancmin
 */
public class ElementsMusicauxPanel extends javax.swing.JPanel {
    private final Selection selection;



        /** Creates new form ElementsMusicauxPanel */
    public ElementsMusicauxPanel(ElementMusical elementMusical, ImageIcon imgIcon) {
        this(new Selection(elementMusical), imgIcon);

    }


    public ElementsMusicauxPanel(ElementMusical elementMusical) {
        this(new Selection(elementMusical));

    }

    public ElementsMusicauxPanel(ElementMusicalImage elementMusical) {
        this(new Selection(elementMusical), ImageLoaderSwing.getImageIconPourBouton(new ImageIcon(
                ((ImageSwing) elementMusical.getImage()).getImageIcon().getImage())));

    }


    public ElementsMusicauxPanel(Selection selection)
    {
        this(selection, SelectionToImageIcon.convert(selection, false));
    }



    public ElementsMusicauxPanel(Selection selection, boolean avecLignesPortees)
    {
        this(selection, SelectionToImageIcon.convert(selection, avecLignesPortees));
    }

    
     public ElementsMusicauxPanel(Selection selection, ImageIcon imgIcon) {
        initComponents();

        this.selection = selection;
        this.lblLabel.setIcon(imgIcon);
        this.setBorder(new RoundedBorder());
        this.setBackground(Color.WHITE);
        int width = imgIcon.getIconWidth() + 6 + 10;
        int height = imgIcon.getIconHeight() + 6 + 10;
        Dimension dimension = new Dimension(width, height);
        this.lblLabel.setPreferredSize(dimension);
        this.lblLabel.setMinimumSize(dimension);
        this.lblLabel.setMaximumSize(dimension);
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
        this.setTransferHandler(new SelectionTransfertHandler(selection));
        modifierSourisCurseurMainDoigt();

    }

     private void modifierSourisCurseurMainDoigt()
     {
         modifierSourisCurseur("maindoigt.png", new Point(4, 0));
     }

     private void modifierSourisCurseurMainQuiPrend()
     {
         modifierSourisCurseur("mainquiprend.png", new Point(4, 8));
     }

     public void modifierSourisCurseur(String img, Point hotSpot){
         //recupere le Toolkit
         Toolkit tk = Toolkit.getDefaultToolkit();
         //sur ce dernier lire le fichier avec "getClass().getRessource" pour
         //pouvoir l'ajouter a un .jar
         Image image= ImageLoaderSwing.getImageIcon(img).getImage();
         //modifi le curseur avec la nouvelle image,en le posissionant grace hotSpot
         //et en lui donnant le nom "X"
         Cursor c = tk.createCustomCursor(image,hotSpot,"X");
         //puis on l'associe au Panel
         setCursor(c);
     }
    
    public Selection getSelection() {
        return selection;
    }



    

    
    


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLabel = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(32, 48));
        setMinimumSize(null);
        setName("Form"); // NOI18N
        setPreferredSize(null);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        setLayout(new java.awt.CardLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(musicwriter.MusicwriterApp.class).getContext().getResourceMap(ElementsMusicauxPanel.class);
        lblLabel.setText(resourceMap.getString("lblLabel.text")); // NOI18N
        lblLabel.setName("lblLabel"); // NOI18N
        add(lblLabel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        getTransferHandler().exportAsDrag(this, evt,
                TransferHandler.COPY);
        evt.consume(); // empêche que l'événement soit transmis au container du PersonnePanel.
        modifierSourisCurseurMainQuiPrend();

    }//GEN-LAST:event_formMousePressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        modifierSourisCurseurMainDoigt();
    }//GEN-LAST:event_formMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblLabel;
    // End of variables declaration//GEN-END:variables

}
