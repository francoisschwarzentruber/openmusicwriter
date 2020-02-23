/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.util.Comparator;

/**
 * Cette classe permet de comparer deux notes. Les notes sont classés de la manière suivante :
 * note1 < note2 ssi
 *  la portée de la note1 a un numéro strictement plus petit que la portée de la note2
 *  ou alors la portée de la note1 = la portée de la note2 et la hauteur de la note1 < la hauteur de la note2
 *
 * @author Ancmin
 */
public class ComparatorNoteHauteurEtPortee implements Comparator<Note> {

        public int compare(Note note1, Note note2) {
            if(note2 == note1)
                return 0;
            else if(note2.getPortee().getPartie().getNumero() < note1.getPortee().getPartie().getNumero())
            {
                return -1;
            }
            else if(note2.getPartie().getNumero() > note1.getPartie().getNumero())
            {
                return 1;
            }
            else if(note2.getPortee().getNumero() < note1.getPortee().getNumero())
                return -1;
            else if((note2.getPortee().getNumero() == note1.getPortee().getNumero())
                     && note1.getHauteur().isStrictementPlusGrave(note2.getHauteur()))
                return -1;
//            else if ((note2.getPortee().getNumero() == note1.getPortee().getNumero())
//                     && note1.getHauteur().equals(note2.getHauteur()))
//                    return 0;
            else
                     return 1;

        }
    }
