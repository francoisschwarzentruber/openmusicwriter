/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.BarreDeMesure;
import musicwriter.donnees.Duree;
import musicwriter.donnees.Moment;
import musicwriter.donnees.PartitionDonnees;

/**
 *
 * @author Ancmin
 */
abstract class PartitionActionMesureAjouter extends PartitionActionActionComposee {

    /**
     * on scinde la partition en moment... pour insrer des mesures là
     * @param partitionDonnees
     * @param moment
     * @param nombre
     */
    public PartitionActionMesureAjouter(final PartitionDonnees partitionDonnees,
                                              final Moment moment,
                                              final int nombre)
    {
        final Moment debut = moment;
        final Duree dureeMesure = partitionDonnees.getMesureSignature(moment).getMesuresDuree();
        final Duree dureeToutesLesMesuresInserees = new Duree(dureeMesure, nombre);
        final Moment fin = dureeToutesLesMesuresInserees.getFinMoment(debut);

        actionAjouter(new PartitionActionInsererTemps(debut, fin));

        for(Moment momenti = debut; momenti.isAvant(fin); momenti = dureeMesure.getFinMoment(momenti))
        {
            actionAjouter(new PartitionActionElementMusicalAjouter(new BarreDeMesure(momenti)));
        }

    }
}
