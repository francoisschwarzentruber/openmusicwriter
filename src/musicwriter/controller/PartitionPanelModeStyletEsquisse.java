/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import musicwriter.gui.Graphics;
import musicwriter.gui.Point;
import musicwriter.gui.StyloEsquisse;
import musicwriter.gui.partitionaffichage.AffichageAccord;

/**
 *
 * @author Ancmin
 */
abstract class PartitionPanelModeStyletEsquisse extends PartitionPanelMode {

    public PartitionPanelModeStyletEsquisse(Controller controller) {
        super(controller);
    }


    private StyloEsquisse styloEsquisse = null;

    
    protected StyloEsquisse getStyloEsquisse() {
        return styloEsquisse;
    }

    
    protected void styloEsquisseInit(Point p)
    {
        styloEsquisse = new StyloEsquisse(getPartitionVue());
        styloEsquisse.styloPointAjouter(p);
    }


    protected boolean isStyloEsquisse()
    {
        return styloEsquisse != null;
    }

    


    @Override
    public void mouseClicked(ControllerMouseEvent evt) {
    }

    @Override
    public void mousePressed(ControllerMouseEvent evt) {
        styloEsquisse = new StyloEsquisse(getPartitionVue());
        styloEsquisse.styloPointAjouter(evt.getPoint());
        
        AffichageAccord affichageAccordDebut = getPartitionVue().getAffichageAccordAvecHampeFin(evt.getPoint());
        if(affichageAccordDebut != null)
        {
                modeChanger(new PartitionPanelModeStyleTraitCroche(getController(),
                        affichageAccordDebut));
        }  
    }

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        styloEsquisse = null;
        
          
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
        if(styloEsquisse != null)
            {
                styloEsquisse.styloPointAjouter(evt.getPoint());

              //  if(styloEsquisse.isGribouilli())
//                    getController().modifierSourisCurseurGomme();
               // else
                {
     //               getController().modifierSourisCurseurCrayon();
     //               getFenetrePrincipale().modeStylet();
                }
                getController().repaint();
            }

    }

    @Override
    public void mouseMoved(ControllerMouseEvent evt) {
    }

    @Override
    public void keyPressed(ControllerKeyEvent evt) {
    }

    @Override
    public void keyReleased(ControllerKeyEvent evt) {
    }

    @Override
    public void keyTyped(ControllerKeyEvent evt) {
    }

    @Override
    public void paintComponent(Graphics g) {
        
        if(styloEsquisse != null)
            styloEsquisse.afficher(g);
    }




    
}
