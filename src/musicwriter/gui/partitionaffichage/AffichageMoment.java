/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import musicwriter.donnees.*;
import musicwriter.gui.*;

/**
 *
 * @author proprietaire
 */
public class AffichageMoment extends AffichageEnsemble {
    final private Systeme systeme;
    final private Moment moment;
    private double accordsdebutx = 0;
    private double barrex = 0;

    final private AffichageEnsemble affichageSilenceSurTouteLaMesureTouteLaPortee = new AffichageEnsemble();

    private boolean contientAccordsOuSilences = false;

    public AffichageMoment(Systeme systeme, double x, Moment moment, Set<ElementMusical> elementsMusicaux) {
            
        super(x);
        accordsdebutx = x;
        double clefsx = x;
        barrex = x; // pour l'affichage si jamais il n'y a pas de barre ;)
        this.systeme = systeme;
        this.moment = moment;
        boolean autreChoseQueNotes = false;

        

        
        for(ElementMusical element : elementsMusicaux)
        {
                if(element instanceof ElementMusicalClef)
                {
                    AffichageElementMusicalClef a = new AffichageElementMusicalClef(systeme, clefsx, (ElementMusicalClef) element);
                    add(a);
                    if(a.getXFin() > x)
                        x = a.getXFin();
                    autreChoseQueNotes = true;
                }
        }




        for(ElementMusical element : elementsMusicaux)
        {
                if(element instanceof ElementMusicalChangementTonalite)
                {
                    AffichageChangementTonalite a = new AffichageChangementTonalite(systeme, x, (ElementMusicalChangementTonalite) element);
                    add(a);
                    x = a.getXFin();
                    autreChoseQueNotes = true;
                }
        }


        for(ElementMusical element : elementsMusicaux)
        {
                if(element instanceof BarreDeMesure)
                {
                    AffichageBarreDeMesure a = new AffichageBarreDeMesure(systeme, x, (BarreDeMesure) element);
                    add(a);
                    barrex = x;
                    x = x + a.getWidth() + 0.4 * systeme.getInterLigneStandard();
                    autreChoseQueNotes = true;

                }
        }


        for(ElementMusical element : elementsMusicaux)
        {
                if(element instanceof ElementMusicalTempo)
                {
                    add(new AffichageElementMusicalTempo(systeme, x, (ElementMusicalTempo) element));
                    autreChoseQueNotes = true;

                }
        }

        for(ElementMusical element : elementsMusicaux)
        {
                if(element instanceof ElementMusicalChangementMesureSignature)
                {
                    AffichageChangementMesureSignature a = new AffichageChangementMesureSignature(systeme, x, (ElementMusicalChangementMesureSignature) element);
                    add(a);
                    x = x + a.getWidth();
                    autreChoseQueNotes = true;
                }
        }
        x = x + 0.8 * systeme.getInterLigneStandard();

        if(autreChoseQueNotes)
            x = x + 0.8 * systeme.getInterLigneStandard();


        boolean yaArpege = false;
        for(ElementMusical element : elementsMusicaux)
        {
                if(element instanceof ElementMusicalArpege)
                {
                    add(new AffichageElementMusicalArpege(systeme, x, (ElementMusicalArpege) element));
                    yaArpege = true;
                    
                }
        }

        if(yaArpege)
            x = x + 1.5 * systeme.getInterLigneStandard();


        accordsdebutx = x + getAlterationDecalage(elementsMusicaux);

        





            if(elementsMusicaux.size() > 40)
            {
                System.out.println("Open Musicwriter may crash... because too musical elements on the same time!");
            }
            
            
            for(ElementMusical element : elementsMusicaux)
            {
                if(element instanceof Accord)
                {
                    double xc = accordsdebutx;
                    AffichageAccord aa = new AffichageAccord(systeme, xc, (Accord) element);
                    
                    Area i = getArea();
                    i.intersect(RegionFactory.createRegion((aa.getRectangle())));
                    while(!i.isEmpty())
                    {
                        aa = new AffichageAccord(systeme, xc, (Accord) element);
                        xc += systeme.getInterLigne(aa.getPortee())/3;
                        i = getArea();
                        i.intersect(RegionFactory.createRegion(aa.getRectangle()));
                    }
                    add(new AffichageAccord(systeme, xc, (Accord) element));
                    contientAccordsOuSilences = true;
                }
            }

            
            for(ElementMusical element : elementsMusicaux)
            {
                if(element instanceof Silence)
                {
                    add(new AffichageSilence(systeme, accordsdebutx, (Silence) element));
                    contientAccordsOuSilences = true;
                }

            }

            
            
            for(ElementMusical element : elementsMusicaux)
            {
                if(element instanceof Accord)
                {                   
                    add(new AffichageAccordFin(systeme, (Accord) element));
                }
            }

            
            
    }




