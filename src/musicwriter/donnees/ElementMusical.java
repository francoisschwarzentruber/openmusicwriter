/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;



import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import musicwriter.Erreur;
import org.jdom.Element;

/**
 * Un élément musical est une classe générique et abstraite. Elle peut se spécifier en
 * note
 * silence
 * clef dans une partition
 * un changement de tempo etc.
 * Essentiellement, cette classe gère la position (moment) dans une partition.
 * @author proprietaire
 */
public abstract class ElementMusical implements Cloneable, Comparable {
    private Moment debutMoment = null;

    public Moment getDebutMoment() {
        return debutMoment;
    }


    
    ElementMusical(Moment departMoment)
    {
        this.debutMoment = departMoment;
        this.debutMoment.setBienDessus();
    }
    

    /*
     *  donne le curseur qui représente la position où est l'élément musical
     */
    public Curseur getCurseur()
    {
        Curseur curseur = new Curseur(debutMoment,
                new Hauteur(0, Hauteur.Alteration.NATUREL), null, null, this);
        //curseur.declarerSurElementMusical(this);
        return curseur;
    }

    
    
    
    
    
    public void deplacer(Moment debutMoment) {
        this.debutMoment = debutMoment;
        this.debutMoment.setBienDessus();
    }

    
    
    
    public Element sauvegarder()
    {
        Erreur.message("Elementmusical : sauvegarder, essaie de sauvegarder un élément musical où la fonction sauvegarder n'a pas été implémenté");
        return new Element("cannot-save");
        

    }
    
    
    
    @Override
    public Object clone()
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ElementMusical.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof ElementMusical)
        {
            return getDebutMoment().compareTo(((ElementMusical) o).getDebutMoment());
        }
        else
             return 1;
    }

    public void setCurseur(Curseur curseur) {
        deplacer(curseur.getMoment());
    }

    public void deplacerRelatif(Duree duree, Intervalle intervalle, PartitionDonnees partitionDonnees, int porteeChangement) {
        setCurseur(getCurseur().getCurseurDeplaceRelatif(duree, intervalle, partitionDonnees, porteeChangement));

    }


    public void deplacerRelatif(Duree duree, Intervalle intervalle) {
        setCurseur(getCurseur().getCurseurDeplaceRelatif(duree, intervalle));

    }


    public void deplacerRelatifCurseur(Curseur curseurRelatif) {
        setCurseur(new Curseur(curseurRelatif.getMoment().getDureeDepuisDebut().getFinMoment(getCurseur().getMoment()),
                               curseurRelatif.getHauteur().getIntervalleDepuisDo0().getHauteur2(getCurseur().getHauteur()),
                               curseurRelatif.getPortee(),
                               curseurRelatif.getVoix()));
                
    }

    public void deplacerRelatif(Duree duree, Intervalle intervalle, Portee portee) {
        setCurseur(getCurseur().getCurseurDeplaceRelatif(duree, intervalle, portee));
    }

    abstract public boolean isSurParties(Collection<Partie> parties);




    

}
