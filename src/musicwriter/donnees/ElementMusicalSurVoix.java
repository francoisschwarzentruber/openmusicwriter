/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import musicwriter.Erreur;
import org.jdom.Element;


/**
 *
 * @author proprietaire
 */
abstract public class ElementMusicalSurVoix extends ElementMusicalDuree {
    private Voix voix = new Voix();
    
    ElementMusicalSurVoix(Moment moment, Partie partie, int divisions, Element element) {
        super(moment, partie,divisions, element);
        if(element.getChild("voice") != null)
             setVoix(new Voix(element.getChild("voice")));
        else
        {
            Erreur.message("ElementMusicalSurVoix : musicxxml ne trouve pas la voix");
            setVoix(new Voix());
        }
        

    }
    
    
    
    ElementMusicalSurVoix(Moment departMoment, Duree duree, Portee portee)
    {
        super(departMoment, duree, portee);
    }
    
    
    
    
    public void setVoix(Voix voix)
    {
        this.voix = voix;
    }
    
    
    
    public Voix getVoix()
    {
        return voix;
    }
    
    
    
    @Override
    public Element sauvegarder()
    {
        Element element = super.sauvegarder();
        element.addContent(getVoix().sauvegarder());
        return element;
        
    }
    
}
