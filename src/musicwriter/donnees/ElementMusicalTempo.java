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
public class ElementMusicalTempo extends ElementMusical {
    private int nbNoireEnUneMinute;
    private String nom;


    /**
     *
     * @param elementDirection
     * @return les mots stockées dans une structure MusicXML <direction>
     * mots qui peuvent être "allegro", ou "allegretto ma con molto" etc.
     * Si c'est pas possible d'extraire quelque chose (car le fichier MusicXML
     * est foutu ou alors que tout simplement ça ne le spécifie pas (ça arrive) etc.), alors ça revoie ""
     */
    static private String getWords(Element elementDirection)
    {
        if(elementDirection.getChild("direction-type") != null)
        {
            if(elementDirection.getChild("direction-type").getChild("words") != null)
                return elementDirection.getChild("direction-type").getChild("words").getText();
        }
              
        return "";

    }

    public ElementMusicalTempo(Moment moment, Element elementDirection) {
        this(moment,
             (int) Double.parseDouble(elementDirection.getChild("sound").getAttributeValue("tempo")),
              getWords(elementDirection));
    }

    public int getNbNoiresEnUneMinute() {
        return nbNoireEnUneMinute;
    }

    public void setNbNoireEnUneMinute(int nbNoireEnUneMinute) {
        this.nbNoireEnUneMinute = nbNoireEnUneMinute;
    }

    public String getNom() {
        return nom;
    }

    /**
     * spécifie le nom du tempo. Par exemple "Allegro", "Allegretto". Ca n'a qu'un
     * but décoratif
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    public ElementMusicalTempo(Moment debutMoment, int nbNoireEnUneMinute, String nom)
    {
        super(debutMoment);
        this.nbNoireEnUneMinute = nbNoireEnUneMinute;
        this.nom = nom;
    }

    @Override
    public Element sauvegarder() {
        Element elementDirection = new Element("direction");
        Element elementDirectionType = new Element("direction-type");
        Element elementDirectionTypeWords = new Element("words");
        elementDirectionTypeWords.setText(getNom());
        elementDirection.addContent(elementDirectionType);
        elementDirectionType.addContent(elementDirectionTypeWords);
        elementDirection.addContent(new Element("sound").setAttribute("tempo", String.valueOf(getNbNoiresEnUneMinute())));
        
        return elementDirection;
    }

    @Override
    public  boolean isSurParties(Collection<Partie> parties) {
        return true;
    }

    
}
