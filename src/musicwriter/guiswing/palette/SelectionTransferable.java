/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.palette;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class SelectionTransferable implements Transferable {

    private final Selection selection;

    public Selection getSelection() {
        return selection;
    }




    public SelectionTransferable(Selection selection) {
        this.selection = selection;
    }



    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] result = new DataFlavor[1];
     //   result[0] = new DataFlavorElementMusical();
        result[0] = DataFlavor.stringFlavor;
        return result;
    }



    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;//(flavor instanceof DataFlavorElementMusical);
    }


    
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

}
