/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.gui.partitionaffichage;

import java.awt.Font;
import java.util.ArrayList;
import musicwriter.Arithmetique;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Selection;
import musicwriter.gui.*;

/**
 * Cette classe représente l'affichage des traits de croches, double-croches etc.
 * pour une succession d'accords
 * @author proprietaire
 */
public class AffichageCrocheReliee implements Affichage {

    final private ArrayList<AffichageAccord> affichageAccords;
    final private Systeme systeme;
    final private double pente;
    final private int traitepaisseur;
    final private int traitespacement;

    public double getPente() {
        return pente;
    }

    /**
     *
     * @param a
     * @return crée un tableau avec un unique accord
     */
    static private ArrayList<AffichageAccord> arrayListSingleton(AffichageAccord a) {
        ArrayList<AffichageAccord> T = new ArrayList<AffichageAccord>();
        T.add(a);
        return T;
    }

    private Portee getPortee() {
        return getAffichagesAccords().get(0).getPortee();
    }

    AffichageCrocheReliee(Systeme systeme, AffichageAccord a) {
        this(systeme, arrayListSingleton(a));

    }


    static public Area getAreaAuDessus(ArrayList<AffichageAccord> T)
    {
        
        final double pente = penteCalculer(T);
        final double interligne = T.get(0).getSysteme().getInterLigne(T.get(0).getPortee());
        final double maxhampeY0 = getMaxHampeYPremierAccordSiHampeVersLeHaut(T)-interligne/2;
        final int hauteur = (int) interligne * 4;
        final int n = T.size() - 1;
        
        final Polygon p = RegionFactory.createPolygon();
        p.addPoint((int) (T.get(0).getHampeX() - interligne/2), (int) maxhampeY0);
        p.addPoint((int) (T.get(n).getHampeX() + interligne/2), (int) (maxhampeY0 + pente * (T.get(n).getHampeX() - T.get(0).getHampeX())));
        p.addPoint((int) (T.get(n).getHampeX() + interligne/2), (int) (maxhampeY0 + pente * (T.get(n).getHampeX() - T.get(0).getHampeX())) - hauteur);
        p.addPoint((int) (T.get(0).getHampeX() - interligne/2), (int) maxhampeY0 - hauteur);
        return RegionFactory.createRegion(p);
    }




    static public Area getAreaAuDessous(ArrayList<AffichageAccord> T)
    {
        
        final double pente = penteCalculer(T);
        final double interligne = T.get(0).getSysteme().getInterLigne(T.get(0).getPortee());
        final double minhampeY0 = getMinHampeYPremierAccordSiHampeVersLeBas(T)+interligne/2;
        final int hauteur = (int) (interligne * 4);
        final int n = T.size() - 1;

        final Polygon p = RegionFactory.createPolygon();
        p.addPoint((int) (T.get(0).getHampeX()  - interligne/2), (int) minhampeY0);
        p.addPoint((int) (T.get(n).getHampeX()  + interligne/2), (int) (minhampeY0 + pente * (T.get(n).getHampeX() - T.get(0).getHampeX())));
        p.addPoint((int) (T.get(n).getHampeX()  + interligne/2), (int) (minhampeY0 + pente * (T.get(n).getHampeX() - T.get(0).getHampeX())) + hauteur);
        p.addPoint((int) (T.get(0).getHampeX()  - interligne/2), (int) minhampeY0 + hauteur);
        return RegionFactory.createRegion(p);
    }


    
    AffichageCrocheReliee(Systeme systeme, ArrayList<AffichageAccord> accords) {
        this.systeme = systeme;
        this.affichageAccords = accords;

        if (accords.get(0).isHampeVersLeHaut()) {
            for (AffichageAccord a : accords) {
                a.setHampeVersLeHautSiAutomatique();
            }
        } else {
            for (AffichageAccord a : accords) {
                a.setHampeVersLeBasSiAutomatique();
            }
        }


        traitepaisseur = (int) (systeme.getInterLigne(accords.get(0).getPortee()) * 0.5);
        traitespacement = (int) (systeme.getInterLigne(accords.get(0).getPortee()) * 0.8);

        
        
        if (accords.size() > 1) {
            pente = penteCalculer();
            calculerHampesY();
        }
        else //s'il n'y qu'un seul accord, il n'y a aucune pente !
        {
            pente = 0.0f;
        }


    }

    @Override
    public void draw(Graphics g) {
        if (affichageAccords.size() == 1) {
            drawQueue(g, affichageAccords.get(0));
        } else {
            drawTraits(g, affichageAccords);
            drawNumeroSiBesoin(g, affichageAccords);

        }


    }

