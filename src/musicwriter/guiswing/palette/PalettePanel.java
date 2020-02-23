/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PalettePanel.java
 *
 * Created on 28 mars 2010, 17:48:24
 */

package musicwriter.guiswing.palette;

import javax.swing.ImageIcon;
import musicwriter.donnees.*;
import musicwriter.donnees.Portee.Clef;
import musicwriter.guiswing.ImageLoaderSwing;

/**
 *
 * @author Ancmin
 */
public class PalettePanel extends javax.swing.JPanel {


    static Moment getDebutMoment()
    {

        return new Moment(new Rational(0, 1));

    }

    final static private Partie partie = new Partie(new Instrument(45));
    final static private Portee portee = partie.getPorteePremiere();
    
    static private Portee getPortee()
    {
        return portee;
    }


    static private Partie getPartie()
    {
        return partie;
    }

    static private Curseur getCurseur()
    {
        return new Curseur(getDebutMoment(), new Hauteur(0, Hauteur.Alteration.NATUREL), getPortee());
    }




    private static Selection getSelectionBasseAlberti()
    {
         Voix voix = new Voix();
         Selection selection = new Selection(new Note(new Moment(new Rational(0, 4)),
                                           new Duree(new Rational(1, 4)),
                                           Hauteur.getDo0(),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(new Moment(new Rational(1, 4)),
                                           new Duree(new Rational(1, 4)),
                                           new Hauteur(4, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(new Moment(new Rational(2, 4)),
                                           new Duree(new Rational(1, 4)),
                                          new Hauteur(2, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(new Moment(new Rational(3, 4)),
                                           new Duree(new Rational(1, 4)),
                                           new Hauteur(4, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         return selection;
    }


    private static Selection getSelectionNUplets(int n, Duree dureeNote)
    {
        Voix voixTriolet = new Voix();

        Selection selectionTriolet = new Selection();
        Moment moment = getDebutMoment();
        for(int i = 0; i < n; i++)
        {
            selectionTriolet.elementMusicalAjouter(new Note(moment,
                                           dureeNote,
                                           Hauteur.getSol0(),
                                           getPortee(), voixTriolet));
            moment = dureeNote.getFinMoment(moment);
        }

        return selectionTriolet;
    }


    private static Selection getSelectionAccompagnementAccord()
    {
         Voix voix = new Voix();
         Selection selection = new Selection(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           Hauteur.getDo0(),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           new Hauteur(2, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           new Hauteur(4, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         return selection;
    }

    private static Selection getSelectionAccordMajeur()
    {
         Voix voix = new Voix();
         Selection selection = new Selection(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           Hauteur.getDo0(),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           new Hauteur(2, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           new Hauteur(4, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         return selection;
    }


    private static Selection getSelectionAccordMineur()
    {
         Voix voix = new Voix();
         Selection selection = new Selection(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           Hauteur.getDo0(),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           new Hauteur(2, Hauteur.Alteration.BEMOL),
                                           getPortee(), voix));

         selection.elementMusicalAjouter(new Note(getDebutMoment(),
                                           new Duree(new Rational(1, 1)),
                                           new Hauteur(4, Hauteur.Alteration.NATUREL),
                                           getPortee(), voix));

         return selection;
    }



    private ImageIcon getImageIcon(String filename)
    {
        return ImageLoaderSwing.getImageIconPourBouton(filename);
        
    }


    private ImageIcon getImageIcon(String filename, double scale)
    {
        return ImageLoaderSwing.getImageIcon(filename, scale);

    }


    /** Creates new form PalettePanel */
    public PalettePanel() {
        initComponents();

        partie.setNom("");

        PaletteContainerPanel panClef = new PaletteContainerPanel();
        panClef.add(new ElementsMusicauxPanel(
                new ElementMusicalClef(getDebutMoment(),
                                       getPortee(),
                                       Clef.ClefDeSol),
                                        ImageLoaderSwing.getSVGImageIconPourBouton("GClef.svg")));
        panClef.add(new ElementsMusicauxPanel(
                new ElementMusicalClef(getDebutMoment(),
                                       getPortee(),
                                       Clef.ClefDeFa),
                                        ImageLoaderSwing.getSVGImageIconPourBouton("FClef.svg")));

        panClef.add(new ElementsMusicauxPanel(
                new ElementMusicalClef(getDebutMoment(),
                                       getPortee(),
                                       Clef.ClefDUt),
                                        ImageLoaderSwing.getSVGImageIconPourBouton("CClef.svg")));

        panClef.add(new ElementsMusicauxPanel(
                new Octaviation(getDebutMoment(),
                                       getPortee(),
                                       -1),
                                getImageIcon("8vaalta.png")));
        panClef.add(new ElementsMusicauxPanel(
                new Octaviation(getDebutMoment(),
                                getPortee(),
                                1),
                             getImageIcon("8vabassa.png")));
        panClef.add(new ElementsMusicauxPanel(
                new Octaviation(getDebutMoment(),
                                getPortee(),
                                0),
                             getImageIcon("8vafin.png")));

        add( new PaletteOngletPanel("Clef", panClef));
     //   toolBarClefOnglet.add(toolBarClef);

        //int tonalitewidth = 42;

        PaletteContainerPanel panArmatures = new PaletteContainerPanel();

        for(int i = 7; i >= 1; i--)
        panArmatures.add(new ElementsMusicauxPanel(
                new ElementMusicalChangementTonalite(getDebutMoment(),
                                       new Tonalite(-i))));
                                       // ImageLoader.getSVGImageIconAvecWidth("tonaliteb" + i + ".svg", tonalitewidth)));
        
        panArmatures.add(new ElementsMusicauxPanel(
                new ElementMusicalChangementTonalite(getDebutMoment(),
                                       new Tonalite(0))), true);
                                      //  ImageLoader.getSVGImageIconAvecWidth("tonalite0.svg", tonalitewidth)));

        for(int i = 1; i <= 7; i++)
        panArmatures.add(new ElementsMusicauxPanel(
                new ElementMusicalChangementTonalite(getDebutMoment(),
                                       new Tonalite(i))));
                                        //ImageLoader.getSVGImageIconAvecWidth("tonalited" + i + ".svg", tonalitewidth)));

        
        
        add( new PaletteOngletPanel("Armatures", panArmatures));


        PaletteContainerPanel panSignature = new PaletteContainerPanel();
        for(int i = 2; i<=8; i++)
        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(i, 4 ))));

        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(12, 4 ))));
        

        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(3, 8 ))));

        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(5, 8 ))));

        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(6, 8 ))));

        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(7, 8 ))));
        
        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(9, 8 ))));
        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(12, 8 ))));

        panSignature.add(
               new ElementsMusicauxPanel(
                new ElementMusicalChangementMesureSignature( getDebutMoment(), new MesureSignature(3, 3, 2, 0, 8 ))));
        add( new PaletteOngletPanel("Signatures", panSignature));





        PaletteContainerPanel panBarreMesure = new PaletteContainerPanel();
        panBarreMesure.add(
               new ElementsMusicauxPanel(
                new BarreDeMesure(getDebutMoment()),
                getImageIcon("barredemesure_normale.png")));

        panBarreMesure.add(
               new ElementsMusicauxPanel(
                new BarreDeMesure(getDebutMoment(), BarreDeMesure.BarreDeMesureType.DOUBLE),
                getImageIcon("barredemesure_double.png")));

        panBarreMesure.add(
               new ElementsMusicauxPanel(
                new BarreDeMesure(getDebutMoment(), BarreDeMesure.BarreDeMesureType.REPRISEGAUCHE),
                getImageIcon("barredemesure_reprise2.png")));

        panBarreMesure.add(
               new ElementsMusicauxPanel(
                new BarreDeMesure(getMomentUn(), BarreDeMesure.BarreDeMesureType.REPRISEDROITE),
                getImageIcon("barredemesure_reprise1.png")));

        panBarreMesure.add(
               new ElementsMusicauxPanel(
                new BarreDeMesure(getDebutMoment(), BarreDeMesure.BarreDeMesureType.REPRISEDOUBLE),
                getImageIcon("barredemesure_reprise12.png")));

        panBarreMesure.add(
               new ElementsMusicauxPanel(
                new BarreDeMesure(getDebutMoment(), BarreDeMesure.BarreDeMesureType.FIN),
                getImageIcon("barredemesure_fin.png")));

