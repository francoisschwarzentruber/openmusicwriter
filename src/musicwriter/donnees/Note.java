/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;
import musicwriter.Erreur;
import org.jdom.Element;



/**
 *
 * @author proprietaire
 */
public class Note extends ElementMusicalSurVoix {
    private HampeDirection hampeDirection = HampeDirection.AUTOMATIC;
    private NoteFigure noteFigure = NoteFigure.OVALE;
    private ArrayList<String> ornements = new ArrayList<String>();
    private ArrayList<String> articulations = new ArrayList<String>();

    public ArrayList<String> getArticulations() {
        return articulations;
    }

    public void setArticulations(ArrayList<String> articulations) {
        this.articulations = articulations;
    }

    public ArrayList<String> getOrnements() {
        return ornements;
    }

    public void setOrnements(ArrayList<String> ornements) {
        this.ornements = ornements;
    }


    public boolean isStaccato()
    {
        return articulations.contains("staccato");
    }

    public NoteFigure getNoteFigure() {
        return noteFigure;
    }





    public void setNoteFigure(NoteFigure noteFigure) {
        this.noteFigure = noteFigure;
    }



    
    private boolean lieeALaSuivante = false;
    private boolean appogiature = false;
    private boolean rythmeBarre = false;

    public boolean isRythmeBarre() {
        return rythmeBarre;
    }

    public void setRythmeBarre(boolean rythmeBarre) {
        this.rythmeBarre = rythmeBarre;
    }

    public boolean isAppogiature() {
        return appogiature;
    }
    

    public void setAppogiature(boolean appogiature) {
        this.appogiature = appogiature;
    }





    /**
     *
     * @return la direction de la hampe. (vers le haut ou vers le bas)
     */
    public HampeDirection getHampeDirection() {
        return hampeDirection;
    }

    public void setHampeDirection(HampeDirection hampeDirection) {
        this.hampeDirection = hampeDirection;
    }

  /**
     * <!ELEMENT ornaments
	(((trill-mark | turn | delayed-turn | inverted-turn |
	   shake | wavy-line | mordent | inverted-mordent |
	   schleifer | tremolo | other-ornament),
	   accidental-mark*)*)>

     */
    public boolean isOrnement(String string) {
        return ornements.contains(string);
    }

    private void addStrings(ArrayList<String> liste, Element element) {
        if(element != null)
        {
            for(Object o : element.getChildren())
            {
                Element e = (Element) o;
                liste.add(e.getName());
            }
        }
        
    }

    private Element getArticulationsForMusicXML() {
        Element elementArticulations = new Element("articulations");

        for(String s : getArticulations())
        {
            elementArticulations.addContent(new Element(s));
        }

        return elementArticulations;
    }



    private Element getOrnementsForMusicXML() {
        Element element = new Element("ornaments");

        for(String s : getOrnements())
        {
            element.addContent(new Element(s));
        }

        return element;
    }

    static private HampeDirection stringToHampeDirection(String text) {
        if(text.equals("up"))
        {
            return HampeDirection.HAUT;
        }
        else if(text.equals("down"))
        {
            return HampeDirection.BAS;
        }
        else
            return HampeDirection.AUTOMATIC;

    }







    public enum NoteFigure{
        OVALE(0), LOSANGE(1), CROIX(2);

        private int numero = 0;

        private NoteFigure(int numero)
        {
            this.numero = numero;
        }

        private NoteFigure(String s)
        {
            
        }


    }



    private String noteFigureToString(NoteFigure noteFigure)
    {
        switch(noteFigure)
        {
            case OVALE: return "oval";
            case CROIX: return "cross";
            case LOSANGE: return "diamond";
            default:
                Erreur.message("noteFigureToString: figure bizarre");
                return "";
        }
    }



    private NoteFigure stringToNoteFigure(String s)
    {
        if(s.equals("diamond"))
            return NoteFigure.LOSANGE;

        if(s.equals("cross"))
            return NoteFigure.CROIX;


        Erreur.message("stringToNoteFigure: je ne comprends pas " + s);
        return NoteFigure.OVALE;
    }





    

    public enum HampeDirection{
        AUTOMATIC(0), HAUT(1), BAS(2);
        
        private int numero = 0;

        private HampeDirection(int numero)
        {
            this.numero = numero;
        }
    }

