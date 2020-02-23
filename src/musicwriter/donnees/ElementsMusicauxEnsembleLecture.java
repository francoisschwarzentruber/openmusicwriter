/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Ancmin
 */
public interface ElementsMusicauxEnsembleLecture {

    Hauteur.Alteration getAlterationParDefaut(Moment moment, 
                                              Moment momentOnRegardePasAvant,
                                              Portee portee,
                                              Hauteur hauteur);

    /**
     *
     * @param moment
     * @param elementMusicalSousClasse
     * @return un élément musical de type elementMusicalSousClasse présent à l'instant moment.
     * S'il n'y en a pas, retourne null.
     */
    ElementMusical getElementMusical(Moment moment, Class<? extends ElementMusical> elementMusicalSousClasse);

    ElementMusicalChangementTonalite getElementMusicalChangementTonaliteCourant(Moment moment);

    /**
     *
     * @param moment
     * @param portee
     * @return retourne l'élement musical qui est une clef placé sur la portée portee
     * et au moment moment EXACTEMENT.
     */
    ElementMusicalClef getElementMusicalClefPileDessus(Moment moment, Portee portee);

    ElementMusicalChangementMesureSignature getElementMusicalMesureSignature(Moment moment);

    /**
     *
     * @return l'ensemble des éléments musicaux en tant qu'objet JAVA de type Set
     */
    Set<ElementMusical> getElementsMusicaux();

    /**
     *
     * @param moment
     * @param partie
     * @return les éléments musicaux sur portée qui commencent au moment moment et qui sont
     * dans la partie partie.
     */
    Set<ElementMusical> getElementsMusicauxPartieQuiCommencent(Moment moment, Partie partie);

    /**
     *
     * @param moment
     * @return l'ensemble des éléments musicaux qui commencent au moment moment.
     */
    Set<ElementMusical> getElementsMusicauxQuiCommencent(Moment moment);

    /**
     *
     * @param moment
     * @param voix
     * @return retourne tous les éléments musicaux (généralement un ensemble de notes ou alors un singleton
     * qui est un silence) qui sont dans la voix spécifiée
     */
    Set<ElementMusicalSurVoix> getElementsMusicauxSurVoix(Moment moment, Voix voix);

    /**
     *
     * @param el
     * @param momentFin
     * @return retourne un ensemble d'éléments musicaux (en vrac! c'est le bordel ! :) )
     * qui contient TOUS les éléments musicaux qui suivent el ie, qui sont dans la même voix que lui.
     * Ca s'arrête au momentFin inclus. Les éléments musicaux qui suivent el et qui sont après sont
     * ignorés.
     */
    Set<ElementMusicalSurVoix> getElementsMusicauxSurVoixSuivants(ElementMusicalSurVoix el, Moment momentFin);

    TreeSet<ElementMusical> getElementsMusicauxTrieesParMoment();

    /**
     * @return le dernier moment de la partition où il se passe quelquechose. Certifié comme "ne retourne JAMAIS null" :).
     * Par exemple
     * si le dernier élément musical qui commence est une noire au temps 3/1 (qui dure 1/1 donc jusque 4/1)
     * la fonction renvoie 4/1.
     */
    Moment getFinMomentAvecDuree();

    /**
     *
     * @return le dernier moment où un élément musical est. Par exemple
     * si le dernier élément musical qui commence est une noire au temps 3/1 (qui dure 1/1 donc jusque 4/1)
     * la fonction renvoie 3/1.
     */
    Moment getFinMomentElementMusicalDebute();

    /**
     *
     * @param moment
     * @return la barre de mesure qui se situe au moment moment. S'il n'y en a pas,
     * retourne null.
     */
    BarreDeMesure getMesureBarreDeMesureDebut(Moment moment);


    /**
     *
     * @param moment
     * @return la signature courante au moment moment. S'il n'y a pas d'indication de mesure, retourne null
     * (l'indication n'a pas besoin d'être pile sur le moment...)
     */
    MesureSignature getMesureSignature(Moment moment);

    /**
     *
     * @return le premier moment de l'ensemble des éléments musicaux
     */
    Moment getMomentDebut();

    /**
     *
     * @param moment
     * @return retourne le moment du début de la mesure qui contient le moment
     * (les mesures sont matérialisées par les objets BarreDeMesure)
     */
    Moment getMesureMomentDebut(Moment moment);

