/*
 * MusicwriterView.java
 */
package musicwriter.guiswing;

import com.sun.org.apache.xalan.internal.xsltc.dom.FilteredStepIterator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import musicwriter.Erreur;
import musicwriter.MusicwriterApp;
import musicwriter.controller.Controller;
import musicwriter.controller.ControllerListener;
import musicwriter.controller.PartitionLecteurListener;
import musicwriter.controller.PartitionPanelModeTexte;
import musicwriter.donnees.Hauteur.Alteration;
import musicwriter.donnees.*;
import musicwriter.Resource;
import musicwriter.donnees.actions.*;
import musicwriter.donnees.io.PartitionDonneesChargementGestionnaire;
import musicwriter.donnees.io.PartitionSauvegardeGestionnaire;
import musicwriter.gui.partitionaffichage.PartitionVue;
import musicwriter.guiswing.dialogs.*;
import musicwriter.midi.InterfaceMIDI;
import noterecognition.NoteRecognition;
import noterecognition.NoteRecognitionEvent;
import noterecognition.NoteRecognitionNoNoteListener;
import noterecognition.NoteRecognitionNotePerformedListener;
import org.jdesktop.application.Action;
import org.jdesktop.application.*;
import org.jdom.JDOMException;

/**
 * The application's main frame.
 */
public final class MusicwriterView extends FrameView {

    private Selection pressePapier = null;
    
    /**
     * dossier courant pour les boites de dialogue
     */
    File currentDirectory = null;
    private String fichierNom = null;

    
    
    private void machineSortieMIDI_creer(int machineSortieMIDInumero) {
        getController().setMachineSortieMIDI(new MachineSortieMIDIEcoutee(
                InterfaceMIDI.getOutputMIDIDeviceAvecNumero(machineSortieMIDInumero)));

    }

    
    private void machineEntreeMIDI_creer(int machineEntreeMIDInumero) {
        getController().setMachineEntreeMIDI(new MachineEntreeMIDI(
            InterfaceMIDI.getInputMIDIDeviceAvecNumero(machineEntreeMIDInumero)));

    }
    
    
    private PartitionPanel getPartitionPanel() {
        return (PartitionPanel) partitionPanel;
    }

    private void modeEcriture() {
        try {
            ((CardLayout) (panBarreOutils.getLayout())).show(panBarreOutils, "modeecriture");
        } catch (Exception e) {
            System.out.println(e);
        }
        // mnuAlteration.setVisible(false);
        mnuVoix.setVisible(false);

        if (helpPanel != null) {
            helpPanel.page_show("ecriture.html");
        }
    }

    private void modeSelection(Selection selection) {
        ((CardLayout) (panBarreOutils.getLayout())).show(panBarreOutils, "modeSelection");
        mnuAlteration.setVisible(true);
        mnuVoix.setVisible(true);
        helpPanel.page_show("selection.html");

        toolbarNotes.setVisible(false);

        cmdSelectionArmure.setVisible(false);
        cmdSelectionClef.setVisible(false);
        cmdSelectionMesureSignature.setVisible(false);
        cmdSelectionTempo.setVisible(false);
        if (selection.isSingleton()) {
            ElementMusical el = selection.getElementMusicalUnique();

            if (el instanceof ElementMusicalDuree) {
            } else if (el instanceof ElementMusicalChangementMesureSignature) {
                cmdSelectionMesureSignature.setVisible(true);
            } else if (el instanceof ElementMusicalChangementTonalite) {
                cmdSelectionArmure.setVisible(true);
            } else if (el instanceof ElementMusicalClef) {
                cmdSelectionClef.setVisible(true);
            } else if (el instanceof ElementMusicalTempo) {
                cmdSelectionTempo.setVisible(true);
            }





        } else {
            toolbarNotes.setVisible(true);




        }
    }

    private void modeStylet() {
        ((CardLayout) (panBarreOutils.getLayout())).show(panBarreOutils, "modeStylet");
    }

    private void modeSelectionCopierPasPlus() {
        ((CardLayout) (panBarreOutils.getLayout())).show(panBarreOutils, "modeSelectionDeplacementOuCopie");
        panelHelpNoteDeplacement.setModeJusteDeplacement();



    }

    private void modeSelectionCopierPlus() {
        ((CardLayout) (panBarreOutils.getLayout())).show(panBarreOutils, "modeSelectionDeplacementOuCopie");
        panelHelpNoteDeplacement.setModeCopie();

    }

    private void modeSelectionPasPlus() {
        lblSelectionPlus.setVisible(true);
        lblSelectionPlus.setOpaque(false);
        lblSelectionPlus.repaint();

    }

    private void modeSelectionPlus() {

        lblSelectionPlus.setVisible(true);
        lblSelectionPlus.setOpaque(true);
        lblSelectionPlus.repaint();
    }

    private void toolBarLecture() {
        ((CardLayout) (panBarreOutils.getLayout())).show(panBarreOutils, "modeLecture");
        helpPanel.page_show("lecture.html");
    }

    private void toolBarEnregistrementMIDI() {
        ((CardLayout) (panBarreOutils.getLayout())).show(panBarreOutils, "modeLecture");
        helpPanel.page_show("enregistrement.html");
    }

    public void declarerPartitionDonnees(PartitionDonnees partitionDonnees) {
       
        if (jScrollPane != null) {
            getController().setPartitionDonnees(partitionDonnees, jScrollPane.getSize().width - 50);

        } else {
            getController().setPartitionDonnees(partitionDonnees, 1000 - 50);
            
        }

        

    }

    public void partitionVueAdapterALEcran() {
        // ((PartitionPanel) partitionPanel).setSystemeLongueur(jScrollPane.getSize().width - 50);
    }



    private void nomFichier_Aucun() {
        fichierNom = null;
        this.getFrame().setTitle("Open Musicwriter - Nouvelle partition");
    }

    public MusicwriterView(SingleFrameApplication app) {
        super(app);

        
        initComponents();
        
        
        
        loadFromResource();
        
        
        PaletteFloatableDialog d = new PaletteFloatableDialog(getFrame(), getController(), false);
        d.setVisible(true);
        
        getController().addControllerListener(new ControllerListener() {

            @Override
            public void whenUpdate(Controller controller) {
                if(getController().isLecture())
                    toolBarLecture();
                else if(getController().isEnregistrement())
                    toolBarEnregistrementMIDI();
                else if(getController().isSelection())
                    modeSelection(getController().getSelection());
                else
                    modeEcriture();
            }
        
        
        
        });
        
        
        

        jScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        getFrame().setFocusTraversalPolicy(new FocusTraversalPolicy() {

            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                return getPartitionPanel();
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                return getPartitionPanel();
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return getPartitionPanel();
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return getPartitionPanel();
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return getPartitionPanel();
            }
        });
        nomFichier_Aucun();













