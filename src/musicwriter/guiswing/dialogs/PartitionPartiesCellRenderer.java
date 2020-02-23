/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import musicwriter.Utils;
import musicwriter.donnees.Partie;

/**
 *
 * @author Ancmin
 */
class PartitionPartiesCellRenderer implements ListCellRenderer, DropTargetListener {
    private static final Color HIGHLIGHT_COLOR = new Color(128, 128, 255);
    public PartitionPartiesCellRenderer() {

    }
    
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = new JLabel();

        if(!(value instanceof Partie))
            return label;
        
        Partie partie = (Partie) value;
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setTransferHandler(new PartitionPartiesTransferHandler());
        label.setIcon(partie.getInstrument().getImageIcon());
        label.setText(partie.getInstrument().getNom());
       // setText(partie.getInstrument().getNom());
        if (isSelected) {
              label.setBackground(HIGHLIGHT_COLOR);
              label.setForeground(Color.white);
            } else {
              label.setBackground(Color.white);
              label.setForeground(Color.black);
            }
        
        
        if(((JDialog) Utils.findWindow(list)).getGlassPane() instanceof GhostGlassPane)
        {
            GhostGlassPane glassPane = (GhostGlassPane) ((JDialog) Utils.findWindow(list)).getGlassPane();
        
        
            label.addMouseListener(new GhostComponentAdapter(glassPane, new PartieTransferable(partie)));
             label.addMouseMotionListener(new GhostMotionAdapter(glassPane));
             label.addMouseListener(new GhostPictureAdapter( glassPane,
                                                        partie.getInstrument().getImageIcon(),
                new PartieTransferable(partie)));
             label.addMouseMotionListener(new GhostMotionAdapter(glassPane));
        }
        
        
        return label;
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dragOver(DropTargetDragEvent dtde) {
        
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dragExit(DropTargetEvent dte) {
       //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drop(DropTargetDropEvent dtde) {
       System.out.println(dtde.getSource());
    }

}
