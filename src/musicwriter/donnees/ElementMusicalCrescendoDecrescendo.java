/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalCrescendoDecrescendo extends ElementMusicalMultiCurseurs {

    public ElementMusicalCrescendoDecrescendo(Curseur curseurPiano, Curseur curseurForte)
    {
        super(curseurPiano, curseurForte);
    }


    public Curseur getCurseurPiano()
    {
        return getCurseur(0);
    }


    public Curseur getCurseurForte()
    {
        return getCurseur(1);
    }


    @Override
    public Object clone() {
        return new ElementMusicalCrescendoDecrescendo(((Curseur) getCurseur(0)).clone(),
                                                      ((Curseur) getCurseur(1)).clone());
    }

}
