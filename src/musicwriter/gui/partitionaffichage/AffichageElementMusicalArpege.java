/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.util.HashSet;
import java.util.Set;
import musicwriter.donnees.ElementMusicalArpege;
import musicwriter.donnees.Portee;
import musicwriter.gui.*;


/**
 *
 * @author Ancmin
 */
public class AffichageElementMusicalArpege extends AffichageElementMusical {

    private double x;
    private final Image img = ImageLoader.getImage("arpegemorceau.png");


    AffichageElementMusicalArpege(Systeme systeme, double x, ElementMusicalArpege elementMusicalArpege) {
        super(systeme, elementMusicalArpege);
        this.x = x;
       
    }




    public ElementMusicalArpege getElementMusicalArpege()
    {
        return (ElementMusicalArpege) getElementMusical();
    }


    private Portee getPortee()
    {
        return getElementMusicalArpege().getCurseurEnHaut().getPortee();
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void draw(Graphics g) {
        Rectangle r = getRectangle();

        for(int i = 0; i < r.getHeight() / getSysteme().getInterLigne(getPortee()); i++)
        {
            g.drawImage(img,
                        r.getMaxX(),
                        r.getMinY() + i*getSysteme().getInterLigne(getPortee()),
                        r.getWidth(),
                        getSysteme().getInterLigne(getPortee()));
        }
    }

    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }

    public Rectangle getRectangle() {
        double yhaut = getSysteme().getY(getElementMusicalArpege().getCurseurEnHaut());
        double ybas = getSysteme().getY(getElementMusicalArpege().getCurseurEnBas());
        return RegionFactory.createRectangle((int) x,
                             (int) yhaut,
                             (int) getWidth(),
                             (int) (ybas - yhaut));
                             
    }


    public double getWidth()
    {
        return getSysteme().getNoteRayon(getPortee());
    }


    public double getXFin()
    {
        return getX() + getWidth();
    }

    public double getXMiddle()
    {
        return x + getSysteme().getNoteRayon(getPortee())/2;
    }

    @Override
    public Set<Poignee> getPoignees()
    {
        Set<Poignee> S = new HashSet<Poignee>();
        S.add(new PoigneeCurseurArpege(getSysteme(), this, 0));
        S.add(new PoigneeCurseurArpege(getSysteme(), this, 1));
        return S;
    }

    
}
