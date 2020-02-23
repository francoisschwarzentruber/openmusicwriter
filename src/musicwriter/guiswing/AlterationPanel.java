/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AlterationPanel.java
 *
 * Created on 28 févr. 2010, 22:02:19
 */

package musicwriter.guiswing;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.Icon;
import musicwriter.donnees.Hauteur;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.TransferHandler;
import musicwriter.Erreur;
import musicwriter.donnees.Hauteur.Alteration;
import org.jdesktop.application.Action;

/**
 * Ceci est un Panel où on peut choisir une altération
 * (bécar, bémol, dièse, double-dièse, double-bémol)
 * @author Ancmin
 */
public class AlterationPanel extends javax.swing.JPanel {
    private ActionListener actionListener = null;

    
    
    
    
    public class JButtonAlterationDragSource implements DragGestureListener,
    DragSourceListener {
        private final JToggleButton button;
        private final Alteration alteration;
        
        
  public JButtonAlterationDragSource(JToggleButton button, Hauteur.Alteration alteration) {
    this.button = button;
    this.alteration = alteration;

    // Use the default DragSource
    DragSource dragSource = DragSource.getDefaultDragSource();

    // Create a DragGestureRecognizer and
    // register as the listener
    dragSource.createDefaultDragGestureRecognizer(button,
        DnDConstants.ACTION_COPY_OR_MOVE, this);
  }

  // Implementation of DragGestureListener interface.
  public void dragGestureRecognized(DragGestureEvent dge) {
    
    Transferable transferable = new AlterationTransferable(alteration);
    dge.startDrag(Cursor.getDefaultCursor(), getIconFromAlteration(alteration).getImage(), new Point(0, 0), transferable, this);
  }

  // Implementation of DragSourceListener interface
  public void dragEnter(DragSourceDragEvent dsde) {

  }

  public void dragOver(DragSourceDragEvent dsde) {

  }

  public void dragExit(DragSourceEvent dse) {

  }

  public void dropActionChanged(DragSourceDragEvent dsde) {

  }

  public void dragDropEnd(DragSourceDropEvent dsde) {

  }
    }
    
    
    
    
    
    
    public class AlterationTransferHandler extends TransferHandler {

      /**
       * Crée un objet transférable adapté, donc un PersonneTransferable.
       * Reçoit comme paramètre la source du drag and drop
       * (ce qui permet de partager des PersonneTransferHandler)
       */

      final Alteration alteration;

        public AlterationTransferHandler(Alteration alteration) {
            this.alteration = alteration;

        }

        @Override
        public Icon getVisualRepresentation(Transferable t) {
            return getIconFromAlteration(alteration);
        }
      
      
              
        
        
        @Override
      protected Transferable createTransferable(JComponent c) {
            return new AlterationTransferable(alteration);
      }

      /**
       * Permet de dire si le drag est possible. Dans notre cas, il l'est toujours.
       * cette méthode peut renvoyer COPY, NONE, MOVE, COPY_OR_MOVE.
       * Si NONE est renvoyé, le drag and drop sera impossible.
       */
        @Override
      public int getSourceActions(JComponent c) {
            return COPY;
      }


            /**
     * Teste si les données proposée comportent une PersonneFlavor.
     * On pourrait aussi accepter des données de mode texte, par exemple.
     */
        @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
            return true;
//        return (c instanceof PartitionPanel);
    }

    /**
     * L'importation de données proprement dite.
     *
     * @param c : la cible du transfert.
     * @param t : données à transférer
     */

