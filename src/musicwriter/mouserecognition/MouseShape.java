/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.mouserecognition;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import musicwriter.gui.Graphics;
import musicwriter.gui.Point;

/**
 * This class represents a shape drawn by the mouse.
 * @author Ancmin
 */
public class MouseShape {

    private final ArrayList<Point> points;

    public ArrayList<Point> getPoints() {
        return points;
    }


    /**
     * 
     * @return the length of the shape (the shape is actually a curve)
     */
    public double getLength()
    {
        double length = 0;

        for(int i = 0; i < points.size()-1; i++)
        {
            length += points.get(i).distance(points.get(i+1));
        }

        return length;
    }



/**
     * create an empty shape
     */
    public MouseShape()
    {
        points = new ArrayList<Point>();
    }


    /**
     * create a shape whose points are given in argument
     * @param points 
     */
    public MouseShape(ArrayList<Point> points)
    {
        this.points = points;
    }


/**
     * add a point to the shape
     * 
     * Example : 
     * addPoint("12 3") will add the point new Point(12, 3)
     * @param XspaceY 
     */
    final void addPoint(String XspaceY)
    {
            final int i = XspaceY.indexOf(" ");

            final int x = Integer.parseInt(XspaceY.substring(0, i));
            final int y = Integer.parseInt(XspaceY.substring(i+1));

            addPoint(new Point(x, y));
    }

    
/**
     * reads a shape from a BufferedReader (tthat can come from a file)
     * @param bufferedReader
     * @throws IOException 
     */
    public MouseShape(BufferedReader bufferedReader) throws IOException
    {
        points = new ArrayList<Point>();

        while(bufferedReader.ready())
        {
            addPoint(bufferedReader.readLine());
        }

        bufferedReader.close();
    }


/**
 * retourne le point comme si un bolide parcourait la courbe de manière uniforme
 * et t = le temps entre 0 (début) et 1 (fin)
 * @param t
 */
    public Point getPoint(double t)
    {
        final double lengthtotal = getLength();
        final double d = lengthtotal * t;
        double length = 0;
        double lengthavant = 0;
        for(int i = 0; i < points.size()-1; i++)
        {
            if((lengthavant <= d) & (d < length))
                return points.get(i);
            
            length += points.get(i).distance(points.get(i+1));
            lengthavant = 0;
        }

        return points.get(points.size() - 1);
    }

    
    /**
     * 
     * @param t
     * @return the angle in radian at time t (between 0 and 1)
     */
    public double getAngle(double t)
    {
        final double lengthtotal = getLength();
        final double d = lengthtotal * t;
        double length = 0;
        double lengthavant = 0;
        for(int i = 0; i < points.size()-1; i++)
        {
            if((lengthavant <= d) & (d < length))
                return Math.atan2(points.get(i+1).y-points.get(i).y, points.get(i+1).x-points.get(i).x);

            length += points.get(i).distance(points.get(i+1));
            lengthavant = 0;
        }

        return 0;
    }

    
    /**
     * draw the shape
     * @param g 
     */
    void draw(Graphics g)
    {
        if(points.isEmpty())
            return;
        
        float lx = points.get(0).x;
        float ly = points.get(0).y;
        for(Point p : points)
        {
            g.drawLine(lx, ly, p.x, p.y);
            lx = p.x;
            ly = p.y;
        }
        
    }

    
    
    void addPoint(Point point) {
        points.add(point);
    }


    /**
     * 
     * @return a string representing the shape (this string can be saved into a file)
     */
    public String getString()
    {
        String s = "";
        for(Point p : points)
        {
            s += p.x + " " + p.y + "\n";
        }
        return s;
    }

}
