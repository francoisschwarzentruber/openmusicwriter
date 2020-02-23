/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import musicwriter.guiswing.FileFilterFactory.FileFilterWithExtension;


/**
 * Cette classe est une boîte de dialogue de demande de fichiers 
 * qui prévient si on va écraser un fichier
 * @author Ancmin
 */
public class JFileChooserNoOverwrite extends JFileChooser
{
    @Override
    public void approveSelection()
    {
        
        String fileName = getSelectedFile().getAbsolutePath();
        if(!(fileName.charAt(fileName.length()-4) == '.'))
        {

            fileName = fileName +  "." + 
                    ((FileFilterWithExtension) getFileFilter()).getExtension();
        }
        File file = new File(fileName);
        if (file.exists())
        {
            int answer = JOptionPane.showConfirmDialog(
                this, file + " exists. Overwrite?", "Overwrite?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
            );
            if (answer != JOptionPane.OK_OPTION)
            {
                return;
            }
        }
        super.approveSelection();
    }
}
