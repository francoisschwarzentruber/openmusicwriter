/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.mouserecognition;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ancmin
 */
public class ShapeActionsDictionnary {
    private Set<ShapeAction> shapeActions = new HashSet<ShapeAction>();
    private double seulSucces = 8.0;


    public void addShapeAction(ShapeAction shapeAction)
    {
        shapeActions.add(shapeAction);
    }


/**
     * 
     * @param shape
     * @return true iff something has been recognized
     */
    public boolean recognize(MouseShape shape)
    {
        double meilleureNote = 10000;
        ShapeAction meilleurShapeAction = null;
        for(ShapeAction shapeAction : shapeActions)
        {
            double note = ShapePatternComparator.compare(shapeAction.getShape(), shape);

            if(note < meilleureNote)
            {
                meilleurShapeAction = shapeAction;
                meilleureNote = note;
            }
        }


        if(meilleurShapeAction == null)
            return false;


        if(meilleureNote < seulSucces)
        {
            meilleurShapeAction.getAction().actionPerformed(shape);
            return true;
        }
        else
            return false;


    }


    public void setSeuilSucces(double seuilSucces)
    {
        this.seulSucces = seuilSucces;
    }


}
