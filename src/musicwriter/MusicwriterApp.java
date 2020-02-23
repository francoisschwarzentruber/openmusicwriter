/*
 * MusicwriterApp.java
 */

package musicwriter;

import java.util.EventObject;
import musicwriter.guiswing.ImageLoaderSwing;
import musicwriter.guiswing.MusicwriterView;
import musicwriter.guiswing.RegionFactorySwing;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class MusicwriterApp extends SingleFrameApplication {

    static private MusicwriterView view;
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {

        ExitListener exit = new ExitListener() {

            @Override
            public boolean canExit(EventObject event) {
                return view.closeConfirmation();
            }
            @Override
            public void willExit(EventObject event){}
        };

        addExitListener(exit);

        view = new MusicwriterView(this);
        show(view);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     * @param root 
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }



    
    /**
     * A convenient static getter for the application instance.
     * @return the instance of MusicwriterApp
     */
    public static MusicwriterApp getApplication() {
        return Application.getInstance(MusicwriterApp.class);
    }

    /**
     * Main method launching the application.
     * @param args 
     */
    public static void main(String[] args) {        
        ImageLoaderSwing.createInstance();
        RegionFactorySwing.createInstance();
        
        launch(MusicwriterApp.class, args);
    }
}
