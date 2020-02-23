/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import musicwriter.donnees.Hauteur.Alteration;

/**
 *
 * @author Ancmin
 */
public class AlterationTransferable implements Transferable {

    
    final Alteration alteration;
    
    AlterationTransferable(Alteration alteration)
    {
        this.alteration = alteration;
    }

    public Alteration getAlteration() {
        return alteration;
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
        return true;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }
    
}
