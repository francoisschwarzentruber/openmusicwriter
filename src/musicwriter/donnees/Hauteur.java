/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees;

import musicwriter.Arithmetique;
import musicwriter.Erreur;
import musicwriter.donnees.Portee.Clef;
import org.jdom.Element;

/**
 * La hauteur représente une hauteur de note. Par exemple, do#4 ou sib2.
 * @author proprietaire
 */
public class Hauteur {

    /*
     * constructeur protégé car cette classe peut se spécialiser en Intervalle.
     * Et comme ça la classe spécifique Intervalle peut appeler ce constructeur.
     */
    protected Hauteur() {
    }

    /**
     *
     * @param c
     * @return retourne le nom de la note qui correspond au caractère c
     * exemple : c = 'A', retourne NoteNom.LA
     */
    private static NoteNom charForMusicXMLToNomNote(char c) {
        switch (c) {
            case 'A':
                return NoteNom.LA;
            case 'B':
                return NoteNom.SI;
            case 'C':
                return NoteNom.DO;
            case 'D':
                return NoteNom.RE;
            case 'E':
                return NoteNom.MI;
            case 'F':
                return NoteNom.FA;
            case 'G':
                return NoteNom.SOL;

            default:
                return NoteNom.DO;
        }
    }

    private static String nomNoteToStringForMusicXML(NoteNom nom) {
        switch (nom) {
            case DO:
                return "C";
            case RE:
                return "D";
            case MI:
                return "E";
            case FA:
                return "F";
            case SOL:
                return "G";
            case LA:
                return "A";
            case SI:
                return "B";
            default: {
                System.out.println("Erreur dans getNbDemiTonsEntreDoCentralEtNoteNom");
                return "A";
            }
        }
    }

    public static Alteration stringToAlteration(String texte) {
        if (texte.equals("bb")) {
            return Alteration.DOUBLEBEMOL;
        } else if (texte.equals("b")) {
            return Alteration.BEMOL;
        } else if (texte.equals("")) {
            return Alteration.NATUREL;
        } else if (texte.equals("#")) {
            return Alteration.DIESE;
        } else if (texte.equals("x")) {
            return Alteration.DOUBLEDIESE;
        } else {
            System.out.println("Erreur dans stringToAlteration");
            return Alteration.NATUREL;
        }


    }

    static public Hauteur getDo0() {
        return new Hauteur(0, Alteration.NATUREL);
    }

    static public Hauteur getRe0() {
        return new Hauteur(1, Alteration.NATUREL);
    }

    static public Hauteur getSol0() {
        return new Hauteur(4, Alteration.NATUREL);
    }

    /**
     * Crée un objet Hauteur avec l'élement element provenant d'un fichier MusicXML.
     * @param element
     */
    public Hauteur(Element element) {
        this(Integer.parseInt(element.getChild("octave").getText()) - 4,
                charForMusicXMLToNomNote(element.getChild("step").getText().charAt(0)),
                Alteration.getAlteration(getAlterationInteger(element.getChild("alter"))));

    }

    public Hauteur(int diatonic, int chromatic) {
        this(diatonic, Alteration.NATUREL);
        setAlteration(Alteration.getAlteration(chromatic - getNbDemiTonsParRapportAuDoCentral()));
    }

    public boolean isEgale(Hauteur autrehauteur) {
        return (numero == autrehauteur.getNumero()) && (alteration == autrehauteur.getAlteration());
    }

    public boolean isEgaleSansTenirCompteDeLAlteration(Hauteur autrehauteur) {
        return (numero == autrehauteur.getNumero());
    }

    public Intervalle getIntervalleDepuisDo0() {
        return new Intervalle(getDo0(), this);
    }

    /**
     *
     * @return retourne l'altération de la hauteur. Dièse, bémol etc.
     */
    public Alteration getAlteration() {
        return alteration;
    }

    public boolean isPlusGrave(Hauteur autrehauteur) {
        // ATTENTION !! fa bémol est plus grave que mi#.
        return (getNbDemiTonsParRapportAuDoCentral() <= autrehauteur.getNbDemiTonsParRapportAuDoCentral());
    }

    public boolean isStrictementPlusGrave(Hauteur autrehauteur) {
        return (getNbDemiTonsParRapportAuDoCentral() < autrehauteur.getNbDemiTonsParRapportAuDoCentral());
    }

    /**
     * définit l'altération de la hauteur à partir d'un entier difference. Cet entier
     * représente le nombre de demi-ton par rapport à la note naturelle. Par exemple,
     * si difference = 1, la note est diésée. Si la différence = 0, la note est naturelle (bécarre).
     * Si la différence = -1, la note est bémol.
     * @param difference
     */
    public void setAlteration(int difference) {
        alteration = Alteration.getAlteration(difference);

    }

