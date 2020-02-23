/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;


import java.util.*;
import musicwriter.Erreur;
import musicwriter.donnees.ElementMusicalNuance.Nuance;
import musicwriter.donnees.*;


/**
 *
 * @author François Schwarzentruber
 * 
 * PartitionLecteur s'occupe de lire la partition passée en entrée
 * et d'appeler les méthodes traiterNoteEnfoncee et 
 * traiterNoteRelachee d'une machine machineSortie.
 * 
 * Cette classe est abstraite car elle s'abstrait de la manière dont se déroule le temps :
 * - temps réel (pour lire une partition)
 * - ou aussi vite que l'ordinateur peut l'exécuter (pratique pour créer un fichier midi par exemple)
 * Par exemple, PartitionLecteur est le pont entre la partition et une machine
 * de sortie qui joue les notes avec l'interface MIDI.
 * 
 * On acroche une machine de sortie avec le constructeur.
 */
abstract public class PartitionLecteur extends Thread {
    static final int precisionNbmilliseconds = 20;
    
    
    private final ArrayList<PartitionLecteurListener> partitionLecteurListeners =
            new ArrayList<PartitionLecteurListener>();
    private final PartitionDonnees partitionALire;
    private final MachineSortie machineSortie;
    private boolean isArretDemande = false;
    private boolean isEnPause = false;
    private Moment momentActuelBut = null;
    private Moment momentActuelSuivi = null;
    private double vitesse = 1.0;

    private final ArrayList<Note> notesANePasRejouer = new ArrayList<Note>();
    private final Map<Note, Interpretation> notesInterpretations = new HashMap<Note, Interpretation>();
    private boolean arretFinPartition = true;
    private int velociteCourante = 64;



    interface Interpretation {
        /**
         * s'occupe de jouer la note (avec son interprétation, trille, mordant, piqué)
         * On donne le moment actuel et ça fait la chose qui faut à ce moment là
         * @param momentActuel
         */
        public void interpreter(Moment momentActuel);

        /**
         * arrête complétement l'interprétation de la note
         */
        public void arreter();
    }



    abstract class InterpretationNote implements Interpretation {
           private Set<Note> notes = new HashSet<Note>();

           protected void noteRelachee(Note note)
           {
               fireNoteRelachee(note);
               notes.remove(note);
           }


           protected void noteEnfoncee(Note note)
           {
               if(!notes.contains(note))
               {
                   fireNoteEnfoncee(note);
                   notes.add(note);
               }
           }
        
    }


/**
 * gère une interprétation où il n'y a qu'une noteJoueeEnCeMoment jouée à la fois
 * Cette classe fournit la fonction noteEnfoncee
 */
    abstract class InterpretationUneNoteALaFois implements Interpretation {
           private Note noteJoueeEnCeMoment = null;

/**
 * débute le son de la note noteAEnfoncee. (s'il y a une note jouée, ça l'éteint
 * au préalable)
 * @param noteAEnfoncee
 */
           protected void noteEnfoncee(Note noteAEnfoncee)
           {
               if(noteJoueeEnCeMoment == noteAEnfoncee)
                   return;
               
               if(noteJoueeEnCeMoment != null)
                   fireNoteRelachee(noteJoueeEnCeMoment);

               noteJoueeEnCeMoment = noteAEnfoncee;
               fireNoteEnfoncee(noteJoueeEnCeMoment);

           }


           
        @Override
        public void arreter() {
            if(noteJoueeEnCeMoment != null)
                fireNoteRelachee(noteJoueeEnCeMoment);
        }





    }


    class InterpretationNotePiquee extends InterpretationNote {
        private final Note note;

        InterpretationNotePiquee(Note note)
        {
            this.note = note;
        }

        @Override
        public void interpreter(Moment momentActuel) {
            if(note.getDuree().multiplier(0.2).getFinMoment(note.getDebutMoment())
                    .isAvant(momentActuel))
                  noteRelachee(note);
        }


