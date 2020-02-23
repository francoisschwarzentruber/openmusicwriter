/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;
import musicwriter.Erreur;
import musicwriter.donnees.Hauteur.Alteration;
import musicwriter.donnees.Hauteur.NoteNom;
import musicwriter.donnees.Portee.Clef;

/**
 *
 * @author Ancmin
 */
public class Tonalite {
    private final int diesesNombre;



    /**
     * crée un objet Tonalité. diesesNombre représente le nombre de dièse à la clef.
     * Si diesesNombre est négatif, alors -diesesNombre est le nombre de bémol à la clef.
     * Par exemple, si diesesNombre = 2, il y a 2 dièses à la clef.
     * Si diesesNombre = 0, il n'y a rien à la clef (do majeur).
     * Si diesesNombre = -3, il y a 3 bémols à la clef.
     * Si diesesNombre > 7 ou < -7, affiche un message d'erreur.
     * @param diesesNombre
     */
    public Tonalite(int diesesNombre)
    {
        this.diesesNombre = diesesNombre;

        if(Math.abs(diesesNombre) > 7)
        {
            Erreur.message("Tonalite(...) : trop de dièses ou de bémols à la clef");
        }
    }


    /**
     *
     * @param hauteurNoteToniqueMajeur
     * @return retourne le nombre de dièses à la clef (ou de bémol et alors ça retourne
     * un nombre négatif dont l'opposé représente le nombre de bémol)
     * pour la tonalité majeur ou la note tonique est hauteurNoteToniqueMajeur.
     * Par exemple, si hauteurNoteToniqueMajeur = mib, retourne -3.
     * Si hauteurNoteToniqueMajeur = do, retourne 0.
     * Si hauteurNoteToniqueMajeur = ré, retourne 2.
     */
    private static int toniqueMajeurToDiesesNombre(Hauteur hauteurNoteToniqueMajeur)
    {
        int[] T = {0, 2, 4, -1, 1, 3, 5};
        return T[hauteurNoteToniqueMajeur.getNumeroModuloOctave()]
                + hauteurNoteToniqueMajeur.getAlteration().getNumero()*7;
    }

    /**
     *
     * @param diesesNombre
     * @return  le nom de la tonalité en bon français où le nombre de dièses à la clef est
     * diesesNombre.
     */
    public static String getNomTonalite(int diesesNombre)
    {
        switch(diesesNombre)
        {
            case -7: return "dob majeur/lab mineur";
            case -6: return "solb majeur/mib mineur";
            case -5: return "réb majeur/sib mineur";
            case -4: return "lab majeur/fab mineur";
            case -3: return "mib majeur/dob mineur";
            case -2: return "sib majeur/solb mineur";
            case -1: return "fa majeur/réb mineur";
            case 0: return "do majeur/la mineur";
            case 1: return "sol majeur/mi mineur";
            case 2: return "ré majeur/si mineur";
            case 3: return "la majeur/fa# mineur";
            case 4: return "mi majeur/do# mineur";
            case 5: return "si majeur/sol# mineur";
            case 6: return "fa# majeur/ré# mineur";
            case 7: return "do# majeur/la# mineur";
            default: return "";
        }
    }




    public String getNomTonalite()
    {
        return getNomTonalite(diesesNombre);
    }



    /**
     *
     * @param diesesNombre
     * @return  le nom de la tonalité en bon français où le nombre de dièses à la clef est
     * diesesNombre.
     */
    public static Hauteur getToniqueMajeure(int diesesNombre)
    {
        switch(diesesNombre)
        {
            case -7: return new Hauteur(0, NoteNom.DO, Alteration.BEMOL);
            case -6: return new Hauteur(0, NoteNom.SOL, Alteration.BEMOL);
            case -5: return new Hauteur(0, NoteNom.RE, Alteration.BEMOL);
            case -4: return new Hauteur(0, NoteNom.LA, Alteration.BEMOL);
            case -3: return new Hauteur(0, NoteNom.MI, Alteration.BEMOL);
            case -2: return new Hauteur(6, NoteNom.SI, Alteration.BEMOL);
            case -1: return new Hauteur(0, NoteNom.FA,  Alteration.NATUREL);
            case 0: return new Hauteur(0, NoteNom.DO, Alteration.NATUREL);
            case 1: return new Hauteur(0, NoteNom.SOL, Alteration.NATUREL);
            case 2: return new Hauteur(0, NoteNom.RE, Alteration.NATUREL);
            case 3: return new Hauteur(0, NoteNom.LA, Alteration.NATUREL);
            case 4: return new Hauteur(0, NoteNom.MI, Alteration.NATUREL);
            case 5: return new Hauteur(0, NoteNom.SI, Alteration.NATUREL);
            case 6: return new Hauteur(0, NoteNom.FA, Alteration.DIESE);
            case 7: return new Hauteur(0, NoteNom.DO, Alteration.DIESE);
            default: return new Hauteur(0, NoteNom.DO,  Alteration.NATUREL);
        }
    }



