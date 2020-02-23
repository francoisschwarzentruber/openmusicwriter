/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.*;

/**
 *
 * @author Ancmin
 */
public class ElementsMusicauxEnsemble implements ElementsMusicauxEnsembleLecture  {
    private boolean is_modifie = false;

    final private TreeMap<Moment, HashSet<ElementMusical>> elementsMusicaux = new TreeMap<Moment, HashSet<ElementMusical>>();
    final private TreeMap<Moment, ElementMusicalChangementTonalite> elementsMusicauxChangementTonalite
            = new TreeMap<Moment, ElementMusicalChangementTonalite>();
    final private TreeMap<Moment, BarreDeMesure> elementsMusicauxBarreDeMesure
            = new TreeMap<Moment, BarreDeMesure>();
    final private TreeMap<Moment, ElementMusicalChangementMesureSignature> elementsMusicauxChangementMesureSignature
            = new TreeMap<Moment, ElementMusicalChangementMesureSignature>();
    final private TreeMap<Moment, ElementMusicalTempo> elementsMusicauxTempo
            = new TreeMap<Moment, ElementMusicalTempo>();


//***********************************************************************************
// ACCEDER AUX DONNEES DE MA PARTITION
    //*******************************************************************************


    @Override
    public TreeSet<ElementMusical> getElementsMusicauxTrieesParMoment()
    {
        return new TreeSet<ElementMusical>(getElementsMusicaux());
    }



    /**
     *
     * @return l'ensemble des éléments musicaux en tant qu'objet JAVA de type Set
     */
    public Set<ElementMusical> getElementsMusicaux()
    {
        Set<ElementMusical> E = new HashSet<ElementMusical>();
     
        if(elementsMusicaux.isEmpty())
            return E;
        else
        {
            for(Moment m = elementsMusicaux.firstKey(); m != null; m = elementsMusicaux.higherKey(m))
            {
                E.addAll(elementsMusicaux.get(m));
            }
        }
        return E;
    }

    /**
     *
     * @return le premier moment de l'ensemble des éléments musicaux. Si jamais la sélection est vide,
     * cette fonction retourne 0/1
     */
    public Moment getMomentDebut()
    {
        if(elementsMusicaux.isEmpty())
        {
            return new Moment(new Rational(0, 1));
        }
        else
            return elementsMusicaux.firstKey();

    }



    /**
     *
     * @return le dernier moment de l'ensemble des éléments musicaux. Si jamais la sélection est vide,
     * cette fonction retourne 0/1
     */
    public Moment getMomentFin()
    {
        if(elementsMusicaux.isEmpty())
        {
            return new Moment(new Rational(0, 1));
        }
        else
            return elementsMusicaux.lastKey();
    }


/**
 *
 * @return le dernier moment où un élément musical est. Par exemple
 * si le dernier élément musical qui commence est une noire au temps 3/1 (qui dure 1/1 donc jusque 4/1)
 * la fonction renvoie 3/1.
 */
    public Moment getFinMomentElementMusicalDebute()
    {
        return elementsMusicaux.lastKey();
    }



/**
 * @return le dernier moment de la partition où il se passe quelquechose. Certifié comme "ne retourne JAMAIS null" :).
 * Par exemple
 * si le dernier élément musical qui commence est une noire au temps 3/1 (qui dure 1/1 donc jusque 4/1)
 * la fonction renvoie 4/1.
 */
    public Moment getFinMomentAvecDuree() {
        if(elementsMusicaux.isEmpty())
            return getMomentDebut();
        else
        {
            Moment moment = elementsMusicaux.lastKey();

            Moment lastmoment = moment;
            while(moment != null)
            {
                lastmoment = moment;
                moment = getMomentSuivant(moment);
            }
            return lastmoment;
        }
    }



    /**
     *
     * @param moment
     * @return retourne le moment du début de la mesure qui contient le moment
     * (les mesures sont matérialisées par les objets BarreDeMesure)
     */
    public Moment getMesureMomentDebut(Moment moment)
    {
        Moment momentD = elementsMusicauxBarreDeMesure.floorKey(moment);

        if(momentD != null)
        {
            return momentD;
        }
        else
        {
            return getMomentDebut();
        }
    }


/**
 *
 * @param moment
 * @return le moment qui correspond au début de la mesure précédente
 */
    public Moment getMesurePrecedenteMomentDebut(Moment moment) {
        Moment momentD = elementsMusicauxBarreDeMesure.floorKey(moment);

        
        if(momentD != null)
        {
            Moment momentD2 = elementsMusicauxBarreDeMesure.lowerKey(momentD);

            if(momentD2 != null)
                return momentD2;
            else
                return getMomentDebut();
        }
        else
        {
            return getMomentDebut();
        }
    }




     /**
     *
     * @param moment
     * @return retourne le moment de fin de la mesure qui contient le moment
     * (les mesures sont matérialisées par les objets BarreDeMesure)
      * (ce moment de fin correspond aussi au moment de début de la prochaine mesure, cela dit)
     */
    public Moment getMesureSuivanteMomentDebut(Moment moment)
    {
        Moment momentD = elementsMusicauxBarreDeMesure.higherKey(moment);

        if(momentD != null)
        {
            return momentD;
        }
        else
        {
            return getFinMomentAvecDuree();
        }
    }




/**
 *
 * @return tous les éléments musicaux présents dans la mesure qui contient le moment
 * momentDansMesure
 */
    public Selection getSelectionMesure(Moment momentDansMesure)
    {
        final Moment momentDebut = getMesureMomentDebut(momentDansMesure);
        final Moment momentFin = getMesureSuivanteMomentDebut(momentDebut);

        return getSelectionEntreM1InclusEtM2Exclus(momentDebut, momentFin);
    }
        
