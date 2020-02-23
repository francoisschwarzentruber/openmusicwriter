package musicwriter.guiswing.dialogs;

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.io.BufferedInputStream;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.SwingUtilities;

public class GhostPictureAdapter extends GhostDropAdapter
{
	private Image image;

	public GhostPictureAdapter(GhostGlassPane glassPane, ImageIcon icon, Transferable transferable) {
	   super(glassPane, transferable);
	   this.image = icon.getImage();

	}

    public void mousePressed(MouseEvent e)
    {
        Component c = e.getComponent();

        glassPane.setVisible(true);

        Point p = (Point) e.getPoint().clone();
        SwingUtilities.convertPointToScreen(p, c);
        SwingUtilities.convertPointFromScreen(p, glassPane);

        glassPane.setPoint(p);
        glassPane.setImage(image);
        glassPane.repaint();
    }

    public void mouseReleased(MouseEvent e)
    {
        Component c = e.getComponent();

        Point p = (Point) e.getPoint().clone();
        SwingUtilities.convertPointToScreen(p, c);

        Point eventPoint = (Point) p.clone();
        SwingUtilities.convertPointFromScreen(p, glassPane);

        glassPane.setPoint(p);
        glassPane.setVisible(false);
        glassPane.setImage(null);

        fireGhostDropEvent(new GhostDropEvent(transferable, eventPoint));
    }
}