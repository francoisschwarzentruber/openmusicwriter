/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Font;
import java.awt.FontMetrics;
import musicwriter.donnees.ElementMusicalTexte;
import musicwriter.gui.*;
/**
 *
 * @author Ancmin
 */
public class AffichageElementMusicalTexte extends AffichageElementMusical {
    
    public AffichageElementMusicalTexte(Systeme systeme, ElementMusicalTexte element)
    {
        super(systeme, element);
       
    }



    public ElementMusicalTexte getElementMusicalTexte()
    {
        return (ElementMusicalTexte) getElementMusical();
    }


        
    @Override
    public double getX() {
        return getSysteme().getXNotes(getElementMusical().getDebutMoment());
    }


    public double getY()
    {
        return getSysteme().getY(getElementMusicalTexte().getCurseur());
    }


    @Override
    public void setX(double x) {        
    }



    public Font getFont()
    {
        return new Font("", Font.ITALIC, getSysteme().getFontSize(getElementMusicalTexte().getPortee(), 14));
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(getFont());
        g.drawString(getElementMusicalTexte().getTexte(), (float) getX(), (float) getY());
    }

    @Override
    public Area getArea() {
        return RegionFactory.createRegion(getRectangle());
    }


    public double getWidth() {
        return 32;
        //return getFontMetrics(getFont()).stringWidth(getElementMusicalTexte().getTexte());
    }


    public double getHeight()
    {
        //return getFontMetrics(getFont()).getHeight();
        return 32;
    }
    
    @Override
    public Rectangle getRectangle() {
        return RegionFactory.createRectangle((int) getX(), (int) (getY() - getHeight()), (int) getWidth(), (int) getHeight());
    }


    public double getXFin() {
        return getX() + getWidth();
    }


}
