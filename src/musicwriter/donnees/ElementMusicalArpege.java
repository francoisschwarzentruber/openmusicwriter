/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalArpege extends ElementMusicalMultiCurseurs {


    public Curseur getCurseurEnBas() {
        return getCurseur(0);
    }

    public void setCurseurEnBas(Curseur curseurEnBas) {
        setCurseur(0, curseurEnBas);
    }

    public Curseur getCurseurEnHaut() {
        return getCurseur(1);
    }

    public void setCurseurEnHaut(Curseur curseurEnHaut) {
        setCurseur(1, curseurEnHaut);
        
    }



   


    public ElementMusicalArpege(Curseur curseurEnBas, Curseur curseurEnHaut)
    {
        super(curseurEnBas, curseurEnHaut);

    }

    
    @Override
    public Object clone() {
        return new ElementMusicalArpege(((Curseur) getCurseurEnBas()).clone(),
                                        ((Curseur) getCurseurEnHaut()).clone());
    }

    
}
