/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import musicwriter.gui.Graphics;
import musicwriter.gui.partitionaffichage.AffichageLectureBarre;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeLecture extends PartitionPanelMode {

    protected final AffichageLectureBarre affichageLectureBarre;



    public PartitionPanelModeLecture(Controller controller) {
        super(controller);
        affichageLectureBarre = new AffichageLectureBarre(getPartitionVue());
        
    }

    @Override
    public void mouseClicked(ControllerMouseEvent evt) {
    }

    @Override
    public void mousePressed(ControllerMouseEvent evt) {
        getPartitionLecteur().setMomentActuel(getPartitionVue().getCurseur(evt.getPoint()).getMoment());
        getController().repaint();

    }



    public PartitionLecteur getPartitionLecteur()
    {
        return getController().getPartitionLecteur();
    }

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
    }

    @Override
    public void mouseMoved(ControllerMouseEvent evt) {
//        getController().modifierSourisCurseurLecturePosition();
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
        affichageLectureBarre.setMoment(getController().getPartitionLecteur().momentActuelGet());
        affichageLectureBarre.draw(g);
        getController().afficherMomentEcran(getPartitionLecteur().momentActuelGet());
    }

    
    
    @Override
    public void paintComponentAvant(Graphics g) {
    }



}
