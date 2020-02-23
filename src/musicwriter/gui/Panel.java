/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

import java.awt.Font;
import java.awt.Rectangle;
import musicwriter.controller.PanelTextEditingListener;

/**
 * this is an abstraction of the panel which will contain the score.
 * (as Graphics is an abstraction of the canvas)
 * 
 * All coordinates are logical
 * @author Ancmin
 */
public interface Panel {

    /**
     * if you want to edit a text that is located at the rectangle (coordinates are logical)
     * the initial text is texte
     * the font is font
     * and the listener which listens to what about with the textbox is panelTextEditingListener
     *
     * @param rectangle
     * @param texte
     * @param font
     * @param panelTextEditingListener
     */
    public void textEditingAsk(musicwriter.gui.Rectangle rectangle, String texte, Font font, PanelTextEditingListener panelTextEditingListener);
    public void textEditingStop();
}
