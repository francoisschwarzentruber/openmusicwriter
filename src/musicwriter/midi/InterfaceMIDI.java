/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.midi;

import java.util.ArrayList;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 *
 * @author proprietaire
 */
public class InterfaceMIDI {

    
    private static ArrayList<String> ArrayListMidiDeviceToArrayListString(ArrayList<MidiDevice> machines)
    {
        ArrayList<String> machinesnoms = new ArrayList<String>();
        
        for(MidiDevice m : machines)
        {
            machinesnoms.add(m.getDeviceInfo().getName());
        }
        
        return machinesnoms;
    }
    
    public static ArrayList<String> getMachinesEntreesMIDIDisponiblesNomsListe() {
        return ArrayListMidiDeviceToArrayListString(
                getMachinesEntreesMIDIDisponiblesListe());
    }
    
    private static ArrayList<MidiDevice> getMachinesMIDIDisponiblesListe()
	{
		ArrayList<MidiDevice> machines = new ArrayList<MidiDevice>();
        
                MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < aInfos.length; i++)
		{
			try
			{
				MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
                                machines.add(device);
			}
			catch (MidiUnavailableException e)
			{

			}
		}
                
                return machines;

	}
    
    
    
    private static ArrayList<MidiDevice> getMachinesEntreesMIDIDisponiblesListe()
    {
        ArrayList<MidiDevice> machines = new ArrayList<MidiDevice>();
        
        for(MidiDevice device : getMachinesMIDIDisponiblesListe())
        {
            if(device.getMaxTransmitters() != 0)
                machines.add(device);
                
        }
        
        return machines;
    }
    
    
    
    private static ArrayList<MidiDevice> getMachinesSortiesMIDIDisponiblesListe()
    {
        ArrayList<MidiDevice> machines = new ArrayList<MidiDevice>();
        
        for(MidiDevice device : getMachinesMIDIDisponiblesListe())
        {
            if(device.getMaxReceivers() != 0)
                machines.add(device);
                
        }
        
        return machines;
    }


    
    
    public static ArrayList<String> getMachinesSortiesMIDIDisponiblesNomsListe()
    {       
        return ArrayListMidiDeviceToArrayListString
                (getMachinesSortiesMIDIDisponiblesListe());
    }
    
    

    
    public static MidiDevice getOutputMIDIDeviceAvecNumero(int i)
    {
        return getMachinesSortiesMIDIDisponiblesListe().get(i);
    }
    
    public static MidiDevice getInputMIDIDeviceAvecNumero(int i)
    {
        return getMachinesEntreesMIDIDisponiblesListe().get(i);
    }
    


    
    
    public static boolean isMIDIDeviceSortieDisponible()
    {
        return (getMachinesSortiesMIDIDisponiblesListe().size() > 0);
    }
    
    
    
    public static boolean isMIDIDeviceEntreeDisponible()
    {
        return (getMachinesEntreesMIDIDisponiblesListe().size() > 0);
    }

    public static int machineSortieMIDIStandardNumeroGet() {
        ArrayList<String> liste = getMachinesSortiesMIDIDisponiblesNomsListe();

        for(int i = 0; i < liste.size(); i++)
        {
            if(liste.get(i).contains("Java"))
            {
                return i;
            }

            if(liste.get(i).contains("java"))
            {
                return i;
            }
        }

        return 0;
    }

    
    
    
    public static int machineEntreeMIDIStandardNumeroGet() {
        return 0;
    }
    
    
}
