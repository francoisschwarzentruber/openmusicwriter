/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing.palette;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import musicwriter.donnees.Selection;

/**
 *
 * @author Ancmin
 */
public class SelectionTransfertHandler extends TransferHandler {

    final private Selection selection;
    
    
    public SelectionTransfertHandler(Selection selection) {
        this.selection = selection;
    }

      /**
       * Crée un objet transférable adapté, donc un PersonneTransferable.
       * Reçoit comme paramètre la source du drag and drop
       * (ce qui permet de partager des PersonneTransferHandler)
       */

        @Override
      protected Transferable createTransferable(JComponent c) {
          //  ElementsMusicauxPanel panel = (ElementsMusicauxPanel) c;
          //  Selection selection = panel.getSelection();
            return new SelectionTransferable(selection);
      }

      /**
       * Permet de dire si le drag est possible. Dans notre cas, il l'est toujours.
       * cette méthode peut renvoyer COPY, NONE, MOVE, COPY_OR_MOVE.
       * Si NONE est renvoyé, le drag and drop sera impossible.
       */
        @Override
      public int getSourceActions(JComponent c) {
            return COPY;
      }


            /**
     * Teste si les données proposée comportent une PersonneFlavor.
     * On pourrait aussi accepter des données de mode texte, par exemple.
     */
        @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
            return true;
//        return (c instanceof PartitionPanel);
    }

    /**
     * L'importation de données proprement dite.
     *
     * @param c : la cible du transfert.
     * @param t : données à transférer
     */

        @Override
    public boolean importData(JComponent c, Transferable t) {

        return true;
    }

    }

