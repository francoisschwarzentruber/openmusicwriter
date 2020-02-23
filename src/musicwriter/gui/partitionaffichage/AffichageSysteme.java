/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.util.Map.Entry;
import java.util.*;
import musicwriter.Arithmetique;
import musicwriter.donnees.*;
import musicwriter.gui.*;

/**
 *
 * @author proprietaire
 */
public class AffichageSysteme implements Affichage {

    private final Systeme systeme;
    private final TreeMap<Moment, AffichageMoment> affichages = new TreeMap<Moment, AffichageMoment>();
    private final AffichageEnsemble croches_affichages = new AffichageEnsemble();
    private final PartitionDonneesGetter partitionDonnees;


    /**
     * les moments clés, c'est à dire enregistré dans la partition sont stockés
     * en temps que couple (XLeft, Moment)
     */
    private TreeMap<Double, Moment> moments;


    

    private final AffichageEnsemble affichageSupplementaires = new AffichageEnsemble();




    private void enregistrerPositionsMoments()
    {
        moments = new TreeMap<Double, Moment>();

        Moment lastMoment = affichages.firstKey();
        double lastX = affichages.firstEntry().getValue().getX();
        
        for(AffichageMoment a : affichages.values())
        {
            /**
             * si jamais l'écart avec le précédent AffichageMoment
             * est trop grand, on remplit de "moment" où on peut
             * écrire des notes
             */
            if(a.getX() > lastX + 32)
            {
                Moment moment = Duree.getDureeNoire().getFinMoment(lastMoment);
                final Duree D = (new Duree(lastMoment, a.getMoment()));
                final double dx = a.getX() - lastX;
                while(moment.isStrictementAvant(a.getMoment()))
                {
                    final double rapport = ((new Duree(lastMoment, moment)).getRational().getRealNumber() / D.getRational().getRealNumber());

                    moments.put(lastX + dx*rapport, moment);
                    moment = Duree.getDureeNoire().getFinMoment(moment);
                }
            }

            moments.put(a.getX(), a.getMoment());

            lastMoment = a.getMoment();
            lastX = a.getX();



        }
    }



    void add(AffichageMoment affichageMoment) {
        affichages.put(affichageMoment.getMoment(), affichageMoment);
    }
    


    public boolean isEmpty()
    {
        return affichages.isEmpty();
    }


    
    
    AffichageCrocheReliee getAffichageCrocheReliee(Point point)
    {
        for(Affichage a : croches_affichages)
        {
            if(a.getArea().contains(point))
                return (AffichageCrocheReliee) a;
        }
        
        return null;
    }
    
    AffichageSysteme(Systeme systeme, PartitionDonneesGetter partitionDonnees)
    {
        this.systeme = systeme;
        this.partitionDonnees = partitionDonnees;
    }



    
    


    
    
    AffichageElementMusical getAffichageElementMusical(ElementMusical element)
    {
        for(Affichage a : affichageSupplementaires)
        {
            if(a instanceof AffichageElementMusical)
            {
                if(((AffichageElementMusical) a).getElementMusical() == element)
                {
                    return ((AffichageElementMusical) a);
                }
            }
        }


        if(affichages.containsKey(element.getDebutMoment()))
        {
            return affichages.get( element.getDebutMoment() ).getAffichageElementMusical(element);
        }
        else
            return null;

        
    }




    public AffichageAccord getAffichageAccordAvecHampeFin(Point extremitePoint)
    {
         Moment moment = getMoment(extremitePoint);

         if(getAffichageMoment(moment) == null)
             return null;
         else
             return getAffichageMoment(moment).getAffichageAccordAvecHampeFin(extremitePoint);

    }

    
    void afficher(Graphics g, ElementMusical element)
    {
        AffichageElementMusical a = getAffichageElementMusical(element);

        if(a != null)
            a.draw(g);
    }
    
    
    
    

    public void draw(Graphics g) {
        for(Affichage a : affichages.values())
        {
            a.draw(g);
        }
        
        croches_affichages.draw(g);
        affichageSupplementaires.draw(g);
    }

    public Area getArea() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Rectangle getRectangle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Selection getSelection(Area area) {
        Selection selection = new Selection();
        for(Affichage a : affichages.values())
        {
            selection.elementsMusicauxAjouter(a.getSelection(area));
        }

        for(Affichage a : affichageSupplementaires)
        {
            selection.elementsMusicauxAjouter(a.getSelection(area));
        }
        
        return selection;
    }

