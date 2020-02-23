/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

import java.awt.Cursor;
import musicwriter.controller.Controller;
import musicwriter.donnees.Curseur;
import musicwriter.donnees.ElementMusical;
import musicwriter.donnees.ElementMusicalMultiCurseurs;
import musicwriter.donnees.Histoire;
import musicwriter.donnees.actions.PartitionActionElementMusicalMultiCurseursCurseurModifier;
import musicwriter.gui.partitionaffichage.Systeme;


/**
 * Une poignée pour contrôler un élément musical avec plusieurs curseurs comme :
 * - une courbe ;
 * - une ligne ;
 * - une arpège ;
 * - un crescendo etc.
 * @author Ancmin
 */

public class PoigneeCurseur extends Poignee {
    private final int numeroCurseur;
    private final ElementMusicalMultiCurseurs elementMusical;
    private ElementMusicalMultiCurseurs elementMusicalCopie;

/**
 * crée une poignée qui contrôle le curseur numéro numeroCurseur de l'élement musical
 * elementMusical
 * @param systeme
 * @param elementMusical
 * @param numeroCurseur
 */
    public PoigneeCurseur(Systeme systeme, ElementMusicalMultiCurseurs elementMusical, int numeroCurseur) {
        super(systeme, elementMusical.getCurseur(numeroCurseur));
        this.numeroCurseur = numeroCurseur;
        this.elementMusical = elementMusical;
        this.elementMusicalCopie = elementMusical;
    }


    private ElementMusicalMultiCurseurs getElementMusicalMultiCurseurs() {
        return elementMusical;
    }

    @Override
    public void mouseDrag(Curseur newCurseur, Controller panel)
    {
           elementMusicalCopie = (ElementMusicalMultiCurseurs) elementMusical.clone();
           elementMusicalCopie.setCurseur(numeroCurseur, newCurseur);
           
    }




    @Override
    public void mouseFinish(Curseur newCurseur, Histoire histoire) {
        histoire.executer(
                new PartitionActionElementMusicalMultiCurseursCurseurModifier(
                    getElementMusicalMultiCurseurs(),
                    numeroCurseur, newCurseur));
    }

    @Override
    public ElementMusical getElementMusicalResultat() {
        return elementMusicalCopie;
    }

    @Override
    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    

    

}
