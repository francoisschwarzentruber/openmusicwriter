/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

/**
 *
 * @author proprietaire
 */
public class Intervalle extends Hauteur {

    public static Intervalle getIntervalleNul() {
       return new Intervalle(new Hauteur(0, Hauteur.Alteration.NATUREL),
                new Hauteur(0, Hauteur.Alteration.NATUREL));
    }

    public static Intervalle getIntervalleQuinte() {
        return new Intervalle(new Hauteur(0, Hauteur.Alteration.NATUREL),
                new Hauteur(0, NoteNom.SOL, Hauteur.Alteration.NATUREL));
    }

    public static Intervalle getIntervalleQuarte() {
        return new Intervalle(new Hauteur(0, Hauteur.Alteration.NATUREL),
                new Hauteur(0, NoteNom.FA, Hauteur.Alteration.NATUREL));
    }

    public static Intervalle getIntervalleSibMoinsUn() {
        return new Intervalle(new Hauteur(0, Hauteur.Alteration.NATUREL),
                new Hauteur(-1, NoteNom.SI, Hauteur.Alteration.BEMOL));
    }

    public static Intervalle getIntervalleOctave(int nbOctaves) {
        return new Intervalle(Hauteur.getDo0(),
                              new Hauteur(8*nbOctaves, Hauteur.Alteration.NATUREL));
    }


    public static Intervalle getIntervalleOctave()
    {
        return new Intervalle(new Hauteur(0, Hauteur.Alteration.NATUREL),
                new Hauteur(7, Hauteur.Alteration.NATUREL));
    }

    public static Intervalle getIntervalleSecondeMajeure() {
        return new Intervalle(Hauteur.getDo0(),
                              Hauteur.getRe0());
    }
    
    
    public Intervalle(Hauteur hauteur1, Hauteur hauteur2)
//    exemple : hauteur1 = do0, hauteur2 = mi2
//              crée une tierce majeure
    {
        super(hauteur2.getNumero() - hauteur1.getNumero(), Alteration.NATUREL);
        int nbDemiTonEscomptes = hauteur2.getNbDemiTonsParRapportAuDoCentral()
                - hauteur1.getNbDemiTonsParRapportAuDoCentral();
        
        setAlteration(nbDemiTonEscomptes - getNbDemiTonsParRapportAuDoCentral());
        
    }

    public Intervalle(int diatonic, int chromatic) {
        this(new Hauteur(0, Alteration.NATUREL), new Hauteur(diatonic, chromatic));
    }

    public Intervalle(int i, Alteration a) {
        this(new Hauteur(0, Alteration.NATUREL), new Hauteur(i, a));
    }


    
    
    public Hauteur getHauteur2(Hauteur hauteur1)
//    exemple : si hauteur1 = ré
//              et que l'objet courant est une tierce majeure
//              renvoie fa#
    {
        Hauteur hauteur2 = new Hauteur(hauteur1.getNumero() + getNumero(),
                                       Alteration.NATUREL);
        
        int nbDemiTonPasBon = hauteur2.getNbDemiTonsParRapportAuDoCentral()
                                - hauteur1.getNbDemiTonsParRapportAuDoCentral();
        
        hauteur2.setAlteration(getNbDemiTonsParRapportAuDoCentral()
                                       - nbDemiTonPasBon
                               );
        
        return hauteur2;
    }
    



    public Hauteur getHauteurFromDo0()
    {
        return this;
    }
    
    public boolean isNull()
    {
        return (alteration.equals(Hauteur.Alteration.NATUREL)) & (numero == 0);
    }
    
    


    public Hauteur getHauteur1(Hauteur hauteur2) {
       Hauteur hauteur1 = new Hauteur(hauteur2.getNumero() - getNumero(),
                                       Alteration.NATUREL);

        int nbDemiTonPasBon = hauteur2.getNbDemiTonsParRapportAuDoCentral()
                                - hauteur1.getNbDemiTonsParRapportAuDoCentral();

        hauteur2.setAlteration(getNbDemiTonsParRapportAuDoCentral()
                                       + nbDemiTonPasBon
                               );

        return hauteur1;
    }


    
}
