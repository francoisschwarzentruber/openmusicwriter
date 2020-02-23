/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.util.HashSet;
import java.util.Set;
import musicwriter.donnees.ElementMusicalMultiCurseurs;
import musicwriter.donnees.Portee;
import musicwriter.gui.*;


/**
 *
 * @author Ancmin
 */
public abstract class AffichageElementMusicalMultiCurseurs extends AffichageElementMusical {
    AffichageElementMusicalMultiCurseurs(Systeme syteme, ElementMusicalMultiCurseurs element)
    {
        super(syteme, element);
    }


    ElementMusicalMultiCurseurs getElementMusicalMultiCurseurs()
    {
        return (ElementMusicalMultiCurseurs) getElementMusical();
    }


    public Point getCurseurPoint(int i)
    {
        return getSysteme().getPoint(getElementMusicalMultiCurseurs().getCurseur(i));
    }


    protected Portee getPortee()
    {
        return getElementMusicalMultiCurseurs().getCurseur(0).getPortee();
    }


    public Point getPoint0()
    {
        return getCurseurPoint(0);
    }

    public Point getPoint1()
    {
        return getCurseurPoint(1);
    }


    public Rectangle getRectangle() {
        Rectangle r = RegionFactory.createRectangle(getPoint0().getX(),  getPoint0().getY(), 0, 0);

        for(int i = 0; i < getElementMusicalMultiCurseurs().getCurseursNombre(); i++)
        {
            r.add(new Point((int) getCurseurPoint(i).x,  (int) getCurseurPoint(i).y));
        }
        
        return r;
    }
    

    @Override
    public Set<Poignee> getPoignees()
    {
        Set<Poignee> S = new HashSet<Poignee>();
        for(int i = 0; i < getElementMusicalMultiCurseurs().getCurseursNombre(); i++)
        {
            S.add(new PoigneeCurseur(getSysteme(), getElementMusicalMultiCurseurs(),  i));
        }
        
        return S;
    }




    

        
}
