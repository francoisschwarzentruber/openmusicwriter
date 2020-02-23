/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter;

/**
 *
 * @author Ancmin
 */
public class ResourceMap {
    
    
    final private org.jdesktop.application.ResourceMap resourceMap;
    final private String className;
      
    
    ResourceMap(Class c)
    {
        resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(c);
        className = c.getName();
    }
    
    
    
    public String getString(String key)
    {
        String result = resourceMap.getString(key);
        if(result == null)
            System.out.println("ERROR: key '" + key + "' not found in " + className + " etc.");
        return result;
    }
    
}