    /**
     *
     * @param moment
     * @return le moment de la fin de la mesure qui contient le moment moment
     * (cette fonction est certifié comme ne renvoyant jamais NULL)
     */
    public Moment getMomentMesureFin(Moment moment)
    {
        Moment momentD = elementsMusicauxBarreDeMesure.higherKey(moment);

        if(momentD != null)
        {
            return momentD;
        }
        else
        {
            return getFinMomentAvecDuree();
        }
    }



/**
 *
 * @param p
 * @param debut
 * @param fin
 * @return retourne true ssi il n'y a aucun élément musical sur la portée p
 * entre le moment debut inclus et le moment fin exclus
 */
    public boolean porteeIsVide(Portee p, Moment debut, Moment fin)
    {
        Moment moment = elementsMusicaux.ceilingKey(debut);

        if(moment == null)
             return true;

        while(moment.isStrictementAvant(fin))
        {
            HashSet<ElementMusical> els = elementsMusicaux.get(moment);
            for(ElementMusical el : els)
            {
                if(el instanceof ElementMusicalDuree)
                {
                    ElementMusicalSurPortee elp = (ElementMusicalSurPortee) el;
                    if(elp.getPortee().equals(p))
                    {
                        return false;
                    }
                }
            }
            moment = elementsMusicaux.higherKey(moment);
            if(moment == null)
                return true;
        }
        return true;

    }


    /**
     *
     * @return vrai ssi il n'y a pas d'éléments musicaux
     */
    public boolean isVide() {
        return elementsMusicaux.isEmpty();
    }




    /**
     *
     * @param momentactuel
     * @return renvoie le prochain moment qui contient un élément musical qui débute.
     * S'il n'y en a pas... retourne null!
     */
    public Moment getMomentSuivantAvecElementsMusicauxQuiDebutent(Moment momentactuel)
    {
        return elementsMusicaux.higherKey(momentactuel);
    }

/**
 *
 * @return le nombre de moments dans la sélection (c'est à dire le nombre de moments
 * où il y a un début d'un élément musical)
 *
 * exemple : pour
 * O     O      O    O
 * O                 O
 *                   O
 *
 * ça renvoie 4.
 */
    public int getMomentsNombre() {
        return elementsMusicaux.keySet().size();
    }

    /**
     *
     * @param momentactuel
     * @return renvoie le prochain moment "où il se passe quelquechose". C'est à dire
     * le prochain moment où un élément musical débute ou un élément musical finit.
     */
    public Moment getMomentSuivant(Moment momentactuel)
    {
        
        Moment moment_candidat = elementsMusicaux.higherKey(momentactuel);
        

        MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = getMomentNotesSurLeFeu(momentactuel);

        Set<ElementMusicalDuree> elementsMusicauxConcernees = momentNotesSurLeFeu.getElementsMusicauxAvecDureeConcernees();
        for(ElementMusicalDuree element : elementsMusicauxConcernees)
        {
            Moment fin = element.getFinMoment();

            if(fin.isStrictementApres(momentactuel))
            {
                if(moment_candidat == null)
                {
                    moment_candidat = fin;
                }
                else
                if(moment_candidat.isApres(fin))
                {
                     moment_candidat = fin;
                }
            }
        }


       return moment_candidat;

    }



    public Moment getMomentSuivant(Moment momentactuel, Portee portee)
    {
        HashSet<Partie> S = new HashSet<Partie>();
        S.add(portee.getPartie());
        return getMomentSuivant(momentactuel, S);
    }


    /**
     *
     * @param momentactuel
     * @param parties
     * @return renvoie le prochain moment "où il se passe quelquechose" dans
     *  les parties parties. C'est à dire
     * le prochain moment où un élément musical débute ou un élément musical finit dans les parties concernées.
     */
    @Override
    public Moment getMomentSuivant(Moment momentactuel, Collection<Partie> parties)
    {
        Moment moment_candidat = elementsMusicaux.higherKey(momentactuel);
        while( (moment_candidat != null) )
        {
            if(isMomentQchDebuteDansParties(moment_candidat, parties))
                break;

            moment_candidat = elementsMusicaux.higherKey(moment_candidat);
        }


        MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = getMomentNotesSurLeFeu(momentactuel);

        Set<ElementMusicalDuree> elementsMusicauxConcernees = momentNotesSurLeFeu.getElementsMusicauxAvecDureeConcernees(parties);

        

        for(ElementMusicalDuree element : elementsMusicauxConcernees)
        {
            Moment fin = element.getFinMoment();

            if(fin.isStrictementApres(momentactuel))
            {
                if(moment_candidat == null)
                {
                    moment_candidat = fin;
                }
                else
                if(moment_candidat.isApres(fin))
                {
                     moment_candidat = fin;
                }
            }
        }


       return moment_candidat;

    }


    /**
     *
     * @param momentactuel
     * @return le prochain moment où il se passe qch comme :
     *  - une note, silence qui s'arrête
     *  - une clef
     *  - un tempo
     *  - une barre de mesure
     *  - bref tout sauf des choses qui sont "supplémentaires" comme courbe, texte et images etc.
     */
    public Moment getMomentSuivantDebutFinNotesSilencesClefsEtc(Moment momentactuel) {
        Moment momentsuivant = getMomentSuivant(momentactuel);

        if(momentsuivant == null)
        {
            return null;
        }
        else
        {
            MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = getMomentNotesSurLeFeu(momentsuivant);

            if(!momentNotesSurLeFeu.getElementsMusicauxQuiSontRelachees().isEmpty())
                return momentsuivant;
            else
            {
                for(ElementMusical el : getElementsMusicauxQuiCommencent(momentsuivant))
                {
                    if(!(el instanceof ElementMusicalCourbe))
                        return momentsuivant;

                }
                return getMomentSuivantDebutFinNotesSilencesClefsEtc(momentsuivant);
            }
        }
    }