        jSliderLectureVitesse.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                vitesseInterfaceToPartitionLecteur();
            }
        });

        this.getPartitionPanel().requestFocus();

        boutonMIDIStop.setVisible(false);
        boutonSortieMIDIReprendre.setVisible(false);
        boutonPause.setVisible(false);



        modeEcriture();


        machineSortieMIDI_creer(InterfaceMIDI.machineSortieMIDIStandardNumeroGet());
        machineEntreeMIDI_creer(InterfaceMIDI.machineEntreeMIDIStandardNumeroGet());

        
        declarerPartitionDonnees(PartitionDonnees.createPartitionDonneesNouvellePourPiano());

                
                
        getFrame().addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                partitionPanelKeyPressed(evt);
            }
        });


        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });




        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuAnnuler);
        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuRefaire);
        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuTransposer);

        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuAlterationBecarre);
        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuAlterationBemol);
        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuAlterationDiese);
        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuAlterationDoubleDiese);
        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuAlterationDoubleBemol);

        MenuItemCurstomizedAccelerator.makeMenuItemCustomizable(mnuAlterationEnharmonique);


        panAide.setVisible(true);


    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MusicwriterApp.getApplication().getMainFrame();
            aboutBox = new MusicwriterAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MusicwriterApp.getApplication().show(aboutBox);
    }

	
	
	
	private void loadFromResource()
	{
           getPartitionPanel().loadFromResource();
            
           musicwriter.ResourceMap resourceMap = Resource.createResourceMap(MusicwriterView.class);
            
//	fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N

      //  mnuNew.setIcon(resourceMap.getIcon("mnuNew.icon")); // NOI18N
        mnuNew.setText(resourceMap.getString("mnuNew.text")); // NOI18N
		
      //  cmdOpen.setIcon(resourceMap.getIcon("cmdOpen.icon")); // NOI18N
        cmdOpen.setText(resourceMap.getString("cmdOpen.text")); // NOI18N
        

     //   mnuSave.setIcon(resourceMap.getIcon("mnuSave.icon")); // NOI18N
        mnuSave.setText(resourceMap.getString("mnuSave.text")); // NOI18N
        

      //  mnuSaveAs.setIcon(resourceMap.getIcon("mnuSaveAs.icon")); // NOI18N
        mnuSaveAs.setText(resourceMap.getString("mnuSaveAs.text")); // NOI18N
        
     //   mnuExport.setIcon(resourceMap.getIcon("mnuExport.icon")); // NOI18N
        mnuExport.setText(resourceMap.getString("mnuExport.text")); // NOI18N
        
     //   mnuPrint.setIcon(resourceMap.getIcon("mnuPrint.icon")); // NOI18N
        mnuPrint.setText(resourceMap.getString("mnuPrint.text")); // NOI18N
        
      //  exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
     //   exitMenuItem.setName("exitMenuItem"); // NOI18N
        

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        
    //    mnuAnnuler.setIcon(resourceMap.getIcon("mnuAnnuler.icon")); // NOI18N
        mnuAnnuler.setText(resourceMap.getString("mnuAnnuler.text")); // NOI18N
        

    //    mnuRefaire.setIcon(resourceMap.getIcon("mnuRefaire.icon")); // NOI18N
        mnuRefaire.setText(resourceMap.getString("mnuRefaire.text")); // NOI18N
        

    //    mnuCut.setIcon(resourceMap.getIcon("mnuCut.icon")); // NOI18N
        mnuCut.setText(resourceMap.getString("mnuCut.text")); // NOI18N
        

      //  mnuCopy.setIcon(resourceMap.getIcon("mnuCopy.icon")); // NOI18N
        mnuCopy.setText(resourceMap.getString("mnuCopy.text")); // NOI18N
        

    //    mnuPaste.setIcon(resourceMap.getIcon("mnuPaste.icon")); // NOI18N
        mnuPaste.setText(resourceMap.getString("mnuPaste.text")); // NOI18N
        

     //   mnuEditionSupprimer.setIcon(resourceMap.getIcon("mnuEditionSupprimer.icon")); // NOI18N
        mnuEditionSupprimer.setText(resourceMap.getString("mnuEditionSupprimer.text")); // NOI18N
        
        mnuSelection.setText(resourceMap.getString("mnuSelection.text")); // NOI18N
        mnuSelectAll.setText(resourceMap.getString("mnuSelectAll.text")); // NOI18N
        
		mnuDeselectAll.setText(resourceMap.getString("mnuDeselectAll.text")); // NOI18N
        mnuDeselectAll.setName("mnuDeselectAll"); // NOI18N
        
        mnuSelectionMesure.setText(resourceMap.getString("mnuSelectionMesure.text")); // NOI18N
        
        mnuInsert.setText(resourceMap.getString("mnuInsert.text")); // NOI18N
        
		mnuNote.setText(resourceMap.getString("mnuNote.text")); // NOI18N
        
		mnuSilence.setText(resourceMap.getString("mnuSilence.text")); // NOI18N
        
		mnuInsererBarreDeMesure.setText(resourceMap.getString("mnuInsererBarreDeMesure.text")); // NOI18N
        
		mnuParoles.setText(resourceMap.getString("mnuParoles.text")); // NOI18N
        
		mnuInstruments.setText(resourceMap.getString("mnuInstruments.text")); // NOI18N
        mnuInstrumentationChange.setText(resourceMap.getString("mnuInstrumentationChange.text")); // NOI18N
        
	//	mnuAffichagePartie.setIcon(resourceMap.getIcon("mnuAffichagePartie.icon")); // NOI18N
        mnuAffichagePartie.setText(resourceMap.getString("mnuAffichagePartie.text")); // NOI18N
        
	//	mnuAffichageToutConducteur.setIcon(resourceMap.getIcon("mnuAffichageToutConducteur.icon")); // NOI18N
        mnuAffichageToutConducteur.setText(resourceMap.getString("mnuAffichageToutConducteur.text")); // NOI18N
        
        mnuDisplay.setText(resourceMap.getString("mnuDisplay.text")); // NOI18N
        
	//	mnuAffichagePartie1.setIcon(resourceMap.getIcon("mnuAffichagePartie1.icon")); // NOI18N
        mnuAffichagePartie1.setText(resourceMap.getString("mnuAffichagePartie1.text")); // NOI18N
        
	//	mnuAffichageToutConducteur1.setIcon(resourceMap.getIcon("mnuAffichageToutConducteur1.icon")); // NOI18N
        mnuAffichageToutConducteur1.setText(resourceMap.getString("mnuAffichageToutConducteur1.text")); // NOI18N
        
		
        mnuZoom.setText(resourceMap.getString("mnuZoom.text")); // NOI18N
       
        mnuZoom50.setText(resourceMap.getString("mnuZoom50.text")); // NOI18N
        
        mnuZoom100.setText(resourceMap.getString("mnuZoom100.text")); // NOI18N
        
        mnuZoom200.setText(resourceMap.getString("mnuZoom200.text")); // NOI18N
        
        jMenuNoteDuree.setText(resourceMap.getString("jMenuNoteDuree.text")); // NOI18N
        
      //  mnuTripleCroche.setIcon(resourceMap.getIcon("mnuTripleCroche.icon")); // NOI18N
        mnuTripleCroche.setText(resourceMap.getString("mnuTripleCroche.text")); // NOI18N
        
     //   mnuDoubleCroche.setIcon(resourceMap.getIcon("mnuDoubleCroche.icon")); // NOI18N
        mnuDoubleCroche.setText(resourceMap.getString("mnuDoubleCroche.text")); // NOI18N
        
    //    jMenuItemCroche.setIcon(resourceMap.getIcon("jMenuItemCroche.icon")); // NOI18N
        jMenuItemCroche.setText(resourceMap.getString("jMenuItemCroche.text")); // NOI18N
        
     //   jMenuItemNoire.setIcon(resourceMap.getIcon("jMenuItemNoire.icon")); // NOI18N
        jMenuItemNoire.setText(resourceMap.getString("jMenuItemNoire.text")); // NOI18N
        
      //  jMenuItemBlanche.setIcon(resourceMap.getIcon("jMenuItemBlanche.icon")); // NOI18N
        jMenuItemBlanche.setText(resourceMap.getString("jMenuItemBlanche.text")); // NOI18N
        
    //    mnuRonde.setIcon(resourceMap.getIcon("mnuRonde.icon")); // NOI18N
        mnuRonde.setText(resourceMap.getString("mnuRonde.text")); // NOI18N
        
      //  mnuSelectionEtirerDoubler.setIcon(resourceMap.getIcon("mnuSelectionEtirerDoubler.icon")); // NOI18N
        mnuSelectionEtirerDoubler.setText(resourceMap.getString("mnuSelectionEtirerDoubler.text")); // NOI18N
        
     //   mnuSelectionDureeDiviserParDeux.setIcon(resourceMap.getIcon("mnuSelectionDureeDiviserParDeux.icon")); // NOI18N
        mnuSelectionDureeDiviserParDeux.setText(resourceMap.getString("mnuSelectionDureeDiviserParDeux.text")); // NOI18N
        
        mnuAlteration.setText(resourceMap.getString("mnuAlteration.text")); // NOI18N
        

      //  mnuAlterationDoubleBemol.setIcon(resourceMap.getIcon("mnuAlterationDoubleBemol.icon")); // NOI18N
        mnuAlterationDoubleBemol.setText(resourceMap.getString("mnuAlterationDoubleBemol.text")); // NOI18N
        
	//	mnuAlterationBemol.setIcon(resourceMap.getIcon("mnuAlterationBemol.icon")); // NOI18N
        mnuAlterationBemol.setText(resourceMap.getString("mnuAlterationBemol.text")); // NOI18N
        
     //   mnuAlterationBecarre.setIcon(resourceMap.getIcon("mnuAlterationBecarre.icon")); // NOI18N
        mnuAlterationBecarre.setText(resourceMap.getString("mnuAlterationBecarre.text")); // NOI18N
        
	//	mnuAlterationDiese.setIcon(resourceMap.getIcon("mnuAlterationDiese.icon")); // NOI18N
        mnuAlterationDiese.setText(resourceMap.getString("mnuAlterationDiese.text")); // NOI18N
        
	//	mnuAlterationDoubleDiese.setIcon(resourceMap.getIcon("mnuAlterationDoubleDiese.icon")); // NOI18N
        mnuAlterationDoubleDiese.setText(resourceMap.getString("mnuAlterationDoubleDiese.text")); // NOI18N
        
	
        
		
		
        mnuAlterationEnharmonique.setText(resourceMap.getString("mnuAlterationEnharmonique.text")); // NOI18N
        
		
        mnuAlterationArmature.setText(resourceMap.getString("mnuAlterationArmature.text")); // NOI18N
        
		
        mnuAlterationViaTonalite.setText(resourceMap.getString("mnuAlterationViaTonalite.text")); // NOI18N
        
		
		
        mnuVoix.setText(resourceMap.getString("mnuVoix.text")); // NOI18N
        
		
     //   mnuNouvelleVoix.setIcon(resourceMap.getIcon("mnuNouvelleVoix.icon")); // NOI18N
        mnuNouvelleVoix.setText(resourceMap.getString("mnuNouvelleVoix.text")); // NOI18N
        
		
        mnuVoiceSameVoice.setText(resourceMap.getString("mnuVoiceSameVoice.text")); // NOI18N
        
		
     //   mnuVoixNotesIndependantes.setIcon(resourceMap.getIcon("mnuVoixNotesIndependantes.icon")); // NOI18N
        mnuVoixNotesIndependantes.setText(resourceMap.getString("mnuVoixNotesIndependantes.text")); // NOI18N
        
		

        mnuHarmonie.setText(resourceMap.getString("mnuHarmonie.text")); // NOI18N
        
		
        
		mnuTransposer.setText(resourceMap.getString("mnuTransposer.text")); // NOI18N
        
		
        mnuIntervalAdd.setText(resourceMap.getString("mnuIntervalAdd.text")); // NOI18N
        
		mnuIntervalAdd1.setText(resourceMap.getString("mnuIntervalAdd1.text")); // NOI18N
        
		mnuIntervalAdd2.setText(resourceMap.getString("mnuIntervalAdd2.text")); // NOI18N
        
		mnuIntervalAdd4.setText(resourceMap.getString("mnuIntervalAdd4.text")); // NOI18N
        
		mnuIntervalAdd5.setText(resourceMap.getString("mnuIntervalAdd5.text")); // NOI18N
        
		
		mnuIntervalAdd6.setText(resourceMap.getString("mnuIntervalAdd6.text")); // NOI18N
        
		mnuIntervalAdd7.setText(resourceMap.getString("mnuIntervalAdd7.text")); // NOI18N
        
		mnuIntervalAdd8.setText(resourceMap.getString("mnuIntervalAdd8.text")); // NOI18N
        
		
        mnuIntervalAddM2.setText(resourceMap.getString("mnuIntervalAddM2.text")); // NOI18N
        
		
        mnuIntervalAddM3.setText(resourceMap.getString("mnuIntervalAddM3.text")); // NOI18N
        
		
        mnuIntervalAddM4.setText(resourceMap.getString("mnuIntervalAddM4.text")); // NOI18N
        
		
        mnuIntervalAddM5.setText(resourceMap.getString("mnuIntervalAddM5.text")); // NOI18N
        
		
        mnuIntervalAddM6.setText(resourceMap.getString("mnuIntervalAddM6.text")); // NOI18N
        
		
        mnuIntervalAddM7.setText(resourceMap.getString("mnuIntervalAddM7.text")); // NOI18N
        
		
        mnuIntervalAddM8.setText(resourceMap.getString("mnuIntervalAddM8.text")); // NOI18N
        
		

        jMenu5.setText(resourceMap.getString("jMenu5.text")); // NOI18N
        
		mnuConfiguration.setText(resourceMap.getString("mnuConfiguration.text")); // NOI18N
        
		
        mnuMicrophoneEnregistrer.setText(resourceMap.getString("mnuMicrophoneEnregistrer.text")); // NOI18N
        
		
        mnuMicrophoneStop.setText(resourceMap.getString("mnuMicrophoneStop.text")); // NOI18N
        
		
    //    helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        
		

        mnuSendComments.setText(resourceMap.getString("mnuSendComments.text")); // NOI18N
        
		
        jMenuItem9.setText(resourceMap.getString("jMenuItem9.text")); // NOI18N
        
	//	aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        
		
     //   jMenuItem8.setIcon(resourceMap.getIcon("jMenuItem8.icon")); // NOI18N
        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text")); // NOI18N
        
		
        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        
		
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        
		
		mnuRepaint.setName("mnuRepaint"); // NOI18N
        
		
        mnuDebugAllSameVoice.setText(resourceMap.getString("mnuDebugAllSameVoice.text")); // NOI18N
       
	   
        statusPanel.setName("statusPanel"); // NOI18N
        
		
	//	boutonOuvrir.setIcon(resourceMap.getIcon("boutonOuvrir.icon")); // NOI18N
        boutonOuvrir.setText(resourceMap.getString("boutonOuvrir.text")); // NOI18N
        boutonOuvrir.setToolTipText(resourceMap.getString("boutonOuvrir.toolTipText")); // NOI18N
        
     //   boutonSauvegarder.setIcon(resourceMap.getIcon("boutonSauvegarder.icon")); // NOI18N
        boutonSauvegarder.setText(resourceMap.getString("boutonSauvegarder.text")); // NOI18N
        
        boutonImprimer.setText(resourceMap.getString("boutonImprimer.text")); // NOI18N
        boutonImprimer.setToolTipText(resourceMap.getString("boutonImprimer.toolTipText")); // NOI18N
        

        
     //   boutonAnnuler.setIcon(resourceMap.getIcon("boutonAnnuler.icon")); // NOI18N
        boutonAnnuler.setText(resourceMap.getString("boutonAnnuler.text")); // NOI18N
        
    //    boutonRefaire.setIcon(resourceMap.getIcon("boutonRefaire.icon")); // NOI18N
        boutonRefaire.setText(resourceMap.getString("boutonRefaire.text")); // NOI18N
        boutonRefaire.setToolTipText(resourceMap.getString("boutonRefaire.toolTipText")); // NOI18N
        

     //   boutonEntreeMIDIEnregistrer.setIcon(resourceMap.getIcon("boutonEntreeMIDIEnregistrer.icon")); // NOI18N
        boutonEntreeMIDIEnregistrer.setText(resourceMap.getString("boutonEntreeMIDIEnregistrer.text")); // NOI18N
        boutonEntreeMIDIEnregistrer.setToolTipText(resourceMap.getString("boutonEntreeMIDIEnregistrer.toolTipText")); // NOI18N
        

     //   boutonSortieMIDILecture.setIcon(resourceMap.getIcon("boutonSortieMIDILecture.icon")); // NOI18N
        boutonSortieMIDILecture.setText(resourceMap.getString("boutonSortieMIDILecture.text")); // NOI18N
        boutonSortieMIDILecture.setToolTipText(resourceMap.getString("boutonSortieMIDILecture.toolTipText")); // NOI18N
        

   //    boutonPause.setIcon(resourceMap.getIcon("boutonPause.icon")); // NOI18N
        boutonPause.setText(resourceMap.getString("boutonPause.text")); // NOI18N
        boutonPause.setToolTipText(resourceMap.getString("boutonPause.toolTipText")); // NOI18N

    //    boutonMIDIStop.setIcon(resourceMap.getIcon("boutonMIDIStop.icon")); // NOI18N
        boutonMIDIStop.setText(resourceMap.getString("boutonMIDIStop.text")); // NOI18N
        boutonMIDIStop.setToolTipText(resourceMap.getString("boutonMIDIStop.toolTipText")); // NOI18N
        

   //     boutonSortieMIDIReprendre.setIcon(resourceMap.getIcon("boutonSortieMIDIReprendre.icon")); // NOI18N
        boutonSortieMIDIReprendre.setText(resourceMap.getString("boutonSortieMIDIReprendre.text")); // NOI18N
        boutonSortieMIDIReprendre.setToolTipText(resourceMap.getString("boutonSortieMIDIReprendre.toolTipText")); // NOI18N
        
    //    boutonPause1.setIcon(resourceMap.getIcon("boutonPause1.icon")); // NOI18N
        boutonPause1.setText(resourceMap.getString("boutonPause1.text")); // NOI18N
        boutonPause1.setToolTipText(resourceMap.getString("boutonPause1.toolTipText")); // NOI18N
        

    //    boutonMIDIStop1.setIcon(resourceMap.getIcon("boutonMIDIStop1.icon")); // NOI18N
        boutonMIDIStop1.setText(resourceMap.getString("boutonMIDIStop1.text")); // NOI18N
        boutonMIDIStop1.setToolTipText(resourceMap.getString("boutonMIDIStop1.toolTipText")); // NOI18N
       

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N


    //    jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
       

       

    //    jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
      
     //   lblSelectionPlus.setIcon(resourceMap.getIcon("lblSelectionPlus.icon")); // NOI18N
        lblSelectionPlus.setText(resourceMap.getString("lblSelectionPlus.text")); // NOI18N
       

        
     //   cmdNoteDecorer.setIcon(resourceMap.getIcon("cmdNoteDecorer.icon")); // NOI18N
        cmdNoteDecorer.setText(resourceMap.getString("cmdNoteDecorer.text")); // NOI18N
        
    //    cmdSelectionSupprimer.setIcon(resourceMap.getIcon("cmdSelectionSupprimer.icon")); // NOI18N
        cmdSelectionSupprimer.setText(resourceMap.getString("cmdSelectionSupprimer.text")); // NOI18N
        cmdSelectionSupprimer.setToolTipText(resourceMap.getString("cmdSelectionSupprimer.toolTipText")); // NOI18N
        

    //    cmdSelectionArmure.setIcon(resourceMap.getIcon("cmdSelectionArmure.icon")); // NOI18N
        cmdSelectionArmure.setText(resourceMap.getString("cmdSelectionArmure.text")); // NOI18N
    //    cmdSelectionMesureSignature.setIcon(resourceMap.getIcon("cmdSelectionMesureSignature.icon")); // NOI18N
        cmdSelectionMesureSignature.setText(resourceMap.getString("cmdSelectionMesureSignature.text")); // NOI18N
    //    cmdSelectionClef.setIcon(resourceMap.getIcon("cmdSelectionClef.icon")); // NOI18N
        cmdSelectionClef.setText(resourceMap.getString("cmdSelectionClef.text")); // NOI18N
   //     cmdSelectionTempo.setIcon(resourceMap.getIcon("cmdSelectionTempo.icon")); // NOI18N
        cmdSelectionTempo.setText(resourceMap.getString("cmdSelectionTempo.text")); // NOI18N


	}
	
	
	
	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        mnuNew = new javax.swing.JMenuItem();
        cmdOpen = new javax.swing.JMenuItem();
        mnuSave = new javax.swing.JMenuItem();
        mnuSaveAs = new javax.swing.JMenuItem();
        mnuExport = new javax.swing.JMenuItem();
        mnuPrint = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuAnnuler = new javax.swing.JMenuItem();
        mnuRefaire = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mnuCut = new javax.swing.JMenuItem();
        mnuCopy = new javax.swing.JMenuItem();
        mnuPaste = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mnuEditionSupprimer = new javax.swing.JMenuItem();
        mnuSelection = new javax.swing.JMenu();
        mnuSelectAll = new javax.swing.JMenuItem();
        mnuDeselectAll = new javax.swing.JMenuItem();
        mnuSelectionMesure = new javax.swing.JMenuItem();
        mnuInsert = new javax.swing.JMenu();
        mnuNote = new javax.swing.JMenuItem();
        mnuSilence = new javax.swing.JMenuItem();
        mnuInsererBarreDeMesure = new javax.swing.JMenuItem();
        mnuParoles = new javax.swing.JMenuItem();
        mnuInstruments = new javax.swing.JMenu();
        mnuInstrumentationChange = new javax.swing.JMenuItem();
        mnuAffichagePartie = new javax.swing.JMenuItem();
        mnuAffichageToutConducteur = new javax.swing.JMenuItem();
        mnuDisplay = new javax.swing.JMenu();
        mnuAffichagePartie1 = new javax.swing.JMenuItem();
        mnuAffichageToutConducteur1 = new javax.swing.JMenuItem();
        mnuZoom = new javax.swing.JMenu();
        mnuZoom50 = new javax.swing.JMenuItem();
        mnuZoom100 = new javax.swing.JMenuItem();
        mnuZoom200 = new javax.swing.JMenuItem();
        jMenuNoteDuree = new javax.swing.JMenu();
        mnuTripleCroche = new javax.swing.JMenuItem();
        mnuDoubleCroche = new javax.swing.JMenuItem();
        jMenuItemCroche = new javax.swing.JMenuItem();
        jMenuItemNoire = new javax.swing.JMenuItem();
        jMenuItemBlanche = new javax.swing.JMenuItem();
        mnuRonde = new javax.swing.JMenuItem();
        mnuSelectionEtirerDoubler = new javax.swing.JMenuItem();
        mnuSelectionDureeDiviserParDeux = new javax.swing.JMenuItem();
        mnuAlteration = new javax.swing.JMenu();
        mnuAlterationDoubleBemol = new javax.swing.JMenuItem();
        mnuAlterationBemol = new javax.swing.JMenuItem();
        mnuAlterationBecarre = new javax.swing.JMenuItem();
        mnuAlterationDiese = new javax.swing.JMenuItem();
        mnuAlterationDoubleDiese = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mnuAlterationEnharmonique = new javax.swing.JMenuItem();
        mnuAlterationArmature = new javax.swing.JMenuItem();
        mnuAlterationViaTonalite = new javax.swing.JMenuItem();
        mnuVoix = new javax.swing.JMenu();
        mnuNouvelleVoix = new javax.swing.JMenuItem();
        mnuVoiceSameVoice = new javax.swing.JMenuItem();
        mnuVoixNotesIndependantes = new javax.swing.JMenuItem();
        mnuHarmonie = new javax.swing.JMenu();
        mnuTransposer = new javax.swing.JMenuItem();
        mnuIntervalAdd = new javax.swing.JMenu();
        mnuIntervalAdd1 = new javax.swing.JMenuItem();
        mnuIntervalAdd2 = new javax.swing.JMenuItem();
        mnuIntervalAdd4 = new javax.swing.JMenuItem();
        mnuIntervalAdd5 = new javax.swing.JMenuItem();
        mnuIntervalAdd6 = new javax.swing.JMenuItem();
        mnuIntervalAdd7 = new javax.swing.JMenuItem();
        mnuIntervalAdd8 = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        mnuIntervalAddM2 = new javax.swing.JMenuItem();
        mnuIntervalAddM3 = new javax.swing.JMenuItem();
        mnuIntervalAddM4 = new javax.swing.JMenuItem();
        mnuIntervalAddM5 = new javax.swing.JMenuItem();
        mnuIntervalAddM6 = new javax.swing.JMenuItem();
        mnuIntervalAddM7 = new javax.swing.JMenuItem();
        mnuIntervalAddM8 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        mnuConfiguration = new javax.swing.JMenuItem();
        mnuMicrophoneEnregistrer = new javax.swing.JMenuItem();
        mnuMicrophoneStop = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        mnuSendComments = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        mnuRepaint = new javax.swing.JMenuItem();
        mnuDebugAllSameVoice = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        panelGeneral = new javax.swing.JPanel();
        panelTravail = new javax.swing.JPanel();
        panBarreOutils = new javax.swing.JPanel();
        panelHelpStylet = new musicwriter.guiswing.PanelHelpStylet();
        toolBarEcriture = new javax.swing.JToolBar();
        boutonOuvrir = new javax.swing.JButton();
        boutonSauvegarder = new javax.swing.JButton();
        boutonImprimer = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        boutonAnnuler = new javax.swing.JButton();
        boutonRefaire = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        boutonEntreeMIDIEnregistrer = new javax.swing.JButton();
        boutonSortieMIDILecture = new javax.swing.JButton();
        boutonPause = new javax.swing.JButton();
        boutonMIDIStop = new javax.swing.JButton();
        toolBarLecture = new javax.swing.JToolBar();
        boutonSortieMIDIReprendre = new javax.swing.JButton();
        boutonPause1 = new javax.swing.JButton();
        boutonMIDIStop1 = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSliderLectureVitesse = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        toolBarSelection = new javax.swing.JToolBar();
        lblSelectionPlus = new javax.swing.JLabel();
        toolbarNotes = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdNoteDecorer = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        cmdSelectionSupprimer = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        cmdSelectionArmure = new javax.swing.JButton();
        cmdSelectionMesureSignature = new javax.swing.JButton();
        cmdSelectionClef = new javax.swing.JButton();
        cmdSelectionTempo = new javax.swing.JButton();
        panelHelpNoteDeplacement = new musicwriter.guiswing.PanelHelpNoteDeplacement();
        surfaceTravail = new javax.swing.JPanel();
        palettePanel = new musicwriter.guiswing.palette.PalettePanel();
        jScrollPane = new javax.swing.JScrollPane();
        partitionPanel = new PartitionPanel();
        panAide = new javax.swing.JPanel();
        helpPanel = new musicwriter.guiswing.HelpPanel();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(MusicwriterView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        mnuNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mnuNew.setIcon(resourceMap.getIcon("mnuNew.icon")); // NOI18N
        mnuNew.setText(resourceMap.getString("mnuNew.text")); // NOI18N
        mnuNew.setName("mnuNew"); // NOI18N
        mnuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewActionPerformed(evt);
            }
        });
        fileMenu.add(mnuNew);

        cmdOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        cmdOpen.setIcon(resourceMap.getIcon("cmdOpen.icon")); // NOI18N
        cmdOpen.setText(resourceMap.getString("cmdOpen.text")); // NOI18N
        cmdOpen.setName("cmdOpen"); // NOI18N
        cmdOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOpenActionPerformed(evt);
            }
        });
        fileMenu.add(cmdOpen);

        mnuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnuSave.setIcon(resourceMap.getIcon("mnuSave.icon")); // NOI18N
        mnuSave.setText(resourceMap.getString("mnuSave.text")); // NOI18N
        mnuSave.setName("mnuSave"); // NOI18N
        mnuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveActionPerformed(evt);
            }
        });
        fileMenu.add(mnuSave);

        mnuSaveAs.setIcon(resourceMap.getIcon("mnuSaveAs.icon")); // NOI18N
        mnuSaveAs.setText(resourceMap.getString("mnuSaveAs.text")); // NOI18N
        mnuSaveAs.setName("mnuSaveAs"); // NOI18N
        mnuSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveAsActionPerformed(evt);
            }
        });
        fileMenu.add(mnuSaveAs);

        mnuExport.setIcon(resourceMap.getIcon("mnuExport.icon")); // NOI18N
        mnuExport.setText(resourceMap.getString("mnuExport.text")); // NOI18N
        mnuExport.setName("mnuExport"); // NOI18N
        mnuExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExportActionPerformed(evt);
            }
        });
        fileMenu.add(mnuExport);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(MusicwriterView.class, this);
        mnuPrint.setAction(actionMap.get("imprimer")); // NOI18N
        mnuPrint.setIcon(resourceMap.getIcon("mnuPrint.icon")); // NOI18N
        mnuPrint.setText(resourceMap.getString("mnuPrint.text")); // NOI18N
        mnuPrint.setName("mnuPrint"); // NOI18N
        fileMenu.add(mnuPrint);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        mnuAnnuler.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        mnuAnnuler.setIcon(resourceMap.getIcon("mnuAnnuler.icon")); // NOI18N
        mnuAnnuler.setText(resourceMap.getString("mnuAnnuler.text")); // NOI18N
        mnuAnnuler.setName("mnuAnnuler"); // NOI18N
        mnuAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAnnulerActionPerformed(evt);
            }
        });
        mnuAnnuler.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mnuAnnulerKeyReleased(evt);
            }
        });
        jMenu2.add(mnuAnnuler);

        mnuRefaire.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        mnuRefaire.setIcon(resourceMap.getIcon("mnuRefaire.icon")); // NOI18N
        mnuRefaire.setText(resourceMap.getString("mnuRefaire.text")); // NOI18N
        mnuRefaire.setName("mnuRefaire"); // NOI18N
        mnuRefaire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRefaireActionPerformed(evt);
            }
        });
        jMenu2.add(mnuRefaire);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jMenu2.add(jSeparator1);

        mnuCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        mnuCut.setIcon(resourceMap.getIcon("mnuCut.icon")); // NOI18N
        mnuCut.setText(resourceMap.getString("mnuCut.text")); // NOI18N
        mnuCut.setName("mnuCut"); // NOI18N
        mnuCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCutActionPerformed(evt);
            }
        });
        jMenu2.add(mnuCut);

        mnuCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mnuCopy.setIcon(resourceMap.getIcon("mnuCopy.icon")); // NOI18N
        mnuCopy.setText(resourceMap.getString("mnuCopy.text")); // NOI18N
        mnuCopy.setName("mnuCopy"); // NOI18N
        mnuCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCopyActionPerformed(evt);
            }
        });
        jMenu2.add(mnuCopy);

        mnuPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        mnuPaste.setIcon(resourceMap.getIcon("mnuPaste.icon")); // NOI18N
        mnuPaste.setText(resourceMap.getString("mnuPaste.text")); // NOI18N
        mnuPaste.setName("mnuPaste"); // NOI18N
        mnuPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPasteActionPerformed(evt);
            }
        });
        jMenu2.add(mnuPaste);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jMenu2.add(jSeparator3);

        mnuEditionSupprimer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        mnuEditionSupprimer.setIcon(resourceMap.getIcon("mnuEditionSupprimer.icon")); // NOI18N
        mnuEditionSupprimer.setText(resourceMap.getString("mnuEditionSupprimer.text")); // NOI18N
        mnuEditionSupprimer.setName("mnuEditionSupprimer"); // NOI18N
        mnuEditionSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEditionSupprimerActionPerformed(evt);
            }
        });
        jMenu2.add(mnuEditionSupprimer);

        mnuSelection.setText(resourceMap.getString("mnuSelection.text")); // NOI18N
        mnuSelection.setName("mnuSelection"); // NOI18N

        mnuSelectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        mnuSelectAll.setText(resourceMap.getString("mnuSelectAll.text")); // NOI18N
        mnuSelectAll.setName("mnuSelectAll"); // NOI18N
        mnuSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSelectAllActionPerformed(evt);
            }
        });
        mnuSelection.add(mnuSelectAll);

        mnuDeselectAll.setAction(actionMap.get("selectionDeselectionnerTout")); // NOI18N
        mnuDeselectAll.setText(resourceMap.getString("mnuDeselectAll.text")); // NOI18N
        mnuDeselectAll.setName("mnuDeselectAll"); // NOI18N
        mnuDeselectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDeselectAllActionPerformed(evt);
            }
        });
        mnuSelection.add(mnuDeselectAll);

        mnuSelectionMesure.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK));
        mnuSelectionMesure.setText(resourceMap.getString("mnuSelectionMesure.text")); // NOI18N
        mnuSelectionMesure.setName("mnuSelectionMesure"); // NOI18N
        mnuSelectionMesure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSelectionMesureActionPerformed(evt);
            }
        });
        mnuSelection.add(mnuSelectionMesure);

        jMenu2.add(mnuSelection);

        menuBar.add(jMenu2);

        mnuInsert.setText(resourceMap.getString("mnuInsert.text")); // NOI18N
        mnuInsert.setName("mnuInsert"); // NOI18N

        mnuNote.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        mnuNote.setText(resourceMap.getString("mnuNote.text")); // NOI18N
        mnuNote.setName("mnuNote"); // NOI18N
        mnuInsert.add(mnuNote);

        mnuSilence.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        mnuSilence.setText(resourceMap.getString("mnuSilence.text")); // NOI18N
        mnuSilence.setName("mnuSilence"); // NOI18N
        mnuInsert.add(mnuSilence);

        mnuInsererBarreDeMesure.setAction(actionMap.get("actionInsertMeasureBar")); // NOI18N
        mnuInsererBarreDeMesure.setText(resourceMap.getString("mnuInsererBarreDeMesure.text")); // NOI18N
        mnuInsererBarreDeMesure.setName("mnuInsererBarreDeMesure"); // NOI18N
        mnuInsert.add(mnuInsererBarreDeMesure);

        mnuParoles.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mnuParoles.setText(resourceMap.getString("mnuParoles.text")); // NOI18N
        mnuParoles.setName("mnuParoles"); // NOI18N
        mnuParoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuParolesActionPerformed(evt);
            }
        });
        mnuInsert.add(mnuParoles);

        menuBar.add(mnuInsert);

        mnuInstruments.setText(resourceMap.getString("mnuInstruments.text")); // NOI18N
        mnuInstruments.setName("mnuInstruments"); // NOI18N

        mnuInstrumentationChange.setIcon(resourceMap.getIcon("mnuInstrumentationChange.icon")); // NOI18N
        mnuInstrumentationChange.setText(resourceMap.getString("mnuInstrumentationChange.text")); // NOI18N
        mnuInstrumentationChange.setName("mnuInstrumentationChange"); // NOI18N
        mnuInstrumentationChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuInstrumentationChangeActionPerformed(evt);
            }
        });
        mnuInstruments.add(mnuInstrumentationChange);

        mnuAffichagePartie.setAction(actionMap.get("afficherPartie")); // NOI18N
        mnuAffichagePartie.setIcon(resourceMap.getIcon("mnuAffichagePartie.icon")); // NOI18N
        mnuAffichagePartie.setText(resourceMap.getString("mnuAffichagePartie.text")); // NOI18N
        mnuAffichagePartie.setName("mnuAffichagePartie"); // NOI18N
        mnuAffichagePartie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAffichagePartieActionPerformed(evt);
            }
        });
        mnuInstruments.add(mnuAffichagePartie);

        mnuAffichageToutConducteur.setAction(actionMap.get("afficherConducteur")); // NOI18N
        mnuAffichageToutConducteur.setIcon(resourceMap.getIcon("mnuAffichageToutConducteur.icon")); // NOI18N
        mnuAffichageToutConducteur.setText(resourceMap.getString("mnuAffichageToutConducteur.text")); // NOI18N
        mnuAffichageToutConducteur.setName("mnuAffichageToutConducteur"); // NOI18N
        mnuAffichageToutConducteur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAffichageToutConducteurActionPerformed(evt);
            }
        });
        mnuInstruments.add(mnuAffichageToutConducteur);

        menuBar.add(mnuInstruments);

        mnuDisplay.setText(resourceMap.getString("mnuDisplay.text")); // NOI18N
        mnuDisplay.setName("mnuDisplay"); // NOI18N

        mnuAffichagePartie1.setAction(actionMap.get("afficherPartie")); // NOI18N
        mnuAffichagePartie1.setIcon(resourceMap.getIcon("mnuAffichagePartie1.icon")); // NOI18N
        mnuAffichagePartie1.setText(resourceMap.getString("mnuAffichagePartie1.text")); // NOI18N
        mnuAffichagePartie1.setName("mnuAffichagePartie1"); // NOI18N
        mnuAffichagePartie1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAffichagePartie1ActionPerformed(evt);
            }
        });
        mnuDisplay.add(mnuAffichagePartie1);

        mnuAffichageToutConducteur1.setAction(actionMap.get("afficherConducteur")); // NOI18N
        mnuAffichageToutConducteur1.setIcon(resourceMap.getIcon("mnuAffichageToutConducteur1.icon")); // NOI18N
        mnuAffichageToutConducteur1.setText(resourceMap.getString("mnuAffichageToutConducteur1.text")); // NOI18N
        mnuAffichageToutConducteur1.setName("mnuAffichageToutConducteur1"); // NOI18N
        mnuAffichageToutConducteur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAffichageToutConducteur1ActionPerformed(evt);
            }
        });
        mnuDisplay.add(mnuAffichageToutConducteur1);

        mnuZoom.setText(resourceMap.getString("mnuZoom.text")); // NOI18N
        mnuZoom.setName("mnuZoom"); // NOI18N

        mnuZoom50.setText(resourceMap.getString("mnuZoom50.text")); // NOI18N
        mnuZoom50.setName("mnuZoom50"); // NOI18N
        mnuZoom50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuZoom50ActionPerformed(evt);
            }
        });
        mnuZoom.add(mnuZoom50);

        mnuZoom100.setText(resourceMap.getString("mnuZoom100.text")); // NOI18N
        mnuZoom100.setName("mnuZoom100"); // NOI18N
        mnuZoom100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuZoom100ActionPerformed(evt);
            }
        });
        mnuZoom.add(mnuZoom100);

        mnuZoom200.setText(resourceMap.getString("mnuZoom200.text")); // NOI18N
        mnuZoom200.setName("mnuZoom200"); // NOI18N
        mnuZoom200.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuZoom200ActionPerformed(evt);
            }
        });
        mnuZoom.add(mnuZoom200);

        mnuDisplay.add(mnuZoom);

        menuBar.add(mnuDisplay);

        jMenuNoteDuree.setText(resourceMap.getString("jMenuNoteDuree.text")); // NOI18N
        jMenuNoteDuree.setName("jMenuNoteDuree"); // NOI18N

        mnuTripleCroche.setIcon(resourceMap.getIcon("mnuTripleCroche.icon")); // NOI18N
        mnuTripleCroche.setText(resourceMap.getString("mnuTripleCroche.text")); // NOI18N
        mnuTripleCroche.setName("mnuTripleCroche"); // NOI18N
        mnuTripleCroche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTripleCrocheActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(mnuTripleCroche);

        mnuDoubleCroche.setIcon(resourceMap.getIcon("mnuDoubleCroche.icon")); // NOI18N
        mnuDoubleCroche.setText(resourceMap.getString("mnuDoubleCroche.text")); // NOI18N
        mnuDoubleCroche.setName("mnuDoubleCroche"); // NOI18N
        mnuDoubleCroche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDoubleCrocheActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(mnuDoubleCroche);

        jMenuItemCroche.setIcon(resourceMap.getIcon("jMenuItemCroche.icon")); // NOI18N
        jMenuItemCroche.setText(resourceMap.getString("jMenuItemCroche.text")); // NOI18N
        jMenuItemCroche.setName("jMenuItemCroche"); // NOI18N
        jMenuItemCroche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCrocheActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(jMenuItemCroche);

        jMenuItemNoire.setIcon(resourceMap.getIcon("jMenuItemNoire.icon")); // NOI18N
        jMenuItemNoire.setText(resourceMap.getString("jMenuItemNoire.text")); // NOI18N
        jMenuItemNoire.setName("jMenuItemNoire"); // NOI18N
        jMenuItemNoire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNoireActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(jMenuItemNoire);

        jMenuItemBlanche.setIcon(resourceMap.getIcon("jMenuItemBlanche.icon")); // NOI18N
        jMenuItemBlanche.setText(resourceMap.getString("jMenuItemBlanche.text")); // NOI18N
        jMenuItemBlanche.setName("jMenuItemBlanche"); // NOI18N
        jMenuItemBlanche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBlancheActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(jMenuItemBlanche);

        mnuRonde.setIcon(resourceMap.getIcon("mnuRonde.icon")); // NOI18N
        mnuRonde.setText(resourceMap.getString("mnuRonde.text")); // NOI18N
        mnuRonde.setName("mnuRonde"); // NOI18N
        mnuRonde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRondeActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(mnuRonde);

        mnuSelectionEtirerDoubler.setIcon(resourceMap.getIcon("mnuSelectionEtirerDoubler.icon")); // NOI18N
        mnuSelectionEtirerDoubler.setText(resourceMap.getString("mnuSelectionEtirerDoubler.text")); // NOI18N
        mnuSelectionEtirerDoubler.setName("mnuSelectionEtirerDoubler"); // NOI18N
        mnuSelectionEtirerDoubler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSelectionEtirerDoublerActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(mnuSelectionEtirerDoubler);

        mnuSelectionDureeDiviserParDeux.setIcon(resourceMap.getIcon("mnuSelectionDureeDiviserParDeux.icon")); // NOI18N
        mnuSelectionDureeDiviserParDeux.setText(resourceMap.getString("mnuSelectionDureeDiviserParDeux.text")); // NOI18N
        mnuSelectionDureeDiviserParDeux.setName("mnuSelectionDureeDiviserParDeux"); // NOI18N
        mnuSelectionDureeDiviserParDeux.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSelectionDureeDiviserParDeuxActionPerformed(evt);
            }
        });
        jMenuNoteDuree.add(mnuSelectionDureeDiviserParDeux);

        menuBar.add(jMenuNoteDuree);

        mnuAlteration.setText(resourceMap.getString("mnuAlteration.text")); // NOI18N
        mnuAlteration.setName("mnuAlteration"); // NOI18N

        mnuAlterationDoubleBemol.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, 0));
        mnuAlterationDoubleBemol.setIcon(resourceMap.getIcon("mnuAlterationDoubleBemol.icon")); // NOI18N
        mnuAlterationDoubleBemol.setText(resourceMap.getString("mnuAlterationDoubleBemol.text")); // NOI18N
        mnuAlterationDoubleBemol.setName("mnuAlterationDoubleBemol"); // NOI18N
        mnuAlterationDoubleBemol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationDoubleBemolActionPerformed(evt);
            }
        });
        mnuAlterationDoubleBemol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuAlterationDoubleBemolKeyPressed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationDoubleBemol);

        mnuAlterationBemol.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, 0));
        mnuAlterationBemol.setIcon(resourceMap.getIcon("mnuAlterationBemol.icon")); // NOI18N
        mnuAlterationBemol.setText(resourceMap.getString("mnuAlterationBemol.text")); // NOI18N
        mnuAlterationBemol.setName("mnuAlterationBemol"); // NOI18N
        mnuAlterationBemol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationBemolActionPerformed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationBemol);

        mnuAlterationBecarre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, 0));
        mnuAlterationBecarre.setIcon(resourceMap.getIcon("mnuAlterationBecarre.icon")); // NOI18N
        mnuAlterationBecarre.setText(resourceMap.getString("mnuAlterationBecarre.text")); // NOI18N
        mnuAlterationBecarre.setName("mnuAlterationBecarre"); // NOI18N
        mnuAlterationBecarre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationBecarreActionPerformed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationBecarre);

        mnuAlterationDiese.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, 0));
        mnuAlterationDiese.setIcon(resourceMap.getIcon("mnuAlterationDiese.icon")); // NOI18N
        mnuAlterationDiese.setText(resourceMap.getString("mnuAlterationDiese.text")); // NOI18N
        mnuAlterationDiese.setName("mnuAlterationDiese"); // NOI18N
        mnuAlterationDiese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationDieseActionPerformed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationDiese);

        mnuAlterationDoubleDiese.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, 0));
        mnuAlterationDoubleDiese.setIcon(resourceMap.getIcon("mnuAlterationDoubleDiese.icon")); // NOI18N
        mnuAlterationDoubleDiese.setText(resourceMap.getString("mnuAlterationDoubleDiese.text")); // NOI18N
        mnuAlterationDoubleDiese.setName("mnuAlterationDoubleDiese"); // NOI18N
        mnuAlterationDoubleDiese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationDoubleDieseActionPerformed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationDoubleDiese);

        jSeparator2.setName("jSeparator2"); // NOI18N
        mnuAlteration.add(jSeparator2);

        mnuAlterationEnharmonique.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mnuAlterationEnharmonique.setText(resourceMap.getString("mnuAlterationEnharmonique.text")); // NOI18N
        mnuAlterationEnharmonique.setName("mnuAlterationEnharmonique"); // NOI18N
        mnuAlterationEnharmonique.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationEnharmoniqueActionPerformed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationEnharmonique);

        mnuAlterationArmature.setText(resourceMap.getString("mnuAlterationArmature.text")); // NOI18N
        mnuAlterationArmature.setName("mnuAlterationArmature"); // NOI18N
        mnuAlterationArmature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationArmatureActionPerformed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationArmature);

        mnuAlterationViaTonalite.setText(resourceMap.getString("mnuAlterationViaTonalite.text")); // NOI18N
        mnuAlterationViaTonalite.setName("mnuAlterationViaTonalite"); // NOI18N
        mnuAlterationViaTonalite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAlterationViaTonaliteActionPerformed(evt);
            }
        });
        mnuAlteration.add(mnuAlterationViaTonalite);

        menuBar.add(mnuAlteration);

        mnuVoix.setText(resourceMap.getString("mnuVoix.text")); // NOI18N
        mnuVoix.setName("mnuVoix"); // NOI18N

        mnuNouvelleVoix.setIcon(resourceMap.getIcon("mnuNouvelleVoix.icon")); // NOI18N
        mnuNouvelleVoix.setText(resourceMap.getString("mnuNouvelleVoix.text")); // NOI18N
        mnuNouvelleVoix.setName("mnuNouvelleVoix"); // NOI18N
        mnuNouvelleVoix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNouvelleVoixActionPerformed(evt);
            }
        });
        mnuVoix.add(mnuNouvelleVoix);

        mnuVoiceSameVoice.setText(resourceMap.getString("mnuVoiceSameVoice.text")); // NOI18N
        mnuVoiceSameVoice.setName("mnuVoiceSameVoice"); // NOI18N
        mnuVoix.add(mnuVoiceSameVoice);

        mnuVoixNotesIndependantes.setIcon(resourceMap.getIcon("mnuVoixNotesIndependantes.icon")); // NOI18N
        mnuVoixNotesIndependantes.setText(resourceMap.getString("mnuVoixNotesIndependantes.text")); // NOI18N
        mnuVoixNotesIndependantes.setName("mnuVoixNotesIndependantes"); // NOI18N
        mnuVoixNotesIndependantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVoixNotesIndependantesActionPerformed(evt);
            }
        });
        mnuVoix.add(mnuVoixNotesIndependantes);

        menuBar.add(mnuVoix);

        mnuHarmonie.setText(resourceMap.getString("mnuHarmonie.text")); // NOI18N
        mnuHarmonie.setName("mnuHarmonie"); // NOI18N

        mnuTransposer.setAction(actionMap.get("actionTranspose")); // NOI18N
        mnuTransposer.setText(resourceMap.getString("mnuTransposer.text")); // NOI18N
        mnuTransposer.setName("mnuTransposer"); // NOI18N
        mnuHarmonie.add(mnuTransposer);

        mnuIntervalAdd.setText(resourceMap.getString("mnuIntervalAdd.text")); // NOI18N
        mnuIntervalAdd.setName("mnuIntervalAdd"); // NOI18N

        mnuIntervalAdd1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.ALT_MASK));
        mnuIntervalAdd1.setText(resourceMap.getString("mnuIntervalAdd1.text")); // NOI18N
        mnuIntervalAdd1.setName("mnuIntervalAdd1"); // NOI18N
        mnuIntervalAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAdd1);

        mnuIntervalAdd2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.ALT_MASK));
        mnuIntervalAdd2.setText(resourceMap.getString("mnuIntervalAdd2.text")); // NOI18N
        mnuIntervalAdd2.setName("mnuIntervalAdd2"); // NOI18N
        mnuIntervalAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAdd2);

        mnuIntervalAdd4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.ALT_MASK));
        mnuIntervalAdd4.setText(resourceMap.getString("mnuIntervalAdd4.text")); // NOI18N
        mnuIntervalAdd4.setName("mnuIntervalAdd4"); // NOI18N
        mnuIntervalAdd4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAdd4);

        mnuIntervalAdd5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.ALT_MASK));
        mnuIntervalAdd5.setText(resourceMap.getString("mnuIntervalAdd5.text")); // NOI18N
        mnuIntervalAdd5.setName("mnuIntervalAdd5"); // NOI18N
        mnuIntervalAdd5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAdd5);

        mnuIntervalAdd6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.InputEvent.ALT_MASK));
        mnuIntervalAdd6.setText(resourceMap.getString("mnuIntervalAdd6.text")); // NOI18N
        mnuIntervalAdd6.setName("mnuIntervalAdd6"); // NOI18N
        mnuIntervalAdd6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAdd6);

        mnuIntervalAdd7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.InputEvent.ALT_MASK));
        mnuIntervalAdd7.setText(resourceMap.getString("mnuIntervalAdd7.text")); // NOI18N
        mnuIntervalAdd7.setName("mnuIntervalAdd7"); // NOI18N
        mnuIntervalAdd7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAdd7);

        mnuIntervalAdd8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.InputEvent.ALT_MASK));
        mnuIntervalAdd8.setText(resourceMap.getString("mnuIntervalAdd8.text")); // NOI18N
        mnuIntervalAdd8.setName("mnuIntervalAdd8"); // NOI18N
        mnuIntervalAdd8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAdd8);

        jSeparator17.setName("jSeparator17"); // NOI18N
        mnuIntervalAdd.add(jSeparator17);

        mnuIntervalAddM2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.SHIFT_MASK));
        mnuIntervalAddM2.setText(resourceMap.getString("mnuIntervalAddM2.text")); // NOI18N
        mnuIntervalAddM2.setName("mnuIntervalAddM2"); // NOI18N
        mnuIntervalAddM2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAddM2);

        mnuIntervalAddM3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.SHIFT_MASK));
        mnuIntervalAddM3.setText(resourceMap.getString("mnuIntervalAddM3.text")); // NOI18N
        mnuIntervalAddM3.setName("mnuIntervalAddM3"); // NOI18N
        mnuIntervalAddM3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAddM3);

        mnuIntervalAddM4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.SHIFT_MASK));
        mnuIntervalAddM4.setText(resourceMap.getString("mnuIntervalAddM4.text")); // NOI18N
        mnuIntervalAddM4.setName("mnuIntervalAddM4"); // NOI18N
        mnuIntervalAddM4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAddM4);

        mnuIntervalAddM5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.SHIFT_MASK));
        mnuIntervalAddM5.setText(resourceMap.getString("mnuIntervalAddM5.text")); // NOI18N
        mnuIntervalAddM5.setName("mnuIntervalAddM5"); // NOI18N
        mnuIntervalAddM5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAddM5);

        mnuIntervalAddM6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.InputEvent.SHIFT_MASK));
        mnuIntervalAddM6.setText(resourceMap.getString("mnuIntervalAddM6.text")); // NOI18N
        mnuIntervalAddM6.setName("mnuIntervalAddM6"); // NOI18N
        mnuIntervalAddM6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAddM6);

        mnuIntervalAddM7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.InputEvent.SHIFT_MASK));
        mnuIntervalAddM7.setText(resourceMap.getString("mnuIntervalAddM7.text")); // NOI18N
        mnuIntervalAddM7.setName("mnuIntervalAddM7"); // NOI18N
        mnuIntervalAddM7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAddM7);

        mnuIntervalAddM8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.InputEvent.SHIFT_MASK));
        mnuIntervalAddM8.setText(resourceMap.getString("mnuIntervalAddM8.text")); // NOI18N
        mnuIntervalAddM8.setName("mnuIntervalAddM8"); // NOI18N
        mnuIntervalAddM8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIntervalAdd1ActionPerformed(evt);
            }
        });
        mnuIntervalAdd.add(mnuIntervalAddM8);

        mnuHarmonie.add(mnuIntervalAdd);

        menuBar.add(mnuHarmonie);

        jMenu5.setText(resourceMap.getString("jMenu5.text")); // NOI18N
        jMenu5.setName("jMenu5"); // NOI18N

        mnuConfiguration.setIcon(resourceMap.getIcon("mnuConfiguration.icon")); // NOI18N
        mnuConfiguration.setText(resourceMap.getString("mnuConfiguration.text")); // NOI18N
        mnuConfiguration.setName("mnuConfiguration"); // NOI18N
        mnuConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigurationActionPerformed(evt);
            }
        });
        jMenu5.add(mnuConfiguration);

        mnuMicrophoneEnregistrer.setText(resourceMap.getString("mnuMicrophoneEnregistrer.text")); // NOI18N
        mnuMicrophoneEnregistrer.setName("mnuMicrophoneEnregistrer"); // NOI18N
        mnuMicrophoneEnregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMicrophoneEnregistrerActionPerformed(evt);
            }
        });
        jMenu5.add(mnuMicrophoneEnregistrer);

        mnuMicrophoneStop.setText(resourceMap.getString("mnuMicrophoneStop.text")); // NOI18N
        mnuMicrophoneStop.setName("mnuMicrophoneStop"); // NOI18N
        mnuMicrophoneStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMicrophoneStopActionPerformed(evt);
            }
        });
        jMenu5.add(mnuMicrophoneStop);

        menuBar.add(jMenu5);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        jMenuItem28.setText(resourceMap.getString("jMenuItem28.text")); // NOI18N
        jMenuItem28.setName("jMenuItem28"); // NOI18N
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem28);

        jMenuItem29.setText(resourceMap.getString("jMenuItem29.text")); // NOI18N
        jMenuItem29.setName("jMenuItem29"); // NOI18N
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem29);

        mnuSendComments.setText(resourceMap.getString("mnuSendComments.text")); // NOI18N
        mnuSendComments.setName("mnuSendComments"); // NOI18N
        mnuSendComments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSendCommentsActionPerformed(evt);
            }
        });
        helpMenu.add(mnuSendComments);

        jMenuItem9.setText(resourceMap.getString("jMenuItem9.text")); // NOI18N
        jMenuItem9.setName("jMenuItem9"); // NOI18N
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem9);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

		languageMenuSet(helpMenu);
		
        jMenuItem8.setIcon(resourceMap.getIcon("jMenuItem8.icon")); // NOI18N
        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text")); // NOI18N
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem8);

        jSeparator16.setName("jSeparator16"); // NOI18N
        helpMenu.add(jSeparator16);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        mnuRepaint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mnuRepaint.setText(resourceMap.getString("mnuRepaint.text")); // NOI18N
        mnuRepaint.setName("mnuRepaint"); // NOI18N
        mnuRepaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRepaintActionPerformed(evt);
            }
        });
        jMenu1.add(mnuRepaint);

        mnuDebugAllSameVoice.setText(resourceMap.getString("mnuDebugAllSameVoice.text")); // NOI18N
        mnuDebugAllSameVoice.setName("mnuDebugAllSameVoice"); // NOI18N
        mnuDebugAllSameVoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDebugAllSameVoiceActionPerformed(evt);
            }
        });
        jMenu1.add(mnuDebugAllSameVoice);

        helpMenu.add(jMenu1);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                statusPanelKeyTyped(evt);
            }
        });

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 407, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        panelGeneral.setName("panelGeneral"); // NOI18N
        panelGeneral.setLayout(new javax.swing.BoxLayout(panelGeneral, javax.swing.BoxLayout.LINE_AXIS));

        panelTravail.setName("panelTravail"); // NOI18N
        panelTravail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                panelTravailKeyPressed(evt);
            }
        });
        panelTravail.setLayout(new javax.swing.BoxLayout(panelTravail, javax.swing.BoxLayout.Y_AXIS));

        panBarreOutils.setMaximumSize(new java.awt.Dimension(2147483647, 41));
        panBarreOutils.setMinimumSize(new java.awt.Dimension(531, 41));
        panBarreOutils.setName("panBarreOutils"); // NOI18N
        panBarreOutils.setPreferredSize(new java.awt.Dimension(1407, 41));
        panBarreOutils.setLayout(new java.awt.CardLayout());

        panelHelpStylet.setMaximumSize(new java.awt.Dimension(597, 32));
        panelHelpStylet.setName("panelHelpStylet"); // NOI18N
        panelHelpStylet.setPreferredSize(new java.awt.Dimension(597, 32));
        panBarreOutils.add(panelHelpStylet, "modeStylet");

        toolBarEcriture.setFloatable(false);
        toolBarEcriture.setRollover(true);
        toolBarEcriture.setFont(resourceMap.getFont("toolBarEcriture.font")); // NOI18N
        toolBarEcriture.setName("toolBarEcriture"); // NOI18N

        boutonOuvrir.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonOuvrir.setIcon(resourceMap.getIcon("boutonOuvrir.icon")); // NOI18N
        boutonOuvrir.setText(resourceMap.getString("boutonOuvrir.text")); // NOI18N
        boutonOuvrir.setToolTipText(resourceMap.getString("boutonOuvrir.toolTipText")); // NOI18N
        boutonOuvrir.setFocusable(false);
        boutonOuvrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonOuvrir.setName("boutonOuvrir"); // NOI18N
        boutonOuvrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonOuvrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonOuvrirActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonOuvrir);

        boutonSauvegarder.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonSauvegarder.setIcon(resourceMap.getIcon("boutonSauvegarder.icon")); // NOI18N
        boutonSauvegarder.setText(resourceMap.getString("boutonSauvegarder.text")); // NOI18N
        boutonSauvegarder.setToolTipText(resourceMap.getString("boutonSauvegarder.toolTipText")); // NOI18N
        boutonSauvegarder.setFocusable(false);
        boutonSauvegarder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonSauvegarder.setName("boutonSauvegarder"); // NOI18N
        boutonSauvegarder.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonSauvegarder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonSauvegarderActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonSauvegarder);

        boutonImprimer.setAction(actionMap.get("imprimer")); // NOI18N
        boutonImprimer.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonImprimer.setIcon(resourceMap.getIcon("boutonImprimer.icon")); // NOI18N
        boutonImprimer.setText(resourceMap.getString("boutonImprimer.text")); // NOI18N
        boutonImprimer.setToolTipText(resourceMap.getString("boutonImprimer.toolTipText")); // NOI18N
        boutonImprimer.setFocusable(false);
        boutonImprimer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonImprimer.setName("boutonImprimer"); // NOI18N
        boutonImprimer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonImprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonImprimerActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonImprimer);

        jSeparator12.setName("jSeparator12"); // NOI18N
        toolBarEcriture.add(jSeparator12);

        boutonAnnuler.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonAnnuler.setIcon(resourceMap.getIcon("boutonAnnuler.icon")); // NOI18N
        boutonAnnuler.setText(resourceMap.getString("boutonAnnuler.text")); // NOI18N
        boutonAnnuler.setToolTipText(resourceMap.getString("boutonAnnuler.toolTipText")); // NOI18N
        boutonAnnuler.setFocusable(false);
        boutonAnnuler.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonAnnuler.setName("boutonAnnuler"); // NOI18N
        boutonAnnuler.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonAnnulerActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonAnnuler);

        boutonRefaire.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonRefaire.setIcon(resourceMap.getIcon("boutonRefaire.icon")); // NOI18N
        boutonRefaire.setText(resourceMap.getString("boutonRefaire.text")); // NOI18N
        boutonRefaire.setToolTipText(resourceMap.getString("boutonRefaire.toolTipText")); // NOI18N
        boutonRefaire.setFocusable(false);
        boutonRefaire.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonRefaire.setName("boutonRefaire"); // NOI18N
        boutonRefaire.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonRefaire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonRefaireActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonRefaire);

        jSeparator13.setName("jSeparator13"); // NOI18N
        toolBarEcriture.add(jSeparator13);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        toolBarEcriture.add(jPanel2);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));
        toolBarEcriture.add(jPanel1);

        boutonEntreeMIDIEnregistrer.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonEntreeMIDIEnregistrer.setIcon(resourceMap.getIcon("boutonEntreeMIDIEnregistrer.icon")); // NOI18N
        boutonEntreeMIDIEnregistrer.setText(resourceMap.getString("boutonEntreeMIDIEnregistrer.text")); // NOI18N
        boutonEntreeMIDIEnregistrer.setToolTipText(resourceMap.getString("boutonEntreeMIDIEnregistrer.toolTipText")); // NOI18N
        boutonEntreeMIDIEnregistrer.setFocusable(false);
        boutonEntreeMIDIEnregistrer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonEntreeMIDIEnregistrer.setName("boutonEntreeMIDIEnregistrer"); // NOI18N
        boutonEntreeMIDIEnregistrer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonEntreeMIDIEnregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonEntreeMIDIEnregistrerActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonEntreeMIDIEnregistrer);

        boutonSortieMIDILecture.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonSortieMIDILecture.setIcon(resourceMap.getIcon("boutonSortieMIDILecture.icon")); // NOI18N
        boutonSortieMIDILecture.setText(resourceMap.getString("boutonSortieMIDILecture.text")); // NOI18N
        boutonSortieMIDILecture.setToolTipText(resourceMap.getString("boutonSortieMIDILecture.toolTipText")); // NOI18N
        boutonSortieMIDILecture.setFocusable(false);
        boutonSortieMIDILecture.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonSortieMIDILecture.setName("boutonSortieMIDILecture"); // NOI18N
        boutonSortieMIDILecture.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonSortieMIDILecture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonSortieMIDILectureActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonSortieMIDILecture);

        boutonPause.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonPause.setIcon(resourceMap.getIcon("boutonPause.icon")); // NOI18N
        boutonPause.setText(resourceMap.getString("boutonPause.text")); // NOI18N
        boutonPause.setToolTipText(resourceMap.getString("boutonPause.toolTipText")); // NOI18N
        boutonPause.setFocusable(false);
        boutonPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonPause.setName("boutonPause"); // NOI18N
        boutonPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonPauseActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonPause);

        boutonMIDIStop.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonMIDIStop.setIcon(resourceMap.getIcon("boutonMIDIStop.icon")); // NOI18N
        boutonMIDIStop.setText(resourceMap.getString("boutonMIDIStop.text")); // NOI18N
        boutonMIDIStop.setToolTipText(resourceMap.getString("boutonMIDIStop.toolTipText")); // NOI18N
        boutonMIDIStop.setFocusable(false);
        boutonMIDIStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonMIDIStop.setName("boutonMIDIStop"); // NOI18N
        boutonMIDIStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonMIDIStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonMIDIStopActionPerformed(evt);
                boutonEntreeMIDIStopActionPerformed(evt);
            }
        });
        toolBarEcriture.add(boutonMIDIStop);

        panBarreOutils.add(toolBarEcriture, "modeecriture");

        toolBarLecture.setFloatable(false);
        toolBarLecture.setRollover(true);
        toolBarLecture.setName("toolBarLecture"); // NOI18N

        boutonSortieMIDIReprendre.setFont(resourceMap.getFont("boutonRefaire.font")); // NOI18N
        boutonSortieMIDIReprendre.setIcon(resourceMap.getIcon("boutonSortieMIDIReprendre.icon")); // NOI18N
        boutonSortieMIDIReprendre.setText(resourceMap.getString("boutonSortieMIDIReprendre.text")); // NOI18N
        boutonSortieMIDIReprendre.setToolTipText(resourceMap.getString("boutonSortieMIDIReprendre.toolTipText")); // NOI18N
        boutonSortieMIDIReprendre.setFocusable(false);
        boutonSortieMIDIReprendre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonSortieMIDIReprendre.setName("boutonSortieMIDIReprendre"); // NOI18N
        boutonSortieMIDIReprendre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonSortieMIDIReprendre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonSortieMIDIReprendreActionPerformed(evt);
            }
        });
        toolBarLecture.add(boutonSortieMIDIReprendre);

        boutonPause1.setFont(resourceMap.getFont("boutonPause1.font")); // NOI18N
        boutonPause1.setIcon(resourceMap.getIcon("boutonPause1.icon")); // NOI18N
        boutonPause1.setText(resourceMap.getString("boutonPause1.text")); // NOI18N
        boutonPause1.setToolTipText(resourceMap.getString("boutonPause1.toolTipText")); // NOI18N
        boutonPause1.setFocusable(false);
        boutonPause1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonPause1.setName("boutonPause1"); // NOI18N
        boutonPause1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonPause1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonPauseActionPerformed(evt);
            }
        });
        toolBarLecture.add(boutonPause1);

        boutonMIDIStop1.setFont(resourceMap.getFont("boutonMIDIStop1.font")); // NOI18N
        boutonMIDIStop1.setIcon(resourceMap.getIcon("boutonMIDIStop1.icon")); // NOI18N
        boutonMIDIStop1.setText(resourceMap.getString("boutonMIDIStop1.text")); // NOI18N
        boutonMIDIStop1.setToolTipText(resourceMap.getString("boutonMIDIStop1.toolTipText")); // NOI18N
        boutonMIDIStop1.setFocusable(false);
        boutonMIDIStop1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boutonMIDIStop1.setName("boutonMIDIStop1"); // NOI18N
        boutonMIDIStop1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boutonMIDIStop1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonMIDIStopActionPerformed(evt);
            }
        });
        toolBarLecture.add(boutonMIDIStop1);

        jSeparator11.setName("jSeparator11"); // NOI18N
        toolBarLecture.add(jSeparator11);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        toolBarLecture.add(jLabel1);

        jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N
        toolBarLecture.add(jLabel4);

        jSliderLectureVitesse.setMaximum(200);
        jSliderLectureVitesse.setMinimum(1);
        jSliderLectureVitesse.setValue(100);
        jSliderLectureVitesse.setMaximumSize(new java.awt.Dimension(150, 23));
        jSliderLectureVitesse.setName("jSliderLectureVitesse"); // NOI18N
        toolBarLecture.add(jSliderLectureVitesse);

        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        toolBarLecture.add(jLabel5);

        panBarreOutils.add(toolBarLecture, "modeLecture");

        toolBarSelection.setFloatable(false);
        toolBarSelection.setRollover(true);
        toolBarSelection.setMinimumSize(new java.awt.Dimension(782, 41));
        toolBarSelection.setName("toolBarSelection"); // NOI18N
        toolBarSelection.setPreferredSize(new java.awt.Dimension(800, 41));

        lblSelectionPlus.setBackground(resourceMap.getColor("lblSelectionPlus.background")); // NOI18N
        lblSelectionPlus.setIcon(resourceMap.getIcon("lblSelectionPlus.icon")); // NOI18N
        lblSelectionPlus.setText(resourceMap.getString("lblSelectionPlus.text")); // NOI18N
        lblSelectionPlus.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblSelectionPlus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectionPlus.setName("lblSelectionPlus"); // NOI18N
        lblSelectionPlus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarSelection.add(lblSelectionPlus);

        toolbarNotes.setFloatable(false);
        toolbarNotes.setRollover(true);
        toolbarNotes.setName("toolbarNotes"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N
        toolbarNotes.add(jSeparator4);

        cmdNoteDecorer.setAction(actionMap.get("actionNoteDecorerBoiteDeDialogue")); // NOI18N
        cmdNoteDecorer.setIcon(resourceMap.getIcon("cmdNoteDecorer.icon")); // NOI18N
        cmdNoteDecorer.setText(resourceMap.getString("cmdNoteDecorer.text")); // NOI18N
        cmdNoteDecorer.setFocusable(false);
        cmdNoteDecorer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cmdNoteDecorer.setName("cmdNoteDecorer"); // NOI18N
        toolbarNotes.add(cmdNoteDecorer);

        toolBarSelection.add(toolbarNotes);

        jSeparator9.setName("jSeparator9"); // NOI18N
        toolBarSelection.add(jSeparator9);

        cmdSelectionSupprimer.setAction(actionMap.get("actionSelectionSupprimer")); // NOI18N
        cmdSelectionSupprimer.setIcon(resourceMap.getIcon("cmdSelectionSupprimer.icon")); // NOI18N
        cmdSelectionSupprimer.setText(resourceMap.getString("cmdSelectionSupprimer.text")); // NOI18N
        cmdSelectionSupprimer.setToolTipText(resourceMap.getString("cmdSelectionSupprimer.toolTipText")); // NOI18N
        cmdSelectionSupprimer.setFocusable(false);
        cmdSelectionSupprimer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdSelectionSupprimer.setName("cmdSelectionSupprimer"); // NOI18N
        cmdSelectionSupprimer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarSelection.add(cmdSelectionSupprimer);

        jSeparator10.setName("jSeparator10"); // NOI18N
        toolBarSelection.add(jSeparator10);

        cmdSelectionArmure.setAction(actionMap.get("actionSelectionArmure")); // NOI18N
        cmdSelectionArmure.setIcon(resourceMap.getIcon("cmdSelectionArmure.icon")); // NOI18N
        cmdSelectionArmure.setText(resourceMap.getString("cmdSelectionArmure.text")); // NOI18N
        cmdSelectionArmure.setFocusable(false);
        cmdSelectionArmure.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cmdSelectionArmure.setName("cmdSelectionArmure"); // NOI18N
        toolBarSelection.add(cmdSelectionArmure);

        cmdSelectionMesureSignature.setAction(actionMap.get("actionSelectionMesureSignature")); // NOI18N
        cmdSelectionMesureSignature.setIcon(resourceMap.getIcon("cmdSelectionMesureSignature.icon")); // NOI18N
        cmdSelectionMesureSignature.setText(resourceMap.getString("cmdSelectionMesureSignature.text")); // NOI18N
        cmdSelectionMesureSignature.setFocusable(false);
        cmdSelectionMesureSignature.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cmdSelectionMesureSignature.setName("cmdSelectionMesureSignature"); // NOI18N
        toolBarSelection.add(cmdSelectionMesureSignature);

        cmdSelectionClef.setAction(actionMap.get("actionSelectionClef")); // NOI18N
        cmdSelectionClef.setIcon(resourceMap.getIcon("cmdSelectionClef.icon")); // NOI18N
        cmdSelectionClef.setText(resourceMap.getString("cmdSelectionClef.text")); // NOI18N
        cmdSelectionClef.setFocusable(false);
        cmdSelectionClef.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cmdSelectionClef.setName("cmdSelectionClef"); // NOI18N
        toolBarSelection.add(cmdSelectionClef);

        cmdSelectionTempo.setAction(actionMap.get("actionSelectionTempo")); // NOI18N
        cmdSelectionTempo.setIcon(resourceMap.getIcon("cmdSelectionTempo.icon")); // NOI18N
        cmdSelectionTempo.setText(resourceMap.getString("cmdSelectionTempo.text")); // NOI18N
        cmdSelectionTempo.setFocusable(false);
        cmdSelectionTempo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cmdSelectionTempo.setName("cmdSelectionTempo"); // NOI18N
        toolBarSelection.add(cmdSelectionTempo);

        panBarreOutils.add(toolBarSelection, "modeSelection");

        panelHelpNoteDeplacement.setName("panelHelpNoteDeplacement"); // NOI18N
        panBarreOutils.add(panelHelpNoteDeplacement, "modeSelectionDeplacementOuCopie");

        panelTravail.add(panBarreOutils);

        surfaceTravail.setName("surfaceTravail"); // NOI18N
        surfaceTravail.setLayout(new javax.swing.BoxLayout(surfaceTravail, javax.swing.BoxLayout.LINE_AXIS));

        palettePanel.setMaximumSize(new java.awt.Dimension(128, 2147483647));
        palettePanel.setMinimumSize(new java.awt.Dimension(128, 225));
        palettePanel.setName("palettePanel"); // NOI18N
        palettePanel.setPreferredSize(new java.awt.Dimension(128, 225));
        surfaceTravail.add(palettePanel);

        jScrollPane.setName("jScrollPane"); // NOI18N
        jScrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScrollPaneComponentResized(evt);
            }
        });

        partitionPanel.setName("jPanel"); // NOI18N
        partitionPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                partitionPanelMouseClicked(evt);
            }
        });
        partitionPanel.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                partitionPanelInputMethodTextChanged(evt);
            }
        });
        partitionPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                partitionPanelKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout partitionPanelLayout = new javax.swing.GroupLayout(partitionPanel);
        partitionPanel.setLayout(partitionPanelLayout);
        partitionPanelLayout.setHorizontalGroup(
            partitionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1228, Short.MAX_VALUE)
        );
        partitionPanelLayout.setVerticalGroup(
            partitionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );

        jScrollPane.setViewportView(partitionPanel);

        surfaceTravail.add(jScrollPane);

        panelTravail.add(surfaceTravail);

        panelGeneral.add(panelTravail);

        panAide.setMaximumSize(new java.awt.Dimension(200, 22000000));
        panAide.setMinimumSize(new java.awt.Dimension(200, 290));
        panAide.setName("panAide"); // NOI18N
        panAide.setPreferredSize(new java.awt.Dimension(200, 400));
        panAide.setLayout(new java.awt.BorderLayout());

        helpPanel.setMaximumSize(new java.awt.Dimension(200, 2147483647));
        helpPanel.setName("helpPanel"); // NOI18N
        panAide.add(helpPanel, java.awt.BorderLayout.CENTER);

        panelGeneral.add(panAide);

        setComponent(panelGeneral);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

