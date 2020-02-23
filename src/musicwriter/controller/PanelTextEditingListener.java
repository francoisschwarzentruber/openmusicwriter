/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

/**
 * This listener listens to what happens with a "textbox" over the panel
 * @author Ancmin
 */
public interface PanelTextEditingListener {
    public void whenEditing(ControllerKeyEvent e, String newText, int cursorPosition);

}