    /**
     *
     * @param momentActuel
     * @return retourne le moment où il y a un élément musical qui commence et qui précède
     * momentActuel
     * S'il n'y a pas de moment qui précède momentActuel la fonction renvoie
     * la même chose que getMomentDebut()
     */
    public Moment getMomentPrecedent(Moment momentActuel) {
        Moment momentPrecedent = elementsMusicaux.lowerKey(momentActuel);

        if(momentPrecedent == null)
            return getMomentDebut();
        else
            return momentPrecedent;
    }



    private Voix getVoix(Curseur curseur, Duree dureeProposee)
    {
        MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = getMomentNotesSurLeFeu(curseur.getMoment());
        Set<Note> notesrelachees = momentNotesSurLeFeu.getNotesQuiSontRelachees();

        Voix voix = null;


        {
            int distancecourante = 1000;

            Set<Note> notesenfoncees = momentNotesSurLeFeu.getNotesAJouer();

            for(Note note : notesenfoncees)
            {
                int nouvelledistance = Math.abs(note.getHauteur().getNumero() - curseur.getHauteur().getNumero());
                if(note.getPortee().equals(curseur.getPortee()) & (nouvelledistance < distancecourante)
                        & note.getDuree().isEgal(dureeProposee)
                        )
                {
                    voix = note.getVoix();
                    distancecourante = nouvelledistance;
                }
            }


            if(voix != null)
                return voix;
        }




        int distancecourante = 1000;
        for(Note note : notesrelachees)
        {
            int nouvelledistance = Math.abs(note.getHauteur().getNumero() - curseur.getHauteur().getNumero());
            if(note.getPortee().equals(curseur.getPortee()) & (nouvelledistance < distancecourante))
            {
                voix = note.getVoix();
                distancecourante = nouvelledistance;
            }
        }


        if(voix == null)
        {
            return new Voix();
        }
        else
        {
            ElementMusicalSurVoix el = momentNotesSurLeFeu.getElementMusicalSurVoix(voix);

            if(el != null)
            {
                if(!el.getDuree().equals(dureeProposee))
                    voix = new Voix();
            }
            return voix;
        }


    }

    public Curseur voixDeviner(Curseur curseur, Duree dureeProposee)
    {
        return new Curseur(curseur.getMoment(),
                           curseur.getHauteur(),
                           curseur.getPortee(),
                           getVoix(curseur, dureeProposee));
    }







    @Override
    public MomentElementsMusicauxSurLeFeu getMomentNotesSurLeFeu(Moment momentActuel)
    {
        MomentElementsMusicauxSurLeFeu notesSurLeFeu = new MomentElementsMusicauxSurLeFeu(momentActuel);

        Moment moment = elementsMusicaux.floorKey(momentActuel);

        for(int i = 0; (i<30) & (moment != null); i++)
        {
            Set<ElementMusical> c = elementsMusicaux.get(moment);
            for(ElementMusical n : c)
            {
                if(n.getDebutMoment().isEgal(momentActuel))
                {
                    notesSurLeFeu.elementMusicalQuiDebuteAjouter(n);
                }
                else if(n instanceof ElementMusicalDuree)
                {
                    ElementMusicalDuree nd = (ElementMusicalDuree) n;
                    if(nd.getDebutMoment().isStrictementAvant(momentActuel) &&
                            momentActuel.isStrictementAvant(nd.getFinMoment()))
                    {
                        notesSurLeFeu.elementMusicalQuiPerdureStrictementAjouter(nd);
                    }
                    else if(nd.getFinMoment().isEgal(momentActuel))
                    {
                        notesSurLeFeu.elementMusicalQuiEstRelacheAjouter(nd);
                    }
                }

            }

            moment = elementsMusicaux.lowerKey(moment);
        }

        return notesSurLeFeu;

    }











    /**
     *
     * @param moment
     * @return l'ensemble des éléments musicaux qui commencent au moment moment.
     */
    @Override
    public Set<ElementMusical> getElementsMusicauxQuiCommencent(Moment moment)
    {
        if(elementsMusicaux.containsKey(moment))
            //faire une copie pour éviter les effets de bords
            return (Set<ElementMusical>) elementsMusicaux.get(moment).clone();
        else
            return new HashSet<ElementMusical>();
    }



    
    public Set<ElementMusical> getElementsMusicauxSurPorteesQuiCommencent(Moment moment)
    {
        if(elementsMusicaux.containsKey(moment))
        {
            HashSet<ElementMusical> els = new HashSet<ElementMusical>();
            for(ElementMusical el : elementsMusicaux.get(moment))
            {
                if(el instanceof ElementMusicalSurPortee)
                {
                        els.add(el);
                }
            }
            //faire une copie pour éviter les effets de bords
            return els;
        }
        else
            return new HashSet<ElementMusical>();
    }
    
    
    /**
     *
     * @param moment
     * @param partie
     * @return les éléments musicaux sur portée qui commencent au moment moment et qui sont
     * dans la partie partie.
     */
    @Override
    public Set<ElementMusical> getElementsMusicauxPartieQuiCommencent(Moment moment, Partie partie)
    {
        if(elementsMusicaux.containsKey(moment))
        {
            HashSet<ElementMusical> els = new HashSet<ElementMusical>();
            for(ElementMusical el : elementsMusicaux.get(moment))
            {
                if(el instanceof ElementMusicalSurPortee)
                {
                    if(((ElementMusicalSurPortee) el).getPortee().getPartie() == partie)
                    {
                        els.add(el);
                    }
                }
                else
                {
                    els.add(el);
                }
            }
            //faire une copie pour éviter les effets de bords
            return els;
        }
        else
            return new HashSet<ElementMusical>();
    }


    
    public Selection getNotesTransposees(Intervalle i)
    {
        Selection S = new Selection();
        for(ElementMusical el : getElementsMusicaux())
        {
            if(el instanceof Note)
            {
                S.elementMusicalAjouter( ((Note) el).getNoteTransposee(i) );
            }
        }

        return S;
    }
    
