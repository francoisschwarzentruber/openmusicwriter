/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.BarreDeMesure;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Selection;
import musicwriter.gui.*;

/**
 * Cette classe représente l'affichage d'une barre de mesure.
 * @author François Schwarzentruber
 */
class AffichageBarreDeMesure extends AffichageElementMusical implements Affichage {
    private double x;

    public AffichageBarreDeMesure(Systeme systeme, double x, BarreDeMesure barreDeMesure) {
        super(systeme, barreDeMesure);
        this.x = x;
    }


    private double getEspace()
    {
        return getSysteme().getInterLigneStandard()/2;
    }


    public void draw(Graphics g) {
        switch(getBarreDeMesureType())
        {
            case NORMALE:
                g.setPenWidth(1.2f);
                getSysteme().afficherBarre(g, x);
                break;

             case DOUBLE:
                g.setPenWidth(1.2f);
                getSysteme().afficherBarre(g, x);
                getSysteme().afficherBarre(g, x + getEspace());
                break;

            case REPRISEGAUCHE:
                doublesPoints(g, x - getEspace());
                g.setPenWidth(1.2f);
                getSysteme().afficherBarre(g, x);
                g.setPenWidth(3f);
                getSysteme().afficherBarre(g, x + getEspace());
                break;

            case REPRISEDROITE:
                
                g.setPenWidth(3f);
                getSysteme().afficherBarre(g, x);
                g.setPenWidth(1.2f);
                getSysteme().afficherBarre(g, x + getEspace());
                doublesPoints(g, x + 2*getEspace());
                break;

            case REPRISEDOUBLE:
                g.setPenWidth(1.2f);
                getSysteme().afficherBarre(g, x);
                getSysteme().afficherBarre(g, x + 2*getEspace());
                g.setPenWidth(3f);
                getSysteme().afficherBarre(g, x + getEspace());
                doublesPoints(g, x - getEspace());
                doublesPoints(g, x + 3*getEspace());
                break;

           case FIN:
                g.setPenWidth(1.2f);
                getSysteme().afficherBarre(g, x);
                g.setPenWidth(3f);
                getSysteme().afficherBarre(g, x + getEspace());
                break;

        }
        
        
    }



    public BarreDeMesure.BarreDeMesureType getBarreDeMesureType()
    {
        return ((BarreDeMesure) getElementMusical()).getBarreDeMesureType();
    }





//    public double getWidth()
//    {
//        switch(getBarreDeMesureType())
//        {
//            case NORMALE: return getEspace();
//            case DOUBLE: return 3*getEspace();
//            case REPRISEGAUCHE: case REPRISEDROITE: return 2*getEspace();
//            default: return 4*getEspace();
//        }
//    }

public double getWidth()
    {
        switch(getBarreDeMesureType())
        {
            case NORMALE: return 0;
            case DOUBLE: return 2*getEspace();
            case REPRISEGAUCHE: case REPRISEDROITE: return 1*getEspace();
            default: return 3*getEspace();
        }
    }
   


    public Rectangle getRectangleForClick()
    {
        return RegionFactory.createRectangle((float) x - 2,
                               (float) getSysteme().getYHaut(),
                               4,
                               (float) (getSysteme().getYBas() - getSysteme().getYHaut()));
    }
    
    
    public Rectangle getRectangle()
    {
        return RegionFactory.createRectangle((int) (x - getEspace()/2),
                             (int)  getSysteme().getYHaut(),
                             (int) getWidth(),
                             (int)  (getSysteme().getYBas() - getSysteme().getYHaut()));
    }
    
    
    @Override
    public Selection getSelection(Area area) {
         if(area.contains(getRectangle()))
             return new Selection(getElementMusical());
         else
             return new Selection();

    }

    public Area getArea() {
        return RegionFactory.createRegion(getRectangleForClick());
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    private void doublesPoints(Graphics g, double x) {
        for(Portee p : getSysteme().getPortees())
            {
                petitPoint(g, x, getSysteme().getY(p, 1));
                petitPoint(g, x, getSysteme().getY(p, -1));
            }
    }

    private void petitPoint(Graphics g, double x, double y) {
        double rayon = getSysteme().getInterLigneStandard() / 5;
        g.fillOval((x-rayon),
                   (y-rayon),
                   (2*rayon),
                   (2*rayon));
    }

    public double getXFin() {
        return getX() + getWidth();
    }



}