private void boutonEntreeMIDIEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonEntreeMIDIEnregistrerActionPerformed
// TODO add your handling code here:
    if(getController().getMachineEntreeMIDI() == null) {
        JOptionPane.showMessageDialog(null, "Je ne trouve pas de clavier, synthétiseur ou autres branché à l'ordinateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    debuterLecture(getController().getCurseurTravail().getMoment());
    getController().getPartitionLecteur().setPasArretFinPartition();


    getController().recordAttachToPlaying();

    boutonMIDIStop.setVisible(true);
    boutonEntreeMIDIEnregistrer.setVisible(false);
    boutonSortieMIDILecture.setVisible(false);

    toolBarEnregistrementMIDI();

}//GEN-LAST:event_boutonEntreeMIDIEnregistrerActionPerformed


private void boutonMIDIStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonMIDIStopActionPerformed
// TODO add your handling code here:
    getController().stop();
    boutonStopDisable();
}//GEN-LAST:event_boutonMIDIStopActionPerformed

private void partitionPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_partitionPanelMouseClicked
// TODO add your handling code here:
}//GEN-LAST:event_partitionPanelMouseClicked


public void boutonStopDisable()
{
    boutonMIDIStop.setVisible(false);
    boutonPause.setVisible(false);
    boutonSortieMIDIReprendre.setVisible(false);
    boutonEntreeMIDIEnregistrer.setVisible(true);
    boutonSortieMIDILecture.setVisible(true);
}


    public void debuterLecture(Moment moment) {
        getController().play(moment);

        getController().getPartitionLecteur().addPartitionLecteurListener(new PartitionLecteurListener() {
            
            @Override
            public void whenStops() {
                getController().stop();
                modeEcriture();
                boutonStopDisable();
            }

            @Override
            public void refresh() {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        getPartitionPanel().repaint();
//                    }
//                });
//                
            }

            @Override
            public void whenBegins() {
            }
        });

        
        vitesseInterfaceToPartitionLecteur();

        

        toolBarLecture();
        boutonMIDIStop.setVisible(true);
        boutonPause.setVisible(true);
        boutonSortieMIDIReprendre.setVisible(false);
        boutonEntreeMIDIEnregistrer.setVisible(false);
        boutonSortieMIDILecture.setVisible(false);
    }

