/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

/**
 *
 * @author Ancmin
 */
public interface PartitionDonneesGetter extends ElementsMusicauxEnsembleLecture {
    public int getPorteesNombre();
    public Portee getPortee(int i);
    public Iterable<Portee> getPortees();
    public Iterable<Partie> getParties();

    public Portee getPorteePremiere();
    public Portee getPorteeDerniere();
    public int getPartieNombre();
    public Partie getPartie(int partieIndex);

    public boolean isMomentApresFin(Moment barreMoment);
}