    public void setAlteration(Alteration alterationNouvelle) {
        alteration = alterationNouvelle;
    }

    static private int getAlterationInteger(Element child) {
        if (child == null) {
            return 0;
        } else {
            return (int) Double.parseDouble(child.getText());
        }
    }

    /**
     * Change l'octave de la note. Par exemple si h est une hauteur qui représente sib2, si
     * on exécute h.setOctave(4) alors h sera un sib4.
     * @param octavenum
     */
    void setOctave(int octavenum) {
        numero = getNumeroModuloOctave() + 7 * octavenum;
    }

    /**
     * modifie le numéro de la note.
     * hnumero vaut 0 (do), 1 (ré).. 6 (si) et cette fonction change la hauteur
     * de façon à ce que la hauteur soit la note spécifiée mais sans changer l'octave
     * (cette fonction est utilisée pour la saisie au clavier)
     * @param hnumero
     */
    public void setNumeroModuloOctave(int hnumero) {
        numero = getOctave() * 7 + hnumero;
    }

    public boolean isInaudibleTropGrave() {
        return numero < -7*3-2;
    }


    public boolean isInaudibleTropAigu() {
        return numero > 7*4;
    }



    public enum Alteration {

        DOUBLEBEMOL(-2), BEMOL(-1), NATUREL(0), DIESE(1), DOUBLEDIESE(2);
        private int numero = 0;

        private Alteration(int numero) {
            this.numero = numero;
        }

        private String enTexte() {
            switch (numero) {
                case -2:
                    return "bb";
                case -1:
                    return "b";
                case 0:
                    return "";
                case 1:
                    return "#";
                case 2:
                    return "x";
                default:
                    return "erreur dans NoteNom";

            }
        }

        public int getNumero() {
            return this.numero;
        }

        public Element sauvegarder() {
            Element elementAlter = new Element("alter");
            elementAlter.addContent(String.valueOf(getNumero()));
            return elementAlter;
        }

        public static Alteration getAlteration(int numero) {
            switch (numero) {
                case -2:
                    return Alteration.DOUBLEBEMOL;
                case -1:
                    return Alteration.BEMOL;
                case 0:
                    return Alteration.NATUREL;
                case 1:
                    return Alteration.DIESE;
                case 2:
                    return Alteration.DOUBLEDIESE;
                default:
                    System.out.println("erreur dans Alteration.getAlteration(" + numero + ")");
                    return Alteration.NATUREL;

            }
        }
    }

    public enum NoteNom {

        DO(0), RE(1), MI(2), FA(3), SOL(4), LA(5), SI(6);
        private int numero = 0;

        public int getNumero() {
            return numero;
        }

        private NoteNom(int numero) {
            this.numero = numero;
        }
    }
    protected int numero = 0;
    protected Alteration alteration = Alteration.NATUREL;

    public Hauteur(int octavenum, NoteNom notenom, Alteration alteration) {
        this.numero = notenom.getNumero() + octavenum * 7;
        this.alteration = alteration;
    }

    public Hauteur(int numero, Alteration alteration) {
        this.numero = numero;
        this.alteration = alteration;
    }

    public int getNumero() {
        return numero;
    }

    public int getNumeroModuloOctave() {
        return Arithmetique.modulo(numero, 7);
    }

    private String getNomNoteString() {
        switch (Arithmetique.modulo(numero, 7)) {
            case 0:
                return "do";
            case 1:
                return "ré";
            case 2:
                return "mi";
            case 3:
                return "fa";
            case 4:
                return "sol";
            case 5:
                return "la";
            case 6:
                return "si";
            default:
                return "erreur dans NoteNom";

        }
    }

    private NoteNom intToNoteNom(int entier) {
        switch (entier) {
            case 0:
                return NoteNom.DO;
            case 1:
                return NoteNom.RE;
            case 2:
                return NoteNom.MI;
            case 3:
                return NoteNom.FA;
            case 4:
                return NoteNom.SOL;
            case 5:
                return NoteNom.LA;
            case 6:
                return NoteNom.SI;
            default: {
                System.out.println("Erreur dans intToNoteNom");
                return NoteNom.DO;
            }

        }
    }

    private int getNbDemiTonsEntreDoCentralEtNoteNom() {
        switch (getNoteNom()) {
            case DO:
                return 0;
            case RE:
                return 2;
            case MI:
                return 4;
            case FA:
                return 5;
            case SOL:
                return 7;
            case LA:
                return 9;
            case SI:
                return 11;
            default: {
                System.out.println("Erreur dans getNbDemiTonsEntreDoCentralEtNoteNom");
                return 0;

            }

        }
    }