private void boutonSortieMIDILectureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonSortieMIDILectureActionPerformed
    debuterLecture(getController().getCurseurTravail().getMoment());

}//GEN-LAST:event_boutonSortieMIDILectureActionPerformed

private void jScrollPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPaneComponentResized
// TODO add your handling code here:
    partitionVueAdapterALEcran();

}//GEN-LAST:event_jScrollPaneComponentResized

private void boutonAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonAnnulerActionPerformed
    mnuAnnulerActionPerformed(evt);

}//GEN-LAST:event_boutonAnnulerActionPerformed

private void boutonRefaireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonRefaireActionPerformed
    mnuRefaireActionPerformed(evt);

}//GEN-LAST:event_boutonRefaireActionPerformed

private void panelTravailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panelTravailKeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_panelTravailKeyPressed

private void partitionPanelInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_partitionPanelInputMethodTextChanged
// TODO add your handling code here:
}//GEN-LAST:event_partitionPanelInputMethodTextChanged

private void partitionPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_partitionPanelKeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_partitionPanelKeyPressed

private void statusPanelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_statusPanelKeyTyped
// TODO add your handling code here:
}//GEN-LAST:event_statusPanelKeyTyped

    

    

    private void open() {
        
        OpenScoreFileDialog fc = new OpenScoreFileDialog(currentDirectory);
                
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            final String fichierNomAOuvrir = fc.getSelectedFile().getAbsolutePath();
            currentDirectory = fc.getSelectedFile().getAbsoluteFile();
            
                
                
//                new SwingWorker<Integer, Integer>() {
//
//                    @Override
//                    protected Integer doInBackground() throws Exception {
//                        WaitDialog.invokeStart("Chargement du fichier " + fichierNomAOuvrir);
//                        
//                        
                        try {
                            declarerPartitionDonnees(
                                PartitionDonneesChargementGestionnaire.getPartitionDonneesDuFichier(fichierNomAOuvrir));
                            } catch (InvalidMidiDataException ex) {
                            Erreur.message("Je suis désolé : ton fichier MIDI contient des données que je n'arrive pas à lire : " + ex.getMessage());
                        } catch (JDOMException ex) {
                            Erreur.message("Je suis désolé : ton fichier existe mais je n'arrive pas à l'interpréter. L'erreur est la suivante : " + ex.getMessage());
                        } catch (IOException ex) {
                            Erreur.message("Je suis désolé : je n'ai pas réussi à lire le fichier. L'erreur est la suivante : " + ex.getMessage());
                        }
//                        return 0;
//                    }
//
//                @Override
//                protected void done() {
                    WaitDialog.finish();
//                }
//                    
//                    
//                    
//                    
//                }.execute();
                
                partitionVueAdapterALEcran();
                getPartitionPanel().repaint();
                setFichierNom(fichierNomAOuvrir);
                
            


        }



    }


    private boolean save() // commande Fichier, Sauvegarder.
    // retourne TRUE ssi une sauvegarde a eu lieu
    {
        String fichierNomASauvegarder = fichierNom;

        if (fichierNom == null) {
            fichierNomASauvegarder = SaveExportAsDialogFactory.demanderFichierNomPourSauvegarder();
        }

        if (fichierNomASauvegarder != null) {
            try {
                PartitionSauvegardeGestionnaire.sauvegarder(getPartitionDonnees(), fichierNomASauvegarder);
                setFichierNom(fichierNomASauvegarder);
                return true;
            } catch (IOException ex) {
                Erreur.message("Je suis désolé : je n'ai pas réussi à écrire dans le fichier.");
                return false;
            }

        }
        return false;
    }

    private void saveAs() {

        String fichierNomASauvegarder = SaveExportAsDialogFactory.demanderFichierNomPourSauvegarder();

        if (fichierNomASauvegarder != null) {
            try {
                PartitionSauvegardeGestionnaire.sauvegarder(getPartitionDonnees(), fichierNomASauvegarder);
                setFichierNom(fichierNomASauvegarder);
            } catch (IOException ex) {
                Erreur.message("Je suis désolé : je n'ai pas réussi à écrire dans le fichier.");
            }

        }
    }

    private void exportAs() {

        String fichierNomASauvegarder = SaveExportAsDialogFactory.demanderFichierNomPourExporter();

        if (fichierNomASauvegarder != null) {
            try {
                PartitionSauvegardeGestionnaire.sauvegarder(getPartitionDonnees(), fichierNomASauvegarder);
            } catch (IOException ex) {
                Erreur.message("Je suis désolé : je n'ai pas réussi à écrire dans le fichier.");
            }

        }
    }

