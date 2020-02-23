/*
 * Ce module permet de faire des divisions de nombres entiers
 * et calculer des modulos
 * (car les instructions java pour le faire sont un peu bizarres)
 */



/**
 *
 * @author proprietaire
 */
public class Arithmetique {
    
    
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
    
    
    static public int modulo(int numero, int diviseur) {
        // le modulo normal mathématique où -1 mod 7 = 6 !!! :)
        if((numero >= 0) || ((numero % diviseur) == 0))
            return numero % diviseur;
        else
            return numero % diviseur + diviseur;
    }

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

}
