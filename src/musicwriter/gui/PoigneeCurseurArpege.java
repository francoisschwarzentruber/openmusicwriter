/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.gui;

import java.awt.Cursor;
import musicwriter.gui.partitionaffichage.AffichageElementMusicalArpege;
import musicwriter.gui.partitionaffichage.Systeme;

/**
 *
 * @author Ancmin
 */
public class PoigneeCurseurArpege extends PoigneeCurseur {
    private final AffichageElementMusicalArpege affichageArpege;


    public PoigneeCurseurArpege(Systeme systeme,
            AffichageElementMusicalArpege affichageArpege,
            int numeroCurseur)
        {
            super(systeme, affichageArpege.getElementMusicalArpege(), numeroCurseur);
            this.affichageArpege = affichageArpege;
        }


    
    @Override
    protected int getX() {
        return (int) affichageArpege.getXMiddle();
    }

    @Override
    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
    }





}
