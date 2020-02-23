/*
 * Un Curseur c'est un endroit dans la partition
 */

package musicwriter.donnees;

/**
 * Cette classe représente une position dans la partition.
 * @author proprietaire
 */
public class Curseur implements Cloneable {
    private final Moment moment;
    private final Hauteur hauteur;
    private final Portee portee;
    private final Voix voix;

    /**
     * si mis à null ===> pas d'élement musical sous le curseur
     * sinon ==> c'est l'élement sous lequel est le curseur
     */
    private final ElementMusical element;


    
    /**
     * Construit un curseur (ie un objet qui dit où on se trouve dans la
     * partition).
     * @param moment
     * @param hauteur
     * @param portee
     */
    public Curseur(Moment moment, Hauteur hauteur, Portee portee)
    {
        this.moment = moment;
        this.hauteur = hauteur;
        this.portee = portee;
        this.voix = new Voix();
        this.element = null;
    }

    /**
     * Construit un curseur (ie un objet qui dit où on se trouve dans la
     * partition).
     * @param moment
     * @param hauteur
     * @param portee
     * @param voix
     */
    public Curseur(Moment moment, Hauteur hauteur, Portee portee, Voix voix)
    {
        this.moment = moment;
        this.hauteur = hauteur;
        this.portee = portee;
        this.voix = voix;
        this.element = null;
    }


    /**
     * Construit un curseur (ie un objet qui dit où on se trouve dans la
     * partition).
     * @param moment
     * @param hauteur
     * @param portee
     * @param voix
     */
    public Curseur(Moment moment, Hauteur hauteur, Portee portee, Voix voix, ElementMusical el)
    {
        this.moment = moment;
        this.hauteur = hauteur;
        this.portee = portee;
        this.voix = voix;
        this.element = el;
    }
    
    /**
     *
     * @return retourne la hauteur (do#3, ou réb4 etc.) spécifiée par le curseur.
     */
    public Hauteur getHauteur() {
        return hauteur;
    }

    public Moment getMoment() {
        return moment;
    }

    public Portee getPortee() {
        return portee;
    }

/**
 *
 * @return la hauteur standard qu'a un silence s'il n'y a rien d'autres
 * sur la portée que lui
 */
    public Hauteur getHauteurSilenceStandard()
    {
        return portee.getHauteurSilenceStandard(getMoment());
    }

    /**
     *
     * @return la partie (ie l'instrument) dans laquelle on est 
     */
    public Partie getPartie()
    {
        return portee.getPartie();
    }

//    public void declarerSurElementMusical(ElementMusical n) {
//        element = n;
//    }
    
    
    public boolean isSurElementMusical()
    {
        return (element != null);
    }

    public boolean isSurNote()
    {
        return (element != null) & (element instanceof Note);
    }



    public ElementMusical getElementMusical() {
        return element;
    }


    public Note getNote()
    {
        return (Note) element;
    }
    

    public int getCoordonneeVerticaleSurPortee()
    {
         return portee.getCoordonneeVerticale(moment, hauteur);
    }
    
    
    public Voix getVoix()
    {
        return this.voix;
    }


    /**
     *
     * @param duree
     * @return le curseur qui a avancé dans la partition de la durée duree. Tous les autres
     * paramètres (hauteur, portée, voix etc.) n'ont pas changé. Seul le moment est désormais avancé.
     */
    public Curseur getCurseurSuivant(Duree duree) {
        return new Curseur(duree.getFinMoment(getMoment()), getHauteur().clone(), getPortee(), getVoix());
    }



    


    /**
     *
     * @param autreMoment
     * @return le même curseur sauf que le moment est autreMoment
     */
    public Curseur getCurseurAutreMoment(Moment autreMoment)
    {
        return new Curseur(autreMoment, getHauteur().clone(),
                           getPortee(), getVoix());
    }


    /**
     *
     * @param autreHauteur
     * @return le même curseur sauf que la hauteur est autreHauteur
     */
    public Curseur getCurseurAutreHauteur(Hauteur autreHauteur)
    {
        return new Curseur(getMoment(), autreHauteur,
                           getPortee(), getVoix());
    }


    /**
     *
     * @param hnumero
     * @return le curseur qui pointe désormais sur la note (hnumero = 0..6 pour respectivement do à si) qui se trouve
     * dans la même octave. Tous les autres paramètres sont inchangés (hauteur, portée, voix).
     */
    public Curseur getCurseurNoteModuloOctave(int hnumero) {
        return new Curseur(getMoment(), new Hauteur(getHauteur().getOctave()*7 + hnumero, getHauteur().getAlteration()),
                           getPortee(), getVoix());
    }




    public Curseur getCurseurDeplaceRelatif(Duree duree, Intervalle intervalle) {
        return new Curseur(duree.getFinMoment(getMoment()),
                               intervalle.getHauteur2(getHauteur()),
                               getPortee(),
                               getVoix());
    }
    
    public Curseur getCurseurDeplaceRelatif(Duree duree, Intervalle intervalle, Portee portee) {
        return new Curseur(duree.getFinMoment(getMoment()),
                               intervalle.getHauteur2(getHauteur()),
                               portee,
                               getVoix());
    }



    static private Portee getPorteeNouvelleDeplacement(PartitionDonnees partitionDonnees, Portee portee, int porteeChangement)
    {
        if(portee == null)
        {
            return null;
        }
        else
        {
             int debutPorteeNumero = portee.getNumeroAffichage();
            int finPorteeNumero = debutPorteeNumero + porteeChangement;

            if(finPorteeNumero < 0)
            {
                finPorteeNumero = 0;
            }
            else if (finPorteeNumero > partitionDonnees.getPorteesNombre()-1)
            {
                finPorteeNumero = partitionDonnees.getPorteesNombre()-1;
            }
            return partitionDonnees.getPortee(finPorteeNumero);
        }

    }


    
    public Curseur getCurseurDeplaceRelatif(Duree duree, Intervalle intervalle, PartitionDonnees partitionDonnees, int porteeChangement) {
        return new Curseur(duree.getFinMoment(getMoment()),
                               intervalle.getHauteur2(getHauteur()),
                               getPorteeNouvelleDeplacement(partitionDonnees, getPortee(), porteeChangement),
                               getVoix());
    }



    @Override
    public Curseur clone() {
        return new Curseur(getMoment(), getHauteur().clone(), getPortee(), getVoix());
    }

    /**
     * 
     * @return la hauteur standard des silences (au milieu de la portée) si la hauteur du curseur
     * est proche du centre de la portée.
     * Sinon, retourne la hauteur du curseur
     * 
     * Cette fonction est utilisé pour insérer un silence :
     * - pour pouvoir insérer un silence sans galérer et qu'il soit bien placer
     * - tout en ayant la possibilité de l'insérer loin
     */
    public Hauteur getHauteurSilenceStandardSiProche() {
        Intervalle i = new Intervalle(getHauteur(), getHauteurSilenceStandard());
        
        if(Math.abs(i.getNumero()) < 4)
        {
            return getHauteurSilenceStandard();
        }
        else
        {
            return getHauteur();
        }
        
    }

    public boolean isMemePorteeMemeMoment(Curseur curseur) {
        return curseur.getMoment().equals(getMoment()) && curseur.getPortee().equals(getPortee());
    }

    @Override
    public String toString() {
        return getMoment().toString();
    }



    


    

}