    public Set<ElementMusicalParoleSyllabe> getElementsMusicauxParoleSyllabe()
    {
        Set<ElementMusicalParoleSyllabe> S = new HashSet<ElementMusicalParoleSyllabe>();

        for(ElementMusical el : getElementsMusicaux())
        {
            if(el instanceof ElementMusicalParoleSyllabe)
                S.add((ElementMusicalParoleSyllabe) el);

        }

        return S;
    }


    

    








    /**
     *
     * @param moment
     * @param portee
     * @return l'ensemble des notes qui commencent au moment et qui sont dans
     * la portée
     */
    public Set<Note> getNotesPorteeQuiCommencent(Moment moment, Portee portee)
    {
        if(elementsMusicaux.containsKey(moment))
        {
            HashSet<Note> els = new HashSet<Note>();
            for(ElementMusical el : elementsMusicaux.get(moment))
            {
                if(el instanceof Note)
                {
                    Note note = (Note) el;
                    if(note.getPortee().equals(portee))
                    {
                        els.add(note);
                    }
                }
            }
            //faire une copie pour éviter les effets de bords
            return els;
        }
        else
            return new HashSet<Note>();
    }


    /**
     *
     * @param moment
     * @return la barre de mesure qui se situe exactement au moment moment. S'il n'y en a pas,
     * retourne null.
     */
    public BarreDeMesure getMesureBarreDeMesureDebut(Moment moment)
    {
        if(elementsMusicauxBarreDeMesure.containsKey(moment))
            return elementsMusicauxBarreDeMesure.get(moment);
        else
            return null;
    }




    



    public ElementMusicalChangementMesureSignature getElementMusicalMesureSignature(Moment moment) {
        Moment momentChangementSignature = elementsMusicauxChangementMesureSignature.floorKey(moment);

        if(momentChangementSignature == null)
            return null;
        else
            return elementsMusicauxChangementMesureSignature.get(momentChangementSignature);
    }




    public int getNbNoiresParMinute(Moment moment) {
        Moment momentChangementTempo = elementsMusicauxTempo.floorKey(moment);

        if(momentChangementTempo == null)
            return 120;
        else
            return elementsMusicauxTempo.get(momentChangementTempo).getNbNoiresEnUneMinute();
    }
    

    public MesureSignature getMesureSignature(Moment moment)
    {
        Moment momentChangementSignature = elementsMusicauxChangementMesureSignature.floorKey(moment);

        if(momentChangementSignature == null)
            return null;
        else
            return elementsMusicauxChangementMesureSignature.get(momentChangementSignature).getSignature();
    }


    public Set<ElementMusical> getElementsMusicauxPrecedentsMemeVoix(Curseur curseur)
    {
        Set<ElementMusical> E = new HashSet<ElementMusical>();
        MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = getMomentNotesSurLeFeu(curseur.getMoment());
        Set<ElementMusicalDuree> elementsMusicauxQuiSontRelachees = momentNotesSurLeFeu.getElementsMusicauxQuiSontRelachees();

        if(curseur.isSurNote() || !curseur.isSurElementMusical())
        for(ElementMusicalDuree el : elementsMusicauxQuiSontRelachees)
        {
            if(el instanceof ElementMusicalSurVoix)
            {
                ElementMusicalSurVoix elv = (ElementMusicalSurVoix) el;

                if(elv.getVoix().equals(curseur.getVoix()))
                {
                    E.add(elv);
                }

            }
        }

        return E;
    }


    
    public ElementMusical getElementMusicalProche(Curseur curseur)
    {
        int min = 100000;
        ElementMusical elProche = null;
        for(ElementMusical el : getElementsMusicauxQuiCommencent(curseur.getMoment()))
        {
            if(el instanceof ElementMusicalSurVoix)
            {
                Curseur curseurEl = ((ElementMusicalSurVoix) el).getCurseur();

                if(curseurEl.getPortee().equals(curseur.getPortee()))
                {
                    int n = Math.abs((new Intervalle(curseurEl.getHauteur(), curseur.getHauteur())).numero);
                    if(n < min )
                    {
                        min = n;
                        elProche = el;
                    }
                }
            }
        }

        return elProche;
    }


/**
 *
 * @param moment
 * @param elementMusicalSousClasse
 * @return un élément musical de type elementMusicalSousClasse présent à l'instant moment.
 * S'il n'y en a pas, retourne null.
 */
    public ElementMusical getElementMusical(Moment moment, Class<? extends ElementMusical> elementMusicalSousClasse)
    {
        Set<ElementMusical> els = getElementsMusicauxQuiCommencent(moment);

        for(ElementMusical el : els)
        {
            if(el.getClass().equals(elementMusicalSousClasse))
            {
                return el;
            }
        }

        return null;
    }




    /**
     * 
     * @param moment
     * @param portee
     * @return retourne l'élément parole syllabe présent ou null sinon
     */
    public ElementMusicalParoleSyllabe getElementMusicalParoleSyllabe(Moment moment, Portee portee)
    {
        Set<ElementMusical> els = getElementsMusicauxQuiCommencent(moment);

        for(ElementMusical el : els)
        {
            if(el instanceof ElementMusicalParoleSyllabe)
            if(((ElementMusicalParoleSyllabe) el).getPortee().equals(portee))
            {
                return (ElementMusicalParoleSyllabe) el;
            }
        }
        return null;
    }

    /**
     *
     * @param moment
     * @param portee
     * @return retourne l'élement musical qui est une clef placé sur la portée portee
     * et au moment moment EXACTEMENT.
     */
    @Override
    public ElementMusicalClef getElementMusicalClefPileDessus(Moment moment, Portee portee)
    {
        ElementMusicalClef emc = portee.getElementMusicalClef(moment);

        if(emc == null)
        {
            return null;
        }
        else
        {
            if(emc.getDebutMoment().equals(moment))
                 return emc;
            else
                 return null;
        }

    }




