/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing;

import java.util.ArrayList;
import javax.sound.midi.*;
import musicwriter.controller.MachineEntree;
import musicwriter.controller.MachineEntreeListener;
import musicwriter.donnees.Hauteur;







/**
 *
 * @author proprietaire
 */
public class MachineEntreeMIDI implements MachineEntree {
    
    final MidiDevice machineEntreeMIDI;
    final private ArrayList<MachineEntreeListener> machineEntreeListener = new ArrayList<MachineEntreeListener>();
    
    
    @Override
    public void addMachineEntreeListener(MachineEntreeListener m)
    {
        machineEntreeListener.add(m);
    }

    @Override
    public void deleteMachineEntreeListener(MachineEntreeListener m) {
        machineEntreeListener.remove(m);
    }
    
    
    
    
    
    
    
    public class RecepteurMessagesEntreeMIDI
	implements	Receiver 
    {
        
        @Override
        public void send(MidiMessage message, long lTimeStamp)
	{

                lTimeStamp = lTimeStamp / 1000;
                
		if (message instanceof ShortMessage)
		{
			traiterShortMessage((ShortMessage) message, lTimeStamp);
		}
		else if (message instanceof SysexMessage)
		{
			traiterSysexMessage((SysexMessage) message, lTimeStamp);
		}
		else if (message instanceof MetaMessage)
		{
			traiterMetaMessage((MetaMessage) message, lTimeStamp);
		}
		else
		{
			System.out.println("unknown message type");
		}

	}

        @Override
        public void close() {
            
        }

        private void traiterMetaMessage(MetaMessage metaMessage, long lTimeStamp) {
           
        }

        
        private Hauteur hauteurNumeroMIDIToHauteur(int hauteurNumeroMIDI)
        {
            int octave = hauteurNumeroMIDI / 12 - 5;
            int notenumero = hauteurNumeroMIDI % 12;
            switch(notenumero)
            {
                case 0:
                    return new Hauteur(octave, Hauteur.NoteNom.DO, Hauteur.Alteration.NATUREL);
                    
                case 1:
                    return new Hauteur(octave, Hauteur.NoteNom.DO, Hauteur.Alteration.DIESE);
                    
                case 2:
                    return new Hauteur(octave, Hauteur.NoteNom.RE, Hauteur.Alteration.NATUREL);
                    
                case 3:
                    return new Hauteur(octave, Hauteur.NoteNom.RE, Hauteur.Alteration.DIESE);
                    
                case 4:
                    return new Hauteur(octave, Hauteur.NoteNom.MI, Hauteur.Alteration.NATUREL);
                    
                case 5:
                    return new Hauteur(octave, Hauteur.NoteNom.FA, Hauteur.Alteration.NATUREL);
                    
                case 6:
                    return new Hauteur(octave, Hauteur.NoteNom.FA, Hauteur.Alteration.DIESE);
                    
                case 7:
                    return new Hauteur(octave, Hauteur.NoteNom.SOL, Hauteur.Alteration.NATUREL);    
                    
                case 8:
                    return new Hauteur(octave, Hauteur.NoteNom.SOL, Hauteur.Alteration.DIESE);
                    
                case 9:
                    return new Hauteur(octave, Hauteur.NoteNom.LA, Hauteur.Alteration.NATUREL);
                    
                case 10:
                    return new Hauteur(octave, Hauteur.NoteNom.LA, Hauteur.Alteration.DIESE);
                    
                case 11:
                    return new Hauteur(octave, Hauteur.NoteNom.SI, Hauteur.Alteration.NATUREL);    
                    

                default:
                    System.out.println("Problème dans hauteurNumeroMIDIToHauteur");
                    return null;
            }
            
            
        }
        
        
        
        
        
        
        
        private void traiterNoteEnfoncee(int hauteurNumeroMIDI, int velocity, long tempsEnMilliSecondes) {
            for(MachineEntreeListener l : machineEntreeListener)
            {
                l.whenNoteEnfoncee(hauteurNumeroMIDIToHauteur(hauteurNumeroMIDI), velocity, tempsEnMilliSecondes);
            }
            
        }

        private void traiterNoteRelachee(int hauteurNumeroMIDI, int velocity, long tempsEnMilliSecondes) {
            for(MachineEntreeListener l : machineEntreeListener)
            {
                l.whenNoteRelachee(hauteurNumeroMIDIToHauteur(hauteurNumeroMIDI), velocity, tempsEnMilliSecondes);
            }
        }

        private void traiterShortMessage(ShortMessage shortMessage, long lTimeStamp) {
            switch (shortMessage.getCommand())
		{
                    case 0x80:
                        traiterNoteRelachee( shortMessage.getData1(), shortMessage.getData2(), lTimeStamp );
                        break;

                    case 0x90:
                        if(shortMessage.getData2() == 0)
                        {
                            traiterNoteRelachee( shortMessage.getData1(), shortMessage.getData2(), lTimeStamp );
                        }
                        else
                        {
                            traiterNoteEnfoncee( shortMessage.getData1(), shortMessage.getData2(), lTimeStamp );
                        }
                        

                        break;
                            
                    default:
                        //System.out.println("Message d'entrée ShortMessage MIDI non géré");
                }
        }

        private void traiterSysexMessage(SysexMessage sysexMessage, long lTimeStamp) {
            
        }
        
    }
    
    
    
    MachineEntreeMIDI(MidiDevice machineEntreeMIDI)
    {
        
        this.machineEntreeMIDI = machineEntreeMIDI;
        machineEntreeMIDIouvrir();
        machineEntreeMIDIconnecterARecepteur(machineEntreeMIDI);
        System.out.println("Device midi input constructed: his name is " + machineEntreeMIDI.getDeviceInfo().getName() + ", " + machineEntreeMIDI.getDeviceInfo().getDescription());
    }
    
    
    
    /*cette fonction me donne les informations du périphérique MIDI
     * dont le numéro est index*/
    private static MidiDevice.Info getMidiDeviceInfo(int index)
    {
            MidiDevice.Info[]	aInfos = MidiSystem.getMidiDeviceInfo();
            if ((index < 0) || (index >= aInfos.length)) {
                    return null;
            }
            return aInfos[index];
    }
    
    
    
    private void machineEntreeMIDIouvrir()
    {
        try
        {
            machineEntreeMIDI.open();
        }
        catch (MidiUnavailableException e)
        {
                System.out.println("Erreur à l'ouverture de la machine MIDI d'entrée");
        }
        
    }
    
    
    
    private void machineEntreeMIDIconnecterARecepteur(MidiDevice machineEntreeMIDI)
    {
        try
	{
             Transmitter t = machineEntreeMIDI.getTransmitter();
             Receiver recepteur = new RecepteurMessagesEntreeMIDI();
             t.setReceiver(recepteur);
             
        }
         catch (MidiUnavailableException e)
            {
                    System.out.println("Impossible de connecter ma machine MIDI à Musicwriter");
                    machineEntreeMIDI.close();
            }
    }
    
    
    @Override
    public void demarrer()
    {
        
    }
           
    
    @Override
    public void arreter()
    {
        machineEntreeMIDI.close();
    }
    
    

}
