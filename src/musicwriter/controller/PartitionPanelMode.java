/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.controller;


import musicwriter.donnees.Histoire;
import musicwriter.donnees.PartitionDonnees;
import musicwriter.donnees.Selection;
import musicwriter.gui.Graphics;
import musicwriter.gui.StyloEsquisse;
import musicwriter.gui.partitionaffichage.PartitionVue;

/**
 *
 * @author Ancmin
 */
public abstract class PartitionPanelMode {



    /**
 * @param evt
 * @return true ssi dans l'objet evt (un java.awt.event.MouseEvent ou un
 * java.awt.event.KeyEvent) dit qu'il y a une touche Control ou Maj appuyée.
 */
    protected static boolean isClavierToucheSpeciale(Object evt)
    {
        if(evt instanceof ControllerMouseEvent)
        {
            return ((ControllerMouseEvent) evt).isControlDown() ||
                   ((ControllerMouseEvent) evt).isShiftDown();
        }
        else if(evt instanceof ControllerKeyEvent)
            return ((ControllerKeyEvent) evt).isControlDown() ||
                   ((ControllerKeyEvent) evt).isShiftDown();
        else
            return false;
    }






      /**
     *
     * @param evt
     * @return vrai ssi la touche enfoncée correspond à une note saisie ("a" à "h")
     *
     */
    final protected boolean isClavierToucheNoteSaisie(ControllerKeyEvent evt) {
        return ('a' <= evt.getKeyChar()) && (evt.getKeyChar() <= 'g');
    }


    protected static int getNumber(ControllerKeyEvent evt)
    {
        switch(evt.getKeyChar())
        {
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            default: return 0;
                   
        }
    }   




    private final Controller controller;

    public Controller getController() {
        return controller;
    }

/**TODO : à nettoyer*/
    public void calculer()
    {
    }



    public PartitionVue getPartitionVue()
    {
        return controller.getPartitionVue();
    }


    public Histoire getHistoire()
    {
        return controller.getHistoire();
    }



    public PartitionDonnees getPartitionDonnees()
    {
        return controller.getPartitionDonnees();
    }

    

    public PartitionPanelMode(Controller controller)
    {
        this.controller = controller;
    }


    protected void modeChanger(PartitionPanelMode mode)
    {
        controller.setMode(mode);
    }


    /**
     * TODO: à mettre dans le contrôleur
     * @param styloEsquisse
     * @return la sélection qui est prise par l'esquisse au
     */
    protected Selection traiterSelectionPolygoneStyloEsquisse(StyloEsquisse styloEsquisse)
    {
        if(styloEsquisse == null)
            return new Selection();

        if(styloEsquisse.isPolygon())
        {
            return getPartitionVue()
                    .getSelectionPolygone(styloEsquisse.getPolygon());
            
        }
        else
            return new Selection();
    }


    abstract public void mouseClicked(ControllerMouseEvent evt);
    abstract public void mousePressed(ControllerMouseEvent evt);
    abstract public void mouseReleased(ControllerMouseEvent evt);
    
    abstract public void mouseDragged(ControllerMouseEvent evt);
    abstract public void mouseMoved(ControllerMouseEvent evt);

    abstract public void keyPressed(ControllerKeyEvent evt);
    abstract public void keyReleased(ControllerKeyEvent evt);
    abstract  public void keyTyped(ControllerKeyEvent evt);
    
    abstract public void paintComponent(Graphics g);

    abstract public void paintComponentAvant(Graphics g);

    void whenQuit() {};

}
