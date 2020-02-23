/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.ArrayList;
import java.util.Collection;
import org.jdom.*;


/**
 * représente une partie dans une partition. Une partie... j'entends une partie
 * piano ou une partie violon etc.
 * @author François Schwarzentruber
 */
public class Partie extends ArrayList<Portee> {

    private Instrument instrument;
    private int numero = 0;
    private Intervalle transposition = Intervalle.getIntervalleNul();
    private String nom;




    public Collection<Portee> getPortees()
    {
        return this;
    }

    /**
     *
     * @return l'intervalle nul quand l'instrument de cette partie n'est pas transposée.
     * Ca retourne l'intervalle "tierce mineure" quand l'instrument est en mib (et pas en do).
     */
    public Intervalle getTransposition() {
        return transposition;
    }


    public Tonalite getTonaliteTransposee(Tonalite tonalite)
    {
        Hauteur hauteurNoteTonaliteSiDO
                = getTransposition().getHauteur1(new Hauteur(0, Hauteur.NoteNom.DO,
                                                            Hauteur.Alteration.NATUREL
                ));

        Tonalite tonaliteSiDO = new Tonalite(hauteurNoteTonaliteSiDO);
        return new Tonalite(tonalite.getDiesesNombre() + tonaliteSiDO.getDiesesNombre() );
    }



    
    public void setTransposition(Intervalle transposition) {
        this.transposition = transposition;
    }

    
    /**
     * crée une partie d'un certain instrument
     * Ce constructeur ajoute automatiquement les portées correspondantes
     * @param instrument 
     */
    public Partie(Instrument instrument)
    {
        super();
        this.instrument = instrument;
        if(instrument.getPorteesNombreStandard() == 2)
        {
            add(new Portee(this));
            add(new Portee(this));
            get(0).setNumero(1);
            get(1).setNumero(2);
        }
        else
        {
            add(new Portee(this));
            get(0).setNumero(1);
        }
        this.nom = null;
    }



    

    /**
     * change l'instrument de la partie. setInstrument(... "flûte"...) dit
     * que la partie courante a un timbre de flûte. Cette fonction ne change rien aux clefs et
     * à la géométrie des portées présentes dans la partition.
     * 
     * Si le nom de la partie n'a jamais été changé, alors cet appel modifie 
     * de manière implicite le nom de la partie (qui est égale au nom de l'instrument)
     * @param instrument
     */
    public void setInstrument(Instrument instrument)
    {
        this.instrument = instrument;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * Par défaut, une partie est de numéro 0. Mais quand on rajoute une partie
     * à une partitionDonnee, alors cette partie reçoit un numéro.
     * Les numéros vont entre 0 et... le nombre de parties dans la partitionDonnee.
     * @param numero 
     */
    void setNumero(int numero)
    {
        this.numero = numero;
    }



    

    public Element sauvegarderMusicXML()
    {
        Element racine = new Element("score-part");
        racine.setAttribute("id", "P" + numero);

        Element scoreInstrument = new Element("score-instrument");
        scoreInstrument.setAttribute("id", "P" + numero + "I" + numero);

        racine.addContent(scoreInstrument);

        Element midiInstrument = new Element("midi-instrument");
        midiInstrument.setAttribute("id", "P" + numero + "I" + numero);
        Element midiChannel = new Element("midi-channel");
        midiChannel.setText(String.valueOf(numero));
        midiInstrument.addContent(midiChannel);

        Element midiProgram = new Element("midi-program");
        midiProgram.setText(String.valueOf(getInstrument().getNumeroMIDI()));
        midiInstrument.addContent(midiProgram);

        racine.addContent(midiInstrument);

        return racine;


    }



    public int getPorteesNombre()
    {
        return size();
    }

    public int getNumero() {
        return numero;
    }

/**
 *
 * @param numeroPortee
 * @return retourne la portée n° numeroPortee. numeroPortee est compris entre 1 et le nombre de portée.
 *
 * La première portée est n° 1.
 */
    public Portee getPortee(int numeroPortee)
    {
        return get(numeroPortee-1);
    }

    public Portee getPorteePremiere() {
        return get(0);
    }


    public Portee getPorteeDerniere() {
        return get(size() - 1);
    }

    public void setNom(String string) {
        nom = string;
    }

    /**
     * 
     * @return le nom de la partie.
     * PS : Si le nom n'a jamais été affecté avec setNom alors cette fonction
     * retourne le nom de l'instrument.
     */
    public String getNom() {
        if(nom == null)
            return getInstrument().getNom();
        else
            return nom;
    }


    



}
