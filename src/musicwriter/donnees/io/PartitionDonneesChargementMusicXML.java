/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import musicwriter.Erreur;
import musicwriter.donnees.BarreDeMesure;
import musicwriter.donnees.Duree;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalChangementMesureSignature;
import musicwriter.donnees.ElementMusicalChangementTonalite;
import musicwriter.donnees.ElementMusicalClef;
import musicwriter.donnees.ElementMusicalNuance;
import musicwriter.donnees.ElementMusicalParoleSyllabe;
import musicwriter.donnees.ElementMusicalTempo;
import musicwriter.donnees.Instrument;
import musicwriter.donnees.Intervalle;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Note;
import musicwriter.donnees.Octaviation;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Portee;
import musicwriter.donnees.Rational;
import musicwriter.donnees.Silence;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author proprietaire
 */
public class PartitionDonneesChargementMusicXML implements PartitionDonneesChargement {

    static private int divisions = 4800;

    /**
     * 
     * @param partitionDonnees
     * @param momentDebutMesure
     * @param partie
     * @param elementMesure
     * @return le moment final de la mesure (i.e. le moment du début de la mesure suivante
     * ou alors le moment final de la partition s'il n'y a pas de mesure suivante)
     *
     * Cette fonction enregistre dans partitionDonnees tous les éléments musicaux et
     * autres indications présents dans elementMesure (qui vient d'un fichier MusicXML)
     * momentDebutMesure = le moment de début de la mesure qu'on traite
     * partie = partie (~ instrument) qu'on traite
     */
    private static Moment traiterElementMusicXMLMesure(PartitionDonnees partitionDonnees,
            Moment momentDebutMesure,
            Partie partie,
            Element elementMesure) {
        final List<Element> enfants = elementMesure.getChildren();

        Note ancienneNote = null;
        Moment moment = momentDebutMesure;
        for(final Element element : enfants) {
            if (element.getName().equals("forward")) {
                moment = (new Duree(divisions, element.getChild("duration"))).getFinMoment(moment);
            } else if (element.getName().equals("backup")) {
                moment = (new Duree(divisions, element.getChild("duration"))).getDebutMoment(moment);
            } else if (element.getName().equals("lyric")) {
                /**
                 * ceci n'est pas conforme au MusicXML mais est permis par Musicwriter
                 * il s'agit de paroles sans notes
                 */
                partitionDonnees.elementMusicalAjouter(new ElementMusicalParoleSyllabe(moment,
                                partie.getPorteePremiere(), element.getChild("text").getText()));
             
            } else if (element.getName().equals("note")) {
                if(element.getChild("rest") != null) {
                    Silence silence = new Silence(moment, partie, divisions, element);
                    partitionDonnees.elementMusicalAjouter(silence);
                    moment = silence.getFinMoment();
                }
                else
                {
                    if ((element.getChild("chord") != null)) {
                        if (ancienneNote != null) {
                            moment = ancienneNote.getDuree().getDebutMoment(moment);
                        }
                    }

                    Note note = new Note(moment, partie, divisions, element);

                    if(element.getChild("lyric") != null)
                    {
                        partitionDonnees.elementMusicalAjouter(new ElementMusicalParoleSyllabe(note.getDebutMoment(),
                                note.getPortee(), element.getChild("lyric").getChild("text").getText()));
                        
                    }

                    if ((element.getChild("grace") != null)) {
                        note.setDuree(new Duree(new Rational(1, 64)));
                    }
                    partitionDonnees.elementMusicalAjouter(note);

                    moment = note.getFinMoment();
                    ancienneNote = note;

                }
            }
            else if (element.getName().equals(("barline"))) {
                traiterBarreDeMesureMusicXML(partitionDonnees, moment, element);
            } else if (element.getName().equals(("attributes"))) {
                List<Element> enfantsAttributs = element.getChildren();
                for (Element elementAttribut : enfantsAttributs) {
                    if (elementAttribut.getName().equals("divisions")) {
                        divisions = (int) Double.parseDouble(elementAttribut.getText());
                    } else if (elementAttribut.getName().equals("transpose")) {
                        if (elementAttribut.getChild("diatonic") != null & elementAttribut.getChild("chromatic") != null) {
                            partie.setTransposition(new Intervalle(
                                    Integer.parseInt(elementAttribut.getChild("diatonic").getValue()),
                                    Integer.parseInt(elementAttribut.getChild("chromatic").getValue())));
                        }
                    } else {
                        ElementMusical elementMusical = getElementMusicalFromElementFromAttributesMusicXML(moment, partie, elementAttribut);

                        if (elementMusical != null) {
                            partitionDonnees.elementMusicalAjouter(elementMusical);
                        }
                    }

                }



            } else {
                ElementMusical elementMusical = getElementMusicalFromElementMusicXML(moment, partie, element);
                if (elementMusical != null) {
                    partitionDonnees.elementMusicalAjouter(elementMusical);
                }
            }

        }

        return moment;
    }