    public Area getArea() {
        Polygon polygon = RegionFactory.createPolygon();
        
        polygon.addPoint((int) getX(), (int) getHampeYFin(0)-8);
        polygon.addPoint((int) getXFin(), (int) getHampeYFin(getNbAccords()-1)-8);
        polygon.addPoint((int) getXFin(), (int) getHampeYFin(getNbAccords()-1) + 16);
        polygon.addPoint((int) getX(), (int) getHampeYFin(0) + 16);                
        
        return RegionFactory.createRegion(polygon);
    }

    @Override
    public Rectangle getRectangle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Selection getSelection(Area area) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ElementMusical getElementMusical(Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int n() {
        return affichageAccords.size() - 1;
    }

    private double getHampeX(int i) {
        return affichageAccords.get(i).getHampeX();
    }

    private double getHampeYFin(int i) {
        return affichageAccords.get(i).getHampeYFin();
    }

    private int getNombreTraits(int i) {
        return affichageAccords.get(i).getNombreTraitsCroche();
    }

    
    private double penteCalculer()
    {
        return penteCalculer(affichageAccords);
    }


    static private double penteCalculer(ArrayList<AffichageAccord> affichageAccords) {
        final double penteLimite = 0.3;
        final int n = affichageAccords.size() - 1;

        /**
         * s'il n'y a qu'un seul accord, pente = 0.0 par défaut
         */
        if(n == 0)
            return 0.0;

        final double penteTemporaire;
        
        penteTemporaire = (affichageAccords.get(n).getHampeYFin() - affichageAccords.get(0).getHampeYFin())
                / (affichageAccords.get(n).getHampeX() - affichageAccords.get(0).getHampeX());

        if (penteTemporaire < -penteLimite) {
            return -penteLimite;
        } else if (penteTemporaire > penteLimite) {
            return penteLimite;
        } else {
            return penteTemporaire;
        }
    }

    static private double getMaxHampeYPremierAccordSiHampeVersLeHaut(ArrayList<AffichageAccord> affichageAccords)
    {

        final double pente = penteCalculer(affichageAccords);
        final int n = affichageAccords.size() - 1;

        double miny = affichageAccords.get(0).getMinY();

        if(n == 0)
            return miny;
        
            for (int i = 0; i <= n; i++) {
                final double y = affichageAccords.get(i).getMinY()
                        - (affichageAccords.get(i).getHampeX() - affichageAccords.get(0).getHampeX()) * pente;
                if (y < miny) {
                    miny = y;
                }
            }
        return miny;
    }


    static private double getMinHampeYPremierAccordSiHampeVersLeBas(ArrayList<AffichageAccord> affichageAccords)
    {
        final double pente = penteCalculer(affichageAccords);
        final int n = affichageAccords.size() - 1;
        double maxy = affichageAccords.get(0).getMaxY();

         if(n == 0)
            return maxy;

        
        for (int i = 0; i <= n; i++) {
            final double y = affichageAccords.get(i).getMaxY()
                    - (affichageAccords.get(i).getHampeX() - affichageAccords.get(0).getHampeX()) * pente;
            if (y > maxy) {
                maxy = y;
            }
        }
        return maxy;
    }

    private void calculerHampesY() {
        if (affichageAccords.get(0).isHampeVersLeHaut()) {
            final double maxy = getMaxHampeYPremierAccordSiHampeVersLeHaut(affichageAccords);
            affichageAccords.get(0).setHampeYFin((maxy)
                    - 3 * systeme.getInterLigne(affichageAccords.get(0).getPortee()));


        } else {
            final double miny = getMinHampeYPremierAccordSiHampeVersLeBas(affichageAccords);

            affichageAccords.get(0).setHampeYFin(miny
                     + 3 * systeme.getInterLigne(affichageAccords.get(0).getPortee()) + pente);
        }

        /**
         * adapte tous les accords
         */
        for (int i = 0; i <= n(); i++) {
            affichageAccords.get(i).setHampeYFin(affichageAccords.get(0).getHampeYFin()
                    + pente * (affichageAccords.get(i).getHampeX() - affichageAccords.get(0).getHampeX()));
        }

    }





    static private Image crochetImg = ImageLoader.getImage("crochet.png");
    static private Image crochetImgGris = ImageLoader.getImage("crochet_gris.png");


    
    /**
     * affiche les queues isolés pour l'accord a selon la durée de cette accord
     * @param g
     * @param a
     */
     static private void drawQueue(Graphics g, AffichageAccord a) {
        double y1 = a.getHampeYFin();
        double x = a.getHampeX();

        final Image img;

        if(g.getColor().equals(Color.BLACK))
        {
            img = crochetImg;
        }
        else
            img = crochetImgGris;


        double interligne = a.getSysteme().getInterLigne(a.getPortee());
        g.setPenWidth(3.0f);
        for (int i = 0; i < a.getNombreTraitsCroche(); i++) {
            g.drawImage(img,
                     x,
                     (y1 + i * 0.6 * interligne * a.getHampe1SiDirectionHaut()),
                     interligne,
                     (2.1 * a.getHampe1SiDirectionHaut() * interligne));
        }
    }

    private void drawTraitSurUnSeul(Graphics g, int i, int traitnumero, int signe) {
        traitnumero--;

        int directionx;

        if (i == 0) {
            directionx = 1;
        } else {
            directionx = -1;
        }

        int x1 = (int) getHampeX(i);
        int y1 = (int) getHampeYFin(i) - 1 + traitespacement * traitnumero * signe;

        int x2 = (int) (getHampeX(i) + directionx * systeme.getInterLigne(getPortee()));
        int y2 = (int) (getHampeYFin(i) - 1 + traitespacement * traitnumero * signe + directionx * getPente() * systeme.getInterLigne(getPortee()));

        Polygon P = RegionFactory.createPolygon();

        P.addPoint(x1, y1);
        P.addPoint(x2, y2);
        P.addPoint(x2, y2 + traitepaisseur);
        P.addPoint(x1, y1 + traitepaisseur);
        g.fillPolygon(P);
    }

    private void drawTrait(Graphics g,
            int accorddebuti,
            int accordfini,
            int traitnumero,
            int signe) {
        if (accorddebuti == accordfini) {
            drawTraitSurUnSeul(g, accorddebuti, traitnumero, signe);
        } else {
            traitnumero--;
            int x1 = (int) getHampeX(accorddebuti);
            int y1 = (int) getHampeYFin(accorddebuti) - 1 + traitespacement * traitnumero * signe;

            int x2 = (int) getHampeX(accordfini);
            int y2 = (int) getHampeYFin(accordfini) - 1 + traitespacement * traitnumero * signe;

            Polygon P = RegionFactory.createPolygon();

            P.addPoint(x1, y1);
            P.addPoint(x2, y2);
            P.addPoint(x2, y2 + traitepaisseur);
            P.addPoint(x1, y1 + traitepaisseur);
            g.fillPolygon(P);
        }

    }

    private void drawTraits(Graphics g, ArrayList<AffichageAccord> accords) {
        //g.setPenWidth(4));

        int signe = accords.get(0).getHampe1SiDirectionHaut();

        for (int t = 1; t < 5; t++) {
            int debuti = -1;
            for (int i = 0; i <= n(); i++) {
                if (debuti == -1) {
                    if (getNombreTraits(i) >= t) {
                        debuti = i;
                    }
                }

                if (debuti > -1) {
                    if (getNombreTraits(i) < t) {
                        drawTrait(g, debuti, i - 1, t, signe);
                        debuti = -1;
                    } else {
                        if (i == n()) {
                            drawTrait(g, debuti, i, t, signe);
                        }
                    }
                }

            }
        }
    }

    public void setX(double x) {
    }

    public double getX() {
        return getHampeX(0);
    }

    public double getXFin() {
        return getHampeX(getNbAccords()-1);
    }

    private ArrayList<AffichageAccord> getAffichagesAccords() {
        return affichageAccords;
    }

    private void drawNumeroSiBesoin(Graphics g, ArrayList<AffichageAccord> affichageAccords) {
        /**
         * si les éléments musicaux n'ont pas la même durée, ce n'est même pas la peine
         */
        for (int i = 1; i < affichageAccords.size(); i++) {
            if (!affichageAccords.get(i).getDuree().equals(affichageAccords.get(0).getDuree())) {
                return;
            }
        }


        /**
         * si c'est une puissance de deux, on indique rien (c'est implicite)
         */
        if (Arithmetique.isPuissanceDeDeux((int) affichageAccords.get(0).getDuree().getRational().getDenominateur())) {
            return;
        }


        /**
         * affiche
         */
        double x = (getHampeX(0) + getHampeX(n())) / 2;
        double y = (getHampeYFin(0) + getHampeYFin(n())) / 2;


        g.setFont(new Font(g.getFont().getName(), 0, systeme.getFontSize(10)));
        if (!affichageAccords.get(0).isHampeVersLeHaut()) {
            y += g.getFontHeight();
        } else {
            y -= traitepaisseur;
        }

        g.drawString(String.valueOf(
                affichageAccords.size()),
                (int) x,
                (int) y);
    }

    private int getNbAccords() {
        return affichageAccords.size();
    }
    
    
    public Selection getSelection()
    {
        Selection selection = new Selection();
        for(AffichageAccord a : getAffichagesAccords())
        {
            selection.elementMusicalAjouter(a.getAccord());
        }
        return selection;
    }


    
}
