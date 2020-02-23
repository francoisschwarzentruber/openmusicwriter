/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import musicwriter.guiswing.palette.*;
import java.awt.datatransfer.DataFlavor;

/**
 *
 * @author Ancmin
 */
public class DataFlavorInstrument extends DataFlavor {
    public DataFlavorInstrument() throws ClassNotFoundException
    {
        super("musiwriter/Instrument");
    }
}