    /**
     * 
     * @param document
     * @return la partition représentée par le fichier MusicXML mis dans document
     */
    private static PartitionDonnees getPartitionDonneesDuDocumentMusicXML(Document document) {
        final PartitionDonnees partitionDonnees = new PartitionDonnees();

        final Element racine = document.getRootElement();

        traiterPartiesStructureMusicXML(partitionDonnees, racine.getChild("part-list"));

        int i = 0;
        for (Object elementPartieO : racine.getChildren("part")) {
            traiterPartieContenuMusicXML(partitionDonnees, partitionDonnees.getPartie(i), (Element) elementPartieO);
            i++;
        }

        return partitionDonnees;

    }

    
    
    
    private static SAXBuilder getSAXBuilder()
    {
        SAXBuilder sxb = new SAXBuilder(false);

        //ça c'est un objet qui résout les liens vers les fichiers écrits dans
        //un fichier XML. (à ce que j'ai compris)
        //avec cet objet on peut rediriger la lecture de fichier
        //vers d'autres fichiers.
        //ici on va rediriger la lecture du fichier DTD d'internet
        //vers un InputStream vide !
        EntityResolver entityResolver = new EntityResolver() {

            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

                //quand on tombe sur la DTD référée avec du http etc.
                // on dit en fait qu'on lit dans un InputStream vide !
                if (systemId.startsWith("http://")) {
                    return new InputSource(new InputStream() {

                        @Override
                        public int read() throws IOException {
                            return -1;
                        }
                    });
                }
                return null;

            }
        };

        sxb.setEntityResolver(entityResolver);

        // malheureusement, setValidation(false) fait que la DTD n'est pas utilisée...
        // mais elle est malheureusement quand même parsée. (on vit dans un monde bizarre)

        sxb.setValidation(false);
        
