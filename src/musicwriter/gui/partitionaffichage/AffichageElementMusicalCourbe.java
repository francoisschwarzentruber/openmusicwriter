/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;


import java.util.ArrayList;
import musicwriter.donnees.Curseur;
import musicwriter.donnees.ElementMusicalCourbe;
import musicwriter.gui.*;
/**
 *
 * @author Ancmin
 */
public class AffichageElementMusicalCourbe extends AffichageElementMusicalMultiCurseurs {

    
    public AffichageElementMusicalCourbe(Systeme systeme, ElementMusicalCourbe elementMusicalCourbe) {
        super(systeme, elementMusicalCourbe);

    }


    public ElementMusicalCourbe getElementMusicalCourbe()
    {
        return (ElementMusicalCourbe) getElementMusical();
     }

    
    public ArrayList<Curseur> getCurseurs()
    {
        return getElementMusicalCourbe().getCurseurs();
    }

    public double getX()
    {
        return getSysteme().getPoint(getCurseurs().get(0)).x;
    }

    public void setX(double x) {
        
    }

    public double getXFin() {
        return getSysteme().getPoint(getCurseurs().get(2)).x;
    }







    public void draw(Graphics g) {
        g.setPenWidth(2.0f);
        getCurve().paint(g);

    }




    private ControlCurve getCurve()
    {
        ControlCurve curve = new NatCubic();

        for(Curseur c : getCurseurs())
        {
            Point np = getSysteme().getPoint(c);
            curve.addPoint((int) np.x, (int) np.y);
        }

        return curve;
    }


    

    @Override
    public musicwriter.gui.Area getArea() {
        return getCurve().getArea();
    }

    @Override
    public Rectangle getRectangle() {
        return getArea().getBounds();
    }




}
