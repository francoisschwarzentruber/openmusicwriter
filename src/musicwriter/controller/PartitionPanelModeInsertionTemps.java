/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import musicwriter.donnees.Duree;
import musicwriter.donnees.Moment;
import musicwriter.donnees.Rational;
import musicwriter.donnees.actions.PartitionActionInsererTempsOuSupprimerTemps;
import musicwriter.gui.Graphics;
import musicwriter.gui.partitionaffichage.Systeme;

/**
 *
 * @author Ancmin
 */
public class PartitionPanelModeInsertionTemps extends PartitionPanelMode {

    private final Moment momentDebut;
    private Duree dureeAInserer = Duree.getDureeNulle();


    private Systeme getSysteme()
    {
        return getPartitionVue().getSysteme(momentDebut);
    }


    private int getXMoment()
    {
        return (int) (getSysteme().getXNotes(momentDebut) - getSysteme().ligneSupplementaireLongueurGet());
    }

    

    public PartitionPanelModeInsertionTemps(Controller controller, Moment momentDebut)
    {
        super(controller);
        this.momentDebut = momentDebut;
    }

    private void valider()
    {
        getHistoire().executer(new PartitionActionInsererTempsOuSupprimerTemps(getPartitionDonnees(), momentDebut, dureeAInserer.getFinMoment(momentDebut)));
        getPartitionVue().miseEnPageCalculer(momentDebut);
        getController().repaint();
        modeChanger(new PartitionPanelModeEcriture(getController()));
    }
    
    
    @Override
    public void mouseClicked(ControllerMouseEvent evt) {
        valider();
    }

    @Override
    public void mousePressed(ControllerMouseEvent evt) {

    }

    @Override
    public void mouseReleased(ControllerMouseEvent evt) {
        mouseClicked(evt);
    }

    @Override
    public void mouseDragged(ControllerMouseEvent evt) {
        mouseMoved(evt);
    }

    @Override
    public void mouseMoved(ControllerMouseEvent evt) {
         if(evt.getX() <=  getXMoment())
         {
             //dureeAInserer = Duree.getDureeNulle();
             dureeAInserer = new Duree(momentDebut, getSysteme().getMoment(evt.getPoint()));
         }
         else
         {
             dureeAInserer = new Duree(new Rational(((int) (evt.getX() -  getXMoment())) / (int) Systeme.getNbpixelDureeNoireParDefaut(), 1));
         }
         getController().repaint();
    }

    @Override
    public void keyPressed(ControllerKeyEvent evt) {
        if(evt.getKeyCode()== KeyEvent.VK_LEFT)
        {
            dureeAInserer = dureeAInserer.moins(Duree.getDureeNoire());
            getController().repaint();
        }
        else if(evt.getKeyCode()== KeyEvent.VK_RIGHT)
        {
            dureeAInserer = dureeAInserer.plus(Duree.getDureeNoire());
            getController().repaint();
        }
        else if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            quitter();
        }
        else if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            valider();
        }
            
            
    }

    @Override
    public void keyReleased(ControllerKeyEvent evt) {

    }

    @Override
    public void keyTyped(ControllerKeyEvent evt) {
        if(evt.getKeyCode() == 0)
            modeChanger(new PartitionPanelModeEcriture(getController()));
    }


    private int getYHaut()
    {
        return (int) getSysteme().getYToutEnHaut();
    }

    private int getYMilieu()
    {
        return (int) (getYHaut() + getSystemeHeight()/2);
    }
    
    private int getSystemeHeight()
    {
        return (int) (getSysteme().getYToutEnBas() - getSysteme().getYToutEnHaut());
    }



    private int getLargeurTrou()
    {
        return (int) (dureeAInserer.getRational().getRealNumber() * Systeme.getNbpixelDureeNoireParDefaut());
    }

    @Override
    public void paintComponent(Graphics g) {
        /**
         * on décalle la partie droite du système (celle qu'on déplace)
         */

        if(dureeAInserer.isSuperieur(Duree.getDureeNulle()))
        {
        
            int x = getXMoment();
            int largeurTrou = getLargeurTrou();
            int y = (int) getYHaut();

/**
 * TODO: modifier 1000 en taille de l'écran ou un truc comme ça
 */
            g.copyArea(x, y,
                       (int) (1000 - x),
                        getSystemeHeight(),
                        largeurTrou, 0);


            g.setColor(Color.WHITE);
            /**
             * on efface la fin du système (on le fait plus)
             */
           // g.fillRect((int) getSysteme().getXFin(), getYHaut(),  500, getSystemeHeight());

            /**
             * on efface la partie où il y a le trou
             */
            g.fillRect(getXMoment(), getYHaut(),  getLargeurTrou(), getSystemeHeight());

            /**
             * mais on affiche quand même les lignes du système (ça apparait vide
             * au niveau du trou où il y aura juste les lignes)
             */
            g.setColor(Color.BLACK);
            getSysteme().afficherPorteesLignes(g);


            /**
             * puis on affiche les barres morcelés au début et à la fin du trou
             */
            g.setPenWidth(4.0f);
            g.setColor(new Color(0.0f, 0.6f, 0.0f));
            getSysteme().afficherBarreCompleteMorcele(g, getXMoment());
            getSysteme().afficherBarreCompleteMorcele(g, getXMoment() + getLargeurTrou());

            /**
             * enfin, on affiche un rectangle blanc transparent au niveau du trou
             * pour montrer que c'est "nouveau", "transparent"
             */
            g.setAlpha(0.5f);
            g.setColor(Color.WHITE);
            g.fillRect(getXMoment(), getYHaut(),  getLargeurTrou(), getSystemeHeight());
            
            g.setColor(Color.BLACK);
            g.rotate(1.5, x+getLargeurTrou()/2, getYMilieu());
            g.drawString("zone à ajouter", x+getLargeurTrou()/2  - g.getFontStringWidth("zone à ajouter")/2, getYMilieu());
        }
        else
            //on supprime une zone
        {
            int x2 = getXMoment();
            int x1 = (int) getSysteme().getXNotes(dureeAInserer.getFinMoment(momentDebut));
            g.setPenWidth(4.0f);
            g.setColor(new Color(0.6f, 0.0f, 0.0f));
            getSysteme().afficherBarreCompleteMorcele(g, x2);
            getSysteme().afficherBarreCompleteMorcele(g, x1);
            
            g.setAlpha(0.5f );
            g.setColor(Color.WHITE);
            g.fillRect(x1, getYHaut(),  x2-x1, getSystemeHeight());
            
            g.setColor(Color.BLACK);
           // g.drawString("zone à supprimer", (x1+x2)/2 - g.getFontStringWidth("zone à supprimer"), getYMilieu());
            g.rotate(1.5, (x1+x2)/2, getYMilieu());
            g.drawString("zone à supprimer", (x1+x2)/2  - g.getFontStringWidth("zone à supprimer")/2, getYMilieu());
        }


    }

    @Override
    public void paintComponentAvant(Graphics g) {
        
    }

    private void quitter() {
        modeChanger(new PartitionPanelModeEcriture(getController()));
    }

}
