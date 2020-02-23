/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing;

import java.util.ArrayList;

/**
 *
 * @author Ancmin
 */
public class PanelNoteArticulations extends ToolBarDefinedWithStrings {
    /**
     * <!ELEMENT articulations
	((accent | strong-accent | staccato | tenuto |
	  detached-legato | staccatissimo | spiccato |
	  scoop | plop | doit | falloff | breath-mark |
	  caesura | stress | unstress | other-articulation)*)>


     */
    private static ArrayList<String> getArticulationsList()
    {
        ArrayList<String> L = new ArrayList<String>();
        L.add("accent");
        L.add("strong-accent");
        L.add("staccato");
        L.add("tenuto");
        L.add("detached-legato");
        L.add("staccatissimo");
        L.add("spiccato");
        L.add("scoop");
        L.add("plop");
        L.add("doit");
        L.add("breath-mark");
        L.add("caesura");
        L.add("stress");
        L.add("unstress");
        //L.add("other-ornament");
        return L;
    }

    public PanelNoteArticulations() {
        super(getArticulationsList());
    }







}
