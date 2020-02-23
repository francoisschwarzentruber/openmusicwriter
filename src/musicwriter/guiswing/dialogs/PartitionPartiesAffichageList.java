/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.Collection;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionPartiesAffichageList extends JList {

    private final PartitionDonnees partitionDonnees;
    private final Collection<Partie> partiesAffichees;

    public void mettreAJour()
    {
        setModel(new PartitionPartiesListModel(partitionDonnees));
    }

    public Collection<Partie> getPartiesAffichees() {
        return partiesAffichees;
    }



    
   
    

    public PartitionPartiesAffichageList(PartitionDonnees partitionDonnees, Collection<Partie> partiesAffichees) {
        this.partitionDonnees = partitionDonnees;
        this.partiesAffichees = partiesAffichees;
        setCellRenderer(new PartitionPartiesAffichageCellRenderer(partiesAffichees));
        mettreAJour();
//        final DragSource ds = new DragSource();
//        DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(this,
//        DnDConstants.ACTION_COPY, new DragGestureListener() {
//
//            public void dragGestureRecognized(DragGestureEvent dge) {
//                PartieTransferable transferable = new PartieTransferable(getSelectedPartie());
//                ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable,
//
//                        new DragSourceListener() {
//
//                    public void dragEnter(DragSourceDragEvent dsde) {
//                    }
//
//                    public void dragOver(DragSourceDragEvent dsde) {
//                    }
//
//                    public void dropActionChanged(DragSourceDragEvent dsde) {
//                    }
//
//                    public void dragExit(DragSourceEvent dse) {
//
//                    }
//
//                    public void dragDropEnd(DragSourceDropEvent dsde) {
//                    }
//                });
//            }
//        });
//
//        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        setDropMode(DropMode.USE_SELECTION);
//        setTransferHandler(new PartitionPartiesTransferHandler());
    }

    public Partie getSelectedPartie() {
         return partitionDonnees.getPartie(getSelectedIndex());
    }



}
