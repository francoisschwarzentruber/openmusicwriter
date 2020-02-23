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
import java.util.Collection;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import musicwriter.donnees.Partie;

/**
 *
 * @author Ancmin
 */
class PartitionPartiesAffichageCellRenderer implements ListCellRenderer, DropTargetListener {
    private static final Color HIGHLIGHT_COLOR = new Color(128, 128, 255);
    private final Collection<Partie> partiesAffichees;



    public PartitionPartiesAffichageCellRenderer(Collection<Partie> partiesAffichees) {
        this.partiesAffichees = partiesAffichees;
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


        if(partiesAffichees.contains(partie))
        {
            label.setText(label.getText() + " (affiché)");
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
