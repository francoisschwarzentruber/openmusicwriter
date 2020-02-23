/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import musicwriter.donnees.Accord;
import musicwriter.donnees.Partie;
import musicwriter.donnees.Voix;

/**
 * Cette objet représente l'ensemble des éléments musicaux concernées par un certain moment
 * @author proprietaire
 */
public class MomentElementsMusicauxSurLeFeu {
    private final Set<ElementMusical> tabElementsMusicauxQuiDebutent = new HashSet<ElementMusical>();
    private final Set<ElementMusicalDuree> tabElementsMusicauxQuiPerdurentStrictement = new HashSet<ElementMusicalDuree>();
    private final Set<ElementMusicalDuree> tabElementsMusicauxQuiSontRelachees = new HashSet<ElementMusicalDuree>();
    
    private final Moment moment;
    
    
    MomentElementsMusicauxSurLeFeu(Moment moment)
    {
        this.moment = moment;
    }
    
    public Set<ElementMusical> getElementsMusicauxAJouer() {
        return tabElementsMusicauxQuiDebutent;
    }
    
    
    public Set<ElementMusical> getElementsMusicauxRassemblesEnAccordsAJouer() {
        Set<ElementMusical> els = new HashSet<ElementMusical>();
        
        Map<Partie, Map<Voix, Accord>> accords = new HashMap<Partie, Map<Voix, Accord> >();
        
        for(ElementMusical el : tabElementsMusicauxQuiDebutent)
        {
             if(el instanceof Note)
             {
                 Note note = (Note) el;

                 if(accords.containsKey(note.getPartie()))
                 {
                     Map<Voix, Accord> accordsPartie = accords.get(note.getPartie());
                     accordsPartie.containsKey(note.getVoix());
                     if(accordsPartie.containsKey(note.getVoix()))
                     {
                         accordsPartie.get(note.getVoix()).ajouterNote(note);
                     }
                     else
                     {
                         accordsPartie.put(note.getVoix(), new Accord(note));
                     }
                 }
                 else
                 {
                     Map<Voix, Accord> accordsPartie = new HashMap<Voix, Accord>();
                     
                     accords.put(note.getPartie(), accordsPartie);
                     accordsPartie.put(note.getVoix(), new Accord(note));
                 }
                 
                 
             }
             else
                 els.add(el);
        }
        
        for( Map<Voix, Accord> accordsPartie : accords.values())
        {
            els.addAll(accordsPartie.values());
        }
        
        return els;
    }

    Set<ElementMusicalDuree> getElementsMusicauxAvecDureeConcernees() {
        
        Set<ElementMusicalDuree> s = getElementsMusicauxDureeAJouer();
        s.addAll(getElementsMusicauxQuiPerdurent());
        return s;
    }
    
    
    private Set<ElementMusicalDuree> getElementsMusicauxDureeAJouer() {
        Set<ElementMusicalDuree> N = new HashSet<ElementMusicalDuree>();
        
        for(ElementMusical el : tabElementsMusicauxQuiDebutent)
        {
            if(el instanceof ElementMusicalDuree)
            {
                N.add((ElementMusicalDuree) el);
            }
        }
        return N;
    }
    
    
    
    public Set<Note> getNotesAJouer() {
        Set<Note> N = new HashSet<Note>();
        
        for(ElementMusical el : tabElementsMusicauxQuiDebutent)
        {
            if(el instanceof Note)
            {
                N.add((Note) el);
            }
        }
        return N;
    }

    public Set<ElementMusicalDuree> getElementsMusicauxQuiPerdurent() {
        return tabElementsMusicauxQuiPerdurentStrictement;
    }
    
    
    public Set<Note> getNotesQuiPerdurent() {
        Set<Note> N = new HashSet<Note>();
        
        for(ElementMusical el : tabElementsMusicauxQuiPerdurentStrictement)
        {
            if(el instanceof Note)
            {
                N.add((Note) el);
            }
        }
        return N;
    }
    
