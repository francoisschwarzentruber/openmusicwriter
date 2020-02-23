/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import java.io.File;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.guiswing.MachineSortieMIDISequence;
import musicwriter.controller.PartitionLecteur;
import musicwriter.controller.PartitionLecteurOneShot;

/**
 *
 * @author Ancmin
 */
public class PartitionSauvegardeMIDI implements PartitionSauvegarde {

    public void sauvegarder(PartitionDonnees partitionDonnees, String nomFichier)  throws java.io.IOException{
   
        MachineSortieMIDISequence machineSortie = new MachineSortieMIDISequence(partitionDonnees);
        PartitionLecteur lecteur = new PartitionLecteurOneShot(partitionDonnees,
                                                               partitionDonnees.getMomentDebut(),
                                                               machineSortie);
        
        //on n'appelle pas un autre thread!
        lecteur.run(); //pas de lecteur.start()!;
        
        Sequence s = machineSortie.getSequence();
        
        File f = new File(nomFichier);
	MidiSystem.write(s,1,f);
        
    }
    
}
