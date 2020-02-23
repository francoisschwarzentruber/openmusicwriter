/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui.partitionaffichage;

import musicwriter.donnees.Duree;
import musicwriter.donnees.MomentElementsMusicauxSurLeFeu;
import musicwriter.donnees.Note;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Rational;
import musicwriter.donnees.Moment;
import java.awt.BasicStroke;
import java.awt.Color;
import musicwriter.gui.Graphics;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import musicwriter.donnees.ComparatorNoteHauteurEtPortee;
import musicwriter.gui.ControlCurve;
import musicwriter.Erreur;
import musicwriter.donnees.PartitionDonneesGetter;
import musicwriter.gui.NatCubic;

/**
 *
 * @author Ancmin
 */
public class AffichageLectureBarre {

    private final PartitionVue partitionVue;
    private Moment moment = null;
    private Color couleur;

    public AffichageLectureBarre(PartitionVue partitionVue) {
        this.partitionVue = partitionVue;
        setLectureBarre();
    }

    public void setMoment(Moment momentActuelGet) {
        this.moment = momentActuelGet;
    }



    public void setLectureBarre()
    {
        couleur = new Color(0, 222, 0);
    }

    public void setEnregistrementLectureBarre()
    {
        couleur = new Color(255, 0, 0);
    }



    public void draw(Graphics g) {
        MomentElementsMusicauxSurLeFeu momentNotesSurLeFeu = getPartitionDonnees().getMomentNotesSurLeFeu(moment);
        momentNotesSurLeFeu.filter(partitionVue.getPartiesAffichees());
        Set<Note> notesQuiPerdurent = momentNotesSurLeFeu.getNotesQuiPerdurent();
        
        if(getPartitionDonnees().getPartieNombre() > 4)
        {
            g.setColor(couleur);
            g.setPenWidth(2.5f);
        
            Systeme systeme = partitionVue.getSysteme(moment);
            systeme.afficherBarreComplete(g, moment);
        }
        else
        {
            courbeDessiner(g, notesQuiPerdurent);
        }
        
        
        notesJoueesSurImprimer(g, notesQuiPerdurent);

        
    }


    


    private PartitionDonneesGetter getPartitionDonnees() {
        return partitionVue.getPartitionDonnees();
    }

    private void notesJoueesSurImprimer(Graphics g, Set<Note> notesQuiPerdurent) {
        for(ElementMusical note : notesQuiPerdurent)
        {
            if(note.isSurParties(partitionVue.getPartiesAffichees()))
                ((AffichageNote) partitionVue.getAffichageElementMusical(note))
                    .dessinerLectureMIDIFleur(g, moment);
        }
    }

    private void courbeDessiner(Graphics g, Set<Note> notesQuiPerdurent) {

        g.setColor(couleur);
        g.setPenWidth(2.5f);

        
        SortedSet<Note> notes = new TreeSet<Note>(new ComparatorNoteHauteurEtPortee());
        notes.addAll(notesQuiPerdurent);
        Systeme systeme = partitionVue.getSysteme(moment);

        if(systeme == null)
        {
            Erreur.message("AffichageLectureBarre : courbeDessiner, pas de système");
            return;
        }

        if(notes.isEmpty())
            systeme.afficherBarreComplete(g, moment);
        else
        {
            ControlCurve p = new NatCubic();

            double yavant = systeme.getYBas()+50;
            for(Note n : notes)
            {
               AffichageNote a = ((AffichageNote) partitionVue.getAffichageElementMusical(n));

               if(a == null)
               {
                   Erreur.message("AffichageLectureBarre : courbeDessiner, pas d'affichage pour une note");
                   return;
               }
               if(yavant == systeme.getYBas()+50)
               if(yavant >  a.getNoteCentreY())
                   p.addPoint((int) systeme.getXNotes(moment), (int) (yavant + a.getNoteCentreY()) / 2 );

               if(n.getDuree().isStrictementInferieur(new Duree(new Rational(2, 1))))
                    p.addPoint( (int) a.getX(), (int)  a.getNoteCentreY());
               else
                    p.addPoint( (int) systeme.getXNotes(moment), (int)  a.getNoteCentreY());
               yavant = a.getNoteCentreY();
            }

            if(yavant > systeme.getYHaut()-50)
                 p.addPoint( (int) systeme.getXNotes(moment),  (int) (systeme.getYHaut()-50 + yavant) / 2);

            p.paint(g);
        }
    }

}