    public ElementMusical getElementMusical(Point point) {
        for(Affichage a : affichages.values())
        {
            ElementMusical el = a.getElementMusical(point);
            if(el != null)
                return el;
        }


        for(Affichage a : affichageSupplementaires)
        {
            ElementMusical el = a.getElementMusical(point);
            if(el != null)
                return el;
        }
        return null;
    }

    /**
     * 
     * @return le dernier moment présent dans ce système.
     */
    public Moment getFinMoment() {
        return affichages.lastKey();
    }



    private boolean isPointSurMoment(Point point, Moment moment)
    {
        if(!affichages.containsKey(moment))
            return false;
        else
            return Arithmetique.is1in23(point.x,
                                 affichages.get(moment).getX(),
                                 affichages.get(moment).getXFin());
    }





    /**
     * 
     * @param point
     * @return le moment (dans lequel il y a bien quelque chose d'écrit dans la partition
     * ou alors juste une fin de note). Le point point se trouve sur ce moment là.
     */
     Moment getMoment(Point point) {
        Entry<Double, Moment> d1 = moments.floorEntry((double)point.getX());
        Entry<Double, Moment> d2 = moments.higherEntry((double)point.getX());

        if(d1 == null)
        {
            if(d2 == null)
                return affichages.firstKey();
            else
                return d2.getValue();
        }
        else
        {
            if(d2 == null)
            {
                return d1.getValue();
            }
            else
                return d1.getValue();
        }

     }
   


     /**
     *
     * @param point
     * @return le moment où se trouve le point point, sachant que le résultat
      * est soit un moment présent dans la partition, soit un moment tel que
      * ce moment + dureeDemandee est un moment présent dans la partition.
     */
    Moment getMoment(Point point, Duree dureeDemandee)
    {
        Moment moment = getMoment(point);

        if(!isPointSurMoment(point, moment))
        {
            Moment momentDebutVague = getMomentVague(point);
            //momentDebutVague ~ 1.6 par exemple
            Moment momentFinVague = dureeDemandee.getFinMoment(momentDebutVague);
            // si durée d'une croche momentFinVague ~ 2.1s

            Moment momentFin = getMomentClefPlusProche(momentFinVague);
            // s'il y a quelquechose au temps 2, momentFin = 2

            Moment momentDebut = dureeDemandee.getDebutMoment(momentFin);
            // momentDebut = 1.5

            return Moment.max(moment, momentDebut);

        }
        else
            return moment;
    }


    /**
     *
     * @param momentVague
     * @return le moment clef (enregistré) dans la partition qui est proche du momentVague
     * exemple : 4 croches... et momentVague = 0.45
     * retourne 0.5
     */
    Moment getMomentClefPlusProche(Moment momentVague)
    {
        Moment moment1 = affichages.floorKey(momentVague);
        Moment moment2 = affichages.ceilingKey(momentVague);

        if(moment1 == null)
            return moment2;
        else if(moment2 == null)
            return moment1;
        {
            if(new Duree(moment1, momentVague).isStrictementInferieur(
                    new Duree(momentVague, moment2)))
                return moment1;
            else
                return moment2;
        }
    }


    /**
     *
     * @param point
     * @return le moment où se trouve le point
     * si le point se trouve pile poil sur un moment clef enregistré alors renvoie
     * ce moment
     * sinon, si le point est entre deux moments, ça fait une interpolation linéaire
     */
    Moment getMomentVague(Point point) {
              Moment resultat = affichages.firstKey();

              Moment momentAvant = affichages.firstKey();
              for(Moment moment = affichages.firstKey(); moment != null; moment = affichages.higherKey(moment) )
              {
                  if(affichages.get(moment).getXNotes() > point.x)
                  {
                      if(moment.isEgal(momentAvant))
                          return momentAvant;
                      else
                      {
                          double lambda = (point.x - affichages.get(momentAvant).getXNotes()) / (affichages.get(moment).getXNotes() - affichages.get(momentAvant).getXNotes());
                      
                          return new Duree(momentAvant, moment).multiplier(lambda).getFinMoment(momentAvant);

                      }
                      
                  }

                  momentAvant = moment;

              }
              return resultat; 
    }


    Moment getMomentBarre(Point point) {
          Moment momentPlusProche = getMoment(point);

          if(getAffichageMoment(momentPlusProche) == null)
              return momentPlusProche;
          else if(!getAffichageMoment(momentPlusProche).isContientAccordsOuSilences())
          {
              return momentPlusProche;
          }
          else
          { 
              for(Moment moment = affichages.firstKey(); moment != null; moment = affichages.higherKey(moment) )
              {
                      if(affichages.get(moment).getXFin() > point.getX())
                      {
                          return moment;
                      }
              }
          }
          return affichages.lastKey();
    }
    
    
    
