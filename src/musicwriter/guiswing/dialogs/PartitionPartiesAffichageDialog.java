/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PartitionPartiesAffichageDialog.java
 *
 * Created on 30 mai 2010, 20:45:59
 */

package musicwriter.guiswing.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
public class PartitionPartiesAffichageDialog extends javax.swing.JDialog {

    private boolean ok = false;
    private final PartitionDonnees partitionDonnees;
    private HashSet<Partie> partiesAffichees = null;


    public boolean isOk() {
        return ok;
    }

    public HashSet<Partie> getPartiesAffichees() {
        return partiesAffichees;
    }


    
    

    /** Creates new form PartitionPartiesAffichageDialog */
    public PartitionPartiesAffichageDialog(java.awt.Frame parent, boolean modal, PartitionDonnees partitionDonnees) {
        super(parent, modal);
        this.partitionDonnees = partitionDonnees;
        initComponents();
        ((PartitionPartiesList) jList).mettreAJour();
        ((PartitionPartiesList) jList).setActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                ok = true;
                setVisible(false);
                partiesAffichees = new HashSet<Partie>();
                partiesAffichees.add(((PartitionPartiesList) jList).getSelectedPartie());
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new PartitionPartiesList(partitionDonnees);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(musicwriter.MusicwriterApp.class).getContext().getResourceMap(PartitionPartiesAffichageDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setLocationByPlatform(true);
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 400));

        jList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList.setMaximumSize(null);
        jList.setMinimumSize(null);
        jList.setName("jList"); // NOI18N
        jList.setPreferredSize(null);
        jScrollPane1.setViewportView(jList);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PartitionPartiesAffichageDialog dialog = new PartitionPartiesAffichageDialog(new javax.swing.JFrame(), true, new PartitionDonnees());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
