/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Ancmin
 */
public class OpenScoreFileDialog extends JFileChooser {

    public OpenScoreFileDialog(File currentDirectory) {
        super(currentDirectory);
        
        setDialogTitle("Ouvrir une partition d'un fichier...");
        
        for(FileFilter f : FileFilterFactory.getOpenableFileFilters())
        {
            addChoosableFileFilter(f);
        }
        
        FileFilter allOpenableFormatFilter = FileFilterFactory.getAllOpenableFormatFilter();
        addChoosableFileFilter(allOpenableFormatFilter);
        setFileFilter(allOpenableFormatFilter);
        
    }
    
    

        
}
