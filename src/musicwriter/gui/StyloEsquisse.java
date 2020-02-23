/*/
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

import musicwriter.controller.PartitionPanelModeSelection;
import musicwriter.gui.partitionaffichage.PartitionVue;
import musicwriter.donnees.Curseur;
import java.awt.Color;
import musicwriter.gui.*;
import java.util.ArrayList;
import musicwriter.Geometrie;

/**
 *
 * @author proprietaire
 */
public class StyloEsquisse {
    private final ArrayList<StyloPoint> points;
    private final PartitionVue partitionVue;

    public boolean isPoint() {
        return getDiametre() < 6;
    }


    public Point getPremierPoint()
    {
        return getSegmentPremierPoint();
    }

    public Point getDernierPoint() {
        return points.get(points.size() - 1).getPoint();
    }


    /**
     *
     * @return le curseur du premier point de l'esquisse
     */
    public Curseur getCurseur() {
        return points.get(0).getCurseur();
    }

    public ArrayList<Point> getPoints() {
        ArrayList<Point> P = new ArrayList<Point>();

        for(StyloPoint p : points)
        {
            P.add(p.getPoint());
        }

        return P;
    }

    /**
     * @return vrai ssi l'esquisse coupe le segment [p1, p2] qui est vertical (p1.x = p2.x)
     */
    public boolean isCoupeSegmentVertical(Point p1, Point p2) {
        for(int i = 0; i < points.size()-1; i++)
        {
            if(Geometrie.isSegmentCoupeSegmentVertical(points.get(i).point, points.get(i+1).point,
                    p1, p2))
                return true;
        }
        return false;
    }

    

    


    
    
    class StyloPoint
    {
        private Point point = null;

        public Curseur getCurseur() {
            return curseur;
        }

        public Point getPoint() {
            return point;
        }
        
        
        private Curseur curseur = null;

        StyloPoint(Point point, Curseur curseur) 
        {
            this.point = point;
            this.curseur = curseur;
        }
 
    }
    
    
    public StyloEsquisse(PartitionVue partitionVue)
        {
            this.points = new ArrayList<StyloPoint>();
            this.partitionVue = partitionVue;
        }
    
    
    public void styloPointAjouter(Point point)
    {
        points.add(new StyloPoint(point, partitionVue.getCurseur(point)));
    }
    
    
    
    public ArrayList<Curseur> getCurseurs()
    {
        ArrayList<Curseur> curseurs = new ArrayList<Curseur>();
        
        for(StyloPoint p : points)
        {
            curseurs.add(p.getCurseur());
        }
        
        return curseurs;
    }

    public ArrayList<Curseur> getCurseursVagues()
    {
        ArrayList<Curseur> curseurs = new ArrayList<Curseur>();

        Curseur curseurAvant = null;

        for(int i = 0; i < points.size(); i++)
        {
            StyloPoint p = points.get(i);

            if((i % 10 == 0) || (i == points.size() - 1) )
            {
                Curseur curseur = partitionVue.getCurseurVague(p.getPoint());

                if(curseurAvant != null)
                {
                if((Math.abs(curseur.getMoment().getRealNumber() - curseurAvant.getMoment().getRealNumber()) > 0.05 )
                   | (curseur.getCoordonneeVerticaleSurPortee() != curseurAvant.getCoordonneeVerticaleSurPortee()))
                        curseurs.add(curseur);
                }
                else
                    curseurs.add(curseur);
            }
        }
        
        return curseurs;
    }




    
    
    
    
    private double getAngleAuPoint(int i)
    {
        return Math.atan2(points.get(i+1).point.y - points.get(i).point.y,
                          points.get(i+1).point.x - points.get(i).point.x);
    }
    
    
    
        
    private double getAngleEntreExtremiteLaPlusLoinEtPoint(int i)
    {
        if(i < points.size() / 2)
        {
            int ifin = points.size() - 1;
            return Math.atan2(points.get(ifin).point.y - points.get(i).point.y,
                              points.get(ifin).point.x - points.get(i).point.x);
        }
        else
        {
            return Math.atan2(points.get(i).point.y - points.get(0).point.y,
                          points.get(i).point.x - points.get(0).point.x);
        }
        
    }
    
    
    private double getSegmentAngleMoyen()
    {
        double sommeangle = 0;
        for(int i = 0; i < points.size() - 2; i++)
        {
            sommeangle += getAngleEntreExtremiteLaPlusLoinEtPoint(i);
        }
        
        return sommeangle / (points.size() - 1);
    }
    
    
    public double getSegmentLongueur()
    {
        int ifin = points.size() - 1;
        return points.get(ifin).getPoint().distance(points.get(0).getPoint());
    }


    public Point getSegmentPremierPoint()
    {
        return points.get(0).getPoint();
    }
    
    
    