    public void hampesCalculer()
    {
        hampesHautBasDecider();
        //hampesHautBasDecider();
        for(AffichageAccord a : getAffichageAccords())
        {
            a.hampesCalculer();
        }
    }
    


    public void traiterAffichageSilenceSurTouteLaMesureTouteLaPortee()
    {
        if(systeme.getPartitionDonnees().isDebutMesure(getMoment())
           & systeme.getFinMoment().isStrictementApres(getMoment()))
        {
            for(Portee p : systeme.getPortees())
            {
                if(systeme.getPartitionDonnees().porteeIsMesureVide(p, getMoment() ))
                    affichageSilenceSurTouteLaMesureTouteLaPortee.add(new AffichageSilenceSurTouteLaMesureTouteLaPortee(systeme, getMoment(), p ));
            }
        }

    }




    /**
     *
     * @param point
     * @param dureeDemandee
     * @return retourne l'afficheur d'accord dont l'accord est de durée dureeDemandee
     * et qui tombe soit sous le point point soit assez près.
     */
    AffichageAccord getAffichageAccordProche(Point point, Duree dureeDemandee)
    {
        for(Affichage a : this)
        {
            if(a instanceof AffichageAccord)
            {
                AffichageAccord affichageAccord = (AffichageAccord) a;
                
                Rectangle r = affichageAccord.getRectangle();
                r.grow(0, (float) systeme.getInterLigne(affichageAccord.getPortee()) * 4);
                
                if(r.contains(point) && affichageAccord.getDuree().equals(dureeDemandee))
                    return affichageAccord;
            }
        }
        return null;
    }


    AffichageAccord getAffichageAccord(Curseur curseur)
    {
        for(Affichage a : this)
        {
            if(a instanceof AffichageAccord)
            {
                AffichageAccord affichageAccord = (AffichageAccord) a;

                if(affichageAccord.getVoix().equals(curseur.getVoix()))
                    return affichageAccord;
            }
        }
        return null;
            
    }
    

    
    
    @Override
    public void setX(double x)
    {
        double ancienx = this.getX();
        super.setX(x);
        accordsdebutx = accordsdebutx + x - ancienx;
        barrex = barrex + x - ancienx;
    }

    
    Moment getMoment()
    {
        return moment;
    }
    
    
    
    public Set<AffichageAccord> getAffichageAccords()
    {
        Set<AffichageAccord> S = new HashSet<AffichageAccord>();
        for(Affichage a : this)
        {
            if(a instanceof AffichageAccord)
                S.add((AffichageAccord) a);
  
        }
        
        return S;
        
    }
    
    AffichageElementMusical getAffichageElementMusical(ElementMusical element)
    {
        for(Affichage a : this)
        {
            if(a instanceof AffichageElementMusical)
            {
                AffichageElementMusical ael = (AffichageElementMusical) a;
                if(ael.getElementMusical() == element)
                {
                    return ael;
                }
            }
            else if((a instanceof AffichageAccord) && (element instanceof Note))
            {
                if(((AffichageAccord) a).getAccord().contains((Note) element))
                    return ((AffichageAccord) a).getAffichageNote((Note) element);
            }
        }
        return null;
    }



    private boolean isContientAccords()
    {
        for(Affichage a : this)
        {
            if(a instanceof AffichageAccord)
                return true;
        }
        return false;
    }

    
    @Override
    public Rectangle getRectangle() {
        Rectangle rectangle = null;

        if(isContientAccords())
        {
            for(Affichage a : this)
            {
                if(a instanceof AffichageBarreDeMesure)
                {
                    Rectangle r = RegionFactory.createRectangle(a.getRectangle().getMinX(), a.getRectangle().getMinY(), 0, a.getRectangle().getHeight());
                    if(rectangle == null)
                    {
                        rectangle = r;
                    }
                    else
                        rectangle.add(r);
                }
                else
                if(!(a instanceof AffichageAccordFin)
                   & !(a instanceof AffichageSilence)
                   & !(a instanceof AffichageElementMusicalTempo))
                {
                    if(rectangle == null)
                    {
                        rectangle = a.getRectangle();
                    }
                    else
                        rectangle.add(a.getRectangle());
                }
            }
        }
        else
        {
            for(Affichage a : this)
            {
                if(!(a instanceof AffichageAccordFin))
                {
                    if(rectangle == null)
                    {
                        rectangle = a.getRectangle();
                    }
                    else
                        rectangle.add(a.getRectangle());
                }
            }
        }
        return rectangle;
    }


    public double getWidth()
    {
        return getRectangle().getWidth();
    }
    
    
    
