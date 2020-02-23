/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class InstrumentTransferable implements Transferable {

    private final Selection selection;

    public Selection getSelection() {
        return selection;
    }




    public InstrumentTransferable(Selection selection) {
        this.selection = selection;
    }



    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] result = new DataFlavor[1];
     //   result[0] = new DataFlavorElementMusical();
        result[0] = DataFlavor.stringFlavor;
        return result;
    }



    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;//(flavor instanceof DataFlavorElementMusical);
    }


    
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

}
