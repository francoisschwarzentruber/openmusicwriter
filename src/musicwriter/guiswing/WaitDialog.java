/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WaitDialog.java
 *
 * Created on 21 févr. 2012, 17:45:15
 */
package musicwriter.guiswing;

import java.awt.Cursor;
import javax.swing.SwingUtilities;
import musicwriter.guiswing.dialogs.RoundedBorder;


/**
 * This dialog box is shown when the software is processing a long task.
 * Design pattern: singleton
 * 
 * @author Ancmin
 */
public class WaitDialog extends javax.swing.JDialog {

    static final private WaitDialog d = new WaitDialog(null, true);
    
    /** Creates new form WaitDialog */
    private WaitDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        lblMessage.setBorder(new RoundedBorder());
    }
    
    
    static public void start(final String message)
    {
        d.setMessage(message);
        d.repaint();
        d.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        d.setVisible(true);
        

    }
    
    
    static public void addMessage(final String message)
    {
        d.setMessage(message);
       

    }
    
    
    static public void invokeStart(final String message)
    {
        SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            WaitDialog.start(message);
                        }
                    });

    }
    
    
    static public void invokeAddMessage(final String message)
    {
        SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            WaitDialog.addMessage(message);
                        }
                    });

    }
    
    static public void finish()
    {
        d.setVisible(false);
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblMessage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Attente - Traitement en cours");
        setMinimumSize(new java.awt.Dimension(300, 100));
        setResizable(false);
        setUndecorated(true);

        lblMessage.setText("<<message>>");
        lblMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMessage.setName("lblMessage"); // NOI18N
        getContentPane().add(lblMessage, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                WaitDialog dialog = new WaitDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblMessage;
    // End of variables declaration//GEN-END:variables

    private void setMessage(String message) {
        lblMessage.setText(message);
    }
}
