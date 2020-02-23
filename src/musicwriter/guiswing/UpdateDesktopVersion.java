/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Ancmin
 */
public class UpdateDesktopVersion {
    
    static private String xmlPath = "http://openmusicwriter.sourceforge.net/update.xml";
    static private int currentVersionDate = 20120404;
    
    static public String getCurrentVersion()
    {
        int first = (currentVersionDate / 10000) - 2000;
        int second = (currentVersionDate % 10000) / 100;
        int third = (currentVersionDate % 100);
        return "1." + String.valueOf(first) +
                "." + String.valueOf(second) + 
                "." + String.valueOf(third);
    }
    
    
    
    static public class Update
    {
        final private String url;
        final private int date;

            public Update(String url, int date) {
                this.url = url;
                this.date = date;
            }

            public String getUrl() {
                return url;
            }

        public int getDate()
        {
            return date;
        }
    }
    
    
    
    /**
     * 
     * @return null if there is no update to do
     *         the information to update
     */
    static public Update getUpdate()
    {
        Update update = getUpdateOnServer();
        if(update.getDate() <= currentVersionDate)
        {
            return null;
        }
        else
            return update;
    }

    
    
    
    static private Update getUpdateOnServer(){
	try {
		final URL xmlUrl = new URL(xmlPath);
		
		//On ouvre une connections sur la page
		URLConnection urlConnection = xmlUrl.openConnection();
		urlConnection.setUseCaches(false);
		
		//On se connecte sur cette page
		urlConnection.connect();
		
		//On récupère le fichier XML sous forme de flux
		final InputStream stream = urlConnection.getInputStream();
					
		final SAXBuilder sxb = new SAXBuilder();
			
                Document xmlDocument = null;
		//On crée le document xml avec son flux
		try {xmlDocument = sxb.build(stream);
		} catch (JDOMException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();}
		
		//On récupère la racine
		Element racine = xmlDocument.getRootElement();
		
		//On liste toutes les versions
		Element elementDesktopUpdate = racine.getChild("desktopupdate");

		Element elementDate = elementDesktopUpdate.getChild("date");
		Element elementURL = elementDesktopUpdate.getChild("url");

                return new Update(elementURL.getText(), Integer.parseInt(elementDate.getText()));

		
		
		
	} catch (MalformedURLException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return null;
}
    
    
    
    
}
