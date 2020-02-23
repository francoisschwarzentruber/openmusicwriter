/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Color;
import musicwriter.donnees.Hauteur.Alteration;
import musicwriter.donnees.*;
import musicwriter.gui.*;



/**
 *
 * @author proprietaire
 */
public class AffichageNote extends AffichageElementMusical implements Affichage {

    static final private Image imgNoteLectureMIDIFleur = ImageLoader.getImage("note-fleur.png");
    static final private Image imgNoteLectureMIDIPacMan = ImageLoader.getImage("note-pacman.png");
    static final private Image imgNoteDureeVague = ImageLoader.getImage("note-dureevague.png");
    static final private Color colorLectureMIDIPacmanLangue = new Color(0.0f, 0.5f, 0.0f);

    private float noteCentreX;
    private final Alteration alterationParDefaut;
    


    
    AffichageNote(Systeme systeme, Note note, float notecentrex) {
        super(systeme, note);
        this.noteCentreX = notecentrex;

        alterationParDefaut = systeme
                .getPartitionDonnees()
                .getAlterationParDefaut
                (note.getDebutMoment(),
                getSysteme().getDebutMoment(),
                note.getPortee(),
                note.getHauteur());
        
    }
    
    
    private Note getNote()
    {
        return (Note) getElementMusical();
    }


    private Moment getFinMoment()
    {
        return getNote().getFinMoment();
    }

    private Area getShape() {
        float cercleRayon = (float) getNoteRayon();
        float cercleCentreY = (float) getSysteme().getY(getNote().getPortee(), getNote().getCoordonneeVerticaleSurPortee());

        return RegionFactory.createEllipse(noteCentreX - cercleRayon - 1,
                                           cercleCentreY - cercleRayon,
                                           2 * cercleRayon + 2,
                                           2 * cercleRayon);
    }


    public double getNoteRayon()
    {
        if(getNote().isAppogiature())
             return 2*getSysteme().getNoteRayon(getPortee())/3;
        else
             return getSysteme().getNoteRayon(getPortee());
    }
    
    
    private double getInterLigne()
    {
        return getSysteme().getInterLigne(getPortee());
    }


    public Portee getPortee()
    {
        return getNote().getPortee();
    }

    
    private void afficherLigneSupplementaire(Graphics g, double xcentrenote, double yLigneSupplementaire) {
            /* affiche UNE ligne supplémentaire*/
            g.drawLine((xcentrenote - getSysteme().ligneSupplementaireLongueurGet() / 2),
                       yLigneSupplementaire, 
                       (xcentrenote + getSysteme().ligneSupplementaireLongueurGet() / 2),
                       yLigneSupplementaire);
        }
    
    

    /**
     *
     * @param g
     * @param xcentrenote
     * @param ytroisiemeligne
     * @param coordonneVerticale
     *
     * Dessine les lignes supplémentaires si jamais la note est trop aigu ou trop grave
     * ytroisiemeligne = le y de la 3e ligne centrale de la portée concernée
     */
    private void afficherLignesSupplementaires(Graphics g,
                                                   double xcentrenote,
                                                   Portee portee,
                                                   int coordonneVerticale) {
        
          g.setPenWidth(1.8f);
          //si une note est trop aigue
            for(int ligneSupplementaireCoordonneeVerticale = 6;
                 ligneSupplementaireCoordonneeVerticale <= coordonneVerticale;
                 ligneSupplementaireCoordonneeVerticale+=2)
            {
                afficherLigneSupplementaire(g,
                                            xcentrenote, 
                                            getSysteme().getY(portee,
                                      ligneSupplementaireCoordonneeVerticale));
            }

            //si une note est trop grave
            //(ce qui est suit c'est ce qui a au dessus avec tous les signes opposés)
            for(int ligneSupplementaireCoordonneeVerticale = -6;
                 ligneSupplementaireCoordonneeVerticale >= coordonneVerticale;
                 ligneSupplementaireCoordonneeVerticale-=2)
            {
                afficherLigneSupplementaire(g,
                                            xcentrenote, 
                                            getSysteme().getY(portee,
                                            ligneSupplementaireCoordonneeVerticale));
            }
        }


    
    
    

    
    
    
        
    
    
