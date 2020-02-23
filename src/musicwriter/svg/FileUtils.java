/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.svg;

import java.io.*;
import java.util.*;

/**
 *
 * @author said
 */
public class FileUtils {
    
    public static String MUSIC_WRITER_HOME =
            System.getProperty("user.home")+
            System.getProperty("file.separator") + ".MusicWriter";
    public static String IMAGES_HOME =
            MUSIC_WRITER_HOME +
            System.getProperty("file.separator") + 
            "images";
    public static String IMAGES_JAR_PATH = "";

    public static File createMusicWriterDirectory() {
        try {
            String dirName = System.getProperty("user.home");
            File parentDir = new File(dirName, ".MusicWriter");
            if (parentDir.mkdir()) {
                System.out.println("Parent dir: " + parentDir + " created.");
            }
            return parentDir;
        } catch (Exception e) {
            System.out.println("Error getting/creating .MusicWriter directory in your home: "+System.getProperty("user.home"));
        }
        return null;
    }

    public static File createImagesDirectory() {
        try {
            File parentDir = createMusicWriterDirectory();
            File imagesDir = new File(parentDir, "images");
            if (imagesDir.mkdir()) {
                System.out.println("images dir: " + imagesDir + " created.");
            }
            return imagesDir;
        } catch (Exception e) {
            System.out.println("Error getting/creating images directory in "+createMusicWriterDirectory());
        }
        return null;
    }

    public static File createImageFile(String fileName) {
        try {
            File imagesDir = createImagesDirectory();
            File file = new File(imagesDir, fileName);
            if (file.createNewFile()) {
                System.out.println("Image file: " + file + " created.");
            }
            return file;
        } catch (Exception e) {
            System.out.println("Error getting/creating image file: " + fileName);
        }
        return null;
    }

    public static void extractImageFile(String imagesJarPath, String fileName) {
        InputStream is = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        String line;
        try {
            File imageFile = createImageFile(fileName);
//            System.out.println("image file: "+imageFile);
            bw = new BufferedWriter(new FileWriter(imageFile));
            is = FileUtils.class.getResourceAsStream(imagesJarPath + fileName);
            br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                bw.write(line);
                bw.newLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error occured while copiying image file: " + fileName);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> readTextFromJar(String s) {
        InputStream is = null;
        BufferedReader br = null;
        String line;
        ArrayList<String> list = new ArrayList<String>();

        try {
            is = FileUtils.class.getResourceAsStream(s);
            br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String getFileNameWithoutExtension(String fileName) {
        File tmpFile = new File(fileName);
        tmpFile.getName();
        int whereDot = tmpFile.getName().lastIndexOf('.');
        if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
            return tmpFile.getName().substring(0, whereDot);
        //extension = filename.substring(whereDot+1);
        }
        return "";
    }
}
