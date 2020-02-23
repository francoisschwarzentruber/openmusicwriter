/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import musicwriter.gui.Graphics;
import musicwriter.gui.Image;
import musicwriter.gui.Polygon;

/**
 *
 * @author Ancmin
 */
public class GraphicsSwing extends Graphics {


    final private Graphics2D g;

    
    private void graphics2Dconfigure(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }
        
        
    public GraphicsSwing(Graphics2D g) {
        this.g = g;
        graphics2Dconfigure(g);
    }

    public GraphicsSwing(java.awt.Graphics graphic) {
        this.g = (Graphics2D) graphic;
        graphics2Dconfigure(this.g);
    }

    @Override
    public void drawLine(double x1, double y1, double x2, double y2) {
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    @Override
    public void drawImage(Image image, double xleft, double yleft, double width, double height) {
        g.drawImage(((ImageSwing) image).getImageIcon().getImage(), (int) xleft, (int) yleft, (int) width, (int) height, null);
    }

    @Override
    public void setPenWidth(float f) {
        g.setStroke(new BasicStroke(f));
    }

    @Override
    public void fillOval(double xleft, double yleft, double width, double height) {
        g.fillOval((int) xleft, (int) yleft, (int) width, (int) height);
    }

    @Override
    public void drawImage(Image image, float x, float y, float width, float height, float angle) {

     g.rotate(angle, x + width/2, y + height/2);
     g.drawImage(((ImageSwing) image).getImageIcon().getImage(), (int) x, (int) y, (int) width, (int) height, null);
     g.rotate(-angle, x + width/2, y + height/2);

    }

    @Override
    public void setColor(Color color) {
        g.setColor(color);
    }

    @Override
    public Color getColor() {
        return g.getColor();
    }

    @Override
    public void fillRect(double xleft, double ytop, double width, double height) {
        g.fillRect((int) xleft, (int) ytop, (int) width, (int) height);
    }

    @Override
    public void setFont(Font font) {
        g.setFont(font);
    }

    @Override
    public void drawString(String nom, float xleft, float ytop) {
        g.drawString(nom, xleft, ytop);
    }

    @Override
    public void fillPolygon(Polygon p) {
        g.fillPolygon(((PolygonSwing) p).getPolygonSwing());
    }

    @Override
    public Font getFont() {
        return g.getFont();
    }

    @Override
    public int getFontHeight() {
        return g.getFontMetrics().getHeight();
    }

    @Override
    public void setAlpha(float alpha) {
       g.setComposite(java.awt.AlphaComposite.getInstance(
                       java.awt.AlphaComposite.SRC_OVER, alpha ));

    }

    @Override
    public void translate(double imageableX, double imageableY) {
        g.translate(imageableY, imageableY);
    }

    @Override
    public void scale(double sx, double sy) {
        g.scale(sx, sy);
    }

    
    
    
    
    
    
    AffineTransform savedTransform;
    static AffineTransform initialTransform;


    /**
     * 
     * pas très propre, mais c'est pour réparer un bug de java.awt.Graphics dans copyArea qui
     * ne tient pas compte des transforms
     * (ce n'est pas dans l'interface)
     * @param transform 
     */
    public static void setInitialTransform(AffineTransform transform) {
        initialTransform = transform;
    }
    
    

    /**
     * should not been called from outside
     * (but it is called because copyArea is not implemented...)
     */
    public void transformationWithZoom(Graphics2D g)
    {
        ((Graphics2D) g).setTransform(savedTransform);
    }

    /**
     * should not been called from outside
     * (but it is called because copyArea is not implemented...)
     */
    public void transformationNoZoom(Graphics2D g)
    {
         savedTransform = ((Graphics2D) g).getTransform();
        ((Graphics2D) g).setTransform(initialTransform);
    }
    
    
    
    
    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        transformationNoZoom(g);


            g.copyArea(x, y,
                       width,
                        height,
                        dx, dy);

          transformationWithZoom(g);
    }

    @Override
    public int getFontStringWidth(String string) {
        return g.getFontMetrics().stringWidth(string);
    }

    @Override
    public void drawOval(double xleft, double ytop, double width, double height) {
        g.drawOval((int) xleft, (int) ytop, (int) width, (int) height);
    }



    @Override
    public void rotate(double angle, double cercleCentreX, double cercleCentreY) {
       g.rotate(angle, cercleCentreX, cercleCentreY);
    }

    @Override
    public void drawPolygon(Polygon p) {
        g.drawPolygon(((PolygonSwing) p).getPolygonSwing());
    }

  
    @Override
    public void fillRect(musicwriter.gui.Rectangle r) {
        g.fillRect((int) r.getMinX(), (int) r.getMinY(), (int) r.getWidth(), (int) r.getHeight());
    }



    
}
