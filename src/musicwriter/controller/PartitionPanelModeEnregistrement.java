/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Hauteur.Alteration;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeEnregistrement extends PartitionPanelModeLecture {
    private final PartitionScribe partitionScribe;
    private HashMap<Integer, Hauteur> clavierPiano = new HashMap<Integer, Hauteur>();
    
    
    public PartitionPanelModeEnregistrement(Controller controller) {
        super(controller);
        affichageLectureBarre.setEnregistrementLectureBarre();

        this.partitionScribe = new PartitionScribe(controller, getPartitionLecteur());
        getController().getMachineEntreeMIDI().addMachineEntreeListener(this.partitionScribe);
        
        clavierPiano.put(KeyEvent.VK_C, new Hauteur(0, Alteration.NATUREL));
        clavierPiano.put(KeyEvent.VK_D, new Hauteur(1, Alteration.NATUREL));
    }

    @Override
    public void keyPressed(ControllerKeyEvent evt) {
        Hauteur hauteur = clavierPiano.get(evt.getKeyCode());
        
        if(hauteur != null)
        {
            partitionScribe.whenNoteEnfoncee(hauteur, 128, 0);
        }
    }

    @Override
    public void keyReleased(ControllerKeyEvent evt) {
        Hauteur hauteur = clavierPiano.get(evt.getKeyCode());
        
        if(hauteur != null)
        {
           partitionScribe.whenNoteRelachee(hauteur, 128, 0);
        }
    }

    @Override
    void whenQuit() {
        if(getController().getMachineEntreeMIDI() != null)
             getController().getMachineEntreeMIDI().deleteMachineEntreeListener(this.partitionScribe);
    }
    
    
    


    

}
