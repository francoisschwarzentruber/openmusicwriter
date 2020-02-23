/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TexteDialog.java
 *
 * Created on 8 mars 2010, 21:46:37
 */

package musicwriter.guiswing.dialogs;

/**
 *
 * @author Ancmin
 */
public class TexteDialog extends javax.swing.JDialog {

    private boolean annule = false;
    /** Creates new form TexteDialog */
    public TexteDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }


    public boolean isAnnule()
    {
        return annule;
    }



    public String getTexte()
    {
        return txtTexte.getText();
    }


    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTexte = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdOK = new javax.swing.JButton();
        cmdAnnuler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(musicwriter.MusicwriterApp.class).getContext().getResourceMap(TexteDialog.class);
        txtTexte.setText(resourceMap.getString("txtTexte.text")); // NOI18N
        txtTexte.setName("txtTexte"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        cmdOK.setIcon(resourceMap.getIcon("cmdOK.icon")); // NOI18N
        cmdOK.setText(resourceMap.getString("cmdOK.text")); // NOI18N
        cmdOK.setName("cmdOK"); // NOI18N
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        cmdAnnuler.setIcon(resourceMap.getIcon("cmdAnnuler.icon")); // NOI18N
        cmdAnnuler.setText(resourceMap.getString("cmdAnnuler.text")); // NOI18N
        cmdAnnuler.setName("cmdAnnuler"); // NOI18N
        cmdAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtTexte, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(cmdOK)
                        .addComponent(cmdAnnuler)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTexte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdOK)
                    .addComponent(cmdAnnuler))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        setVisible(false);
        annule = false;
}//GEN-LAST:event_cmdOKActionPerformed

    private void cmdAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAnnulerActionPerformed
        setVisible(false);
        annule = true;
}//GEN-LAST:event_cmdAnnulerActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TexteDialog dialog = new TexteDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cmdAnnuler;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtTexte;
    // End of variables declaration//GEN-END:variables

}
