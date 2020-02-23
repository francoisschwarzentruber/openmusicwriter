/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing.dialogs;

import musicwriter.donnees.Instrument;
import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.TransferHandler;
import musicwriter.Utils;

/**
 * Cette classe affiche un instrument donnée (en paramètre à
 * getListCellRendererComponent... désolé :p c'est demandé par ListCellRenderer
 * qui dans l'interface JAVA) dans une liste.
 * @author Ancmin
 */
class InstrumentCellRenderer implements ListCellRenderer {

    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

    

    public class InstrumentTransferHandler extends TransferHandler {

        /**
         * Crée un objet transférable adapté, donc un PersonneTransferable.
         * Reçoit comme paramètre la source du drag and drop
         * (ce qui permet de partager des PersonneTransferHandler)
         */
        @Override
        protected Transferable createTransferable(JComponent c) {
            InstrumentLabel panel = (InstrumentLabel) c;

            return new InstrumentTransferable(panel.getInstrument());
        }

        /**
         * Permet de dire si le drag est possible. Dans notre cas, il l'est toujours.
         * cette méthode peut renvoyer COPY, NONE, MOVE, COPY_OR_MOVE.
         * Si NONE est renvoyé, le drag and drop sera impossible.
         */
        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }

        /**
         * Teste si les données proposée comportent une PersonneFlavor.
         * On pourrait aussi accepter des données de mode texte, par exemple.
         */
        @Override
        public boolean canImport(JComponent c, DataFlavor[] flavors) {
            return true;
//        return (c instanceof PartitionPanel);
        }

        /**
         * L'importation de données proprement dite.
         *
         * @param c : la cible du transfert.
         * @param t : données à transférer
         */
        @Override
        public boolean importData(JComponent c, Transferable t) {

            return true;
        }
    }

    public InstrumentCellRenderer() {
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Instrument instrument = (Instrument) value;

        InstrumentLabel label = new InstrumentLabel(instrument);
        label.setTransferHandler(new InstrumentTransferHandler());
        //  setText(instrument.getNom());
        if (isSelected) {
            label.setBackground(HIGHLIGHT_COLOR);
            label.setForeground(Color.white);
        } else {
            label.setBackground(Color.white);
            label.setForeground(Color.black);
        }
        
        
        
        
        GhostGlassPane glassPane = (GhostGlassPane) ((JDialog) Utils.findWindow(list)).getGlassPane();
        
        
       
        label.addMouseListener(new GhostPictureAdapter( glassPane,
                                                        label.getImageIcon(),
                new InstrumentTransferable(instrument)));
        label.addMouseMotionListener(new GhostMotionAdapter(glassPane));

        
        
        
        
        
        
        
        
        
        
        return label;
    }
}
