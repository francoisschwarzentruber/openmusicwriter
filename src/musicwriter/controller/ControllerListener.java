/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public abstract class ControllerListener {
    public void whenUpdate(Controller controller) {}
    public void whenScroll(musicwriter.gui.Rectangle rectangle)  {}
    public void whenCursorOverSomething() {}
    public void whenCursorOverNothing() {}
    public void whenSelection(Selection selection) {}
    
}
