/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import musicwriter.controller.MachineSortie;
import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;

/**
 * Cette machine reçoit des "messages" traiterNoteEnfoncee et traiterNoteRelachee.
 * Puis génère une séquence MIDI (prête à être enregistré dans un fichier).
 * 
 * Utilisation : on crée une machine et la connecte avec un PartitionLecteurOneShot
 * puis on fait run sur le PartitionLecteurOneShot.
 * Puis on fait getSequence sur MachineSortieMIDISequence
 * 
 * @author Ancmin
 */
public class MachineSortieMIDISequence implements MachineSortie {

    /**
     * dit pour une partie dans quelle piste elle va s'écrire
     */
    private final HashMap<Partie, Track> partieToTrack = new HashMap<Partie, Track>();
    private final Sequence sequence;
    
    private final int resolution = 24;
    
    /**
     *
     * @param partitionDonnees
     * @return une séquence vide qui contient des pistes pour chaque partie.
     * Effet de bord : remplit partieToTrack
     */
    private Sequence createEmptySequence(PartitionDonnees partitionDonnees)
    {
        Sequence s;
        try {
            s = new Sequence(javax.sound.midi.Sequence.PPQ,resolution);
            
            for(Partie partie : partitionDonnees.getElementsMusicauxParties())
            {
                final Track track = s.createTrack();
                ShortMessage message = new ShortMessage();
                message.setMessage(ShortMessage.PROGRAM_CHANGE + partie.getNumero(), partie.getInstrument().getNumeroMIDI(), 0);
                track.add(new MidiEvent(message, 0));
                partieToTrack.put(partie, track);
            }
            
            return s;
            
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(MachineSortieMIDISequence.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public MachineSortieMIDISequence(PartitionDonnees partitionDonnees)
    {
        this.sequence = createEmptySequence(partitionDonnees);
        
    }
    
    
    
    private ShortMessage createShortMessage(int command, int numeroMIDI, int velocite)
    {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(command, numeroMIDI, velocite);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(MachineSortieMIDISequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }
    
    
    @Override
    public void traiterNoteEnfoncee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie) {
        final Track track = partieToTrack.get(partie);
        ShortMessage message = createShortMessage(ShortMessage.NOTE_ON + partie.getNumero(), hauteur.getNumeroMIDI(), velocite);
        track.add(new MidiEvent(message, tempsEnMilliSecondes/20));
    }

    @Override
    public void traiterNoteRelachee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie) {
        final Track track = partieToTrack.get(partie);
        ShortMessage message = createShortMessage(ShortMessage.NOTE_OFF + partie.getNumero(), hauteur.getNumeroMIDI(), velocite);
        track.add(new MidiEvent(message, tempsEnMilliSecondes/20));
    }

    @Override
    public void demarrer() {
        
    }

    @Override
    public void arreter() {

    }

    public Sequence getSequence() {
        return sequence;
    }
    
}
