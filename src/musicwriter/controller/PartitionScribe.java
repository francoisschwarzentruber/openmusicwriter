/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;


import java.util.HashSet;
import java.util.Set;
import musicwriter.donnees.*;

/**
 * Cette objet écoute une machine d'entrée (synthé MIDI, entrée clavier etc.)
 * et écrit des notes dans la partition. Il utilise un partitionLecteur qui lit
 * la partition et se synchronise sur lui.
 * @author proprietaire
 */
public class PartitionScribe implements MachineEntreeListener {
    
    private final Controller controller;
    private final Set<Note> notesQuiDurent = new HashSet<Note>();
    
    private long tempsEnMilliSecondesAvant = -10000;
    private final PartitionLecteur partitionLecteur;
    
    
    public PartitionScribe(Controller controller, PartitionLecteur partitionLecteur)
    {
        this.controller = controller;
        this.partitionLecteur = partitionLecteur;
    }
    
    private PartitionDonnees getPartitionDonnees()
    {
        return controller.getPartitionDonnees();
    }
    
    private Moment getMoment(long tempsEnMilliSecondes)
    {
         return (new Duree(tempsEnMilliSecondes,
                           getPartitionDonnees().getNbNoiresParMinute(getPartitionDonnees().getMomentDebut()))).getFinMoment(getPartitionDonnees().getMomentDebut());
    }
    


    private Moment getMoment()
    {
        Moment momentRecu = partitionLecteur.momentActuelGet();
        return new Duree(200, getPartitionDonnees().getNbNoiresParMinute(momentRecu)).getDebutMoment(momentRecu);
    }

    @Override
     public void whenNoteEnfoncee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes) {
       //     System.out.println("traiterNoteEnfoncee appelée : " + hauteur.toString() + "," +
       //                         " velocité : " + String.valueOf(velocite));

            

            if(Math.abs(tempsEnMilliSecondesAvant - tempsEnMilliSecondes) < 50)
                tempsEnMilliSecondes = tempsEnMilliSecondesAvant;
            
            if(getNoteQuiPerdure(hauteur) != null)
                return;

            final Portee porteeDansLaQuelleJEcris;
            if(hauteur.isPlusGrave(new Hauteur(0, Hauteur.NoteNom.DO, Hauteur.Alteration.NATUREL)))
            {
                porteeDansLaQuelleJEcris = getPartitionDonnees().getPortee(1);
            }
            else
            {
                 porteeDansLaQuelleJEcris = getPartitionDonnees().getPortee(0);
            }
            
            Note note = new Note(getMoment(),
                                                   new Duree(0, 1), 
                                                   hauteur,
                                                   porteeDansLaQuelleJEcris);
                    
            notesQuiDurent.add(note);
            getPartitionDonnees().elementMusicalAjouter(note);

            controller.calculer(note.getDebutMoment());
            controller.update();
        
           tempsEnMilliSecondesAvant = tempsEnMilliSecondes;
        }

    
    private Note getNoteQuiPerdure(Hauteur hauteur)
    {
          for(Note n : notesQuiDurent)
          {
              if(n.getHauteur().isEgale(hauteur))
              {
                  return n;
              }
          }
          
          return null;
    }
    
    
    
    
    @Override
    public void whenNoteRelachee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes) {
        Moment momentactuel = getMoment();

        Note noteAArreter = getNoteQuiPerdure(hauteur);
        notesQuiDurent.remove(noteAArreter);
       // System.out.println("traiterNoteRelachee appelée : " + hauteur.toString());
        if(noteAArreter == null)
        {
            System.out.println("Erreur dans traiterNoteRelachee : je ne trouve pas la note que tu veux arrêter");
        }
        else
        {
            noteAArreter.setFinMoment(momentactuel);
            noteAArreter.setDureeVague();
            controller.calculer(noteAArreter.getDebutMoment());

            
        }
        
        
    }
    
    
    
    
}