    public boolean isSegmentVertical()
    {
        return isSegment() && Math.abs(getSegmentAngleMoyen() - Math.PI / 2) < 0.3;
    }
    
    
    
    public boolean isSegmentHorizontal()
    {
        return isSegment() && Math.abs(getSegmentAngleMoyen()) < 0.3;
    }
    
    
    public boolean isSegment()
    {
        if(points.size() < 2)
            return false;
        
        double anglemoyen = getSegmentAngleMoyen();
        
        if(points.size() > 10)
            anglemoyen += 0;
        
        
        
        for(int i = 0; i < points.size() - 2; i++)
        {
            if(Math.abs(getAngleEntreExtremiteLaPlusLoinEtPoint(i) - anglemoyen) > 0.5)
                return false;
        }
        
        
        return true;
    }
    
    


    private double getDiametre()
    {
        double d = 0;
        for(StyloPoint p : points)
        {
            for(StyloPoint p2 : points)
            {
                if(d < p.getPoint().distanceSq(p2.getPoint()))
                {
                    d = p.getPoint().distanceSq(p2.getPoint());
                }
            }
        }
        return d;
    }
    
    private float calcA(Point P, Point Q)
    {
        if(P.x == Q.x)
             return 1;
        else
            return -(P.y - Q.y) / (P.x - Q.x);
    }
    
    
    private float calcB(Point P, Point Q)
    {
        if(P.x == Q.x)
             return 0;
        else
            return 1;
    }
    
    
    
    private float calcC(Point P, Point Q)
    {
        if(P.x == Q.x)
             return -P.x;
        else
            return -(calcA(P, Q)*P.x + calcB(P, Q)*P.y);
        
        
    }
    
    
    
    
    private Point isIntersection(Point A, Point B, Point C, Point D)
    //renvoie le point d'intersection si les segments [AB] et [CD] s'intersectent
    //sinon renvoie null        
    {
        float a1 = calcA(A, B);
        float b1 = calcB(A, B);
        float c1 = calcC(A, B);
        
        float a2 = calcA(C, D);
        float b2 = calcB(C, D);
        float c2 = calcC(C, D);
        
        float delta = a1*b2 - a2*b1;
        
        
        if(delta == 0)
            return null;
        else
        {
            Point I = new Point((int) ((b2*c1 - b1*c2) / delta),
                    (int)((- a2*c1 + a1*c2)/ delta));
            
            if(((a1*C.x + b1*C.y + c1) * (a1*D.x + b1*D.y + c1) <= 0) &&
             ((a2*A.x + b2*A.y + c2) * (a2*B.x + b2*B.y + c2) <= 0))
                return I;
            else
                return null;
                       
        }

    }




    
    
    
    
    private int nbIntersectionInPoints()
    {
        int compteur = 0;
        for(int i = 0; i < points.size() - 2; i++)
        {
            for(int j = i+3; j < points.size() - 1; j++)
                    if(isIntersection(points.get(i).getPoint(),
                                      points.get(i+1).getPoint(),
                                      points.get(j).getPoint(),
                                      points.get(j+1).getPoint()) != null)
                        compteur++;
                
        }
        
        return compteur;
        
    }
    
    
    
    public boolean isGribouilli()
    {
        if(isSegment())
            return false;
        else
        return (points.size() > 5) && (nbIntersectionInPoints() > 10);
    }
    
    
    public boolean isPolygon()
    {
        return ( (points.size() > 3) && !isSegment() && (nbIntersectionInPoints() < 3) );
    }
    
    
    
    public Polygon getPolygon()
    {
        Polygon p = RegionFactory.createPolygon();
        for(int i = 0; i < points.size() - 1; i++)
        {
            p.addPoint(points.get(i).getPoint().x,
                       points.get(i).getPoint().y);
        }
        
        return p;
    }
    
    
    public void afficher(Graphics g)
    {
        if(points.isEmpty())
            return;
        
        StyloPoint pa = points.get(0);
        
        
        if(isGribouilli())
        {
            g.setPenWidth(8);
            g.setColor(Color.white);
        }
        else if(isSegment())
        {
            g.setPenWidth(1.5f);
            g.setColor(Color.blue);
        }
        else
        {
            g.setPenWidth(1.5f);
            g.setColor(PartitionPanelModeSelection.getColorSelection());
        }
        
        for(StyloPoint p : points)
        {
            g.drawLine(pa.getPoint().x, pa.getPoint().y, p.getPoint().x, p.getPoint().y);
            pa = p;
        }
    }
    
    
    
    public void afficherTraitCroche(Graphics g)
    {
        g.setPenWidth(4.0f);
        g.setColor(Color.BLACK);
        
        if(points.isEmpty())
            return;
        
        StyloPoint pa = points.get(0);
        for(StyloPoint p : points)
        {
            g.drawLine(pa.getPoint().x, pa.getPoint().y, p.getPoint().x, p.getPoint().y);
            pa = p;
        }
    }
    
    
}
