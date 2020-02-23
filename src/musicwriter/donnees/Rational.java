/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import musicwriter.Erreur;

/**
 *
 * @author proprietaire
 */

public class Rational {
    private final long numerateur;
    private final long denominateur;
    
    
    
    public Rational(long numerateur, long denominateur)
    {
        if(denominateur == 0)
         {
             Erreur.message("Création d'un nombre rational avec un dénominateur nul.");
             denominateur = 1;
         }

         
         long d = pgcd(numerateur, denominateur);
         this.numerateur = numerateur / d;
         this.denominateur = denominateur / d;

    }

    public boolean isZero() {
        return numerateur == 0;
    }

    

    public Rational moins(Rational rational) {
        return new Rational(numerateur * rational.getDenominateur()
                    - rational.getNumerateur() * denominateur,
                    denominateur * rational.getDenominateur());
    }


    public Rational plus(Rational rational) {
        return new Rational(numerateur * rational.getDenominateur()
                    + rational.getNumerateur() * denominateur,
                    denominateur * rational.getDenominateur());
    }

    
    public Rational getRationalDiviserParDeux()
    {
        return new Rational(getNumerateur(), 2*getDenominateur());
    }


    
    private long pgcd(long a, long b)
    {
        long q = a / b;
        long r = a % b;
        
        if(r == 0)
        {
            return b;
        }
        else
        {
            return pgcd(b, r);
        }

    }
    
    
    

    
    

    public long getDenominateur() {
        return denominateur;
    }

    public long getNumerateur() {
        return numerateur;
    }


    public double getRealNumber()
    {
        return ((float) numerateur) / ((float) denominateur);
    }

    public boolean isEgal(Rational rational) {
        return (numerateur == rational.getNumerateur()) 
                && (denominateur == rational.getDenominateur());
    }
    
    public boolean isInferieur(Rational rational) {
         return (getRealNumber() <= rational.getRealNumber());
    }
    
    
    public boolean isStrictementInferieur(Rational rational) {
        return (getRealNumber() < rational.getRealNumber());
    }

    public boolean isSuperieur(Rational rational) {
        return (getRealNumber() >= rational.getRealNumber());
    }
    
    public boolean isStrictementSuperieur(Rational rational) {
        return (getRealNumber() > rational.getRealNumber());
    }
    
    
    @Override
    public String toString()
    {
        return numerateur + "/" + denominateur;
    }
    
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Rational))
        {
            return false;
        }
        else
        {
            return isEgal((Rational) o);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.numerateur ^ (this.numerateur >>> 32));
        hash = 83 * hash + (int) (this.denominateur ^ (this.denominateur >>> 32));
        return hash;
    }

    public Rational multiplier(Rational rational) {
        return new Rational(numerateur * rational.getNumerateur(), denominateur * rational.getDenominateur());
        
    }

    boolean isPositive() {
        return numerateur * denominateur >= 0;
    }
    
}

