/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import java.util.Set;
import org.jdom.Element;
/**
 *
 * @author proprietaire
 */
public abstract class ElementMusicalSurPortee extends ElementMusical {
    private Portee portee = null;
    
    
    ElementMusicalSurPortee(Moment moment, Partie partie, Element element) {
        super(moment);
        if(element == null)
            System.out.println("problème ouverture");

        if(partie.getPorteesNombre() == 1)
        {
            setPortee(partie.getPorteePremiere());
        }
        else
        {
            if(element.getChild("staff") == null)
            {
                System.out.println("problème ouverture : il manque le numéro de la portée à une note");
                setPortee(partie.getPorteePremiere());
            }
            else
            {
                int porteeNumero = Integer.parseInt(element.getChild("staff").getText());
                setPortee(partie.getPortee(porteeNumero));
            }
        }
    }
    
    public void setPortee(Portee portee) {
        this.portee = portee;
    }
    
    
        
    public Portee getPortee()
    {
        return portee;
    }
    

    public Partie getPartie()
    {
        return portee.getPartie();
    }



    @Override
    public  boolean isSurParties(Collection<Partie> parties) {
        return parties.contains(getPartie());
    }



    

        
    ElementMusicalSurPortee(Moment debutMoment, Portee portee)
    {
        super(debutMoment);
        this.portee = portee;
    }

    @Override
    public void setCurseur(Curseur curseur) {
        super.setCurseur(curseur);
        this.setPortee(curseur.getPortee());
    }

    @Override
    public Curseur getCurseur() {
        return new Curseur(getDebutMoment(), Hauteur.getDo0(), getPortee(), null, this);
    }




    
    
}
