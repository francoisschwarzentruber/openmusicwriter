/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter;

import musicwriter.gui.Point;

/**
 *
 * @author Ancmin
 */
public class Geometrie {
    
    
        /**
     * 
     * @param y
     * @param A
     * @param B
     * @return vérifie si le nombre y appartient à [A, B]
     */
    static private boolean isArg1inArg2Arg3(double y, double A, double B) {
        return ((A <= y) && (y <= B)) || ((B <= y) && (y <= A));
    }
    
    
    /**
     * 
     * [p1, p2] = un segment vertical
     * 
     * @param a1
     * @param a2
     * @param p1
     * @param p2
     * @return vrai ssi le segment [a1, a2] coupe le segment vertical [p1, p2] (p1.x = p2.x)
     */
    static public boolean isSegmentCoupeSegmentVertical(Point a1, Point a2, Point p1, Point p2) {
        //segment [a1, a2] vertical
        if(a1.x == a2.x)
        {
            if(a1.x != p1.x)
                return false;
            else
                return isArg1inArg2Arg3(a1.y, p1.y, p2.y);
        }
        else
        {
            
            double xIntersection = p1.x;
            
            
            if(!isArg1inArg2Arg3(xIntersection, a1.x, a2.x))
                return false;
            
            double alpha = ((double) (a1.y - a2.y)) / ((double) (a1.x - a2.x));
            double beta = a1.y - alpha * a1.x;
            
            double yIntersection = xIntersection * alpha + beta;
            
            if(!isArg1inArg2Arg3(yIntersection, p1.y, p2.y))
                return false;
            
//            if(!isArg1inArg2Arg3(yIntersection, a1.y, a2.y))
//                return false;
            
            
            return true;
        }
     
        
    }
}
