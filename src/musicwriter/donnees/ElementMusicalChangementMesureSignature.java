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
public class ElementMusicalChangementMesureSignature extends ElementMusical {
    private final MesureSignature signature;

    public  ElementMusicalChangementMesureSignature(Moment departMoment, MesureSignature signature)
     {
         super(departMoment);
         getDebutMoment().setPrecede();
         this.signature = signature;
     }

    public ElementMusicalChangementMesureSignature(Moment moment, Element child) {
        this(moment, new MesureSignature(child.getChildText("beats"),
                Integer.parseInt(child.getChildText("beat-type"))));
        
    }



     public MesureSignature getSignature()
     {
         return signature;
     }

     
    @Override
    public void deplacer(Moment debutMoment) {
        super.deplacer(debutMoment);
        getDebutMoment().setPrecede();
    }






    @Override
     public Element sauvegarder()
     {
         Element element = new Element("time");
         element.addContent((new Element("beats"))
                 .addContent(signature.getNumerateur()));
         element.addContent((new Element("beat-type"))
                 .addContent(String.valueOf(signature.getDenominateur())));
         return element;
     }

    @Override
    public boolean isSurParties(Collection<Partie> parties) {
        return true;
    }
}
