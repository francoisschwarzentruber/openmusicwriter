/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter;

import java.awt.Component;
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;

/**
 *
 * @author Ancmin
 */
public class Utils {
    public static Window findWindow(Component c) {
    if (c == null) {
        return JOptionPane.getRootFrame();
    } else if (c instanceof Window) {
        return (Window) c;
    } else {
        return findWindow(c.getParent());
    }
}
    
    
    public static RootPaneContainer findRootPane(Component c) {
        if (c instanceof javax.swing.JDialog) {
            return (RootPaneContainer) c;
        } else {
            return findRootPane(c.getParent());
        }
}
}
