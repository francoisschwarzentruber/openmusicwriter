/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.mouserecognition;

/**
 * 
 * @author Ancmin
 */
public class ShapeAction {
    private final MouseShape shape;
    private final MouseRecognitionActionListener action;

    public ShapeAction(MouseShape shape, MouseRecognitionActionListener action)
    {
        this.shape = shape;
        this.action = action;
    }

    public MouseShape getShape() {
        return shape;
    }

    public MouseRecognitionActionListener getAction() {
        return action;
    }


    
}
