/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import musicwriter.donnees.Hauteur;

/**
 * Cette interface propose d'implémenter un écouteur de machine d'entrée.
 * Les deux événements sont quand une note est enfoncée et quand une note est relâchée.
 * @author Ancmin
 */
public interface MachineEntreeListener {
    void whenNoteEnfoncee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes);
    void whenNoteRelachee(Hauteur hauteur, int velocite, long tempsEnMilliSecondes);
    
}