    /**
     *
     * @param moment
     * @param portee
     * @return retourne l'octaviation qui est une clef placé sur la portée portee
     * et au moment moment EXACTEMENT.
     */
    public Octaviation getOctaviationPileDessus(Moment moment, Portee portee)
    {
        Octaviation emc = portee.getOctaviation(moment);

        if(emc == null)
        {
            return null;
        }
        else
        {
            if(emc.getDebutMoment().equals(moment))
                 return emc;
            else
                 return null;
        }

    }

    public ElementMusicalChangementTonalite getElementMusicalChangementTonaliteCourant(Moment moment)
    {
        Moment momentChangementTonalite = elementsMusicauxChangementTonalite.floorKey(moment);

        if(momentChangementTonalite == null)
            return null;
        else
            return elementsMusicauxChangementTonalite.get(momentChangementTonalite);
    }

    public Tonalite getTonalite(Moment moment)
    {
        Moment momentChangementTonalite = elementsMusicauxChangementTonalite.floorKey(moment);

        if(momentChangementTonalite == null)
            return new Tonalite(0);
        else
            return elementsMusicauxChangementTonalite.get(momentChangementTonalite).getTonalite();
    }


    public Tonalite getTonaliteJusteAvant(Moment moment)
    {
        Moment momentChangementTonalite = elementsMusicauxChangementTonalite.lowerKey(moment);

        if(momentChangementTonalite == null)
            return new Tonalite(0);
        else
            return elementsMusicauxChangementTonalite.get(momentChangementTonalite).getTonalite();
    }


    public Tonalite getTonalite(Moment moment, Partie partie)
    {
        return partie.getTonaliteTransposee(getTonalite(moment));
    }



    public Tonalite getTonaliteJusteAvant(Moment moment, Partie partie)
    {
        return partie.getTonaliteTransposee(getTonaliteJusteAvant(moment));
    }


    public Hauteur.Alteration getAlterationParDefaut(Moment moment, Moment momentOnRegardePasAvant, Portee portee, Hauteur hauteur)
    {
        Moment momentMesureDebut = getMesureMomentDebut(moment);

        if(!moment.equals(momentMesureDebut))
        {
            moment = getMomentPrecedent(moment);

            while(moment.isApres(momentMesureDebut) && momentOnRegardePasAvant.isAvant(moment))
            {
                for(Note note : getNotesPorteeQuiCommencent(moment, portee))
                {
                    if(note.getHauteur().getNumero() == hauteur.getNumero())
                        return note.getHauteur().getAlteration();
                }



                moment = getMomentPrecedent(moment);

                if(isMomentDebut(moment))
                    break;

            }
        }

         return getTonalite(moment, portee.getPartie()).getAlteration(hauteur);
    }








   


    /**
     *
     * @return une sélection qui contient tous les éléments musicaux de l'ensemble
     */
    public Selection getSelectionTout()
    {
        Selection s = new Selection();
        for(Set<ElementMusical> E : elementsMusicaux.values())
        {
            s.ajouterSelection(E);
        }
        return s;
    }


    /**
     *
     * @param moment
     * @return une sélection qui contient tous les éléments musicaux qui sont après
     * (non strict) le moment moment.
     */
    public Selection getSelectionToutApresMoment(Moment moment)
    {
        Selection s = new Selection();
        moment = elementsMusicaux.ceilingKey(moment); // supérieur ou égal
        for(; moment != null; moment = elementsMusicaux.higherKey(moment))
        {
            s.ajouterSelection(elementsMusicaux.get(moment));
        }

        return s;
    }



    /**
     *
     * @param moment1 
     * @param moment2 
     * @return un tableau contenant tous les éléments musicaux qui sont après, dans l'ordre
     * anti-chronologique de la partition
     * (non strict) le moment moment.
     */
    public ArrayList<ElementMusical> getSelectionToutEntreMoment1Moment2ElementsMusicauxALEnvers(final Moment moment1, final Moment moment2)
    {
        ArrayList<ElementMusical> s = new ArrayList<ElementMusical>();
        
        Moment m;
        if(moment2 == null)
        {
            m = elementsMusicaux.lastKey(); // supérieur ou égal
        }
        else
        {
            m = elementsMusicaux.floorKey(moment2);
        }
        
        for(; m != null && m.isApres(moment1); m = elementsMusicaux.lowerKey(m))
        {
            s.addAll(elementsMusicaux.get(m));
        }

        return s;
    }

    /**
     *
     * @param moment1 
     * @param moment2 
     * @return un tableau contenant tous les éléments musicaux qui sont après, dans l'ordre
     * chronologique de la partition
     * (non strict) le moment moment.
     */
    public ArrayList<ElementMusical> getSelectionToutEntreMoment1Moment2ElementsMusicauxALEndroit(
            final Moment moment1, final Moment moment2)
    {
        ArrayList<ElementMusical> s = new ArrayList<ElementMusical>();
        Moment m = elementsMusicaux.ceilingKey(moment1); // supérieur ou égal
        for(; m != null; m = elementsMusicaux.higherKey(m))
        {
            if(moment2 != null)
            {
                if(m.isApres(moment2))
                    break;
            }
            s.addAll(elementsMusicaux.get(m));
        }

        return s;
    }

