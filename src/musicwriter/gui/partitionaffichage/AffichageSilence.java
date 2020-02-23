/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.Selection;
import musicwriter.donnees.Duree;
import musicwriter.donnees.Rational;
import musicwriter.donnees.Silence;
import java.awt.Color;
import musicwriter.gui.Graphics;
import musicwriter.donnees.Portee;
import musicwriter.gui.*;

/**
 *
 * @author proprietaire
 */
public class AffichageSilence extends AffichageElementMusical {
    private double x;
    private static Image imgSoupir = ImageLoader.getImage("soupir.png");
    private static Image imgPause = ImageLoader.getImage("pause.png");
    private static Image imgDemiPause = ImageLoader.getImage("demi-pause.png");
    private static Image imgDemiSoupir = ImageLoader.getImage("demi-soupir.png");
    private static Image imgQuartDeSoupir = ImageLoader.getImage("quart-de-soupir.png");
    private static Image imgHuitDeSoupir = ImageLoader.getImage("huit-de-soupir.png");
    private static Image imgSeizeDeSoupir = ImageLoader.getImage("seize-de-soupir.png");

/**
 * image réellement affiché pour ce silence
 */
    private final Image img;
    



    static private Image getImage(int nbTraits)
    {
        switch(nbTraits)
        {
            case 0: return imgSoupir;
            case 1: return imgDemiSoupir;
            case 2: return imgQuartDeSoupir;
            case 3: return imgHuitDeSoupir;
            case 4: return imgSeizeDeSoupir;
            default: return imgSeizeDeSoupir;
        }
    }
    


    public AffichageSilence(final Systeme systeme, final double x, final Silence silence)
    {
        super(systeme, silence);
        this.x = x;

        if(getDuree().getRational().isSuperieur(new Rational(4, 1)))
            this.img = imgPause;
        else if(getDuree().getRational().isSuperieur(new Rational(2, 1)))
            this.img = imgDemiPause;
        else
            this.img = getImage(getDuree().getNombreTraitsCroche());


        
    }
    
    private Silence getSilence()
    {
        return (Silence) getElementMusical();
    }
    


    private Portee getPortee()
    {
        return getSilence().getPortee();
    }
    
    private double getNoteRayon()
    {
        return getSysteme().getNoteRayon(getPortee());
    }
    
    
    private double getYMiddle()
    {
        Rectangle r = getRectangle();
        return (r.getMaxY() + r.getMinY()) / 2;
    }
    
    
    private void drawPetitPoint(final Graphics g)
    {
        Rectangle r = getRectangle();
        dessinerDisque(g, r.getMaxX() + getNoteRayon(),
                          getYMiddle(), 0.4*getNoteRayon());
    }
    
    private void drawDeuxiemePetitPoint(final Graphics g)
    {
        Rectangle r = getRectangle();
        dessinerDisque(g, r.getMaxX() + 2*getNoteRayon(),
                          getYMiddle(), 0.4*getNoteRayon());
    }
    
    public void draw(final Graphics g) {
        Rectangle r = getRectangle();
        
        if(!g.getColor().equals(Color.BLACK))
             g.fillRect(r);
        
        g.drawImage(img, r);
        
        if(getDuree().isPremierPetitPoint())
        {
            drawPetitPoint(g);
        }
        
        
        if(getDuree().isDeuxiemePetitPoint())
        {
            drawDeuxiemePetitPoint(g);
        }
    }

    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }

    /**
     * @return le y moyen du silence
     */
    public double getY()
    {
        int coordonneVerticale = getSilence().getCoordonneeVerticaleSurPortee();

        //ça c'est pour éviter d'avoir un dessin de silence un peu entre les lignes
        coordonneVerticale = 2*(coordonneVerticale / 2);

        //ça c'est car la demi-pause est un peu plus bas que les autres silences
        // --> On ajuste
        if(img == imgDemiPause)
            coordonneVerticale--;
        
        return getSysteme().getY(getSilence().getPortee(), coordonneVerticale);
    }


    public Rectangle getRectangle() {
        double y =   getY();
        
        return RegionFactory.createRectangle(
            (int) (getX() - getNoteRayon()),
            (int) (y ),//-  (img.getIconHeight() / 2)*getSysteme().getInterLigne(getPortee())/50),
            (int) (img.getWidth()*getSysteme().getInterLigne(getPortee())/50),
            (int) (img.getHeight()*getSysteme().getInterLigne(getPortee())/50));
    }

    @Override
    public Selection getSelection(final Area area) {
        if(area.intersects(getRectangle()))
            return new Selection(getElementMusical());
        else
            return new Selection();
    }


    public void setX(double x) {
        this.x = x;
    }

    private Duree getDuree() {
        return getSilence().getDuree();
    }

    public double getX() {
        return x;
    }

    public double getXFin() {
        return getX();
    }




}
