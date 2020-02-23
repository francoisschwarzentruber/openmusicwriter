package musicwriter.donnees;

import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ancmin
 */
public class ElementMusicalTexte extends ElementMusicalSurPortee {
    private Curseur curseur;
    private String texte;


    

    public ElementMusicalTexte(Curseur curseur, String texte)
    {
        super(curseur.getMoment(), curseur.getPortee());
        this.curseur = curseur;
        this.texte = texte;
    }



    
    @Override
    public Curseur getCurseur()
    {
        return curseur;
    }

    public String getTexte() {
        return texte;
    }

    @Override
    public void setPortee(Portee portee) {
        super.setPortee(portee);
        curseur = new Curseur(getDebutMoment(), curseur.getHauteur(), portee);
    }

    @Override
    public Portee getPortee() {
        return curseur.getPortee();
    }

    @Override
    public Moment getDebutMoment() {
        return curseur.getMoment();
    }



    @Override
    public void setCurseur(Curseur curseur) {
        this.curseur = curseur;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }



    


    public Hauteur getHauteur()
    {
        return curseur.getHauteur();
    }

    public void setHauteur(Hauteur hauteur)
    {
        curseur = new Curseur(getDebutMoment(), hauteur, curseur.getPortee());
    }

    @Override
    public void deplacer(Moment debutMoment) {
        curseur = new Curseur(debutMoment, curseur.getHauteur(), curseur.getPortee());
    }




}