    @Override
    public double getXFin() {
        double xfin = getXNotes() + systeme.getInterLigneStandard();//+ 2*systeme.getInterLigneStandard();

        if(isContientAccordsOuSilences())
        {
            for(Affichage a : this)
            {
                if(!(a instanceof AffichageAccordFin)
                 //  & !(a instanceof AffichageSilence)
                   & !(a instanceof AffichageElementMusicalTempo))
                {
                    if(a.getXFin() > xfin)
                        xfin = a.getXFin();
                }
            }
        }
        else
        {
            for(Affichage a : this)
            {
                if(!(a instanceof AffichageAccordFin))
                {
                    if(a.getXFin() > xfin)
                        xfin = a.getXFin();
                }
            }
        }

        return xfin;
    }
    


    public double getXDebut()
    {
          return getXNotes() - systeme.getInterLigneStandard();
    }

    
    public boolean isBienPourCouperSysteme()
    {
        for(Affichage a : this)
        {
            if(a instanceof AffichageBarreDeMesure)
                return true;
        }
        return false;
    }



    public boolean isContientAccordsOuSilences()
    {
        return contientAccordsOuSilences;
    }
    
/**
 * supprime tous les accords et silences... utile pour supprimer un temps à la fin
 * d'un système et mettre que le changement de tonalité, de clef, et la barre de mesure
 */
    public void removeAllAccordsEtSilences() {
        for(Iterator<Affichage>it = this.iterator() ; it.hasNext() ; )
        {
            Affichage a = it.next();

            if(a instanceof AffichageAccord)
            {
                it.remove();
            }
            
            if(a instanceof AffichageSilence)
            {
                it.remove();
            }
            
            if(a instanceof AffichageAccordFin)
            {
                it.remove();
            }

            if(a instanceof AffichageElementMusicalTempo)
            {
                it.remove();
            }
            
        }
    }

    double getXNotes() {
        return accordsdebutx;
    }


    double getXBarreAuDebut()
    {
        return barrex;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        affichageSilenceSurTouteLaMesureTouteLaPortee.draw(g);
    }



    private Area getAreaPriveDe(AffichageAccord aaaNePasCompter) {
        Area area = RegionFactory.createEmptyRegion();

        for(Affichage a : this)
            if(a != aaaNePasCompter)
            {
                if(a instanceof AffichageAccord)
                {
                    area.add(RegionFactory.createRegion(((AffichageAccord) a).getHampeRectangle()));
                }
                //else
                 //  area.add(a.getArea());
            }
        return area;
    }

    
    private void hampesHautBasDecider()
    {
        for(AffichageAccord a : getAffichageAccords())
        {
                hampeHautBasDecider(a);
        }
    }


    private void hampeHautBasDecider(AffichageAccord a) {

           Note.HampeDirection sauvegarde = a.getHampeDirection();

           a.setHampeVersLeHautSiAutomatique();
           Area i = getAreaPriveDe(a);
           i.intersect(RegionFactory.createRegion(a.getHampeRectangle()));
           double evalSiHaut = i.getBounds().getHeight()*i.getBounds().getWidth();

           a.setHampeVersLeBasSiAutomatique();
           i = getAreaPriveDe(a);
           i.intersect(RegionFactory.createRegion(a.getHampeRectangle()));
           double evalSiBas = i.getBounds().getHeight()*i.getBounds().getWidth();

           if(evalSiBas < evalSiHaut)
           {
               a.setHampeVersLeBasSiAutomatique();
           }
           else if(evalSiHaut < evalSiBas)
           {
               a.setHampeVersLeHautSiAutomatique();
           }
           else
           {
               a.setHampeDirectionSiAutomatique(sauvegarde);
           }

    }


    /**
     *
     * @param extremitePoint
     * @return l'affichage d'un accord dont l'extrémité de la hampe coincide à
     * peu près avec le point extremitePoint passé en paramètre.
     * Renvoie null si aucun affichage d'accord est trouvé.
     */
    AffichageAccord getAffichageAccordAvecHampeFin(Point extremitePoint) {
        for(AffichageAccord a : getAffichageAccords())
        {
            if(a.isProcheHampeFin(extremitePoint))
            {
                return a;
            }
        }
        return null;
    }




    public Set<AffichageNote> getAffichageNotes()
    {
        Set<AffichageNote> S = new HashSet<AffichageNote>();
        for(AffichageAccord a : getAffichageAccords())
        {
            S.addAll(a.getAffichageNotes());
        }
        return S;
    }

    private double getAlterationDecalage(Set<ElementMusical> elementsMusicaux) {
        double decalXAlteration = 0;
        for(ElementMusical element : elementsMusicaux)
            {
                if(element instanceof Accord)
                {
                    Accord accord = (Accord) element;
                    for(Note note : accord)
                    {
                        if(!note.getHauteur().getAlteration().equals(systeme.getPartitionDonnees()
                            .getAlterationParDefaut
                            (note.getDebutMoment(),
                            systeme.getDebutMoment(),
                            note.getPortee(),
                            note.getHauteur())))
                            decalXAlteration = systeme.getInterLigne(note.getPortee());
                    }
                }
        }
        return decalXAlteration;
    }







    
}
