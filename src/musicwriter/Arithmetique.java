/*
 * Ce module permet de faire des divisions de nombres entiers
 * et calculer des modulos
 * (car les instructions java pour le faire sont un peu bizarres)
 */

package musicwriter;

/**
 *
 * @author proprietaire
 */
public class Arithmetique {
    


    /**
     *
     * @param numero
     * @param diviseur
     * @return la division de numero par diviseur. Cette fonction
     * est là pour corriger le "bug" de l'opérateur / de Java dans
     * le cas où numero est négatif
     * division(-1, 2) retourne -1 alors que -1 / 2 vaut 0.
     */
    static public int division(int numero, int diviseur) {
        if((numero >= 0) || ((numero % diviseur) == 0))
        {
            return numero / diviseur;
        }
        else
            return numero / diviseur - 1;
    }
    
    static public int divisionReelleVersEntier(double numero, double diviseur) {
        return division((int) numero, (int) diviseur);
    }
    

    /**
     *
     * @param numero
     * @param diviseur
     * @return numero % diviseur mais corrige le "bug" de la fonction Java
     * dans le cas où numero est négatif
     */
    static public int modulo(int numero, int diviseur) {
        // le modulo normal mathématique où -1 mod 7 = 6 !!! :)
        if((numero >= 0) || ((numero % diviseur) == 0))
            return numero % diviseur;
        else
            return numero % diviseur + diviseur;
    }


    /**
     *
     * @param d
     * @return le carré de d, i.e. d*d
     */
    static double sqr(double d) {
        return d * d;
    }


    /**
     *
     * @param x
     * @param a
     * @param b
     * @return vrai is x est dans le segment [a, b]
     */
    static public boolean is1in23(double x, double a, double b) {
        return (a <= x) && (x <= b);
    }

    public static boolean isPuissanceDeDeux(int nombre) {
        if(Math.abs(nombre) == 1)
        {
            return true;
        }
        else
        if(Math.abs(nombre) == 0)
        {
            return false;
        }
        else
        if(modulo(nombre, 2) == 1)
        {
            return false;
        }
        else
        {
            return isPuissanceDeDeux(nombre / 2);
        }
    }

}