    AffichageAccord getAffichageAccord(Point point, Duree dureeDemandee)
    {
        Moment moment = getMoment(point);

        if(getAffichageMoment(moment) == null)
            return null;
        else
            return getAffichageMoment(moment).getAffichageAccordProche(point, dureeDemandee);
    }




    public Set<AffichageNote> getAffichageNotes()
    {
        Set<AffichageNote> S = new HashSet<AffichageNote>();
        for(AffichageMoment a : affichages.values())
        {
            S.addAll(a.getAffichageNotes());
        }
        return S;
    }



    public int getCoordonneeVerticaleMax(Portee p)
    {
        int m = 4;
        for(AffichageNote a : getAffichageNotes())
        {
            if(a.getPortee().equals(p))
            {
                if(a.getCoordonneeVerticale() > m)
                    m = a.getCoordonneeVerticale();
            }
        }
        return m;
    }





    public int getCoordonneeVerticaleMin(Portee p)
    {
        int m = -4;
        for(AffichageNote a : getAffichageNotes())
        {
            if(a.getPortee().equals(p))
            {
                if(a.getCoordonneeVerticale() < m)
                    m = a.getCoordonneeVerticale();
            }
        }
        return m;
    }
    
    
    AffichageAccord getAffichageAccord(Curseur curseur)
    {
        if(affichages.containsKey(curseur.getMoment()))
            return getAffichageMoment(curseur.getMoment()).getAffichageAccord(curseur);
        else
            return null;
    }

    double getXNotes(Moment moment) {
        if(affichages.lastKey().isAvant(moment))
        {
            //moment est après la fin de la partition
            //bref, il faut improviser !
            Moment dernierMoment = affichages.lastKey();
               return affichages.get(dernierMoment).getXNotes()
                                + (new Duree(dernierMoment, moment)).getRational().getRealNumber() * Systeme.nbpixelDureeNoireParDefaut;
        }
        else if(moment.isStrictementAvant(affichages.firstKey()))
        {
            return 0;
        }
        else
        {
            Moment moment1 = affichages.floorKey(moment);
            Moment moment2 = affichages.higherKey(moment);
            
            double r1 = moment1.getRational().getRealNumber();
            double r2 = moment2.getRational().getRealNumber();
            double m = moment.getRational().getRealNumber();
            double lambda = (m - r1) / (r2 - r1);
            return affichages.get(moment1).getXNotes() * (1 - lambda) + affichages.get(moment2).getXNotes() * lambda;
        }
        
    }



    public double getXDebut(Moment moment)
    {
        if(affichages.containsKey(moment))
        {
            return affichages.get(moment).getXDebut();
        }
        else
            return getXNotes(moment);
    }



    public double getXFin(Moment moment)
    {
        if(affichages.containsKey(moment))
        {
            return affichages.get(moment).getXFin();
        }
        else
            return getXNotes(moment);
    }






    public AffichageMoment getAffichageMoment(Moment moment) {
        return affichages.get(moment);
    }

    boolean isTermineParUnMomentARepeterSurSystemeSuivant() {
        if(getDernierAffichageMoment().getXFin() <= 0.9*systeme.getXFin())
            return false;
        else
        return getDernierAffichageMoment().isBienPourCouperSysteme();
    }

    private boolean isTientDansLeSysteme() {
        if(affichages.isEmpty())
            return true;
        else
            return getDernierAffichageMoment().getXFin() <= 0.9*systeme.getXFin();
    }
    
    

    @SuppressWarnings("element-type-mismatch")
    private Area getOccupiedArea(Moment m1, Moment m2, Area intersectedWithThis, ArrayList<AffichageAccord> except)
    {
        if(m1 == null)
            return RegionFactory.createEmptyRegion();
        
        if(m2 == null)
            return RegionFactory.createEmptyRegion();

        if(affichages == null)
            return RegionFactory.createEmptyRegion();

        Area a = RegionFactory.createEmptyRegion();

        try
        {
            for(Moment m = m1; m.isAvant(m2);  m = affichages.higherKey(m))
            {
                for(Affichage aff : affichages.get(m))
                {
                    if(!(aff instanceof AffichageElementMusicalTempo))
                        if(!except.contains(aff))
                         a.add(aff.getArea());
                }
            }
        }
        catch(Exception e)
        {

        }
        
        a.intersect(intersectedWithThis);

        return a;
    }



    
    private AffichageAccord pop(SortedSet<AffichageAccord> accords)
    { 
        if(accords.isEmpty())
            return null;
        else
        {
            AffichageAccord a = accords.first();
            accords.remove(a);
            return a;
        }
//        for(AffichageAccord a : accords)
//        {
//            accords.remove(a);
//            return a;
//        }
//        return null;
    }
    
