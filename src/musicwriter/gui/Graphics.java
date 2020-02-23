/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

import java.awt.Color;
import java.awt.Font;


/**
 *
 * @author Ancmin
 */
abstract public  class Graphics {

    abstract public void drawLine(double x1, double y1, double x2, double y2);

    abstract public void drawImage(Image image, double d, double y, double d0, double d1);

    
    
    abstract public void setPenWidth(float f); //g.setPenWidth(1.2f)); en Swing

    abstract public void fillOval(double d, double d0, double d1, double d2);

    abstract public void drawImage(Image image, float x, float y, float width, float height, float angle);
    
    public void drawImage(Image image, Rectangle r)
    {
        drawImage(image, r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
    }

    public void drawImage(Image image, Rectangle r, float angle)
    {
        drawImage(image, r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight(), angle);
    }
    
    abstract public void setColor(Color colorLectureMIDIPacmanLangue);

    abstract public Color getColor();

    abstract public void fillRect(double d, double d0, double d1, double d2);

    abstract public void setFont(Font font);

    abstract public void fillPolygon(Polygon p);

    abstract public Font getFont();

    abstract public int getFontHeight();

    
    
    abstract public void setAlpha(float f);
    
    abstract public void translate(double imageableX, double imageableY);

    abstract public void scale(double d, double d0);

    abstract public void copyArea(int x, int y, int width, int height, int dx, int dy);

    abstract public int getFontStringWidth(String string);

    abstract public void drawOval(double xleft, double ytop, double width, double height);

    abstract public void drawString(String texte, float f, float f0);

    abstract public void rotate(double angle, double cercleCentreX, double cercleCentreY);

    abstract public void drawPolygon(Polygon p);

    abstract public void fillRect(Rectangle r);

    

    
}
