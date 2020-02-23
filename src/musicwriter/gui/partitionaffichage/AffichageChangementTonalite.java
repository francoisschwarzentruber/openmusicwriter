/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Tonalite;
import musicwriter.donnees.Hauteur;
import musicwriter.donnees.ElementMusicalChangementTonalite;
import musicwriter.gui.*;


/**
 *
 * @author Ancmin
 */
public class AffichageChangementTonalite extends AffichageElementMusical implements Affichage {
    private double x;

    AffichageChangementTonalite(Systeme systeme, double x, ElementMusicalChangementTonalite element)
    {
          super(systeme, element);
          this.x = x;
    }

    AffichageChangementTonalite(Systeme systeme, ElementMusicalChangementTonalite elementMusicalChangementTonalite) {
        this(systeme, systeme.getXBarreAuDebut(elementMusicalChangementTonalite.getDebutMoment()),
                elementMusicalChangementTonalite);

        if(getSysteme().getPartitionDonnees().isBarreDeMesure(getDebutMoment())   )
        {
            deplacerX(-getWidth());
        }
        
        
    }

    private int getNombreDieses(Portee portee)
    {
        return getTonalite(portee).getDiesesNombre();
    }

    private int getNombreDiesesJusteAvant(Portee portee)
    {
        return getTonaliteJusteAvant(portee).getDiesesNombre();
    }


    private boolean isAfficheBecarres(Portee portee)
    {
        return getNombreDieses(portee) * getNombreDiesesJusteAvant(portee) <= 0;
    }



    private Tonalite getTonalite(Portee portee)
    {
        return portee.getPartie().getTonaliteTransposee(getElementMusicalChangementTonalite().getTonalite());
    }


    private Tonalite getTonaliteJusteAvant(Portee portee)
    {
        return getSysteme().getPartitionDonnees().getTonaliteJusteAvant(getDebutMoment(),
                                                                   portee.getPartie());
    }


    private ElementMusicalChangementTonalite getElementMusicalChangementTonalite()
    {
        return ((ElementMusicalChangementTonalite) getElementMusical());
    }




    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }


    /**
     *
     * @param p
     * @return l'espace horizontal qu'il y a entre deux altérations dessinées
     */
    private double getEspaceEntreAlteration(Portee p)
    {
        return 0.6*getSysteme().getInterLigne(p);
    }

    public void draw(Graphics g) {

        
        for(Portee p : getSysteme().getPortees())
        {
            double xcourant = getX();

            if(isAfficheBecarres(p))
                for(Hauteur hauteur : getTonaliteJusteAvant(p).getHauteursAltereesAffichees(p.getClef(getDebutMoment())))
                {
                    getSysteme().dessinerAlterationADroite(g,
                            Hauteur.Alteration.NATUREL,
                            xcourant,
                            p,
                            p.getCoordonneeVerticaleSansOctaviation(
                                getDebutMoment(),
                                hauteur));
                    xcourant += getEspaceEntreAlteration(p);
                }


            for(Hauteur hauteur : getTonalite(p).getHauteursAltereesAffichees(p.getClef(getDebutMoment())))
            {
                getSysteme().dessinerAlterationADroite(g,
                        hauteur.getAlteration(),
                        xcourant,
                        p,
                        p.getCoordonneeVerticaleSansOctaviation(
                            getDebutMoment(),
                            hauteur));
                xcourant += getEspaceEntreAlteration(p);
            }
        }
    }


    public int getWidth()
    {
        int width = 0;
        for(Portee p : getSysteme().getPortees())
        {
            int nvwidth = (int) (getEspaceEntreAlteration(p) * Math.abs(getNombreDieses(p)) + getSysteme().getInterLigne(p));

            if(isAfficheBecarres(p))
                nvwidth += (int) (getEspaceEntreAlteration(p) * Math.abs(getNombreDiesesJusteAvant(p)));

            if(nvwidth > width)
                width = nvwidth;
        }
        return width;
    }

    @Override
    public Rectangle getRectangle() {
        return RegionFactory.createRectangle((int) getX(),
                             (int) getSysteme().getYHaut(),
                             getWidth(),
                             (int) (getSysteme().getYBas() - getSysteme().getYHaut()));
    }

    @Override
    public Selection getSelection(Area area) {
         if(area.contains(getRectangle()))
             return new Selection(getElementMusical());
         else
             return new Selection();
    }

    @Override
    public Area getArea() {
        Area a = RegionFactory.createEmptyRegion();
        for(Portee p : getSysteme().getPortees())
        {
            a.add(RegionFactory.createRegion(RegionFactory.createRectangle((int) getX(),
                    (int) ( getSysteme().getPorteeYHaut(p) - getSysteme().getInterLigne(p) ),
                    getWidth(),
                    (int) ( getSysteme().getPorteeHeight(p) + 2*getSysteme().getInterLigne(p)) )));
        }
        return a;
    }

    void deplacerX(double delta) {
        setX(getX() + delta);
    }

    public double getXFin() {
        return getX() + getWidth();
    }

}
