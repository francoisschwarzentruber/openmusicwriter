/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import musicwriter.donnees.*;
import musicwriter.gui.Graphics;
import musicwriter.gui.Point;
import musicwriter.gui.Polygon;
import musicwriter.gui.Rectangle;

/**
 *
 * @author proprietaire
 */
public class PartitionVue {
    private double systemesLongueur = 0; // it is defined in the constructor
    private final double interligne; 
    private final Moment momentDebut;
    private final double pageHeight;
    private final PartitionDonneesGetter partitionDonnees;
    private ArrayList<Systeme> systemes = null;
    private Collection<Partie> parties;


    private static final Color colorHighLightElementMusicalAvantMemeVoix = new Color(0.8f, 0.4f, 0.4f);
    private static final Color colorHighLightElementMusicalAvantPasMemeVoix = new Color(0.5f, 0.5f, 0.5f);
    private static final Color colorHighLightElementMusicalSurCurseur = new Color(0.9f, 0.5f, 0.4f);


    public void afficherContexteFond(Graphics g, Curseur curseur) {
        getSysteme(curseur.getMoment()).afficherContexteFond(g, curseur);
    }

    public Collection<Partie> getPartiesAffichees() {
        return parties;
    }



    

    
    public void afficherContexte(Graphics g, Curseur curseur) {
        MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = partitionDonnees.getMomentNotesSurLeFeu(curseur.getMoment());
        Set<ElementMusicalDuree> elementsMusicauxQuiSontRelachees = momentNotesSurLeFeu.getElementsMusicauxQuiSontRelachees();


        /**
         * affiche les éléments musicaux qui sont avant en surbrillance
         */

        

        if(curseur.isSurNote() || !curseur.isSurElementMusical())
        for(ElementMusicalDuree el : elementsMusicauxQuiSontRelachees)
        {
            if(el instanceof ElementMusicalSurVoix)
            {
                ElementMusicalSurVoix elv = (ElementMusicalSurVoix) el;
                AffichageElementMusical a = getAffichageElementMusical(elv);
                
                if(elv.getVoix().equals(curseur.getVoix()))
                {
                    g.setColor(colorHighLightElementMusicalAvantMemeVoix);
                    g.setAlpha(1.0f);
                    a.draw(g);
                }
                else
                    g.setColor(colorHighLightElementMusicalAvantPasMemeVoix);
                    //afficherElementMusical(g, elv);
                    
                    g.setAlpha(0.4f);
                    
                    g.setPenWidth(3.0f);
                    if(a instanceof AffichageNote)
                    {
                        g.drawLine((int) ((AffichageNote) a).getX(),
                                    (int) ((AffichageNote) a).getNoteCentreY(),
                                    (int) getSysteme(elv.getFinMoment()).getXNotes(elv.getFinMoment()),
                                    (int) ((AffichageNote) a).getNoteCentreY());
                        
                    }
                    else
                    if(a instanceof AffichageSilence)
                    {
                        g.drawLine((int) ((AffichageSilence) a).getX(),
                                    (int) ((AffichageSilence) a).getY(),
                                    (int) getSysteme(elv.getFinMoment()).getXNotes(elv.getFinMoment()),
                                    (int) ((AffichageSilence) a).getY());

                    }
                    
                
            }
        }
        

        if(curseur.isSurElementMusical())
        {
            g.setColor(colorHighLightElementMusicalSurCurseur);
            afficherElementMusical(g, curseur.getElementMusical());

        }
        else
        {
            //affiche l'accord sous lequel le curseur se trouve
            AffichageAccord accordCourant = getAffichageAccordProche(curseur);
            if(accordCourant != null)
            {
                g.setColor(colorHighLightElementMusicalSurCurseur);

                accordCourant.afficherHampeJusqua(g, curseur);

                g.setColor(colorHighLightElementMusicalSurCurseur);

                g.setAlpha(0.2f);

                Rectangle r = accordCourant.getRectangle();
                r.grow(4, 4);
                r.add(accordCourant.getSysteme().getPoint(curseur));
                g.fillRect(r);

                g.setAlpha(0.5f);

                accordCourant.draw(g);

            }
        }
        

        
        //on remet un affichage sans transparence
        g.setAlpha(1.0f);
        
    }




    /**
     *
     * @param polygon
     * @return les systèmes qui touchent le polygone polygon
     */
    private Set<Systeme> getSystemesInPolygone(Polygon polygon) {
        Set<Systeme> set = new HashSet<Systeme>();
        
        for(Systeme systeme : systemes)
        {
            if(polygon.intersects(systeme.getRectangle()))
                set.add(systeme);
            
        }
        return set;
    }



