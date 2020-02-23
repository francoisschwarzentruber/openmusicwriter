/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import java.awt.Font;
import musicwriter.donnees.ElementMusicalParoleSyllabe;

/**
 *
 * @author Ancmin
 */
public class AffichageElementMusicalParoleSyllabe extends AffichageElementMusicalTexte {

    

    
    public AffichageElementMusicalParoleSyllabe(Systeme systeme, ElementMusicalParoleSyllabe element)
    {
        super(systeme, element);

    }

    @Override
    public Font getFont() {
       return new Font("", Font.PLAIN, getSysteme().getFontSize(getElementMusicalTexte().getPortee(), 14));
    }

    @Override
    public double getY() {
        return getSysteme().getY(getElementMusicalTexte().getPortee(), -10);
    }





}
