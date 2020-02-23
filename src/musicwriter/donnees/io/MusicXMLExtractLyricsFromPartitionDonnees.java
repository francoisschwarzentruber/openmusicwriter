/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.io;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import musicwriter.donnees.ElementMusicalParoleSyllabe;
import musicwriter.donnees.ElementsMusicauxEnsemble;
import musicwriter.donnees.Note;

/**
 * Cette objet sert pour enregistrer dans le format MusicXML.
 *
 * Dans Musicwriter : les paroles sont libres et peuvent être placés n'importe où
 * sans aucune liaison avec une note. Il peut même y avoir une parole
 * alors qu'il n'y a pas de note !
 *
 * Dans MusicXML, les paroles sont enregistrées dans les notes. Si jamais une parole n'est
 * pas sous une note... tant pis ça fait un truc pas conforme.
 *
 *
 * Cette objet prend une partition (ensemble d'élements musicaux) et classe les notes
 * et les syllabes de paroles :
 *
 * - mapNotesParoles contient une correspondance "une note" et "toutes les syllabes sous cette note"
 * - elementMusicauxParoleSyllabesOrphelin contient toutes les syllabes qui n'ont pas de notes à qui être attachées
 * 
 * @author Ancmin
 */

public class MusicXMLExtractLyricsFromPartitionDonnees {

    private final Map<Note, Set<ElementMusicalParoleSyllabe>> mapNotesParoles;
    private final Set<ElementMusicalParoleSyllabe> elementMusicauxParoleSyllabesOrphelin;

    public Map<Note, Set<ElementMusicalParoleSyllabe>> getMapNotesParoles() {
        return mapNotesParoles;
    }

    public Set<ElementMusicalParoleSyllabe> getElementMusicauxParoleSyllabesOrphelin() {
        return elementMusicauxParoleSyllabesOrphelin;
    }





    MusicXMLExtractLyricsFromPartitionDonnees(ElementsMusicauxEnsemble partitionDonnees)
    {
        mapNotesParoles = new HashMap<Note, Set<ElementMusicalParoleSyllabe>>();
        elementMusicauxParoleSyllabesOrphelin = new HashSet<ElementMusicalParoleSyllabe>();
        
        for(ElementMusicalParoleSyllabe el : partitionDonnees.getElementsMusicauxParoleSyllabe())
        {
            Set<Note> setNotes = partitionDonnees.getNotesPorteeQuiCommencent(el.getDebutMoment(), el.getPortee());

            if(setNotes.isEmpty())
                elementMusicauxParoleSyllabesOrphelin.add(el);
            else
            {
                Note note = setNotes.iterator().next();
                if(mapNotesParoles.containsKey(note))
                {
                    mapNotesParoles.get(note).add(el);
                }
                else
                {
                    Set<ElementMusicalParoleSyllabe> Snote = new HashSet<ElementMusicalParoleSyllabe>();
                    Snote.add(el);
                    mapNotesParoles.put(note, Snote);
                }
            }
             
        }

        
    }



}