    private AffichageAccord getSuivant(Set<AffichageAccord> accords, AffichageAccord accord)
    {
        for(AffichageAccord a : accords)
        {
            if(a.getVoix().equals(accord.getVoix()))
            {
                if(a.getMomentDebut().equals(accord.getMomentFin()))
                {
                    return a;
                }
            }
        }

        return null;
    }
    
    public void calculerCrochesReliees()
    {
        SortedSet<AffichageAccord> accords = getAffichageAccords();
        
        while(!accords.isEmpty())
        {
            calculerUnPaquetCrochesReliees(accords);
        }
    }
    
    
    private void calculerUnPaquetCrochesReliees(SortedSet<AffichageAccord> affichageAccords)
    {
        ArrayList<AffichageAccord> elementsRelieesEnsemble = new ArrayList<AffichageAccord>();

        AffichageAccord premieraccord = pop(affichageAccords);
        Moment momentDebutMesure = partitionDonnees.getMesureMomentDebut(premieraccord.getMomentDebut());
        MesureSignature signature = systeme.getPartitionDonnees().getMesureSignature(premieraccord.getMomentDebut());
        
//        int T = (int) Math.floor((premieraccord.getMomentDebut().getRealNumber() - momentDebutMesure.getRealNumber())/signature.getGroupe());
//        for(AffichageAccord a = premieraccord;
//            a != null &&
//            a.getNombreTraitsCroche() > 0 &&
//            (((a.getMomentDebut().getRealNumber() - momentDebutMesure.getRealNumber())/signature.getGroupe()) < T + 1)
//            && (partitionDonnees.getMesureMomentDebut(a.getMomentDebut()).equals(momentDebutMesure));
//            a = getSuivant(accords, a))
//        {
//            accords.remove(a);
//            elementsRelieesEnsemble.add(a);
//        }


        final double fin;

        if(signature == null)
            fin = 1000000.0f;
        else
            fin = signature.getFinGroupe( (premieraccord.getMomentDebut().getRealNumber() - momentDebutMesure.getRealNumber()) );
        
        for(AffichageAccord a = premieraccord;
            a != null &&
            //a.getNombreTraitsCroche() > 0 &&
            ((a.getMomentDebut().getRealNumber() - momentDebutMesure.getRealNumber()) < fin)
            && (partitionDonnees.getMesureMomentDebut(a.getMomentDebut()).equals(momentDebutMesure));
            a = getSuivant(affichageAccords, a))
        {
            /**
             * on arrête là si on tombe sur un accord qui n'a pas de traits de croche
             */
            if(a != premieraccord)
            {
                if(a.getNombreTraitsCroche() == 0)
                    break;
            }
            affichageAccords.remove(a);
            elementsRelieesEnsemble.add(a);
        }

        if(!elementsRelieesEnsemble.isEmpty())
        {
            final int n = elementsRelieesEnsemble.size() - 1;
            Area auDessus = getOccupiedArea(elementsRelieesEnsemble.get(0).getAccord().getDebutMoment(),
                    elementsRelieesEnsemble.get(n).getAccord().getDebutMoment(),
                    AffichageCrocheReliee.getAreaAuDessus(elementsRelieesEnsemble), elementsRelieesEnsemble);
            Area auDessous = getOccupiedArea(elementsRelieesEnsemble.get(0).getAccord().getDebutMoment(),
                    elementsRelieesEnsemble.get(n).getAccord().getDebutMoment(),
                    AffichageCrocheReliee.getAreaAuDessous(elementsRelieesEnsemble), elementsRelieesEnsemble);

            double auDessusSurface = auDessus.getBounds().getWidth() * auDessus.getBounds().getHeight();
            double auDessousSurface = auDessous.getBounds().getWidth() * auDessous.getBounds().getHeight();
            if(auDessusSurface < auDessousSurface)
            {
                for(AffichageAccord a : elementsRelieesEnsemble)
                {
                    a.setHampeVersLeHautSiAutomatique();
                }
            }
            else if (auDessusSurface > auDessousSurface)
            {
                for(AffichageAccord a : elementsRelieesEnsemble)
                {
                    a.setHampeVersLeBasSiAutomatique();
                }
            }
            croches_affichages.add(new AffichageCrocheReliee(systeme, elementsRelieesEnsemble));
            
        }
    }

    
    /**
     * 
     * @return tous les objets affichage qui correspondent à des accords
     */
    public SortedSet<AffichageAccord> getAffichageAccords() {
        SortedSet<AffichageAccord> affichageAccords = new TreeSet<AffichageAccord>(new ComparatorAffichageAccord());
        
        for(AffichageMoment am : this.affichages.values())
        {
            affichageAccords.addAll(am.getAffichageAccords());
        }
        
        return affichageAccords;
    }

