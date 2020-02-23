/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalLigne extends ElementMusicalMultiCurseurs {

    public ElementMusicalLigne(Curseur curseur0, Curseur curseur1) {
        super(curseur0, curseur1);
    }
    
    @Override
    public Object clone() {
        return new ElementMusicalLigne(((Curseur) getCurseur(0)).clone(),
                                        ((Curseur) getCurseur(1)).clone());
    }

}
