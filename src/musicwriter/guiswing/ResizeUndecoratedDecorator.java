/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import javax.swing.JDialog;

/**
 *
 * @author Ancmin
 */
/** *********************************************************************************************
* RESIZE UNDECORATED WINDOW CLASS
*
* @author Stanislav Zorjan
* @copyright (c)Copyright 2007 - Stanislav Zorjan newcomponents@neobee.net
*
* As the name of the class says, this class resizes undecorated or decorated window (JFrame)
* Create new instance of the class and than call "onPress" method with "mousePressed" event
* and then "resizeWindowWidth" or "resizeWindowHeight" with "mouseDragged" event.
*/
/***********************************************************************************************/

 
public class ResizeUndecoratedDecorator {
     
    private int x1;
    private int x2;
    private int y1;
    private int y2;
     
    private int positionx;
    private int positiony;
     
    private JDialog frame;
     
    ResizeUndecoratedDecorator(JDialog frame){
        this.frame = frame;
    }
     
     
    public void resizeWindowWidth(java.awt.event.MouseEvent evt){
         
        this.positionx = evt.getXOnScreen();
     
            if(this.positionx > this.x1){
                this.x2 = this.positionx - this.x1;
                this.frame.setSize(this.frame.getSize().width + this.x2, this.frame.getSize().height);
            }else if(this.positionx < this.x1){
                this.x2 =  this.x1- this.positionx;
                this.frame.setSize(this.frame.getSize().width - this.x2, this.frame.getSize().height);
            }
             
            this.x1 = this.positionx;
    }
     
     
    public void resizeWindowHeight(java.awt.event.MouseEvent evt){
         
        this.positiony = evt.getYOnScreen();
     
            if(this.positiony > this.y1){
                this.y2 = this.positiony - this.y1;
                this.frame.setSize(this.frame.getSize().width, this.frame.getSize().height + this.y2);
            }else if(this.positiony < this.y1){
                this.y2 =  this.y1 - this.positiony;
                this.frame.setSize(this.frame.getSize().width, this.frame.getSize().height - this.y2);
            }
             
            this.y1 = this.positiony;
    }
     
     
    public void onPress(java.awt.event.MouseEvent evt){
        this.x1 = evt.getXOnScreen();
        this.y1 = evt.getYOnScreen();
    }
}
