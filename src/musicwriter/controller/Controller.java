/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import musicwriter.Erreur;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.*;
import musicwriter.gui.*;
import musicwriter.guiswing.MachineEntreeMIDI;

/**
 *
 * @author Ancmin
 */
public class Controller {
    final private Panel panel;
    private PartitionPanelMode mode = null;
    private Curseur curseurSouris = null;
    private Moment curseurSourisMomentBarre = null;
    
    private PartitionVueEcran partitionVue = null;
    private Histoire histoire = null;
    MachineEntree machineEntreeMIDI = null;
    private MachineSortie machineSortieMIDI = null;
    private PartitionLecteur partitionLecteur = null;
    private PartitionDonnees partitionDonnees = null;
    
    
    String fichierNom = null;

    public void setPartitionDonnees(PartitionDonnees partitionDonnees, int systemeLongueur) {
        this.partitionDonnees = partitionDonnees;
        partitionVue = new PartitionVueEcran(partitionDonnees,
                    partitionDonnees.getParties(),
                    1000 - 50);
        
        histoire = new Histoire(partitionDonnees, partitionVue);
        
        curseurSouris = new Curseur(getPartitionDonnees().getMomentDebut(), Hauteur.getDo0(), getPartitionDonnees().getPorteePremiere());
        this.mode = new PartitionPanelModeEcriture(this);
    }
    
    
    final private ArrayList<ControllerListener> controllerListeners = new ArrayList<ControllerListener>();

    public Controller(Panel panel) {
        this.panel = panel;
    }

    
     public void addControllerListener(ControllerListener controllerListener)
     {
        controllerListeners.add(controllerListener);
        update();
     }
     
     
     

    
    
    
    
    
    
    
    
    public Histoire getHistoire() {
        return histoire;
    }

    public void setMachineSortieMIDI(MachineSortie machineSortieMIDIEcoutee) {
        this.machineSortieMIDI = machineSortieMIDIEcoutee;
    }

    public PartitionLecteur getPartitionLecteur() {
        return partitionLecteur;
    }

    public PartitionVueEcran getPartitionVue() {
        return partitionVue;
    }

    public MachineSortie getMachineSortieMIDI() {
        return machineSortieMIDI;
    }

    public PartitionDonnees getPartitionDonnees() {
        return partitionDonnees;
    }
    
    

    /**
     * start the recording from the machineEntreeMIDI
     */
    public void recordAttachToPlaying() {
        setMode(new PartitionPanelModeEnregistrement(this));
        repaint();
        
    }



    public void play(Moment moment) {
        partitionLecteur = new PartitionLecteurTempsReel(getPartitionDonnees(),
                moment,
                machineSortieMIDI);
         
        partitionLecteur.setMomentActuel(moment);
        partitionLecteur.start();
        
        partitionLecteur.addPartitionLecteurListener(new PartitionLecteurListener() {

            @Override
            public void whenBegins() {
            }

            @Override
            public void whenStops() {
            }

            @Override
            public void refresh() {
                update();
            }
            
        });
        if (!(getMode() instanceof PartitionPanelModeLecture)) {
            setMode(new PartitionPanelModeLecture(this));
        }
        repaint();
    }

    public void pause() {
        partitionLecteur.pause();
    }

    public void replay() {
        partitionLecteur.reprendre();
    }

    public void executer(PartitionAction partitionAction) {
        histoire.executer(partitionAction);
    }

    public boolean isAnnulerPossible() {
        return histoire.isAnnulerPossible();
    }

    public void annulerLaDerniereAction() {
        histoire.annulerLaDerniereAction();
    }

    public boolean isRefairePossible() {
        return histoire.isRefairePossible();
    }

    public void refaireLaDerniereAction() {
        histoire.refaireLaDerniereAction();
    }

    public void setPartiesAffichees(Collection<Partie> parties) {
        partitionVue.setParties(parties);
    }
    


    public Collection<Partie> getPartiesAffichees() {
        return getPartitionVue().getPartiesAffichees();
    }

    

    /**
     *
     * @return le curseur qui est sous le curseur de la souris
     */
    public Curseur getCurseurSouris() {
        return curseurSouris;
    }
    
    
    public Curseur getCurseurTravail()
    {
        if(mode instanceof PartitionPanelModeEcriture)
        {
            return ((PartitionPanelModeEcriture) mode).getCurseurTravail();
        }
        else if(mode instanceof PartitionPanelModeSelection)
        {
            return ((PartitionPanelModeSelection) mode).getSelection().getCurseurDebut();
        }
        else
            return getCurseurSouris();
    }

    public Moment getCurseurSourisMomentBarre() {
        return curseurSourisMomentBarre;
    }

    

