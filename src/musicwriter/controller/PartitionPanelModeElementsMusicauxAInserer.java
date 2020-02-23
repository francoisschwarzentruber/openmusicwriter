/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import musicwriter.donnees.Curseur;
import musicwriter.donnees.Selection;
import musicwriter.gui.Graphics;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeElementsMusicauxAInserer extends PartitionPanelMode {

    private final NotesDeplacementSouris notesDeplacementSouris;
    private final Selection selection;

    public Selection getSelection() {
        return selection;
    }



    public PartitionPanelModeElementsMusicauxAInserer(Controller controller, Curseur curseur, Selection selection) {
        super(controller);
        this.selection = selection;
        notesDeplacementSouris = new NotesDeplacementSouris(controller, curseur, selection);
    }


    

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        if(isClavierToucheSpeciale(evt))
            notesDeplacementSouris.effectuerCopie();
        else
            notesDeplacementSouris.effectuer();

//        getController().modifierSourisCurseurMainOuverte();
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {

        notesDeplacementSouris.setReferenceCurseurFutur(
                getPartitionVue()
                    .getCurseur(evt.getPoint(),
                                getSelection().getDureeTotale(), null)
                                );
        getController().repaint();
    }





    @Override
    public void keyPressed(ControllerKeyEvent evt) {
        if(isClavierToucheSpeciale(evt))
         {
//             getFenetrePrincipale().modeSelectionPlus();
         
//             if(notesDeplacementSouris != null)
//             {
//                  notesDeplacementSouris.setModeEffectuerCopie();
//                  getFenetrePrincipale().modeSelectionCopierPlus();
//             }
//             else
//                  getFenetrePrincipale().modeSelectionPlus();
        }
    }

    @Override
    public void keyReleased(ControllerKeyEvent evt) {
        if(!isClavierToucheSpeciale(evt))
         {
             notesDeplacementSouris.setModeEffectuerDeplacement();
        //     getFenetrePrincipale().modeSelectionCopierPasPlus();



         }
    }






    @Override
    public void paintComponent(Graphics g) {
        notesDeplacementSouris.afficher(g);
    }



    @Override
    public void mouseClicked(ControllerMouseEvent evt) {

    }

    @Override
    public void mousePressed(ControllerMouseEvent evt) {
     
    }

    @Override
    public void mouseMoved(ControllerMouseEvent evt) {

    }

    @Override
    public void keyTyped(ControllerKeyEvent evt) {

    }

    @Override
    public void paintComponentAvant(Graphics g) {

    }

    

     

    
}
