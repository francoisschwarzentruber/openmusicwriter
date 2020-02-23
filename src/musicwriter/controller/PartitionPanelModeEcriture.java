/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import musicwriter.donnees.Hauteur.Alteration;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.*;
import musicwriter.gui.Graphics;
import musicwriter.gui.Point;
import musicwriter.gui.StyloEsquisse;
import musicwriter.gui.partitionaffichage.Systeme;
import musicwriter.guiswing.NoteEcoute;
import musicwriter.mouserecognition.MouseRecognitionActionListener;
import musicwriter.mouserecognition.MouseShape;
import musicwriter.mouserecognition.ShapeAction;
import musicwriter.mouserecognition.ShapeActionsDictionnary;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeEcriture extends PartitionPanelModeStyletEsquisse {

    private Curseur curseurTravail;
    private boolean curseurTravailEtat = true;
    private Partie partieSiDevantSysteme = null;
    static private Note sourisCurseurNote = null;

    private Timer tmrCurseurTravail = new Timer(1000, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            curseurTravailEtat = !curseurTravailEtat;
            getController().update();
        }
    });
    
    private long lastClick = 0;
    
    
    /**
     * durée de la note qu'on écrit.
     * c'est statique car si on change de mode et qu'on revient à celui là...
     * on garde la durée qu'on avait écrite avant
     */
    static private Duree curseurNoteDuree = new Duree(new Rational(1, 1));

    private ShapeActionsDictionnary shapeDico = getShapeActionsDictionnary();




    static public Duree getCurseurNoteDureeEntree()
    {
        return curseurNoteDuree;
    }
    
    
    final private MachineEntreeListener machineEntreeListener;

    public PartitionPanelModeEcriture(Controller controller) {
        super(controller);
        curseurTravail = controller.getCurseurSouris();
        
        machineEntreeListener = new MachineEntreeListener() {

            @Override
            public void whenNoteEnfoncee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes) {
                setCurseurTravail(new Curseur(curseurTravail.getMoment(), hauteur, curseurTravail.getPortee()));
                curseurSourisInsererNotePuisDecalerCurseur();
            }

            @Override
            public void whenNoteRelachee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes) {
            }
        };
        
        
        tmrCurseurTravail.start();
        
        getController().getMachineEntreeMIDI().addMachineEntreeListener(machineEntreeListener);
    }


    private void setSourisCurseurNote(Note note)
    {
        sourisCurseurNote = note;
    }


    private void sourisCurseurNoteAucune()
    {
        sourisCurseurNote = null;

    }





     static public void setNoteDuree(Duree d)
    {
        curseurNoteDuree = d;

        if(sourisCurseurNote != null)
             sourisCurseurNote.setDuree(d);
  
    }



    public void devinerVoix()
    {
        Curseur curseurAvecVoixDevinee = getPartitionDonnees().voixDeviner(
                getController().getCurseurSouris(),
                curseurNoteDuree);
        getController().setCurseurSouris(curseurAvecVoixDevinee);
    }



    private void curseurSourisInsererNote()
    {
        Curseur curseur = getController().getCurseurSouris();
        
        Set<Note> notes = getPartitionDonnees().getNotesPorteeQuiCommencent(curseur.getMoment(), curseur.getPortee());
        
        int count = 0;
        
        Voix voix = curseur.getVoix();
        for(Note n : notes)
        {
            if(n.getHauteur().getNumero() == curseur.getHauteur().getNumero())
                voix = new Voix();
        }
        
        if(count < 2)
        {
            Note noteAAjouter = new Note(curseur.getMoment(),
                                        getCurseurNoteDureeEntree(),
                                        curseur.getHauteur(),
                                        curseur.getPortee(),
                                        voix);

            (new NoteEcoute(noteAAjouter, getController().getMachineSortieMIDI())).ecouter();

            getHistoire().executer(new PartitionActionElementMusicalAjouter(noteAAjouter));
            getPartitionVue().miseEnPageCalculer(curseur.getMoment());
        }
        
    }


    @Override
    public void mouseClicked(ControllerMouseEvent evt) {
        if(evt.getClickCount() == 2)
        {
            modeChanger(new PartitionPanelModeSelection(getController(), getPartitionDonnees().getSelectionMesure(getCurseur(evt.getPoint()).getMoment())));
        }
    }

    @Override
    public void mousePressed(ControllerMouseEvent evt) {
        super.mousePressed(evt);
        Curseur curseur = getController().getCurseur(evt.getPoint());

        if(curseur.isSurElementMusical())
        {
            modeChanger(new PartitionPanelModeSelectionDeplacementOuCopie(getController(),
                        curseur,
                        new Selection(curseur.getElementMusical())));

//            if(isClavierToucheSpeciale(evt))
//                getController().modifierSourisCurseurMainQuiPrendPlus();
//            else
//                getController().modifierSourisCurseurMainQuiPrend();
        }
        else if(evt.isControlDown())
        {
            modeChanger(new PartitionPanelModeSelection(getController(), new Selection()));
            //modeChanger(new PartitionPanelModeInsertionTemps(getController(), getCurseurTravailMoment()));
        }
        // pas de déplacement ou copie quand on est sur le trait de croche...
        // car ça fait conflit avec l'esquisse au stylet d'un trait de croche!
//        else if(getPartitionVue().getAffichageCrocheRelie(evt.getPoint()) != null)
//        {
//            AffichageCrocheReliee a = getPartitionVue().getAffichageCrocheRelie(evt.getPoint());
//            modeChanger(new PartitionPanelModeSelectionDeplacementOuCopie(getPartitionPanel(),
//                        curseur,
//                        a.getSelection()));
//
//            if(isClavierToucheSpeciale(evt))
//                getPartitionPanel().modifierSourisCurseurMainQuiPrendPlus();
//            else
//                getPartitionPanel().modifierSourisCurseurMainQuiPrend();
//        }
            
    }

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        //?getController().getFenetrePrincipale().modeEcriture();
        styloEsquisseTraiter(getStyloEsquisse());
        super.mouseReleased(evt);
        
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
        super.mouseDragged(evt);
    }


    public Curseur getCurseur(Point point)
    {
        return getPartitionVue().getCurseur(point);
    }


    @Override
    public void mouseMoved(ControllerMouseEvent evt) {
        super.mouseMoved(evt);
        partieSiDevantSysteme = getPartitionVue().getPartieSiDevantSysteme(evt.getPoint());

        if(partieSiDevantSysteme != null)
        {
            sourisCurseurNoteAucune();
         //   getPartitionPanel().modifierSourisCurseurMainDoigt();
         //   getPartitionPanel().setPopupMenuPartie();
            
        }
        else
        if(getPartitionVue().getAffichageAccordAvecHampeFin(evt.getPoint()) != null)
        {
            sourisCurseurNoteAucune();
        //    getPartitionPanel().modifierSourisCurseurCrayonTraitCroche();
        }
        else
            //conflit avec l'écriture de notes
//        if(getPartitionVue().getAffichageCrocheRelie(evt.getPoint()) != null)
//        {
//            sourisCurseurNoteAucune();
//            getPartitionPanel().modifierSourisCurseurMainDoigt();
//        }
//        else
        if(getController().getCurseurSouris().isSurElementMusical())
        {
            sourisCurseurNoteAucune();
            getController().fireCursorOverSomething();
//            getController().modifierSourisCurseurMainDoigt();
        }
        else
        {
            recalculerNoteApartirCurseurSouris();
            getController().fireCursorOverNothing();
//          
        }
    }




    private void setCurseurSourisControleClavier(Curseur curseur)
    {
        Curseur curseurAvecVoixDevinee = getPartitionDonnees().voixDeviner(curseur, curseurNoteDuree);
        setCurseurTravail(curseurAvecVoixDevinee);
        getController().afficherMomentEcran(curseur.getMoment());
        recalculerNoteApartirCurseurSouris();
    }



    
    

    
    @Override
    public void keyPressed(ControllerKeyEvent evt) {
        if(evt.getKeyChar() == ' ')
        {
             getController().getHistoire().executer(
                     new PartitionActionElementMusicalAjouter(
                          new Silence(getController().getCurseurSouris().getMoment(),
                                      getCurseurNoteDureeEntree(),
                                      getController().getCurseurSouris().getPortee(),
                                      getController().getCurseurSouris().getHauteurSilenceStandardSiProche())));

             getController().calculer(getController().getCurseurSouris().getMoment());
        }


        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            Set<ElementMusical> E = getPartitionDonnees().getElementsMusicauxPrecedentsMemeVoix(getController().getCurseurSouris());

            if(!E.isEmpty())
            {
                getHistoire().executer(new PartitionActionSelectionSupprimer(new Selection(E)));

                final Moment momentPrecedent = E.iterator().next().getDebutMoment();
                setCurseurSourisControleClavier(
                         getController().getCurseurSouris().getCurseurAutreMoment(
                         momentPrecedent));
                getController().calculer(momentPrecedent);
                
            }
            
        }//à faire
