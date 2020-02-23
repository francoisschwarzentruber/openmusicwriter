/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import org.jdom.Element;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalParoleSyllabe extends ElementMusicalTexte {

    public ElementMusicalParoleSyllabe(Curseur curseur, String texte) {
        super(curseur, texte);

    }

    
    public ElementMusicalParoleSyllabe(Moment moment, Portee portee, String texte) {
        super(new Curseur(moment, new Hauteur(), portee), texte);

    }

    

    @Override
    public Element sauvegarder() {
        Element el = new Element("lyric");
        Element elText = new Element("text");
        elText.setText(getTexte());
        el.addContent(elText);
        return el;
    }



    
}
