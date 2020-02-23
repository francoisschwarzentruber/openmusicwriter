package musicwriter.donnees;

import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ancmin
 */
public class ElementMusicalCourbe extends ElementMusicalMultiCurseurs {
    public ElementMusicalCourbe(ArrayList<Curseur> curseurs)
    {
        super(curseurs);
    }


    public ElementMusicalCourbe(Curseur curseur0,Curseur  curseur1, Curseur curseur2)
    {
        super(curseur0, curseur1, curseur2);
    }


    @Override
    public Object clone() {
        return new ElementMusicalCourbe(arrayListCloner(getCurseurs()));
    }
}
