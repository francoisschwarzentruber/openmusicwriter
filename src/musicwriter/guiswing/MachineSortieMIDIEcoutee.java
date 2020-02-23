/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import musicwriter.Erreur;
import musicwriter.controller.MachineSortie;
import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Instrument;
import musicwriter.donnees.Partie;

/**
 * Cette machine reçoit des "messages" traiterNoteEnfoncee et traiterNoteRelachee.
 * Puis génère du son via la carte son.
 * Se couple avec PartitionLecteurTempsReel
 * @author proprietaire
 */
public class MachineSortieMIDIEcoutee  implements MachineSortie {


    final private HashMap<Instrument, Byte> instrumentToCanal = new HashMap<Instrument, Byte>();
    private Receiver receiver = null;
    final private MidiDevice inputDevice;
    
    MachineSortieMIDIEcoutee(MidiDevice machineSortieMIDI)
    {
        inputDevice = machineSortieMIDI;
        
    }
    

    /**
     * 
     * @return the name of device. Example : "Gervill", "Java Sound Synthetiser"
     */
    public String getName()
    {
        return inputDevice.getDeviceInfo().getName();
    }
    
    
    
    private void receiverSend(ShortMessage message, long timeStamp)
    {
        if(receiver == null)
        {
             System.out.println("erreur midi, pas de receiver, j'envoie rien!");
        }
        else if(inputDevice.isOpen())
             receiver.send(message, timeStamp);
        else
            System.out.println("erreur midi, inputDevice fermé !");
    }


    private void changerInstrument(int canalMIDI, int instrumentNumero)
    {
        demarrer();
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.PROGRAM_CHANGE + canalMIDI, instrumentNumero, 0);
            receiverSend(message, -1);
        } catch (InvalidMidiDataException ex) {
            System.out.println("erreur midi, changement d'instrument, canal " + canalMIDI);
            Logger.getLogger(MachineSortieMIDIEcoutee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    @Override
    public void traiterNoteEnfoncee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie) {
        demarrer();
        ShortMessage message = new ShortMessage();
        try {
            tempsEnMilliSecondes = 0; //crucial pour qu'on entende le son TOUT DE SUITE
            message.setMessage(0x90 + getNumeroCanalMIDIInstrument(partie.getInstrument()), hauteur.getNumeroMIDI(), velocite);
            receiverSend(message, tempsEnMilliSecondes);
        } catch (InvalidMidiDataException ex) {
            Erreur.message("Désolé, je n'arrive pas à jouer la note... trop haute ou trop basse pour la machine MIDI...");
        }
          catch (java.lang.IllegalStateException ex) {
              Erreur.message("Désolé, je n'arrive pas à jouer la note... j'ai des problèmes diplomatiques avec la machine MIDI : en fait la machine MIDI est apparemment pas ouverte... bon... ben j'essaie de l'ouvrir...");
        }
    }    
        
    @Override
    public void traiterNoteRelachee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(0x80 + getNumeroCanalMIDIInstrument(partie.getInstrument()), hauteur.getNumeroMIDI(), 0);
            tempsEnMilliSecondes = 0;
            receiverSend(message, tempsEnMilliSecondes);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(MachineSortieMIDIEcoutee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    
    
    @Override
    public void demarrer()
    {
        if(!isDemarre())
            try {
                inputDevice.open();
                receiver = inputDevice.getReceiver();
                if(receiver == null)
                {
                    System.out.println("pas de receiver au démarrage de la machine MIDI!!");
                }
                instrumentToCanal.clear();
            } catch (MidiUnavailableException ex) {
                Erreur.message("Désolé, je n'arrive pas à ouvrir la machine MIDI.");
            }
    }
        
        
    @Override
    public void arreter()
    {
        inputDevice.close();
    }


    public boolean isDemarre()
    {
        return inputDevice.isOpen();
    }

    private byte getNumeroCanalMIDIInstrument(Instrument instrument) {
        if(instrumentToCanal.containsKey(instrument))
            return instrumentToCanal.get(instrument);
        else
        {
            byte canalMIDI = (byte) instrumentToCanal.size();
            if(canalMIDI >= 9)
                canalMIDI++;
            instrumentToCanal.put(instrument, canalMIDI);
            changerInstrument(canalMIDI, instrument.getNumeroMIDI());
            return canalMIDI;
        }
    }


}
