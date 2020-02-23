/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

/**
 *
 * @author Ancmin
 */
public class MenuItemCurstomizedAccelerator {


    static void makeMenuItemCustomizable(final JMenuItem menuItem)
    {
        menuItem.addMenuKeyListener(new MenuKeyListener() {

            public void menuKeyTyped(MenuKeyEvent e) {
            }

            public void menuKeyPressed(MenuKeyEvent e) {
                if(menuItem.isArmed())
                        menuItem.setAccelerator(KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers(), true));

                
            }

            public void menuKeyReleased(MenuKeyEvent e) {
            }
        });

        
    }
}
