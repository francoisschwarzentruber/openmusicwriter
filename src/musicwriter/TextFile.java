/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author proprietaire
 */
public class TextFile {
    
    
    static class Pipo
    {
         
    }
        
        
    public static void string_ecrire_dans_fichier(String text, String fichier_nom) throws IOException {
        BufferedWriter fichier = new BufferedWriter(new FileWriter(fichier_nom));
        fichier.write(text);
        fichier.close();
    }
    
    
    
    public static String string_lire_dun_fichier(String fichier_nom) throws FileNotFoundException, IOException
    {
        FileReader fr = (new FileReader(fichier_nom));
        BufferedReader fichier = new BufferedReader(fr);
        
        String s = "";
    
        while(fichier.ready())
        {
            s = s + fichier.readLine() + "\n";
        }
        
        fichier.close();
        return s;
    }
    
    public static String lire_dans_un_BufferedReader(BufferedReader fichier) throws FileNotFoundException, IOException
    {
        String s = "";
    
        while(fichier.ready())
        {
            s = s + fichier.readLine() + "\n";
        }
        
        fichier.close();
        return s;
    }
    
    
    
    public static String lire_dans_un_BufferedReader_sans_les_load_descheme(BufferedReader fichier) throws FileNotFoundException, IOException
    {
        String s = "";
    
        while(fichier.ready())
        {
            String line = fichier.readLine();
            
            if(!line.trim().startsWith("(load"))
                 s = s + line + "\n";
        }
        
        fichier.close();
        return s;
    }
    
    public static String lire_dans_une_ressource(String ressource_nom) throws FileNotFoundException, IOException
    {
        Pipo pipo = new Pipo();
        InputStreamReader isr = new InputStreamReader(
        pipo.getClass().getResourceAsStream(ressource_nom));
        BufferedReader fichier = new BufferedReader(isr);
        return lire_dans_un_BufferedReader(fichier);
        
    }
    
    
    public static String lire_dans_une_ressource_sans_les_load_descheme(String ressource_nom) throws FileNotFoundException, IOException
    {
        Pipo pipo = new Pipo();
        InputStreamReader isr = new InputStreamReader(
        pipo.getClass().getResourceAsStream(ressource_nom));
        BufferedReader fichier = new BufferedReader(isr);
        return lire_dans_un_BufferedReader_sans_les_load_descheme(fichier);
        
    }

    public static String lire_dans_une_url(URL uRL) throws FileNotFoundException, IOException {
        InputStream flot = uRL.openStream();
        InputStreamReader fichier = new InputStreamReader(flot);
        
        char[] c = new char[1000];
        
        int number_of_char = fichier.read(c);
        
        String s = String.copyValueOf(c);
        
        s = s.substring(0, number_of_char);
        
        fichier.close();
        return s;
    }
}
