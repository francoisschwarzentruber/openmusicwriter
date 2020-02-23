/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import musicwriter.guiswing.palette.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import musicwriter.donnees.Instrument;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class InstrumentTransferable implements Transferable {

    private final Instrument instrument;

    public Instrument getInstrument() {
        return instrument;
    }

    public InstrumentTransferable(Instrument instrument) {
        this.instrument = instrument;
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
