/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ZoomablePanel.java
 *
 * Created on 25 avr. 2010, 16:57:53
 */

package musicwriter.guiswing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Ancmin
 */
public class ZoomablePanel extends javax.swing.JPanel {

    private final JPanel panel = this;
    private Point curseurSouris = null;
    private int curseurSourisClicAnimationi = -1;
    private final Timer timer = new Timer(50, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            curseurSourisClicAnimationi++;
            if(curseurSourisClicAnimationi > 4)
            {
                curseurSourisClicAnimationi = -1;
                timer.stop();
            }
            panel.repaint();
        }
        
    });
    private double zoom = 1.0;

    public double getZoom() {
        return zoom;
    }


    public void setZoom(double zoom)
    {
        this.zoom = zoom;
    }



    public double getXLogical(double physicalX)
    {
        return physicalX / zoom;
    }

    public double getYLogical(double physicalY)
    {
        return physicalY / zoom;
    }

    public double getXPhysical(double logicalX)
    {
        return logicalX * zoom;
    }

    public double getYPhysical(double logicalY)
    {
        return logicalY * zoom;
    }

    public musicwriter.gui.Rectangle getRectangleLogical(Rectangle rectanglePhysical)
    {
        return new RectangleSwing((int) getXLogical(rectanglePhysical.x),
                             (int) getYLogical(rectanglePhysical.y),
                             (int) getXLogical(rectanglePhysical.width),
                             (int) getYLogical(rectanglePhysical.height));
    }

    public Font getFontPhysical(Font font)
    {
        return new Font(font.getName(), font.getStyle(), (int) (font.getSize() * zoom));
    }

    protected java.awt.Rectangle getRectanglePhysical(musicwriter.gui.Rectangle rectangleLogical)
    {
        return new Rectangle((int) getXPhysical(rectangleLogical.getMinX()),
                             (int) getYPhysical(rectangleLogical.getMinY()),
                             (int) getXPhysical(rectangleLogical.getWidth()),
                             (int) getYPhysical(rectangleLogical.getHeight()));
    }

    public Dimension getDimensionPhysical(Dimension dimensionLogical)
    {
        return new Dimension((int) (dimensionLogical.width * zoom),
                             (int) (dimensionLogical.height * zoom));
    }

    public musicwriter.gui.Point getPointLogical(Point pointPhysical)
    {
        return new musicwriter.gui.Point((int) getXLogical(pointPhysical.getX()), (int) getYLogical(pointPhysical.getY()));
    }

    public Point getPointPhysical(musicwriter.gui.Point pointLogical)
    {
        return new Point((int) getXPhysical(pointLogical.getX()), (int) getYPhysical(pointLogical.getY()));
    }



    private java.awt.event.MouseEvent mouseEventAdapt(java.awt.event.MouseEvent evt)
    {
       return new java.awt.event.MouseEvent(
                             (Component) evt.getSource(),
                             evt.getID(),
                             evt.getWhen(),
                             evt.getModifiers(),
                             (int) (evt.getX() / zoom),
                             (int) (evt.getY() / zoom),
                             evt.getClickCount(),
                             evt.isPopupTrigger());
    }






    protected void zoomMouseListenerAdapt()
    {
        for(final MouseListener m : getMouseListeners())
        {
            removeMouseListener(m);
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                     m.mouseClicked(mouseEventAdapt(evt));
                }
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                     m.mousePressed(mouseEventAdapt(evt));
                     curseurSouris = evt.getPoint();
                     //timer.start();
                }
                @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                m.mouseReleased(mouseEventAdapt(evt));
            }
            });
        }


        for(final MouseMotionListener m : getMouseMotionListeners())
        {
            removeMouseMotionListener(m);
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                m.mouseDragged(mouseEventAdapt(evt));
            }
                @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                m.mouseMoved(mouseEventAdapt(evt));
            }
        });
        }
            

    }

    /** Creates new form ZoomablePanel */
    public ZoomablePanel() {
        initComponents();

        
        }
        

    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName("Form"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents







    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        

        
        GraphicsSwing.setInitialTransform(((Graphics2D) g).getTransform());
        ((Graphics2D) g).scale(zoom, zoom);
        
        final int nbpetittraits = 8;

        if(curseurSourisClicAnimationi >= 0)
        {
            g.setColor(new Color(curseurSourisClicAnimationi*64,
                                 curseurSourisClicAnimationi*64,
                                 curseurSourisClicAnimationi*64));
            double D1 = 8+3*curseurSourisClicAnimationi;
            double D2 = 8+3*curseurSourisClicAnimationi + 6;
            for(int i = 0; i < nbpetittraits; i++)
            {
                double angle = 2 * Math.PI * i / nbpetittraits;
                g.drawLine((int) (curseurSouris.x + D1 * Math.cos(angle)),
                           (int) (curseurSouris.y + D1 * Math.sin(angle)),
                           (int) (curseurSouris.x + D2 * Math.cos(angle)),
                           (int) (curseurSouris.y + D2 * Math.sin(angle)));
            }
        }

        
    }





    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