        @Override
        public void arreter() {
            noteRelachee(note);
        }
        
    }
    

    class InterpretationNoteMordent extends InterpretationUneNoteALaFois {
        private final Note note;
        private final Note noteEtrangere;

        InterpretationNoteMordent(Note note)
        {
            this.note = note;
            this.noteEtrangere = new Note(note.getDebutMoment(),
                    note.getDuree(), Intervalle.getIntervalleSecondeMajeure().getHauteur2(note.getHauteur()), note.getPortee());
        }

        @Override
        public void interpreter(Moment momentActuel) {
            double r = (new Duree(note.getDebutMoment(), momentActuel)).getRational().getRealNumber();
            int i = (int) (r * 10);

            if(i < 5)
            if(i % 2 == 0)
            {
                noteEnfoncee(note);
            }
            else
            {
                noteEnfoncee(noteEtrangere);
                
            }
        }




    }

class InterpretationNoteTrille extends InterpretationUneNoteALaFois {
        private final Note note;
        private final Note noteEtrangere;

        InterpretationNoteTrille(Note note)
        {
            this.note = note;

            final Hauteur hauteurNoteDessus = Intervalle.getIntervalleSecondeMajeure().getHauteur2(note.getHauteur());
            hauteurNoteDessus.setAlteration(getPartitionDonnees().getTonalite(note.getDebutMoment()).getAlteration(hauteurNoteDessus));

            /**
             * hauteurNoteDessus est la noteJoueeEnCeMoment au dessus (et son altération est
             * réglé à partir de la tonalité courante)
             */
            this.noteEtrangere = new Note(note.getDebutMoment(),
                    note.getDuree(), hauteurNoteDessus, note.getPortee());
        }

        
        @Override
        public void interpreter(Moment momentActuel) {
            double r = (new Duree(note.getDebutMoment(), momentActuel)).getRational().getRealNumber();
            int i = (int) (r * 10);

            if(i % 2 == 0)
            {
                noteEnfoncee(note);
            }
            else
            {
                noteEnfoncee(noteEtrangere);

            }
        }




    }

    class InterpretationNoteTurn extends InterpretationUneNoteALaFois {
        private final Note note;
        private final Note noteEtrangere1;
        private final Note noteEtrangere2;
        private int etat = 0;

        InterpretationNoteTurn(Note note)
        {
            this.note = note;
            this.noteEtrangere1 = new Note(note.getDebutMoment(),
                    note.getDuree(), Intervalle.getIntervalleSecondeMajeure().getHauteur2(note.getHauteur()), note.getPortee());
            this.noteEtrangere2 = new Note(note.getDebutMoment(),
                    note.getDuree(), Intervalle.getIntervalleSecondeMajeure().getHauteur1(note.getHauteur()), note.getPortee());

        }

        @Override
        public void interpreter(Moment momentActuel) {
            double r = (new Duree(note.getDebutMoment(), momentActuel)).getRational().getRealNumber();
            int i = (int) (r * 10);


            switch(etat)
            {
                    case 0:
                        noteEnfoncee(note);
                        break;
                    case 1:
                        noteEnfoncee(noteEtrangere1);
                        break;
                    case 2:
                        noteEnfoncee(note);
                        break;

                    case 3:
                        noteEnfoncee(noteEtrangere2);
                        break;

                        default:noteEnfoncee(note);
            }

           etat++;
        }



    }
    


    
    
    public void addPartitionLecteurListener(PartitionLecteurListener partitionLecteurListener)
    {
        partitionLecteurListeners.add(partitionLecteurListener);
    }
    
    PartitionLecteur(
                     PartitionDonnees partitionALire,
                     Moment momentDebut,
                     MachineSortie machineSortie)
    {
        this.partitionALire = partitionALire;
        this.machineSortie = machineSortie;
        this.isArretDemande = false;
        this.momentActuelSuivi = momentDebut;
        this.momentActuelBut = momentDebut;

    }


    /**
     * mettre en pause la lecture
     */
    public void pause() {
        isEnPause = true;
    }