private void boutonSauvegarderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonSauvegarderActionPerformed
// TODO add your handling code here:
    save();

}//GEN-LAST:event_boutonSauvegarderActionPerformed

    /**
     * S'occupe de demander s'il faut sauvegarderMusicXML la partition
     * Affiche une boite de dialogue "tu veux sauvegarderMusicXML ?"
     * Puis si il y a besoin, propose un lieu pour sauvegarderMusicXML la partition !
     * @return true ssi il est vraiment sûr que l'on ne perd pas de données ! :)
     */
    public boolean closeConfirmation() {
        if (getPartitionDonnees().isModifie()) {
            int option = JOptionPane.showConfirmDialog(null,
                    "Veux-tu sauvegarder la partition ?",
                    "Open Musicwriter", JOptionPane.YES_NO_CANCEL_OPTION);

            switch (option) {
                case JOptionPane.YES_OPTION:
                    return save();
                case JOptionPane.NO_OPTION:
                    return true;
                case JOptionPane.CANCEL_OPTION:
                    return false;
                default:
                    return false;
            }
        } else {
            return true;
        }

    }

private void boutonOuvrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonOuvrirActionPerformed
// TODO add your handling code here:
    open();


}//GEN-LAST:event_boutonOuvrirActionPerformed

private void boutonImprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonImprimerActionPerformed
}//GEN-LAST:event_boutonImprimerActionPerformed

