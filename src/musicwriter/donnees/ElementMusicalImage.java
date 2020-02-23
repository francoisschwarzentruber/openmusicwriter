/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Collection;
import musicwriter.gui.Image;
import musicwriter.gui.ImageLoader;

/**
 *
 * @author Ancmin
 */
public class ElementMusicalImage extends ElementMusical {
    private final Image image;
    private Curseur curseur;

    
    public ElementMusicalImage(Curseur curseur, String nomFichier)
    {
        super(curseur.getMoment());
        this.curseur = curseur;
        image = ImageLoader.getImage(nomFichier);
    }


    public ElementMusicalImage(Curseur curseur, Image image)
    {
        super(curseur.getMoment());
        this.curseur = curseur;
        this.image = image;
    }

    public Image getImage()
    {
        return image;
    }

    @Override
    public Curseur getCurseur() {
        return curseur;
    }

    @Override
    public Moment getDebutMoment() {
        return curseur.getMoment();
    }

    @Override
    public void deplacer(Moment debutMoment) {
        curseur = new Curseur(debutMoment, curseur.getHauteur(), curseur.getPortee());
    }








    public void setHauteur(Hauteur hauteur)
    {
        curseur = new Curseur(getDebutMoment(), hauteur, curseur.getPortee());
    }

    public Hauteur getHauteur()
    {
        return curseur.getHauteur();
    }

    @Override
    public void setCurseur(Curseur curseur) {
        this.curseur = curseur;
    }

    @Override
    public  boolean isSurParties(Collection<Partie> parties) {
        return parties.contains(curseur.getPartie());
    }


    

}
