/*
 * To move to gui
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.net.URL;
import javax.swing.ImageIcon;
import musicwriter.Erreur;
import musicwriter.ResourceAnchor;
import musicwriter.svg.SVGToImageFactory;

/**
 *
 * @author Ancmin
 */
public class ImageLoader {

    static private ImageIcon imgDefault = ImageLoader.getImageIcon("note_erreur.png");
    
    /**
     * 
     * @param filename
     * @return the complete filename with the path inside the jar file
     */
    static private String cheminComplet(String filename)
    {
        System.out.println("cheminComplet(" + filename + ")");
        
        if(filename.startsWith("/"))
                return filename;

        URL url = ResourceAnchor.class.getResource("/musicwriter/guiswing/resources/" + filename);

        if(url != null)
            return "/musicwriter/guiswing/resources/" + filename;

        url = ResourceAnchor.class.getResource("/musicwriter/guiswing/dialogs/resources/"
                                                    + filename);
        if(url != null)
                return "/musicwriter/guiswing/dialogs/resources/" + filename;

        url = ResourceAnchor.class.getResource("/musicwriter/donnees/resources/"
                                                    + filename);
        if(url != null)
                 return "/musicwriter/donnees/resources/"
                                                    + filename;

                
        url = ResourceAnchor.class.getResource(filename);
            
        if(url != null)
                 return filename;
            
        Erreur.message("ImageLoader.cheminComplet : " + filename + " non trouvé dans le .jar");
        return filename;

    }

    


//    static public ImageIcon reduireSiTropGrandPourBouton(ImageIcon imgIcon)
//    {
//        int width = imgIcon.getIconWidth();
//        int height = imgIcon.getIconHeight();
//        int widthmax = 48;
//        int heightmax = 48;
//        if(width < widthmax && height < widthmax)
//        {
//
//        }
//        else
//        {
//            if(width > height)
//            {
//                if(width > widthmax)
//                imgIcon = new ImageIcon(imgIcon.getImage().getScaledInstance(widthmax, widthmax*height/width, 0));
//            }
//            else
//            {
//                if(height > heightmax)
//                imgIcon = new ImageIcon(imgIcon.getImage().getScaledInstance(heightmax*width/height, heightmax, 0));
//            }
//        }
//        return imgIcon;
//    }


    static private ImageIcon reduireSiTropGrandPourBouton(ImageIcon imgIcon)
    {
        return reduireSiTropGrandPourBouton(imgIcon, 32*32);
    }


    static private ImageIcon reduireSiTropGrandPourPetitBouton(ImageIcon imgIcon)
    {
        return reduireSiTropGrandPourBouton(imgIcon, 16*16);
    }


    static private ImageIcon reduireSiTropGrandPourBouton(ImageIcon imgIcon, double aire)
    {
        int width0 = imgIcon.getIconWidth();
        int height0 = imgIcon.getIconHeight();

        if(width0 * height0 < aire)
        {}
        else
        {

             int width = (int) Math.round(Math.sqrt(aire * width0 / height0));
             imgIcon = new ImageIcon(imgIcon.getImage().getScaledInstance(width, width*height0/width0, 0));

        }
        return imgIcon;
    }

    
    static public ImageIcon getSVGImageIconPourBouton(String filename)
    {
        return reduireSiTropGrandPourBouton(getSVGImageIconAvecWidth(filename, 48));

        
    }


    static public Image getSVGImage(String filename)
    {
            return SVGToImageFactory.getImage(cheminComplet(filename));
    }


    static private ImageIcon getSVGImageIconAvecScale(String filename, float scale)
    {
        return new ImageIcon(SVGToImageFactory.getImageWithScale(cheminComplet(filename), scale));
    }


    public static ImageIcon getSVGImageIconAvecWidth(String filename, int width) {
        return new ImageIcon(SVGToImageFactory.getImageWithWidth(cheminComplet(filename), width));
    }

    
    /**
     * 
     * @param filename
     * @return the image contained in the file filename (used in the class to draw the score)
     */
    static public ImageIcon getImageIcon(String filename)
    {
        if(filename.endsWith(".png"))
            return getImageIconPNG(filename);
        else
            return getSVGImageIconAvecScale(filename, 8.0f);
    }

/**
     * 
     * @param filename
     * @return the image contained in the file filename (used in the class to draw the score)
     */
    static public ImageIcon getImageIconPNG(String filename)
    {
        try
        {
            URL url = ImageLoader.class.getResource(cheminComplet(filename));
            
            if(url != null)
                return new ImageIcon(url);
            else
            {
                System.out.println("problem getImageIcon with " + filename);
                return imgDefault;
            }
            
        }
        catch(Exception e)
        {
            Erreur.message("ImageLoader.getImageIcon(" + filename + ")");
            return new ImageIcon(new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR));
        }


        //return new ImageIcon(PalettePanel.class.getResource("../resources/clef_sol.png"));
    }

    public static ImageIcon getImageIcon(String filename, double scale) {
        ImageIcon img = getImageIcon(filename);
        return new ImageIcon(img.getImage().getScaledInstance((int) (img.getIconWidth() * scale), (int) (img.getIconHeight() * scale), 0));
    }

    static private ImageIcon getImageIconNullSiRien(String filename)
    {
        try
        {
                return new ImageIcon(ImageLoader.class.getResource(cheminComplet(filename)));

        }
        catch(Exception e)
        {
            Erreur.message("ImageLoader.getImageIcon(" + filename + ")");
            return imgDefault;
        }


        //return new ImageIcon(PalettePanel.class.getResource("../resources/clef_sol.png"));
    }

    static public ImageIcon getImageIconPourBouton(String filename)
    {
        ImageIcon img = getImageIcon(filename);
        return  reduireSiTropGrandPourBouton(img);
    }

    static public ImageIcon getImageIconPourPetitBouton(String filename)
    {
        ImageIcon img = getImageIcon(filename);
        return  reduireSiTropGrandPourPetitBouton(img);
    }


    static public ImageIcon getImageIconPourBouton(ImageIcon img)
    {
        return  reduireSiTropGrandPourBouton(img);
    }


    static public ImageIcon getImageIconPourBoutonNullSiRien(String filename)
    {
        ImageIcon img = getImageIconNullSiRien(filename);
        if(img != null)
             return reduireSiTropGrandPourBouton(img);
        else
            return imgDefault;
    }

    
    
    /**
     * 
     * @param crochetImg
     * @return the image but in gray
     */
    public static ImageIcon getImageGris(ImageIcon crochetImg) {
        RescaleOp scOp = new RescaleOp(1000.0f, 150.0f, null);
        BufferedImage bimg = new  BufferedImage(crochetImg.getIconWidth(),
                                                crochetImg.getIconHeight(),
                                                BufferedImage.TYPE_INT_ARGB );
        bimg.getGraphics().drawImage(crochetImg.getImage(), 0, 0, null);
        scOp.filter(bimg, bimg);
        return new ImageIcon(bimg);
    }

    
    
    
    
    public static URL getURL(String string) {
        return ResourceAnchor.class.getResource(cheminComplet(string));
    }




    
}