    /**
     *
     * @param polygon
     * @return la sélection qui contient tous les éléments musicaux contenus
     * dans le polygone polygon
     * s'il n'y a pas d'éléments musicaux, renvoie une sélection vide (non null)
     */
    public Selection getSelectionPolygone(Polygon polygon) {
       Selection selection = new Selection();

        for(Systeme systeme : getSystemesInPolygone(polygon))
        {
            selection.elementsMusicauxAjouter(systeme.getSelectionPolygone(polygon));
        }

        return selection;
        
    }
    
    
    
    

    /**
     *
     * @param el
     * @return l'afficheur d'un élément musical.
     * Si l'élément musical n'est pas dans la vue, ça retourne null.
     */
    public AffichageElementMusical getAffichageElementMusical(ElementMusical el)
    {
        
        Set<Systeme> systemesSet = getSystemes(el.getDebutMoment());
        for(Systeme systeme : systemesSet)
        {
            AffichageElementMusical a = systeme.getAffichageElementMusical(el);
            if(a != null)
                return a;
        }
        return null;
    }



    /**
     * affiche la partition, les éléments musicaux etc. présents dans visibleRect
     * @param g
     * @param visibleRect
     */
    public void afficherPartition(Graphics g, Rectangle visibleRect) {
        if(systemes == null)
            miseEnPageCalculer();

        Systeme systemeEnHaut
                = getSystemeContenantPoint(
                new Point((int) visibleRect.getMinX(),
                          (int) visibleRect.getMinY())
                                          );
        Systeme systemeEnBas
                = getSystemeContenantPoint(
                new Point((int) visibleRect.getMinX(),
                          (int) visibleRect.getMaxY())
                                          );


        for(int systemei = systemeEnHaut.getNumeroSysteme();
               systemei <= systemeEnBas.getNumeroSysteme();
               systemei++ )
        {
            systemes.get(systemei).afficher(g);
        }
    }


    /**
     * affiche les notes d'une sélection en rouge
     * @param g
     * @param selection
     */
    public void afficherSelection(Graphics g, Selection selection) {
        
       for(ElementMusical element : selection)
       {
          afficherElementMusical(g, element);
       }
    }


    /**
     * affiche des notes etc. qui ne sont pas enregistrées dans la partition
     * @param g
     * @param selection
     */
    public void afficherSelectionDehors(Graphics g, Selection selection) {
        /* affiche les notes d'une sélection en rouge*/
        
       for(ElementMusical element : selection)
       {
          afficherElementMusicalDehors(g, element);
       }
    }


    /**
     *
     * @return le nombre de système
     */
    public int getSystemesNombre() {
        return systemes.size();
    }


    /**
     * Définit la longueur des systèmes
     * @param systemeLongueur
     */
    public void setSystemesLongueur(int systemeLongueur) {
       this.systemesLongueur = systemeLongueur;
    }


    /**
     * affiche l'élément musical element (qui doit être présent dans la partition
     * et aussi présent dans cette vue)
     * @param g
     * @param note
     */
    public void afficherElementMusical(Graphics g, ElementMusical note) {
        Set<Systeme> systemesSet = getSystemes(note.getDebutMoment());
        
        for(Systeme systeme : systemesSet)
        {
            systeme.afficherElementMusical(g, note);
        }
    }


    /**
     * affiche l'élément musical element (qui n'est pas dans la partition)
     * @param g
     * @param elementMusical
     */
    public void afficherElementMusicalDehors(Graphics g, ElementMusical elementMusical) {
         Set<Systeme> systemesSet = getSystemes(elementMusical.getDebutMoment());
        
         for(Systeme systeme : systemesSet)
        {
            systeme.afficherElementMusicalDehors(g, elementMusical);
        }
    }


    /**
     *
     * @param moment
     * @return le système qui contient le momentDebut momentDebut
     * retourne null si aucun système ne contient ce momentDebut.
     */
    public Systeme getSysteme(Moment moment)
    {


        for(int systemeNumero = systemes.size() - 1;
            systemeNumero >= 0 ;
            systemeNumero--)
        {
            if(systemes.get(systemeNumero).getDebutMoment().isAvant(moment) &
               ( (systemeNumero == systemes.size() - 1) || systemes.get(systemeNumero).getFinMoment().isApres(moment) ))
            {
                return systemes.get(systemeNumero);
            }
        }
        return null;
    }