    public void dureeEntreeTraiter(Duree d) {
       // if (mode instanceof PartitionPanelModeEcriture) {
        
        PartitionPanelModeEcriture.setNoteDuree(d);

        if(getMode() instanceof PartitionPanelModeEcriture)
            ((PartitionPanelModeEcriture) getMode()).devinerVoix();
        
        update();
        
       // } else
        if (isSelection()) {
            selectionElementsMusicauxDureeSet(d);
        }
        
        repaint();
    }

    public void setCurseurSourisDuree(Duree d) {
        if (mode instanceof PartitionPanelModeEcriture) {
            PartitionPanelModeEcriture.setNoteDuree(d);
        }
        repaint();
    }

    public void selectionElementsMusicauxDureeSet(Duree d) {
        if (isSelection()) {
            PartitionActionActionComposee a = new PartitionActionActionComposee();
            a.actionAjouter(new PartitionActionSelectionDureeFixeChanger(getPartitionDonnees(), getSelection(), d));
            a.actionAjouter(new PartitionActionSelectionVoixSet(getSelection(), getSelection().getVoixPresenteAuPif()));
            histoire.executer(a);
            calculer(getSelection().getMomentDebut());
            setSelection(new Selection(getSelection().getElementsMusicaux()));
        }
        repaint();
    }

    /**
     * Définit la sélection (les elementsMusicaux en rouges affichés comme ça)
     * Si selection == null, pas de sélection
     * Si selection est un objet sélection avec personne dedans => pas de séletion
     * Sinon, la sélection courante devient l'objet sélection
     * Tout l'affichage est mis à jour
     * @param selection
     */
    public void setSelection(Selection selection) {
        if (selection == null) {
            selectionAbandonner();
        }

        if (selection.isVide()) {
            selectionAbandonner();
        } else {
            selectionEntamer(selection);
        }
    }







    
    /**
     *
     * @return la sélection courante. Si jamais il n'y a pas de sélection, cette
     * fonction crache un message d'erreur sur la console (car normalement elle ne
     * doit pas être appelé s'il n'y a pas de sélection. Utiliser isSelection() d'abord.
     * Néanmoins, elle renverra un objet Selection valide mais vide.
     */
    public Selection getSelection() {
        if (mode instanceof PartitionPanelModeSelection) {
            return ((PartitionPanelModeSelection) mode).getSelection();
        } else {
            Erreur.message("Tu veux une sélection alors qu'il n'y en a pas ?");
            return new Selection();
        }
    }

    /**
     *
     * @return true ssi il y a une sélection
     */
    public boolean isSelection() {
        return mode instanceof PartitionPanelModeSelection;
    }

    /**
     * Fait qu'il n'y a plus de sélection courante. (isSelection() renvoie faux)
     * L'affichage est géré en conséquence.
     */
    public void selectionAbandonner() {
        setMode(new PartitionPanelModeEcriture(this));

    }

    private void selectionEntamer(Selection selection) {
        setMode(new PartitionPanelModeSelection(this, selection));


    }

    
    
    




                        

public void formMousePressed(ControllerMouseEvent evt) {                                  
// quand on appuie sur un bouton de la souris

    mode.mousePressed(evt);

}                                 

public void formMouseReleased(ControllerMouseEvent evt) {                                   
// TODO add your handling code here:
    mode.mouseReleased(evt);
    repaint();
}                                  

public void formMouseMoved(ControllerMouseEvent evt) {                                
// quand on bouge la souris sans qu'aucun bouton n'est appuyé

    curseurSouris = getCurseur(evt.getPoint(), 
                               PartitionPanelModeEcriture.getCurseurNoteDureeEntree(),
                               getCurseurSouris().getPortee());
    
    curseurSourisMomentBarre = getMomentBarre(evt.getPoint());


    mode.mouseMoved(evt);
    




    repaint();


}                               

public void formMouseDragged(ControllerMouseEvent evt) {                                  
    mode.mouseDragged(evt);

}                                 

    

    public void elementMusicalTexteChangerClefBoiteDeDialogue(ElementMusicalTexte el) {
//        TexteDialog d = new TexteDialog(null, true);
//        d.setVisible(true);
//
//        if (!d.isAnnule()) {
//            histoire.executer(new PartitionActionElementMusicalSupprimer(el));
//            histoire.executer(new PartitionActionElementMusicalAjouter(
//                    new ElementMusicalTexte(el.getCurseur(), d.getTexte())));
//            selectionAbandonner();
//            calculer(el.getDebutMoment());
//        }
        setMode(new PartitionPanelModeTexte(this, el));
    }

public void formMouseClicked(ControllerMouseEvent evt) {                                  
// TODO add your handling code here:
    
    mode.mouseClicked(evt);
    

}                                 

public void formKeyPressed(ControllerKeyEvent evt) {                                
// TODO add your handling code here:
    mode.keyPressed(evt);

}                               

public void formKeyTyped(ControllerKeyEvent evt) {                              
    mode.keyTyped(evt);
}                             

public void formKeyReleased(ControllerKeyEvent evt) {                                 
    mode.keyReleased(evt);

}                                