    private void drawPetitPoint(Graphics g)
    {
        double y = getNoteCentreY();

        if(isSurLigne())
            y -= getInterLigne()*0.4;
        
        dessinerDisque(g, noteCentreX + 2.2*getNoteRayon(),
                          y, 0.4*getNoteRayon());
    }
    
    private void drawDeuxiemePetitPoint(Graphics g)
    {
        double y = getNoteCentreY();

        if(isSurLigne())
            y -= getInterLigne()*0.4;
        
        dessinerDisque(g, noteCentreX + 3.2*getNoteRayon(),
                          y, 0.4*getNoteRayon());
    }




    private void drawLectureMIDIPacManLangue(Graphics g, int x1, int y, int x2, double ranimation)
    {
        int n = (int) ((x2 - x1) / getInterLigne());
        int epaisseur = (int) ((getInterLigne()/8) * (1.2+Math.cos(ranimation*5)));
        for(int i = 0; i<n; i++)
        {
            int x = x1 + (int) (i*getInterLigne());
            g.drawLine(x, y - epaisseur,
                    x + (getInterLigne()/2),
                    y + epaisseur);
            g.drawLine(x + getInterLigne(), y - epaisseur,
                    x + (getInterLigne()/2),
                    y + epaisseur);
        }
    }

    public void dessinerLectureMIDIFleur(Graphics g, Moment moment)
    {
        Rectangle r = getRectangle();
        r.grow((int) (0.3*getInterLigne()), (int) (0.3*getInterLigne()));

        if(getNote().getDuree().isStrictementInferieur(new Duree(new Rational(2, 1))))
        {
            float angle = (float) moment.getRational().getRealNumber()*2;
            g.drawImage(imgNoteLectureMIDIFleur, r, angle);
        }
        else
        {
            g.drawImage(imgNoteLectureMIDIPacMan, r);
            g.setPenWidth(2.0f);
            g.setColor(colorLectureMIDIPacmanLangue);

            drawLectureMIDIPacManLangue(g, (int) r.getCenterX(),
                       (int) r.getCenterY(),
                       (int) getSysteme().getXNotes(moment),
                       moment.getRealNumber());
        }

    }



    private boolean isSurLigne()
    {
        return (getNote().getCoordonneeVerticaleSurPortee() % 2 == 0);
    }


    @Override
    public void draw(Graphics g) {        
        afficherLignesSupplementaires(g,
                                      noteCentreX,
                                      getNote().getPortee(),
                                      getNote().getCoordonneeVerticaleSurPortee());

        //affiche les altérations #, b, etc.
        if(!getNote().getHauteur().getAlteration().equals(alterationParDefaut))
            getSysteme().dessinerAlterationGauche(g,
                               getNote().getHauteur().getAlteration(),
                               getNoteCentreX() - getNoteRayon(),
                               getPortee(),
                               getCoordonneeVerticaleSurPortee());
        
        
        if(getNote().getDuree().getRational().isZero()
                || getNote().getDuree().isVague())
        {
            Rectangle r = getRectangle();
            r.grow((int) (0.3*getInterLigne()), (int) (0.3*getInterLigne()));

            if(getNote().getDuree().isStrictementInferieur(new Duree(new Rational(2, 1))))
            {
                g.drawImage(imgNoteDureeVague, r);
            }
            return;
        }

        


        switch(getNote().getNoteFigure())
        {
            case OVALE:
                if(getNote().getDuree().getRational().isSuperieur(new Rational(2, 1)))
                    dessinerCercle(g, getNoteCentreX(), getNoteCentreY(), getNoteRayon());
                else
                    dessinerDisque(g, getNoteCentreX(), getNoteCentreY(), getNoteRayon());
                break;

            case LOSANGE:
                if(getNote().getDuree().getRational().isSuperieur(new Rational(2, 1)))
                    dessinerLosange(g, getNoteCentreX(), getNoteCentreY(), getNoteRayon());
                else
                    dessinerLosangeRempli(g, getNoteCentreX(), getNoteCentreY(), getNoteRayon());
                break;

           case CROIX:
                    dessinerCroix(g, getNoteCentreX(), getNoteCentreY(), getNoteRayon());
                break;
        }
        
        
        
        if(getNote().getDuree().isPremierPetitPoint())
        {
            drawPetitPoint(g);
        }
        
        
        if(getNote().getDuree().isDeuxiemePetitPoint())
        {
            drawDeuxiemePetitPoint(g);
        }
        
        
        

        if(getNote().isLieeALaSuivante())
            drawLieeALaSuivante(g);

        
        
        
    }