//         if(isClavierToucheNoteSaisie(evt))
//         {
//             int hnumero = getClavierToucheNoteSaisieNumero(evt);
//
//             getPartitionPanel().setCurseurSouris(getPartitionPanel().getCurseurSouris().getCurseurNoteModuloOctave(hnumero));
//             curseurSourisInsererNotePuisDecalerCurseur();
//
//         }
         
         else if(evt.getKeyCode() == 10)
         {
             curseurSourisInsererNotePuisDecalerCurseur();
         }
         else if(evt.getKeyCode() == KeyEvent.VK_DELETE)
         {
            ElementMusical elProche = getPartitionDonnees().getElementMusicalProche(getController().getCurseurSouris());

            if(elProche != null)
            {
                getHistoire().executer(new PartitionActionElementMusicalSupprimer(elProche));
                getController().calculer(elProche.getDebutMoment());
            }
            else //s'il n'y a rien d'écrit... on supprime un "blanc"
            {
                getController().tempsSupprimerSousCurseur(getCurseurTravail());
            }
         }
         else if(evt.getKeyCode()== KeyEvent.VK_UP & evt.isControlDown())
         {
             curseurSourisPorteeDeplacementRelatif(-1);
         }
         else if(evt.getKeyCode()== KeyEvent.VK_DOWN & evt.isControlDown())
         {
             curseurSourisPorteeDeplacementRelatif(1);
         }
         else if(((evt.getKeyCode()== KeyEvent.VK_LEFT) || (evt.getKeyCode()== KeyEvent.VK_RIGHT)) & evt.isShiftDown())
         {
             modeChanger(new PartitionPanelModeSelection(getController(),
                         getPartitionDonnees().getSelectionPlage(getController().getCurseurSouris(),
                                                                 getController().getCurseurSouris())));
         }
         else if(evt.getKeyCode()== KeyEvent.VK_LEFT & evt.isControlDown())
         {
             final Moment debutMesureOuAller;
             if(getPartitionDonnees().isDebutMesure(getCurseurTravailMoment()))
             {
                debutMesureOuAller = getPartitionDonnees().getMesurePrecedenteMomentDebut(getCurseurTravailMoment());
             }
             else
             {
                 debutMesureOuAller = getPartitionDonnees().getMesureMomentDebut(getCurseurTravailMoment());
             }

             setCurseurSourisControleClavier(
                     getController().getCurseurSouris().getCurseurAutreMoment(
                     debutMesureOuAller));
             getController().repaint();
         }
         else if(evt.getKeyCode()== KeyEvent.VK_RIGHT & evt.isControlDown())
         {
             setCurseurSourisControleClavier(
                     getController().getCurseurSouris().getCurseurAutreMoment(
                     getPartitionDonnees().getMesureSuivanteMomentDebut(getCurseurTravailMoment())));
             getController().repaint();
         }
         else if(evt.getKeyCode()== KeyEvent.VK_LEFT)
         {
             setCurseurSourisControleClavier(
                     getCurseurTravail().getCurseurAutreMoment(
                     getPartitionDonnees().getMomentPrecedent(getCurseurTravailMoment())));
             getController().repaint();
         }
         else if(evt.getKeyCode()== KeyEvent.VK_RIGHT)
         {
             if(getPartitionDonnees().getMomentSuivant(getCurseurTravailMoment()) == null)
                 return;
             
             setCurseurSourisControleClavier(
                     getCurseurTravail().getCurseurAutreMoment(
                     getPartitionDonnees().getMomentSuivant(getCurseurTravailMoment())));
             getController().repaint();
         }
         else if(evt.getKeyCode() == KeyEvent.VK_UP)
         {
             curseurSourisHauteurDeplacementRelatif(1);
             
         }
        else if(evt.getKeyCode() == KeyEvent.VK_DOWN)
         {
             curseurSourisHauteurDeplacementRelatif(-1);
         }
        else if(evt.getKeyCode() == KeyEvent.VK_INSERT)
         {
             modeChanger(new PartitionPanelModeInsertionTemps(getController(), getCurseurTravailMoment()));
         }
        else
        {
            if(DureeSaisie.traiter(getNumber(evt)))
                setNoteDuree(DureeSaisie.getDuree());
            
        }
         //DureeEntreeFractionPanel.tryDureePanel(evt, getController());

         
    }

    
    
    
    
        
        
        
    @Override
    public void keyReleased(ControllerKeyEvent evt) {
    }

    @Override
    public void keyTyped(ControllerKeyEvent evt) {
    }


    /**
     * 
     * @return moment du curseur de travail
     */
    private Moment getCurseurTravailMoment()
    {
        return getCurseurTravail().getMoment();
    }


    private Portee getPortee()
    {
        return getController().getCurseurSouris().getPortee();
    }

    private void sourisCurseurNoteAfficher(Graphics g)
    {
        
        if(sourisCurseurNote != null)
        {
        /*à quoi correspond ce dessin ?*/
            
            Systeme systeme = getPartitionVue().getSysteme(sourisCurseurNote.getDebutMoment());
            Point p = systeme.getPoint(sourisCurseurNote.getCurseur());
            double p2x = systeme.getXNotes(getCurseurNoteDureeEntree().getFinMoment(sourisCurseurNote.getDebutMoment()));

            g.setColor(Color.GRAY);
            g.setAlpha(0.5f);
            g.setPenWidth(3.0f);
//            g.drawLine((int) p.x,
//                    (int) p.y,
//                    (int) p2x,
//                    (int) p.y);
            g.setAlpha(1.0f);
            
            getPartitionVue().afficherElementMusicalDehors(g, sourisCurseurNote);
        }

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!isStyloEsquisse())
        {
            
            
            if(partieSiDevantSysteme == null)
                if(curseurTravail.isMemePorteeMemeMoment(getController().getCurseurSouris()))
                {
                    sourisCurseurNoteAfficher(g);
                    getPartitionVue().afficherContexte(g, getController().getCurseurSouris());
                    getPartitionVue().getSysteme(curseurTravail.getMoment()).afficherNomNote(g, getController().getCurseurSouris());
                    
                }
            
            curseurTravailAfficher(g);
            
        }

    }





    




    /**
     * cette fonction s'occupe de traiter le dessin réalisé au stylet
     * @param styloEsquisse 
     */
    private void styloEsquisseTraiter(StyloEsquisse styloEsquisse) {
        if(styloEsquisse.isGribouilli())
        {
            styloEsquisseGribouilliElementsMusicauxSupprimer(styloEsquisse);
            
        }

          
        if(styloEsquisse.isPoint())
        {
                Curseur curseur = getCurseur(styloEsquisse.getPremierPoint());
               //conflit avec l'écriture de notes
                //AffichageCrocheReliee a = getPartitionVue().getAffichageCrocheRelie(styloEsquisse.getPremierPoint());
               // if(a != null)
               //     modeChanger(new PartitionPanelModeSelection(getPartitionPanel(), a.getSelection()));
               // else 
               if(curseur.isSurElementMusical())
                    modeChanger(new PartitionPanelModeSelection(getController(), 
                                                                new Selection(curseur.getElementMusical())));
                else if(!(getPartitionVue().getAffichageAccordAvecHampeFin(styloEsquisse.getPremierPoint()) != null))
                {
                    if(partieSiDevantSysteme != null)
                        return;

                    if(curseurTravail.isMemePorteeMemeMoment(curseur) && (System.currentTimeMillis() > lastClick + 400))
                    {
                        curseurSourisInsererNotePuisDecalerCurseur();
                    }
                    else
                    {
                        lastClick = System.currentTimeMillis();
                        curseurTravail = curseur;
                    }
                }
        }
        else if(styloEsquisse.isSegmentVertical() && getPartitionVue().getNote(styloEsquisse.getDernierPoint()) != null)
        {
              Note note = getPartitionVue().getNote(styloEsquisse.getDernierPoint());
              getHistoire().executer(new PartitionActionSelectionDureeFixeChanger(getPartitionDonnees(), new Selection(note), 
                                     note.getDuree().getDureeDiviserParDeux()));
              getController().calculer(note.getDebutMoment());
        }
        else
        if(styloEsquisse.isSegmentVertical() && styloEsquisse.getSegmentLongueur() > 50)
        {
            styloEsquisseBarreDeMesureAjouter(styloEsquisse);
        }
        else if(styloEsquisse.isSegmentVertical() && styloEsquisse.getSegmentLongueur() < 40)
            //soupir dessiné au stylet
        {
            
            getHistoire().executer(new PartitionActionElementMusicalAjouter(
                    new Silence(styloEsquisse.getCurseur().getMoment(),
                                new Duree(new Rational(1, 1)),
                                styloEsquisse.getCurseur().getPortee(),
                                styloEsquisse.getCurseur().getHauteurSilenceStandardSiProche())));
            getPartitionVue().miseEnPageCalculer(styloEsquisse.getCurseur().getMoment());
        }
        else if(styloEsquisse.isSegmentHorizontal() && (getPartitionVue().getSilence(styloEsquisse.getDernierPoint()) != null))
        {

            Silence silence = getPartitionVue().getSilence(styloEsquisse.getDernierPoint()) ;

            getHistoire().executer(new PartitionActionElementMusicalDureeChanger(silence, silence.getDuree().getDureeDiviserParDeux()));
            getPartitionVue().miseEnPageCalculer(silence.getDebutMoment());
 
        }
        else if(styloEsquisse.isSegmentHorizontal() &&  getPartitionVue().getNoteProche(styloEsquisse.getPremierPoint()) != null)
        {
            Note note =  getPartitionVue().getNoteProche(styloEsquisse.getPremierPoint());
            getHistoire().executer(new PartitionActionNoteLierALaSuivante(note, true));
            getPartitionVue().miseEnPageCalculer(note.getDebutMoment());
        }
        else if(!shapeDico.recognize(new MouseShape(styloEsquisse.getPoints())))
        {
            Selection selection = traiterSelectionPolygoneStyloEsquisse(styloEsquisse);
            
            
            if(!selection.isVide())
                modeChanger(new PartitionPanelModeSelection(getController(), selection));
        }

        
//        {
//            histoire.executer(
//                             new PartitionActionElementMusicalAjouter(
//                                         new ElementMusicalCourbe(styloEsquisse.getCurseursVagues())
//                                            )
//                                      );
//            partitionVue.miseEnPageCalculer();
//        }

    }

    @Override
    public void paintComponentAvant(Graphics g) {
       if(partieSiDevantSysteme == null)
            getPartitionVue().afficherContexteFond(g, getController().getCurseurSouris());
       
//       if(getController().getCurseurSouris().isSurElementMusical())
//       {
//           SelectionAffichage a = new SelectionAffichage(getPartitionVue(), new Selection(getController().getCurseurSouris().getElementMusical()));
//           a.drawSelectionFond(g, true);
//       }      

    }


