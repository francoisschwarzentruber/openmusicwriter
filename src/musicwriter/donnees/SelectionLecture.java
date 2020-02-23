/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

/**
 *
 * @author Ancmin
 */
public interface SelectionLecture {
    public Curseur getCurseurDebut();
    public Iterable<ElementMusicalSurPortee> getElementsMusicauxSurPortee();
    public Iterable<Note> getNotes();
    public Selection getSelectionMemeHauteur(Hauteur hauteur);
    public int getElementsMusicauxNombre();
}
