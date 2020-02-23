/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;
import musicwriter.donnees.Histoire;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionPartiesList extends JList {

    private final PartitionDonnees partitionDonnees;
    private ActionListener actionListener = null;


    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public PartitionDonnees getPartitionDonnees() {
        return partitionDonnees;
    }


    

    public void mettreAJour()
    {
        setModel(new PartitionPartiesListModel(partitionDonnees));
    }



    public PartitionPartiesList(PartitionDonnees partitionDonnees) {
        this.partitionDonnees = partitionDonnees;
        setCellRenderer(new PartitionPartiesCellRenderer());
        addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                click();
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }


        });
        mettreAJour();
    }


    public Partie getSelectedPartie() {
         return partitionDonnees.getPartie(getSelectedIndex());
    }





    private void click() {
        if(actionListener != null)
        {
            actionListener.actionPerformed(null);
        }

    }

}
