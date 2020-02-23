/*
 * PartitionPanel.java
 *
 * Created on 2 septembre 2008, 17:45
 */
package musicwriter.guiswing;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import musicwriter.Erreur;
import musicwriter.Resource;
import musicwriter.ResourceMap;
import musicwriter.controller.*;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.PartitionActionElementMusicalAjouter;
import musicwriter.donnees.actions.PartitionActionElementMusicalSupprimer;
import musicwriter.donnees.actions.PartitionActionMesureAjouterApres;
import musicwriter.donnees.actions.PartitionActionMesureAjouterAvant;
import musicwriter.gui.Graphics;
import musicwriter.gui.Panel;
import musicwriter.guiswing.dialogs.*;
import musicwriter.guiswing.palette.PartitionPanelDropTargetListener;
import org.jdesktop.application.Action;

/**
 *
 * @author  proprietaire
 */
public final class PartitionPanel extends ZoomablePanel implements Panel {
     
    final private Controller controller;
    
    

    
  

    public void modifierSourisCurseur(Cursor cursor) {
        setCursor(cursor);
    }

    private void modifierSourisCurseur(String img, Point hotSpot) {
        try {
            //recupere le Toolkit
            Toolkit tk = Toolkit.getDefaultToolkit();
            //sur ce dernier lire le fichier avec "getClass().getRessource" pour
            //pouvoir l'ajouter a un .jar
            Image image = ImageLoaderSwing.getImageIcon(img).getImage();//tk.getImage(getClass().getResource(img));
            //modifi le curseur avec la nouvelle image,en le posissionant grace hotSpot
            //et en lui donnant le nom "X"
            Cursor c = tk.createCustomCursor(image, hotSpot, "X");


            //puis on l'associe au Panel
            setCursor(c);
        } catch (Exception e) {
            Erreur.message("erreur dans modifierSourisCurseur avec img = " + img);
        }
    }

    
    public void modifierSourisCurseurCrayon() {
        modifierSourisCurseur("crayon.png", new Point(2, 30));
    }

    public void modifierSourisCurseurEcrireNote() {
        modifierSourisCurseur("minicroix.png", new Point(16, 16));
    }

    public void modifierSourisCurseurCrayonTraitCroche() {
        modifierSourisCurseur("crayon.png", new Point(5, 31));
    }

    public void modifierSourisCurseurGomme() {
        modifierSourisCurseur("gomme.png", new Point(8, 26));
    }

    public void modifierSourisCurseurMainDoigt() {
        modifierSourisCurseur("maindoigt.png", new Point(4, 0));
    }

    public void modifierSourisCurseurMainDeselectionner() {
        setCursor(Cursor.getDefaultCursor());
        //modifierSourisCurseur(sourisCurseurChemin + "maindoigt.png", new Point(4, 0));
    }

    /**
     * affiche un curseur en forme de main, un curseur qui est prêt à prendre des notes et autres éléments
     * musicaux (pour les déplacer)
     */
    public void modifierSourisCurseurMainOuverte() {
        modifierSourisCurseur("mainouverte.png", new Point(4, 0));
    }

    /**
     * affiche un curseur qui prend des noteset autres éléments
     * musicaux (pour les déplacer)
     */
    public void modifierSourisCurseurMainQuiPrend() {
        modifierSourisCurseur("mainquiprend.png", new Point(4, 8));
    }

    public void modifierSourisCurseurMainQuiPrendPlus() {
        modifierSourisCurseur("mainquiprendplus.png", new Point(4, 8));
    }

