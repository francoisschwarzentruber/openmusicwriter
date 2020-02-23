/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import javax.swing.JLabel;

/**
 *
 * @author Ancmin
 */
public class JUrlLabel extends JLabel {

    public JUrlLabel() {
        this("");
    }

    public JUrlLabel(String label) {
        super(label);

        setForeground(Color.BLUE.darker());
        setCursor(
                new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(
                new URLOpenAdapter());
    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }

    
    


    //this is used to underline the text
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.blue);

        Insets insets = getInsets();

        int left = insets.left;
        if (getIcon() != null) {
            left += getIcon().getIconWidth() + getIconTextGap();
        }

        g.drawLine(left, getHeight() - 1 - insets.bottom, 
			(int) getPreferredSize().getWidth()
                - insets.right, getHeight() - 1 - insets.bottom);
    }

    private class URLOpenAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            WebBrowserInteraction.goToURL(getText());
        }
    }
}
