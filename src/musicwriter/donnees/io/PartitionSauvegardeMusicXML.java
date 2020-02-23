/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.donnees.io;

import java.io.FileOutputStream;
import java.util.Set;
import musicwriter.donnees.BarreDeMesure;
import musicwriter.donnees.Duree;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalChangementMesureSignature;
import musicwriter.donnees.ElementMusicalChangementTonalite;
import musicwriter.donnees.ElementMusicalClef;
import musicwriter.donnees.ElementMusicalDuree;
import musicwriter.donnees.ElementMusicalParoleSyllabe;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Note;
import musicwriter.donnees.Partie;
import musicwriter.donnees.PartitionDonnees;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 *
 * @author Ancmin
 */
public class PartitionSauvegardeMusicXML implements PartitionSauvegarde {

    @Override
    public void sauvegarder(PartitionDonnees partitionDonnees, String nomFichier) throws java.io.IOException {
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        Document document = sauvegarderMusicXML(partitionDonnees);
        
        document.setDocType(new DocType("score-partwise",
                "-//Recordare//DTD MusicXML 1.1 Partwise//EN",
                "http://www.musicxml.org/dtds/partwise.dtd"));
        sortie.output(document, new FileOutputStream(nomFichier));
        partitionDonnees.setPasDeModification();
    }
    
    
    
    
    
    
    
    
    
    
    
    static private Element partiesStructureSauvegarderMusicXML(PartitionDonnees partitionDonnees)
    {
        Element racine = new Element("part-list");

        for(Partie partie : partitionDonnees.getParties())
        {
            racine.addContent(partie.sauvegarderMusicXML());
        }
        return racine;
    }




    static public Document sauvegarderMusicXML(PartitionDonnees partitionDonnees)
    {
        Element racine = new Element("score-partwise");
        Document document = new Document(racine);
        
        racine.addContent(partiesStructureSauvegarderMusicXML(partitionDonnees));

        for(Partie partie : partitionDonnees.getParties())
        {
              racine.addContent(partieContenuSauvegarder(partitionDonnees, partie));
        }

        
        return document;
    
    }
    
    
    
    
    
    
    

    
    /**
     * 
     * @return  <barline location="right">
        <bar-style>light-heavy</bar-style>
        <repeat direction="backward"/>
        </barline>
     */
    static Element createRepeatBackwardBarlineElement()
    {
           return createRepeat("right", "light-heavy", "backward");
    }

    /**
     * 
     * @return  <barline location="left">
        <bar-style>heavy-light</bar-style>
        <repeat direction="forward"/>
        </barline>
     */
    static Element createRepeatForwardBarlineElement()
    {
           return createRepeat("left", "heavy-light", "forward");
    }
    
