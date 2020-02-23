/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PaletteNoteDialog.java
 *
 * Created on 10 août 2011, 02:30:12
 */
package musicwriter.guiswing;

import musicwriter.controller.ControllerListener;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import musicwriter.controller.Controller;
import musicwriter.controller.PartitionPanelModeEcriture;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.*;
import musicwriter.guiswing.dialogs.DivisionsExceptionnellesDialog;
import org.jdesktop.application.Action;

/**
 *
 * @author Ancmin
 */
public class PaletteFloatableDialog extends javax.swing.JDialog {

    final ResizeUndecoratedDecorator resizeUndecoratedDecorator;
    final Controller controller;
    
    
    /** Creates new form PaletteNoteDialog */
    public PaletteFloatableDialog(java.awt.Frame parent, final Controller controller, boolean modal) {
        super(parent, modal);
        //setUndecorated(true);
        initComponents();
        
        
        this.resizeUndecoratedDecorator = new ResizeUndecoratedDecorator(this);
        this.controller = controller;
        
        
        dureePanel.addListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                controller.dureeEntreeTraiter(dureePanel.getDuree());
               
            }
        });
        
        
        
        
        alterationPanel.addListener(new ActionListener() {

            private void alterer(Hauteur.Alteration alteration)
            {
                Histoire histoire = controller.getHistoire();

                    if(controller.isSelection())
                        //s'il y a une sélection, on l'altère
                    {
                        histoire.executer(
                                new PartitionActionSelectionAlterer(
                                      controller.getSelection(),
                                      alteration));
                        controller.calculerModificationSelection();
                        controller.repaint();
                    }
                    else if(controller.getCurseurSouris().isSurNote())
                        //si le curseur est sous une note, on l'altère
                    {
                        histoire.executer(new PartitionActionNoteAlterer(
                                controller.getCurseurSouris().getNote(), alteration));
                        controller.getPartitionVue().miseEnPageCalculer(controller.getCurseurSouris().getNote().getDebutMoment());
                        controller.repaint();
                    }
                    else
                       controller.setAlterationCourante(alteration);

            }
        
        
        
            public void actionPerformed(ActionEvent e) {
                alterer(alterationPanel.getAlteration());
            }
        });
		
		

        
        
        selectionHampePanel.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.selectionNotesHampesDirectionsSet(selectionHampePanel.getHampeDirection());
            }
        });
        
        
        
        
        
        panelBarreDeMesure.addListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final BarreDeMesure barre = (BarreDeMesure) getPartitionPanel().getSelection().getElementMusicalUnique();
                final BarreDeMesure nouvelleBarre = new BarreDeMesure(barre.getDebutMoment(), panelBarreDeMesure.getBarreDeMesureType());
                getHistoire().executer(
                        new PartitionActionElementMusicalRemplacer(
                           barre,
                           nouvelleBarre));
                getPartitionPanel().setSelection(new Selection(nouvelleBarre));
                getPartitionPanel().calculer(barre.getDebutMoment());
                
            }

        });
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        controller.addControllerListener(new ControllerListener() {

            public void whenUpdate(Controller partitionPanel) {
                panelNotes.setVisible(false);    
                panelBarreDeMesure.setVisible(false);
                
                
                
                    if(!partitionPanel.isSelection())
                    {
                        panelNotes.setVisible(true);
                        return;
                    }
                         
            
                    Selection selection = partitionPanel.getSelection();
            
                    if(selection.isSingleton())
                    {
                        ElementMusical el = selection.getElementMusicalUnique();

                        if(el instanceof ElementMusicalDuree)
                        {
                            if(el instanceof Silence)
                                dureePanel.afficherSilence();
                            else
                                dureePanel.afficherNotes();
                            
                            dureePanel.setDuree(((ElementMusicalDuree) el).getDuree());
                            if(selection.getNotes().iterator().hasNext())
                            {
                                Note note = selection.getNotes().iterator().next();
                                menuNoteLieeALaSuivante.setSelected(note.isLieeALaSuivante());
                                menuNoteLieeALaSuivante.setVisible(true);
                            }
                            else
                            {
                                menuNoteLieeALaSuivante.setVisible(false);
                            }
                            panelNotes.setVisible(true);
                        }
                        else if(el instanceof BarreDeMesure)
                        {
                            panelBarreDeMesure.setVisible(true);
                            panelBarreDeMesure.setBarreDeMesureType(((BarreDeMesure) el).getBarreDeMesureType());
                        }
                    }
                    else
                    {
                        dureePanel.afficherNotes();
                        panelNotes.setVisible(true);
                          dureePanel
                            .setDuree(
                                  PartitionPanelModeEcriture.getCurseurNoteDureeEntree()
                                  );
                    }
                    
                    if(selection.getNotes().iterator().hasNext())
                    {
                        selectionHampePanel.setHampeDirection(selection.getNotes().iterator().next().getHampeDirection());
                    }
                
                }


        });
        
        
        
        setLocationRelativeTo(null);
        
    }//fin constructeur


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdTitle = new javax.swing.JButton();
        panelNotes = new javax.swing.JPanel();
        jPanel = new javax.swing.JPanel();
        alterationPanel = new musicwriter.guiswing.AlterationPanel();
        dureePanel = new musicwriter.guiswing.DureePanel();
        jToolBar2 = new javax.swing.JToolBar();
        cmdTriolets = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        cmdVoixGrouper = new javax.swing.JButton();
        cmdVoixIndependante = new javax.swing.JButton();
        menuNoteLieeALaSuivante = new javax.swing.JToggleButton();
        selectionHampePanel = new musicwriter.guiswing.HampePanel();
        panelBarreDeMesure = new musicwriter.guiswing.PanelBarreDeMesure();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(280, 68));
        setName("Form"); // NOI18N
        setUndecorated(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(musicwriter.MusicwriterApp.class).getContext().getResourceMap(PaletteFloatableDialog.class);
        cmdTitle.setText(resourceMap.getString("cmdTitle.text")); // NOI18N
        cmdTitle.setAlignmentX(0.5F);
        cmdTitle.setFocusable(false);
        cmdTitle.setMaximumSize(new java.awt.Dimension(280, 16));
        cmdTitle.setMinimumSize(new java.awt.Dimension(120, 16));
        cmdTitle.setName("cmdTitle"); // NOI18N
        cmdTitle.setPreferredSize(new java.awt.Dimension(33, 12));
        cmdTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cmdTitleMousePressed(evt);
            }
        });
        cmdTitle.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cmdTitleMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cmdTitleMouseMoved(evt);
            }
        });
        cmdTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTitleActionPerformed(evt);
            }
        });
        getContentPane().add(cmdTitle);

        panelNotes.setName("panelNotes"); // NOI18N
        panelNotes.setLayout(new javax.swing.BoxLayout(panelNotes, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel.setMinimumSize(new java.awt.Dimension(272, 52));
        jPanel.setName("jPanel"); // NOI18N
        jPanel.setPreferredSize(new java.awt.Dimension(168, 52));
        jPanel.setLayout(new javax.swing.BoxLayout(jPanel, javax.swing.BoxLayout.LINE_AXIS));

        alterationPanel.setName("alterationPanel"); // NOI18N
        jPanel.add(alterationPanel);

        dureePanel.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        dureePanel.setName("dureePanel"); // NOI18N
        jPanel.add(dureePanel);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        cmdTriolets.setIcon(resourceMap.getIcon("cmdTriolets.icon")); // NOI18N
        cmdTriolets.setFocusable(false);
        cmdTriolets.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdTriolets.setName("cmdTriolets"); // NOI18N
        cmdTriolets.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdTriolets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTrioletsActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdTriolets);

        jPanel.add(jToolBar2);

        panelNotes.add(jPanel);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(190, 66));
        jToolBar1.setName("jToolBar1"); // NOI18N

        cmdVoixGrouper.setIcon(resourceMap.getIcon("cmdVoixGrouper.icon")); // NOI18N
        cmdVoixGrouper.setFocusable(false);
        cmdVoixGrouper.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdVoixGrouper.setName("cmdVoixGrouper"); // NOI18N
        cmdVoixGrouper.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdVoixGrouper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdVoixGrouperActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdVoixGrouper);

        cmdVoixIndependante.setIcon(resourceMap.getIcon("cmdVoixIndependante.icon")); // NOI18N
        cmdVoixIndependante.setFocusable(false);
        cmdVoixIndependante.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdVoixIndependante.setName("cmdVoixIndependante"); // NOI18N
        cmdVoixIndependante.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdVoixIndependante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdVoixIndependanteActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdVoixIndependante);

        menuNoteLieeALaSuivante.setIcon(resourceMap.getIcon("menuNoteLieeALaSuivante.icon")); // NOI18N
        menuNoteLieeALaSuivante.setFocusable(false);
        menuNoteLieeALaSuivante.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menuNoteLieeALaSuivante.setName("menuNoteLieeALaSuivante"); // NOI18N
        menuNoteLieeALaSuivante.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        menuNoteLieeALaSuivante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuNoteLieeALaSuivanteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuNoteLieeALaSuivanteMouseExited(evt);
            }
        });
        menuNoteLieeALaSuivante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNoteLieeALaSuivanteActionPerformed(evt);
            }
        });
        jToolBar1.add(menuNoteLieeALaSuivante);

        selectionHampePanel.setName("selectionHampePanel"); // NOI18N
        jToolBar1.add(selectionHampePanel);

        jPanel2.add(jToolBar1);

        panelNotes.add(jPanel2);

        getContentPane().add(panelNotes);

        panelBarreDeMesure.setName("panelBarreDeMesure"); // NOI18N
        getContentPane().add(panelBarreDeMesure);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    Point lastPoint;
    
    private void cmdTitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdTitleMousePressed
        lastPoint = evt.getPoint();
}//GEN-LAST:event_cmdTitleMousePressed

    private void cmdTitleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdTitleMouseDragged
        setBounds(getBounds().x + evt.getPoint().x - lastPoint.x,
                getBounds().y + evt.getPoint().y - lastPoint.y,
                getBounds().width, getBounds().height);
        //lastPoint = evt.getPoint();
}//GEN-LAST:event_cmdTitleMouseDragged

    private void cmdTitleMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdTitleMouseMoved
        
}//GEN-LAST:event_cmdTitleMouseMoved

    private void cmdTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdTitleActionPerformed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        resizeUndecoratedDecorator.onPress(evt);
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        resizeUndecoratedDecorator.resizeWindowHeight(evt);
        resizeUndecoratedDecorator.resizeWindowWidth(evt);
    }//GEN-LAST:event_formMouseDragged

    private void cmdVoixGrouperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdVoixGrouperActionPerformed
        getHistoire().executer(new PartitionActionSelectionVoixSet(getPartitionPanel().getSelection(), new Voix()));
        getPartitionPanel().calculerModificationSelection();
        getPartitionPanel().repaint();
}//GEN-LAST:event_cmdVoixGrouperActionPerformed

    private void cmdVoixIndependanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdVoixIndependanteActionPerformed
        getHistoire().executer(new PartitionActionSelectionPasDeVoix(getPartitionPanel().getSelection()));
        getPartitionPanel().calculerModificationSelection();
        getPartitionPanel().repaint();
}//GEN-LAST:event_cmdVoixIndependanteActionPerformed

    private void menuNoteLieeALaSuivanteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuNoteLieeALaSuivanteMouseEntered
        //  partitionActionBouton = new PartitionActionSelectionNotesLierAuxSuivantes(getPartitionPanel().getSelection(), true);
        //    partitionActionBouton.executer(getPartitionDonnees());
        //    getPartitionPanel().calculerModificationSelection();
}//GEN-LAST:event_menuNoteLieeALaSuivanteMouseEntered

    private void menuNoteLieeALaSuivanteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuNoteLieeALaSuivanteMouseExited
        //    partitionActionBouton.executerInverse(getPartitionDonnees());
        //    getPartitionPanel().calculerModificationSelection();
}//GEN-LAST:event_menuNoteLieeALaSuivanteMouseExited

    private void menuNoteLieeALaSuivanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNoteLieeALaSuivanteActionPerformed
        Note note = getPartitionPanel().getSelection().getNotes().iterator().next();
        
        getHistoire().executer(new PartitionActionSelectionNotesLierAuxSuivantes(getPartitionPanel().getSelection(),
                !note.isLieeALaSuivante()));
        getPartitionPanel().calculerModificationSelection();
        getPartitionPanel().repaint();
        
        
    }//GEN-LAST:event_menuNoteLieeALaSuivanteActionPerformed

    private void cmdTrioletsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTrioletsActionPerformed
        actionDureeTrioletsBoiteDeDialogue();
    }//GEN-LAST:event_cmdTrioletsActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        ((JFrame) getParent()).toFront();
    }//GEN-LAST:event_formFocusGained

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                PaletteFloatableDialog dialog = new PaletteFloatableDialog(new javax.swing.JFrame(), null, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                
                dialog.setVisible(true);
            }
        });
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Action
    public void actionDureeTrioletsBoiteDeDialogue() {
        final int nombre;
        
        if(controller.isSelection())
        {
            nombre = controller.getSelection().getMomentsNombre();
        }
        else
            nombre = 3;
        
        DivisionsExceptionnellesDialog d = new DivisionsExceptionnellesDialog(null, true, nombre);
        d.setVisible(true);
        

        if(!d.isAnnule())
        {
            controller.dureeEntreeTraiter(d.getDuree());
        }
        
    }


    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private musicwriter.guiswing.AlterationPanel alterationPanel;
    private javax.swing.JButton cmdTitle;
    private javax.swing.JButton cmdTriolets;
    private javax.swing.JButton cmdVoixGrouper;
    private javax.swing.JButton cmdVoixIndependante;
    private musicwriter.guiswing.DureePanel dureePanel;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToggleButton menuNoteLieeALaSuivante;
    private musicwriter.guiswing.PanelBarreDeMesure panelBarreDeMesure;
    private javax.swing.JPanel panelNotes;
    private musicwriter.guiswing.HampePanel selectionHampePanel;
    // End of variables declaration//GEN-END:variables

    private Controller getPartitionPanel() {
        return controller;
    }
    
    
    private Histoire getHistoire()
    {
        return controller.getHistoire();
    }
}
