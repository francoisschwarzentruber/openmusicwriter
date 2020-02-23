/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Cette classe représente un élément musical qui possède plusieurs curseurs comme :
 * un phrasé etc.
 * @author Ancmin
 */
public abstract class ElementMusicalMultiCurseurs extends ElementMusical {
private final ArrayList<Curseur> curseurs;

    private static Moment curseursGetMomentDepart( ArrayList<Curseur> curseurs)
    {
        Moment m = curseurs.get(0).getMoment();
        for(Curseur c : curseurs)
        {
            if(c.getMoment().isStrictementAvant(m))
            {
                m = c.getMoment();
            }
        }
        return m;
    }

    @Override
    public boolean isSurParties(Collection<Partie> parties) {
        for(Curseur c : curseurs)
        {
            if(!parties.contains(c.getPartie()))
            {
                return false;
            }
            
        }
        return true;
    }




    


    /**
     *
     * @return le moment de début, ie le plus petit moment des moments présents dans
     * les curseurs
     */
    @Override
    public Moment getDebutMoment() {
        return curseursGetMomentDepart(curseurs);
    }




    
/**
 *
 * @param curseur0
 * @param curseur1
 * @return un tableau composé de curseur0 et curseur1
 */
    private static ArrayList<Curseur> createArrayListCurseur(Curseur curseur0, Curseur curseur1)
    {
        ArrayList<Curseur> curseursArray = new ArrayList<Curseur>();
        curseursArray.add(curseur0);
        curseursArray.add(curseur1);
        return curseursArray;
    }



    private static ArrayList<Curseur> createArrayListCurseur(Curseur curseur0, Curseur curseur1, Curseur curseur2)
    {
        ArrayList<Curseur> curseursArray = new ArrayList<Curseur>();
        curseursArray.add(curseur0);
        curseursArray.add(curseur1);
        curseursArray.add(curseur2);
        return curseursArray;
    }

    public ElementMusicalMultiCurseurs(ArrayList<Curseur> curseurs)
    {
        super(curseursGetMomentDepart(curseurs));
        this.curseurs = curseurs;
    }


    public ElementMusicalMultiCurseurs(Curseur curseur0, Curseur curseur1)
    {
        this(createArrayListCurseur(curseur0, curseur1));
    }



    public ElementMusicalMultiCurseurs(Curseur curseur0,Curseur  curseur1, Curseur curseur2)
    {
        this(createArrayListCurseur(curseur0, curseur1, curseur2));
    }

    public ArrayList<Curseur> getCurseurs()
    {
        return curseurs;
    }


    public Curseur getCurseur(int i)
    {
        return curseurs.get(i);
    }


    public void setCurseur(int i, Curseur curseur)
    {
        curseurs.set(i, curseur);
    }



    @Override
    public void deplacerRelatif(Duree duree, Intervalle intervalle, PartitionDonnees partitionDonnees, int porteeChangement) {
        for(int i = 0; i < getCurseursNombre(); i++)
        {
            setCurseur(i, getCurseur(i).getCurseurDeplaceRelatif(duree, intervalle, partitionDonnees, porteeChangement));
        }

    }


    @Override
    public void deplacerRelatif(Duree duree, Intervalle intervalle) {
        for(int i = 0; i < getCurseursNombre(); i++)
        {
            setCurseur(i, getCurseur(i).getCurseurDeplaceRelatif(duree, intervalle));
        }


    }

    @Override
    public void deplacerRelatif(Duree duree, Intervalle intervalle, Portee portee) {
        for(int i = 0; i < getCurseursNombre(); i++)
        {
            setCurseur(i, getCurseur(i).getCurseurDeplaceRelatif(duree, intervalle, portee));
        }
    }

/**
 *
 * @return le nombre de curseurs "points importants" dans l'élément musical
 */
    public int getCurseursNombre() {
        return curseurs.size();
    }



    protected static ArrayList<Curseur> arrayListCloner(ArrayList<Curseur> curseurs)
    {
        ArrayList<Curseur> C = new ArrayList<Curseur>();

        for(Curseur curseur : curseurs)
        {
            C.add((Curseur) curseur.clone());
        }

        return C;
    }
    

    

}