    public double getX() {
        return 0;
    }

    public void setX(double x) {
        x = 0;
    }
    
    public AffichageMoment getDernierAffichageMoment()
    {
        return affichages.get(affichages.lastKey());
    }
    
    private AffichageMoment getAffichageMomentACouper()
    {
        if(affichages.isEmpty())
            return null;
        else
        {
             Moment moment = affichages.lastKey();
             
             while(moment != null)
             {
                 if(affichages.get(moment).isBienPourCouperSysteme())
                     return affichages.get(moment);
                 moment = affichages.lowerKey(moment);
             }
             
             return null;
        }
        
    }
    
    
   
    private void etirer()
    {
        double xdebut = affichages.firstEntry().getValue().getX();

        double facteur = (systeme.getXFin() - xdebut) / (getDernierAffichageMoment().getXFin() - xdebut);
        
        for(AffichageMoment a : affichages.values())
        {
            a.setX((a.getX() - xdebut) * facteur  + xdebut);
        }
        
        getDernierAffichageMoment().setX(systeme.getXFin() - getDernierAffichageMoment().getWidth());
        //getDernierAffichageMoment().setX(systeme.getXFin());
    }


    
    private void momentsAjouterFin()
    {
         Moment moment = getFinMoment();
         Duree mesuresDuree = new Duree(new Rational(1, 1));//systeme.getPartitionDonnees().getMesureSignature(moment).getMesuresDuree();
         moment = mesuresDuree.getFinMoment(moment);
         while(getXNotes(moment) < systeme.getXFin())
         {
             add(new AffichageMoment(systeme, getXNotes(moment), moment, new HashSet<ElementMusical>()));
             moment = mesuresDuree.getFinMoment(moment);
         }
    }




    
    
    public void miseEnPage()
    {

        if(isTientDansLeSysteme())
        {
            momentsAjouterFin();
        }
        else
        {

            if(!isPeuRempliEtQuUneMesure())
            {

                AffichageMoment dernierAffichageMoment = getAffichageMomentACouper();

                if(dernierAffichageMoment != null)
                {
                    removeAllStrictementApresMoment(dernierAffichageMoment.getMoment());
                    dernierAffichageMoment.removeAllAccordsEtSilences();
                    etirer();

                }
            }
        }

        enregistrerPositionsMoments();
        traiterAffichageSilencesSurTouteLaMesureTouteLaPortee();

    }

    private void removeAllStrictementApresMoment(Moment moment) {
        while(affichages.lastKey().isStrictementApres(moment))
        {
            affichages.remove(affichages.lastKey());
        }
    }

    public double getXBarreAuDebut(Moment moment) {
       if(affichages.lastKey().isStrictementAvant(moment))
        {
            //moment est après la fin de la partition
            //bref, il faut improviser !
           return getXNotes(moment);
        }
        else 
        {
            moment = affichages.floorKey(moment);

           if(moment == null)
               return 0;
           else
               return affichages.get(moment).getXBarreAuDebut();
        }
    }

    private boolean isPeuRempliEtQuUneMesure() {
        return affichages.size() < 16 & false;
    }

    void ajouterAffichage(Affichage affichageElementMusicalCourbe) {
        affichageSupplementaires.add(affichageElementMusicalCourbe);
    }

    public double getXFin() {
        return systeme.getXFin();
    }

    public void hampesCalculer() {
        
        for(AffichageMoment a : affichages.values())
        {
            a.hampesCalculer();
        }
    }


    /**
     * calcule là où il faut des silences car une portée ne joue rien sur toute une mesure
     */
    private void traiterAffichageSilencesSurTouteLaMesureTouteLaPortee() {
        for(AffichageMoment a : affichages.values())
             a.traiterAffichageSilenceSurTouteLaMesureTouteLaPortee();

    }

    private Iterable<AffichageMoment> getAffichageMoments() {
        return affichages.values();
    }

    
    





    
    
}