    @Override
    public Selection getSelection(Area area) {
        
        Area a = getShape();
        a.intersect(area);
        
        if(!a.isEmpty())
        {
            return new Selection(getNote());
        }
        else
            return null;
        
    }

    @Override
    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }
    
    @Override
    public Rectangle getRectangle()
    {
        double cercleRayon = getNoteRayon();

        return RegionFactory.createRectangle((int) (getNoteCentreX() - cercleRayon),
                             (int) (getNoteCentreY() - cercleRayon),
                             (int) (2 * cercleRayon)+1,
                             (int) (2 * cercleRayon)+1);
    }


    public double getNoteCentreY()
    {
        return  getSysteme().getY(getNote().getPortee(), getCoordonneeVerticaleSurPortee());
    }


    private double getNoteCentreX()
    {
        return noteCentreX;
    }

    public double getX() {
        return noteCentreX;
    }

    public double getXFin() {
        return getX() + getNoteRayon();
    }





    private int getCoordonneeVerticaleSurPortee()
    {
        return getNote().getCoordonneeVerticaleSurPortee();
    }

    

    public void setX(double x) {
        noteCentreX = (float) x;
    }

    private void drawLieeALaSuivante(Graphics g) {
        ControlCurve p = new NatCubic();
        double xdebut = (getNoteCentreX() + getNoteRayon() + getInterLigne() * 0.2 );
        double xfin = getSysteme().getXNotes(getFinMoment()) - getNoteRayon()  - getInterLigne() * 0.2;
        double y = getNoteCentreY() + getNoteRayon();
        p.addPoint( (int) xdebut, (int) y);
        p.addPoint( (int) (xdebut + xfin) / 2, (int) (y + getInterLigne() * 0.5));
        p.addPoint( (int) xfin, (int) (y));
        p.paint(g);
    }


    private Polygon getLosangePolygon(double noteCentreX, double noteCentreY, double noteRayon)
    {
        Polygon p = RegionFactory.createPolygon();
        p.addPoint((int) (noteCentreX),
                   (int) (noteCentreY - noteRayon));
        p.addPoint((int) (noteCentreX + noteRayon),
                   (int) (noteCentreY));
        p.addPoint((int) (noteCentreX),
                   (int) (noteCentreY + noteRayon));
        p.addPoint((int) (noteCentreX - noteRayon),
                   (int) (noteCentreY));
        return p;

    }
    private void dessinerLosange(Graphics g, double noteCentreX, double noteCentreY, double noteRayon) {
        Polygon p = getLosangePolygon(noteCentreX, noteCentreY, noteRayon);
        g.drawPolygon(p);
    }

    private void dessinerLosangeRempli(Graphics g, double noteCentreX, double noteCentreY, double noteRayon) {
        Polygon p = getLosangePolygon(noteCentreX, noteCentreY, noteRayon);
        g.drawPolygon(p);
        g.fillPolygon(p);
    }

    private void dessinerCroix(Graphics g, double noteCentreX, double noteCentreY, double noteRayon) {
        g.drawLine((int) (noteCentreX - noteRayon),
                   (int) (noteCentreY - noteRayon),
                   (int) (noteCentreX + noteRayon),
                   (int) (noteCentreY + noteRayon));

        g.drawLine((int) (noteCentreX - noteRayon),
                   (int) (noteCentreY + noteRayon),
                   (int) (noteCentreX + noteRayon),
                   (int) (noteCentreY - noteRayon));
    }

//    @Override
//    public Set<Poignee> getPoignees() {
//        Set<Poignee> P = new HashSet<Poignee>();
//        P.add(new PoigneeElementMusicalDureeFin(getSysteme(), getNote()));
//        return P;
//    }

    public int getCoordonneeVerticale() {
        return getNote().getCoordonneeVerticaleSurPortee();
    }



    


}