//        panBarreMesure.add(
//               new ElementsMusicauxPanel(
//                new ElementMusicalLigne(getCurseur(),
//                                        new Curseur(new Moment(new Rational(3, 1)),
//                                                    Hauteur.getSol0(), getPortee())),
//                getImageIcon("barredemesure_fin.png")));


        add( new PaletteOngletPanel("Barres", panBarreMesure));




        

        separation();
        

        

        PaletteContainerPanel panNotes = new PaletteContainerPanel();
        for(int i = 3; i < 9; i++)
        panNotes.add(new ElementsMusicauxPanel(
                  getSelectionNUplets(i , new Duree(new Rational(1, i)))));


        for(int i = 3; i < 9; i++)
        panNotes.add(new ElementsMusicauxPanel(
                  getSelectionNUplets(i , new Duree(new Rational(1, i*2)))));
        



        panNotes.add(new ElementsMusicauxPanel(getSelectionBasseAlberti()));

        Note noteAppogiature = new Note(getDebutMoment(), new Duree(new Rational(1, 16)), Hauteur.getDo0(), getPortee() );
        noteAppogiature.setAppogiature(true);
        
        panNotes.add(
                new ElementsMusicauxPanel(
                  noteAppogiature,
                //  ImageLoader.getSVGImageIconPourBouton("acciaccatura.svg")));
                  getImageIcon("appogiaturebreve.png")));


        Note noteLosange = new Note(getDebutMoment(), new Duree(new Rational(2, 1)), Hauteur.getDo0(), getPortee() );
        noteLosange.setNoteFigure(Note.NoteFigure.LOSANGE);

        panNotes.add(
                new ElementsMusicauxPanel(
                  noteLosange,
                  getImageIcon("notelosange.png")));

        Note noteCroix = new Note(getDebutMoment(), new Duree(new Rational(2, 1)), Hauteur.getDo0(), getPortee() );
        noteCroix.setNoteFigure(Note.NoteFigure.CROIX);

        panNotes.add(
                new ElementsMusicauxPanel(
                  noteCroix,
                  getImageIcon("notecroix.png")));

        add( new PaletteOngletPanel("Notes", panNotes));



