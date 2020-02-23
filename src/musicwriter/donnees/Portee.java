/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.TreeMap;
import org.jdom.Element;

/**
 *
 * @author proprietaire
 */
public class Portee {

    private final TreeMap<Moment, ElementMusicalClef> elementsMusicauxClefs
            = new TreeMap<Moment, ElementMusicalClef>();

    private final TreeMap<Moment, Octaviation> octaviations
            = new TreeMap<Moment, Octaviation>();


    
    private final Partie partie;
    

    public int getCoordonneeVerticaleSansOctaviation(Moment debutMoment, Hauteur hauteur) {
        ElementMusicalClef clef = getElementMusicalClef(debutMoment);
        if(clef != null)
        {
            return clef.getCoordonneeVerticale(hauteur);
        }
        else
           return Clef.ClefDeSol.getCoordonneeVerticale(hauteur);


    }


    public int getCoordonneeVerticale(Moment debutMoment, Hauteur hauteur) {
        Octaviation octaviation = getOctaviation(debutMoment);
        if(octaviation == null)
            return getCoordonneeVerticaleSansOctaviation(debutMoment, hauteur);
        else
            return octaviation.getCoordonneeVerticaleReelle(getCoordonneeVerticaleSansOctaviation(debutMoment, hauteur));
        
    }





    public Hauteur getHauteur(Moment moment, int coordonneeVerticale) {
        final ElementMusicalClef clef = getElementMusicalClef(moment);
        final Hauteur hauteur;
        
        if(clef != null)
        {
            hauteur =  clef.getHauteur(coordonneeVerticale);
        }
        else
        {
            hauteur = Clef.ClefDeSol.getHauteur(coordonneeVerticale);
        }

        
        Octaviation octaviation = getOctaviation(moment);
         if(octaviation == null)
            return hauteur;
        else
            return octaviation.getHauteurReelle(hauteur);
        
    }

    
    /**
     * définit le numéro d'une portée au sein d'une partie
     * @param numero 
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Partie getPartie() {
        return this.partie;
    }



    public void clefSupprimer(ElementMusicalClef elementClef) {
        elementsMusicauxClefs.remove(elementClef.getDebutMoment());
    }


    public void octaviationSupprimer(Octaviation elementOctaviation) {
        octaviations.remove(elementOctaviation.getDebutMoment());
    }

    public void clefAjouter(ElementMusicalClef elementClef) {
        elementsMusicauxClefs.put(elementClef.getDebutMoment(), elementClef);
    }

    public void octaviationAjouter(Octaviation octaviation) {
        octaviations.put(octaviation.getDebutMoment(), octaviation);
    }
    


    public Clef getClef(Moment moment)
    {
        ElementMusicalClef elementMusicalClef = getElementMusicalClef(moment);

        if(elementMusicalClef == null)
            return Clef.ClefDeSol;
        else
        {
            return elementMusicalClef.getClef();
        }
    }


    /**
     *
     * @param moment
     * @return retourne l'élement musical clef qui "gouverne" au moment moment.
     * L'élement musical n'est pas forcément placé au moment moment, mais cette fonction
     * retourne la plus proche clef qui précède le moment moment.
     */
    public ElementMusicalClef getElementMusicalClef(Moment moment) {
        moment = elementsMusicauxClefs.floorKey(moment);

        if(moment == null)
            return null;
        else
        {
            return elementsMusicauxClefs.get(moment);
        }
    }



    /**
     *
     * @param moment
     * @return retourne l'élement musical clef qui "gouverne" au moment moment.
     * L'élement musical n'est pas forcément placé au moment moment, mais cette fonction
     * retourne la plus proche clef qui précède le moment moment.
     */
    public Octaviation getOctaviation(Moment moment) {
        moment = octaviations.floorKey(moment);

        if(moment == null)
            return null;
        else
        {
            return octaviations.get(moment);
        }
    }

    public Hauteur getHauteurSilenceStandard(Moment moment) {
        return getHauteur(moment, 2);
    }



    




    public enum Clef {
        ClefDeSol(-6), ClefDeFa(6), ClefDUt(0);


        



         Clef(int decalageEntreHauteurNumeroEtCoordonneeVerticale)
         {
            this.decalageEntreHauteurNumeroEtCoordonneeVerticale
                    = decalageEntreHauteurNumeroEtCoordonneeVerticale;
        }




        private final int decalageEntreHauteurNumeroEtCoordonneeVerticale;
      
        
        
        
        
        public int getCoordonneeVerticale(Hauteur hauteur) {
            return hauteur.getNumero() + decalageEntreHauteurNumeroEtCoordonneeVerticale;
            
            // ex : un do avec une clef de sol
            // do => hauteur.getNumero() = 0
            //clef de sol => decalageEntreHauteurNumeroEtCoordonneeVerticale = -6
            // ==> on écrit sur la coordonnée verticale = -6.
        }
        
        
        



        /**
         *
         * @param coordonneeVerticale
         * @return Par exemple, si clefSol désigne la clef de sol,
         * clefSol.getHauteur(0) donne si0.
         * clefSol.getHauteur(2) donne ré1.
         */
        public Hauteur getHauteur(int coordonneeVerticale)
        {
            return new Hauteur(coordonneeVerticale - decalageEntreHauteurNumeroEtCoordonneeVerticale, 
                               Hauteur.Alteration.NATUREL);
        }
        
        

        


       
        
        public Element sauvegarder()
        {
            Element elementClef = new Element("clef");
            
            switch(decalageEntreHauteurNumeroEtCoordonneeVerticale)
            {
                    case -6:   elementClef.addContent("clefDeSol");
                               break;
                               
                    case 6:   elementClef.addContent("clefDeFa");
                               break;           
                               
                default:
                    System.out.println("erreur dans Clef.sauvegarder()");
            }
            
            return elementClef;
            
        }
        
        
    }
    
    /**pas propre ces histoires de numéro*/
    
    private int numero = 0;
    private int numeroAffichage = 0;

    
    /**
     * 
     * @return le numéro de la portée au sein de la partition
     */
    public int getNumeroAffichage() {
        return numeroAffichage;
    }

    
    /**
     * définit le numéro de la portée au sein de la partition
     * @param numeroAffichage 
     */
    public void setNumeroAffichage(int numeroAffichage) {
        this.numeroAffichage = numeroAffichage;
    }
    
    public Portee(Partie partie)
    {
        this.numero = 0;
        this.partie = partie;
        
    }
    
    
    /**
     * 
     * @return le numéro de la portée au sein de la partie
     */
    public int getNumero()
    {
        return numero;
    }
    
    


    /**
     *
     * @return un élément XML qui contient <staff> i <\staff> où i est le numéro de la portée
     */
    public Element sauvegarderNumero()
    {
        Element elementPorteeNumero = new Element("staff");
        elementPorteeNumero.addContent(String.valueOf(numero));
        return elementPorteeNumero;
    }
    
    
    

    
    
    
}
