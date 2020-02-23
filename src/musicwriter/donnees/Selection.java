/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author proprietaire
 */
public class Selection extends ElementsMusicauxEnsemble implements Iterable<ElementMusical>, Cloneable, SelectionLecture {

    /**
     * renvoie la durée maximale des éléments de els qui ont une durée
     * si els ne contient pas d'éléments avec des durées, alors
     * renvoie 0/1
     * @param els
     */
    public static Duree getDureeMaximale(Set<ElementMusical> els) {
        Duree duree = new Duree(new Rational(0, 1));

        for(ElementMusical el : els)
        {
            if(el instanceof ElementMusicalDuree)
            {
                ElementMusicalDuree d = (ElementMusicalDuree) el;
                if(d.getDuree().isStrictementSuperieur(duree))
                    duree = d.getDuree();
            }
        }
        return duree;
    }

    /**
     * Crée un objet Selection vide.
     */
    public Selection() {}

    /**
     * crée une sélection avec un seul élément musical : elementMusical
     * @param elementMusical
     */
    public Selection(ElementMusical elementMusical) {
        elementMusicalAjouter(elementMusical);
    }

    /**
     * crée une sélection qui contient exactement tous les éléments musicaux
     * présents dans l'ensemble elementsMusicaux
     * @param elementsMusicaux
     */
    public Selection(Set<ElementMusical> elementsMusicaux) {
        this();

        for(ElementMusical el : elementsMusicaux)
        {
            elementMusicalAjouter(el);
        }
    }



    public Iterator<ElementMusical> iterator() {
        return getElementsMusicaux().iterator();
    }
    


    /**
     *
     * @return l'élément musical qui se trouve dans la sélection si la sélection contient
     * un unique élément
     * Si tel n'est pas le cas, renvoie le "premier élément" du tableau (un peu moche)
     * Si c'est vide, ça plante ! :)
     */
    public ElementMusical getElementMusicalUnique()
    {
        return getElementsMusicaux().iterator().next();
    }



    public boolean contains(ElementMusical element) {
        return getElementsMusicaux().contains(element);
    }



    /**
     * 
     * @return un élément musical placé au début
     */
    private ElementMusical getElementMusicalDebut()
    {
        return getElementsMusicauxQuiCommencent(getMomentDebut()).iterator().next();
    }




    private ElementMusical getElementMusicalFin() {
        return getElementsMusicauxQuiCommencent(getMomentFin()).iterator().next();
    }





/**
 *
 * @return un curseur d'un élement musical qui est le plus à gauche dans le temps
 */
    public Curseur getCurseurDebut()
    {
        return getElementMusicalDebut().getCurseur();
    }




/**
 *
 * @return la durée de la sélection avec la durée des derniers éléments compris
 * exemple : sélection = une noire renvoie une durée de 1
 */
    public Duree getDureeTotale()
    {
        return new Duree(getMomentDebut(), getFinMomentAvecDuree());
    }


    public Iterable<ElementMusicalSurPortee> getElementsMusicauxSurPortee() {
        ArrayList<ElementMusicalSurPortee> N = new ArrayList<ElementMusicalSurPortee>();
        
        for(ElementMusical el : getElementsMusicaux())
        {
            if(el instanceof ElementMusicalSurPortee)
            {
                N.add((ElementMusicalSurPortee) el);
            }
        }
        
        return N;
    }

    public Iterable<Note> getNotes() {
        ArrayList<Note> N = new ArrayList<Note>();
        
        for(ElementMusical el : getElementsMusicaux())
        {
            if(el instanceof Note)
            {
                N.add((Note) el);
            }
        }
        
        return N;
        
    }

    

    /**
     *
     * @return un clone de la sélection, c'est à dire où TOUS LES élements musicaux
     * sont clonés
     */
    @Override
    public Selection clone()
    {
        Selection nouvelleSelection = new Selection();
        
        for(ElementMusical el : getElementsMusicaux())
        {
            nouvelleSelection.elementMusicalAjouter((ElementMusical) el.clone());
        }
        return nouvelleSelection;
        
    }


    

    public void ajouterSelection(Set<ElementMusical> E) {
        for(ElementMusical el : E)
        {
            elementMusicalAjouter(el);
        }
    }


    /**
     *
     * @param hauteur
     * @return la sélection qui contient l'ensemble des notes qui ont la même hauteur
     * que hauteur modulo l'altération. On ne tient pas compte de l'altération.
     * Par exemple, getSelectionMemeHauteur(do1) retourne tous les do
     * qui sont dans la sélection (des do1, des do3b, des do-3, des do#2 etc.)
     */
    public Selection getSelectionMemeHauteur(Hauteur hauteur) {
        Selection nouvelleSelection = new Selection();

        for(ElementMusical el : getElementsMusicaux())
        {
            if(el instanceof Note)
            {
                Note note = (Note) el;

                if(note.getHauteur().getNumeroModuloOctave() == hauteur.getNumeroModuloOctave())
                      nouvelleSelection.elementMusicalAjouter(el);
            }
            
        }
        return nouvelleSelection;
    }


    /**
     *
     * @return true ssi la sélection ne contient qu'un seul élément
     */
    public boolean isSingleton() {
        return getElementsMusicaux().size() == 1;
    }


    /**
     *
     * @return le nombre d'éléments musicaux qu'il y a dans la sélection
     */
    public int getElementsMusicauxNombre() {
        return getElementsMusicaux().size();
    }


    /**
     * modifie le moment de début de la sélection, c'est à dire... que ça déplace
     * tous les élements musicaux de façon à ce que le début (les premiers éléments musicaux de la sélection)
     * débute au moment debutMomentNouveau
     * (utilisé pour le presse-papier lors du collage)
     * @param debutMomentNouveau
     */
    public void setDebutMoment(Moment debutMomentNouveau) {
        Duree duree = new Duree(getMomentDebut(), debutMomentNouveau);
        
        for(ElementMusical el : getElementsMusicaux())
        {
            elementMusicalDeplacer(el, duree.getFinMoment(el.getDebutMoment()));
        }

    }

    public Curseur getCurseurFin() {
        return getElementMusicalFin().getCurseur();
    }



    





}