    public void modifierSourisCurseurLecturePosition() {
        modifierSourisCurseur("curseur_lecture_position.png", new Point(4, 2));
    }

    

    
    /** Creates new form PartitionPanel
     */
    public PartitionPanel() {
        initComponents();
        this.controller = new Controller(this);
        DropTargetListener droplistener = new PartitionPanelDropTargetListener();
        setDropTarget(new DropTarget(this, droplistener));
        modifierSourisCurseurEcrireNote();
        zoomMouseListenerAdapt();
        
        final PartitionPanel partitionPanel = this;
        controller.addControllerListener(new ControllerListener() {

            @Override
            public void whenUpdate(Controller controller) {
                repaint();
            }

            @Override
            public void whenScroll(musicwriter.gui.Rectangle rectangle) {
                if(!getVisibleRect().contains(getRectanglePhysical(rectangle)))
                 scrollRectToVisible(getRectanglePhysical(rectangle));

            }

            @Override
            public void whenCursorOverSomething() {
                modifierSourisCurseur(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void whenCursorOverNothing() {
                modifierSourisCurseur(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
            
            
            
               
            
        });

    }

 @Action
    public void actionMesureAddBefore() {
        final Moment momentDebutMesure = getController().getPartitionDonnees()
                          .getMesureMomentDebut(getController().getCurseurSouris().getMoment());

        final String reponse;
	final String message = "Combien de mesures voulez-vous insérer avant ?";
	reponse = JOptionPane.showInputDialog(this, message);
	
        final int nombre = Integer.parseInt(reponse);

        getController().executer(
                new PartitionActionMesureAjouterAvant(
                       getController().getPartitionDonnees(),
                       getController().getCurseurSouris().getMoment(),
                       nombre));

        getController().calculer(momentDebutMesure);
        
    }

    @Action
    public void actionMesureAddAfter() {
        final Moment momentDebutMesure = getController().getPartitionDonnees()
                          .getMesureSuivanteMomentDebut(getController().getCurseurSouris().getMoment());

        final String reponse;
	final String message = "Combien de mesures voulez-vous insérer après ?";
	reponse = JOptionPane.showInputDialog(this, message);

        final int nombre = Integer.parseInt(reponse);

        getController().executer(
                new PartitionActionMesureAjouterApres(
                       getController().getPartitionDonnees(),
                       getController().getCurseurSouris().getMoment(),
                       nombre));

        getController().calculer(momentDebutMesure);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuPrincipal = new javax.swing.JPopupMenu();
        mnuLecture = new javax.swing.JMenuItem();
        mnuTempsInserer = new javax.swing.JMenuItem();
        mnuTempsSupprimer = new javax.swing.JMenuItem();
        mnuClefInserer = new javax.swing.JMenuItem();
        mnuInsererChangementTonalite = new javax.swing.JMenuItem();
        mnuMesure = new javax.swing.JMenu();
        mnuMesureCorriger = new javax.swing.JMenuItem();
        mnuMesureSignatureChanger = new javax.swing.JMenuItem();
        mnuMesureAddBefore = new javax.swing.JMenuItem();
        mnuMesureAddAfter = new javax.swing.JMenuItem();
        mnuInsererTempo = new javax.swing.JMenuItem();
        mnuNotes = new javax.swing.JMenu();
        mnuNotesLierAuxSuivantes = new javax.swing.JMenuItem();
        mnuNotesNePasLierAuxSuivantes = new javax.swing.JMenuItem();
        mnuSelection = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnuTexteInserer = new javax.swing.JMenuItem();
        jPopupMenuPartie = new javax.swing.JPopupMenu();
        mnuInstrumentsModifier = new javax.swing.JMenuItem();
        mnuPartieSelectionnerTout = new javax.swing.JMenuItem();

        jPopupMenuPrincipal.setComponentPopupMenu(jPopupMenuPrincipal);
        jPopupMenuPrincipal.setName("jPopupMenuPrincipal"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(PartitionPanel.class);
        mnuLecture.setIcon(resourceMap.getIcon("mnuLecture.icon")); // NOI18N
        mnuLecture.setText(resourceMap.getString("mnuLecture.text")); // NOI18N
        mnuLecture.setName("mnuLecture"); // NOI18N
        mnuLecture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLectureActionPerformed(evt);
            }
        });
        jPopupMenuPrincipal.add(mnuLecture);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(PartitionPanel.class, this);
        mnuTempsInserer.setAction(actionMap.get("tempsInserer")); // NOI18N
        mnuTempsInserer.setIcon(resourceMap.getIcon("mnuTempsInserer.icon")); // NOI18N
        mnuTempsInserer.setText(resourceMap.getString("mnuTempsInserer.text")); // NOI18N
        mnuTempsInserer.setName("mnuTempsInserer"); // NOI18N
        jPopupMenuPrincipal.add(mnuTempsInserer);

        mnuTempsSupprimer.setIcon(resourceMap.getIcon("mnuTempsSupprimer.icon")); // NOI18N
        mnuTempsSupprimer.setText(resourceMap.getString("mnuTempsSupprimer.text")); // NOI18N
        mnuTempsSupprimer.setName("mnuTempsSupprimer"); // NOI18N
        mnuTempsSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTempsSupprimerActionPerformed(evt);
            }
        });
        jPopupMenuPrincipal.add(mnuTempsSupprimer);

        mnuClefInserer.setAction(actionMap.get("clefPorteeInserer")); // NOI18N
        mnuClefInserer.setIcon(resourceMap.getIcon("mnuClefInserer.icon")); // NOI18N
        mnuClefInserer.setText(resourceMap.getString("mnuClefInserer.text")); // NOI18N
        mnuClefInserer.setName("mnuClefInserer"); // NOI18N
        jPopupMenuPrincipal.add(mnuClefInserer);

