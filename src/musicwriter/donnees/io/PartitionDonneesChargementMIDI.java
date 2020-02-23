/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import musicwriter.donnees.BarreDeMesure;
import musicwriter.donnees.Duree;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalClef;
import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Note;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Portee.Clef;
import musicwriter.donnees.Rational;
import musicwriter.donnees.Instrument;
import musicwriter.donnees.actions.PartitionActionSilencesCompleter;

/**
 *
 * @author Nadine Guiraud
 */
public class PartitionDonneesChargementMIDI implements PartitionDonneesChargement {

    @Override
    public PartitionDonnees getPartitionDonneesDuFichier(String nomFichier) throws IOException {
        final PartitionDonnees partitionDonnees = new PartitionDonnees();
        try {
            /*
             * DONE : traiter la durée de la note 
             * DONE : traiter l'ajout de la meilleure clef 
             * TODO : attention si changement d'instrument dans une même track : cela remplace l'instrument de toute la partie.
             * TODO : attention si plus d'instruments dans la patch list que de nombre de tracks ...
             * TODO : pour l'instant, les notes sont ajoutées dans une seule portee par partie ?? pas sur
             * TODO : pour l'instant, pas de voix
             * TODO : il manque les clefs
             * TODO : il manque la création de l'armure, etc.
             */

            //creer une partitionDonnees
            //recuperer le midi et ses caracteristiques
            Sequence sequence = MidiSystem.getSequence(new File(nomFichier));

            Track[] tracks = sequence.getTracks();
            printMessage("Nombre tracks:" + tracks.length);
            Patch[] patchs = sequence.getPatchList();
            printMessage("nbr patchs" + patchs.length);


            /*
             * Note : si la patch list contient plus d'instruments que le nombre
             * de tracks, ils ne seront pas pris en compte
             */

            // traiter chaque track en ajoutant la partie et l'instrument correspondant
            for (int i = 0; i < tracks.length; i++) {
                final Partie partie;
                // trouver dans la patch list l'instrument de la track :
                if (i < patchs.length) {
                    final int program = (sequence.getPatchList())[i].getProgram();
                    partie = this.getPartie(partitionDonnees, program);
                } else // pour la track, définir une nouvelle partie avec comme instrument le bon programme
                {
                    partie = this.addPartie(partitionDonnees);
                }
                final HashMap<Integer, MidiEvent> nombreDemiTonsToEventNoteOn = new HashMap<Integer, MidiEvent>();

                for (int j = 0; j < tracks[i].size(); j++) {
                    traiterEvent(partitionDonnees, partie, sequence, tracks[i].get(j), nombreDemiTonsToEventNoteOn);
                    // recuperer l'evenement et ses caracteristiques

                }
            }

        } catch (InvalidMidiDataException ex) {
            throw new IOException(ex.getMessage());
        }
        printMessage("tout fini !!");




        ajouterBarresDeMesures(partitionDonnees);
        clefsDeviner(partitionDonnees);
        
        new PartitionActionSilencesCompleter(partitionDonnees).executer(partitionDonnees);


        return partitionDonnees;
    }

