/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TempoDialog.java
 *
 * Created on 12 févr. 2010, 10:50:20
 */

package musicwriter.guiswing.dialogs;

import musicwriter.donnees.ElementMusicalTempo;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Rational;
import org.jdesktop.application.Action;

/**
 *
 * @author Ancmin
 */
public class TempoDialog extends javax.swing.JDialog {

    static private  Moment moment = new Moment(new Rational(0, 1));
    private boolean annule = true;

    int pulsationClefs[] = {30, 40, 60, 80, 100, 150, 200};


    public ElementMusicalTempo getTempo() {
        if(annule)
            return null;
        else
            return new ElementMusicalTempo(moment, getPulsation(), getTempoNom());
    }

    /** Creates new form TempoDialog */
    public TempoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         panelTempoMorphing.setTempoPulsation(getPulsation());
    }


    /** Creates new form TempoDialog */
    public TempoDialog(java.awt.Frame parent, boolean modal, ElementMusicalTempo tempo) {
        super(parent, modal);
        initComponents();
        scrollBarTempo.setValue(tempo.getNbNoiresEnUneMinute());
        txtTempoNom.setText(tempo.getNom());
         panelTempoMorphing.setTempoPulsation(getPulsation());
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTempoNom = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelTempoMorphing = new musicwriter.guiswing.dialogs.PanelTempoMorphing();
        txtPulsation = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        scrollBarTempo = new javax.swing.JScrollBar();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cmdOK = new javax.swing.JButton();
        cmdAnnuler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(musicwriter.MusicwriterApp.class).getContext().getResourceMap(TempoDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel4.add(jLabel2);

        txtTempoNom.setFont(resourceMap.getFont("txtTempoNom.font")); // NOI18N
        txtTempoNom.setText(resourceMap.getString("txtTempoNom.text")); // NOI18N
        txtTempoNom.setName("txtTempoNom"); // NOI18N
        jPanel4.add(txtTempoNom);

        getContentPane().add(jPanel4);

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 16));
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(312, 16));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel5);

        jPanel3.setMaximumSize(new java.awt.Dimension(32799, 64));
        jPanel3.setMinimumSize(new java.awt.Dimension(32, 64));
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(312, 64));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        panelTempoMorphing.setMaximumSize(new java.awt.Dimension(32767, 64));
        panelTempoMorphing.setMinimumSize(new java.awt.Dimension(0, 64));
        panelTempoMorphing.setName("panelTempoMorphing"); // NOI18N
        panelTempoMorphing.setPreferredSize(new java.awt.Dimension(280, 64));

        javax.swing.GroupLayout panelTempoMorphingLayout = new javax.swing.GroupLayout(panelTempoMorphing);
        panelTempoMorphing.setLayout(panelTempoMorphingLayout);
        panelTempoMorphingLayout.setHorizontalGroup(
            panelTempoMorphingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 282, Short.MAX_VALUE)
        );
        panelTempoMorphingLayout.setVerticalGroup(
            panelTempoMorphingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        jPanel3.add(panelTempoMorphing);

        txtPulsation.setIcon(resourceMap.getIcon("txtPulsation.icon")); // NOI18N
        txtPulsation.setText(resourceMap.getString("txtPulsation.text")); // NOI18N
        txtPulsation.setName("txtPulsation"); // NOI18N
        jPanel3.add(txtPulsation);

        getContentPane().add(jPanel3);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel1);

        scrollBarTempo.setMaximum(220);
        scrollBarTempo.setMinimum(10);
        scrollBarTempo.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        scrollBarTempo.setName("scrollBarTempo"); // NOI18N
        scrollBarTempo.setPreferredSize(new java.awt.Dimension(48, 24));
        scrollBarTempo.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                scrollBarTempoAdjustmentValueChanged(evt);
            }
        });
        jPanel1.add(scrollBarTempo);

        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel3);

        getContentPane().add(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(musicwriter.MusicwriterApp.class).getContext().getActionMap(TempoDialog.class, this);
        cmdOK.setAction(actionMap.get("ok")); // NOI18N
        cmdOK.setIcon(resourceMap.getIcon("cmdOK.icon")); // NOI18N
        cmdOK.setText(resourceMap.getString("cmdOK.text")); // NOI18N
        cmdOK.setName("cmdOK"); // NOI18N
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });
        jPanel2.add(cmdOK);

        cmdAnnuler.setAction(actionMap.get("cancel")); // NOI18N
        cmdAnnuler.setIcon(resourceMap.getIcon("cmdAnnuler.icon")); // NOI18N
        cmdAnnuler.setText(resourceMap.getString("cmdAnnuler.text")); // NOI18N
        cmdAnnuler.setName("cmdAnnuler"); // NOI18N
        cmdAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAnnulerActionPerformed(evt);
            }
        });
        jPanel2.add(cmdAnnuler);

        getContentPane().add(jPanel2);

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

    private void scrollBarTempoAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_scrollBarTempoAdjustmentValueChanged
        mettreAJour();
    }//GEN-LAST:event_scrollBarTempoAdjustmentValueChanged


    private void mettreAJour()
    {
        if(isStandardName(getTempoNom()))
             txtTempoNom.setText(getStandardName(scrollBarTempo.getValue()));

        txtPulsation.setText(String.valueOf(getPulsation()));
        panelTempoMorphing.setTempoPulsation(getPulsation());
    }

    private int getPulsation()
    {
        return scrollBarTempo.getValue();
    }



    

    private String getTempoNom()
    {
        return txtTempoNom.getText();
    }





    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TempoDialog dialog = new TempoDialog(new javax.swing.JFrame(), true);
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



    String TempoStandardNoms[] = {"Adagio", "Andante", "Andantino", "Allegretto", "Allegro", "Presto"};

    
    static private String getStandardName(int pulsationalanoire)
    {
        int T[] = {40, 60, 80, 100, 150, 200};

        int diffmax = 1000;
        int pulsationstandardlaplusproche = 80;
        for(int v : T)
        {
            if(Math.abs(pulsationalanoire - v) < diffmax)
            {
                pulsationstandardlaplusproche = v;
                diffmax = Math.abs(pulsationalanoire - v);
            }
        }


        switch(pulsationstandardlaplusproche)
        {
            case 40: return "Adagio";
            case 60: return "Andante";
            case 80: return "Andantino";
            case 100: return "Allegretto";
            case 150: return "Allegro";
            case 200: return "Presto";
            default: return "";
        }

    }


    static private int getStandardNumeroPlusProche(int pulsationalanoire)
    {
        int T[] = {30, 40, 60, 80, 100, 150, 200};

        int diffmax = 1000;
        int pulsationstandardlaplusproche = 80;
        for(int v : T)
        {
            if(Math.abs(pulsationalanoire - v) < diffmax)
            {
                pulsationstandardlaplusproche = v;
                diffmax = Math.abs(pulsationalanoire - v);
            }
        }


        switch(pulsationstandardlaplusproche)
        {
            case 30: return 0;
            case 40: return 1;
            case 60: return 2;
            case 80: return 3;
            case 100: return 4;
            case 150: return 5;
            case 200: return 6;
            default: return 7;
        }

    }

    @Action
    public void ok() {
        annule = false;
        setVisible(false);
    }

    @Action
    public void cancel() {
        annule = true;
        setVisible(false);
    }

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAnnuler;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private musicwriter.guiswing.dialogs.PanelTempoMorphing panelTempoMorphing;
    private javax.swing.JScrollBar scrollBarTempo;
    private javax.swing.JLabel txtPulsation;
    private javax.swing.JTextField txtTempoNom;
    // End of variables declaration//GEN-END:variables

    private boolean isStandardName(String tempoNom) {
        for(String s : TempoStandardNoms)
        {
            if(s.equals(tempoNom))
                return true;
        }
        return false;
    }

    

}
