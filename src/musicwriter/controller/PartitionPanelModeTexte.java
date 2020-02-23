/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import java.awt.event.KeyEvent;
import musicwriter.donnees.*;
import musicwriter.donnees.actions.PartitionActionElementMusicalAjouter;
import musicwriter.donnees.actions.PartitionActionElementMusicalSupprimer;
import musicwriter.donnees.actions.PartitionActionElementTexteChanger;
import musicwriter.gui.Graphics;
import musicwriter.gui.partitionaffichage.AffichageElementMusicalTexte;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeTexte extends PartitionPanelMode {

    

    final private ElementMusicalTexte elTexte;
    private String currentText;


    public PartitionPanelModeTexte(final Controller controller, final ElementMusicalTexte elTexte) {
        super(controller);
        
        final AffichageElementMusicalTexte a = (AffichageElementMusicalTexte) getPartitionVue().getAffichageElementMusical(elTexte);
        currentText = elTexte.getTexte();
        
        controller.getPanel().textEditingAsk(a.getRectangle(), elTexte.getTexte(), a.getFont(), new PanelTextEditingListener() {

            @Override
            public void whenEditing(ControllerKeyEvent e, String newText, int cursorPosition) {
                currentText = newText;
                
                if(e.getKeyCode() == (KeyEvent.VK_ENTER))
                {
                    enregistrerQuitterModeParole(newText);
                }
                else
                if(e.getKeyCode() == (KeyEvent.VK_ESCAPE))
                {
                    annulerQuitterModeParole();
                }
                if(isSyllabeParole())
                if((e.getKeyCode() == KeyEvent.VK_SPACE) ||
                        ((e.getKeyCode() == KeyEvent.VK_RIGHT) &&
                         (cursorPosition >= newText.length()-1)))
                {
                    Moment momentSuivant = getPartitionDonnees().getMomentSuivant(elTexte.getDebutMoment(), elTexte.getPortee());

                    if(momentSuivant != null)
                    {
                            enregistrerModeParole(newText, getElementMusicalParoleSyllabe(getController(), momentSuivant, elTexte.getPortee()));

                    }

                }

            }

  
        });
                
        this.elTexte = elTexte;
        

      
        

        
    }


    private boolean isSyllabeParole() {
         return elTexte instanceof ElementMusicalParoleSyllabe;
    }

    
    @Override
    public void mouseClicked(ControllerMouseEvent evt) {

    }

    @Override
    public void mousePressed(ControllerMouseEvent evt) {

    }

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        Curseur curseur = getController().getCurseur(evt.getPoint());

        if(curseur.isSurElementMusical())
        {
            if(curseur.getElementMusical() instanceof ElementMusicalTexte)
            {
                modeParole((ElementMusicalTexte) curseur.getElementMusical());
            }
            else
                enregistrerQuitterModeParole(currentText);
        }
        else
            enregistrerQuitterModeParole(currentText);

    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {

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

    }

    @Override
    public void paintComponentAvant(Graphics g) {

    }

    private void modeParole(ElementMusicalTexte elementMusical) {
     //   getController().removeAll();
        modeChanger(new PartitionPanelModeTexte(getController(), elementMusical));
    }

    private void enregistrerQuitterModeParole(String newText) {
        sauvegarderModification(newText);
        quitterModeParole();
    }

    private void enregistrerModeParole(String newText, ElementMusicalTexte elementMusical) {
        sauvegarderModification(newText);
        modeParole(elementMusical);
    }

    private void quitterModeParole()
    {
        getController().getPanel().textEditingStop();
        modeChanger(new PartitionPanelModeEcriture(getController()));
    }



    private void sauvegarderModification(String newText) {
        if(newText.isEmpty())
        {
            getHistoire().executer(new PartitionActionElementMusicalSupprimer(elTexte));
        }
        else
        {
            getHistoire().executer(new PartitionActionElementTexteChanger(elTexte, newText));
        }
        getController().calculer(elTexte.getDebutMoment());
        
    }


    private void annulerQuitterModeParole() {
        quitterModeParole();
    }


    private static ElementMusicalParoleSyllabe getElementMusicalParoleSyllabe(Controller controller, Moment moment, Portee portee)
    {
        ElementMusicalParoleSyllabe elSuivant
                = controller.getPartitionDonnees().getElementMusicalParoleSyllabe(moment, portee);

        if(elSuivant != null)
        {
            return elSuivant;
        }
        else
        {
            ElementMusicalParoleSyllabe elSuivantCree =  new ElementMusicalParoleSyllabe(moment, portee, "");
            controller.getHistoire().executer(new PartitionActionElementMusicalAjouter(elSuivantCree));
            controller.calculerModificationSelection();
            return elSuivantCree;
        }
    }

    private static PartitionPanelModeTexte getParoleSyllabeEditionMode(Controller controller, Moment moment, Portee portee) {
            return new PartitionPanelModeTexte(controller, getElementMusicalParoleSyllabe(controller, moment, portee));

        
    }

    static public PartitionPanelModeTexte getParoleSyllabeEditionMode(Controller controller, Curseur curseur)
    {
        return getParoleSyllabeEditionMode(controller, curseur.getMoment(), curseur.getPortee());
        
    }
}