    private void traiterEvent(PartitionDonnees partitionDonnees, Partie partie, Sequence sequence, MidiEvent event,
            HashMap<Integer, MidiEvent> nombreDemiTonsToEventNoteOn) {
        final MidiMessage message = event.getMessage();
        if (message instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) message;
            printMessage("Channel: " + shortMessage.getChannel() + " ");
            // traiter chaque element du midi et l'ajouter dans la partitionDonnees
            switch (shortMessage.getCommand()) {
                case ShortMessage.NOTE_OFF: {
                    // TODO : traiter la nuance int velocity = this.getVelocity(shortMessageOff);
                    this.creerNote(nombreDemiTonsToEventNoteOn, event, sequence, partitionDonnees, partie);
                    // Supprimer la note on de la liste
                    break;
                }
                case ShortMessage.NOTE_ON: {
                    if (shortMessage.getData2() == 0) {
                        this.creerNote(nombreDemiTonsToEventNoteOn, event, sequence, partitionDonnees, partie);
                    } else {
                        this.addNoteToNotesOn(event, nombreDemiTonsToEventNoteOn);
                    }
                    break;
                }
                case ShortMessage.PROGRAM_CHANGE: {
                    int program = this.getProgram(shortMessage);
                    printMessage("Program = " + program);
                    partie.setInstrument(new Instrument(program));
                }
                default:
                    printMessage("Command:" + shortMessage.getCommand());

            }
        } else if (message instanceof MetaMessage) {
            //message.getStatus()
            printMessage("Other message: " + message.getClass());// pas un short message
        } else {
            //MetaMessage m;
            //m.
            // les paroles ?
            printMessage("Other message: " + message.getClass());// pas un short message

        }
    }

    private void ajouterBarresDeMesures(PartitionDonnees partitionDonnees) {
        Moment moment = partitionDonnees.getMomentDebut();
        moment = Duree.getDureeRonde().getFinMoment(moment);
        for (; moment.isAvant(partitionDonnees.getFinMomentAvecDuree()); moment = Duree.getDureeRonde().getFinMoment(moment)) {
            partitionDonnees.elementMusicalAjouter(new BarreDeMesure(moment));
        }
    }

    private void addNoteToNotesOn(MidiEvent eventOn, HashMap<Integer, MidiEvent> nombreDemiTonsToEventNoteOn) {
        // toutes les durees sont pour l'instant des noires
        // pour l'instant, une seule portee
        // pour l'instant, pas de voix
        // il manque les clefs
        int nombreDemiTons = this.getNombreDemiTons((ShortMessage) eventOn.getMessage());
        int velocity = this.getVelocity((ShortMessage) eventOn.getMessage());
        nombreDemiTonsToEventNoteOn.put(nombreDemiTons, eventOn);
    }

    private Partie getPartie(PartitionDonnees partitionDonnees, int program) {
        Partie partie = new Partie(new Instrument(program));
        //partie.add(new Portee(partie));
        partitionDonnees.partieAjouter(partie);
        return partie;
    }
    
    
    
    static private void printMessage(String message)
    {
       // System.out.println(message);
    }

    /**
     * 
     * @param sequence
     * @param event
     * @return le moment dans la partition qui correspond au MidiEvent event issu de la séquence sequence
     * @throws InvalidMidiDataException 
     */
    private Moment getMoment(Sequence sequence, MidiEvent event) throws InvalidMidiDataException {
        float divisionType = sequence.getDivisionType();
        long tick = event.getTick();
        int resolution = sequence.getResolution(); //(nombre de ticks per frame ou bien de ticks per quarternote, selon q'on est en SMPTE ou PPQ
        printMessage("tick" + tick);
        if (divisionType == Sequence.PPQ) {
            printMessage("PPQ");
            return (new Moment(new Rational(tick, (long) resolution)));
        } else if (divisionType == Sequence.SMPTE_24) {
            printMessage("SMPTE_24");
            return (new Moment(new Rational(tick, (long) resolution)));
        } else if (divisionType == Sequence.SMPTE_25) {
            printMessage("SMPTE_25");
            return (new Moment(new Rational(tick, (long) resolution)));
        } else if (divisionType == Sequence.SMPTE_30DROP) {
            printMessage("SMPTE_30DROP");
            return (new Moment(new Rational(tick, (long) resolution)));
        } else if (divisionType == Sequence.SMPTE_30) {
            printMessage("SMPTE_30");
            return (new Moment(new Rational(tick, (long) resolution)));
        } else {
            throw new InvalidMidiDataException("Unsupported division type: " + divisionType);
        }
        //calculer le moment où la note est ajoutée dans la partition
        // supposons qu'un tick est une noire
    }

    private int getProgram(ShortMessage shortMessage) {// dans le cas d'une shortMessage de programChange
        return (shortMessage.getData1()); // data1 = numéro de l'instrument
    }

    /**
     * 
     * @param partitionDonnees
     * @return une partie ajouté à partitionDonnees (l'instrument est au pif)
     */
    private Partie addPartie(PartitionDonnees partitionDonnees) {
        Partie partie = new Partie(new Instrument(66));
        //partie.add(new Portee(partie));
        partitionDonnees.partieAjouter(partie);
        return partie;
    }

    private int getNombreDemiTons(ShortMessage shortMessage) {
        return (shortMessage.getData1()); // data1 = note = nombre de demi-tons
    }

    private int getVelocity(ShortMessage shortMessage) {
        return (shortMessage.getData2()); // data2 = velocity = nuance
    }

    private void creerNote(HashMap<Integer, MidiEvent> nombreDemiTonsToEventNoteOn, MidiEvent eventOff, Sequence sequence, PartitionDonnees partitionDonnees, Partie partie) {
        try {
            final int nombreDemiTons = getNombreDemiTons((ShortMessage) eventOff.getMessage());
            // si l'élément ON est la même note que l'élément OFF
            final MidiEvent eventOn = nombreDemiTonsToEventNoteOn.get(nombreDemiTons);
            // alors on récupère tickOn : moment d'enfoncement de la touche
            final long tickOn = eventOn.getTick();
            // on récupère tickOff : moment de levée de la touche
            final long tickOff = eventOff.getTick();
            // on calcule tickOff - tickIn = dureeEnTicks
            final long dureeEnTicks = tickOff - tickOn;
            // on récupère divisionType et resolution 
            final int resolution = sequence.getResolution();
            final Duree duree = new Duree(new Rational(dureeEnTicks, (long) resolution));
            Moment moment = this.getMoment(sequence, eventOn);
            partitionDonnees.elementMusicalAjouter(new Note(moment,
                    duree,
                    new Hauteur(nombreDemiTons - (5 * 12)), // nombre entre -(5*12) et 12
                    partie.getPorteePremiere()));
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(PartitionDonneesChargementMIDI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
    
    
    
    
    
    
    
    private Clef devinerClef(PartitionDonnees partitionDonnees, Portee portee)
    {
        ArrayList<Clef> clefs = new ArrayList<Clef>();
        clefs.add(Clef.ClefDeSol);
        clefs.add(Clef.ClefDeFa);
        
        double evaluationClefMeilleure = Integer.MAX_VALUE;
        Clef clefMeilleure = null;
        
        for(Clef clef : clefs)
        {
            int nombre = 0;
            int sommeDefaut = 0;
            
            for(ElementMusical el : partitionDonnees.getSelectionToutPortee(portee))
            {
                if(el instanceof Note)
                {
                     Note note = (Note) el;
                     
                     int absCoordonneeVerticale = Math.abs(clef.getCoordonneeVerticale(note.getHauteur()));
                     
                     if(absCoordonneeVerticale > 4)
                     {
                         sommeDefaut += absCoordonneeVerticale;
                     }
                     nombre++;
                }
            }
            
            if(nombre == 0)
                return Clef.ClefDeSol;
            
            double evaluationCourante = ((double) sommeDefaut) / ((double) nombre);
            if(evaluationClefMeilleure > evaluationCourante)
            {
                evaluationClefMeilleure = evaluationCourante;
                clefMeilleure = clef;
            }
        }
        
        return clefMeilleure;
    }

    private void clefsDeviner(PartitionDonnees partitionDonnees) {
        for(Portee p : partitionDonnees.getPortees())
        {
            Clef clef = devinerClef(partitionDonnees, p);
            partitionDonnees.elementMusicalAjouter(new ElementMusicalClef(partitionDonnees.getMomentDebut(), p, clef));
        }
    }
    
    
    
    
    
}
