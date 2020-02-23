/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import musicwriter.donnees.Portee.Clef;
import org.jdom.Element;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalClef extends ElementMusicalSurPortee {
     private final Portee.Clef clef;
     private int verticalLineCoordinate;
     

     static Portee getPorteePartieElementNumber(Partie partie, Element elementAttribut)
     {
        if(elementAttribut.getAttributeValue("number") == null)
        {
             return partie.getPorteePremiere();
        }
             return partie.getPortee(Integer.parseInt(elementAttribut.getAttributeValue("number")));
     }
     


        
     /**
      * 
      * @param elementAttributMusicXML
      * @return extract the clef from the MusicalXML element
      */
    private static Clef getClef(Element elementAttributMusicXML)
    {
        return clefToChar.getAntecedant(elementAttributMusicXML.getChild("sign").getText());
    }
        
        
    private int getVerticalLineCoordinate(Element elementAttributMusicXML) {
        int line = Integer.parseInt(elementAttributMusicXML.getChild("line").getText());
        return (line - clefToStandardLine.get(getClef())) * 2;
    }

    /**
     * Crée un élément clef à partir d'un elementAttribut (MusicXML)
     * on donne à manger aussi le moment où sera l'élement musical et aussi
     * la partie (style partie piano, partie flûte) dans laquelle la clef est.
     * (la portée dans laquelle elle est est spécifié dans elementAttribut.)
     * @param moment
     * @param partie
     * @param elementAttribut
     */
    public ElementMusicalClef(Moment moment, Partie partie, Element elementAttribut) {
        this(moment,
              getPorteePartieElementNumber(partie, elementAttribut),
              getClef(elementAttribut));
        
        verticalLineCoordinate = getVerticalLineCoordinate(elementAttribut);
       
    }

    public Clef getClef() {
        return clef;
    }

     public ElementMusicalClef(Moment moment, Portee portee, Portee.Clef clef)
     {
          super(moment, portee);
          getDebutMoment().setPrecede();
          this.clef = clef;
     }

    @Override
    public Curseur getCurseur()
    {
        Curseur curseur = new Curseur(getDebutMoment(),
                getPortee().getHauteur(getDebutMoment(), verticalLineCoordinate), getPortee(), null, this);
        //curseur.declarerSurElementMusical(this);
        return curseur;
    }

    @Override
    public void deplacer(Moment debutMoment) {
        super.deplacer(debutMoment);
        getDebutMoment().setPrecede();
    }

    
    
    
    
    
    
    final static private Map<Clef, ArrayList<Integer>> 
                verticalLineCoordinates = createVerticalLineCoordinates();
    
    private static Map<Clef, ArrayList<Integer>> createVerticalLineCoordinates()
    {
        final Map<Clef, ArrayList<Integer>> 
                M = new EnumMap<Clef, ArrayList<Integer>>(Clef.class);   
                    
        M.put(Clef.ClefDeSol, new ArrayList<Integer>());
        M.put(Clef.ClefDeFa, new ArrayList<Integer>());
        M.put(Clef.ClefDUt, new ArrayList<Integer>());
                    
        M.get(Clef.ClefDeSol).add(0);
        M.get(Clef.ClefDeSol).add(-2);
        
        M.get(Clef.ClefDeFa).add(0);
        M.get(Clef.ClefDeFa).add(-2);
        M.get(Clef.ClefDeFa).add(2);
        
        for(int i = -4; i <= 4; i += 2)
            M.get(Clef.ClefDUt).add(i);
        
        
        return M;
    };
    
    
    
    
    
    
    
    
    @Override
    public void setCurseur(Curseur curseur) {
        super.setCurseur(curseur);
        
        verticalLineCoordinate = curseur.getCoordonneeVerticaleSurPortee();
        
        verticalLineCoordinate = getPlusProche(verticalLineCoordinates.get(getClef()), verticalLineCoordinate);
        
        
        
    }

    
    
    
    public int getVerticalLineCoordinate() {
        return verticalLineCoordinate;
    }



    
    
    static Bijection<Clef, String> clefToChar = clefToChar();
    private static Bijection<Clef, String> clefToChar()
    {
        final Bijection<Clef, String> B = new Bijection<Clef, String>();
        
        B.put(Clef.ClefDUt, "C");
        B.put(Clef.ClefDeFa, "F");
        B.put(Clef.ClefDeSol, "G");
        
        return B;
    };
    
    
    static Map<Clef, Integer> clefToStandardLine = clefToStandardLine();
    private static Map<Clef, Integer> clefToStandardLine()
    {
        final Map<Clef, Integer> B = new EnumMap<Clef, Integer>(Clef.class);
        
        B.put(Clef.ClefDUt, 3);
        B.put(Clef.ClefDeFa, 4);
        B.put(Clef.ClefDeSol, 2);
        
        return B;
    };
    
    @Override
    public Element sauvegarder()
    {
        Element elementClef = new Element("clef");
        elementClef.setAttribute("number", String.valueOf(getPortee().getNumero()));

        elementClef.addContent(new Element("sign").addContent(clefToChar.getImage(getClef())));
        elementClef.addContent(new Element("line").addContent(String.valueOf(clefToStandardLine.get(getClef()) + verticalLineCoordinate/2)));

        return elementClef;
    }

    
    /**
     * 
     * @param coordonneeVerticale
     * @return la hauteur d'une note de coordonnée verticale qui est placé après cette élément musical clef
     */
    Hauteur getHauteur(int coordonneeVerticale) {
        return getClef().getHauteur(coordonneeVerticale - verticalLineCoordinate);
    }

    public int getCoordonneeVerticale(Hauteur hauteur) {
        return getClef().getCoordonneeVerticale(hauteur) + verticalLineCoordinate;
    }

    private int getPlusProche(ArrayList<Integer> get, int verticalLineCoordinate) {
        int evaluation = 456123456;
        int pp = 0;
        
        for(int v : get)
        {
            if(Math.abs(v - verticalLineCoordinate) < evaluation)
            {
                evaluation = Math.abs(v - verticalLineCoordinate);
                pp = v;
            }
        }
        
        return pp;
    }



}
