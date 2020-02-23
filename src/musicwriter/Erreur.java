/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter;

import musicwriter.guiswing.dialogs.DialogErreur;

/**
 *
 * @author proprietaire
 */
public class Erreur {
    static final boolean boitededialogue = false;

    /**
     * prévient l'utilisateur geek d'une erreur dans le programme
     * @param texte
     */
    public static void message(String texte)
    {
        System.out.println("erreur : " + texte);
        if(boitededialogue)
        {
            DialogErreur d = new DialogErreur(null, true);
            d.setMessage(texte);
            d.setVisible(true);
        }
        
    }

}