//        toolBarOrnements.add(
//                new ElementsMusicauxPanel(
//                  new ElementMusicalImage(getCurseur(), "mordent.png"),
//                  getImageIcon("/musicwriter/donnees/resources/mordent.png")));
//
//        toolBarOrnements.add(
//                new ElementsMusicauxPanel(
//                  new ElementMusicalImage(getCurseur(), "pointdorgue.png"),
//                  getImageIcon("/musicwriter/donnees/resources/pointdorgue.png")));
//        add( new PaletteOngletPanel("Ornements", toolBarOrnements));





        PaletteContainerPanel toolBarSilences = new PaletteContainerPanel();
        toolBarSilences.add(
               new ElementsMusicauxPanel(
                new Silence( getDebutMoment(), new Duree(new Rational(4, 1)), getPortee(), Hauteur.getDo0() ),
                getImageIcon("pause.png", 0.2)));

        toolBarSilences.add(
               new ElementsMusicauxPanel(
                new Silence( getDebutMoment(), new Duree(new Rational(2, 1)), getPortee(), Hauteur.getDo0() ),
                getImageIcon("demi-pause.png", 0.2)));

        toolBarSilences.add(
               new ElementsMusicauxPanel(
                new Silence( getDebutMoment(), new Duree(new Rational(1, 1)), getPortee(), Hauteur.getDo0() ),
                getImageIcon("soupir.png", 0.2)));

        toolBarSilences.add(
               new ElementsMusicauxPanel(
                new Silence( getDebutMoment(), new Duree(new Rational(1, 2)), getPortee(), Hauteur.getDo0() ),
                getImageIcon("demi-soupir.png", 0.2)));

        toolBarSilences.add(
               new ElementsMusicauxPanel(
                new Silence( getDebutMoment(), new Duree(new Rational(1, 4)), getPortee(), Hauteur.getDo0() ),
                getImageIcon("quart-de-soupir.png", 0.2)));

        toolBarSilences.add(
               new ElementsMusicauxPanel(
                new Silence( getDebutMoment(), new Duree(new Rational(1, 8)), getPortee(), Hauteur.getDo0() ),
                getImageIcon("huit-de-soupir.png", 0.2)));

        toolBarSilences.add(
               new ElementsMusicauxPanel(
                new Silence( getDebutMoment(), new Duree(new Rational(1, 16)), getPortee(), Hauteur.getDo0() ),
                getImageIcon("seize-de-soupir.png", 0.2)));

        add( new PaletteOngletPanel("Silences", toolBarSilences));

        PaletteContainerPanel toolBarAccords = new PaletteContainerPanel();
        toolBarAccords.add(
               new ElementsMusicauxPanel(
                getSelectionAccordMajeur(), true));

        toolBarAccords.add(
               new ElementsMusicauxPanel(
                getSelectionAccordMineur(), true));
        
        toolBarAccords.add(
                new ElementsMusicauxPanel(getSelectionAccompagnementAccord()), true);

        add( new PaletteOngletPanel("Accords", toolBarAccords));
