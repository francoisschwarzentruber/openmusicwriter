/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.donnees;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import musicwriter.Erreur;
import musicwriter.donnees.Hauteur.Alteration;
import musicwriter.donnees.Hauteur.NoteNom;
import musicwriter.gui.partitionaffichage.AffichageSilence;
import musicwriter.guiswing.ImageLoaderSwing;

/**
 *
 * @author Ancmin
 */
public class Instrument {
    private final int numeroMIDI;
    static private final String[] instrumentsNoms = chargerNoms();

    public Instrument(int numeroMIDI)
    {
        if(!((0 <= numeroMIDI) & (numeroMIDI < 128)))
        {
                Erreur.message("Le fichier contient un instrument que Schwarz Musicwriter ne comprend pas : le numéro de l'instrument MIDI est le "
                                + numeroMIDI + ". Schwarz Musicwriter a décidé de remplacer cet instrument inconnu par un piano (instrument MIDI = 0)");
               this.numeroMIDI = 0;
        }
        else
            this.numeroMIDI = numeroMIDI;
    }



    public String getNom()
    {
         return instrumentsNoms[numeroMIDI];
    }


    private ImageIcon getImageIconTailleReelle()
    {
        URL urlpng = ImageLoaderSwing.getURL("instruments/" + (numeroMIDI+1) + ".png");
        URL urljpg = ImageLoaderSwing.getURL("instruments/" + (numeroMIDI+1) + ".jpg");
        
        if(urlpng != null)
             return (new ImageIcon(urlpng));
        else
        {
            if(urljpg != null)
             return (new ImageIcon(urljpg));
            else
            {
                return null;
            }
        }
             
    }
    

    public ImageIcon getImageIcon()
    {
        ImageIcon imgI = getImageIconTailleReelle();

        if(imgI == null)
            return null;
        else
            if(imgI.getIconHeight() < 96)
                return imgI;
            else
            {
                double scale = 96.0f / imgI.getIconHeight();
                Image img = imgI.getImage().getScaledInstance((int) (imgI.getIconWidth() * scale),
                             (int) (imgI.getIconHeight() * scale), Image.SCALE_DEFAULT);
                return new ImageIcon(img);
            }


    }

    
    
    static private Map<Integer, ImageIcon> instrumentImage = new HashMap<Integer, ImageIcon>();
    
    
    private ImageIcon createImageIconSmall()
    {
        ImageIcon imgI = getImageIconTailleReelle();

        if(imgI == null)
            return null;
        else
            if(imgI.getIconHeight() < 48)
                return imgI;
            else
            {
                double scale = 48.0f / imgI.getIconHeight();
                Image img = imgI.getImage().getScaledInstance((int) (imgI.getIconWidth() * scale),
                             (int) (imgI.getIconHeight() * scale), Image.SCALE_DEFAULT);
                return new ImageIcon(img);
            }


    }
    
    
    public ImageIcon getImageIconSmall()
    {
        if(instrumentImage.containsKey(this.numeroMIDI))
        {
            return instrumentImage.get(this.numeroMIDI);
        }
        else
        {
            final ImageIcon img = createImageIconSmall();
            instrumentImage.put(this.numeroMIDI, img);
            return img;
        }

    }


    public int getPorteesNombreStandard()
    {
        if(numeroMIDI < 10)
            return 2;
        else
            return 1;
    }

    public int getNumeroMIDI() {
        return numeroMIDI;
    }

    
    public Portee.Clef getClefStandard() {
        switch(numeroMIDI)
        {
            case 57:
            case 60: return Portee.Clef.ClefDeFa;
            case 47:
            case 32: return Portee.Clef.ClefDeFa;
            default: return Portee.Clef.ClefDeSol;
                   
        }
    }

    static private String[] chargerNoms() {
        String[] noms = new String[128];
        
        for(int i = 0; i < 128; i++)
        {
            noms[i] = "<instrument inconnu pour musicwriter>";
        }

        
        noms[0] = "piano";
        noms[1] = "piano";
        noms[2] = "piano";
        noms[3] = "piano";
        noms[4] = "piano";
        noms[5] = "piano";
        noms[6] = "clavecin";
        noms[7] = "piano";
        noms[8] = "celesta";
        noms[9] = "glockenspiel";
        noms[10] = "boîte à musique";
        noms[11] = "vibraphone";
        noms[12] = "marimba";
        noms[13] = "xylophone";
        noms[14] = "cloches tubulaires";
        noms[15] = "Tympanon";
        noms[16] = "Orgue";
        noms[17] = "piano";
        noms[18] = "piano";
        noms[19] = "piano";
        noms[20] = "piano";
        noms[21] = "accordéon";
        noms[22] = "harmonica";
        noms[23] = "bandonéon";

        noms[24] = "guitare";

        noms[40] = "violon";
        noms[41] = "alto";
        noms[42] = "violoncelle";
        noms[43] = "contrebasse";
        noms[44] = "cloches tubulaires";
        noms[45] = "Tympanon";
        noms[46] = "Harpe";
        noms[47] = "Timbales";
        noms[48] = "piano";
        noms[49] = "piano";


        noms[56] = "Trompette";
        noms[57] = "Trombone";
        noms[58] = "Tuba";
        noms[59] = "Trompette en sourdine";
        noms[60] = "Cor d'harmonie";

        noms[64] = "Saxophone soprano";
        noms[65] = "Saxophone alto";
        noms[66] = "Saxophone ténor";
        noms[67] = "Saxophone baryton";
        noms[68] = "Hautbois";
        noms[69] = "Cor anglais";
        noms[70] = "Basson";
        noms[71] = "Clarinette";

        noms[72] = "Piccolo";
        noms[73] = "Flûte";
        noms[74] = "Flûte à bec";
        noms[75] = "Flûte de pan";
        noms[76] = "Bouteille";
        noms[77] = "Saxophone baryton";
        return noms;
    }

    public boolean isJouable(Hauteur hauteur) {
        if(numeroMIDI == 40)//flûte
            return hauteur.isPlusGrave(new Hauteur(3, NoteNom.DO, Alteration.NATUREL)) &
                   (new Hauteur(-1, NoteNom.SOL, Alteration.NATUREL)).isPlusGrave(hauteur);
        if(numeroMIDI == 73)//flûte
            return hauteur.isPlusGrave(new Hauteur(3, NoteNom.DO, Alteration.NATUREL)) & Hauteur.getDo0().isPlusGrave(hauteur);
        else
            return true;
    }






}
