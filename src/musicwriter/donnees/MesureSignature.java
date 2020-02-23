/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;

/**
 *
 * @author Ancmin
 */
public class MesureSignature {

    private final ArrayList<Long> numerateurs = new ArrayList<Long>();
    private final long denominateur;




    public MesureSignature(long numerateur, long denominateur)
    {
         this.numerateurs.add(numerateur);
         this.denominateur = denominateur;
    }

    public MesureSignature(long num1, long num2, long num3, long num4, long denominateur) {
        if(num2 == 0)
        {
            this.numerateurs.add(num1);
        }
        else if(num3 == 0)
        {
            this.numerateurs.add(num1);
            this.numerateurs.add(num2);
        }
        else if(num4 == 0)
        {
            this.numerateurs.add(num1);
            this.numerateurs.add(num2);
            this.numerateurs.add(num3);
        }
        else
        {
            this.numerateurs.add(num1);
            this.numerateurs.add(num2);
            this.numerateurs.add(num3);
            this.numerateurs.add(num4);
        }
        this.denominateur = denominateur;
    }

    MesureSignature(String numerateur, int denominateur) {
        while(numerateur.indexOf("+") >= 0)
        {
            int i = numerateur.indexOf("+");
            numerateurs.add(Long.parseLong(numerateur.substring(0, i)));
            numerateur = numerateur.substring(i+1);
        }
        numerateurs.add(Long.parseLong(numerateur));
        
        this.denominateur = denominateur;
    }



    public long getDenominateur() {
        return denominateur;
    }

    public String getNumerateur() {
        String s = "";
        for(int i = 0; i < numerateurs.size() - 1; i++)
        {
            s += numerateurs.get(i) + "+";
        }
        s += numerateurs.get(numerateurs.size() - 1);
        return s;
    }

    

    public ArrayList<Double> getGroupes()
    {
        ArrayList<Double> G = new ArrayList<Double>();

        for(long n : numerateurs)
        {
            if(denominateur == 2)
                G.add(2.0);
            else if(denominateur == 4)
                G.add(1.0);
            else if(denominateur == 8)
            {
                if(n % 3 == 0)
                    G.add(1.5);
                else if(n % 2 == 0)
                    G.add(1.0);
                else
                    G.add(1.0);
                
            }
            else
                G.add(1.0);
        }

        return G;
    }




    private ArrayList<Double> getDebutDouble() {
        ArrayList<Double> G = new ArrayList<Double>();
        double s = 0;
        for(long i : numerateurs)
        {
            G.add(s);
            s += ((double) i* 4) / ((double) denominateur);
        }

        return G;
        
    }



    private int getNumeroPaquet(double debut)
    {
        ArrayList<Double> D = getDebutDouble();
        for(int i = 0; i < D.size(); i++)
        {
            if(debut < D.get(i))
                return i-1;
        }
        return D.size() - 1;
    }

    
    public double getFinGroupe(double debut)
    {
        final int i = getNumeroPaquet(debut);
        final ArrayList<Double> D = getDebutDouble();
        final ArrayList<Double> G = getGroupes();
        
        final double debutPaquet = D.get(i);
        final double tailleGroupe = G.get(i);

        final double n = Math.floor((debut - debutPaquet) / tailleGroupe);


        return debutPaquet + (n+1)*tailleGroupe;
        
        
        
    }

    
    public double getGroupe() {
        if(numerateurs.get(0) == 6 && denominateur == 8)
            return 1.5;
        else
            return 1;

    }



    /**
     * 
     * @return la durée que dure une mesure avec cette signature
     */
    public Duree getMesuresDuree() {
        int s = 0;
        for(long i : numerateurs)
        {
            s += i;
        }

        return new Duree(new Rational(s * 4, denominateur));
    }


}