private void boutonEntreeMIDIStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonEntreeMIDIStopActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_boutonEntreeMIDIStopActionPerformed

private void boutonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonPauseActionPerformed
// TODO add your handling code here:
    getController().pause();
    
    boutonPause.setVisible(false);
    boutonSortieMIDIReprendre.setVisible(true);
}//GEN-LAST:event_boutonPauseActionPerformed

private void boutonSortieMIDIReprendreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonSortieMIDIReprendreActionPerformed
// TODO add your handling code here:
    getController().replay();
    
    boutonPause.setVisible(true);
    boutonSortieMIDIReprendre.setVisible(false);
}//GEN-LAST:event_boutonSortieMIDIReprendreActionPerformed

private void jMenuItemCrocheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCrocheActionPerformed
// TODO add your handling code here:
    getController().dureeEntreeTraiter(new Duree(new Rational(1, 2)));
}//GEN-LAST:event_jMenuItemCrocheActionPerformed

private void jMenuItemNoireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNoireActionPerformed
// TODO add your handling code here:
    getController().dureeEntreeTraiter(new Duree(new Rational(1, 1)));
}//GEN-LAST:event_jMenuItemNoireActionPerformed

private void jMenuItemBlancheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBlancheActionPerformed
// TODO add your handling code here:
    getController().dureeEntreeTraiter(new Duree(new Rational(2, 1)));
}//GEN-LAST:event_jMenuItemBlancheActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
// TODO add your handling code here:
    for (int i = 0; i < 10000; i++) {
        Note n = new Note(new Moment(new Rational(i, 8)),
                new Duree(new Rational(1, 8)),
                new Hauteur((int) Math.round(Math.random() * 10), Alteration.NATUREL),
                getPartitionDonnees().getPorteePremiere());
        getPartitionDonnees().elementMusicalAjouter(n);
    }

    getPartitionVue().miseEnPageCalculer();
    partitionPanel.repaint();
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void mnuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewActionPerformed
// TODO add your handling code here:
    if (closeConfirmation()) {
        PartitionDonnees nouvellePartition = PartitionDonnees.createPartitionDonneesNouvellePourPiano();

        PartitionPartiesDialog d = new PartitionPartiesDialog(getFrame(), true, nouvellePartition);
        d.setVisible(true);
        if (!d.isAnnule()) {
            declarerPartitionDonnees(nouvellePartition);
            partitionVueAdapterALEcran();
            getPartitionPanel().repaint();
            nomFichier_Aucun();
        }
    }

}//GEN-LAST:event_mnuNewActionPerformed

    private void setFichierNom(String fichierNom) {
        this.fichierNom = fichierNom;
        this.getFrame().setTitle("Open Musicwriter - " + fichierNom);
    }

