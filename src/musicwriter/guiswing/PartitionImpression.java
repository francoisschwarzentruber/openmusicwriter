/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing;

import musicwriter.gui.partitionaffichage.PartitionVue;
import musicwriter.donnees.PartitionDonnees;
import java.awt.print.*;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import musicwriter.gui.Graphics;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Partie;


/**
 *
 * @author proprietaire
 */
 public class PartitionImpression {

    static private double facteurPrecision = 5;
    
    static class ObjetAImprimer extends JPanel implements Printable {

        /**
         * stocke les pages. Chaque page = une vue
         */
        private final ArrayList<PartitionVue> pages = new ArrayList<PartitionVue>();

        /**
         * crée un objet qui après peut s'imprimer
         * partitionDonnees = les données de la partition
         * pageFormat = les informations sur la page (taille du papier etc.)
         * parties = les parties qu'on imprime (généralement c'est toutes les parties de la partition
         * mais on peut demander de n'imprimer que la partie flûte par exemple)
         * @param partitionDonnees
         * @param pageFormat
         * @param parties
         */
        public ObjetAImprimer(PartitionDonnees partitionDonnees, PageFormat pageFormat, Collection<Partie> parties )
        {

            Moment momentCourant = partitionDonnees.getMomentDebut();


            double  systemeLongueur = (( pageFormat.getImageableWidth()) * facteurPrecision);
            //- 55 c'est du biddouillage... je sais pas comment faire autrement...
            //si - 0 : il y a des notes qui sont bouffées à droite
            //si - 100 : la zone horizontale devient toute petite
            double pageHeight = (( pageFormat.getImageableHeight()) * facteurPrecision);

            double interligne = 4 * facteurPrecision;

            while(momentCourant != null)
            {
                PartitionVue partitionVuePageCourante =
                        new PartitionVue(partitionDonnees,
                                         parties,
                                         momentCourant,
                                         systemeLongueur,
                                         interligne,
                                         pageHeight);
                momentCourant = partitionVuePageCourante.getMomentApresLaFinDeLaPage();
                pages.add(partitionVuePageCourante);
            }






        }
        
        


      public int print( java.awt.Graphics graphic, PageFormat pageFormat, int numeroPage)
      //return NO_SUCH_PAGE ou PAGE_EXISTS
      {
          Graphics g = new GraphicsSwing(graphic);
          g.setPenWidth((float) facteurPrecision /2.0f);
          g.translate(pageFormat.getImageableX(),pageFormat.getImageableY());
          g.scale(1.0f / facteurPrecision, 1.0f /facteurPrecision);
        //  g.translate(120 * facteurPrecision, 0);
          
       
          
          if(numeroPage >= pages.size())
              return NO_SUCH_PAGE;
          
          pages.get(numeroPage).afficherPartition(g);
          
          return PAGE_EXISTS;

      }

    }

    
    
    
    static public void boiteDialogueImprimerPuisImprimer(PartitionDonnees partitionDonnees, Collection<Partie> parties)
    {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        PageFormat fmtP = printJob.defaultPage();
        //PageFormat pageFormat = printJob.pageDialog(fmtP);
        //printJob.defaultPage(fmtP);

        PageFormat pageFormat = printJob.defaultPage();
//        PageFormat pageFormat = new PageFormat();
//        Paper paper = new Paper();
//        pageFormat.setPaper(paper);
//        paper.setImageableArea(0D, 0D, Double.MAX_VALUE, Double.MAX_VALUE);
//        pageFormat = printJob.validatePage(pageFormat);
        printJob.setPrintable( new ObjetAImprimer(partitionDonnees, pageFormat, parties), pageFormat);
        //printJob.defaultPage(pageFormat);

        if (printJob.printDialog())
        { // le dialogue d’impression

              try
              {
                    printJob.print();
              }
              catch (PrinterException exception) {
                    JOptionPane.showMessageDialog(null, exception);

              }

        }
    }
    
}
