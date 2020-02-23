/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import musicwriter.donnees.Hauteur.Alteration;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.*;
import musicwriter.gui.Graphics;
import musicwriter.gui.Poignee;
import musicwriter.gui.partitionaffichage.AffichageCrocheReliee;
import musicwriter.gui.partitionaffichage.AffichageElementMusical;
import musicwriter.gui.partitionaffichage.SelectionAffichage;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeSelection extends PartitionPanelModeStyletEsquisse {

    private final Selection selection;
    private final SelectionAffichage selectionAffichage;
    private boolean selectionIsSousCurseur = true;
    static private final Color colorSelection = new Color(0.2f, 0.2f, 1.0f);//new Color(0.8f, 0.0f, 0.2f);
    private final Curseur plageSelectionCurseurReference;
    private final Curseur plageSelectionCurseurDeuxieme;



    static public Color getColorSelection() {
        return colorSelection;
    }

    





    public Selection getSelection() {
        return selection;
    }



    private Curseur plageSelectionCurseurDeuxieme()
    {
        if(plageSelectionCurseurDeuxieme == null)
        {
            if(selection.getCurseurFin().getMoment().isStrictementApres(plageSelectionCurseurReference.getMoment()))
            {
                return selection.getCurseurFin();
            }
            else
                return selection.getCurseurDebut();
        }
        else
            return plageSelectionCurseurDeuxieme;
        
    }
    


    private Set<Poignee> getPoignees() {
        Set<Poignee> S = new HashSet<Poignee>();

        if(selection.isSingleton())
        {
            AffichageElementMusical a = getPartitionVue().getAffichageElementMusical(
            selection.getElementMusicalUnique());
            if(a != null)
                S.addAll(a.getPoignees());
        }

        return S;
    }




    /**
     *
     * @param evt
     * @return la poignée sous le curseur souris proposée par evt
     * Si il n'y a pas de poignées, renvoie null.
     */
    private Poignee getPoignee(ControllerMouseEvent evt) {

        for(Poignee p : getPoignees())
        {
            if(p.isContientPoint(evt.getPoint()))
            {
                return p;
            }
        }
        return null;
    }

    private void dessinerSelection(Graphics g) {
            g.setColor(colorSelection);
            g.setPenWidth(2);

            getPartitionVue().afficherSelection(g, selection);

            for(Poignee p : getPoignees())
            {
                p.draw(g);
            }
    }




    public PartitionPanelModeSelection(Controller controller, Selection selection) {
        this(controller, selection, selection.getCurseurDebut(), null);

    }



    public PartitionPanelModeSelection(Controller controller,
                                       Selection selection,
                                       Curseur plageSelectionCurseurReference,
                                       Curseur plageSelectionCurseurDeuxieme) {
        super(controller);

        this.selection = selection;
        this.selectionAffichage = new SelectionAffichage(getPartitionVue(), selection);
        this.plageSelectionCurseurReference = plageSelectionCurseurReference;
        this.plageSelectionCurseurDeuxieme = plageSelectionCurseurDeuxieme;
        calculer();
        getController().update();
    }
   
    @Override
    public void mouseClicked(ControllerMouseEvent evt) {
        


    }



    @Override
    public void mousePressed(ControllerMouseEvent evt) {
        Curseur curseur = getController().getCurseur(evt.getPoint());

        /**
         * sélection par plage
         */
        if(evt.isShiftDown())
        {
            selectionEtendre(curseur);
            return;
        }

        if(getPoignee(evt) != null)
        {
//            getController().modifierSourisCurseur(getPoignee(evt).getCursor());
            modeChanger(new PartitionPanelModePoignee(getController(), getSelection(), getPoignee(evt)));
            
        }
        else
        if(getPartitionVue().getAffichageCrocheRelie(evt.getPoint()) != null)
        {
            AffichageCrocheReliee a = getPartitionVue().getAffichageCrocheRelie(evt.getPoint());
            Selection nouvelleselection;
            if(!isClavierToucheSpeciale(evt))
                 nouvelleselection = new Selection();
            else
                nouvelleselection = selection;

                nouvelleselection.selectionAjouter(a.getSelection());

                modeChanger(new PartitionPanelModeSelectionDeplacementOuCopie(getController(),
                                                                          curseur,
                                                                          nouvelleselection));
        }
        else
        if(curseur.isSurElementMusical())
        {

            if(!selection.contains(curseur.getElementMusical()))
            {
                Selection nouvelleselection;
                if(!isClavierToucheSpeciale(evt))
                     nouvelleselection = new Selection();
                else
                    nouvelleselection = selection;

                nouvelleselection.elementMusicalAjouter(curseur.getElementMusical());

                modeChanger(new PartitionPanelModeSelectionDeplacementOuCopie(getController(),
                                                                          curseur,
                                                                          nouvelleselection));
            }
            else
                //le curseur est sous l'élément musical
            {
                modeChanger(new PartitionPanelModeSelectionDeplacementOuCopie(getController(),
                                                                          curseur,
                                                                          getSelection()));
            }

            

//            if(isClavierToucheSpeciale(evt))
//                getController().modifierSourisCurseurMainQuiPrendPlus();
//            else
//                getController().modifierSourisCurseurMainQuiPrend();
        }
        else if(selectionAffichage.isPointInSelectionPolygones(evt.getPoint()))
        {
            modeChanger(new PartitionPanelModeSelectionDeplacementOuCopie(getController(),
                                                                          curseur,
                                                                          selection));
//            if(isClavierToucheSpeciale(evt))
//                getController().modifierSourisCurseurMainQuiPrendPlus();
//            else
//                getController().modifierSourisCurseurMainQuiPrend();
        }
        else
        {
            super.mousePressed(evt);
        }

    }








    @Override
    public void mouseReleased(ControllerMouseEvent evt) {

/**
 * copier-coller à la UNIX
 */
        if(evt.isMiddleButton())
        {
            Selection selectionAAjouter = this.selection.clone();
            selectionAAjouter.setDebutMoment(getController().getCurseurSouris().getMoment());
            getHistoire().executer(new PartitionActionSelectionInserer(selectionAAjouter));
            getController().calculer(selectionAAjouter.getMomentDebut());
            selectionChanger(selectionAAjouter);
            
            return;
        }


        
        Selection selectionProchaine = traiterSelectionPolygoneStyloEsquisse(getStyloEsquisse());
//        getController().getFenetrePrincipale().modeSelection(getSelection());
        getController().update();
        
        if(!isClavierToucheSpeciale(evt))
            //pas de touche spéciale comme ctrl, maj
        {
            if(selectionProchaine.isVide())
            {
                if(!selectionAffichage.isPointInSelectionPolygones(evt.getPoint()))
                     modeChanger(new PartitionPanelModeEcriture(getController()));
            }
            else
                selectionChanger(selectionProchaine);
        }
        else
            // on a appuyé sur maj, ctrl etc.
        {
            getSelection().ajouterSelection(selectionProchaine.getElementsMusicaux());
            selectionChanger(getSelection());
        }

        super.mouseReleased(evt);

    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
        super.mouseDragged(evt);
    }

    @Override
    public void mouseMoved(ControllerMouseEvent evt) {
        super.mouseMoved(evt);

        if(getPoignee(evt) != null)
            //on est sous une poignée
        {
//            getController().modifierSourisCurseur(getPoignee(evt).getCursor());
        }
        else
        if(getController().getCurseurSouris().isSurElementMusical() ||
           selectionAffichage.isPointInSelectionPolygones(evt.getPoint()) ||
           getPartitionVue().getAffichageCrocheRelie(evt.getPoint()) != null)
        {
            //when we have a selection under the mouse
            getController().fireCursorOverSomething();
            selectionSousCurseur();
        }
        else
        {
            //pas sur la sélection, sur rien
            getController().fireCursorOverNothing();
            selectionPasSousCurseur();
        }
    }

    @Override
    public void keyPressed(ControllerKeyEvent evt) {
//         if(isClavierToucheSpeciale(evt))
//             getFenetrePrincipale().modeSelectionPlus();
    //     DureeEntreeFractionPanel.tryDureePanel(evt, getController());

         /**
          * on échange les notes et les silences ;) si la touche espace a été
          * appuyé
          */
         if(evt.getKeyChar() == ' ')
         {
             PartitionActionSelectionNoteSilenceSwitcher action = new PartitionActionSelectionNoteSilenceSwitcher(getSelection());
             getHistoire().executer(action);
             getController().calculerModificationSelection();
             selectionChanger(action.getSelectionFutur());
         }


        
        if(evt.getKeyCode() == KeyEvent.VK_LEFT & evt.isControlDown())
         {
             selectionDeplacementTemporelRelatifMesureAvant();

         }
        else if(evt.getKeyCode() == KeyEvent.VK_RIGHT & evt.isControlDown())
         {
             selectionDeplacementTemporelRelatifMesureApres();
         }
        else if(evt.getKeyCode() == KeyEvent.VK_LEFT & evt.isShiftDown())
        {
            selectionEtendreGauche();
        }
        else if(evt.getKeyCode() == KeyEvent.VK_RIGHT & evt.isShiftDown())
        {
            selectionEtendreDroite();
        }
        else if(evt.getKeyCode() == KeyEvent.VK_UP & evt.isShiftDown())
        {
            selectionEtendreHaut();
        }
        else if(evt.getKeyCode() == KeyEvent.VK_DOWN & evt.isShiftDown())
        {
            selectionEtendreBas();
        }
        else if(evt.getKeyCode() == KeyEvent.VK_UP)
         {
             selectionDeplacementRelatif(1);
         }
        else if(evt.getKeyCode() == KeyEvent.VK_DOWN)
         {
             selectionDeplacementRelatif(-1);
         }
        else if(evt.getKeyCode() == KeyEvent.VK_LEFT)
         {
             selectionDeplacementTemporelRelatifAvant();

         }
        else if(evt.getKeyCode() == KeyEvent.VK_RIGHT)
         {
             selectionDeplacementTemporelRelatifApres();
         }

        else if((evt.getKeyCode() == KeyEvent.VK_ESCAPE) || (evt.getKeyCode() == KeyEvent.VK_ENTER))
        {
            modeChanger(new PartitionPanelModeEcriture(getController()));
            getController().setCurseurSouris(selection.getCurseurFin());
        }
        else
        {
            if(DureeSaisie.traiter(getNumber(evt)))
                getController().selectionElementsMusicauxDureeSet(DureeSaisie.getDuree());
        }




    }

    @Override
    public void keyReleased(ControllerKeyEvent evt) {
//         if(!isClavierToucheSpeciale(evt))
//         {
//              getFenetrePrincipale().modeSelectionPasPlus();
//
//         }
    }

    @Override
    public void keyTyped(ControllerKeyEvent evt) {
    }

    @Override
    public void paintComponentAvant(Graphics g) {
        selectionAffichage.drawSelectionFond(g, selectionIsSousCurseur);
    }






    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        dessinerSelection(g);
        
    }

    private void selectionSousCurseur() {
        selectionIsSousCurseur = true;
        getController().repaint();
    }

    private void selectionPasSousCurseur() {
        selectionIsSousCurseur = false;
        getController().repaint();
    }

    private void selectionDeplacementRelatif(int deplacementRelatif) {
        PartitionAction a = new PartitionActionSelectionDeplacerHauteur(
                getSelection(),
                new Intervalle(deplacementRelatif,
                               Alteration.NATUREL));
        getController().calculer(Moment.min(getSelection().getMomentDebut(),
                                                   getSelection().getMomentDebut()));
        getHistoire().executer(a);
        selectionChanger(selection);
    }



    private void selectionDeplacementTemporel(Moment selectionMomentDebut, Moment selectionMomentDebutNouveau) {
      
        getHistoire().executer(new PartitionActionSelectionTempsDeplacer(selection, new Duree(selectionMomentDebut, selectionMomentDebutNouveau)));
        getController().calculer(Moment.min(selectionMomentDebut, selectionMomentDebutNouveau));
        selectionChanger(new Selection(selection.getElementsMusicaux()));

    }

    
    private void selectionDeplacementTemporelRelatifAvant() {
        final Moment moment = selection.getMomentDebut();
       final Moment momentNouveau = getPartitionDonnees().getMomentPrecedent(moment);

       selectionDeplacementTemporel(moment, momentNouveau);

    }



    private void selectionDeplacementTemporelRelatifApres() {
       final Moment moment = selection.getMomentDebut();
       final Moment momentNouveau = getPartitionDonnees().getMomentSuivant(moment);

       selectionDeplacementTemporel(moment, momentNouveau);

    }

    private void selectionDeplacementTemporelRelatifMesureAvant() {
       final Moment moment = selection.getMomentDebut();
       final Moment momentNouveau = getPartitionDonnees().getMesurePrecedenteMomentDebut(moment);

       selectionDeplacementTemporel(moment, momentNouveau);
    }

    private void selectionDeplacementTemporelRelatifMesureApres() {
       final Moment moment = selection.getMomentDebut();
       final Moment momentNouveau = getPartitionDonnees().getMesureSuivanteMomentDebut(moment);

       selectionDeplacementTemporel(moment, momentNouveau);
    }



    private void selectionEtendre(Curseur curseurDeuxieme)
    {
        modeChanger(new PartitionPanelModeSelection(getController(),
                getPartitionDonnees().getSelectionPlage(plageSelectionCurseurReference,
                                                                curseurDeuxieme),
                plageSelectionCurseurReference,
                curseurDeuxieme));

    }

    
    private void selectionEtendreGauche() {
       final Curseur curseurDeuxieme = getPartitionDonnees().getCurseurMomentPrecedent(plageSelectionCurseurDeuxieme());
       selectionEtendre(curseurDeuxieme);
    }



    private void selectionEtendreHaut()
    {
        final Curseur curseurDeuxieme = getPartitionDonnees().getCurseurPorteeUneEnHaut(plageSelectionCurseurDeuxieme());
       selectionEtendre(curseurDeuxieme);
    }


    private void selectionEtendreBas()
    {
        final Curseur curseurDeuxieme = getPartitionDonnees().getCurseurPorteeUneEnBas(plageSelectionCurseurDeuxieme());
       selectionEtendre(curseurDeuxieme);
    }


    private void selectionEtendreDroite() {
       final Curseur curseurDeuxieme = getPartitionDonnees().getCurseurMomentSuivant(plageSelectionCurseurDeuxieme());
       selectionEtendre(curseurDeuxieme);
    }

    private void selectionChanger(Selection newSelection)
    {
        modeChanger(new PartitionPanelModeSelection(getController(), newSelection));
    }

    
    
    private Alteration intToAlteration(int i)
    {
        if(i < 0)
            return Alteration.DIESE;
        else
            return Alteration.BEMOL;
        
    }
    


    
    
    

}



//if(evt.getKeyCode() == KeyEvent.VK_DOWN)//enharmonie
//    {
//        if(getPartitionPanel().isSelection())
//        {
//            histoire.executer(
//                    new PartitionActionSelectionPorteeChanger(
//                          getPartitionDonnees(),
//                          getPartitionPanel().getSelection(),
//                          1));
//            getPartitionPanel().calculerModificationSelection();
//            getPartitionPanel().repaint();
//        }
//    }
//
//
//    if(evt.getKeyCode() == KeyEvent.VK_UP)//enharmonie
//    {
//        if(getPartitionPanel().isSelection())
//        {
//            histoire.executer(
//                    new PartitionActionSelectionPorteeChanger(
//                          getPartitionDonnees(),
//                          getPartitionPanel().getSelection(),
//                          -1));
//            partitionVue.miseEnPageCalculer();
//            getPartitionPanel().repaint();
//        }
//    }