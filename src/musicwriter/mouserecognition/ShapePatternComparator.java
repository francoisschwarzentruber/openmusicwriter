/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.mouserecognition;

/**
 *
 * @author Ancmin
 */
public class ShapePatternComparator {

    static public double compare(MouseShape s1, MouseShape s2)
    {
        double note = 0;
        final int nbstep = 12;
        for(int j = 0;  j < nbstep; j++)
        {
            note += Math.abs(s1.getAngle((double) j/nbstep) - s2.getAngle((double) j/nbstep));
        }
        return note;
    }

}