private void mnuRepaintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRepaintActionPerformed
// TODO add your handling code here:
    getController().calculer();

}//GEN-LAST:event_mnuRepaintActionPerformed

private void mnuCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCutActionPerformed
// TODO add your handling code here:
    Selection selection = getController().getSelection();

    if (selection != null) {
        pressePapierCopier();
        getController().executer(new PartitionActionSelectionSupprimer(selection));
        getController().calculer();
    }
}//GEN-LAST:event_mnuCutActionPerformed

    
    
    private void pressePapierCopier() {
        Selection selection = getController().getSelection();

        if (selection != null) {
            pressePapier = selection.clone();
            for (ElementMusical el : pressePapier) {
                if (el instanceof Note) {
                    ((Note) el).transposer(Intervalle.getIntervalleOctave());
                }
                //el.deplacer((new Duree(1, 2)).getFinMoment(el.getDebutMoment()) );

            }

        }
    }
private void mnuCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCopyActionPerformed

    pressePapierCopier();
}//GEN-LAST:event_mnuCopyActionPerformed

private void mnuPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPasteActionPerformed

    //if(!getPartitionPanel().isAffichageMomentAffiche(pressePapier.getMomentDebut()))
    //  {
    Selection objetsAColler = pressePapier.clone();
    objetsAColler.setDebutMoment(getController().getCurseurSouris().getMoment());
    // }

    getController().executer((new PartitionActionSelectionInserer(objetsAColler)));
    getController().calculer();
    getController().setSelection(objetsAColler);
    getController().repaint();
}//GEN-LAST:event_mnuPasteActionPerformed

private void mnuDoubleCrocheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDoubleCrocheActionPerformed
// TODO add your handling code here:
    getController().dureeEntreeTraiter(new Duree(new Rational(1, 4)));
}//GEN-LAST:event_mnuDoubleCrocheActionPerformed

private void mnuTripleCrocheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTripleCrocheActionPerformed
// TODO add your handling code here:
    getController().dureeEntreeTraiter(new Duree(new Rational(1, 8)));
}//GEN-LAST:event_mnuTripleCrocheActionPerformed

private void mnuRondeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRondeActionPerformed
// TODO add your handling code here:
    getController().dureeEntreeTraiter(new Duree(new Rational(4, 1)));
}//GEN-LAST:event_mnuRondeActionPerformed

private void mnuVoixNotesIndependantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVoixNotesIndependantesActionPerformed
// TODO add your handling code here:
    getController().executer(new PartitionActionSelectionPasDeVoix(getController().getSelection()));
    getController().calculer();
    getController().repaint();
}//GEN-LAST:event_mnuVoixNotesIndependantesActionPerformed

private void mnuNouvelleVoixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNouvelleVoixActionPerformed
    getController().executer(new PartitionActionSelectionVoixSet(getController().getSelection(), new Voix()));
    getController().calculer();
    getController().repaint();
}//GEN-LAST:event_mnuNouvelleVoixActionPerformed

private void mnuAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAnnulerActionPerformed
// TODO add your handling code here:
    if (getController().isAnnulerPossible()) {
        getController().selectionAbandonner();
        getController().annulerLaDerniereAction();
        partitionPanel.repaint();
    } else {
        Toolkit.getDefaultToolkit().beep();
    }

}//GEN-LAST:event_mnuAnnulerActionPerformed

private void mnuRefaireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRefaireActionPerformed
// TODO add your handling code here:
    if (getController().isRefairePossible()) {
        getController().selectionAbandonner();
        getController().refaireLaDerniereAction();
        partitionPanel.repaint();
    } else {
        Toolkit.getDefaultToolkit().beep();
    }

}//GEN-LAST:event_mnuRefaireActionPerformed

private void mnuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveActionPerformed
    save();
}//GEN-LAST:event_mnuSaveActionPerformed

private void mnuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveAsActionPerformed
    saveAs();
}//GEN-LAST:event_mnuSaveAsActionPerformed

private void cmdOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOpenActionPerformed
// TODO add your handling code here:
    open();
}//GEN-LAST:event_cmdOpenActionPerformed

    

private void mnuAlterationDoubleBemolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationDoubleBemolActionPerformed
    getController().alterer(Alteration.DOUBLEBEMOL);
}//GEN-LAST:event_mnuAlterationDoubleBemolActionPerformed

private void mnuAlterationBemolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationBemolActionPerformed
    getController().alterer(Alteration.BEMOL);
}//GEN-LAST:event_mnuAlterationBemolActionPerformed

private void mnuAlterationBecarreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationBecarreActionPerformed
    getController().alterer(Alteration.NATUREL);
}//GEN-LAST:event_mnuAlterationBecarreActionPerformed

private void mnuAlterationDieseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationDieseActionPerformed
    getController().alterer(Alteration.DIESE);
}//GEN-LAST:event_mnuAlterationDieseActionPerformed

private void mnuAlterationDoubleDieseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationDoubleDieseActionPerformed
    getController().alterer(Alteration.DOUBLEDIESE);
}//GEN-LAST:event_mnuAlterationDoubleDieseActionPerformed

private void mnuAlterationEnharmoniqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationEnharmoniqueActionPerformed
    if (getController().isSelection()) {
        getController().executer(
                new PartitionActionSelectionEnharmonique(
                getController().getSelection()));

        getController().repaint();
    }
}//GEN-LAST:event_mnuAlterationEnharmoniqueActionPerformed

private void mnuAlterationViaTonaliteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationViaTonaliteActionPerformed
    TonaliteDialog d = new TonaliteDialog(null, true);
    d.setVisible(true);

    if (d.getTonalite() != null) {
        getController().executer(
                new PartitionActionSelectionAltererSelonTonalite(
                getController().getSelection(),
                d.getTonalite()));


        getController().repaint();
    }

}//GEN-LAST:event_mnuAlterationViaTonaliteActionPerformed

private void mnuEditionSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEditionSupprimerActionPerformed
    if (getController().isSelection()) {
        getController().executer(
                new PartitionActionSelectionSupprimer(
                getController().getSelection()));
        getPartitionVue().miseEnPageCalculer(getController().getSelection().getMomentDebut());
        getController().selectionAbandonner();
        getController().repaint();
    }
}//GEN-LAST:event_mnuEditionSupprimerActionPerformed

private void mnuInstrumentationChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuInstrumentationChangeActionPerformed
    PartitionPartiesDialog d = new PartitionPartiesDialog(getFrame(), true, getController().getHistoire(), getPartitionDonnees());
    d.setVisible(true);
    getPartitionVue().miseEnPageCalculer();
    getController().repaint();
}//GEN-LAST:event_mnuInstrumentationChangeActionPerformed

    private void vitesseInterfaceToPartitionLecteur() {
        getController().getPartitionLecteur().setVitesse(((double) jSliderLectureVitesse.getValue()) / 100.0);
    }

private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
    panAide.setVisible(!panAide.isVisible());
}//GEN-LAST:event_jMenuItem8ActionPerformed

private void mnuConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConfigurationActionPerformed
    configurationMIDI();
}//GEN-LAST:event_mnuConfigurationActionPerformed


private void configurationMIDI()
{
    DialogConfiguration dialogueConfiguration = new DialogConfiguration(getFrame(), true);
    
    dialogueConfiguration.setMachineSortieMIDI(((MachineSortieMIDIEcoutee) getController().getMachineSortieMIDI()).getName());
    dialogueConfiguration.setVisible(true);
    machineSortieMIDI_creer(dialogueConfiguration.machineSortieMIDINumeroGet());
    machineEntreeMIDI_creer(dialogueConfiguration.machineEntreeMIDINumeroGet());
}


private void mnuSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSelectAllActionPerformed
    getController().setSelection(getPartitionDonnees().getSelectionTout());
    getController().repaint();
}//GEN-LAST:event_mnuSelectAllActionPerformed

private void mnuDeselectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDeselectAllActionPerformed
    getController().selectionAbandonner();
    getController().repaint();
}//GEN-LAST:event_mnuDeselectAllActionPerformed

private void mnuDebugAllSameVoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDebugAllSameVoiceActionPerformed
    for (ElementMusical el : getPartitionDonnees().getElementsMusicaux()) {
        if (el instanceof ElementMusicalSurVoix) {
            ElementMusicalSurVoix elv = (ElementMusicalSurVoix) el;
            elv.setVoix(new Voix(0));
        }
    }
}//GEN-LAST:event_mnuDebugAllSameVoiceActionPerformed

private void mnuAffichagePartieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAffichagePartieActionPerformed
}//GEN-LAST:event_mnuAffichagePartieActionPerformed

private void mnuAffichageToutConducteurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAffichageToutConducteurActionPerformed
}//GEN-LAST:event_mnuAffichageToutConducteurActionPerformed

private void mnuAffichagePartie1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAffichagePartie1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_mnuAffichagePartie1ActionPerformed

private void mnuAffichageToutConducteur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAffichageToutConducteur1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_mnuAffichageToutConducteur1ActionPerformed

private void mnuSelectionEtirerDoublerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSelectionEtirerDoublerActionPerformed
    getController().executer(new PartitionActionSelectionDureesEtirer(
            getController().getSelection(),
            new Rational(2, 1)));

    getController().calculerModificationSelection();


}//GEN-LAST:event_mnuSelectionEtirerDoublerActionPerformed

private void mnuSelectionDureeDiviserParDeuxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSelectionDureeDiviserParDeuxActionPerformed
    getController().executer(new PartitionActionSelectionDureesEtirer(
            getController().getSelection(),
            new Rational(1, 2)));

    getController().calculerModificationSelection();
}//GEN-LAST:event_mnuSelectionDureeDiviserParDeuxActionPerformed

private void mnuAlterationDoubleBemolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuAlterationDoubleBemolKeyPressed
}//GEN-LAST:event_mnuAlterationDoubleBemolKeyPressed

private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
    (new TipsDialog(getFrame(), true)).setVisible(true);

}//GEN-LAST:event_jMenuItem9ActionPerformed

private void mnuZoom200ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuZoom200ActionPerformed
    getPartitionPanel().setZoom(2.0);
}//GEN-LAST:event_mnuZoom200ActionPerformed

private void mnuZoom50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuZoom50ActionPerformed
    getPartitionPanel().setZoom(0.5);
}//GEN-LAST:event_mnuZoom50ActionPerformed

private void mnuZoom100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuZoom100ActionPerformed
    getPartitionPanel().setZoom(1.0);
}//GEN-LAST:event_mnuZoom100ActionPerformed