        mnuInsererChangementTonalite.setIcon(resourceMap.getIcon("mnuInsererChangementTonalite.icon")); // NOI18N
        mnuInsererChangementTonalite.setText(resourceMap.getString("mnuInsererChangementTonalite.text")); // NOI18N
        mnuInsererChangementTonalite.setName("mnuInsererChangementTonalite"); // NOI18N
        mnuInsererChangementTonalite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuInsererChangementTonaliteActionPerformed(evt);
            }
        });
        jPopupMenuPrincipal.add(mnuInsererChangementTonalite);

        mnuMesure.setIcon(resourceMap.getIcon("mnuMesure.icon")); // NOI18N
        mnuMesure.setText(resourceMap.getString("mnuMesure.text")); // NOI18N
        mnuMesure.setName("mnuMesure"); // NOI18N

        mnuMesureCorriger.setIcon(resourceMap.getIcon("mnuMesureCorriger.icon")); // NOI18N
        mnuMesureCorriger.setText(resourceMap.getString("mnuMesureCorriger.text")); // NOI18N
        mnuMesureCorriger.setName("mnuMesureCorriger"); // NOI18N
        mnuMesureCorriger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMesureCorrigerActionPerformed(evt);
            }
        });
        mnuMesure.add(mnuMesureCorriger);

        mnuMesureSignatureChanger.setIcon(resourceMap.getIcon("mnuMesureSignatureChanger.icon")); // NOI18N
        mnuMesureSignatureChanger.setText(resourceMap.getString("mnuMesureSignatureChanger.text")); // NOI18N
        mnuMesureSignatureChanger.setName("mnuMesureSignatureChanger"); // NOI18N
        mnuMesureSignatureChanger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMesureSignatureChangerActionPerformed(evt);
            }
        });
        mnuMesure.add(mnuMesureSignatureChanger);

        mnuMesureAddBefore.setAction(actionMap.get("actionMesureAddBefore")); // NOI18N
        mnuMesureAddBefore.setIcon(resourceMap.getIcon("mnuMesureAddBefore.icon")); // NOI18N
        mnuMesureAddBefore.setText(resourceMap.getString("mnuMesureAddBefore.text")); // NOI18N
        mnuMesureAddBefore.setName("mnuMesureAddBefore"); // NOI18N
        mnuMesure.add(mnuMesureAddBefore);

        mnuMesureAddAfter.setAction(actionMap.get("actionMesureAddAfter")); // NOI18N
        mnuMesureAddAfter.setIcon(resourceMap.getIcon("mnuMesureAddAfter.icon")); // NOI18N
        mnuMesureAddAfter.setText(resourceMap.getString("mnuMesureAddAfter.text")); // NOI18N
        mnuMesureAddAfter.setName("mnuMesureAddAfter"); // NOI18N
        mnuMesure.add(mnuMesureAddAfter);

        jPopupMenuPrincipal.add(mnuMesure);

        mnuInsererTempo.setAction(actionMap.get("tempoInserer")); // NOI18N
        mnuInsererTempo.setIcon(resourceMap.getIcon("mnuInsererTempo.icon")); // NOI18N
        mnuInsererTempo.setText(resourceMap.getString("mnuInsererTempo.text")); // NOI18N
        mnuInsererTempo.setName("mnuInsererTempo"); // NOI18N
        jPopupMenuPrincipal.add(mnuInsererTempo);

        mnuNotes.setIcon(resourceMap.getIcon("mnuNotes.icon")); // NOI18N
        mnuNotes.setText(resourceMap.getString("mnuNotes.text")); // NOI18N
        mnuNotes.setName("mnuNotes"); // NOI18N

        mnuNotesLierAuxSuivantes.setAction(actionMap.get("notesLierAuxSuivantes")); // NOI18N
        mnuNotesLierAuxSuivantes.setIcon(resourceMap.getIcon("mnuNotesLierAuxSuivantes.icon")); // NOI18N
        mnuNotesLierAuxSuivantes.setText(resourceMap.getString("mnuNotesLierAuxSuivantes.text")); // NOI18N
        mnuNotesLierAuxSuivantes.setName("mnuNotesLierAuxSuivantes"); // NOI18N
        mnuNotes.add(mnuNotesLierAuxSuivantes);

        mnuNotesNePasLierAuxSuivantes.setAction(actionMap.get("notesNePasLierAuxSuivantes")); // NOI18N
        mnuNotesNePasLierAuxSuivantes.setIcon(resourceMap.getIcon("mnuNotesNePasLierAuxSuivantes.icon")); // NOI18N
        mnuNotesNePasLierAuxSuivantes.setText(resourceMap.getString("mnuNotesNePasLierAuxSuivantes.text")); // NOI18N
        mnuNotesNePasLierAuxSuivantes.setName("mnuNotesNePasLierAuxSuivantes"); // NOI18N
        mnuNotes.add(mnuNotesNePasLierAuxSuivantes);

        jPopupMenuPrincipal.add(mnuNotes);

        mnuSelection.setIcon(resourceMap.getIcon("mnuSelection.icon")); // NOI18N
        mnuSelection.setText(resourceMap.getString("mnuSelection.text")); // NOI18N
        mnuSelection.setName("mnuSelection"); // NOI18N

        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuSelection.add(jMenuItem2);

        jPopupMenuPrincipal.add(mnuSelection);

        mnuTexteInserer.setAction(actionMap.get("actionTexteInserer")); // NOI18N
        mnuTexteInserer.setText(resourceMap.getString("mnuTexteInserer.text")); // NOI18N
        mnuTexteInserer.setName("mnuTexteInserer"); // NOI18N
        jPopupMenuPrincipal.add(mnuTexteInserer);

        jPopupMenuPartie.setName("jPopupMenuPartie"); // NOI18N

        mnuInstrumentsModifier.setText(resourceMap.getString("mnuInstrumentsModifier.text")); // NOI18N
        mnuInstrumentsModifier.setName("mnuInstrumentsModifier"); // NOI18N
        mnuInstrumentsModifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuInstrumentsModifierActionPerformed(evt);
            }
        });
        jPopupMenuPartie.add(mnuInstrumentsModifier);

        mnuPartieSelectionnerTout.setText(resourceMap.getString("mnuPartieSelectionnerTout.text")); // NOI18N
        mnuPartieSelectionnerTout.setName("mnuPartieSelectionnerTout"); // NOI18N
        mnuPartieSelectionnerTout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPartieSelectionnerToutActionPerformed(evt);
            }
        });
        jPopupMenuPartie.add(mnuPartieSelectionnerTout);

        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setComponentPopupMenu(jPopupMenuPrincipal);
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 582, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 706, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

