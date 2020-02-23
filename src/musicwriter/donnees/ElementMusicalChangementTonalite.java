/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import org.jdom.Element;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalChangementTonalite extends ElementMusical {
    final private Tonalite tonalite;

    public ElementMusicalChangementTonalite(Moment moment, Element element) {
        this(moment, new Tonalite(Integer.parseInt(element.getChild("fifths").getText())));

        
    }

    public Tonalite getTonalite() {
        return tonalite;
    }

    public ElementMusicalChangementTonalite(Moment departMoment, Tonalite tonalite)
    {
        super(departMoment);
        getDebutMoment().setPrecede();
        this.tonalite = tonalite;
    }

    @Override
    public void deplacer(Moment debutMoment) {
        super.deplacer(debutMoment);
        getDebutMoment().setPrecede();
    }





    @Override
    public Element sauvegarder()
    {
        return new Element("key")
                .addContent(new Element("fifths")
                .addContent(String.valueOf(tonalite.getDiesesNombre())));
    }

    @Override
    public boolean isSurParties(Collection<Partie> parties) {
        return true;
    }




   

}