    /**
     * reprendre la lecture qui a été mise en pause
     */
    public void reprendre() {
        isEnPause = false;
    }
    
    
    
    private void listenerRefresh()
    {
        for(PartitionLecteurListener l : partitionLecteurListeners)
        {
            l.refresh();
        }


    }
    
    
    abstract protected long getTime();
    
    
    abstract protected void timeSleep(int nbMillisecondsDuration);
    
    
    
    private void attendre(Moment momentdebut, Duree duree) {
        int nbNoiresParMinute = getPartitionDonnees().getNbNoiresParMinute(momentdebut);
        long nbmillisecondes_a_attendre = Math.round(duree.getNbMilliSecondes(nbNoiresParMinute));
        long tare = getTime();

        listenerRefresh();
      //  partitionPanel.setPartitionLecteur(this);
        while(((getTime() - tare) * vitesse < nbmillisecondes_a_attendre) && !isArretDemande)
        {
            Duree dureeactuelsuivi = new Duree((long) ((getTime() - tare) * vitesse), nbNoiresParMinute);
            momentActuelSuivi = new Moment(momentdebut.getRational().plus(dureeactuelsuivi.getRational()));
            
            timeSleep(precisionNbmilliseconds);

            for(Interpretation i : notesInterpretations.values())
            {
                i.interpreter(momentActuelSuivi);
            }
            

            listenerRefresh();
             

        }
    }
    
    
    