    public Hauteur getToniqueMajeure()
    {
        return getToniqueMajeure(diesesNombre);
    }




/**
 * Crée la tonalité où la tonique est hauteurNoteToniqueMajeur si la tonalité était
 * en majeur.
 * @param hauteurNoteToniqueMajeur
 */
    Tonalite(Hauteur hauteurNoteToniqueMajeur) {
        this(toniqueMajeurToDiesesNombre(hauteurNoteToniqueMajeur));
    }

/**
 *
 * @return le nombre de dièse de cette tonalité. Par exemple ré majeur => la fonction retourne 2.
 * Pour mib majeur, la fonction retourne -3. (nombre négatif = nombre de bémol).
 * Pour do majeur, la fonction retourne 0.
 */
    public int getDiesesNombre()
    {
        return diesesNombre;
    }

/**
     * 
     * @return les hauteurs qui sont altérés (sous-routines pour obtenir les hauteurs 
     * devant les clefs)
     */
    private ArrayList<Hauteur> getHauteursAlterees()
    {
        ArrayList<Hauteur> hauteurs = new ArrayList<Hauteur>();

        Hauteur h = new Hauteur(0, Hauteur.NoteNom.FA, Hauteur.Alteration.DIESE);
        for(int i = 1; i <= getDiesesNombre(); i++)
        {
            hauteurs.add(h);
            h = Intervalle.getIntervalleQuinte().getHauteur2(h);
        }


        h = new Hauteur(-1, Hauteur.NoteNom.SI, Hauteur.Alteration.BEMOL);
        for(int i = -1; i >= getDiesesNombre(); i--)
        {
            hauteurs.add(h);
            h = Intervalle.getIntervalleQuarte().getHauteur2(h);
        }

        return hauteurs;
    }


    /**
     *
     * @param clef
     * @return retourne la liste des hauteurs de notes que l'on affiche devant la clef
     * clef.
     */
    public ArrayList<Hauteur> getHauteursAltereesAffichees(Clef clef) {
        ArrayList<Hauteur> hauteurs = getHauteursAlterees();

        if(isAvecDiese())
            for(Hauteur hauteur : hauteurs)
            {
                if(hauteur.getNumeroModuloOctave() <= 4)
                    hauteur.setOctave(1);
                else
                    hauteur.setOctave(0);
            }
        else
        {
            for(Hauteur hauteur : hauteurs)
            {
                if(hauteur.getNumeroModuloOctave() < 3)
                    hauteur.setOctave(1);
                else
                    hauteur.setOctave(0);
            }
        }

        if(clef.equals(Clef.ClefDeFa))
        {
            for(Hauteur hauteur : hauteurs)
            {
                    hauteur.setOctave(hauteur.getOctave()-2);
            }
        }

        return hauteurs;
    }

    /**
     *
     * @param hauteur
     * @return l'altération normale que devrait alors la note hauteur.
     * Par exemple si la tonalité est ré majeur,
     * getAlteration(do bémol) = #
     * getAlteration(do) = #
     * getAlteration(mi) = bécarre
     */
    public Alteration getAlteration(Hauteur hauteur) {
        for(Hauteur h : getHauteursAlterees())
        {
              if(h.getNumeroModuloOctave() == hauteur.getNumeroModuloOctave())
                  return h.getAlteration();
        }
        return Alteration.NATUREL;
    }

    private boolean isAvecDiese() {
        return diesesNombre > 0;
    }


    /**
     *
     * @param numero
     * @return la hauteur dont la note est la note de numéro numero et
     * l'altération est l'altération naturelle que possède cette note dans
     * la tonalité
     */
    public Hauteur getHauteur(int numero) {
        return new Hauteur(numero, getAlteration(new Hauteur(numero, Alteration.NATUREL)));
    }

}