    /**
     *
     * @param momentDebut
     * @return l'ensemble des systèmes qui ont affaire avec le momentDebut. Généralement, il y en a
     * qu'un seul. Mais des fois, on a un momentDebut qui est la fin d'un système et au début d'un autre.
     */
    private Set<Systeme> getSystemes(Moment moment) {
        
        Set<Systeme> systemesSet = new HashSet<Systeme>();
        for(int systemeNumero = systemes.size() - 1;
            systemeNumero >= 0;
            systemeNumero--)
        {
            if(systemes.get(systemeNumero).getDebutMoment().isAvant(moment) &
               ( (systemeNumero == systemes.size() - 1) || systemes.get(systemeNumero).getFinMoment().isApres(moment) ))
            {
                systemesSet.add(systemes.get(systemeNumero));
            }
        }
        return systemesSet;
    }
    
    
    
    



    
    
    
    

    


    /**
     * Crée une vue de la partition partitionDonnees qui débute à partir du momentDebut
     * momentDebut.
     * Les propriétés géométriques sont les suivantes :
     *  - la longueur d'un système est systemesLongueur ;
     *  - la largeur de la page est pageHeight ;
     *         Remarque : si le paramètre pageHeight <= 0 alors cette classe
     *                    construit une vue à partir de momentDebut jusqu'à la
     *                    fin de la partition
     *  - l'interligne (i.e. demi-espacement entre deux lignes de portées vaut interligne)
     *     Pour l'écran interligne = 10 est une bonne valeur.
     * @param partitionDonnees
     * @param parties
     * @param momentDebut
     * @param systemeLongueur
     * @param interligne
     * @param pageHeight
     */
    public PartitionVue(PartitionDonnees partitionDonnees,
                        Collection<Partie> parties,
                        Moment momentDebut,
                        double systemeLongueur,
                        double interligne,
                        double pageHeight)
    {
        this.partitionDonnees = partitionDonnees;
        this.momentDebut = momentDebut;
        this.systemesLongueur = systemeLongueur;
        this.interligne = interligne;
        this.pageHeight = pageHeight;
        this.parties = parties;

        systemes = new ArrayList<Systeme>();

        
        miseEnPageCalculer();

    }
    
    
    


/**
 *
 * @return le momentDebut à partir du quel débute la vue
 */
    public Moment getMomentDebut()
    {
        return momentDebut;
    }

    public Moment getMomentApresLaFinDeLaPage()
    {
        return systemes.get(systemes.size() - 1).getMomentApresLaFinDuSysteme();
    }


    
    
    /**
     * affiche les systèmes sur g
     * @param g
     */
    public void afficherPartition(Graphics g)
    {
        if(systemes == null)
        {
            miseEnPageCalculer();
        }

        for(Systeme systeme : systemes)
        {
            systeme.afficher(g);
        }
    }

    
/**
     * affiche les éléments musicaux SANS les lignes de portées etc.
     * @param g
     */
    public void afficherElementsMusicauxSansLigne(Graphics g)
    {
        if(systemes == null)
        {
            miseEnPageCalculer();
        }

        for(Systeme systeme : systemes)
        {
            systeme.afficherElementsMusicaux(g);
        }
    }

    
/**
 *
 * @param point
 * @return le système où se trouve le point point (coordonnées en pixel)
 * Ne retourne JAMAIS null.
 */
    public Systeme getSystemeContenantPoint(Point point)
    {
        return getSystemeAvecY(point.y);
        
    }
    
/**
 *
 * @param y
 * @return le système où se trouve les points d'ordonnées y.
 * Ne retourne JAMAIS null.
 */
    public Systeme getSystemeAvecY(double y)
    {
        int meilleureevaluation = 10000000;
        Systeme meilleurSysteme = systemes.get(0);
        
        for(int numeroSysteme = 0; numeroSysteme < systemes.size(); numeroSysteme++)
        {
            int evaluation = systemes.get(numeroSysteme).getEvaluationAppartenance(y);
            if(evaluation < meilleureevaluation)
            {
                 meilleureevaluation = evaluation;
                 meilleurSysteme = systemes.get(numeroSysteme);
            }

        }

        return meilleurSysteme;

    }



    public Curseur getCurseur(Point point) {
        return getCurseur(point, new Duree(new Rational(0, 1)), null); // la durée est nul
    }