    private void fireNoteEnfoncee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie)
    {
        machineSortie.traiterNoteEnfoncee(hauteur, velocite, tempsEnMilliSecondes, partie);
    }
    
    private void fireNoteEnfoncee(Note note)
    {
        machineSortie.traiterNoteEnfoncee(note.getHauteur(), velociteCourante, getTime(),
                        note.getPartie());
    }
    
    private void fireNoteRelachee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie)
    {
        machineSortie.traiterNoteRelachee(hauteur, velocite, tempsEnMilliSecondes, partie);
    }
    
    
    private void fireNoteRelachee(Note note)
    {
        machineSortie.traiterNoteRelachee(note.getHauteur(), velociteCourante, getTime(),
                        note.getPartie());
    }
    
    
    
    public void setMomentActuel(Moment momentactuel)
    {
        momentRelacherNotes(this.momentActuelBut);
        this.momentActuelBut = momentactuel;
        this.momentActuelSuivi = momentactuel;
    }
    
    
    public Moment momentActuelGet()
    {
        return momentActuelSuivi;
    }
    
    
    private void momentRelacherNotes(Moment momentactuel)
    {
        Set<Note> notesQuiSontRelachees = partitionALire.getMomentNotesSurLeFeu(momentactuel).getNotesQuiSontRelachees();
        notesRelacher(notesQuiSontRelachees);

        
    }
    
    private void traiterNotes(Moment momentactuel) {
        Set<Note> notesAJouer = partitionALire.getMomentNotesSurLeFeu(momentactuel).getNotesAJouer();
        momentRelacherNotes(momentactuel);
        notesEnfoncer(notesAJouer);

        
    }
    
    
    /**
     * ceci est le programme pour lire une partition
     */
    @Override
    public void run()
    {
        machineSortie.demarrer();
        
        
        Moment momentprecedent = momentActuelBut;
        
        for(;
            (momentActuelBut != null) && (!isArretDemande);
            momentActuelBut = partitionALire.getMomentSuivant(momentActuelBut))
        {

            attendre(momentprecedent,
                     new Duree(momentprecedent,
                               momentActuelBut)
                     );
            
            if(isArretDemande)
                break;
            
            
            while(isEnPause) {}

            traiterNuances(momentActuelBut);
            traiterNotes(momentActuelBut);

            
            momentprecedent = momentActuelBut; 
        }


        if(!arretFinPartition)
        {
            attendre(momentprecedent,
                     new Duree(new Rational(1000000, 1)));
        }

        
        
        arreter();
        
        listenerStops();
        
    }

    private void notesEnfoncer(Set<Note> notesAJouer) {
        for(Note noteAJouer : notesAJouer)
        {
            if(!isNoteQuiSuitUneLieeALaSuivante(noteAJouer))
                 fireNoteEnfoncee(noteAJouer.getHauteurEntendue(),
                             velociteCourante,
                             getTime(),
                             noteAJouer.getPortee().getPartie());
                             //noteAJouer.getDebutMoment().getNbMillisecondesDepuisDebut());

            if(noteAJouer.isStaccato())
            {
                notesInterpretations.put(noteAJouer, new InterpretationNotePiquee(noteAJouer));
            }
            else
            if(noteAJouer.isOrnement("mordent"))
            {
                notesInterpretations.put(noteAJouer, new InterpretationNoteMordent(noteAJouer));
            }
            else
            if(noteAJouer.isOrnement("inverted-turn"))
            {
                notesInterpretations.put(noteAJouer, new InterpretationNoteTurn(noteAJouer));
            }
            else
            if(noteAJouer.isOrnement("trill-mark"))
            {
                notesInterpretations.put(noteAJouer, new InterpretationNoteTrille(noteAJouer));
            }



        }
    }

    private void notesRelacher(Set<Note> notesQuiSontRelachees) {
        for(Note noteQuiEstRelachee : notesQuiSontRelachees)
        {
            if(!noteQuiEstRelachee.isLieeALaSuivante())
            {
                 fireNoteRelachee(noteQuiEstRelachee.getHauteurEntendue(),
                             velociteCourante,
                             getTime(),
                             noteQuiEstRelachee.getPortee().getPartie());
                             //noteQuiEstRelachee.getDebutMoment().getNbMillisecondesDepuisDebut());
            }
            else
            {
                notesANePasRejouer.add(noteQuiEstRelachee);
            }

            if(notesInterpretations.containsKey(noteQuiEstRelachee))
            {
                notesInterpretations.get(noteQuiEstRelachee).arreter();
                notesInterpretations.remove(noteQuiEstRelachee);
            }
        }
    }

    
    
    private void listenerStops()
    {
        for(PartitionLecteurListener l : partitionLecteurListeners)
        {
            l.whenStops();
        }
    }
    
    
    
    
    public void arreter()
    {
        
        
        
        if(isArretDemande)
            return;

        isArretDemande = true;
        
        listenerStops();
        
        machineSortie.arreter();
        
        
        
       
        notesANePasRejouer.clear();
        
        isEnPause = false;

    }



    public void setPasArretFinPartition()
    {
        arretFinPartition = false;
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
    }

    private PartitionDonnees getPartitionDonnees() {
        return partitionALire;
    }

    private boolean isNoteQuiSuitUneLieeALaSuivante(Note noteAJouer) {
        for(Note noteANePasReJouer : notesANePasRejouer)
        {
            if(noteANePasReJouer.getVoix().equals(noteAJouer.getVoix()) &
               noteANePasReJouer.getHauteur().equals(noteAJouer.getHauteur())  )
            {
                notesANePasRejouer.remove(noteANePasReJouer);
                return true;
            }
        }
        return false;
    }



    private int nuanceToVelociteMIDI(Nuance nuance)
    {
        switch(nuance)
            {
            case PPP: return 8;
            case PP: return 16;
            case P: return 32;
            case MP: return 48;
            case MF:  return 64;
            case F: return 96;
            case FF: return 112;
            case FFF: return 127;
            default:
                Erreur.message("nuanceToVelociteMIDI: nuance non supporté");
                return 64;

            }
    }

    private void traiterNuances(Moment momentActuelBut) {
        ElementMusicalNuance nuance = (ElementMusicalNuance) getPartitionDonnees().getElementMusical(momentActuelBut, ElementMusicalNuance.class);

        if(nuance != null)
        {
            velociteCourante = nuanceToVelociteMIDI(nuance.getNuance());
        }
    }
    
}
