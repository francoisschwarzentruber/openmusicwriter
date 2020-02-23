/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

/**
 *
 * @author Ancmin
 */
public interface MachineEntree {

    public void arreter();
    public void demarrer();
    public void addMachineEntreeListener(MachineEntreeListener m);
    public void deleteMachineEntreeListener(MachineEntreeListener m);
}