    /**
     *
     * @param partie
     * @return tous les éléments musicaux sur portée qui sont dans la partie partie
     */
    public Selection getSelectionToutPartie(Partie partie)
    {
        Selection s = new Selection();
        for(Set<ElementMusical> E : elementsMusicaux.values())
        {
            for(ElementMusical el : E)
            {
                if(el instanceof ElementMusicalSurPortee)
                {
                    if(((ElementMusicalSurPortee) el).getPortee().getPartie() == partie)
                    {
                        s.elementMusicalAjouter(el);
                    }
                }
            }


        }
        return s;
    }

    
    
    
    
    
    
    
    /**
     *
     * @param portee 
     * @return tous les éléments musicaux sur portée qui sont dans la portéee portee
     */
    public Selection getSelectionToutPortee(Portee portee)
    {
        Selection s = new Selection();
        for(Set<ElementMusical> E : elementsMusicaux.values())
        {
            for(ElementMusical el : E)
            {
                if(el instanceof ElementMusicalSurPortee)
                {
                    if(((ElementMusicalSurPortee) el).getPortee() == portee)
                    {
                        s.elementMusicalAjouter(el);
                    }
                }
            }


        }
        return s;
    }

//***************************************************************************
//  LA SAUVEGARDE EN MusicXML
//*****************************************************************************


   















//************************************************************************************
// MODIFICATION DE LA PARTITION
//************************************************************************************

    // modification de la partition

    /**
     * déplace l'élément musical element qui est supposé appartenir à l'ensemble et le place
     * à l'instant debutMomentFutur
     * @param element
     * @param debutMomentFutur
     */
    public void elementMusicalDeplacer(ElementMusical element, Moment debutMomentFutur) {
        elementMusicalSupprimer(element);
        element.deplacer(debutMomentFutur);
        elementMusicalAjouter(element);
    }



    /**
     * Ajoute l'élement musical element
     * 
     * Exemple : si on fait elementMusicalAjouter(une note qui est un fa, sur la portée 1, à l'instant 4/1
     * et qui dure 2 temps)
     * 
     * 
     * Cette fonction peut éventuellement ne rien faire.
     * Exemple : si on fait elementMusicalAjouter(une clef de sol sur la portée 1, à l'instant 4/1)
     * et qu'il y a une clef de fa sur la portée 1 à l'instant 4/1, cette fonction
     * ne fait rien et laisse la clef de fa.
     * A l'utilisateur de cette classe, de supprimer d'abord la clef de fa avec elementMusicalSupprimer
     * (en utilisant éventuellement getElementMusicalClefPileDessus pour obtenir la clef qui est dessus)
     * etc.
     * @param element
     */
    public void elementMusicalAjouter(ElementMusical element)
    {
        if(element instanceof Accord)
        {
            for(Note note : (Accord) element)
            {
                elementMusicalAjouter(note);
            }
            return;
        }
        
        Moment moment = element.getDebutMoment();

        if(element instanceof ElementMusicalChangementTonalite)
        {
            if(elementsMusicauxChangementTonalite.containsKey(element.getDebutMoment()))
                return;

            elementsMusicauxChangementTonalite.put(moment, (ElementMusicalChangementTonalite) element);

        }

        if(element instanceof ElementMusicalTempo)
        {
            if(elementsMusicauxTempo.containsKey(element.getDebutMoment()))
                return;

            elementsMusicauxTempo.put(moment, (ElementMusicalTempo) element);

        }

        if(element instanceof ElementMusicalChangementMesureSignature)
        {
            if(elementsMusicauxChangementMesureSignature.containsKey(element.getDebutMoment()))
                return;

            elementsMusicauxChangementMesureSignature.put(moment, (ElementMusicalChangementMesureSignature) element);

        }


        



        if(element instanceof BarreDeMesure)
        {
            if(elementsMusicauxBarreDeMesure.containsKey(element.getDebutMoment()))
                return;

            elementsMusicauxBarreDeMesure.put(moment, (BarreDeMesure) element);
        }

        if(elementsMusicaux.containsKey(moment))
        {
            Set<ElementMusical> els = elementsMusicaux.get(element.getDebutMoment());
            els.add(element);
        }
        else
        {
             HashSet<ElementMusical> els = new HashSet<ElementMusical>();
             els.add(element);
             elementsMusicaux.put(moment, els);
        }

        modificationOui();


    }



    /**
     * supprime l'élement musical de l'ensemble
     * @param element
     */
    public void elementMusicalSupprimer(ElementMusical element)
    {
        Moment moment = element.getDebutMoment();
        Set<ElementMusical> els = elementsMusicaux.get(moment);
        els.remove(element);
        if(els.isEmpty())
        {
            elementsMusicaux.remove(moment);
        }

        if(element instanceof ElementMusicalChangementTonalite)
        {
           ElementMusicalChangementTonalite eltonalite = (ElementMusicalChangementTonalite) element;
           elementsMusicauxChangementTonalite.remove(eltonalite.getDebutMoment());
        }

        if(element instanceof ElementMusicalTempo)
        {
           ElementMusicalTempo eltempo = (ElementMusicalTempo) element;
           elementsMusicauxTempo.remove(eltempo.getDebutMoment());
        }

        if(element instanceof BarreDeMesure)
        {
           BarreDeMesure barre = (BarreDeMesure) element;
           elementsMusicauxBarreDeMesure.remove(barre.getDebutMoment());
        }

        if(element instanceof ElementMusicalChangementMesureSignature)
        {
           ElementMusicalChangementMesureSignature barre = (ElementMusicalChangementMesureSignature) element;
           elementsMusicauxChangementMesureSignature.remove(barre.getDebutMoment());
        }


        modificationOui();

    }


    /**
     *
     * @param moment
     * @return vrai ssi il y a une barre de mesure au moment moment.
     */
    public boolean isBarreDeMesure(Moment moment) {
        return elementsMusicauxBarreDeMesure.containsKey(moment);
    }


    /**
     *
     * @return true ssi l'ensemble a été modifié
     */
    public boolean isModifie() {
        return is_modifie;
    }


    /**
     * initialise l'ensemble pour dire qu'il n'est pas modifié
     * Les fonctions de modification elementMusicalAjouter et elementMusicalSupprimer
     * signale si l'ensemble est modifié ou non.
     */
    public void setPasDeModification()
    {
        is_modifie = false;
    }


    private void modificationOui()
    {
        is_modifie = true;
    }

    private boolean isMomentDebut(Moment moment) {
        return moment.equals(getMomentDebut());
    }


