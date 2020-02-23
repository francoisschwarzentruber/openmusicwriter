/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.palette;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.TransferHandler;
import musicwriter.guiswing.PartitionPanel;
import musicwriter.donnees.Selection;
import musicwriter.donnees.Curseur;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Hauteur.Alteration;
import musicwriter.donnees.Note;
import musicwriter.donnees.actions.PartitionActionNoteAlterer;
import musicwriter.donnees.actions.PartitionActionSelectionInserer;
import musicwriter.guiswing.AlterationTransferable;
import musicwriter.controller.PartitionPanelModeSelection;


/**
 *
 * @author Ancmin
 */
public class PartitionPanelDropTargetListener implements DropTargetListener {
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
                dtde.acceptDrag(TransferHandler.COPY);
    }

    @Override
        public void dragOver(DropTargetDragEvent dtde) {
          dtde.acceptDrag(TransferHandler.COPY);
          
          Transferable transferable = extractTransferable(dtde.getTransferable());
          
          if(transferable instanceof SelectionTransferable)
          {
                SelectionTransferable t = (SelectionTransferable) transferable;
                
                PartitionPanel p = (PartitionPanel) dtde.getDropTargetContext().getComponent();
                Selection selection = ((SelectionTransferable) t).getSelection();

                Curseur curseur = p.getController().getCurseur(p.getPointLogical(dtde.getLocation()), selection.getDureeTotale());

                p.setElementsMusicauxDeplacer(getSelectionAInserer(((SelectionTransferable) t), curseur));
                p.repaint();


                dtde.acceptDrag(TransferHandler.COPY);
          }
          else if(transferable instanceof AlterationTransferable)
          {
              final PartitionPanel p = (PartitionPanel) dtde.getDropTargetContext().getComponent();

              Note note = getNote(p, dtde.getLocation());
                    
                if(note != null)
                {
                    dtde.acceptDrag(TransferHandler.COPY);
                }
                else
                {
                    dtde.rejectDrag();
                }
          }
        }




        Selection getSelectionAInserer(SelectionTransferable selectionTransferable, Curseur curseurDepot)
        {
            Selection selection = ((SelectionTransferable) selectionTransferable).getSelection();

            Selection selectionAInserer = new Selection();
            for(ElementMusical elementModele : selection)
            {
                ElementMusical elementAInserer = (ElementMusical) elementModele.clone();
                elementAInserer.deplacerRelatif(curseurDepot.getMoment().getDureeDepuisDebut(),
                                                 curseurDepot.getHauteur().getIntervalleDepuisDo0(),
                                                 curseurDepot.getPortee());
               // elementAInserer.deplacer(elementModele.getDebutMoment().getDureeDepuisDebut().getFinMoment(curseur.getMoment()));
                selectionAInserer.elementMusicalAjouter(elementAInserer);
            }
            return selectionAInserer;
        }

    @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
               dtde.acceptDrag(TransferHandler.COPY);
            }

    @Override
            public void dragExit(DropTargetEvent dte) {
                PartitionPanel p = (PartitionPanel) dte.getDropTargetContext().getComponent();
                
                p.setElementsMusicauxDeplacer(null);
               // throw new UnsupportedOperationException("Not supported yet.");
            }

            
            
            
            
            private Transferable extractTransferable(Transferable t)
            {
                try {
                        try {
                    try {
                        return (Transferable) (t.getTransferData(new DataFlavorElementMusical()));
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PartitionPanelDropTargetListener.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                        } catch (IOException ex) {
                            Logger.getLogger(PartitionPanel.class.getName()).log(Level.SEVERE, null, ex);
                            return null;
                        }
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(PartitionPanel.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
            }
                        
                        
                        
                        
    @Override
            public void drop(DropTargetDropEvent dtde) {
                Transferable transferable = extractTransferable(dtde.getTransferable());
                if(transferable instanceof SelectionTransferable)
                {
                    final SelectionTransferable t = (SelectionTransferable) transferable;
//                    

                    final PartitionPanel p = (PartitionPanel) dtde.getDropTargetContext().getComponent();
                    final Selection selection = ((SelectionTransferable) t).getSelection();
                    final Curseur curseur = p.getController().getCurseur(p.getPointLogical(dtde.getLocation()), selection.getDureeTotale());
                    final Selection selectionAInserer = getSelectionAInserer(((SelectionTransferable) t), curseur);

                    p.getController().executer(new PartitionActionSelectionInserer(selectionAInserer));
                    p.getController().calculer(selectionAInserer.getMomentDebut());
                    p.getController().setMode(new PartitionPanelModeSelection(p.getController(), selectionAInserer));
                    p.setElementsMusicauxDeplacer(null);
                    p.repaint();

                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                }
                else if(transferable instanceof AlterationTransferable)
                {
                    final AlterationTransferable t = (AlterationTransferable) transferable;
                    final PartitionPanel p = (PartitionPanel) dtde.getDropTargetContext().getComponent();

                    Note note = getNote(p, dtde.getLocation());
                    
                    if(note != null)
                    {
                        p.getController().executer(new PartitionActionNoteAlterer(note, t.getAlteration()));
                        p.getController().calculer(note.getDebutMoment());
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    }
                }
                

            }
    
    
    
    
            Note getNote(PartitionPanel p, Point pointLocation)
            {
                musicwriter.gui.Point point = p.getPointLogical(pointLocation);
                return p.getController().getPartitionVue().getNote(point);
            }
}