    public Set<Note> getNotesQuiSontRelachees() {
        Set<Note> N = new HashSet<Note>();
        
        for(ElementMusical el : tabElementsMusicauxQuiSontRelachees)
        {
            if(el instanceof Note)
            {
                N.add((Note) el);
            }
        }
        return N;
    }
    
    
    public Set<ElementMusicalDuree> getElementsMusicauxQuiSontRelachees() {
        return tabElementsMusicauxQuiSontRelachees;
    }
    
    /**
     * ajoute un élément musicaux pour dire qu'il débute au moment en question
     * @param elementMusical
     */
    public void elementMusicalQuiDebuteAjouter(ElementMusical elementMusical)
    {
        tabElementsMusicauxQuiDebutent.add(elementMusical);
    }
    
    
    public void elementMusicalQuiPerdureStrictementAjouter(ElementMusicalDuree elementMusical)
    {
        tabElementsMusicauxQuiPerdurentStrictement.add(elementMusical);
    }

    public Moment getMoment() {
        return moment;
    }

    
    public void elementMusicalQuiEstRelacheAjouter(ElementMusicalDuree elementMusical) {
        tabElementsMusicauxQuiSontRelachees.add(elementMusical);
    }



    public Duree getElementsMusicauxQuiDebutentDureeMinimum() {
        Set<ElementMusicalDuree> T = getElementsMusicauxDureeAJouer();
        
        if(T.isEmpty())
            return new Duree(new Rational(0, 1));
        
        Duree d = new Duree(new Rational(100, 1));
        for(ElementMusicalDuree el : T)
        {
            if(d.getRational().isSuperieur(el.getDuree().getRational()))
            {
                d = el.getDuree();
            }
            
        }
        return d;
    }


    public ElementMusicalSurVoix getElementMusicalSurVoix(Voix voix) {
        for(ElementMusical el : getElementsMusicauxRassemblesEnAccordsAJouer())
        {
            if(el instanceof Accord)
            {
                Accord accord = (Accord) el;
                if(accord.getVoix().equals(voix))
                    return accord;
            }
        }
        return null;
    }


    
    public void filter(Collection<Partie> parties) {
        for(Iterator<ElementMusical>it = tabElementsMusicauxQuiDebutent.iterator() ; it.hasNext(); )
        {
           ElementMusical el = it.next();
           if(!el.isSurParties(parties))
           {
               it.remove();
           }

        }

        for(Iterator<ElementMusicalDuree>it = tabElementsMusicauxQuiPerdurentStrictement.iterator() ; it.hasNext(); )
        {
           ElementMusical el = it.next();
           if(!el.isSurParties(parties))
           {
               it.remove();
           }

        }

        for(Iterator<ElementMusicalDuree>it = tabElementsMusicauxQuiSontRelachees.iterator() ; it.hasNext(); )
        {
           ElementMusical el = it.next();
           if(!el.isSurParties(parties))
           {
               it.remove();
           }

        }
    }

    public Set<ElementMusicalDuree> getElementsMusicauxAvecDureeConcernees(Collection<Partie> parties) {
        Set<ElementMusicalDuree> s = getElementsMusicauxDureeAJouer(parties);
        s.addAll(getElementsMusicauxQuiPerdurentStrictement(parties));
        return s;
    }

    private Set<ElementMusicalDuree> getElementsMusicauxDureeAJouer(Collection<Partie> parties) {
        Set<ElementMusicalDuree> N = new HashSet<ElementMusicalDuree>();

        for(ElementMusical el : tabElementsMusicauxQuiDebutent)
        {
            if((el instanceof ElementMusicalDuree) && (el.isSurParties(parties)))
            {
                N.add((ElementMusicalDuree) el);
            }
        }
        return N;
    }
    

    
    public Set<ElementMusicalDuree> getElementsMusicauxQuiPerdurentStrictement(Collection<Partie> parties) {
        Set<ElementMusicalDuree> N = new HashSet<ElementMusicalDuree>();

        for(ElementMusicalDuree el : tabElementsMusicauxQuiPerdurentStrictement)
        {
            if((el instanceof ElementMusicalDuree) && (el.isSurParties(parties)))
            {
                N.add((ElementMusicalDuree) el);
            }
        }
        return N;
    }
    
    
    
    
    
    
}
