/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.awt.Desktop;
import java.net.URI;

/**
 *
 * @author Ancmin
 */
public class WebBrowserInteraction {
    static public void goToURL(String url)
    {
        if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Throwable t) {
                    System.out.println("Error with the web browser : " + t);
                }
            }
        else
        {
            System.out.println("Desktop not supported... I cannot go the good webpage!");
        }
    }
    
}
