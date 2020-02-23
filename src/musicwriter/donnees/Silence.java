/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import org.jdom.Element;

/**
 *
 * @author proprietaire
 */
public class Silence extends ElementMusicalSurVoix {

    private Hauteur hauteur = null;

    public Silence(Moment departMoment, Partie partie, int divisions, Element element) {
        super(departMoment, partie, divisions, element);
        
        if(element.getChild("pitch") != null)
            setHauteur(new Hauteur(element.getChild("pitch")));
        else
            setHauteur(new Hauteur(2, Hauteur.Alteration.NATUREL));
    }
    
    public Silence(Moment departMoment, Duree duree, Portee portee, Hauteur hauteur)
    {
        super(departMoment, duree, portee);
        this.hauteur = hauteur;
        
    }

    public Silence(Moment debutMoment, Duree duree, Portee portee, Hauteur hauteur, Voix voix) {
        this(debutMoment, duree, portee, hauteur);
        setVoix(voix);
    }

    public int getCoordonneeVerticaleSurPortee() {
        return getPortee().getCoordonneeVerticale(getDebutMoment(), hauteur);
    }

    
    public Hauteur getHauteur() {
        return hauteur;
    }
    
    public void setHauteur(Hauteur hauteur)
    {
        this.hauteur = hauteur;
    }
    
    
    @Override
    public Element sauvegarder()
    {
        Element elementSilence = super.sauvegarder();
        
        Element elementPitch = getHauteur().sauvegarder();   
        elementSilence.addContent(elementPitch);
        elementSilence.addContent(new Element("rest"));
        elementSilence.addContent(getPortee().sauvegarderNumero());
        
        return elementSilence;
        

    }
    
    
    
    
    @Override
    public Curseur getCurseur() {
        return new Curseur(getDebutMoment(), getHauteur(), getPortee(), null, this);
    }


    @Override
    public void setCurseur(Curseur curseur) {
        deplacer(curseur.getMoment());
        setPortee(curseur.getPortee());
        hauteur = curseur.getHauteur();
    }

    @Override
    public boolean isSurParties(Collection<Partie> parties) {
        return parties.contains(getPartie());
    }


}