private void mnuIntervalAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuIntervalAdd1ActionPerformed
    /**
     * on prend le comportement à partir des racourcis claviers
     */
    JMenuItem m = ((JMenuItem) evt.getSource());
    final int absi = Integer.parseInt(String.valueOf(m.getAccelerator().getKeyCode())) - 49;

    final int i;

    /**
     * Maj = intervalle vers le bas
     * sinon intervalle vers le haut
     */
    if (m.getAccelerator().getModifiers() == java.awt.event.InputEvent.SHIFT_DOWN_MASK
            + java.awt.event.InputEvent.SHIFT_MASK) {
        i = -absi;
    } else {
        i = absi;
    }


    final Intervalle intervalle = new Intervalle(i, Alteration.NATUREL);

    if (getController().isSelection()) {
        final Selection notesTransposeesAjoutees =
                getController().getSelection().getNotesTransposees(intervalle);

        getController().executer(
                new PartitionActionSelectionInserer(notesTransposeesAjoutees));
        getController().setSelection(notesTransposeesAjoutees);
        getController().calculerModificationSelection();
    } else {
        final ElementMusical el = getPartitionDonnees().getElementMusicalProche(getController().getCurseurSouris());

        if (el instanceof Note) {
            getController().executer(
                    new PartitionActionElementMusicalAjouter(((Note) el).getNoteTransposee(intervalle)));
            getController().calculer(el.getDebutMoment());
        }
    }


}//GEN-LAST:event_mnuIntervalAdd1ActionPerformed

private void mnuAlterationArmatureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAlterationArmatureActionPerformed
    getController().executer(
            new PartitionActionSelectionAltererSelonTonalite(
            getController().getSelection(),
            getPartitionDonnees().getTonalite(getController().getSelection().getMomentDebut())));

}//GEN-LAST:event_mnuAlterationArmatureActionPerformed

private void mnuAnnulerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuAnnulerKeyReleased
}//GEN-LAST:event_mnuAnnulerKeyReleased

private void mnuSelectionMesureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSelectionMesureActionPerformed
    getController().selectionnerMesure();
}//GEN-LAST:event_mnuSelectionMesureActionPerformed
    noterecognition.NoteRecognition noteRecognition = new NoteRecognition(new NoteRecognitionNotePerformedListener() {

        @Override
        public void whenNotePerformed(NoteRecognitionEvent nre) {
            if (nre.getDurationSinceBeginningOfNote() > 200) {
                getController().setCurseurSouris(
                        getController().getCurseurSouris().getCurseurAutreHauteur(new Hauteur(12 * 3 + nre.getNoteNumber() - 7)));
            }
        }
    },
            new NoteRecognitionNoNoteListener() {

                @Override
                public void whenNoNote() {
                }
            });

private void mnuMicrophoneEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMicrophoneEnregistrerActionPerformed
    noteRecognition.start();
}//GEN-LAST:event_mnuMicrophoneEnregistrerActionPerformed

private void mnuMicrophoneStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMicrophoneStopActionPerformed
    noteRecognition.stop();
}//GEN-LAST:event_mnuMicrophoneStopActionPerformed

private void mnuParolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuParolesActionPerformed
    if (getController().isSelection()) {
        final Curseur curseur = getController().getSelection().getNotes().iterator().next().getCurseur();

        getController().setMode(PartitionPanelModeTexte.getParoleSyllabeEditionMode(getController(), curseur));

    }
}//GEN-LAST:event_mnuParolesActionPerformed

private void mnuExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportActionPerformed
    exportAs();
}//GEN-LAST:event_mnuExportActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        UpdateDialog d = new UpdateDialog(getFrame(), true);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        configurationMIDI();
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void mnuSendCommentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSendCommentsActionPerformed
        WebBrowserInteraction.goToURL("http://openmusicwriter.sourceforge.net/comment.html");
    }//GEN-LAST:event_mnuSendCommentsActionPerformed
//PartitionAction partitionActionBouton;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boutonAnnuler;
    private javax.swing.JButton boutonEntreeMIDIEnregistrer;
    private javax.swing.JButton boutonImprimer;
    private javax.swing.JButton boutonMIDIStop;
    private javax.swing.JButton boutonMIDIStop1;
    private javax.swing.JButton boutonOuvrir;
    private javax.swing.JButton boutonPause;
    private javax.swing.JButton boutonPause1;
    private javax.swing.JButton boutonRefaire;
    private javax.swing.JButton boutonSauvegarder;
    private javax.swing.JButton boutonSortieMIDILecture;
    private javax.swing.JButton boutonSortieMIDIReprendre;
    private javax.swing.JButton cmdNoteDecorer;
    private javax.swing.JMenuItem cmdOpen;
    private javax.swing.JButton cmdSelectionArmure;
    private javax.swing.JButton cmdSelectionClef;
    private javax.swing.JButton cmdSelectionMesureSignature;
    private javax.swing.JButton cmdSelectionSupprimer;
    private javax.swing.JButton cmdSelectionTempo;
    private musicwriter.guiswing.HelpPanel helpPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemBlanche;
    private javax.swing.JMenuItem jMenuItemCroche;
    private javax.swing.JMenuItem jMenuItemNoire;
    private javax.swing.JMenu jMenuNoteDuree;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JSlider jSliderLectureVitesse;
    private javax.swing.JLabel lblSelectionPlus;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mnuAffichagePartie;
    private javax.swing.JMenuItem mnuAffichagePartie1;
    private javax.swing.JMenuItem mnuAffichageToutConducteur;
    private javax.swing.JMenuItem mnuAffichageToutConducteur1;
    private javax.swing.JMenu mnuAlteration;
    private javax.swing.JMenuItem mnuAlterationArmature;
    private javax.swing.JMenuItem mnuAlterationBecarre;
    private javax.swing.JMenuItem mnuAlterationBemol;
    private javax.swing.JMenuItem mnuAlterationDiese;
    private javax.swing.JMenuItem mnuAlterationDoubleBemol;
    private javax.swing.JMenuItem mnuAlterationDoubleDiese;
    private javax.swing.JMenuItem mnuAlterationEnharmonique;
    private javax.swing.JMenuItem mnuAlterationViaTonalite;
    private javax.swing.JMenuItem mnuAnnuler;
    private javax.swing.JMenuItem mnuConfiguration;
    private javax.swing.JMenuItem mnuCopy;
    private javax.swing.JMenuItem mnuCut;
    private javax.swing.JMenuItem mnuDebugAllSameVoice;
    private javax.swing.JMenuItem mnuDeselectAll;
    private javax.swing.JMenu mnuDisplay;
    private javax.swing.JMenuItem mnuDoubleCroche;
    private javax.swing.JMenuItem mnuEditionSupprimer;
    private javax.swing.JMenuItem mnuExport;
    private javax.swing.JMenu mnuHarmonie;
    private javax.swing.JMenuItem mnuInsererBarreDeMesure;
    private javax.swing.JMenu mnuInsert;
    private javax.swing.JMenuItem mnuInstrumentationChange;
    private javax.swing.JMenu mnuInstruments;
    private javax.swing.JMenu mnuIntervalAdd;
    private javax.swing.JMenuItem mnuIntervalAdd1;
    private javax.swing.JMenuItem mnuIntervalAdd2;
    private javax.swing.JMenuItem mnuIntervalAdd4;
    private javax.swing.JMenuItem mnuIntervalAdd5;
    private javax.swing.JMenuItem mnuIntervalAdd6;
    private javax.swing.JMenuItem mnuIntervalAdd7;
    private javax.swing.JMenuItem mnuIntervalAdd8;
    private javax.swing.JMenuItem mnuIntervalAddM2;
    private javax.swing.JMenuItem mnuIntervalAddM3;
    private javax.swing.JMenuItem mnuIntervalAddM4;
    private javax.swing.JMenuItem mnuIntervalAddM5;
    private javax.swing.JMenuItem mnuIntervalAddM6;
    private javax.swing.JMenuItem mnuIntervalAddM7;
    private javax.swing.JMenuItem mnuIntervalAddM8;
    private javax.swing.JMenuItem mnuMicrophoneEnregistrer;
    private javax.swing.JMenuItem mnuMicrophoneStop;
    private javax.swing.JMenuItem mnuNew;
    private javax.swing.JMenuItem mnuNote;
    private javax.swing.JMenuItem mnuNouvelleVoix;
    private javax.swing.JMenuItem mnuParoles;
    private javax.swing.JMenuItem mnuPaste;
    private javax.swing.JMenuItem mnuPrint;
    private javax.swing.JMenuItem mnuRefaire;
    private javax.swing.JMenuItem mnuRepaint;
    private javax.swing.JMenuItem mnuRonde;
    private javax.swing.JMenuItem mnuSave;
    private javax.swing.JMenuItem mnuSaveAs;
    private javax.swing.JMenuItem mnuSelectAll;
    private javax.swing.JMenu mnuSelection;
    private javax.swing.JMenuItem mnuSelectionDureeDiviserParDeux;
    private javax.swing.JMenuItem mnuSelectionEtirerDoubler;
    private javax.swing.JMenuItem mnuSelectionMesure;
    private javax.swing.JMenuItem mnuSendComments;
    private javax.swing.JMenuItem mnuSilence;
    private javax.swing.JMenuItem mnuTransposer;
    private javax.swing.JMenuItem mnuTripleCroche;
    private javax.swing.JMenuItem mnuVoiceSameVoice;
    private javax.swing.JMenu mnuVoix;
    private javax.swing.JMenuItem mnuVoixNotesIndependantes;
    private javax.swing.JMenu mnuZoom;
    private javax.swing.JMenuItem mnuZoom100;
    private javax.swing.JMenuItem mnuZoom200;
    private javax.swing.JMenuItem mnuZoom50;
    private musicwriter.guiswing.palette.PalettePanel palettePanel;
    private javax.swing.JPanel panAide;
    private javax.swing.JPanel panBarreOutils;
    private javax.swing.JPanel panelGeneral;
    private musicwriter.guiswing.PanelHelpNoteDeplacement panelHelpNoteDeplacement;
    private musicwriter.guiswing.PanelHelpStylet panelHelpStylet;
    private javax.swing.JPanel panelTravail;
    private javax.swing.JPanel partitionPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel surfaceTravail;
    private javax.swing.JToolBar toolBarEcriture;
    private javax.swing.JToolBar toolBarLecture;
    private javax.swing.JToolBar toolBarSelection;
    private javax.swing.JToolBar toolbarNotes;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;

    private PartitionDonnees getPartitionDonnees() {
        return getController().getPartitionDonnees();
    }

    @Action
    public void actionSelectionSupprimer() {
        Selection selectionASupprimer = getController().getSelection();
        getController().executer(new PartitionActionSelectionSupprimer(selectionASupprimer));
        getController().selectionAbandonner();
        getController().calculer(selectionASupprimer.getMomentDebut());
        modeEcriture();
    }

    @Action
    public void actionSelectionArmure() {
        getPartitionPanel().elementMusicalTonaliteChangerTonaliteBoiteDeDialogue(((ElementMusicalChangementTonalite) getController().getSelection().getElementMusicalUnique()));

    }

    @Action
    public void actionSelectionMesureSignature() {
        getPartitionPanel().elementMusicalMesureSignatureChangerTonaliteBoiteDeDialogue(
                ((ElementMusicalChangementMesureSignature) getController().getSelection().getElementMusicalUnique()));
    }

    @Action
    public void actionSelectionTempo() {
        getPartitionPanel().elementMusicalTempoChangerTempoBoiteDeDialogue(
                ((ElementMusicalTempo) getController().getSelection().getElementMusicalUnique()));
    }

    @Action
    public void actionSelectionClef() {
        getPartitionPanel().elementMusicalClefChangerClefBoiteDeDialogue(
                ((ElementMusicalClef) getController().getSelection().getElementMusicalUnique()));
    }

    @Action
    public void actionNoteDecorerBoiteDeDialogue() {
        NoteDecorationPanel d = new NoteDecorationPanel(getFrame(), true);
        Note note = getController().getSelection().getNotes().iterator().next();
        d.setArticulations(note.getArticulations());
        d.setOrnements(note.getOrnements());

        d.setVisible(true);

        if (d.isOK()) {
            getController().executer(new PartitionActionSelectionNotesDecorer(getController().getSelection(),
                    d.getArticulations(), d.getOrnements()));
            getController().executer(new PartitionActionSelectionNotesFigures(getController().getSelection(), d.getNoteFigure()));
        }
    }

    @Action
    public void imprimer() {
        PartitionImpression.boiteDialogueImprimerPuisImprimer(getPartitionDonnees(), getController().getPartiesAffichees());
    }

    @Action
    public void afficherPartie() {
        PartitionPartiesAffichageDialog d = new PartitionPartiesAffichageDialog(getFrame(), true, getPartitionDonnees());
        d.setVisible(true);

        if (d.isOk()) {
            getController().setPartiesAffichees(d.getPartiesAffichees());
            getController().repaint();
        }
    }

    @Action
    public void afficherConducteur() {
        getController().setPartiesAffichees(getPartitionDonnees().getParties());
        getController().repaint();
    }

    @Action
    public void actionInsertMeasureBar() {
        if (getPartitionDonnees().isBarreDeMesure(getController().getCurseurSouris().getMoment())) {
            return;
        }

        getController().executer(new PartitionActionElementMusicalAjouter(new BarreDeMesure(getController().getCurseurSouris().getMoment())));
        getController().calculer(getController().getCurseurSouris().getMoment());
        getController().repaint();
    }

    @Action
    public void actionHampeDirectionChanger() {
        getController().selectionNotesHampesDirectionsChanger();
    }

    @Action
    public void actionTranspose() {
        TransposerDialog d = new TransposerDialog(getFrame(), true);
        d.setVisible(true);

        if (d.isOk()) {
            if (getController().isSelection()) {
                getController().executer(
                        new PartitionActionSelectionDeplacerHauteur(getController().getSelection(),
                        d.getIntervalle()));
                getController().calculerModificationSelection();
            } else {
                getController().executer(
                        new PartitionActionSelectionDeplacerHauteur(getPartitionDonnees().getSelectionTout(),
                        d.getIntervalle()));
                getController().calculer();
            }
        }

    }

    private PartitionVue getPartitionVue() {
       return getController().getPartitionVue();
    }

    private Controller getController() {
        return getPartitionPanel().getController();
    }

    private void languageMenuSet(JMenu helpMenu) {
        JMenu languageMenu = new JMenu("Languages / Langues");
        helpMenu.add(languageMenu);
        
        languageAdd(languageMenu,Locale.getDefault());
        languageMenu.add(new JSeparator());
        languageAdd(languageMenu,Locale.FRENCH);
        languageAdd(languageMenu,Locale.ENGLISH);
        
    }
    
    private void languageAdd(final JMenu languagesMenu, final Locale l)
    {
        final JMenuItem languageMenu = new JMenuItem(l.getDisplayLanguage() + "(" + l.getLanguage() + ")");
        languageMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for(Component c : languagesMenu.getComponents())
                {
                    if(c instanceof JMenuItem )
                    {
                        ((JMenuItem) c).setSelected(false);
                    }
                }
                languageMenu.setSelected(true);
                Resource.setLocale(l);
                loadFromResource();
            }
        });
        
        if(l.equals(Locale.getDefault()))
            languageMenu.setSelected(true);
        
        languagesMenu.add(languageMenu);
    }
}
