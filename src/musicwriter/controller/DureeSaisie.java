/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import musicwriter.donnees.Duree;
import musicwriter.donnees.Rational;

/**
 *
 * @author Ancmin
 */
 public class DureeSaisie {
    
    static private int lastnumber = 0;
    static private long lasttimefordureechange = 0;
    static private Duree duree;
    
      static boolean traiter(final int n)
      {
        if(n != 0)
        {
            if(System.currentTimeMillis() < lasttimefordureechange + 300)
            {
                duree = new Duree(new Rational(lastnumber, n));
            }
            else
            {
                lastnumber = n;
                lasttimefordureechange = System.currentTimeMillis();
                duree = new Duree(new Rational(n, 1));
                
            }
            return true;

        }
        return false;
    }
    
    static Duree getDuree()
    {
        return duree;
    }
}
