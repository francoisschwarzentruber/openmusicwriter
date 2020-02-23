/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Color;
import musicwriter.donnees.ElementMusicalClef;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Portee.Clef;
import musicwriter.donnees.Selection;
import musicwriter.gui.*;


/**
 * représente un affichage de Clef
 * @author Ancmin
 */
public class AffichageElementMusicalClef extends AffichageElementMusical {

    private double x;

    static final Image imgClefSol = ImageLoader.getImage("GClef.svg");
    static final Image imgClefFa = ImageLoader.getImage("FClef.svg");
    static final Image imgClefDo = ImageLoader.getImage("CClef.svg");

    private final Image img;

    public AffichageElementMusicalClef(Systeme systeme, double x, ElementMusicalClef elementMusicalClef) {
            super(systeme, elementMusicalClef);

        if(elementMusicalClef.getClef().equals(Clef.ClefDeSol))
                img = imgClefSol;
        else if(elementMusicalClef.getClef().equals(Clef.ClefDeFa))
            img = imgClefFa;
        else if(elementMusicalClef.getClef().equals(Clef.ClefDUt))
            img = imgClefDo;
        else
                img = null;

        this.x = x;
        
    }


/**
 * crée un affichage pour une clef qui n'est pas forcément dans la partition.
 * Sa position est calculée "un peu à l'arrache"
 * @param systeme
 * @param elementMusicalClef
 */
    public AffichageElementMusicalClef(Systeme systeme, ElementMusicalClef elementMusicalClef) {
        this(systeme, systeme.getXBarreAuDebut(elementMusicalClef.getDebutMoment()),
                elementMusicalClef);

        //if(systeme.getPartitionDonnees().isBarreDeMesure(getDebutMoment())   )
     //   {
            deplacerX(-getImageWidth());
       // }


    }

    void deplacerX(double delta) {
        setX(getX() + delta);
    }
        

    public ElementMusicalClef getElementMusicalClef()
    {
           return (ElementMusicalClef) getElementMusical();
    }
    

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void draw(Graphics g) {
        
        Rectangle r = getImageRectangle();

        if(!g.getColor().equals(Color.BLACK))
        {
            g.fillRect(r);
        }


        g.drawImage(img, r);
    }



    private final double scale = 0.17/8.0;

    public double getAffichageWidth()
    {
        return ((Math.max(imgClefSol.getWidth(), Math.max(imgClefFa.getWidth(), imgClefDo.getWidth()))*getSysteme().getInterLigne(getPortee()))*scale);
    }


    public double getImageWidth()
    {
        return ((img.getWidth()*getSysteme().getInterLigne(getPortee()))*scale);
    }


    public double getHeight()
    {
        return ((img.getHeight()*getSysteme().getInterLigne(getPortee()))*scale);
    }


    private Portee getPortee()
    {
        return getElementMusicalClef().getPortee();
    }

    @Override
    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }

    public Rectangle getImageRectangle() {
        int yCoordonneeVerticale = (int) getSysteme().getY(getPortee(), getVerticalLineCoordinate());
        
        return RegionFactory.createRectangle((int) x,
                    (int) (yCoordonneeVerticale  - getHeight() / 2),
                    (int) getImageWidth(),
                    (int) getHeight());
    }


    @Override
    public Rectangle getRectangle() {
        int yCoordonneeVerticale = (int) getSysteme().getY(getPortee(), 0);

        return RegionFactory.createRectangle((int) x,
                    (int) (yCoordonneeVerticale  - getHeight() / 2),
                    (int) getAffichageWidth(),
                    (int) getHeight());
    }

    @Override
    public Selection getSelection(Area area) {
         if(area.contains(getRectangle()))
             return new Selection(getElementMusical());
         else
             return new Selection();
    }

    public double getXFin() {
        return x + getAffichageWidth();
    }

    private int getVerticalLineCoordinate() {
        return ((ElementMusicalClef) getElementMusical()).getVerticalLineCoordinate();
    }





}