    public Moment getMomentBarre(Point point) {
        Systeme systeme = getSystemeContenantPoint(point);
        return systeme.getMomentBarre(point);
    }


/**
 *
 * @param point
 * @param dureeDemandee
 * @param porteePreferee
 * @return un curseur qui indique où on se trouve dans la partition.
 *    point représente le point (coordonnées en pixel)
 *    dureeDemandee représente la durée que l'on souhaite insérer.
 *    
 *    Le paramètre dureeDemandee est utile pour deux choses :
 *      - il permet de deviner dans quelle voix on écrit
 *      - il permet de renvoyer des curseurs qui valent un momentDebut clef de la partition
 *         moins la durée demandée
 *
 *    Si porteePreferee = null alors la fonction getCurseur prend la portée la plus proche du point point
 *    Sinon, la fonction getCurseur fait un compromis. Si le point n'est pas loin de la portée porteePreferee
 *    alors la portée du curseur sera porteePreferee. Sinon, ça prendra la portée la plus proche
 *
 */
    public Curseur getCurseur(Point point, Duree dureeDemandee, Portee porteePreferee) {
        Systeme systeme = getSystemeContenantPoint(point);
              
        Voix voix = systeme.getVoix(point, dureeDemandee);
             
        ElementMusical elementMusical = systeme.getElementMusical(point);

        final Portee portee = systeme.getPortee(point, porteePreferee);


        Hauteur hauteur = systeme.getHauteur(portee, point);
        //je connais tout ce qui est "vertical"

        Moment moment = systeme.getMoment(point, dureeDemandee);


        
        //if(elementMusical instanceof Note)
        if(elementMusical != null)
        {
            if(elementMusical instanceof ElementMusicalSurVoix)
                return new Curseur(moment, hauteur, portee, ((ElementMusicalSurVoix) elementMusical).getVoix(), elementMusical);
            else
                return new Curseur(moment, hauteur, portee, null, elementMusical);
        }
        
        
        
        

        
        final Curseur curseur = new Curseur(moment, hauteur, portee, voix);
        
        if(voix == null)
        {
            return partitionDonnees.voixDeviner(curseur, dureeDemandee);
        }
       
        
        return curseur;

        
    }

    
    public Curseur getCurseurVague(Point point) {
        Systeme systeme = getSystemeContenantPoint(point);

        Portee portee = systeme.getPortee(point);
        Hauteur hauteur = systeme.getHauteur(portee, point);
        //je connais tout ce qui est "vertical"

        Moment moment = systeme.getMomentVague(point);


        return new Curseur(moment, hauteur, portee, null);


    }


    public PartitionDonneesGetter getPartitionDonnees() {
        return partitionDonnees;
    }
    
    


    

    /**
     * réalise un calcul de mise en page sur tout le document
     */
    public void miseEnPageCalculer() {
        systemes = new ArrayList<Systeme>();
        miseEnPageCalculer(getMomentDebut());
        
    }
    
    

/**
 * réalise une mise en page sachant qu'il n'y a eu qu'une modification locale au moment
 * moment
 * @param moment
 */
    public void miseEnPageCalculer(Moment moment)
    {
        miseEnPageCalculer(moment, moment);
    }


    
    
    Systeme createSysteme(Moment debutMoment, int systemeNumero, double systemeYToutEnHaut)
    {
        return new Systeme(systemeNumero, getPartitionDonnees(),
                                      getPartiesAffichees(),
                                      systemeYToutEnHaut,
                                      getSystemesLongueur(),
                                      interligne,
                                      debutMoment
                                      );
        
    }
    
    
    
    

