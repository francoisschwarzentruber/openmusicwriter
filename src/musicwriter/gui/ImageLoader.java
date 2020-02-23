/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui;

/**
 * This class represents an abstract image factory.
 * It is a singleton.
 * @author Ancmin
 */
public abstract class ImageLoader {
    
    protected static ImageLoader img;
    
    static public void createInstance()
    {
        
    }
    
    
    abstract protected Image getImageFromString(String string);
    
    static public Image getImage(String string)
    {
        return img.getImageFromString(string);
    }
    
    
    
}
