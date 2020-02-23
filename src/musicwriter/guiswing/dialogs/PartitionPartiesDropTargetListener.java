/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.TransferHandler;
import musicwriter.donnees.Instrument;
import musicwriter.donnees.Partie;
import musicwriter.donnees.actions.PartitionActionPartieAjouter;
import musicwriter.donnees.actions.PartitionActionPartieDeplacer;

/**
 *
 * @author Ancmin
 */
public class PartitionPartiesDropTargetListener extends DropTarget {

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        dtde.acceptDrag(TransferHandler.MOVE);
    }

    public void dragOver(DropTargetDragEvent dtde) {
        dtde.acceptDrag(TransferHandler.COPY);
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        dtde.acceptDrag(TransferHandler.MOVE);
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {
        dtde.getDropTargetContext();
        final PartitionPartiesEditorList L = (PartitionPartiesEditorList) dtde.getDropTargetContext().getComponent();
        final int index = L.getIndex(dtde.getLocation());

        Object t;
        try {
            try {
                t = dtde.getTransferable().getTransferData(new DataFlavorInstrument());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PartitionPartiesDropTargetListener.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        } catch (UnsupportedFlavorException ex) {
            Logger.getLogger(PartitionPartiesDropTargetListener.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(PartitionPartiesDropTargetListener.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }


        if(t instanceof InstrumentTransferable)
        {
            final InstrumentTransferable i = (InstrumentTransferable) t;

            final Partie partie = new Partie(i.getInstrument());
            L.getHistoire().executer(new PartitionActionPartieAjouter(index, partie));

            L.mettreAJour();
        }



        if(t  instanceof PartieTransferable)
        {
            final PartieTransferable p = (PartieTransferable) t;

            Partie partieADeplacer = p.getPartie();

            if(index > L.getPartitionDonnees().getPartieNombre() - 1)
                L.getHistoire().executer(
                    new PartitionActionPartieDeplacer(partieADeplacer, index-1));
            else
                L.getHistoire().executer(
                    new PartitionActionPartieDeplacer(partieADeplacer, index));

            L.mettreAJour();
        }
        
    }

}