private ControllerMouseEvent getControllerMouseEventFromAWTMouseEvent(java.awt.event.MouseEvent evt)
{
    return new ControllerMouseEvent(evt.getX(), evt.getY(), evt.isControlDown(), evt.isShiftDown(), evt.getButton() == MouseEvent.BUTTON2, evt.getClickCount());
}
    
    
private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
// quand on appuie sur un bouton de la souris

    controller.formMousePressed(getControllerMouseEventFromAWTMouseEvent(evt));

}//GEN-LAST:event_formMousePressed

private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
// TODO add your handling code here:
    controller.formMouseReleased(getControllerMouseEventFromAWTMouseEvent(evt));
    repaint();
}//GEN-LAST:event_formMouseReleased

private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
// quand on bouge la souris sans qu'aucun bouton n'est appuyé

    controller.formMouseMoved(getControllerMouseEventFromAWTMouseEvent(evt));
    setComponentPopupMenu(jPopupMenuPrincipal);
    


}//GEN-LAST:event_formMouseMoved

private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
    controller.formMouseDragged(getControllerMouseEventFromAWTMouseEvent(evt));

}//GEN-LAST:event_formMouseDragged

    /**
     *
     * @param el
     *
     * affiche une boite de dialogue qui permet de modifier la tonalité de l'élement musical
     * si c'est ok, ça change la tonalite de l'élément musical el, ça met à jour l'écran
     * ça s'occupe de faire annuler tout gentil et tout et tout :)
     */
    public void elementMusicalTonaliteChangerTonaliteBoiteDeDialogue(ElementMusicalChangementTonalite el) {
        TonaliteDialog d = new TonaliteDialog(null, true);
        d.setVisible(true);

        if (d.getTonalite() != null) {
            controller.executer(new PartitionActionElementMusicalSupprimer(el));
            controller.executer(new PartitionActionElementMusicalAjouter(
                    new ElementMusicalChangementTonalite(el.getDebutMoment(),
                    d.getTonalite())));
            controller.selectionAbandonner();
            controller.calculer(el.getDebutMoment());
        }
    }

    public void elementMusicalMesureSignatureChangerTonaliteBoiteDeDialogue(ElementMusicalChangementMesureSignature el) {
        MesureSignatureChangementDialog d = new MesureSignatureChangementDialog(null, true);
        d.setVisible(true);

        if (d.getSignature() != null) {
            controller.executer(new PartitionActionElementMusicalSupprimer(el));
            controller.executer(new PartitionActionElementMusicalAjouter(
                    new ElementMusicalChangementMesureSignature(el.getDebutMoment(),
                    d.getSignature())));
            controller.selectionAbandonner();
            controller.calculer(el.getDebutMoment());
        }
    }

    public void elementMusicalTempoChangerTempoBoiteDeDialogue(ElementMusicalTempo el) {
        TempoDialog d = new TempoDialog(null, true, el);
        d.setVisible(true);

        if (d.getTempo() != null) {
            controller.executer(new PartitionActionElementMusicalSupprimer(el));
            controller.executer(new PartitionActionElementMusicalAjouter(
                    new ElementMusicalTempo(el.getDebutMoment(),
                    d.getTempo().getNbNoiresEnUneMinute(), d.getTempo().getNom())));
            controller.selectionAbandonner();
            controller.calculer(el.getDebutMoment());
        }
    }

    public void elementMusicalClefChangerClefBoiteDeDialogue(ElementMusicalClef el) {
        ClefDialog d = new ClefDialog(null, true);
        d.setVisible(true);

        if (d.getClef() != null) {
            controller.executer(new PartitionActionElementMusicalSupprimer(el));
            controller.executer(new PartitionActionElementMusicalAjouter(
                    new ElementMusicalClef(el.getDebutMoment(),
                    controller.getCurseurSouris().getPortee(),
                    d.getClef())));
            controller.selectionAbandonner();
            controller.calculer(el.getDebutMoment());
        }
    }

    public void elementMusicalTexteChangerClefBoiteDeDialogue(ElementMusicalTexte el) {
//        TexteDialog d = new TexteDialog(null, true);
//        d.setVisible(true);
//
//        if (!d.isAnnule()) {
//            histoire.executer(new PartitionActionElementMusicalSupprimer(el));
//            histoire.executer(new PartitionActionElementMusicalAjouter(
//                    new ElementMusicalTexte(el.getCurseur(), d.getTexte())));
//            controller.selectionAbandonner();
//            calculer(el.getDebutMoment());
//        }
       // setMode(new PartitionPanelModeTexte(this, el));
    }

