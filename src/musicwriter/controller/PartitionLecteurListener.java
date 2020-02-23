/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

/**
 * Un écouteur qui écoute un partitionLecteur.
 * @author Ancmin
 */
public interface PartitionLecteurListener {
    
    /**
     * when the playing begins
     */
    public void whenBegins();
    
    /**
     * quand la lecture s'arrête (soit par un arrêt de l'utilisateur, soit parce que 
     * la partition est finie)
     */
    public void whenStops();
    
    /**
     * est appelé de temps en temps pour faire du réaffichage
     */
    public void refresh();
    
}