 /**
     * 
     * @return  <barline location="right">
        <bar-style>light-heavy</bar-style>
        <repeat direction="backward"/>
        </barline>
     */
    static Element createRepeat(String location, String barstyle, String direction)
    {
        Element elementBarLine = new Element("barline");
        elementBarLine.setAttribute("location", location);
        
        Element elementBarStyle = new Element("bar-style");
        elementBarStyle.addContent(barstyle);
        
        elementBarLine.addContent(elementBarStyle);
        
        Element elementRepeat = new Element("repeat");
        elementRepeat.setAttribute("direction", direction);
        elementBarLine.addContent(elementRepeat);
       
        return elementBarLine;
    }
    
   
    
    
    static Element partieContenuSauvegarder(PartitionDonnees partitionDonnees, Partie partie)
    {
        MusicXMLExtractLyricsFromPartitionDonnees musicXMLExtractLyricsFromPartitionDonnees = new MusicXMLExtractLyricsFromPartitionDonnees(partitionDonnees);
        Element elementPart = new Element("part");
        elementPart.setAttribute("id", "P" + partie.getNumero());

        Element elementMesure = new Element("measure");
        elementPart.addContent(elementMesure);

        Element elementAttributes = new Element("attributes");
        elementMesure.addContent(elementAttributes);

        Element elementStaves = new Element("staves");
        elementStaves.setText(String.valueOf(partie.getPorteesNombre()));
        elementAttributes.addContent(elementStaves);
        
        elementAttributes.addContent(new Element("divisions").setText(String.valueOf(Duree.divisionsStandard)));

        if(!partie.getTransposition().isNull())
        {
            Element elementTranspose = new Element("transpose");
            Element elementTransposeDiatonic = new Element("diatonic");
            elementTransposeDiatonic.setText(String.valueOf(partie.getTransposition().getNumero()));

            Element elementTransposeChromatic = new Element("chromatic");
            elementTransposeChromatic.setText(String.valueOf(partie.getTransposition().getNbDemiTonsParRapportAuDoCentral()));

            elementTranspose.addContent(elementTransposeDiatonic);
            elementTranspose.addContent(elementTransposeChromatic);
            elementAttributes.addContent(elementTranspose);

        }

        
        
        
        
        for(Moment moment = partitionDonnees.getMomentDebut();
                moment != null;
                moment = partitionDonnees.getMomentSuivantAvecElementsMusicauxQuiDebutent(moment))
        {
            Set<ElementMusical> els = partitionDonnees.getElementsMusicauxPartieQuiCommencent(moment, partie);

            BarreDeMesure barreDeMesure = partitionDonnees.getMesureBarreDeMesureDebut(moment);
            if(barreDeMesure != null)
            {
                if(barreDeMesure.isRepeatBackward())
                {
                     elementMesure.addContent(createRepeatBackwardBarlineElement());
                }
                                
                                
                elementMesure = new Element("measure");
                elementPart.addContent(elementMesure);
                elementAttributes = new Element("attributes");
                elementMesure.addContent(elementAttributes);
                els.remove(barreDeMesure);
                
                if(barreDeMesure.isRepeatForward())
                {
                     elementMesure.addContent(createRepeatForwardBarlineElement());
                }
            }
            
            for(ElementMusical el : els)
            {
                if(el instanceof ElementMusicalChangementTonalite)
                {
                    elementMesure.addContent(new Element("attributes").addContent(el.sauvegarder()));
                }
                else
                if(el instanceof ElementMusicalChangementMesureSignature)
                {
                    elementMesure.addContent(new Element("attributes").addContent(el.sauvegarder()));
                }
                else
                if(el instanceof ElementMusicalChangementMesureSignature)
                {
                    elementMesure.addContent(new Element("attributes").addContent(el.sauvegarder()));
                }
                else
                if(el instanceof ElementMusicalClef)
                {
                    elementMesure.addContent(new Element("attributes").addContent(el.sauvegarder()));
                }
                else
                if(el instanceof ElementMusicalParoleSyllabe)
                {
                    if(musicXMLExtractLyricsFromPartitionDonnees
                            .getElementMusicauxParoleSyllabesOrphelin()
                            .contains((ElementMusicalParoleSyllabe) el))
                         elementMesure.addContent(el.sauvegarder());
                }
                else
                {
                    Element element = el.sauvegarder();

                    if(el instanceof Note)
                    {
                        if(musicXMLExtractLyricsFromPartitionDonnees.getMapNotesParoles().get((Note) el) != null)
                        for(ElementMusicalParoleSyllabe lyric : musicXMLExtractLyricsFromPartitionDonnees.getMapNotesParoles().get((Note) el))
                        {
                            element.addContent(lyric.sauvegarder());
                        }
                    }

                    elementMesure.addContent(element);
                }
                    

                if(el instanceof ElementMusicalDuree)
                       elementMesure.addContent(((ElementMusicalDuree) el).getDuree().getElementBackUp());

            }

            if(partitionDonnees.getMomentSuivantAvecElementsMusicauxQuiDebutent(moment) != null)
            {
                 elementMesure.addContent((new Duree(moment, partitionDonnees.getMomentSuivantAvecElementsMusicauxQuiDebutent(moment))).getElementForward());
            }
        }
        
        
       

        return elementPart;
    }
    









}