private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
// TODO add your handling code here:
    requestFocus();
    controller.formMouseClicked(getControllerMouseEventFromAWTMouseEvent(evt));
    if (evt.getClickCount() > 1) {

        if (controller.getCurseurSouris().getElementMusical() instanceof ElementMusicalChangementMesureSignature) {
            elementMusicalMesureSignatureChangerTonaliteBoiteDeDialogue((ElementMusicalChangementMesureSignature) controller.getCurseurSouris().getElementMusical());
        } else if (controller.getCurseurSouris().getElementMusical() instanceof ElementMusicalChangementTonalite) {
            elementMusicalTonaliteChangerTonaliteBoiteDeDialogue((ElementMusicalChangementTonalite) controller.getCurseurSouris().getElementMusical());
        } else if (controller.getCurseurSouris().getElementMusical() instanceof ElementMusicalTempo) {
            elementMusicalTempoChangerTempoBoiteDeDialogue((ElementMusicalTempo) controller.getCurseurSouris().getElementMusical());
        } else if (controller.getCurseurSouris().getElementMusical() instanceof ElementMusicalClef) {
            elementMusicalClefChangerClefBoiteDeDialogue((ElementMusicalClef) controller.getCurseurSouris().getElementMusical());
        } else if (controller.getCurseurSouris().getElementMusical() instanceof ElementMusicalTexte) {
            elementMusicalTexteChangerClefBoiteDeDialogue((ElementMusicalTexte) controller.getCurseurSouris().getElementMusical());
        } else if (controller.getCurseurSouris().getElementMusical() instanceof Note) {
            if (controller.isSelection()) {
                controller.setSelection(controller.getSelection().getSelectionMemeHauteur(controller.getCurseurSouris().getHauteur()));
                repaint();
            }
        }
    }

}//GEN-LAST:event_formMouseClicked



private ControllerKeyEvent keyEventSwingToControllerKeyEvent(java.awt.event.KeyEvent evt)
{
    return new ControllerKeyEvent(evt.getKeyChar(), evt.getKeyCode(), evt.isControlDown(), evt.isShiftDown());
}



private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
// TODO add your handling code here:
    controller.formKeyPressed(keyEventSwingToControllerKeyEvent(evt));

}//GEN-LAST:event_formKeyPressed

