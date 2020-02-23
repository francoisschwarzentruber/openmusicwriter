/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter;

import java.util.Locale;

/**
 *
 * @author Ancmin
 */
public class Resource {
    
    
    public static ResourceMap createResourceMap(Class c)
    {
        return new ResourceMap(c);
    }
    
    public static void setLocaleDefault()
    {
        Locale.setDefault(Locale.getDefault());
    }
    
    
    public static void setLocale(Locale locale)
    {
        Locale.setDefault(locale);
    }
    
    



}
