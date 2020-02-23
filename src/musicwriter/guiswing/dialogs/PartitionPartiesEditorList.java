/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import musicwriter.donnees.Histoire;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionPartiesEditorList extends PartitionPartiesList {


    final Histoire histoire;

    public Histoire getHistoire() {
        return histoire;
    }


    


    public PartitionPartiesEditorList(PartitionDonnees partitionDonnees, Histoire histoire) {
        super(partitionDonnees);
        this.histoire = histoire;
        setCellRenderer(new PartitionPartiesCellRenderer());
        mettreAJour();
        final DragSource ds = new DragSource();
        DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(this,
        DnDConstants.ACTION_COPY, new DragGestureListener() {

            public void dragGestureRecognized(DragGestureEvent dge) {
                PartieTransferable transferable = new PartieTransferable(getSelectedPartie());
                ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable,

                        new DragSourceListener() {

                    public void dragEnter(DragSourceDragEvent dsde) {
                    }

                    public void dragOver(DragSourceDragEvent dsde) {
                    }

                    public void dropActionChanged(DragSourceDragEvent dsde) {
                    }

                    public void dragExit(DragSourceEvent dse) {

                    }

                    public void dragDropEnd(DragSourceDropEvent dsde) {
                    }
                });
            }
        });

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setDropMode(DropMode.USE_SELECTION);
        setTransferHandler(new PartitionPartiesTransferHandler());
    }

    public int getIndex(Point location) {
        int index = locationToIndex(location);


        if(index < 0)
            return 0;
        else
        if(index >= getPartitionDonnees().getPartieNombre() - 1)
        {
            if(getCellBounds(index, index).contains(location))
                return index;
            else
                return index + 1;
        }
        else
            return index;


    }





}