private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
    controller.formKeyTyped(keyEventSwingToControllerKeyEvent(evt));
}//GEN-LAST:event_formKeyTyped

private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
    controller.formKeyReleased(keyEventSwingToControllerKeyEvent(evt));

}//GEN-LAST:event_formKeyReleased

private void mnuInsererChangementTonaliteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuInsererChangementTonaliteActionPerformed
    TonaliteDialog d = new TonaliteDialog(null, true);
    d.setVisible(true);

    if (d.getTonalite() != null) {
        controller.executer(new PartitionActionElementMusicalAjouter(
                new ElementMusicalChangementTonalite(controller.getCurseurSouris().getMoment(),
                d.getTonalite())));
        controller.selectionAbandonner();
        controller.calculer(controller.getCurseurSouris().getMoment());
        repaint();
    }

}//GEN-LAST:event_mnuInsererChangementTonaliteActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    controller.selectionPartieSousCurseurSouris();
    
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void mnuMesureCorrigerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMesureCorrigerActionPerformed
    controller.mesureCorrigerSousCurseurSouris();
    


}//GEN-LAST:event_mnuMesureCorrigerActionPerformed

private void mnuLectureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLectureActionPerformed
    controller.playFromCurseurSouris();

}//GEN-LAST:event_mnuLectureActionPerformed

private void mnuMesureSignatureChangerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMesureSignatureChangerActionPerformed
    MesureSignatureChangementDialog d = new MesureSignatureChangementDialog(null, true);
    d.setVisible(true);

    if (d.getSignature() != null) {
        controller.executer(new PartitionActionElementMusicalAjouter(
                new ElementMusicalChangementMesureSignature(controller.getCurseurSouris().getMoment(),
                d.getSignature())));
        controller.selectionAbandonner();
        controller.calculer(controller.getCurseurSouris().getMoment());
    }

}//GEN-LAST:event_mnuMesureSignatureChangerActionPerformed



private void mnuTempsSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTempsSupprimerActionPerformed
    controller.tempsSupprimerSousCurseur(controller.getCurseurSouris());
    
}//GEN-LAST:event_mnuTempsSupprimerActionPerformed

private void mnuPartieSelectionnerToutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPartieSelectionnerToutActionPerformed
    controller.selectionPartieSousCurseurSouris();
}//GEN-LAST:event_mnuPartieSelectionnerToutActionPerformed

private void mnuInstrumentsModifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuInstrumentsModifierActionPerformed
//    PartitionPartiesDialog d = new PartitionPartiesDialog(null, true, getPartitionDonnees());
//    d.setVisible(true);
//    getPartitionVue().miseEnPageCalculer();
//    repaint();
}//GEN-LAST:event_mnuInstrumentsModifierActionPerformed


public void gererZoomOuDefilementWheelMoved(java.awt.event.MouseWheelEvent evt) {                                     
    if(evt.isControlDown())
    {
        double newZoom = getZoom() - ((double) evt.getWheelRotation())/10.0;
        if((newZoom < 0.2) || (newZoom > 2.0))
             return;


        Point pointPhysical = evt.getPoint();
        musicwriter.gui.Point pointLogical = getPointLogical(pointPhysical);

        int vx = ((JViewport) getParent()).getViewPosition().x;
        int vy = ((JViewport) getParent()).getViewPosition().y;

        setZoom(newZoom);

        Point newPointPhysical = getPointPhysical(pointLogical);
        ((JViewport) getParent()).setViewPosition(new Point(vx + newPointPhysical.x - pointPhysical.x,
                   vy +  newPointPhysical.y - pointPhysical.y));

        
        

        

        


        
        repaint();
    }
    else
    {
        int y = ((JViewport) getParent()).getViewPosition().y + evt.getWheelRotation() * 32;

        if(y < 0)
            return;
        
        ((JViewport) getParent()).setViewPosition(new Point(((JViewport) getParent()).getViewPosition().x,
                   y));

        
        //setLocation(getLocation().x, getLocation().y - evt.getWheelRotation() * 32);
    }
}                 

private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
    gererZoomOuDefilementWheelMoved(evt);
}//GEN-LAST:event_formMouseWheelMoved



    private void dessinerArrierePlan(java.awt.Graphics g) {
        g.setColor(Color.white);
        g.fillRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);

    }

    @Override
    protected void paintComponent(java.awt.Graphics awtGraphics) {
       
        Graphics g = new GraphicsSwing(awtGraphics);

        dessinerArrierePlan(awtGraphics);

        super.paintComponent(awtGraphics);
        controller.paintComponent(g, getRectangleLogical(getVisibleRect()));
        

        //pour que la taille se redimensionne tout le temps
        //(pour que la barre de défilement dès qu'on en a besoin)
        
        redimensionner();

    }

    
    
