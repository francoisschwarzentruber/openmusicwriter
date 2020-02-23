/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import musicwriter.donnees.Hauteur;
import musicwriter.donnees.Partie;

/**
 *
 * @author proprietaire
 */
public interface MachineSortie {
   public void traiterNoteEnfoncee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie);
   public void traiterNoteRelachee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes, Partie partie);

   public void demarrer();
   public void arreter();
}