    public void paintComponent(Graphics g, Rectangle visibleLogicalRect) {
      
        mode.paintComponentAvant(g);

        g.setColor(Color.BLACK);

        getPartitionVue().afficherPartition(g, visibleLogicalRect);

        mode.paintComponent(g);


    }

    public Curseur getCurseur(Point point) {
        return getPartitionVue().getCurseur(point);
    }


    public Curseur getCurseur(Point point, Duree duree) {
        return getPartitionVue().getCurseur(point, duree, null);
    }


    public Curseur getCurseur(Point point, Duree duree, Portee porteePreferee) {
        return getPartitionVue().getCurseur(point, duree, porteePreferee);
    }

    public Moment getMomentBarre(Point point) {
        return getPartitionVue().getMomentBarre(point);
    }



    public void setSystemeLongueur(int systemeLongueur) {
        getPartitionVue().setSystemesLongueur(systemeLongueur);
        getPartitionVue().miseEnPageCalculer();
    }

    public boolean isLecture() {
        return mode instanceof PartitionPanelModeLecture;
        //return (partitionLecteur != null);
    }

    public boolean isEnregistrement() {
        return mode instanceof PartitionPanelModeEnregistrement;
    }







   

    public void tempsInserer() {
         setMode(new PartitionPanelModeInsertionTemps(this, getCurseurSourisMomentBarre()));
        //        histoire.executer(new PartitionActionInsererTemps(getCurseurSourisMomentBarre(), new Duree(new Rational(1, 1)).getFinMoment(getCurseurSourisMomentBarre())));
//        getPartitionVue().miseEnPageCalculer(getCurseurSourisMomentBarre());
//        repaint();
    }

    public void notesLierAuxSuivantes() {
        histoire.executer(new PartitionActionSelectionNotesLierAuxSuivantes(getSelection(), true));
        getPartitionVue().miseEnPageCalculer(getCurseurSourisMomentBarre());
        repaint();
    }


    public void notesNePasLierAuxSuivantes() {
        histoire.executer(new PartitionActionSelectionNotesLierAuxSuivantes(getSelection(), false));
        getPartitionVue().miseEnPageCalculer(getCurseurSourisMomentBarre());
        repaint();
    }
      

   

    

    

    public void setElementsMusicauxDeplacer(Selection selection) {
        if (selection == null) {
            setMode(new PartitionPanelModeEcriture(this));
        } else {
            setMode(new PartitionPanelModeElementsMusicauxAInserer(this,
                    selection.getCurseurDebut(), selection));
            
            
            //TODO: modifierSourisCurseurMainQuiPrendPlus();
        }
    }

    /**
     *
     * @param evt
     * @return vrai ssi la touche enfoncée correspond à une note saisie ("a" à "h")
     * 
     */
    private boolean isClavierToucheNoteSaisie(KeyEvent evt) {
        return ('a' <= evt.getKeyChar()) && (evt.getKeyChar() <= 'g');
    }

    private int getClavierToucheNoteSaisieNumero(KeyEvent evt) {
        switch (evt.getKeyChar()) {
            case 'c':
                return 0;
            case 'd':
                return 1;
            case 'e':
                return 2;
            case 'f':
                return 3;
            case 'g':
                return 4;
            case 'a':
                return 5;
            case 'b':
                return 6;
            default: {
                Erreur.message("getClavierToucheNoteSaisieNumero");
                return 0;
            }
        }
    }

    public void selectionNotesHampesDirectionsSet(Note.HampeDirection hampeDirection) {
        if (isSelection()) {
            histoire.executer(new PartitionActionSelectionNotesHampesDirections(getSelection(), hampeDirection));
            calculerModificationSelection();
        }
        repaint();
    }



    public void selectionNotesHampesDirectionsChanger() {
        if (isSelection()) {
            histoire.executer(new PartitionActionSelectionNotesHampesDirectionsChanger(getSelection()));
            calculerModificationSelection();
        }
        repaint();
    }


    



    void selectionNotesFigureNotesSet(Note.NoteFigure noteFigure) {
        if (isSelection()) {
            histoire.executer(new PartitionActionSelectionNotesFigures(getSelection(), noteFigure));
            calculerModificationSelection();
        }
        repaint();
    }

    public void calculerModificationSelection() {
        calculer(getSelection().getMomentDebut());
    }
    
    
    public void calculer(Moment moment)
    {
        getPartitionVue().miseEnPageCalculer(moment);
        mode.calculer();
        repaint();
    
    }