//        toolBarArpeges.add(
//                new ElementsMusicauxPanel(
//                new ElementMusicalArpege(getCurseur(),
//                                     new Curseur(getDebutMoment(), new Hauteur(7, Hauteur.Alteration.NATUREL), getPortee())),
//                getImageIcon("arpege.png")));
//        add( new PaletteOngletPanel("Arpèges", toolBarArpeges));




        separation();



        

        PaletteContainerPanel toolBarNuances = new PaletteContainerPanel();
        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalCrescendoDecrescendo(getCurseur(),
                                     new Curseur(new Moment(new Rational(3, 1)), new Hauteur(0, Hauteur.Alteration.NATUREL), getPortee())),
                getImageIcon("crescendo.png")));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalCrescendoDecrescendo(
                new Curseur(new Moment(new Rational(3, 1)), 
                            new Hauteur(0, Hauteur.Alteration.NATUREL),
                            getPortee()),
                getCurseur()),
                getImageIcon("descrescendo.png")));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.PPP)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.PP)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.P)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.MP)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.MF)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.F)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.FF)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalNuance(getCurseur(), ElementMusicalNuance.Nuance.FFF)));

        toolBarNuances.add(new ElementsMusicauxPanel(
                new ElementMusicalCourbe(getCurseur(),
                       new Curseur(new Moment(new Rational(2, 1) ),
                                   Hauteur.getSol0(), getPortee() ),
                       new Curseur(new Moment(new Rational(3, 1) ),
                                   Hauteur.getDo0(), getPortee() )),
                                   getImageIcon("phrase.png")));

        add( new PaletteOngletPanel("Nuances", toolBarNuances));


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBarAccords = new javax.swing.JToolBar();
        toolBarArpeges = new javax.swing.JToolBar();
        toolBarOrnements = new javax.swing.JToolBar();
        toolBarClef = new javax.swing.JToolBar();
        toolBarTonalites = new javax.swing.JToolBar();
        toolBarNuances = new javax.swing.JToolBar();
        toolBarNotes = new javax.swing.JPanel();
        toolBarBarreMesure = new javax.swing.JToolBar();
        toolBarSilences = new javax.swing.JPanel();
        toolBarSignature = new javax.swing.JPanel();

        toolBarAccords.setFloatable(false);
        toolBarAccords.setOrientation(1);
        toolBarAccords.setRollover(true);
        toolBarAccords.setMaximumSize(new java.awt.Dimension(64, 23232323));
        toolBarAccords.setMinimumSize(new java.awt.Dimension(64, 23232323));
        toolBarAccords.setName("toolBarAccords"); // NOI18N
        toolBarAccords.setPreferredSize(null);

        toolBarArpeges.setFloatable(false);
        toolBarArpeges.setOrientation(1);
        toolBarArpeges.setRollover(true);
        toolBarArpeges.setMaximumSize(new java.awt.Dimension(64, 23232323));
        toolBarArpeges.setMinimumSize(new java.awt.Dimension(64, 23232323));
        toolBarArpeges.setName("toolBarArpèges"); // NOI18N
        toolBarArpeges.setPreferredSize(null);

        toolBarOrnements.setFloatable(false);
        toolBarOrnements.setOrientation(1);
        toolBarOrnements.setRollover(true);
        toolBarOrnements.setMaximumSize(new java.awt.Dimension(64, 23232323));
        toolBarOrnements.setMinimumSize(new java.awt.Dimension(64, 23232323));
        toolBarOrnements.setName("toolBarOrnements"); // NOI18N
        toolBarOrnements.setPreferredSize(null);

        toolBarClef.setFloatable(false);
        toolBarClef.setOrientation(1);
        toolBarClef.setRollover(true);
        toolBarClef.setMaximumSize(new java.awt.Dimension(64, 23232323));
        toolBarClef.setMinimumSize(new java.awt.Dimension(64, 23232323));
        toolBarClef.setName("toolBarClef"); // NOI18N
        toolBarClef.setPreferredSize(null);

        toolBarTonalites.setFloatable(false);
        toolBarTonalites.setOrientation(1);
        toolBarTonalites.setRollover(true);
        toolBarTonalites.setMaximumSize(new java.awt.Dimension(64, 23232323));
        toolBarTonalites.setMinimumSize(new java.awt.Dimension(64, 23232323));
        toolBarTonalites.setName("toolBarTonalites"); // NOI18N
        toolBarTonalites.setPreferredSize(null);

        toolBarNuances.setFloatable(false);
        toolBarNuances.setOrientation(1);
        toolBarNuances.setRollover(true);
        toolBarNuances.setMaximumSize(new java.awt.Dimension(64, 23232323));
        toolBarNuances.setMinimumSize(new java.awt.Dimension(64, 23232323));
        toolBarNuances.setName("toolBarNuances"); // NOI18N
        toolBarNuances.setPreferredSize(null);

        toolBarNotes.setMaximumSize(new java.awt.Dimension(128, 32767));
        toolBarNotes.setMinimumSize(new java.awt.Dimension(128, 10));
        toolBarNotes.setName("toolBarNotes"); // NOI18N
        toolBarNotes.setPreferredSize(new java.awt.Dimension(128, 100));

        toolBarBarreMesure.setFloatable(false);
        toolBarBarreMesure.setOrientation(1);
        toolBarBarreMesure.setRollover(true);
        toolBarBarreMesure.setMaximumSize(new java.awt.Dimension(64, 23232323));
        toolBarBarreMesure.setMinimumSize(new java.awt.Dimension(64, 23232323));
        toolBarBarreMesure.setName("toolBarBarreMesure"); // NOI18N

        toolBarSilences.setMaximumSize(new java.awt.Dimension(64, 32767));
        toolBarSilences.setMinimumSize(new java.awt.Dimension(64, 10));
        toolBarSilences.setName("toolBarSilences"); // NOI18N
        toolBarSilences.setPreferredSize(new java.awt.Dimension(64, 100));
        toolBarSilences.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        toolBarSignature.setName("toolBarSignature"); // NOI18N
        toolBarSignature.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        setName("Form"); // NOI18N
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar toolBarAccords;
    private javax.swing.JToolBar toolBarArpeges;
    private javax.swing.JToolBar toolBarBarreMesure;
    private javax.swing.JToolBar toolBarClef;
    private javax.swing.JPanel toolBarNotes;
    private javax.swing.JToolBar toolBarNuances;
    private javax.swing.JToolBar toolBarOrnements;
    private javax.swing.JPanel toolBarSignature;
    private javax.swing.JPanel toolBarSilences;
    private javax.swing.JToolBar toolBarTonalites;
    // End of variables declaration//GEN-END:variables

    private void separation() {
        add(new PaletteSeparator());
    }

    private Moment getMomentUn() {
        return new Moment(new Rational(1, 1));
    }

}
