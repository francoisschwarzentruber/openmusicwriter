/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import java.awt.Color;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.PartitionActionSelectionSupprimer;
import musicwriter.gui.Graphics;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeSelectionDeplacementOuCopie extends PartitionPanelModeSelection {

    private final NotesDeplacementSouris notesDeplacementSouris;

    public PartitionPanelModeSelectionDeplacementOuCopie(Controller controller, Curseur curseur, Selection selection) {
        super(controller, selection);
        
        ElementMusical el = selection.getElementMusicalProche(curseur);
        if(el instanceof Note)
        {
            curseur = new Curseur(curseur.getMoment(), ((Note) el).getHauteur(), curseur.getPortee());
        }
       //ICI IL FAUT TROUVER UN BON CURSEUR... pour pas avoir de bémol et double-bémol débiles selection.getC
//        getFenetrePrincipale().modeSelectionCopierPasPlus();
        notesDeplacementSouris = new NotesDeplacementSouris(controller, curseur, selection);
    }


    private void dessinerSelection(Graphics g) {
            if(notesDeplacementSouris.isEffectueraCopie())
                // dessine la sélection quand il y a des notes en déplacements et que
                //ces dernière sera en fait copiés
            {
                g.setColor(Color.BLACK);
                g.setPenWidth(1);
            }
            else // dessine la sélection quand il y a des notes en déplacements et que
                //ces dernière sera bien déplacer
            {
                g.setColor(new Color(240,240,240));
                g.setPenWidth(1);
            }
            
            getPartitionVue().afficherSelection(g, getSelection());

        
    }

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        super.mouseReleased(evt);
        
        if(isSelectionToDelete())
        {
            final Moment momentDebut = getSelection().getMomentDebut();
            getController().getHistoire().executer(new PartitionActionSelectionSupprimer(getSelection()));
            getController().calculer(momentDebut);
            getController().repaint();
            modeChanger(new PartitionPanelModeEcriture(getController()));
            
        }
        else
        {
            if(isClavierToucheSpeciale(evt))
                notesDeplacementSouris.effectuerCopie();
            else
                notesDeplacementSouris.effectuer();

//            getController().modifierSourisCurseurMainOuverte();
        }
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
        super.mouseDragged(evt);

        if(evt.getPoint().getX() < 0 || evt.getPoint().getX() > getPartitionVue().getSystemesLongueur()+16)
            //suppression par drag-and-drop
        {
            setSelectionToDelete(true);
//            getController().modifierSourisCurseurGomme();
        }
        else
        {
            setSelectionToDelete(false);
     //       getController().modifierSourisCurseurMainOuverte();
        
            notesDeplacementSouris.setReferenceCurseurFutur(
                    getPartitionVue()
                        .getCurseur(evt.getPoint(),
                                    getSelection().getDureeTotale(), null));
        }
        getController().repaint();
    }



private boolean selectionToDelete = false;

    public boolean isSelectionToDelete() {
        return selectionToDelete;
    }

    private void setSelectionToDelete(boolean selectionToDelete) {
        this.selectionToDelete = selectionToDelete;
    }


    
    
    @Override
    public void keyPressed(ControllerKeyEvent evt) {
        if(isClavierToucheSpeciale(evt))
         {
//             getFenetrePrincipale().modeSelectionPlus();
         
             if(notesDeplacementSouris != null)
             {
                  notesDeplacementSouris.setModeEffectuerCopie();
         //         getFenetrePrincipale().modeSelectionCopierPlus();
             }
          //   else
          //        getFenetrePrincipale().modeSelectionPlus();
        }

        if(evt.getKeyChar() == '-')
        {
            notesDeplacementSouris.setAlterationMoins();
            getController().repaint();
        }


        if(evt.getKeyChar() == '+')
        {
            notesDeplacementSouris.setAlterationPlus();
            getController().repaint();
        }


    }

    @Override
    public void keyReleased(ControllerKeyEvent evt) {
        if(!isClavierToucheSpeciale(evt))
         {
             notesDeplacementSouris.setModeEffectuerDeplacement();
//             getFenetrePrincipale().modeSelectionCopierPasPlus();



         }
    }






    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        dessinerSelection(g);
        
        if(!isSelectionToDelete())
            notesDeplacementSouris.afficher(g);
    }

    @Override
    public void paintComponentAvant(Graphics g) {
        getPartitionVue().afficherContexteFond(g, notesDeplacementSouris.getReferenceCurseurFutur());
        
        if(notesDeplacementSouris.isEffectueraCopie() || notesDeplacementSouris.isTrivial() )
        //on affiche le fond de la sélection etc.
        {
             super.paintComponentAvant(g);
        }
       
    }

    

     

    
}
