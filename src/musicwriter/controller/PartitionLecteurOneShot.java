/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import musicwriter.donnees.Moment;
import musicwriter.donnees.PartitionDonnees;

/**
 * Lit une partition tout d'un coup "one shot"
 * Utile pour exporter en midi une partitionDonnee
 * Utilisation : on crée puis on fait run
 * @author Ancmin
 */
public class PartitionLecteurOneShot extends PartitionLecteur {

    public PartitionLecteurOneShot(
                     PartitionDonnees partitionALire,
                     Moment momentDebut,
                     MachineSortie machineSortie)
    {
        super(partitionALire, momentDebut, machineSortie);

    }

    
    private long time = 0;
    
    @Override
    protected long getTime() {
        return time;
    }

    @Override
    protected void timeSleep(int nbMilliseconds) {
        time += nbMilliseconds;
    }
    
    
    
    
}
