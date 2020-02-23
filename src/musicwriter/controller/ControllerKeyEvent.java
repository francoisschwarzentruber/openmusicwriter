/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

/**
 *
 * @author Ancmin
 */
public class ControllerKeyEvent {

    private final char keyChar;
    private final int keyCode;
    private final boolean controlDown;
    private final boolean shiftDown;

    public ControllerKeyEvent(char keyChar, int keyCode, boolean controlDown, boolean shiftDown) {
        this.keyChar = keyChar;
        this.keyCode = keyCode;
        this.controlDown = controlDown;
        this.shiftDown = shiftDown;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isControlDown() {
        return controlDown;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }
    
    
    
    

    
}
