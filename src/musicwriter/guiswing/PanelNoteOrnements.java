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
public class PanelNoteOrnements extends ToolBarDefinedWithStrings {

        /**
     * <!ELEMENT ornaments
	(((trill-mark | turn | delayed-turn | inverted-turn |
	   shake | wavy-line | mordent | inverted-mordent |
	   schleifer | tremolo | other-ornament),
	   accidental-mark*)*)>

     */
    private static ArrayList<String> getOrnamentsList()
    {
        ArrayList<String> L = new ArrayList<String>();
        L.add("trill-mark");
        L.add("turn");
        L.add("delayed-turn");
        L.add("inverted-turn");
        L.add("shake");
        L.add("wavy-line");
        L.add("mordent");
        L.add("inverted-mordent");
        L.add("schleifer");
        L.add("tremolo");
        //L.add("other-ornament");
        return L;
    }

    public PanelNoteOrnements() {
        super(getOrnamentsList());
    }






}