        @Override
    public boolean importData(JComponent c, Transferable t) {

        return true;
    }

    }

    
    
    
    
    static private ImageIcon getIconFromAlteration(Alteration alteration)
    {
        switch(alteration)
        {
            case DOUBLEBEMOL: return ImageLoaderSwing.getImageIconPourPetitBouton("double-bemol.png");
            case BEMOL: return ImageLoaderSwing.getImageIconPourPetitBouton("bemol.png");
            case NATUREL: return ImageLoaderSwing.getImageIconPourPetitBouton("becarre.png");
            case DIESE: return ImageLoaderSwing.getImageIconPourPetitBouton("diese.png");
            case DOUBLEDIESE: return ImageLoaderSwing.getImageIconPourPetitBouton("double-diese.png");
            default: return null;
        }
        
    }
    
    
    
    
    /** Creates new form AlterationPanel */
    public AlterationPanel() {
        initComponents();

        cmdDoubleBemol.setIcon(ImageLoaderSwing.getImageIconPourPetitBouton("double-bemol.png"));
        cmdDoubleBemol.setTransferHandler(new AlterationTransferHandler(Alteration.DOUBLEBEMOL));
        // JButtonAlterationDragSource dragSource = new JButtonAlterationDragSource(cmdDoubleBemol, Alteration.DOUBLEBEMOL);
         
        
        cmdBemol.setIcon(ImageLoaderSwing.getImageIconPourPetitBouton("bemol.png"));
        //cmdBemol.setTransferHandler(new TransferHandler("icon"));
        cmdBemol.setTransferHandler(new AlterationTransferHandler(Alteration.BEMOL));
        
        cmdBecarre.setIcon(ImageLoaderSwing.getImageIconPourPetitBouton("becarre.png"));
        cmdBecarre.setTransferHandler(new AlterationTransferHandler(Alteration.NATUREL));
        
        cmdDiese.setIcon(ImageLoaderSwing.getImageIconPourPetitBouton("diese.png"));
        cmdDiese.setTransferHandler(new AlterationTransferHandler(Alteration.DIESE));
        
        cmdDoubleDiese.setIcon(ImageLoaderSwing.getImageIconPourPetitBouton("double-diese.png"));
        cmdDoubleDiese.setTransferHandler(new AlterationTransferHandler(Alteration.DOUBLEDIESE));
    }


    /**
     * set the listener to a click to one button.
     * The listener can access to the alteration by thisAlterationPanel.getAlteration()
     * @param a 
     */
    public void addListener(ActionListener a)
    {
        this.actionListener = a;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        cmdDoubleBemol = new javax.swing.JToggleButton();
        cmdBemol = new javax.swing.JToggleButton();
        jToolBar2 = new javax.swing.JToolBar();
        cmdBecarre = new javax.swing.JToggleButton();
        jToolBar3 = new javax.swing.JToolBar();
        cmdDiese = new javax.swing.JToggleButton();
        cmdDoubleDiese = new javax.swing.JToggleButton();

        setName("Form"); // NOI18N
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jToolBar1.setFloatable(false);
        jToolBar1.setOrientation(1);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(26, 52));
        jToolBar1.setMinimumSize(new java.awt.Dimension(26, 52));
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setPreferredSize(new java.awt.Dimension(26, 52));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(AlterationPanel.class, this);
        cmdDoubleBemol.setAction(actionMap.get("change")); // NOI18N
        buttonGroup.add(cmdDoubleBemol);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(AlterationPanel.class);
        cmdDoubleBemol.setIcon(resourceMap.getIcon("cmdDoubleBemol.icon")); // NOI18N
        cmdDoubleBemol.setText(resourceMap.getString("cmdDoubleBemol.text")); // NOI18N
        cmdDoubleBemol.setFocusable(false);
        cmdDoubleBemol.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdDoubleBemol.setMaximumSize(new java.awt.Dimension(28, 25));
        cmdDoubleBemol.setMinimumSize(new java.awt.Dimension(28, 25));
        cmdDoubleBemol.setName("cmdDoubleBemol"); // NOI18N
        cmdDoubleBemol.setPreferredSize(new java.awt.Dimension(28, 25));
        cmdDoubleBemol.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdDoubleBemol.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cmdDoubleBemolMousePressed(evt);
            }
        });
        cmdDoubleBemol.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cmdDoubleBemolMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cmdDoubleBemolMouseMoved(evt);
            }
        });
        jToolBar1.add(cmdDoubleBemol);

        cmdBemol.setAction(actionMap.get("change")); // NOI18N
        buttonGroup.add(cmdBemol);
        cmdBemol.setIcon(resourceMap.getIcon("cmdBemol.icon")); // NOI18N
        cmdBemol.setText(resourceMap.getString("cmdBemol.text")); // NOI18N
        cmdBemol.setFocusable(false);
        cmdBemol.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdBemol.setMaximumSize(new java.awt.Dimension(20, 25));
        cmdBemol.setMinimumSize(new java.awt.Dimension(32, 25));
        cmdBemol.setName("cmdBemol"); // NOI18N
        cmdBemol.setPreferredSize(new java.awt.Dimension(32, 25));
        cmdBemol.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdBemol.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cmdBemolMouseDragged(evt);
            }
        });
        jToolBar1.add(cmdBemol);

        add(jToolBar1);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setMaximumSize(new java.awt.Dimension(20, 27));
        jToolBar2.setName("jToolBar2"); // NOI18N

        cmdBecarre.setAction(actionMap.get("change")); // NOI18N
        buttonGroup.add(cmdBecarre);
        cmdBecarre.setIcon(resourceMap.getIcon("cmdBecarre.icon")); // NOI18N
        cmdBecarre.setText(resourceMap.getString("cmdBecarre.text")); // NOI18N
        cmdBecarre.setFocusable(false);
        cmdBecarre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdBecarre.setMaximumSize(new java.awt.Dimension(20, 25));
        cmdBecarre.setMinimumSize(new java.awt.Dimension(20, 25));
        cmdBecarre.setName("cmdBecarre"); // NOI18N
        cmdBecarre.setPreferredSize(new java.awt.Dimension(20, 25));
        cmdBecarre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdBecarre.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cmdBecarreMouseDragged(evt);
            }
        });
        jToolBar2.add(cmdBecarre);

        add(jToolBar2);

        jToolBar3.setFloatable(false);
        jToolBar3.setOrientation(1);
        jToolBar3.setRollover(true);
        jToolBar3.setMaximumSize(new java.awt.Dimension(20, 52));
        jToolBar3.setMinimumSize(new java.awt.Dimension(20, 52));
        jToolBar3.setName("jToolBar3"); // NOI18N
        jToolBar3.setPreferredSize(new java.awt.Dimension(20, 52));

        cmdDiese.setAction(actionMap.get("change")); // NOI18N
        buttonGroup.add(cmdDiese);
        cmdDiese.setIcon(resourceMap.getIcon("cmdDiese.icon")); // NOI18N
        cmdDiese.setFocusable(false);
        cmdDiese.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdDiese.setMaximumSize(new java.awt.Dimension(20, 25));
        cmdDiese.setMinimumSize(new java.awt.Dimension(20, 25));
        cmdDiese.setName("cmdDiese"); // NOI18N
        cmdDiese.setPreferredSize(new java.awt.Dimension(32, 25));
        cmdDiese.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdDiese.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cmdDieseMouseDragged(evt);
            }
        });
        jToolBar3.add(cmdDiese);

        cmdDoubleDiese.setAction(actionMap.get("change")); // NOI18N
        buttonGroup.add(cmdDoubleDiese);
        cmdDoubleDiese.setIcon(resourceMap.getIcon("cmdDoubleDiese.icon")); // NOI18N
        cmdDoubleDiese.setFocusable(false);
        cmdDoubleDiese.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdDoubleDiese.setMaximumSize(new java.awt.Dimension(20, 25));
        cmdDoubleDiese.setMinimumSize(new java.awt.Dimension(32, 25));
        cmdDoubleDiese.setName("cmdDoubleDiese"); // NOI18N
        cmdDoubleDiese.setPreferredSize(new java.awt.Dimension(32, 25));
        cmdDoubleDiese.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdDoubleDiese.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cmdDoubleDieseMouseDragged(evt);
            }
        });
        jToolBar3.add(cmdDoubleDiese);

        add(jToolBar3);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdDoubleBemolMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdDoubleBemolMousePressed
        
        
    //    evt.consume(); // empêche que l'événement soit transmis au container du PersonnePanel.
    }//GEN-LAST:event_cmdDoubleBemolMousePressed

    private void cmdDoubleBemolMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdDoubleBemolMouseMoved

    }//GEN-LAST:event_cmdDoubleBemolMouseMoved

    private void cmdDoubleBemolMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdDoubleBemolMouseDragged
        cmdDoubleBemol.getTransferHandler().exportAsDrag(cmdDoubleBemol, evt,
               TransferHandler.COPY);
        evt.consume();
    }//GEN-LAST:event_cmdDoubleBemolMouseDragged

    private void cmdBemolMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdBemolMouseDragged

        cmdBemol.getTransferHandler().exportAsDrag(cmdBemol, evt,
                TransferHandler.COPY);
        evt.consume();
    }//GEN-LAST:event_cmdBemolMouseDragged

    private void cmdBecarreMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdBecarreMouseDragged
        cmdBecarre.getTransferHandler().exportAsDrag(cmdBecarre, evt,
                TransferHandler.COPY);
        evt.consume();
    }//GEN-LAST:event_cmdBecarreMouseDragged

    private void cmdDieseMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdDieseMouseDragged
        cmdDiese.getTransferHandler().exportAsDrag(cmdDiese, evt,
                TransferHandler.COPY);
        evt.consume();
    }//GEN-LAST:event_cmdDieseMouseDragged

    private void cmdDoubleDieseMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdDoubleDieseMouseDragged
        cmdDoubleDiese.getTransferHandler().exportAsDrag(cmdDoubleDiese, evt,
                TransferHandler.COPY);
        evt.consume();
    }//GEN-LAST:event_cmdDoubleDieseMouseDragged

    @Action
    public void change() {
        if(actionListener != null)
        {
            actionListener.actionPerformed(null);
        }
    }


/**
     * 
     * @return the alteration that corresponds to the button that is selected in this panel
     */
    public Hauteur.Alteration getAlteration()
    {
        if(cmdDoubleBemol.isSelected())
            return Hauteur.Alteration.DOUBLEBEMOL;
        else if(cmdBemol.isSelected())
            return Hauteur.Alteration.BEMOL;
        else if(cmdBecarre.isSelected())
            return Hauteur.Alteration.NATUREL;
        else if(cmdDiese.isSelected())
            return Hauteur.Alteration.DIESE;
        else if(cmdDoubleDiese.isSelected())
            return Hauteur.Alteration.DOUBLEDIESE;
        else
        {
            Erreur.message("AlterationPanel.getAlteration()");
            return Hauteur.Alteration.NATUREL;
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JToggleButton cmdBecarre;
    private javax.swing.JToggleButton cmdBemol;
    private javax.swing.JToggleButton cmdDiese;
    private javax.swing.JToggleButton cmdDoubleBemol;
    private javax.swing.JToggleButton cmdDoubleDiese;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    // End of variables declaration//GEN-END:variables

}