    public void setMode(PartitionPanelMode mode) {
        this.mode.whenQuit();
        this.mode = mode;
        repaint();
        update();
    }



    public void setCurseurSouris(Curseur curseurSouris) {
        this.curseurSouris = curseurSouris;

        if(mode instanceof PartitionPanelModeEcriture)
            ((PartitionPanelModeEcriture) mode).recalculerNoteApartirCurseurSouris();

        repaint();

    }

    PartitionPanelMode getMode() {
        return mode;
    }

    public void calculer(Moment min, Moment max) {
        getPartitionVue().miseEnPageCalculer(min, max);
        mode.calculer();
        repaint();
    }
    
    
    public void calculer() {
        getPartitionVue().miseEnPageCalculer();
        mode.calculer();
        repaint();
    }

    public void setAlterationCourante(Hauteur.Alteration alteration) {
        if(mode instanceof PartitionPanelModeEcriture)
        {
            ((PartitionPanelModeEcriture) mode).setAlterationCourante(alteration);
        }
    }







    /*
     * sélectionner la mesure sous le curseur
     */
    public void selectionnerMesure() {
        setSelection(getPartitionDonnees().getSelectionMesure(getCurseurSouris().getMoment()));
    }

    public void selectionPartieSousCurseurSouris() {
        setSelection(getPartitionDonnees().getSelectionToutPartie(curseurSouris.getPartie()));
        repaint();
    }

    public void mesureCorrigerSousCurseurSouris() {
        Moment moment = getCurseurSouris().getMoment();
        Moment debutMesure = getPartitionDonnees().getMesureMomentDebut(moment);
        Moment finMesure = getPartitionDonnees().getMomentMesureFin(moment);

        Duree dureeEscomptees = getPartitionDonnees().getMesureSignature(moment).getMesuresDuree();

        histoire.executer(new PartitionActionInsererTemps(finMesure, dureeEscomptees.getFinMoment(debutMesure)));
        calculer(debutMesure);
    }

    public void playFromCurseurSouris() {
        play(getCurseurSouris().getMoment());
    }

    public void tempsSupprimerSousCurseur(Curseur curseur) {
        if (getPartitionDonnees().isMomentPasDeNotes(curseur.getMoment())) {
        Moment moment = curseur.getMoment();
        histoire.executer(
                new PartitionActionInsererTemps(
                getPartitionDonnees().getMomentSuivant(moment),
                moment));

        getPartitionVue().miseEnPageCalculer(curseur.getMoment());
        repaint();
    }
    }

/**
 * TODO: à mettre en private
 */
    public void repaint()
    {
        update();
    }

    void afficherMomentEcran(Moment moment) {
        Rectangle systemeRectangle =
               getPartitionVue().getSysteme(moment).getRectangle();
 
        
        for(ControllerListener l : controllerListeners)
        {
            l.whenScroll(systemeRectangle);
        }
    }

    
    
    
    
    public void alterer(Hauteur.Alteration alteration) {
        if (isSelection()) //s'il y a une sélection, on l'altère
        {
            executer(
                    new PartitionActionSelectionAlterer(
                    getSelection(),
                    alteration));
            calculerModificationSelection();
            repaint();
        } else if (getCurseurSouris().isSurNote()) //si le curseur est sous une note, on l'altère
        {
            executer(new PartitionActionNoteAlterer(
                    getCurseurSouris().getNote(), alteration));
            getPartitionVue().miseEnPageCalculer(getCurseurSouris().getNote().getDebutMoment());
            repaint();
        } else {
            setAlterationCourante(alteration);
        }

    }

    
    
    
     public void update()
     {
         for(ControllerListener l : controllerListeners)
         {
             l.whenUpdate(this);
         }
     }
         
         
         
    public void fireCursorOverSomething() {
        for(ControllerListener l : controllerListeners)
         {
             l.whenCursorOverSomething();
         }
    }

    void fireCursorOverNothing() {
        for(ControllerListener l : controllerListeners)
         {
             l.whenCursorOverNothing();
         }
    }

    
    
    

    public void stop() {
        if(mode instanceof PartitionPanelModeLecture)
        {
//            if (machineEntreeMIDI != null) {
//                machineEntreeMIDI.arreter();
//            }

            if (partitionLecteur != null) {
                partitionLecteur.arreter();
            }

            //machineEntreeMIDI = null;
            partitionLecteur = null;


            setMode(new PartitionPanelModeEcriture(this));
            update();
        }
    }

    Panel getPanel() {
        return panel;
    }

    
    

    public void setMachineEntreeMIDI(MachineEntreeMIDI machineEntreeMIDI) {
        this.machineEntreeMIDI = machineEntreeMIDI;

    }

    public MachineEntree getMachineEntreeMIDI() {
        return machineEntreeMIDI;
    }
}
