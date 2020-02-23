/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;



/**
 * Un moment représente un "moment" dans une partition, c'est à dire une position dans le temps.
 * Par exemple, après le moment
 * "après avoir joué en temps 10 noires + une croche".
 * @author proprietaire
 */
public class Moment implements Comparable {
    private final Rational r;
    private boolean precede = false;

    
    /**
     * 
     * @return vrai ssi le moment précède sa valeur rationnelle (clefs, etc.)
     */
    public boolean isPrecede() {
        return precede;
    }

    
    public Rational getRational()
    {
        return r;
    }


    public double getRealNumber()
    {
        return r.getRealNumber();
    }

    
    public Moment(Rational r)
    {
        this.r = r;
        this.precede = false;
    }


    /**
     * dit que le moment précède les notes etc.
     */
    public void setPrecede()
    {
       // this.precede = true;
    }


    public boolean isApres(Moment autreMoment) {
        return isEgal(autreMoment) || isStrictementApres(autreMoment);
    }
    
    
    
    public boolean isAvant(Moment autreMoment) {
        return isEgal(autreMoment) || isStrictementAvant(autreMoment);
    }

    
    public boolean isEgal(Moment autreMoment) {
        return ( this.r.isEgal(autreMoment.getRational()) )
                && (this.isPrecede() == autreMoment.isPrecede());
    }
    
    
    public boolean isStrictementAvant(Moment autreMoment) {
        return ( this.r.isStrictementInferieur(autreMoment.getRational() ) ) ||
                (this.r.isEgal(autreMoment.getRational()) && this.isPrecede() && !autreMoment.isPrecede());
    }


    public boolean isStrictementApres(Moment autreMoment) {
         return ( this.r.isStrictementSuperieur(autreMoment.getRational() ) ) ||
                (this.r.isEgal(autreMoment.getRational()) && !this.isPrecede() && autreMoment.isPrecede()) ;
    }

    
    

    public int compareTo(Object o) {
        Moment m = (Moment) o;
        
        if(this.isStrictementAvant(m))
        {
            return -1;
        }
        else if(isEgal(m))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
    
    
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Moment))
            return false;
        else
            return isEgal((Moment) o);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.r != null ? this.r.hashCode() : 0);
        hash = 41 * hash + (this.precede ? 1 : 0);
        return hash;
    }


    
    @Override
    public String toString()
    {
        if(isPrecede())
            return r.toString() + "-";
        else
            return r.toString();
    }

    public Duree getDureeDepuisDebut() {
        return new Duree(new Moment(new Rational(0, 1)), this);
    }

    public void setBienDessus() {
        precede = false;
    }



    public static Moment min(Moment m1, Moment m2)
    {
        if(m1.isAvant(m2))
            return m1;
        else
            return m2;
    }



    public static Moment max(Moment m1, Moment m2)
    {
        if(m2.isAvant(m1))
            return m1;
        else
            return m2;
    }


}
