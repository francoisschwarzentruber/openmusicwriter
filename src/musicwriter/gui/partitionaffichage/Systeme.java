/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;


import java.awt.Color;
import java.awt.Font;
import java.util.Map.Entry;
import java.util.*;
import musicwriter.Arithmetique;
import musicwriter.Erreur;
import musicwriter.donnees.*;
import musicwriter.gui.*;

/**
 * Un système est une ligne de portées.
 * @author proprietaire
 */
public class Systeme
    {
        static final Image imgAccolade = ImageLoader.getImage("accolade.png");
        private final Image imgDiese = ImageLoader.getImage("diese.png");
        private final Image imgDoubleDiese = ImageLoader.getImage("double-diese.png");
        private final Image imgDoubleBemol = ImageLoader.getImage("double-bemol.png");
        private final Image imgBemol = ImageLoader.getImage("bemol.png");
        private final Image imgBecarre = ImageLoader.getImage( "becarre.png");

        private final Image imgNoteErreur = ImageLoader.getImage( "note_erreur.png");

        static public final double nbpixelDureeNoireParDefaut = 32;
        private final double interligne_pixel;

        private final PartitionDonneesGetter partitionDonnees;

        private final int numero;
        private final Moment momentApresLaFinDuSysteme;
        private final AffichageSysteme affichage;
        private final double lignesDebutX;
        private final HashMap<Portee, Double> porteeTroisiemeLigneY = new HashMap<Portee, Double>();

        private final double ytoutenhaut;
        private final double systemeLongueur;
        private double xFinDernierElementsMusicaux;
        private final Moment systemeCourantMomentDebut;
        private final Collection<Partie> parties;

/**
 * crée un système, i.e. une ligne dans la partition
 * numero = numéro du système dans le système d'affichage (pas très utile, le virer ?)
 * partitionDonnees = la partition
 *
 * @param numero
 * @param partitionDonnees
 * @param premierePorteeTroisiemeLigneY
 * @param porteesEcart
 * @param systemeLongueur
 * @param interligne
 * @param systemeCourantMomentDebut
 */
        Systeme(int numero,
        PartitionDonneesGetter partitionDonnees,
        Collection<Partie> parties,
        double ytoutenhaut,
        double systemeLongueur,
        double interligne,
        Moment systemeCourantMomentDebut)
        {
            this.affichage = new AffichageSysteme(this, partitionDonnees);
            this.numero = numero;
            this.partitionDonnees = partitionDonnees;
            this.parties = parties;
            this.ytoutenhaut = ytoutenhaut;
            this.systemeCourantMomentDebut = systemeCourantMomentDebut;
            this.systemeLongueur = systemeLongueur;
            this.xFinDernierElementsMusicaux = systemeLongueur;
            this.interligne_pixel = interligne;

            if(isPartiesNomsAffichees())
                this.lignesDebutX = 6*interligne;
            else
                this.lignesDebutX = getAccoladeWidth();

            //première distribution des portées pour pouvoir placer des notes
            porteesDistribution();

            this.momentApresLaFinDuSysteme = remplirJusquALaMarge();
            


        }


        /**
         *
         * @return le nombre de pixel pour une durée d'une noire (par défaut)
         */
    static public double getNbpixelDureeNoireParDefaut() {
        return nbpixelDureeNoireParDefaut;
    }





        /**
         *
         * @return le numéro du système (c'est donné à la création du système)
         * Le premier système d'une partition a le numéro 0. Le suivant a le nuémro 1 etc.
         */
        public int getNumeroSysteme() {
            return numero;
        }





        

        /**
         *
         * @param portee 
         * @return le rayon d'une note
         */
        public double getNoteRayon(Portee portee)
        {
            return (getInterLigne(portee) / 2) * 0.99;
        }


    /**
     *
     * @return le rectangle reservé à l'affichage du système
     */
    public Rectangle getRectangle() {
        double y = getYToutEnHaut();
        return RegionFactory.createRectangle(0,
                             (int) y,
                             (int) systemeLongueur,
                             (int) (getYToutEnBas() - y));
    }



/**
 *
 * @return l'abscisse où les lignes des portées s'arrêtent à droite (la longueur du système).
 */
    public double getXFin() {
        return systemeLongueur;
    }


    /**
     *
     * @return l'abscisse où il y a le dernier élément musical si le système n'est pas rempli jusqu'à la fin
     * Si le système est rempli jusqu'à la fin, renvoie comme getXFin()
     */
    public double getXFinDernierElementsMusicaux() {
        return xFinDernierElementsMusicaux;
    }


    

    /**
     *
     * @param point
     * @param dureeDemandee
     * @return la voix de l'éventuel accord qui se trouve sous le point point
     * et qui en plus dure exactement la longueur dureeDemandee
     * S'il n'y en a pas, retourne null.
     * Si par exemple point est un point où il y a dessus une blanche, et que dureeDemandee = 2
     * alors cette fonction renvoie la voix dans laquelle se trouve cette blanche.
     */
    Voix getVoix(Point point, Duree dureeDemandee) {
        AffichageAccord a = affichage.getAffichageAccord(point, dureeDemandee);
        if(a == null)
            return null;
        else
            return a.getVoix();
    }

    /**
     *
     * @return le nombre de pixels (ou autre, ça dépend de l'unité de mesure)
     * qu'il y a entre deux lignes horizontales consécutives de la portée p
     */
        double getInterLigne(Portee p) {
            if(getPartieNombre() == 2)
            {
                if((getPartitionDonnees().getPartie(0).getPorteesNombre() != 2) &&
                        (getPartitionDonnees().getPartie(1).getPorteesNombre() == 2) )
                {
                    if(getPartitionDonnees().getPartie(0).contains(p))
                        return interligne_pixel * 0.8;
                    else
                        return interligne_pixel;
                }
                else
                    return interligne_pixel;
            }
            else
            if(getPartieNombre() == 3)
            {
                if((getPartitionDonnees().getPartie(0).getPorteesNombre() != 2) &&
                        (getPartitionDonnees().getPartie(1).getPorteesNombre() != 2) &&
                        (getPartitionDonnees().getPartie(2).getPorteesNombre() == 2) )
                {
                    if(getPartitionDonnees().getPartie(0).contains(p) ||
                            getPartitionDonnees().getPartie(1).contains(p))
                        return interligne_pixel * 0.8;
                    else
                        return interligne_pixel;
                }
                else
                    return interligne_pixel;
            }
            else
                 return interligne_pixel;
        }
        

        /**
         *
         * @return la longueur d'une ligne supplémentaire pour dessiner des notes qui sont en dehors
         * des 5 lignes traditionnelles d'une portée
         */
        public double ligneSupplementaireLongueurGet()
        {
            return 1.4 * interligne_pixel;
        }
        
        /**
         *
         * @return le moment par lequel le prochain système devrait commencer.
         * Si on a atteint la fin de la partition, cette fonction renvoie null.
         */
        public Moment getMomentApresLaFinDuSysteme() {
            return momentApresLaFinDuSysteme;
        }
        


        /**
         *
         * @param polygon
         * @return une sélection d'éléments musicaux qui intersectent le polygone
         * polygon et qui sont sur ce système
         */
        Selection getSelectionPolygone(Polygon polygon)
        {
            return affichage.getSelection(RegionFactory.createRegion(polygon));
        }
        
        
    
        
        
        /**
         *
         * @param point
         * @return le moment présent dans la partition où se trouve le point point
         */
        public Moment getMoment(Point point) {
            return affichage.getMoment(point);
        }

         /**
         *
         * @param point
         * @param dureeDemandee 
         * @return le moment où se trouve le point point, sachant que le résultat
          * est soit un moment présent dans la partition, soit un moment tel que
          * ce moment + dureeDemandee est un moment présent dans la partition.
         */
        public Moment getMoment(Point point, Duree dureeDemandee) {
            return affichage.getMoment(point, dureeDemandee);
        }


        public Moment getMomentBarre(Point point) {
             return affichage.getMomentBarre(point);
        }


        
        /**
         * 
         * @return tous les objets affichage qui correspondent à des accords
         */
        public Set<AffichageAccord> getAffichageAccords()
        {
            return affichage.getAffichageAccords();
        }


        public AffichageAccord getAffichageAccordAvecHampeFin(Point extremitePoint)
        {
            return affichage.getAffichageAccordAvecHampeFin(extremitePoint);
        }


        /**
         * affiche une barre qui fait toute la ligne au moment moment.
         * Utilisé pour la lecture MIDI
         * @param g
         * @param moment
         */
        public void afficherBarreComplete(Graphics g, Moment moment) {
            afficherBarreComplete(g, getXNotes(moment));

        }


        private void afficherBarreComplete(Graphics g, double x) {
            double y1 = getYHaut();
            double y2 = getYBas();

            g.drawLine((int) x,
                       (int) y1,
                       (int) x,
                       (int) y2);

        }



        public void afficherBarreCompleteDepasse(Graphics g, Moment moment) {
            afficherBarreCompleteDepasse(g, getXNotes(moment));

        }

        private void afficherBarreCompleteDepasse(Graphics g, double x) {
            double y1 = getYToutEnHaut();
            double y2 = getYToutEnBas();

            g.drawLine((int) x,
                       (int) y1,
                       (int) x,
                       (int) y2);

        }


        public void afficherBarreCompleteMorcele(Graphics g, double x) {
            final double y1 = getYHaut();
            final double y2 = getYBas();
            final int ecartX = 4;
            double y = y1;
            while(y < y2)
            {
                g.drawLine((int) x-ecartX,
                       (int) y,
                       (int) x+ecartX,
                       (int) y+16);
                g.drawLine((int) x+ecartX,
                       (int) y+16,
                       (int) x-ecartX,
                       (int) y+32);
                y += 32;
            }
            

        }
    
    
    /**
     *
     * @return l'ordonnée d'un point situé sur la première portée en haut (qui est sur
     * la cinquième ligne)
     */
    public double getYHaut()
    {
        return getY(getPorteePremiere(), 4);
    }
    
    
    /**
     *
     * @return l'ordonnée d'un point situé sur la dernière portée en bas (qui est sur
     * la première ligne)
     */
    public double getYBas()
    {
        return getY(getPorteeDerniere(), -4);
    }



     /**
         * affiche une barre (comme une barre de mesure) à l'abscisse x.
         * @param g
         * @param x
         */
    public void afficherBarre(Graphics g, double x) {
        for(Partie partie : getParties())
        {
            g.drawLine(x,
                       getY(partie.getPorteePremiere(), 4),
                       x,
                       getY(partie.getPorteeDerniere(), -4));
        } 
    }

    public void afficherElementMusical(Graphics g, ElementMusical el) {
        affichage.afficher(g, el);
    }



    private AffichageElementMusical createAffichageElementMusicalDefaut(ElementMusical el)
    {
        if(el instanceof Note)
        {
             return null;
        }
        else if(el instanceof Silence)
        {
             return (new AffichageSilence(this, getXNotes(el.getDebutMoment()), (Silence) el));
        }
        else if(el instanceof BarreDeMesure)
        {
            return (new AffichageBarreDeMesure(this, getXBarreAuDebut(el.getDebutMoment()), (BarreDeMesure) el));
        }
        else if(el instanceof ElementMusicalChangementMesureSignature)
        {
            ElementMusicalChangementMesureSignature ms = partitionDonnees.getElementMusicalMesureSignature(el.getDebutMoment());

            if(ms != null)
                if(ms.getDebutMoment().equals(el.getDebutMoment()))
                {
                    return new AffichageChangementMesureSignature(this,
                         affichage.getAffichageElementMusical(ms).getX(),
                        (ElementMusicalChangementMesureSignature) el);
                    
                }

            return new AffichageChangementMesureSignature(this
                        , getXBarreAuDebut(el.getDebutMoment()),
                        (ElementMusicalChangementMesureSignature) el);


        }
        else if(el instanceof ElementMusicalChangementTonalite)
        {
            ElementMusicalChangementTonalite ct = partitionDonnees.getElementMusicalChangementTonaliteCourant(el.getDebutMoment());

            if(ct != null)
                if(ct.getDebutMoment().equals(el.getDebutMoment()))
                {
                    return new AffichageChangementTonalite(this,
                         affichage.getAffichageElementMusical(ct).getX(),
                        (ElementMusicalChangementTonalite) el);
                }


            return 
                    new AffichageChangementTonalite(this,
                    (ElementMusicalChangementTonalite) el);


        }
        else if(el instanceof ElementMusicalClef)
        {
            ElementMusicalClef elc = (ElementMusicalClef) el;
            ElementMusicalClef ct = elc.getPortee().getElementMusicalClef(el.getDebutMoment());

            if(ct != null)
                if(ct.getDebutMoment().equals(el.getDebutMoment()))
                {
                    return new AffichageElementMusicalClef(this,
                         affichage.getAffichageElementMusical(ct).getX(),
                        elc);
                }

            return 
                    new AffichageElementMusicalClef(this,
                    elc);


        }
        else if(el instanceof ElementMusicalTempo)
        {
            ElementMusicalTempo ct = (ElementMusicalTempo)
                    partitionDonnees.getElementMusical(el.getDebutMoment(), ElementMusicalTempo.class);

            if(ct != null)
                 return (new AffichageElementMusicalTempo(this,
                                              affichage.getAffichageElementMusical(ct).getX(),
                                              (ElementMusicalTempo) el));
            else
                return (new AffichageElementMusicalTempo(this,
                                              getXNotes(el.getDebutMoment()),
                                              (ElementMusicalTempo) el));
        }
        else if(el instanceof ElementMusicalArpege)
        {
            return (new AffichageElementMusicalArpege(this,
                                              getXNotes(el.getDebutMoment()),
                                              (ElementMusicalArpege) el));
        }
        else
        if(el instanceof ElementMusicalCourbe)
            return new AffichageElementMusicalCourbe(this, (ElementMusicalCourbe) el);
        else
        if(el instanceof ElementMusicalParoleSyllabe)
            return new AffichageElementMusicalParoleSyllabe(this, (ElementMusicalParoleSyllabe) el);
        if(el instanceof ElementMusicalTexte)
            return new AffichageElementMusicalTexte(this, (ElementMusicalTexte) el);
        else
        if(el instanceof ElementMusicalImage)
              return new AffichageElementMusicalImage(this, (ElementMusicalImage) el);
        else
        if(el instanceof ElementMusicalLigne)
              return new AffichageLigne(this, (ElementMusicalLigne) el);
        else
        if(el instanceof ElementMusicalCrescendoDecrescendo)
              return new AffichageElementMusicalCrescendoDecrescendo(this, (ElementMusicalCrescendoDecrescendo) el);
        else
        if(el instanceof Octaviation)
              return new AffichageOctaviation(this, (Octaviation) el);

        return null;
    }

    

    public void afficherElementMusicalDehors(Graphics g, ElementMusical el) {
        if(el instanceof Note)
        {
             AffichageAccord affichageAccord = (new AffichageAccord(this, getXNotes(el.getDebutMoment()), new Accord((Note) el)));
             affichageAccord.draw(g);
           //  (new AffichageCrocheRelieeDehors(this, affichageAccord)).draw(g);
             (new AffichageCrocheReliee(this, affichageAccord)).draw(g);
        }
        else
             createAffichageElementMusicalDefaut(el).draw(g);

        
    }

        
        
        
        
        
        
        
        
        /**
         *
         * @return le moment au tout début du système (complétement
               à gauche)
         */
        public Moment getDebutMoment() {
            return systemeCourantMomentDebut;
        }
        
        
        /**
         *
         * @return  le moment au tout début du système (complétement
               à gauche)
         */
        public Moment getFinMoment() {
            
            if(affichage.isEmpty())
                 return getDebutMoment();
            else
                 return affichage.getFinMoment();
        }
        
        

/**
 * affiche le système
 * @param g
 */
        public void afficher(Graphics g) {
            /* afficher un système =
             *  1. afficher les lignes des portées
             *  2. afficher les notes qu'il y a dessus
             */


            if(AffichageOptions.grilleEcriture)
                afficherReperes(g);

            
            g.setColor(Color.BLACK);

           afficherAccolades(g);


           if(isPartiesNomsAffichees())
               afficherPartiesNoms(g);
           
           afficherPorteesLignes(g);

 
           afficherElementsMusicaux(g);

           afficherErreurs(g);

           
           
        }
        




        private int getPorteeCoordonneeVerticale(Portee portee, Point point)
        {
            double ydanslaportee = (point.y - getPorteeTroisiemeLigneY(portee));
            ydanslaportee = -ydanslaportee;

            return Arithmetique.divisionReelleVersEntier(
                    ydanslaportee + interligne_pixel / 4,
                    (interligne_pixel / 2));
        }


/**
 *
 * @param portee
 * @param point
 * @return la hauteur (sans altération) en fonction de la portée
               et du point
               ex : si portée = une portée avec clef de sol,
                     et point sur la 2e ligne,
                    renvoie "sol0" */
        public Hauteur getHauteur(Portee portee, Point point) {
            
            Moment moment = getMoment(point);
            
            int coordonneeVerticale = getPorteeCoordonneeVerticale(portee, point);
            
            Hauteur hauteur = portee.getHauteur(moment, coordonneeVerticale);

            hauteur.setAlteration(partitionDonnees.getAlterationParDefaut(getMoment(point), getDebutMoment(), portee, hauteur));
            return hauteur;
        }

        
        /**
         * @param point
         * @return l'élément musical qui est sous le point point
        sinon renvoie null
         */
        public ElementMusical getElementMusical(Point point) {
            return affichage.getElementMusical(point);
            
        }

        
        public ElementMusical getElementMusicalProche(Point point) {
            return affichage.getElementMusical(point);
            
        }
        

        
        /**
         * Remplit le système jusqu'au bout. Tente de s'arrêter à une barre de
         * mesure si c'est possible.
         * @return le moment à partir duquel doit commencer le prochain système.
         */
        private Moment remplirJusquALaMarge()
        {
                Moment momentactuel = systemeCourantMomentDebut;
                Moment momentsuivant = momentactuel;
                double x = lignesDebutX;
                for(;
                    (momentactuel != null) && (x < systemeLongueur);
                    momentactuel = momentsuivant)
                {
                      MomentElementsMusicauxSurLeFeu notesSurLeFeu = partitionDonnees.getMomentNotesSurLeFeu(momentactuel);
                      notesSurLeFeu.filter(getParties());

                      Set<ElementMusical> elementsMusicaux = notesSurLeFeu.getElementsMusicauxRassemblesEnAccordsAJouer();

                      for(Iterator<ElementMusical>it = elementsMusicaux.iterator() ; it.hasNext() ; )
                      {
                                ElementMusical el = it.next();
                                if(el instanceof ElementMusicalCourbe)
                                {
                                    it.remove();
                                }
                      }


                      if(momentactuel.equals(systemeCourantMomentDebut))
                      {
                          ElementMusicalChangementTonalite ct = partitionDonnees.getElementMusicalChangementTonaliteCourant(momentactuel);
                          if(ct != null)
                              if(!ct.getDebutMoment().equals(momentactuel))
                                  elementsMusicaux.add(ct);

                          for(Portee p : getPortees())
                          {
                              ElementMusicalClef elc = p.getElementMusicalClef(momentactuel);

                              if(elc != null)
                                  if(!elc.getDebutMoment().equals(momentactuel))
                                      elementsMusicaux.add(elc);
                          }
                      }

                      //if(momentactuel.isEgal(getDebutMoment()))
                      if(momentactuel.equals(systemeCourantMomentDebut))
                      {
                           for(Iterator<ElementMusical>it = elementsMusicaux.iterator() ; it.hasNext() ; )
                            {
                                    ElementMusical el = it.next();
                                    if(el instanceof BarreDeMesure)
                                    {
                                        it.remove();
                                    }
                            }
                      }
                      

                      momentsuivant = partitionDonnees.getMomentSuivantDebutFinNotesSilencesClefsEtc(momentactuel, getParties());
                      

                      AffichageMoment affichageMoment = new AffichageMoment(this,
                                                                            x,
                                                                            momentactuel,
                                                                            elementsMusicaux);
                                                 
                         affichage.add(affichageMoment);
                      

                          if(momentsuivant != null)
                          {
                              if((new Duree(momentactuel, momentsuivant).isZero()))
                                  x = affichageMoment.getXFin();
                              else
                                x = affichageMoment.getXFin()
                                      + interligne_pixel * (
                                         Math.max(0,
                                                  1.0*Math.log(3*(new Duree(momentactuel, momentsuivant).getRational().getRealNumber()))));
//                              Math.max(0,
//                                                  1.0*Math.log(7*(new Duree(momentactuel, momentsuivant).getRational().getRealNumber()))));

                              if(partitionDonnees.isBarreDeMesure(momentsuivant))
                                   x += interligne_pixel*1;
                            //      x += interligne_pixel*1.7;

                              /**
                               * un moment où il y a rien
                               */
                              if(!affichageMoment.isContientAccordsOuSilences())
                                  x += 5 + (new Duree(momentactuel, momentsuivant).getRational().getRealNumber()) * nbpixelDureeNoireParDefaut;
                          }
                          else
                              xFinDernierElementsMusicaux = affichageMoment.getXFin();
                      
                      
                }


                if(!affichage.isEmpty())
                    affichage.miseEnPage();

                porteesDistribution();


               // réglage de l'affichage
                affichage.hampesCalculer();
                affichage.calculerCrochesReliees();
                ajouterElementsMusicauxSupplementaires();

                if(!affichage.isEmpty())
                {
                    if(affichage.isTermineParUnMomentARepeterSurSystemeSuivant())
                        return affichage.getDernierAffichageMoment().getMoment();
                    else
                        return partitionDonnees.getMomentSuivant(affichage.getDernierAffichageMoment().getMoment());
                }
                else
                    return null;
                
            }
        
        
        
        /**
         *
         * @param portee
         * @return l'ordonnée de la troisième ligne de la portée portee
         */
        public double getPorteeTroisiemeLigneY(Portee portee)
        {
            if(porteeTroisiemeLigneY.containsKey(portee))
            {
                return porteeTroisiemeLigneY.get(portee);
            }
            else
            {
                Erreur.message("ne trouve pas la portée qu'il faut afficher");
                return 0;
            }
        }
        
        
        /**
         *
         * @param point
         * @return la portée où le point point est.
         * Cette fonction suppose que le point est dans le bon système. Sinon, ça risque de renvoyer la
         * première portée ou la dernière portée selon que le point est bien avant ou bien après.
         */
        public Portee getPortee(Point point)
        {
            double distancemin = 100000000;
            Portee porteeDistanceMin = null;
            for(Entry<Portee, Double> e : porteeTroisiemeLigneY.entrySet())
            {
                if(Math.abs(point.y - e.getValue()) < distancemin)
                {
                    distancemin = Math.abs(point.y - e.getValue());
                    porteeDistanceMin = e.getKey();
                }
            }

            return porteeDistanceMin;
                       
        }


        /**
         *
         * @param portee
         * @param nbpoint
         * @return la taille de la police si on écrit sur la portée portee avec une taille de nbpoint
         * Plus l'interligne de la portée est petite et plus la taille de la police est petite.
         */
        public int getFontSize(Portee portee, int nbpoint)
        {
            return (int) ((nbpoint * getInterLigne(portee)) / 10.0);
        }


        /**
         *
         * @param nbpoint
         * @return la taille de la police en prenant en comptant en l'interligne
         */
        public int getFontSize(int nbpoint)
        {
            return (int) ((nbpoint * getInterLigneStandard()) / 10.0);
        }



        private String alterationToTexte(Hauteur.Alteration alteration)
        {
            switch(alteration)
            {
                case DIESE: return "♯";
                case DOUBLEDIESE:
                     return "x";
                case BEMOL:
                     return "♭";
                case DOUBLEBEMOL:
                     return "bb";
                case NATUREL:
                     return "♮";
                 default: return "";
            }
        }



        private Image getAlterationImageIcon(Hauteur.Alteration alteration)
        {
             switch(alteration)
            {
                case DIESE: return imgDiese;
                case DOUBLEDIESE: return imgDoubleDiese;
                case BEMOL: return imgBemol;
                case DOUBLEBEMOL: return imgDoubleBemol;
                case NATUREL: return imgBecarre;
                 default: return null;
            }
        }



        public void dessinerAlterationADroite(Graphics g,
                                    Hauteur.Alteration alteration,
                                    double x,
                                    Portee p,
                                    int coordonneeVerticale) {

//            g.setFont(new Font(g.getFont().getName(), Font.BOLD, getFontSize(16) ));
           // String alteration_texte = alterationToTexte(alteration);
           double y = getY(p, coordonneeVerticale);

            Image img = getAlterationImageIcon(alteration);
          //  if(alteration_texte != null)
               if(!g.getColor().equals(Color.BLACK))
               {
                   g.fillRect(
                            (int) x,
                            (int) (y -  (img.getHeight() / 2)*getInterLigne(p)/20),
                            (int) (img.getWidth()*getInterLigne(p)/20),
                            (int) (img.getHeight()*getInterLigne(p)/20));
               }

               g.drawImage(img,
                            (int) x,
                            (int) (y -  (img.getHeight() / 2)*getInterLigne(p)/20),
                            (int) (img.getWidth()*getInterLigne(p)/20),
                            (int) (img.getHeight()*getInterLigne(p)/20));


        }


        public void dessinerAlterationGauche(Graphics g,
                                    Hauteur.Alteration alteration,
                                    double x,
                                    Portee p,
                                    int coordonneeVerticale) {

          //  String alteration_texte =  alterationToTexte(alteration);
            //g.setFont(new Font(g.getFont().getName(), Font.BOLD, getFontSize(16) ));

            Image img = getAlterationImageIcon(alteration);
            double y = getY(p, coordonneeVerticale);
        //    int alteration_longueur_pixel = (int) g.getFontMetrics().getStringBounds(alteration_texte, g).getWidth();
           // if(alteration_texte != null)

            if(!g.getColor().equals(Color.BLACK))
            {
                g.fillRect(
                    (x - ((img.getWidth())*getInterLigne(p)/20)),
                    (y -  (img.getHeight() / 2)*getInterLigne(p)/20),
                    (img.getWidth()*getInterLigne(p)/20),
                    (img.getHeight()*getInterLigne(p)/20));

            }

            g.drawImage(img,
                (x - ((img.getWidth())*getInterLigne(p)/20)),
                (y -  (img.getHeight() / 2)*getInterLigne(p)/20),
                (img.getWidth()*getInterLigne(p)/20),
                (img.getHeight()*getInterLigne(p)/20));

        }


        /**
         * affiche uniquement les éléments musicaux du système
         * @param g
         */
    public void afficherElementsMusicaux(Graphics g) {
        affichage.draw(g);
    }

    
    private void afficherPorteeLignes(Graphics g, double xdebut, double xfin, Portee portee)
    {
        g.setPenWidth(1.0f);
        for(int coordonneeVerticale = -4;
            coordonneeVerticale <= 4;
            coordonneeVerticale+=2)
        {
            double yCoordonneeVerticale = getY(portee, coordonneeVerticale);
            g.drawLine(xdebut, yCoordonneeVerticale, xfin, yCoordonneeVerticale);
        }
    }
    
    
    
/**
 * afficher les lignes des portées (les 5 cinq pour les portées usuelles)
 * @param g
 */
    public void afficherPorteesLignes(Graphics g) {
        for(Portee portee : getPortees())
            afficherPorteeLignes(g,
                    lignesDebutX,
                    systemeLongueur,
                    portee);
            
    }


    private int getPartieInstrumentNomX()
    {
        return 0;
    }

    private int getPartieInstrumentNomY(Partie partie)
    {
        return (int)
                (getPorteeTroisiemeLigneY(partie.getPorteePremiere()) +
                getPorteeTroisiemeLigneY(partie.getPorteeDerniere())) / 2;
    }


    /**
     * affiche les noms des instruments au début du système (tout à gauche) en face
     * de chaque portée ou groupe de portées
     * @param g
     */
    private void afficherPartiesNoms(Graphics g)
    {
        
        for(Partie partie : getParties())
        {
            g.setFont(new Font("", 0, getFontSize(partie.getPorteePremiere(), 11)));
            g.drawString(partie.getNom(),
                         getPartieInstrumentNomX(),
                         getPartieInstrumentNomY(partie));
        }
    }
     
    



/**
 * 
 * @param portee
 * @param coordonneeVerticale
 * @return l'ordonnée d'un point situé sur la portée portee et que ce point est situé
     * avec la coordonnée verticale coordonneeVerticale.
     * coordonneeVerticale c'est :
     * -1 -> ligne du la0 en clef de sol
     * 0 -> ligne du si0 en clef de sol
     * 1 -> ligne du do1 en clef de sol
     * 2 -> ligne du ré1 en clef de sol
 */
    public double getY(Portee portee, int coordonneeVerticale)
    {
        return getPorteeTroisiemeLigneY(portee) - coordonneeVerticale * getInterLigne(portee) / 2;
    }



    public double getY(Curseur curseur)
    {
        return getY(curseur.getPortee(), curseur.getCoordonneeVerticaleSurPortee());
    }


    /**
     *
     * @param el
     * @return l'objet qui affiche l'élément musical el
     */
    public AffichageElementMusical getAffichageElementMusical(ElementMusical el) {
        return affichage.getAffichageElementMusical(el);
    }


    /**
     *
     * @return les portées affichées
     */
    public ArrayList<Portee> getPortees()
    {
        ArrayList<Portee> portees = new ArrayList<Portee>();
        for(Partie partie : parties)
        {
            portees.addAll(partie.getPortees());
        }
        return portees;
    }


    /**
     *
     * @param moment
     * @return l'absisse des notes au moment moment.
     */
    public double getXNotes(Moment moment) {
        return affichage.getXNotes(moment);
    }


    /**
     *
     * @param moment
     * @return l'abcisse d'une barre au moment moment.
     */
    public double getXBarreAuDebut(Moment moment) {
        return affichage.getXBarreAuDebut(moment);
    }


    /**
     *
     * @return le y le plus petit à partir duquel on est sur la zone de ce système
     */
    public double getYToutEnHaut() {
        return ytoutenhaut;
    }

/**
 *
 * @return le y le plus grand à partir duquel on est sur la zone de ce système
 */
    public double getYToutEnBas() {
        Portee portee = getPorteeDerniere();
        int coordmin = Math.max(10, -affichage.getCoordonneeVerticaleMin(portee));
        return getY(getPorteeDerniere(), -coordmin);
    }
        

       

    /**
     * affiche accolades et barre verticale tout au début du système
     * @param g
     */
    private void afficherAccolades(Graphics g) {

        g.setPenWidth(0.5f);
        afficherBarreComplete(g, lignesDebutX);


        for(Partie partie : getParties())
        {
            if(partie.getPorteesNombre() > 1)
            {
                double y1 = getY(partie.getPorteePremiere(), 4);
                double y2 = getY(partie.getPorteeDerniere(), -4);

                g.drawImage(imgAccolade, 
                            (lignesDebutX - 2.2*getInterLigneStandard()),
                            y1,
                            getAccoladeWidth(),
                            (y2 - y1));
            }
        }
    }


    /**
     *
     * @return la partition
     */
    public PartitionDonneesGetter getPartitionDonnees() {
        return partitionDonnees;
    }


    /**
     * affiche des traits pour montrer le curseur curseur. Affiche aussi
     * les notes en couleurs de la bonne voix etc.
     * @param g
     * @param curseur
     */
    public void afficherContexteFond(Graphics g, Curseur curseur) {
        if(curseur.isSurElementMusical() & !curseur.isSurNote())
            return;
        
        g.setColor(Color.lightGray);
        
        

        
   
        
        

        Moment moment = curseur.getMoment();
        Moment momentDebutMesure = partitionDonnees.getMesureMomentDebut(moment);
        Duree dureeDepuisDebutMesure = new Duree(momentDebutMesure, moment);
        double x = getXNotes(moment);
        
        
        final int xdebutmesure = (int) getXBarreAuDebut(momentDebutMesure);
        final int xfinmesure = (int) getXBarreAuDebut(partitionDonnees.getMomentMesureFin(moment));
        
        g.setColor(new Color(0.9f, 0.9f, 1.0f));
        //mesure
        g.fillRect((int) xdebutmesure+2,
                   (int) getYToutEnHaut(), 
                   (int) (xfinmesure - xdebutmesure-4), 16);
        

        
        
        
        //graduations tous les temps
        g.setColor(new Color(0.6f, 0.6f, 0.6f));
        for(Moment formoment = momentDebutMesure;
                formoment.isStrictementAvant(partitionDonnees.getMomentMesureFin(moment));
                formoment = Duree.getDureeNoire().getFinMoment(formoment))
        {
            final int xformoment = (int) getXNotes(formoment);
            g.drawLine((int) xformoment,
                   (int) getYToutEnHaut(),
                   (int) xformoment,
                   (int) getYToutEnHaut()+7);
        }
        
        //moment      
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        final int xmoment = (int) getXNotes(moment);
        g.drawLine((int) xmoment,
                   (int) getYToutEnHaut(),
                   (int) xmoment,
                   (int) getYToutEnHaut()+14);
        
        
        g.setColor(new Color(0.6f, 0.6f, 0.6f));
        g.setPenWidth(0.3f);
        
       

        
//        g.drawLine((int) xdebut,
//                   (int) y,
//                   (int) x,
//                   (int) y);



   

        
       
       
        
        //    double dureeDouble =
   //            dureeDepuisDebutMesure.getRational().getRealNumber();
      //  if(dureeDouble > 0)
           // g.drawString(String.valueOf(dureeDouble), (int) x, (int) y+8);
          //   g.drawString(String.valueOf(dureeDouble), (int) (x + xdebut) / 2-16, (int) y+8);

    }
    
    
    
    public void afficherNomNote(Graphics g, Curseur curseur)
    {
        double x = getXNotes(curseur.getMoment()) - 32;
        double y1 = getY(curseur);//getPorteeYToutHaut(curseur.getPortee());
        double y = y1+8;
        //nom de note
        g.setFont(new Font(g.getFont().getName(), 0, getFontSize(12)));
        g.setColor(new Color(255, 255,128));
        g.fillRect(x-8, y+8, 32, 24);
        g.setColor(Color.BLACK);
        
        g.drawString(curseur.getHauteur().toString(), (int) x, (int) y+24);
    }


    
    public AffichageAccord getAffichageAccordProche(Curseur curseur) {
        return affichage.getAffichageAccord(curseur);
    }

        
    /**
     *
     * @param p
     * @return le y qui correspond à la cinquième ligne de la portée
     */
    public double getPorteeYHaut(Portee p)
    {
        return getY(p, 4);
    }


    /**
     *
     * @param p
     * @return la hauteur de la portée p (ie. le nombre de pixels entre
     * la première et la cinquième ligne)
     */
    public double getPorteeHeight(Portee p)
    {
        return getInterLigne(p) * 4;
    
    }



    public Point getPoint(Curseur curseur)
    {
        return new Point((float) getXNotes(curseur.getMoment()),
                          (float)        getY(curseur.getPortee(),
                                       curseur.getCoordonneeVerticaleSurPortee()));
    }



    /**
     *
     * @param point
     * @return obtient le moment au point point mais c'est un moment "vague" dans le
     * sens où il n'est pas collé aux notes déjà présentes. Il peut valoir 2.45233 par exemple.
     */
    public Moment getMomentVague(Point point) {
        return affichage.getMomentVague(point);
    }



/**
 *
 * @param point
 * @return la partie sous le curseur si le curseur se trouve dans la zone à gauche
 * des notes (avant que les lignes commencent)
 * Sinon, retourne null.
 */
    public Partie getPartieSiDevantSysteme(Point point)
    {
        if(point.x < lignesDebutX)
        {
            return getPortee(point).getPartie();

        }
        else
            return null;
    }


    /**
     *
     * @param el
     * @return vrai ssi l'élement musical en question est un élément musical "supplémentaire".
     * C'est à dire un élément musical qui n'affecte pas la mise en forme.
     */
    private boolean isElementMusicalSupplementaire(ElementMusical el)
    {
        return !((el instanceof Note) ||
                 (el instanceof ElementMusicalClef) ||
                 (el instanceof ElementMusicalArpege) ||
                 (el instanceof Accord) ||
                 (el instanceof Silence) ||
                 (el instanceof ElementMusicalChangementMesureSignature) ||
                 (el instanceof ElementMusicalChangementTonalite) ||
                 (el instanceof BarreDeMesure) ||
                 (el instanceof ElementMusicalTempo));
    }

    /**
     * Ajoute les éléments musicaux comme les courbes à rajouter par dessus la partition mais qui ne participent
     * aucunement à la mise en page de la partition
     */
    private void ajouterElementsMusicauxSupplementaires() {
        for(Moment momentactuel = getDebutMoment();
            (momentactuel != null) && (momentactuel.isAvant(getFinMoment()));
            momentactuel = partitionDonnees.getMomentSuivant(momentactuel))
        {
              MomentElementsMusicauxSurLeFeu notesSurLeFeu = partitionDonnees.getMomentNotesSurLeFeu(momentactuel);
              notesSurLeFeu.filter(getParties());
              
              Set<ElementMusical> elementsMusicaux = notesSurLeFeu.getElementsMusicauxRassemblesEnAccordsAJouer();

              for(Iterator<ElementMusical>it = elementsMusicaux.iterator() ; it.hasNext() ; )
              {
                        ElementMusical el = it.next();
                        if(!isElementMusicalSupplementaire(el))
                        {
                            it.remove();
                        }
              }


              for(ElementMusical el : elementsMusicaux)
              {
                  affichage.ajouterAffichage(createAffichageElementMusicalDefaut(el));
              }
                  

        }
    }


    /**
     *
     * @return l'ensemble des parties affichées
     */
    private Collection<Partie> getParties() {
        //return partitionDonnees.getParties();
        return parties;
    }







    /**
     *
     * @return l'interligne standard d'une portée qui a taille normale. Utile
     * pour savoir ce positionner un peu dans la partition.. connaître ses dimensions etc.
     */
    public double getInterLigneStandard() {
        return interligne_pixel;
    }



    /**
     * place verticalement les différentes portées
     */
    private void porteesDistribution() {
        System.out.println("(systeme " + getNumeroSysteme() + ").porteesDistribution");
        double yToutEnHautPorteeDAvant = ytoutenhaut + getMargeEntreSysteme();

         for(Portee portee : getPortees())
         {
             int coordmax = Math.max(8, affichage.getCoordonneeVerticaleMax(portee));
             double yporteeTroisiemeLigneY = yToutEnHautPorteeDAvant + coordmax * getInterLigne(portee)/2;

             porteeTroisiemeLigneY.put(portee,yporteeTroisiemeLigneY);
             int coordmin = Math.max(8, -affichage.getCoordonneeVerticaleMin(portee));
             yToutEnHautPorteeDAvant = yporteeTroisiemeLigneY + (coordmin + 4) * getInterLigne(portee)/2;
         }

    }


    /**
     *
     * @return le nombre de parties affichées dans le système
     */
    private int getPartieNombre() {
        return getParties().size();
    }


    /**
     *
     * @return la portée affichée la plus en bas
     */
    public Portee getPorteeDerniere() {
        ArrayList<Portee> portees = getPortees();
        return portees.get(portees.size() - 1);
    }

/**
 *
 * @return la portée affichée la plus en haut
 */
    private Portee getPorteePremiere() {
        ArrayList<Portee> portees = getPortees();
        return portees.get(0);
    }






    /**
     * @param y
     * @return 0 si c'est sûr que le point d'ordonnée est dans le système
     * ensuite, propose une note/évaluation pour dire si le point semble
     * être dans ce système ou non
     */
    public int getEvaluationAppartenance(double y)
    {
        double y1 = getPorteeTroisiemeLigneY(getPorteePremiere());
        double y2 = getPorteeTroisiemeLigneY(getPorteeDerniere());

        if(y < y1)
            return (int) Math.round(y1 - y);

        if(y2 < y)
            return (int) Math.round(y - y2);

        return 0;
    }

    private double getMargeEntreSysteme() {
        return getInterLigneStandard()*5;
    }



    public Portee getPortee(Point point, Portee porteePreferee) {
        final Portee porteeLaPlusProche = getPortee(point);

        if(porteePreferee == null)
            return porteeLaPlusProche;
        else if(porteePreferee == porteeLaPlusProche)
        {
            return porteeLaPlusProche;
        }
        else
        {
            if(point.y <= porteeTroisiemeLigneY.get(getPorteePremiere()))
                return getPorteePremiere();
            else if (point.y > porteeTroisiemeLigneY.get(getPorteeDerniere()))
                return getPorteeDerniere();
            else if(Math.abs(getPorteeCoordonneeVerticale(porteeLaPlusProche, point)) < 7)
                return porteeLaPlusProche;
            else
                return porteePreferee;

        }
    }


    /**
     *
     * @return le moment qu'il y a tout au début du système
     */
    public Moment getMomentDebutSysteme() {
        return systemeCourantMomentDebut;
    }

    /**
     *
     * @return true ssi les noms des parties (les noms des instruments) sont affichés
     * au début du système devant chaque portée ou groupe de portées
     */
    private boolean isPartiesNomsAffichees() {
        return (getMomentDebutSysteme().equals(partitionDonnees.getMomentDebut()));
    }

    private int getAccoladeWidth() {
        return (int) (2*getInterLigneStandard());
    }

    public Portee getPorteePrecedente(Portee portee) {
        int i = getPortees().indexOf(portee);

        if(i == -1)
            return null;
        else if(i == 0)
            return getPortees().get(0);
        else
        return getPortees().get(i-1);
    }



    public Portee getPorteeSuivante(Portee portee) {
        int i = getPortees().indexOf(portee);

        if(i == -1)
            return null;
        else if(i == getPortees().size() - 1)
            return getPortees().get(getPortees().size() - 1);
        else
        return getPortees().get(i+1);
    }




    private void afficherErreurs(Graphics g) {
        if(AffichageOptions.correctionMesure)
            afficherErreursMesures(g);

        if(AffichageOptions.correctionNotes)
            afficherErreursNotesTessitures(g);
    }


    private AffichageNote getAffichageNote(Note note)
    {
        return (AffichageNote) getAffichageElementMusical(note);
    }

    
    private void afficherErreursNotesTessitures(Graphics g) {
        for(Portee portee : getPortees())
        {
            for(Moment moment = getDebutMoment(); moment != null; moment = getPartitionDonnees().getMomentSuivant(moment))
            {
                if(moment.isStrictementApres(getFinMoment()))
                    return;
                
                for(Note note : getPartitionDonnees().getNotesPorteeQuiCommencent(moment, portee))
                {
                    if(note.getHauteur().isInaudibleTropGrave() || note.getHauteur().isInaudibleTropAigu())
                    {
                        g.setColor(new Color(255, 128, 0));
                        g.setFont(new Font("", 0, 16));
                        AffichageNote aNote = getAffichageNote(note);
                        int x = (int) aNote.getX();
                        int y = (int)aNote.getNoteCentreY();
                        int r = (int) (aNote.getNoteRayon()*2);
                        g.drawImage(imgNoteErreur, x - r, y - r, r*2, r*2);
                        g.drawString("inaudible", x+r, y+r+g.getFontHeight());
                        

                    }
                    else if(!portee.getPartie().getInstrument().isJouable(note.getHauteur()))
                    {
                        g.setColor(new Color(255, 128, 0));
                        g.setFont(new Font("", 0, 16));
                        AffichageNote aNote = getAffichageNote(note);
                        int x = (int) aNote.getX();
                        int y = (int)aNote.getNoteCentreY();
                        int r = (int) (aNote.getNoteRayon()*2);
                        g.drawImage(imgNoteErreur, x - r, y - r, r*2, r*2);
                        g.drawString("injouable", x+r, y+r+g.getFontHeight());
                    }
                    
                }
                
            }
        }
    }
    
    private void afficherErreursMesures(Graphics g) {
        Moment moment = getPartitionDonnees().getMesureMomentDebut(getDebutMoment());

        while(moment.isStrictementAvant(getFinMoment()))
        {
            final Moment momentFinMesure = getPartitionDonnees().getMomentMesureFin(moment);

            if(getPartitionDonnees().getMesureSignature(moment) != null)
            {
                final Duree dureeMesureReelle = new Duree(moment, momentFinMesure);
                final Duree dureeMesureSignature = getPartitionDonnees().getMesureSignature(moment).getMesuresDuree();
                final Moment momentFinMesureSignature = dureeMesureSignature.getFinMoment(moment);
                
                if(dureeMesureReelle.isStrictementSuperieur(dureeMesureSignature))
                {
                    g.setColor(new Color(255, 128, 0));
                    g.setFont(new Font("", 0, 16));
                    g.drawString("mesure trop longue", (int) getXBarreAuDebut(momentFinMesureSignature),
                                 (int) getYToutEnHaut() + g.getFontHeight());
                    afficherBarreCompleteMorcele(g, getXBarreAuDebut(momentFinMesureSignature));
                }
                else if(dureeMesureReelle.isStrictementInferieur(dureeMesureSignature) 
                        & !dureeMesureReelle.isZero()
                        & !getPartitionDonnees().getElementMusicalMesureSignature(moment).getDebutMoment().equals(moment)
                        )
                {
                    g.setColor(new Color(255, 128, 0));
                    g.setFont(new Font("", 0, 16));
                    g.drawString("mesure trop courte", (int) getXBarreAuDebut(momentFinMesure),
                                 (int) getYToutEnHaut() + 2*g.getFontHeight());
                }
            }
            
            if(moment.isEgal(momentFinMesure))
                return;
            
            moment = momentFinMesure;



            
        }
        
    }

    private void afficherReperes(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setPenWidth(0.5f);
        for(Moment moment = getDebutMoment(); moment.isAvant(getFinMoment()); moment = Duree.getDureeNoire().getFinMoment(moment))
        {
            afficherBarreCompleteDepasse(g, getXNotes(moment));
        }
    }

    AffichageCrocheReliee getAffichageCrocheRelie(Point point) {
        return affichage.getAffichageCrocheReliee(point);
    }

    private double getXDebut() {
        return lignesDebutX;
    }

    private double getPorteeYToutHaut(Portee portee) {
        return getY(portee, 12);
    }
} //fin systeme
    