        return sxb;
    }
    
    
    
    /**
     * 
     * @param nomFichier
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private static Document getDocumentDuFichierMusicXML(String nomFichier) throws JDOMException, IOException {
        SAXBuilder sxb = getSAXBuilder();
        return sxb.build(new File(nomFichier));

    }
    
    

    public static Document getDocumentFromInputStream(InputStream in) throws JDOMException, IOException {
        SAXBuilder sxb = getSAXBuilder();
        return sxb.build(in);

    }

    public PartitionDonnees getPartitionDonneesDuFichier(String nomFichier) throws IOException {
        try
        {
            return getPartitionDonneesDuDocumentMusicXML(getDocumentDuFichierMusicXML(nomFichier));
        }   
        catch(JDOMException e)
        {
            
          //  throws new IOException();
            return null;
        }
    }
    
    
    
    static public PartitionDonnees getPartitionDonneesFromInputStream(InputStream in) throws IOException {
        try
        {
            return getPartitionDonneesDuDocumentMusicXML(getDocumentFromInputStream(in));
        }   
        catch(JDOMException e)
        {
            
          //  throws new IOException();
            return null;
        }
    }

    private static void traiterPartiesStructureMusicXML(PartitionDonnees partitionDonnees, Element racinePartList) {
        for (Object partieElementO : racinePartList.getChildren()) {
            Element partieElement = (Element) partieElementO;
            int numeroMIDI = 0;
            try {
                numeroMIDI = Integer.parseInt(partieElement.getChild("midi-instrument").getChild("midi-program").getText());
            } catch (Exception e) {
                System.out.println("Erreur XML : pas pu lire l'instrument MIDI");
            }
            Partie partie = new Partie(new Instrument(numeroMIDI));

            partitionDonnees.partieAjouter(partie);
          //  partitionDonnees.partieClefsInstaller(partie);
        }
    }

    private static void traiterPartieContenuMusicXML(PartitionDonnees partitionDonnees, Partie partie, Element elementPart) {
        List<Element> elementMesures = elementPart.getChildren("measure");

        divisions = 4800;
        Moment moment = partitionDonnees.getMomentDebut();
        for (Element elementMesure : elementMesures) {
            if (!partitionDonnees.isVide()) {
                partitionDonnees.elementMusicalAjouter(new BarreDeMesure(moment));
            }
            moment = traiterElementMusicXMLMesure(partitionDonnees, moment, partie, elementMesure);
        }
    }

    private static ElementMusical getElementMusicalFromElementMusicXML(Moment moment, Partie partie, Element element) {
        if (element.getName().equals("direction")) {
            if (element.getChild("sound") != null) {
                if (element.getChild("sound").getAttribute("tempo") != null) {
                    return new ElementMusicalTempo(moment, element);
                }
            }

            /**
             * <direction placement="below">
            <direction-type>
            <dynamics>
            <f/>
            </dynamics>
            </direction-type>
            <staff>1</staff>
            </direction>
             */
            if (element.getChild("direction-type") != null) {
                if (element.getChild("direction-type").getChild("dynamics") != null) {
                    return new ElementMusicalNuance(moment, partie, element);
                }
            }

            /**
             *  <direction placement="above">
            <direction-type>
            <octave-shift type="down" size="8"/>
            </direction-type>
            <staff>1</staff>
            </direction>
             * @return
             */
            if (element.getChild("direction-type") != null) {
                if (element.getChild("direction-type").getChild("octave-shift") != null) {
                    return new Octaviation(moment, partie, element);
                }
            }

        }



        Erreur.message("getElementMusicalFromElement: je n'arrive pas à lire l'élément" + element.toString());
        return null;





    }

    private static ElementMusical getElementMusicalFromElementFromAttributesMusicXML(Moment moment, Partie partie, Element elementAttribut) {
        if (elementAttribut.getName().equals("key")) {
            return new ElementMusicalChangementTonalite(moment,
                    elementAttribut);
        }

        if (elementAttribut.getName().equals("clef")) {
            return new ElementMusicalClef(moment, partie,
                    elementAttribut);
        }




        if (elementAttribut.getName().equals("time")) {
            return new ElementMusicalChangementMesureSignature(moment,
                    elementAttribut);
        }



        Erreur.message("getElementMusicalFromElementFromAttributes: je n'arrive pas à lire l'élément" + elementAttribut.toString());
        return null;
    }

    private static void traiterBarreDeMesureMusicXML(PartitionDonnees partitionDonnees, Moment moment, Element element) {
        if (partitionDonnees.isBarreDeMesure(moment)) {
            ((BarreDeMesure) partitionDonnees.getElementMusical(moment, BarreDeMesure.class)).setBarreDeMesureType(element);


        } else {
            partitionDonnees.elementMusicalAjouter(new BarreDeMesure(moment, element));






        }
    }

    /**
     *
     * @param partie
     * @param element
     * @return la portée de la partie qui correspond avec le numéro indiqué dans element
     * element doit contenir un fils qui s'appelle "staff"
     */
    public static Portee getPorteeFromElementMusicXML(Partie partie, Element element) {
        int porteeNumero;
        if (element.getChild("staff") != null) {
            porteeNumero = Integer.parseInt(element.getChild("staff").getText());
        } else {
            porteeNumero = 1;
        }

        return partie.getPortee(porteeNumero);
    }
}
