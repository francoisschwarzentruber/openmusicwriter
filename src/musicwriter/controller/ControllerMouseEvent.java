/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import musicwriter.gui.Point;

/**
 *
 * @author Ancmin
 */
public class ControllerMouseEvent {
    private final float x;
    private final float y;
    private final boolean controlDown;
    private final boolean shiftDown;
    private final boolean middleButton;
    private final int clickCount;
    
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public Point getPoint()
    {
        return new Point((int) x, (int) y);
    }

    public ControllerMouseEvent(float x, float y, boolean controlDown, boolean shiftDown, boolean middleButton, int clickCount) {
        this.x = x;
        this.y = y;
        this.controlDown = controlDown;
        this.shiftDown = shiftDown;
        this.middleButton = middleButton;
        this.clickCount = clickCount;
    }


    public boolean isControlDown() {
        return controlDown;
    }

    public boolean isMiddleButton() {
        return middleButton;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public int getClickCount() {
        return clickCount;
    }
    
    


 
    
    
}
