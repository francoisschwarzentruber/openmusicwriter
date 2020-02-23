/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Un accord est un ensemble de notes. Il est calculé avant l'affichage en prenant
 * l'ensemble des notes de la partition qui tombe en même temps et qui sont dans
 * la même voix.
 * @author François Schwarzentruber
 */
public class Accord extends ElementMusicalSurVoix implements Iterable<Note> {
    /**
     * stocke les notes classés de la plus grave à la plus aigu (dans une meme portee)
     * et sinon si pas dans une même portée, d'abord les notes les plus en bas (numéro de portée les plus grands)
     */
    private final SortedSet<Note> notes = new TreeSet<Note>(new ComparatorNoteHauteurEtPortee());


    /**
     *
     * @return un "itérateur"... ça c'est pour pouvoir faire :
     * for(Note note : accord)
     * {
     *   ...
     * }
     */
    @Override
    public Iterator<Note> iterator() {
        return notes.iterator();
    }


    /**
     *
     * @param note
     * @return vrai ssi la note est dans l'accord
     */
    public boolean contains(Note note) {
        for(Note accordNote : notes)
        {
            if(accordNote == note)
                return true;
        }
        return false;
        //return notes.contains(note);
    }
    
    
    
     
     
     /**
      * construit un accord avec qu'une seule note
      * @param note
      */
     public Accord(Note note)
     {
         super(note.getDebutMoment(), note.getDuree(), note.getPortee());
         ajouterNote(note);
     }
     
     /**
      * Ajoute la note note à l'accord
      * @param note
      */
     final public void ajouterNote(Note note)
     {
             notes.add(note);
     }
     

     /**
      *
      * @return l'ensemble des notes qui sont dans l'accord
      */
     public Set<Note> getNotes()
     {
         return notes;
     }
     
     
     /**
      *
      * @return la note la plus haute (note la plus aigu et dans la portée dont
      * le numéro est le plus petit, ie. le plus en haut)
      */
     public Note noteLaPlusHaute()
     {
         return notes.last();
     }



     /**
      * 
      * @return retourne la plus basse (en terme de sonorité...
      * ça correspond à la note qui aura le y le plus grand)
      * (ça regarde aussi les portées... et ça prend la note sur la portée
      * la plus en dessous)
      */
     public Note noteLaPlusBasse()
     {
         return notes.first();
     }
     
     
     @Override
     public Duree getDuree()
     {
         return notes.first().getDuree();
     }

    @Override
    /**
     * modifier la durée d'un accord c'est modifier la durée de toutes les notes qui le composent
     */
    public void setDuree(Duree duree) {
        super.setDuree(duree);
        for(Note n : this)
        {
            n.setDuree(duree);
        }
    }
     
     
     
     
    @Override
     public Voix getVoix()
     {
         return notes.first().getVoix();
     }
     
     /**
      *
      * @param note
      * @return true ssi il existe une note juste avant (exemple :
      * note = mi2 et il y a un ré2 dans l'accord
      */
     public boolean isNoteHautDessusNoteMoinsUn(Note note)
     {
         SortedSet<Note> noteavant = notes.headSet(note);
         
         if(noteavant.size() == 0)
             return false;
         else
         {
             Note notejusteavant = noteavant.last();
             return (notejusteavant.getPortee() == note.getPortee())
                     && (notejusteavant.getHauteur().getNumero() == note.getHauteur().getNumero() - 1);
         }
         
     }


/**
 *
 * @param parties
 * @return true ssi toutes les notes sont dans les parties
 */
    @Override
    public boolean isSurParties(Collection<Partie> parties) {
        for(Note note : getNotes())
        {
            if(!note.isSurParties(parties))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for(Note n : notes)
        {
            s = s + " " + n.toString();
        }
        return s;
    }
     
    
    
    
    
}
