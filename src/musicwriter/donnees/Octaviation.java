/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import musicwriter.donnees.io.PartitionDonneesChargementMusicXML;
import org.jdom.Element;

/**
 *
 * @author Ancmin
 */
public class Octaviation extends ElementMusicalSurPortee {

    private final int nbOctave;
    private static final Bijection<Integer, String> bijectionTypeNbOctave = getBijectionTypeNbOctave();






    public Octaviation(Moment moment, Portee portee, int nbOctave)
    {
        super(moment, portee);
        this.nbOctave = nbOctave;
    }



    private static int getNbOctave(Element element)
    {
        return bijectionTypeNbOctave.getAntecedant(
                element.getChild("direction-type")
                       .getChild("octave-shift")
                       .getAttributeValue("type"));
    }


    public Octaviation(Moment moment, Partie partie, Element element)
    {
        this(moment,
              PartitionDonneesChargementMusicXML.getPorteeFromElementMusicXML(partie, element),
              getNbOctave(element));
    }


    /**
     *  <direction placement="above">
        <direction-type>
          <octave-shift type="down" size="8"/>
          </direction-type>
        <staff>1</staff>
        </direction>
     * @return an XML element that represents an octaviation
     */
    @Override
    public Element sauvegarder() {
        Element elementDirection = new Element("direction");
        Element elementDirectionType = new Element("direction-type");
        Element elementDirectionTypeOctaveShift = new Element("octave-shift");
        elementDirectionTypeOctaveShift.setAttribute("type", bijectionTypeNbOctave.getImage(nbOctave));
        elementDirection.addContent(elementDirectionType);
        elementDirectionType.addContent(elementDirectionTypeOctaveShift);
        elementDirectionType.addContent(getPortee().sauvegarderNumero());
        return elementDirection;
    }


    /**
     *
     * @return la bijection "nombre d'octave de différence" avec le nom MusicXML du changement d'octave
     */
    static private Bijection<Integer, String> getBijectionTypeNbOctave() {
        Bijection<Integer, String> B = new Bijection<Integer, String>();
        B.put(0, "stop");
        B.put(-1, "down");
        B.put(1, "up");
        return B;
        
    }


    /**
     * 
     * @return le nombre d'octave entre écriture et son.
     * Par exemple, si on écrit des choses dans l'aigu, on a fréquemment
     * getNbOctavesEntreEcritureEtSon() retourne 1.
     */
    public int getNbOctavesEntreEcritureEtSon() {
        return nbOctave;
    }

    public int getCoordonneeVerticaleReelle(int coordonneeVerticaleSiPasOctaviation) {
        return coordonneeVerticaleSiPasOctaviation + 7*getNbOctavesEntreEcritureEtSon();
    }

    public Hauteur getHauteurReelle(Hauteur hauteur) {
        return Intervalle.getIntervalleOctave(-getNbOctavesEntreEcritureEtSon()).getHauteur2(hauteur);
    }










}