    public Note(Moment moment, Partie partie, int divisions, Element element) {
        super(moment, partie, divisions, element);
        setHauteur(new Hauteur(element.getChild("pitch")));

        if(element.getChild("tie") != null)
        {
            String a = element.getChild("tie").getAttributeValue("type");

            if(a != null)
                if(a.equals("start"))
                    setLieeALaSuivante(true);
        }



        if(element.getChild("notehead") != null)
        {
           noteFigure = stringToNoteFigure(element.getChild("notehead").getText());
        }

        if(element.getChild("stem") != null)
        {
           setHampeDirection(stringToHampeDirection(element.getChild("stem").getText()));
        }



        

        Element notations = element.getChild("notations");
        if(notations != null)
        {
              addStrings(articulations, notations.getChild("articulations"));
              addStrings(ornements, notations.getChild("ornaments"));
        }










    }



    public void setHauteur(Hauteur hauteur) {
        this.hauteur = hauteur;
    }

    
    public void transposer(Intervalle intervalle)
    {
        setHauteur(intervalle.getHauteur2(hauteur));
    }


    public Note getNoteTransposee(Intervalle intervalle)
    {
        return new Note(getDebutMoment(), getDuree(), intervalle.getHauteur2(hauteur), getPortee(), getVoix());
    }

    public Hauteur getHauteur() {
        return hauteur;
    }
    

    public Hauteur getHauteurEntendue()
    {
        return getPortee().getPartie().getTransposition().getHauteur2(hauteur);
    }



    public boolean isLieeALaSuivante()
    {
        return lieeALaSuivante;
    }


    public void setLieeALaSuivante(boolean ouinon)
    {
        lieeALaSuivante = ouinon;
    }
        
        
    private Hauteur hauteur = null;
    
    
    public Note(Moment departMoment, Duree duree, Hauteur hauteur, Portee portee)
    {
        super(departMoment, duree, portee);
        this.hauteur = hauteur;
        
    }
    
    public Note(Moment departMoment, Duree duree, Hauteur hauteur, Portee portee, Voix voix)
    {
        super(departMoment, duree, portee);
        this.hauteur = hauteur;
        setVoix(voix);
        
    }
    
    @Override
    public Curseur getCurseur()
    {
        Curseur curseur = new Curseur(getDebutMoment(), hauteur, getPortee(), getVoix(), this);
        return curseur;
    }

    @Override
    public void setCurseur(Curseur curseur) {
        super.setCurseur(curseur);
        setHauteur(curseur.getHauteur());
    }

    
    
   
    
    
    
    public int getCoordonneeVerticaleSurPortee()
    {
         return getPortee().getCoordonneeVerticale(getDebutMoment(), hauteur);
    }
    
    



    
    
    


    
    
    
    
/**
 *
 * @return something like
 * <note>
        <pitch>
          <step>G</step>
          <octave>4</octave>
          </pitch>
        <duration>480</duration>
        <voice>1</voice>
        <type>quarter</type>
        <stem>up</stem>
        <staff>1</staff>
        <notations>
          <articulations>
            <tenuto/>
            </articulations>
          </notations>
        </note>
 */
    @Override
    public Element sauvegarder()
    {
        Element elementNote = super.sauvegarder();
        
        Element elementPitch = getHauteur().sauvegarder();   
        elementNote.addContent(elementPitch);
        elementNote.addContent(getPortee().sauvegarderNumero());

        if(!noteFigure.equals(NoteFigure.OVALE))
            elementNote.addContent(new Element("notehead")
                                    .setText(noteFigureToString(noteFigure)));


        Element elementNotations = new Element("notations");
        elementNotations.addContent(getArticulationsForMusicXML());
        elementNotations.addContent(getOrnementsForMusicXML());

        elementNote.addContent(elementNotations);

        if(isLieeALaSuivante())
        {
            Element elementTie = new Element("tie");
            elementTie.setAttribute("type", "start");
            elementNote.addContent(elementTie);
        }


        if(getHampeDirection().equals(HampeDirection.HAUT))
        {
            elementNote.addContent(new Element("stem").setText("up"));
        }
        else
        {
            elementNote.addContent(new Element("stem").setText("down"));
        }


        return elementNote;
        

    }
    
    
    
    @Override
    public String toString()
    {
        return "note " + hauteur.toString() + ", début moment : " + getDebutMoment().toString() + ", durée : " + getDuree();
    }

    @Override
    public Note clone() {
        Note note = new Note(getDebutMoment(), getDuree(), getHauteur().clone(), getPortee(), getVoix());
        note.setNoteFigure(noteFigure);
        note.setAppogiature(appogiature);
        note.setArticulations((ArrayList<String>) articulations.clone());
        return note;
    }

     
    
    
    

}
