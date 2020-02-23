/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees.actions;

import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.Note;
import musicwriter.donnees.Selection;
import musicwriter.donnees.Silence;

/**
 *
 * @author Ancmin
 */
public class PartitionActionSelectionNoteSilenceSwitcher
        extends PartitionActionActionComposee
{


    private final Selection selectionFutur;



    public PartitionActionSelectionNoteSilenceSwitcher(Selection selection)
    {
        selectionFutur = new Selection();
        for(final ElementMusical element : selection.getElementsMusicaux())
        {
            if(element instanceof Note)
            {
                final Note note = (Note) element;
                final Silence silence = new Silence(note.getDebutMoment(), note.getDuree(), note.getPortee(),
                                    note.getHauteur(), note.getVoix());

                actionAjouter(new PartitionActionElementMusicalRemplacer(note,
                        silence));
                        
                selectionFutur.elementMusicalAjouter(silence);
                
            }
            else
            if(element instanceof Silence)
            {
                final Silence silence = (Silence) element;
                final Note note = new Note(silence.getDebutMoment(), silence.getDuree(),
                                    silence.getHauteur(), silence.getPortee(), silence.getVoix());

                actionAjouter(new PartitionActionElementMusicalRemplacer(silence,
                        note));

                selectionFutur.elementMusicalAjouter(note);


            }
            else
                selectionFutur.elementMusicalAjouter(element);


        }
    }

    public Selection getSelectionFutur() {
        return selectionFutur;
    }



    

}