//    Dimension lastSize = new Dimension(0, 0);
//
//    public void redimensionner() {
//        Dimension d = getPreferredSize();
//        if(!lastSize.equals(d))
//        {
//            lastSize = d;
//            super.setSize(d);
//        }
//           
//    }
//    
    
    
    private void redimensionner()
    {
        getParent().doLayout();
    }
            
    
    

    
    @Override
    public Dimension getPreferredSize() {
        return getDimensionPhysical(controller.getPartitionVue().getDimension());
    }

    @Action
    public void tempoInserer() {
        TempoDialog d = new TempoDialog(null, true);
        d.setVisible(true);

        if (d.getTempo() != null) {
            controller.executer(new PartitionActionElementMusicalAjouter(
                    new ElementMusicalTempo(controller.getCurseurSouris().getMoment(),
                    d.getTempo().getNbNoiresEnUneMinute(), d.getTempo().getNom())));
            controller.selectionAbandonner();
            controller.calculer(controller.getCurseurSouris().getMoment());
            repaint();
        }

    }

    @Action
    public void tempsInserer() {
        controller.tempsInserer(); 
        
        //        histoire.executer(new PartitionActionInsererTemps(getCurseurSourisMomentBarre(), new Duree(new Rational(1, 1)).getFinMoment(getCurseurSourisMomentBarre())));
//        getPartitionVue().miseEnPageCalculer(getCurseurSourisMomentBarre());
//        repaint();
    }

    @Action
    public void notesLierAuxSuivantes() {
        controller.notesLierAuxSuivantes();
    }

    @Action
    public void notesNePasLierAuxSuivantes() {
        controller.notesNePasLierAuxSuivantes();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu jPopupMenuPartie;
    private javax.swing.JPopupMenu jPopupMenuPrincipal;
    private javax.swing.JMenuItem mnuClefInserer;
    private javax.swing.JMenuItem mnuInsererChangementTonalite;
    private javax.swing.JMenuItem mnuInsererTempo;
    private javax.swing.JMenuItem mnuInstrumentsModifier;
    private javax.swing.JMenuItem mnuLecture;
    private javax.swing.JMenu mnuMesure;
    private javax.swing.JMenuItem mnuMesureAddAfter;
    private javax.swing.JMenuItem mnuMesureAddBefore;
    private javax.swing.JMenuItem mnuMesureCorriger;
    private javax.swing.JMenuItem mnuMesureSignatureChanger;
    private javax.swing.JMenu mnuNotes;
    private javax.swing.JMenuItem mnuNotesLierAuxSuivantes;
    private javax.swing.JMenuItem mnuNotesNePasLierAuxSuivantes;
    private javax.swing.JMenuItem mnuPartieSelectionnerTout;
    private javax.swing.JMenu mnuSelection;
    private javax.swing.JMenuItem mnuTempsInserer;
    private javax.swing.JMenuItem mnuTempsSupprimer;
    private javax.swing.JMenuItem mnuTexteInserer;
    // End of variables declaration//GEN-END:variables



    @Action
    public void clefPorteeInserer() {
        ClefDialog d = new ClefDialog(null, true);
        d.setVisible(true);

        if (d.getClef() != null) {
            controller.selectionAbandonner();
            controller.executer(new PartitionActionElementMusicalAjouter(
                    new ElementMusicalClef(controller.getCurseurSouris().getMoment(),
                    controller.getCurseurSouris().getPortee(),
                    d.getClef())));

            controller.calculer(controller.getCurseurSouris().getMoment());
            repaint();
        }
    }

    @Action
    public void actionTexteInserer() {
        TexteDialog d = new TexteDialog(null, true);
        d.setVisible(true);

        if (!d.isAnnule()) {
            controller.selectionAbandonner();
            controller.executer(new PartitionActionElementMusicalAjouter(
                    new ElementMusicalTexte(controller.getCurseurSouris(),
                    d.getTexte())));

            controller.calculer(controller.getCurseurSouris().getMoment());
            repaint();
        }
    }

    public void setElementsMusicauxDeplacer(Selection selection) {
        controller.setElementsMusicauxDeplacer(selection);

    }

    /**
     *
     * @param evt
     * @return vrai ssi la touche enfoncée correspond à une note saisie ("a" à "h")
     * 
     */
    private boolean isClavierToucheNoteSaisie(KeyEvent evt) {
        return ('a' <= evt.getKeyChar()) && (evt.getKeyChar() <= 'g');
    }

    private int getClavierToucheNoteSaisieNumero(KeyEvent evt) {
        switch (evt.getKeyChar()) {
            case 'c':
                return 0;
            case 'd':
                return 1;
            case 'e':
                return 2;
            case 'f':
                return 3;
            case 'g':
                return 4;
            case 'a':
                return 5;
            case 'b':
                return 6;
            default: {
                Erreur.message("getClavierToucheNoteSaisieNumero");
                return 0;
            }
        }
    }

    public Controller getController() {
        return controller;
    }

    public void setPopupMenuPartie() {
        setComponentPopupMenu(jPopupMenuPartie);
    }
    
    
    
    
    
    
    
    
    private JTextField txtTexte;
    
    
    


    @Override
    public void textEditingAsk(musicwriter.gui.Rectangle rectangle, String texte, Font font, final PanelTextEditingListener panelTextEditingListener) {
        removeAll();
        txtTexte = new JTextField(texte);
        add(txtTexte);
        txtTexte.setFont(getFontPhysical(font));
        Rectangle2D txtTexteRectangleGoodSize = getFontMetrics(txtTexte.getFont()).getStringBounds(txtTexte.getText(),
                                                               getGraphics());

        txtTexte.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                    panelTextEditingListener.whenEditing(keyEventSwingToControllerKeyEvent(e),
                                                         txtTexte.getText(),
                                                         txtTexte.getSelectionStart());

                }

            @Override
            public void keyReleased(KeyEvent ke) {

            }
        });
        final musicwriter.gui.Rectangle rectangleLogique = rectangle;
        rectangleLogique.setWidth(Math.max(rectangleLogique.getWidth(), 16));

        final Rectangle rectanglePhysique = getRectanglePhysical(rectangleLogique);
        rectanglePhysique.width = Math.max(rectanglePhysique.width, (int) txtTexteRectangleGoodSize.getWidth()+16);

        txtTexte.setBounds(rectanglePhysique);
        
        txtTexte.selectAll();
        txtTexte.setVisible(true);
        txtTexte.requestFocusInWindow();
    }

    @Override
    public void textEditingStop() {
        removeAll();
        txtTexte = null;
    }

    public void loadFromResource() {
        
        ResourceMap resourceMap = Resource.createResourceMap(PartitionPanel.class);
        mnuLecture.setText(resourceMap.getString("mnuLecture.text")); // NOI18N
        mnuTempsInserer.setText(resourceMap.getString("tempsInserer.text")); // NOI18N
        mnuTempsSupprimer.setText(resourceMap.getString("mnuTempsSupprimer.text")); // NOI18N
        mnuClefInserer.setText(resourceMap.getString("mnuClefInserer.text")); // NOI18N
        mnuInsererChangementTonalite.setText(resourceMap.getString("mnuInsererChangementTonalite.text")); // NOI18N
        mnuMesure.setText(resourceMap.getString("mnuMesure.text")); // NOI18N
        mnuMesureCorriger.setText(resourceMap.getString("mnuMesureCorriger.text")); // NOI18N
        mnuMesureSignatureChanger.setText(resourceMap.getString("mnuMesureSignatureChanger.text")); // NOI18N
        mnuMesureAddBefore.setText(resourceMap.getString("mnuMesureAddBefore.text")); // NOI18N
        mnuMesureAddAfter.setText(resourceMap.getString("mnuMesureAddAfter.text")); // NOI18N
        mnuInsererTempo.setText(resourceMap.getString("mnuInsererTempo.text")); // NOI18N
        mnuNotes.setText(resourceMap.getString("mnuNotes.text")); // NOI18N
        mnuNotesLierAuxSuivantes.setText(resourceMap.getString("mnuNotesLierAuxSuivantes.text")); // NOI18N
        mnuNotesNePasLierAuxSuivantes.setText(resourceMap.getString("mnuNotesNePasLierAuxSuivantes.text")); // NOI18N
        mnuSelection.setText(resourceMap.getString("mnuSelection.text")); // NOI18N
        mnuTexteInserer.setText(resourceMap.getString("mnuTexteInserer.text")); // NOI18N
        mnuInstrumentsModifier.setText(resourceMap.getString("mnuInstrumentsModifier.text")); // NOI18N
        mnuPartieSelectionnerTout.setText(resourceMap.getString("mnuPartieSelectionnerTout.text")); // NOI18N
        
    }

    
    
}
