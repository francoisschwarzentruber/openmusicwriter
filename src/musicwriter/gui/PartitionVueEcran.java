/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.gui.partitionaffichage.PartitionVue;

/**
 *
 * @author Ancmin
 */
public class PartitionVueEcran extends PartitionVue {
    Thread affichageCalcul = null;
    
    
    
    
    
    
    
    public PartitionVueEcran(PartitionDonnees partitionDonnees,
                             Collection<Partie> parties,
                             int systemeLongueur)
    {
        super(partitionDonnees,
              parties,
              partitionDonnees.getMomentDebut(),
              systemeLongueur,
              10,
              -1);
    }
    
    
        /**
     * réalise la mise en page sachant que les modifications ont été effectuées entre
     * le momentDebut momentDebut et momentFinImperatif.
     * Si jamais il y a peu de modifications, la procédure ne recalculera pas
     * ce qui n'est pas nécessaire de miseEnPageCalculer !
     * @param momentDebut
     * @param momentFinImperatif
     */
    @Override
    public void miseEnPageCalculer(final Moment momentDebut, final Moment momentFinImperatif) {
            if(affichageCalcul != null)
            {
                affichageCalcul.interrupt();
            }
        
            affichageCalcul = new Thread(new Runnable()
            {
            @Override
               public void run()
               {
                   // this will be run in a separate thread

                   miseEnPageCalculerSansThread(momentDebut, momentFinImperatif);
               }
            });
            affichageCalcul.start();
            try {
                affichageCalcul.join(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(PartitionVueEcran.class.getName()).log(Level.SEVERE, null, "miseEnPageCalculer avec Thread : " + ex);
            }
        
        
    }

    
    public void miseEnPageCalculerSansThread(final Moment momentDebut, final Moment momentFinImperatif) {
        super.miseEnPageCalculer(momentDebut, momentFinImperatif);
    }
    
    
    
}
