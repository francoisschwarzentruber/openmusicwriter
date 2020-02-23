/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees;

import org.jdom.Element;

/**
 * représente une durée
 * @author François Schwarzentruber
 */
public class Duree {

    static final public long divisionsStandard = 480 * 7 * 3;

    public static Duree getDureeNoire() {
        return new Duree(new Rational(1, 1));
    }
    
    public static Duree getDureeCroche() {
        return new Duree(new Rational(1, 2));
    }

    public static Duree getDureeNulle() {
        return new Duree(new Rational(0, 1));
    }

    public static Duree getDureeRonde() {
        return new Duree(new Rational(4, 1));
    }

    public static Duree getDureeBlanche() {
        return new Duree(new Rational(2, 1));
    }

    public static Duree getDureeDoubleCroche() {
        return new Duree(new Rational(1, 4));
    }

    public static Duree getDureeTripleCroche() {
        return new Duree(new Rational(1, 8));
    }

    public static Duree getDureeQuadrupleCroche() {
        return new Duree(new Rational(1, 16));
    }



    
    private Rational r = null;
    private boolean vague = false; // dit si la durée est bien fixée ou non

    public Duree(Moment debutMoment, Moment finMoment) {
        r = finMoment.getRational().moins(debutMoment.getRational());
    }

    public Duree(Rational r) {
        this.r = r;
    }

    public Duree(long nbMilliSecondes, int nbNoiresParMinute) {
        this(new Rational(400 * nbMilliSecondes * nbNoiresParMinute / (60 * 1000), 400));
    }

    public Duree(Duree dureeMesure, int nombreDeFois) {
        this(new Rational(dureeMesure.getRational().getNumerateur() * nombreDeFois, dureeMesure.getRational().getDenominateur()));
    }

    /**
     *
     * @param debutMoment
     * @return le moment qui est après debutMoment quand on a attendu la durée
     */
    public Moment getFinMoment(Moment debutMoment) {
        return new Moment(debutMoment.getRational().plus(r));
    }

    /**
     *
     * @param finMoment
     * @return le moment qui, en attendant la durée, on est à finMoment
     */
    public Moment getDebutMoment(Moment finMoment) {
        return new Moment(finMoment.getRational().moins(r));
    }

    /**
     *
     * @param nbNoiresEnUneMinute
     * @return le nombre de millisecondes à attendre sachant le nombre de noires
     * en une minute
     */
    public int getNbMilliSecondes(int nbNoiresEnUneMinute) {
        double dureeNoire = 60 * 1000 / nbNoiresEnUneMinute;
        return (int) (r.getRealNumber() * dureeNoire);
    }

    public Rational getRational() {
        return r;
    }

    public boolean isDeuxiemePetitPoint() {
        return r.getNumerateur() == 7;
    }

    /**
     *
     * @return true ssi la durée est vague, approximative
     */
    public boolean isVague() {
        return vague;
    }

    /**
     * Prévient que cette durée est approximative, vague. (nécessaire pour l'enregistrement
     * pour dire qu'il y a des imprécisions d'enregistrements, de délai informatique,
     * de mécompréhension de l'interprétation de l'exécutant)
     */
    public void setVague() {
        vague = true;
    }

    boolean isEgal(Duree duree) {
        return r.isEgal(duree.getRational());
    }

    public boolean isPremierPetitPoint() {
        return (r.getNumerateur() == 3) || (r.getNumerateur() == 7);
    }

    public boolean isZero() {
        return r.isZero();
    }

    public boolean isStrictementInferieur(Duree d) {
        return this.getRational().isStrictementInferieur(d.getRational());
    }

    public boolean isSuperieur(Duree d) {
        return this.getRational().isSuperieur(d.getRational());
    }

    public boolean isInferieur(Duree d) {
        return this.getRational().isInferieur(d.getRational());
    }

    public boolean isStrictementSuperieur(Duree d) {
        return this.getRational().isStrictementSuperieur(d.getRational());
    }

    /**
     *
     * @return la représentation d'une durée en MusicXML
     */
    public Element sauvegarder() {
        Element element = new Element("duration");
        element.addContent(String.valueOf(Math.round(divisionsStandard * r.getRealNumber())));
        return element;
    }

    /**
     *
     * @return le nom stocké en MusicXML de la durée.
     */
    public String musicXMLgetType() {
        if (isSuperieur(new Duree(new Rational(4, 1)))) {
            return "whole";
        } else if (isSuperieur(new Duree(new Rational(2, 1)))) {
            return "half";
        } else if (isSuperieur(new Duree(new Rational(1, 1)))) {
            return "quarter";
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 4)))) {
            return "eighth";
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 8)))) {
            return "16th";
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 16)))) {
            return "32th";
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 32)))) {
            return "64th";
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 64)))) {
            return "128th";
        } else {
            return null;
        }

    }

    /**
     *
     * @param element
     * @return la valeur entière qui se trouve DANS l'élément XML element
     */
    private static int elementGetInteger(Element element) {
        if (element == null) {
            System.out.println("erreur d'ouverture de durée dans le fichier XML");
            return 1;
        } else {
            return (int) Double.parseDouble(element.getValue());
        }
    }

    public Duree(int divisions, Element element) {
        this(new Rational(elementGetInteger(element), divisions));
    }

    @Override
    public String toString() {
        return r.toString();
    }

    /**
     *
     * @return un élément MusicXML qui permet de revenir en arrière de cette durée
     *
     */
    public Element getElementBackUp() {
        Element element = new Element("backup");
        element.addContent(sauvegarder());
        return element;
    }

    /**
     *
     * @return un élément MusicXML qui permet d'avancer en avant de cette durée
     */
    public Element getElementForward() {
        Element element = new Element("forward");
        element.addContent(sauvegarder());
        return element;
    }

    /**
     *
     * @return le nombre de traits qu'il faut pour représenter une note qui a cette durée
     */
    public int getNombreTraitsCroche() {
        if(isEgal(new Duree(new Rational(2, 3))))
            return 0;
        else
        if (isZero()) {
            return 0;
        } else if (isSuperieur(new Duree(new Rational(1, 1)))) {
            return 0;
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 4)))) {
            return 1;
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 8)))) {
            return 2;
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 16)))) {
            return 3;
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 32)))) {
            return 4;
        } else if (isStrictementSuperieur(new Duree(new Rational(1, 64)))) {
            return 5;
        } else {
            return 6;
        }

    }

    public Duree moins(Duree duree) {
        return new Duree(getRational().moins(duree.getRational()));
    }
    
    public Duree plus(Duree duree) {
        return new Duree(getRational().plus(duree.getRational()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Duree) {
            return isEgal((Duree) obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.r != null ? this.r.hashCode() : 0);
        return hash;
    }

    public Duree multiplier(double lambda) {
        return new Duree(getRational().multiplier(new Rational((int) (lambda * 256), 256)));
    }

    public Duree multiplier(Rational lambda) {
        return new Duree(getRational().multiplier(lambda));
    }

    public Duree getDureeDiviserParDeux() {
        return new Duree(getRational().getRationalDiviserParDeux());
    }

    public boolean isPositive() {
        return getRational().isPositive();
    }


}
