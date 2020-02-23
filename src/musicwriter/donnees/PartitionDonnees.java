/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author proprietaire
 */
public class PartitionDonnees extends ElementsMusicauxEnsemble implements PartitionDonneesGetter {



//***********************************************************************************
// ACCEDER AUX DONNEES DE MA PARTITION
    //*******************************************************************************

    private final ArrayList<Portee> portees = new ArrayList<Portee>();
    private final ArrayList<Partie> parties = new ArrayList<Partie>();
    // Les moments



    /**
     *
     * @return le premier moment de la partition
     */
    @Override
    public Moment getMomentDebut()
    {
        Moment debutMoment = new Moment(new Rational(0, 1));
        debutMoment.setPrecede();
        return debutMoment;
        
    }


/**
 *
 * @return le nombre de portées. Par exemple, pour une partition typique de piano
 * cette fonction retourne 2.
 */
    public int getPorteesNombre()
    {
        return portees.size();
    }

    public Portee getPortee(int i) {
        return portees.get(i);
    }


    public Portee getPorteePremiere()
    {
        return portees.get(0);
    }

    public Portee getPorteeDerniere()
    {
        return portees.get(portees.size() - 1);
    }

    public Iterable<Portee> getPortees() {
        return portees;
    }


    public Collection<Partie> getParties() {
        return parties;
    }



    /**
     *
     * @param partieIndex
     * @return la partie numéro partieIndex dans {0, ... getPartieNombre() - 1}
     */
    public Partie getPartie(int partieIndex) {
        return parties.get(partieIndex);
    }

    /**
     *
     * @return le nombre de partie. Par exemple, pour une partition typique
     * piano-violon, cette fonction retourne 2.
     */
    public int getPartieNombre() {
        return parties.size();
    }

    

    



    
    









//************************************************************************************
// MODIFICATION DE LA PARTITION
//************************************************************************************

    // modifier la structure de ma partition

/**
 * renumérote les parties
 * récupère les portées à partir des parties
 */
    private void porteesCalculer() {

        for(int i = 0; i <= parties.size() - 1; i++)
        {
            parties.get(i).setNumero(i);
        }

        portees.clear();

        for(Partie partie : parties)
        {
            portees.addAll(partie);
        }

        for(int i = 0; i <= portees.size() - 1; i++)
        {
            portees.get(i).setNumeroAffichage(i);
        }

    }

    public void partieSupprimer(int index) {
        parties.remove(index);
        porteesCalculer();
    }

    public void partieAjouter(Partie partie)
    {
        parties.add(partie);
        porteesCalculer();
    }



    public void partieAjouter(int i, Partie partie)
    {
        parties.add(i, partie);
        porteesCalculer();
    }


    public void partieSupprimer(Partie partie)
    {
        parties.remove(partie);
    }








    public void partieClefsInstaller(Partie partie) {
        if(partie.size() == 2)
        {
            elementMusicalAjouter(new ElementMusicalClef(getMomentDebut(), partie.get(0), Portee.Clef.ClefDeSol));
            elementMusicalAjouter(new ElementMusicalClef(getMomentDebut(), partie.get(1), Portee.Clef.ClefDeFa));
        }
        else
        {
            elementMusicalAjouter(new ElementMusicalClef(getMomentDebut(), partie.get(0), partie.getInstrument().getClefStandard()));
        }

    }

    @Override
    public void elementMusicalSupprimer(ElementMusical element) {
        if(element instanceof ElementMusicalClef)
        {
            ElementMusicalClef elementClef = (ElementMusicalClef) element;
            elementClef.getPortee().clefSupprimer(elementClef);
        }

        if(element instanceof Octaviation)
        {
            Octaviation elementOctaviation = (Octaviation) element;
            elementOctaviation.getPortee().octaviationSupprimer(elementOctaviation);
        }


        super.elementMusicalSupprimer(element);
    }






    @Override
    public void elementMusicalAjouter(ElementMusical element) {
        if(element instanceof ElementMusicalClef)
        {
            Moment moment = element.getDebutMoment();
            if(((ElementMusicalClef) element).getPortee().getElementMusicalClef(moment) != null)
            {
                if(((ElementMusicalClef) element).getPortee().getElementMusicalClef(moment).getDebutMoment().equals(moment))
                    return;
            }


            ElementMusicalClef elementClef = (ElementMusicalClef) element;
            elementClef.getPortee().clefAjouter(elementClef);
        }


        if(element instanceof Octaviation)
        {
            Moment moment = element.getDebutMoment();
            if(((Octaviation) element).getPortee().getOctaviation(moment) != null)
            {
                if(((Octaviation) element).getPortee().getOctaviation(moment).getDebutMoment().equals(moment))
                    return;
            }


         Octaviation octaviation = (Octaviation) element;
         octaviation.getPortee().octaviationAjouter(octaviation);
        }


//interdire de poser une barre de mesure toute simple au début de la partition
        if(element instanceof BarreDeMesure)
        {
            if(((BarreDeMesure) element).getBarreDeMesureType().equals(BarreDeMesure.BarreDeMesureType.NORMALE))
                if(element.getDebutMoment().equals(getMomentDebut()))
                    return;
        }
        
        super.elementMusicalAjouter(element);
    }



    /**
     *
     * @param Partie
     * @return vrai ssi il n'y a pas de notes, pas de silences, pas d'éléments musicaux
     * qui repose sur la partie partie.
     */
    public boolean isPartieVide(Partie Partie) {
        return getSelectionToutPartie(Partie).isVide();
    }

    public Curseur getCurseurPorteeUneEnHaut(Curseur curseur) {
        final int n = curseur.getPortee().getNumeroAffichage();
        final int hautN;
        
        if(n > 0)
            hautN = n-1;
        else
            hautN = 0;
        
        
        return new Curseur(curseur.getMoment(), curseur.getHauteur(), getPortee(hautN));
    }

    public Curseur getCurseurPorteeUneEnBas(Curseur curseur) {
        final int n = curseur.getPortee().getNumeroAffichage();
        final int hautN;

        if(n < getPorteesNombre() - 1)
            hautN = n+1;
        else
            hautN = n;


        return new Curseur(curseur.getMoment(), curseur.getHauteur(), getPortee(hautN));
    }

    @Override
    public boolean isMomentApresFin(Moment moment) {
        return getMomentFin().isAvant(moment);
    }





    static public PartitionDonnees createPartitionDonneesNouvellePourPiano() {
        PartitionDonnees partitionDonneesPourPiano = new PartitionDonnees();
        Partie partiePiano = new Partie(new Instrument(0));
        partitionDonneesPourPiano.partieAjouter(0, partiePiano);
        partitionDonneesPourPiano.partieClefsInstaller(partiePiano);
        partitionDonneesPourPiano.elementMusicalAjouter(new ElementMusicalChangementMesureSignature(partitionDonneesPourPiano.getMomentDebut(), new MesureSignature(4, 4)));
        // partitionDonneesPourPiano.elementMusicalAjouter(new ElementMusicalChangementTonalite(partitionDonneesPourPiano.getMomentDebut(),  new Tonalite(2)));
        partitionDonneesPourPiano.elementMusicalAjouter(new ElementMusicalTempo(partitionDonneesPourPiano.getMomentDebut(), 120, "Allegro"));
        partitionDonneesPourPiano.setPasDeModification();
        return partitionDonneesPourPiano;
    }








}
