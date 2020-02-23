/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.svg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import musicwriter.Erreur;
import musicwriter.ResourceAnchor;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.ImageTranscoder;
/**
 *
 * @author said
 */
public class SVGToImageFactory {



    static public InputStream getResourceAsInputStream(String resourceCompleteNameInJAR) {
        InputStream I = ResourceAnchor.class.getClassLoader().getSystemResourceAsStream(resourceCompleteNameInJAR);
        if(I == null)
            Erreur.message("getResourceAsInputStream donne null");
	return I;

    }


    private static String getCompleteFileName(String fileName) {
        String completeFileName = FileUtils.IMAGES_HOME +
                System.getProperty("file.separator") + fileName;
        return completeFileName;
    }

    private static String getURI(String completeFileName) {
        String uri = new File(completeFileName).toURI().toString();
        
        if(uri == null)
            System.out.println("getURI à null avec " + completeFileName);
        return uri;
    }

    private static BufferedImage getImage(String fileName, float width, float height) {
        String cheminEntierSansNomFichier = fileName.substring(0, fileName.lastIndexOf("/")+1);
        String nomFichierSansChemin = fileName.substring(fileName.lastIndexOf("/")+1);

        FileUtils.extractImageFile(cheminEntierSansNomFichier, nomFichierSansChemin);
        String completeFileName = SVGToImageFactory.getCompleteFileName(nomFichierSansChemin);
        String uri = SVGToImageFactory.getURI(completeFileName);
        MyTranscoder transcoder = new MyTranscoder();


        try {
            if (width != -1) {
                transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, width);
            }
            if (height != -1) {
                transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, height);
            }
            transcoder.transcode(new TranscoderInput(uri), null);
        } catch (Exception ex) {
            System.out.println("Exception occured while transcoding the image: " + fileName + "\n" +
                    "Exception details: " + ex.getMessage());
        }
        BufferedImage image = transcoder.getImage();
        return image;
    }

    public static BufferedImage getImage(String fileName) {
        return SVGToImageFactory.getImage(fileName, -1, -1);
    }

    public static BufferedImage getImageWithHeight(String fileName, float height) {
        return SVGToImageFactory.getImage(fileName, -1, height);
    }

    public static BufferedImage getImageWithWidth(String fileName, float width) {
        return SVGToImageFactory.getImage(fileName, width, -1);
    }

    public static BufferedImage getImageWithScale(String fileName, float scale) {
        BufferedImage image = SVGToImageFactory.getImage(fileName, -1, -1);
        int initialWidth = image.getWidth();
        image = SVGToImageFactory.getImage(fileName, scale*initialWidth, -1);
        return image;
    }
}