    /**
     *
     * @param moment
     * @return le moment de la fin de la mesure qui contient le moment moment
     * (cette fonction est certifié comme ne renvoyant jamais NULL)
     */
    Moment getMomentMesureFin(Moment moment);

    MomentElementsMusicauxSurLeFeu getMomentNotesSurLeFeu(Moment momentActuel);

    /**
     *
     * @param momentactuel
     * @return renvoie le prochain moment "où il se passe quelquechose". C'est à dire
     * le prochain moment où un élément musical débute ou un élément musical finit.
     */
    Moment getMomentSuivant(Moment momentactuel);



    
    /**
     *
     * @param momentactuel
     * @param parties
     * @return renvoie le prochain moment "où il se passe quelquechose" dans
     *  les parties parties. C'est à dire
     * le prochain moment où un élément musical débute ou un élément musical finit dans les parties concernées.
     */
    public Moment getMomentSuivant(Moment momentactuel, Collection<Partie> parties);



            
    /**
     *
     * @param momentactuel
     * @return renvoie le prochain moment qui contient un élément musical qui débute
     */
    Moment getMomentSuivantAvecElementsMusicauxQuiDebutent(Moment momentactuel);

    /**
     *
     * @param momentactuel
     * @return le prochain moment où il se passe qch comme :
     * - une note, silence qui s'arrête
     * - une clef
     * - un tempo
     * - une barre de mesure
     * - bref tout sauf des choses qui sont "supplémentaires" comme courbe, texte et images etc.
     */
    Moment getMomentSuivantDebutFinNotesSilencesClefsEtc(Moment momentactuel);


    /**
     *
     * @param momentactuel
     * @return le prochain moment où il se passe qch comme :
     * - une note, silence qui s'arrête
     * - une clef
     * - un tempo
     * - une barre de mesure
     * - bref tout sauf des choses qui sont "supplémentaires" comme courbe, texte et images etc.
     */
    Moment getMomentSuivantDebutFinNotesSilencesClefsEtc(Moment momentactuel, Collection<Partie> parties);




    int getNbNoiresParMinute(Moment moment);

    /**
     *
     * @param moment
     * @param portee
     * @return l'ensemble des notes qui commencent au moment et qui sont dans
     * la portée
     */
    Set<Note> getNotesPorteeQuiCommencent(Moment moment, Portee portee);

    /**
     *
     * @return une sélection qui contient tous les éléments musicaux de l'ensemble
     */
    Selection getSelectionTout();

    /**
     *
     * @param moment
     * @return une sélection qui contient tous les éléments musicaux qui sont après
     * (non strict) le moment moment.
     */
    Selection getSelectionToutApresMoment(Moment moment);

    /**
     *
     * @param partie
     * @return tous les éléments musicaux sur portée qui sont dans la partie partie
     */
    Selection getSelectionToutPartie(Partie partie);

    Tonalite getTonalite(Moment moment);

    Tonalite getTonalite(Moment moment, Partie partie);

    Tonalite getTonaliteJusteAvant(Moment moment);

    Tonalite getTonaliteJusteAvant(Moment moment, Partie partie);

    /**
     *
     * @param moment
     * @return vrai ssi il y a une barre de mesure au moment moment.
     */
    boolean isBarreDeMesure(Moment moment);

    boolean isDebutMesure(Moment moment);

    /**
     *
     * @param moment
     * @return true ssi le moment ne contient pas de notes
     */
    boolean isMomentPasDeNotes(Moment moment);

    /**
     *
     * @param moment
     * @return true ssi le moment ne contient pas d'élément musical qui commence à ce moment là
     */
    boolean isMomentVide(Moment moment);

    /**
     *
     * @return vrai ssi il n'y a pas d'éléments musicaux
     */
    boolean isVide();

    boolean porteeIsMesureVide(Portee p, Moment debutMoment);

    /**
     *
     * @param p
     * @param debut
     * @param fin
     * @return retourne true ssi il n'y a aucun élément musical sur la portée p
     * entre le moment debut inclus et le moment fin exclus
     */
    boolean porteeIsVide(Portee p, Moment debut, Moment fin);

    Curseur voixDeviner(Curseur curseur, Duree dureeProposee);

}