    private boolean isMomentFin(Moment moment) {

        return moment.equals(getFinMomentAvecDuree());
    }

    public boolean porteeIsMesureVide(Portee p, Moment debutMoment) {
        return porteeIsVide(p, debutMoment, getMomentMesureFin(debutMoment));
    }

    public boolean isDebutMesure(Moment moment) {
        return (isMomentDebut(moment) | isBarreDeMesure(moment)) & !isMomentFin(moment);
    }

    /**
     *
     * @param moment
     * @return true ssi le moment ne contient pas d'élément musical qui commence à ce moment là
     */
    public boolean isMomentVide(Moment moment) {
        return !elementsMusicaux.containsKey(moment);
    }


    /**
     *
     * @param moment
     * @return true ssi le moment ne contient pas de notes
     */
    public boolean isMomentPasDeNotes(Moment moment) {
        if(elementsMusicaux.containsKey(moment))
        {
            for(ElementMusical el : elementsMusicaux.get(moment))
            {
                if(el instanceof Note)
                    return false;
            }
            return true;
        }
        else
        {
            return true;
        }
    }


    /**
     *
     * @param moment
     * @return true ssi le moment ne contient pas d'éléments musicaux sur la portée portee
     */
    public boolean isMomentVideSurPortee(Moment moment, Portee portee) {
        if(elementsMusicaux.containsKey(moment))
        {
            for(ElementMusical el : elementsMusicaux.get(moment))
            {
                if(el instanceof ElementMusicalSurPortee)
                    if(((ElementMusicalSurPortee) el).getPortee().equals(portee))
                        return false;
            }
            return true;
        }
        else
        {
            return true;
        }
    }

    /**
     *
     * @param curseur
     * @return retourne un nouveau curseur qui est placé à peu près
     * sur l'élément musical qui précède
     * le curseur dans la même voix que celle spécifiée dans curseur.
     * Si un tel élément musical n'existe pas, cette fonction retourne null.
     */
    Curseur getCurseurPrecedentMemeVoix(Curseur curseur) {
        Set<ElementMusicalDuree> elementsMusicauxQuiSontRelachees = getMomentNotesSurLeFeu(curseur.getMoment()).getElementsMusicauxQuiSontRelachees();

        for(ElementMusicalDuree el : elementsMusicauxQuiSontRelachees)
        {
            if(el instanceof ElementMusicalSurVoix)
            {
                ElementMusicalSurVoix elv = (ElementMusicalSurVoix) el;

                if(elv.getVoix().equals(curseur.getVoix()))
                {
                    return new Curseur(elv.getDebutMoment(), curseur.getHauteur(), elv.getPortee(), curseur.getVoix());
                }
            }
        }
        return null;

    }

    /**
     *
     * @param el
     * @param momentFin
     * @return retourne un ensemble d'éléments musicaux (en vrac! c'est le bordel ! :) )
     * qui contient TOUS les éléments musicaux qui suivent el ie, qui sont dans la même voix que lui.
     * Ca s'arrête au momentFin inclus. Les éléments musicaux qui suivent el et qui sont après sont
     * ignorés.
     */
    public Set<ElementMusicalSurVoix> getElementsMusicauxSurVoixSuivants(ElementMusicalSurVoix el, Moment momentFin) {
        Set<ElementMusicalSurVoix> resultat = new HashSet<ElementMusicalSurVoix>();
        Moment moment = el.getFinMoment();
        Set<ElementMusicalSurVoix> els = getElementsMusicauxSurVoix(el.getFinMoment(), el.getVoix());
        while((!els.isEmpty()) & moment.isAvant(momentFin))
        {
            resultat.addAll(els);
            moment = els.iterator().next().getFinMoment();
            els = getElementsMusicauxSurVoix(moment, el.getVoix());
        }

        return resultat;
    }


/**
 *
 * @param moment
 * @param voix
 * @return retourne tous les éléments musicaux (généralement un ensemble de notes ou alors un singleton
 * qui est un silence) qui sont dans la voix spécifiée
 */
    public Set<ElementMusicalSurVoix> getElementsMusicauxSurVoix(Moment moment, Voix voix) {
        Set<ElementMusicalSurVoix> resultat = new HashSet<ElementMusicalSurVoix>();
        for(ElementMusical el : getElementsMusicauxQuiCommencent(moment))
        {
            if(el instanceof ElementMusicalSurVoix)
            {
                if(((ElementMusicalSurVoix) el).getVoix().equals(voix))
                {
                    resultat.add((ElementMusicalSurVoix) el);
                }
            }
        }
        return resultat;
    }

    public void elementsMusicauxAjouter(Selection selection) {
        if(selection == null)
            return;
        
        for(ElementMusical el : selection)
        {
            elementMusicalAjouter(el);
        }
    }
    
    
    public void elementsMusicauxAjouter(Set<ElementMusical> elementsMusicauxEnsemble) {       
        for(ElementMusical el : elementsMusicauxEnsemble)
        {
            elementMusicalAjouter(el);
        }
    }

    
    

    
    private boolean isMomentQchDebuteDansParties(Moment moment, Collection<Partie> parties) {
        if(elementsMusicaux.containsKey(moment))
        {
            for(ElementMusical el : elementsMusicaux.get(moment))
            {
                if(el.isSurParties(parties))
                    return true;
            }
            return false;
        }
        else
        {
            return false;
        }
    }

