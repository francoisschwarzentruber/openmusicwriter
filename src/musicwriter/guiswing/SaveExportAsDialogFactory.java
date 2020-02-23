/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicwriter.guiswing;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Ancmin
 */
public class SaveExportAsDialogFactory {
    static public String demanderFichierNomPourExporter() {
        JFileChooser fc = new JFileChooserNoOverwrite();
        fc.setDialogTitle("Exporter la partition dans un fichier...");
        
        for(FileFilter f : FileFilterFactory.getExportableFileFilters())
        {
            fc.addChoosableFileFilter(f);
        }

        fc.setAcceptAllFileFilterUsed(false);


        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            if (fileName.charAt(fileName.length() - 4) == '.') {
                return fc.getSelectedFile().getAbsolutePath();
            } else {
                return fc.getSelectedFile().getAbsolutePath() + "."
                        + ((FileFilterFactory.FileFilterWithExtension) fc.getFileFilter()).getExtension();
            }

        } else {
            return null;
        }
    }

    static public String demanderFichierNomPourSauvegarder() {
        JFileChooser fc = new JFileChooserNoOverwrite();
        fc.setDialogTitle("Sauvegarder la partition dans un fichier...");
        fc.addChoosableFileFilter(FileFilterFactory.getMusicXMLFileFilter());
        fc.addChoosableFileFilter(FileFilterFactory.getMusicMXLFileFilter());

        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            if (fileName.charAt(fileName.length() - 4) == '.') {
                return fc.getSelectedFile().getAbsolutePath();
            } else {
                return fc.getSelectedFile().getAbsolutePath() + "."
                        + ((FileFilterFactory.FileFilterWithExtension) fc.getFileFilter()).getExtension();
            }

        } else {
            return null;
        }
    }
}