    /**
     * réalise la mise en page sachant que les modifications ont été effectuées entre
     * le momentDebut momentDebut et momentFinImperatif.
     * Si jamais il y a peu de modifications, la procédure ne recalculera pas
     * ce qui n'est pas nécessaire de miseEnPageCalculer !
     * @param momentDebut
     * @param momentFinImperatif
     */
    public void miseEnPageCalculer(Moment momentDebut, Moment momentFinImperatif) {
        
        
        
        
        Systeme systemeDebut = getSysteme(momentDebut);


        
        int systemeNumero;
        double systemeYToutEnHaut;
        Moment systemeCourantMomentDebut;

        if(systemeDebut == null)
        {
            systemeNumero = 0;
            systemeYToutEnHaut = 0;
            systemeCourantMomentDebut = getMomentDebut();
        }
        else
        {
            systemeNumero = systemeDebut.getNumeroSysteme();
            systemeYToutEnHaut = systemeDebut.getYToutEnHaut();
            systemeCourantMomentDebut = systemeDebut.getDebutMoment();

        }

        

        Systeme systeme = createSysteme(systemeCourantMomentDebut, systemeNumero, systemeYToutEnHaut);
        if(systemeNumero < systemes.size())
             systemes.set(systemeNumero, systeme);
        else
             systemes.add(systeme);
        
        systemeCourantMomentDebut = systeme.getMomentApresLaFinDuSysteme();
        
        while(systemeCourantMomentDebut != null)
        {
            systemeNumero++;

            /**
             * si ça se trouve on arrive finalement à vouloir construire un système qui le même momentDebut
             * de début.. or ce travail a déjà été fait par une opération de mise en page précédente...
             * Solution : on quitte cette fonction car tout est ok, c'est à dire :
             * - on est retombé sur nos pattes
             * - et ceci n'a pas été modifié (on a dépassé STRICTEMENT momentFinImperatif)
             * (avant il y avait un bug quand ce n'était pas "strictement")
             */
            if(systemeNumero < systemes.size())
            if(systemeCourantMomentDebut.isEgal(systemes.get(systemeNumero).getDebutMoment()) &
               systemes.get(systemeNumero).getDebutMoment().isStrictementApres(momentFinImperatif))
                return;
            
            systemeYToutEnHaut = systeme.getYToutEnBas();


              


            systeme = new Systeme(systemeNumero,
                                  partitionDonnees,
                                  getPartiesAffichees(),
                                  systemeYToutEnHaut,
                                  getSystemesLongueur(),
                                  interligne,
                                  systemeCourantMomentDebut
                                  );


            /**si le système n'entre pas dans la page*/
            if(pageHeight > 0)
              {
                 if(systeme.getYToutEnBas() > pageHeight)
                 {
                     return;
                 }
              }

            if(systemeNumero < systemes.size())
                 systemes.set(systemeNumero, systeme);
            else
                 systemes.add(systeme);
            
            systemeCourantMomentDebut = systeme.getMomentApresLaFinDuSysteme();
            
        }

        /**
         * supprime les systèmes superflus... car peut-être maintenant, la partition est plus petite
         */
        while(systemes.size() > systemeNumero+1)
             systemes.remove(systemeNumero+1);





        
        
    }
        
    public Dimension getDimension()
    {
        if(systemes.isEmpty())
        {
            return new Dimension((int) systemesLongueur+100, 200);
        }
        else
        {
            Systeme dernierSysteme = systemes
                              .get(systemes.size() - 1);
            return new Dimension((int) systemesLongueur+100,
                             (int) (dernierSysteme
                               .getY(dernierSysteme.getPorteeDerniere(),
                                     -50)));
        }
        
    }





    public AffichageAccord getAffichageAccordProche(Curseur curseur) {
        Systeme systeme = getSysteme(curseur.getMoment());
        return systeme.getAffichageAccordProche(curseur);
    }

    
    
    public ElementMusical getElementMusical(Point point) {
        Systeme systeme = getSystemeContenantPoint(point);
        return systeme.getElementMusical(point);
    }
    
    private ElementMusical getElementMusicalProche(Point point) {
        Systeme systeme = getSystemeContenantPoint(point);
        return systeme.getElementMusicalProche(point);
    }
    
    
    
    public Note getNote(Point point) {
        ElementMusical el = getElementMusical(point);
        if(el instanceof Note)
            return (Note) el;
        else
            return null;
    } 
    
    
    
    public Silence getSilence(Point point) {
        ElementMusical el = getElementMusical(point);
        if(el instanceof Silence)
            return (Silence) el;
        else
            return null;
    } 
        
        
    public AffichageAccord getAffichageAccordAvecHampeFin(Point pointdebut) {
         Systeme systeme = getSystemeContenantPoint(pointdebut);
         return systeme.getAffichageAccordAvecHampeFin(pointdebut);
    }

    public double getSystemesLongueur() {
        return systemesLongueur;
    }

    public Partie getPartieSiDevantSysteme(Point point) {
        return getSystemeContenantPoint(point).getPartieSiDevantSysteme(point);
    }

    public void setParties(Collection<Partie> parties) {
        this.parties = parties;
        miseEnPageCalculer();
    }

    public AffichageCrocheReliee getAffichageCrocheRelie(Point point) {
        return getSystemeAvecY(point.y).getAffichageCrocheRelie(point);
    }

    /**
     * 
     * @return the width of the view
     */
    public int getWidth()
    {
        return (int) systemesLongueur;
    }
    
    
    /**
     * 
     * @return the ordinates of the point that is the most on the bottom
     */
    public int getYToutEnBas()
    {
        return (int) getSystemeDernier().getYToutEnBas();
    }

    private Systeme getSystemeDernier() {
        return systemes.get(systemes.size()-1);
    }

    public Note getNoteProche(Point point) {
        ElementMusical el = getElementMusicalProche(point);
        if(el instanceof Note)
            return (Note) el;
        else
            return null;
    }



}