    public Moment getMomentSuivantDebutFinNotesSilencesClefsEtc(Moment momentactuel, Collection<Partie> parties) {
        Moment momentsuivant = getMomentSuivant(momentactuel, parties);

        if(momentsuivant == null)
        {
            return null;
        }
        else
        {
            MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = getMomentNotesSurLeFeu(momentsuivant);

            if(!momentNotesSurLeFeu.getElementsMusicauxQuiSontRelachees().isEmpty())
                return momentsuivant;
            else
            {
                for(ElementMusical el : getElementsMusicauxQuiCommencent(momentsuivant))
                {
                    if(!(el instanceof ElementMusicalCourbe))
                        return momentsuivant;

                }
                return getMomentSuivantDebutFinNotesSilencesClefsEtc(momentsuivant, parties);
            }
        }
    }



/**
 * cette fonction supprime de l'ensemble d'éléments musicaux tous les élements qui sont
 * dans selectionASupprimer
 * @param selectionASupprimer
 */
    public void selectionSupprimer(Selection selectionASupprimer)
    {
        for(ElementMusical element : selectionASupprimer.getElementsMusicaux())
        {
            elementMusicalSupprimer(element);
        }
    }


/**
 * cette fonction ajoute la sélection selectionAAjouter à l'ensemble d'éléments musicaux
 * @param selectionAAjouter
 */
    public void selectionAjouter(Selection selectionAAjouter)
    {
        for(ElementMusical element : selectionAAjouter.getElementsMusicaux())
        {
            elementMusicalAjouter(element);
        }
    }



    /**
     *
     * @return la voix d'un élément musical au hasard présent dans l'ensemble.
     * S'il n'y a aucun élément musical dans la sélection, ou aucun élément musical
     * contenu dans une voix (par exemple que des barres de mesure), retourne une nouvelle voix.
     */
    public Voix getVoixPresenteAuPif()
    {
        for(ElementMusical el : getElementsMusicaux())
        {
            if(el instanceof ElementMusicalSurVoix)
            {
                return ((ElementMusicalSurVoix) el).getVoix();
            }
        }
        return new Voix();
    }

    
    private Selection getSelectionEntreM1InclusEtM2Exclus(Moment momentDebut, Moment momentFin) {
        Selection s = new Selection();
         
        final Moment momentDebutOK = elementsMusicaux.ceilingKey(momentDebut);
        Moment moment = momentDebutOK;


        while(moment != null)
        {// supérieur ou égal
            if(!moment.isStrictementAvant(momentFin))
                break;

            if(momentDebutOK.equals(moment))
            {
                for(ElementMusical el : elementsMusicaux.get(moment))
                {
                    if(!(el instanceof ElementMusicalClef) &
                            !(el instanceof BarreDeMesure) &
                            !(el instanceof ElementMusicalChangementMesureSignature) &
                            !(el instanceof ElementMusicalChangementTonalite) &
                            !(el instanceof ElementMusicalTempo))
                    s.elementMusicalAjouter(el);
                }
                
            }
            else
                s.ajouterSelection(elementsMusicaux.get(moment));
            
            moment = elementsMusicaux.higherKey(moment);
        }

        return s;
    }




    public Curseur getCurseurMomentSuivant(Curseur curseur) {
        return curseur.getCurseurAutreMoment(getMomentSuivant(curseur.getMoment()));
    }

    public Curseur getCurseurMomentPrecedent(Curseur curseur) {
        return curseur.getCurseurAutreMoment(getMomentPrecedent(curseur.getMoment()));
    }



    public Selection getSelectionPlage(Curseur curseur1, Curseur curseur2)
    {
        final Moment moment1 = Moment.min(curseur1.getMoment(), curseur2.getMoment());
        final Moment moment2 = Moment.max(curseur1.getMoment(), curseur2.getMoment());

        final int portee1 = Math.min(curseur1.getPortee().getNumero(), curseur2.getPortee().getNumero());
        final int portee2 = Math.max(curseur1.getPortee().getNumero(), curseur2.getPortee().getNumero());

        return getSelectionPlage(moment1, moment2, portee1, portee2, false);
    }

    
    
    public Selection getSelectionPlageStrict(Moment moment1, Moment moment2)
    {
        return getSelectionPlage(moment1, moment2, 0, Integer.MAX_VALUE, true);
    }
    
    
    /**
     * 
     * @param selection
     * @return true if other note, things are occuring in the same time that selection or the selection contains all the note
     * in the corresponding range
     */
    public boolean selectionOtherThingsInTheSameTime(Selection selection)
    {
        Selection selectionConcernees = getSelectionPlageStrict(selection.getMomentDebut(), selection.getFinMomentAvecDuree());
//        Moment momentFinNouveau = dureeDeplacement.getDebutMoment(moment);
//        momentFinNouveau = duree.getFinMoment(momentFinNouveau);
        
        return selectionConcernees.getElementsMusicauxNombre() != selection.getElementsMusicauxNombre();
    }
    
    
    
    private Selection getSelectionPlage(Moment moment1, Moment moment2, int portee1, int portee2, boolean strict) {
        Selection s = new Selection();

        Moment moment = moment1;


        while(moment != null)
        {// supérieur ou égal
            if(strict)
            {
                if(!moment.isStrictementAvant(moment2))
                    break;
            } 
            else
            {
                if(!moment.isAvant(moment2))
                break;
            }

                for(ElementMusical el : getElementsMusicauxQuiCommencent(moment))
                {
                    if((el instanceof ElementMusicalSurPortee))
                    {
                        ElementMusicalSurPortee p = (ElementMusicalSurPortee) el;

                        if(!(p instanceof ElementMusicalClef & p.getDebutMoment().equals(getMomentDebut())))
                           if(portee1 <= p.getPortee().getNumero() & p.getPortee().getNumero() <= portee2)
                               s.elementMusicalAjouter(el);
                        
                    }

                }


            moment = elementsMusicaux.higherKey(moment);
        }

        return s;
    }

/**
 *
 * @return l'ensemble des parties sur lequel il y a des éléments qui sont sur des portées
 */
    public Set<Partie> getElementsMusicauxParties() {
        Set<Partie> S = new HashSet<Partie>();
        for(ElementMusical el : getElementsMusicaux())
        {
            if(el instanceof ElementMusicalSurPortee)
            {
                S.add(((ElementMusicalSurPortee) el).getPartie());
            }
        }
        return S;
    }
}