    static private int getNumeroFromNbDemiTonsDepuisDo0DansOctave(int dansoctave)
    {
        switch(dansoctave)
        {
            case 0: case 1: return 0;
            case 2: case 3: return 1;
            case 4: return 2;
            case 5: case 6: return 3;
            case 7: case 8: return 4;
            case 9: case 10: return 5;
            case 11: return 6;
            default:
                return 0;
        }
    }


    
    static private int getNumeroFromNbDemiTonsDepuisDo0(int n)
    {
        final int octave = Arithmetique.division(n, 12);

        final int dansoctave = Arithmetique.modulo(n, 12);

        return octave * 7 + getNumeroFromNbDemiTonsDepuisDo0DansOctave(dansoctave);

    }



    private static Alteration getAlterationFromNbDemiTonsDepuisDo0(int n)
    {
        final int dansoctave = Arithmetique.modulo(n, 12);

        switch(dansoctave)
        {
            case 1: case 3: case 6: case 8:case 10: return Alteration.DIESE;
            default: return Alteration.NATUREL;
        }
    }


    
    public Hauteur(int nbDemiTons)
    {
        this(getNumeroFromNbDemiTonsDepuisDo0(nbDemiTons),
             getAlterationFromNbDemiTonsDepuisDo0(nbDemiTons));

        if(toString().startsWith("mi#"))
            Erreur.message("bizarre : Hauteur(int nbDemiTons) création d'un mi#");
    }



/**
 *
 * @return le numéro d'octave dans la notation française
 * ex: le do central ---> 3
 */
    public int getOctaveFrancais()
    {
        return getOctave() + 3;
    }


/**
 *
 * @return le numéro d'octave dans la notation anglaise
 * ex : le do central -----> 4
 */
    public int getOctaveAnglais()
    {
        return getOctave() + 4;
    }

    /**
     *
     * @return le numéro de l'octave. Le do central du piano (celui de la clef d'ut)
     * a un numéro d'octave = à 0. Pour les numéros d'octave spécifiques aux notations
     * musicales anglaises ou françaises, voir getOctaveFrancais() et getOctaveAnglais()
     */
    public int getOctave() {
        return Arithmetique.division(numero, 7);
    }

    /**
     *
     * @return le nom de la note. Si h représente un sib3 par exemple, h.getNoteNom()
     * renvoie NoteNom.SI.
     */
    public NoteNom getNoteNom() {
        return intToNoteNom(Arithmetique.modulo(numero, 7));
    }

    private int getNbDemiTonsDusOctave() {
        return getOctave() * 12;
    }

    private int getNbDemiTonsDusALAlteration() {
        return getAlteration().getNumero();

    }

    /**
     *
     * @return le nombre de demi-tons par rapport au do central. Exemple : si mi0, ça donne 4. sur fa0, ça donne 5.
     * sur fa#0, ça donne 6.
     */
    public int getNbDemiTonsParRapportAuDoCentral() {
        return getNbDemiTonsDusOctave()
                + getNbDemiTonsEntreDoCentralEtNoteNom()
                + getNbDemiTonsDusALAlteration();
    }

    @Override
    public String toString() {
        return getNomNoteString() + alteration.enTexte() + String.valueOf(getOctaveFrancais());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Hauteur) {
            Hauteur h = (Hauteur) o;
            return (h.getNumero() == getNumero()) && (getAlteration() == h.getAlteration());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.numero;
        hash = 71 * hash + (this.alteration != null ? this.alteration.hashCode() : 0);
        return hash;
    }

    /**
     *
     * @return un élément à écrire dans un fichier MusicXML qui représente cette hauteur.
     */
    Element sauvegarder() {
        Element elementHauteur = new Element("pitch");

        Element elementStep = new Element("step");
        elementStep.addContent(nomNoteToStringForMusicXML(getNoteNom()));

        Element elementOctave = new Element("octave");
        elementOctave.addContent(String.valueOf(getOctave() + 4));

        Element elementAlter = getAlteration().sauvegarder();

        elementHauteur.addContent(elementStep);
        elementHauteur.addContent(elementOctave);
        elementHauteur.addContent(elementAlter);

        return elementHauteur;


    }

    
    public int getNumeroMIDI()
    {
        return 12*5 + getNbDemiTonsParRapportAuDoCentral();
        
    }

    @Override
    public Hauteur clone()
    {
        return new Hauteur(numero, alteration);
    }
}
