/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import musicwriter.Erreur;
import musicwriter.donnees.io.PartitionDonneesChargementMusicXML;
import musicwriter.gui.Image;
import musicwriter.gui.ImageLoader;
import org.jdom.Element;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalNuance extends ElementMusicalImage {
    private final Nuance nuance;

    public Nuance getNuance() {
        return nuance;
    }

    
     public enum Nuance{
        PPP(0), PP(1), P(2), MP(3), MF(4), F(5), FF(6), FFF(7);

        private int numero = 0;

        private Nuance(int numero)
        {
            this.numero = numero;
        }
    }


     static private String nuanceTypeToNomFichier(Nuance nuance)
    {
        switch(nuance)
        {
            case PPP: return "Music_dynamic_pianississimo.svg";
            case PP: return "Music_dynamic_pianissimo.svg";
            case P: return "Music_dynamic_piano.svg";
            case MP: return "Music_dynamic_mezzo_piano.svg";
            case MF: return "Music_dynamic_mezzo_forte.svg";
            case F: return "Music_dynamic_forte.svg";
            case FF: return "Music_dynamic_fortissimo.svg";
            case FFF: return "Music_dynamic_fortississimo.svg";
            default: return null;
        }
    }

     static private String nuanceTypeToTexte(Nuance nuance)
    {
        switch(nuance)
        {
            case PPP: return "ppp";
            case PP: return "pp";
            case P: return "p";
            case MP: return "mp";
            case MF: return "mf";
            case F: return "f";
            case FF: return "ff";
            case FFF: return "fff";
            default: return null;
        }
    }



     static private Nuance TexteToNuance(String s)
     {
         if(s.equals("ppp"))
             return Nuance.PPP;

         if(s.equals("pp"))
             return Nuance.PP;

         if(s.equals("p"))
             return Nuance.P;

         if(s.equals("mp"))
             return Nuance.MP;

         if(s.equals("mf"))
             return Nuance.MF;

         if(s.equals("f"))
             return Nuance.F;

         if(s.equals("ff"))
             return Nuance.FF;

         if(s.equals("fff"))
             return Nuance.FFF;

         Erreur.message("TexteToNuance: on m'a dit " + s + " et je ne comprends pas cette nuance. Je mets ''piano''");
         return Nuance.P;
     }

     

     private String getNuanceTexte()
     {
         return nuanceTypeToTexte(nuance);
     }
     
    static private Image nuanceTypeToImage(Nuance nuance)
    {
        return ImageLoader.getImage(nuanceTypeToNomFichier(nuance));
    }



    /**
     * <direction placement="below">
        <direction-type>
          <dynamics>
            <f/>
            </dynamics>
          </direction-type>
        <staff>1</staff>
        </direction>
     * @param moment
     * @param partie
     * @param element
     * @return
     */
    static private Curseur curseurFromElement(Moment moment, Partie partie, Element element)
    {
        Portee portee;
        
        
        try
        {
             portee = PartitionDonneesChargementMusicXML.getPorteeFromElementMusicXML(partie, element);
        }
        catch(Exception e)
        {
            Erreur.message("ElementMusicalNuance.curseurFromElement : erreur avec la portée");
            portee = partie.getPorteePremiere();
        }

        return new Curseur(moment, Hauteur.getDo0(), portee);
    }


/**
 * <direction placement="below">
        <direction-type>
          <dynamics>
            <f/>
            </dynamics>
          </direction-type>
        <staff>1</staff>
        </direction>
 *
 * extrait le <f/>
 * @param element
 * @return
 */
    static private Nuance nuanceFromElement(Element element)
    {
        for(Object el : element.getChild("direction-type").getChild("dynamics").getChildren())
        {
            return TexteToNuance(((Element) el).getName());
        }
        
        Erreur.message("nuanceFromElement: je ne trouve pas rien dans 'dynamics'. Par défaut, j'ai mis ''piano'' ");
        return Nuance.P;
    }

    public ElementMusicalNuance(Moment moment, Partie partie, Element element)
    {
        this(curseurFromElement(moment, partie, element), nuanceFromElement(element));
    }

    public ElementMusicalNuance(Curseur curseur, Nuance nuance) {
        super(curseur, nuanceTypeToImage(nuance));
        this.nuance = nuance;
    }



    /**
 * <direction placement="below">
        <direction-type>
          <dynamics>
            <f/>
            </dynamics>
          </direction-type>
        <staff>1</staff>
        </direction>
 *
 * extrait le <f/>
 * @return un élément XML qui représente la nuance
 */

    
    @Override
    public Element sauvegarder() {
        Element elementDirection = new Element("direction");
        Element elementDirectionType = new Element("direction-type");
        Element elementDirectionTypeDynamics = new Element("dynamics");
        elementDirectionTypeDynamics.addContent(new Element(getNuanceTexte()));
        elementDirection.addContent(elementDirectionType);
        elementDirectionType.addContent(elementDirectionTypeDynamics);
        elementDirection.addContent(getCurseur().getPortee().sauvegarderNumero());
        return elementDirection;
    }

    


    
}
