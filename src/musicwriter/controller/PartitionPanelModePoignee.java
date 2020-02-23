/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import musicwriter.gui.Graphics;
import java.awt.event.MouseEvent;
import musicwriter.donnees.Selection;
import musicwriter.gui.Poignee;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModePoignee extends PartitionPanelModeSelection {

    private final Poignee poignee;

    public PartitionPanelModePoignee(Controller controller, Selection selection, Poignee poignee) {
        super(controller, selection);
        this.poignee = poignee;
        
    }


    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        poignee.mouseFinish(getPartitionVue().getCurseur(evt.getPoint()), getController().getHistoire());
        getController().calculer();
        modeChanger(new PartitionPanelModeSelection(getController(), getSelection()));
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
        getController().repaint();
        poignee.mouseDrag(getPartitionVue().getCurseur(evt.getPoint()),
                          getController());
    //    getController().modifierSourisCurseur(poignee.getCursor());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        getPartitionVue().afficherElementMusicalDehors(g, poignee.getElementMusicalResultat());
        
    }





}
