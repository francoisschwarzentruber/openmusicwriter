/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.controller;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import musicwriter.Geometrie;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.PartitionActionSelectionDureeFixeChanger;
import musicwriter.donnees.actions.PartitionActionSelectionDureesEtirer;
import musicwriter.donnees.actions.PartitionActionSelectionVoixSet;
import musicwriter.gui.Graphics;
import musicwriter.gui.Point;
import musicwriter.gui.partitionaffichage.AffichageAccord;
import musicwriter.gui.partitionaffichage.Systeme;

/**
 *
 * @author Ancmin
 */
class PartitionPanelModeStyleTraitCroche extends PartitionPanelModeStyletEsquisse {
    final private AffichageAccord affichageAccordDebut;
    private Point pointDestination;
    
    public PartitionPanelModeStyleTraitCroche(Controller controller, AffichageAccord affichageAccordDebut) {
        super(controller);
        this.affichageAccordDebut = affichageAccordDebut;
        pointDestination = affichageAccordDebut.getHampeFinPoint();
        styloEsquisseInit(pointDestination);
    }

    @Override
    public void mouseClicked(ControllerMouseEvent evt) {
    
    }

    @Override
    public void mousePressed(ControllerMouseEvent evt) {
       super.mousePressed(evt);
    }

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        perform();
         modeChanger(new PartitionPanelModeEcriture(getController()));
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
        super.mouseDragged(evt);
        pointDestination = evt.getPoint();
        getController().repaint();
        
    }

    @Override
    public void mouseMoved(ControllerMouseEvent evt) {

    }

    @Override
    public void keyPressed(ControllerKeyEvent evt) {
    }

    @Override
    public void keyReleased(ControllerKeyEvent evt) {

    }

    @Override
    public void keyTyped(ControllerKeyEvent evt) {

    }

    @Override
    public void paintComponent(Graphics g) {
        getStyloEsquisse().afficherTraitCroche(g);
        
        g.setPenWidth(1.0f);
        g.setColor(Color.RED);
        
        for(AffichageAccord a : getAffichageAccordConcernees())
        {
            a.draw(g);
        }
        
        
        //g.setPenWidth(4.0f));
       // g.setColor(Color.BLACK);
       // g.drawLine(getPointDebut().x, getPointDebut().y, pointDestination.x, pointDestination.y);
    }

    @Override
    public void paintComponentAvant(Graphics g) {
    }

    private Point getPointDebut() {
        return affichageAccordDebut.getHampeFinPoint();
    }
    
    
    
    
    
    
    
    private Set<AffichageAccord> getAffichageAccordConcernees() {
        
        if(getPointDebut().distanceSq(getPointDestination()) < 15)
            return  new HashSet<AffichageAccord>();
        
        
        
        Systeme systeme = getAffichageAccordDebut().getSysteme();
        Set<AffichageAccord> affichageAccordsTous = systeme.getAffichageAccords();
        Set<AffichageAccord> accordsConcernes = new HashSet<AffichageAccord>();
               
        accordsConcernes.add(getAffichageAccordDebut());
               
        int nbAccords = 0;

        for(AffichageAccord a : affichageAccordsTous)
        {
           for(int i = 0; i < getStyloEsquisse().getPoints().size()-1; i++)
           {
               if(Geometrie.isSegmentCoupeSegmentVertical(
                   getStyloEsquisse().getPoints().get(i), getStyloEsquisse().getPoints().get(i + 1),
                   a.getHampeDebutPoint(), a.getHampeFinPoint()))
                   {
                       accordsConcernes.add(a);
                       nbAccords++;
                   }
           }
           
        }
              
        return accordsConcernes;
    }
               
               
    
    
    private Selection getAccordConcernes()
    {
        Set<AffichageAccord> A = getAffichageAccordConcernees();
        Selection selection = new Selection();
        
        for(AffichageAccord a : A)
        {
            selection.elementMusicalAjouter(a.getAccord());
        }
        
        return selection;
    }
    
    
    private boolean isQueDesNoires()
    {
         for(ElementMusical el : getAccordConcernes())
         {
             if(!((ElementMusicalDuree) el).getDuree().equals(Duree.getDureeNoire()))
                 return false;
         }
         
         return true;
    }
    
    
    private void perform()
    {
        final Set<AffichageAccord> A = getAffichageAccordConcernees();
        final Selection selection = getAccordConcernes();
        final AffichageAccord adebut = getAffichageAccordDebut();
        if(A.size() == 3 && isQueDesNoires())
               //on transforme en triolets
               {
                    getHistoire().executer(new PartitionActionSelectionVoixSet(selection, adebut.getVoix()));
                    getHistoire().executer(new PartitionActionSelectionDureeFixeChanger(getPartitionDonnees(), selection, new Duree(new Rational(1, 3))));
               }
               else if(isQueDesNoires())
               {
                    getHistoire().executer(new PartitionActionSelectionVoixSet(selection, adebut.getVoix()));
                    getHistoire().executer(new PartitionActionSelectionDureeFixeChanger(getPartitionDonnees(), selection, new Duree(new Rational(1, 2))));
 
               }
               else
               {
                   getHistoire().executer(new PartitionActionSelectionVoixSet(selection, adebut.getVoix()));
                   getHistoire().executer(new PartitionActionSelectionDureesEtirer(selection, new Rational(1, 2)));
               }
                getController().calculer(adebut.getAccord().getDebutMoment());
        }





    private AffichageAccord getAffichageAccordDebut() {
        return affichageAccordDebut;
    }

    private Point getPointDestination() {
        return pointDestination;
    }


    }

