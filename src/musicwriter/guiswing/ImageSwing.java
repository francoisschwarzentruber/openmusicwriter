/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import javax.swing.ImageIcon;
import musicwriter.gui.Image;

/**
 *
 * @author Ancmin
 */
public class ImageSwing implements Image {
    final private ImageIcon imageIcon;

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public ImageSwing(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public int getHeight() {
        return imageIcon.getIconHeight();
    }

    @Override
    public int getWidth() {
        return imageIcon.getIconWidth();
    }
 
    
}