/**
 * Dit que l'altération du curseur courant est celle donné en paramètre
 * @param alteration
 */
    public void setAlterationCourante(Alteration alteration)
    {
        sourisCurseurNote.getHauteur().setAlteration(alteration);
        getController().repaint();
        
    }

    static private void addResource(ShapeActionsDictionnary D, String name, MouseRecognitionActionListener action)
    {
        InputStreamReader isr = new InputStreamReader(Graphics.class.getResourceAsStream(name));
        BufferedReader fichier = new BufferedReader(isr);
        try {
            D.addShapeAction(new ShapeAction(new MouseShape(fichier), action));
        } catch (IOException ex) {
            Logger.getLogger(PartitionPanelModeEcriture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
     private ShapeActionsDictionnary getShapeActionsDictionnary() {
        ShapeActionsDictionnary D = new ShapeActionsDictionnary();

        addResource(D, "resources/pattern_demi_pause.txt", new MouseRecognitionActionListener() {
            @Override
                public void actionPerformed(MouseShape e) {
                    Curseur curseur = getController().getCurseur(e.getPoints().get(0));
                    getHistoire().executer(new PartitionActionElementMusicalAjouter(new Silence(curseur.getMoment(), new Duree(new Rational(2,1)), curseur.getPortee(), curseur.getHauteurSilenceStandardSiProche())));
                    getController().calculer(curseur.getMoment());
                    
                }
            });

        addResource(D, "resources/pattern_demi_soupir.txt", new MouseRecognitionActionListener() {
            @Override
                public void actionPerformed(MouseShape e) {
                    Curseur curseur = getController().getCurseur(e.getPoints().get(0));
                    getHistoire().executer(new PartitionActionElementMusicalAjouter(new Silence(curseur.getMoment(), new Duree(new Rational(1,2)), curseur.getPortee(), curseur.getHauteurSilenceStandardSiProche())));
                    getController().calculer(curseur.getMoment());

                }
            });

        
        addResource(D, "resources/pattern_clef_de_sol.txt", new MouseRecognitionActionListener() {
            @Override
                public void actionPerformed(MouseShape e) {
                     Curseur curseur = getController().getCurseur(e.getPoints().get(0));
                     getHistoire().executer(
                             new PartitionActionElementMusicalAjouter(
                             new ElementMusicalClef(curseur.getMoment(), curseur.getPortee(), Portee.Clef.ClefDeSol)));
                     getController().calculer(curseur.getMoment());
                }
            });

        addResource(D, "resources/pattern_clef_de_fa.txt", new MouseRecognitionActionListener() {
            @Override
                public void actionPerformed(MouseShape e) {
                    Curseur curseur = getController().getCurseur(e.getPoints().get(0));
                     getHistoire().executer(
                             new PartitionActionElementMusicalAjouter(
                             new ElementMusicalClef(curseur.getMoment(), curseur.getPortee(), Portee.Clef.ClefDeFa)));
                     getController().calculer(curseur.getMoment());
                }
            });

        return D;
    }


     /**
      *
      * @param evt
      * @return le numéro de la note
      */
    static private int getClavierToucheNoteSaisieNumero(KeyEvent evt) {
        switch(evt.getKeyChar())
        {
            case 'a': return 5;
            case 'b': return 6;
            case 'c': return 0;
            case 'd': return 1;
            case 'e': return 2;
            case 'f': return 3;
            case 'g': return 4;
            default: return -1;
        }
    }

    private void curseurSourisHauteurDeplacementRelatif(int i) {
        final Hauteur hauteur = getPartitionDonnees()
                     .getTonalite(getCurseurTravailMoment())
                     .getHauteur(getController().getCurseurSouris().getHauteur().getNumero()+i);

         setCurseurTravail(getCurseurTravail().getCurseurAutreHauteur(hauteur));
         getController().repaint();
    }

    public void recalculerNoteApartirCurseurSouris() {
        Note note = new Note(getController().getCurseurSouris().getMoment(),
                                  getCurseurNoteDureeEntree(),
                                  getController().getCurseurSouris().getHauteur(),
                                  getController().getCurseurSouris().getPortee());
        if(getPartitionVue().getAffichageAccordProche(getController().getCurseurSouris()) != null)
        {
            note.setHampeDirection(getPartitionVue().getAffichageAccordProche(getController().getCurseurSouris()).getHampeDirection());
        }

        setSourisCurseurNote(note);
//        getController().modifierSourisCurseurEcrireNote();
    }

    private void curseurSourisInsererNotePuisDecalerCurseur() {
        curseurSourisInsererNote();
        setCurseurTravail(getController().getCurseurSouris().getCurseurSuivant(curseurNoteDuree));
         recalculerNoteApartirCurseurSouris();
         getController().repaint();
    }

    private void curseurSourisPorteeDeplacementRelatif(int i) {
        
        final Portee portee;

        if(i == -1)
           portee = getPartitionVue().getSysteme(getCurseurTravailMoment()).getPorteePrecedente(getPortee());
        else
            portee = getPartitionVue().getSysteme(getCurseurTravailMoment()).getPorteeSuivante(getPortee());


        if(portee == getPortee())
            return;

         setCurseurSourisControleClavier(
                 new Curseur(getCurseurTravailMoment(), getPartitionDonnees().getTonalite(getCurseurTravailMoment())
                     .getHauteur(0), portee));
         getController().repaint();
    }

    private void styloEsquisseGribouilliElementsMusicauxSupprimer(StyloEsquisse styloEsquisse) {
        
            ArrayList<ElementMusical> elementsMusicaux = new ArrayList<ElementMusical>();

            for(Curseur curseur : styloEsquisse.getCurseurs())
            {
                if(curseur.isSurElementMusical())
                {
                    if(!elementsMusicaux.contains(curseur.getElementMusical()))
                        elementsMusicaux.add(curseur.getElementMusical());

                }
            }
            for(ElementMusical element : elementsMusicaux)
            {
                     getHistoire().executer(
                             new PartitionActionElementMusicalSupprimer(
                                        element
                                            )
                                      );

            }
            getPartitionVue().miseEnPageCalculer();
    }

    
    
    private void styloEsquisseBarreDeMesureAjouter(StyloEsquisse styloEsquisse) {
        
            final Moment barreMoment = getController().getMomentBarre(styloEsquisse.getSegmentPremierPoint());
            final Moment momentDebutMesure = getPartitionDonnees().getMesureMomentDebut(barreMoment);
            final Duree mesureDuree;
            if(getPartitionDonnees().getMesureSignature(barreMoment) != null)
                 mesureDuree = getPartitionDonnees().getMesureSignature(barreMoment).getMesuresDuree();
            else
                mesureDuree = Duree.getDureeNulle();
            
            
            /**
             * Si on trace une barre au début, où on trace une barre pile à côté d'une barre existante
             */
            if(getPartitionVue().getPartitionDonnees().getMomentDebut().isEgal(barreMoment)
                    || getPartitionVue().getPartitionDonnees().isBarreDeMesure(barreMoment))
            {
                Moment momentFin = mesureDuree.getFinMoment(momentDebutMesure);
                PartitionActionActionComposee a = new PartitionActionActionComposee();
                a.actionAjouter(new PartitionActionInsererTemps(momentDebutMesure,
                            momentFin));
                a.actionAjouter(new PartitionActionElementMusicalAjouter(new BarreDeMesure(momentFin)));
                getHistoire().executer(a);
            }
            /** on trace une barre à la fin de la partition et que l'on est après la fin de l'éventuelle
             * première mesure
             * 
             */
            else if(getPartitionVue().getPartitionDonnees().isMomentApresFin(barreMoment) &&
                    barreMoment.isApres(getPartitionDonnees().getMesureSignature(barreMoment).getMesuresDuree().getFinMoment(getPartitionDonnees().getMomentDebut())))
            {
                Moment momentFin = mesureDuree.getFinMoment(momentDebutMesure);
                getHistoire().executer(new PartitionActionElementMusicalAjouter(new BarreDeMesure(momentFin)));
            }
            else
            {
                  getHistoire().executer(new PartitionActionElementMusicalAjouter(new BarreDeMesure(barreMoment)));
            }
            getPartitionVue().miseEnPageCalculer(momentDebutMesure);
    }

    private void curseurTravailAfficher(Graphics g) {
        if(curseurTravailEtat)
        {
            Systeme s = getPartitionVue().getSysteme(curseurTravail.getMoment());
            double x = s.getXNotes(curseurTravail.getMoment()) - s.getNoteRayon(curseurTravail.getPortee()) - 3;
            double y1 = s.getY(curseurTravail.getPortee(), -3);
            double y2 = s.getY(curseurTravail.getPortee(), 3);
            g.setPenWidth(1.5f);
            g.setColor(Color.BLACK);
            g.drawLine(x, y1, x, y2);
            g.drawLine(x-3, y1, x+3, y1);
            g.drawLine(x-3, y2, x+3, y2);
        }
        
    }

    
    private void setCurseurTravail(Curseur curseur) {
        curseurTravail = curseur;
        getController().setCurseurSouris(curseur);
    }

    public Curseur getCurseurTravail() {
        return curseurTravail;
    }

    @Override
    void whenQuit() {
        if(getController().getMachineEntreeMIDI() != null)
            getController().getMachineEntreeMIDI().deleteMachineEntreeListener(machineEntreeListener);
        
        tmrCurseurTravail.stop();
    }


    


}
