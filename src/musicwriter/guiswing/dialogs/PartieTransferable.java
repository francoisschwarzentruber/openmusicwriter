/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import musicwriter.donnees.Partie;

/**
 *
 * @author Ancmin
 */
public class PartieTransferable implements Transferable {
    private final Partie partie;

    public Partie getPartie() {
        return partie;
    }

    public PartieTransferable(Partie partie)
    {
        this.partie = partie;
    }


    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] result = new DataFlavor[1];
     //   result[0] = new DataFlavorElementMusical();
        result[0] = DataFlavor.stringFlavor;
        return result;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

}
