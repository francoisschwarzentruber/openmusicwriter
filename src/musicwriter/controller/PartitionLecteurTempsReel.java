/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import musicwriter.donnees.Moment;
import musicwriter.donnees.PartitionDonnees;

/**
 * Lit une partition en temps réel.
 * Utilisation : on crée puis on fait start
 * @author Ancmin
 */
public class PartitionLecteurTempsReel extends PartitionLecteur {
    
    public PartitionLecteurTempsReel(
                     PartitionDonnees partitionALire,
                     Moment momentDebut,
                     MachineSortie machineSortie)
    {
        super(partitionALire, momentDebut, machineSortie);

    }        
    
    @Override
    protected long getTime()
    {
        return System.currentTimeMillis();
    }
    
    
    @Override
    protected void timeSleep(int nbMilliseconds)
    {
        try {
                sleep(nbMilliseconds);
            } catch (InterruptedException ex) {
                Logger.getLogger(PartitionLecteur.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
