/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package musicwriter.guiswing.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import musicwriter.Utils;
import musicwriter.donnees.Instrument;
import musicwriter.donnees.Partie;
import musicwriter.donnees.actions.PartitionActionPartieAjouter;

/**
 *
 * @author Ancmin
 */
public class InstrumentLabel extends JLabel implements DragSourceListener,
    DragGestureListener {

        final Instrument instrument;
      //  final DragSource ds;

        InstrumentLabel(Instrument instrument) {
            super();

            this.instrument = instrument;

            setOpaque(true);
            setBorder(new RoundedBorder());
            setBackground(new Color(250,250,250));
            setIconTextGap(4);
            setHorizontalAlignment(CENTER);
            setHorizontalTextPosition(RIGHT);

            setIcon(instrument.getImageIconSmall());
            setText(instrument.getNom());

           //  ds = new DragSource();
          //  DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(this,
          //    DnDConstants.ACTION_COPY, this);

            
        }

        
        
        
        
        public void addGhostManagers(GhostGlassPane ghostGlassPane)
        {
              addMouseListener(new GhostComponentAdapter(ghostGlassPane, new InstrumentTransferable(getInstrument())));
              GhostPictureAdapter pictureAdapter;
              
              
              addMouseMotionListener(new GhostMotionAdapter(ghostGlassPane));
              addMouseListener(pictureAdapter = new GhostPictureAdapter( ghostGlassPane,
                                                        instrument.getImageIconSmall(),
                      new InstrumentTransferable(getInstrument())));
              
              final Component cthis = this;
              GhostDropListener listener = new  GhostDropListener() {
                    @Override
                    public void ghostDropped(GhostDropEvent e) {
                        RootPaneContainer c = Utils.findRootPane(cthis);
                        Point point = (Point) e.getDropLocation().clone();
                        SwingUtilities.convertPointFromScreen(point, c.getRootPane());
                        Component component = c.getContentPane().findComponentAt(point);
                        
                        if(!(component instanceof PartitionPartiesEditorList))
                            return;
                        
                            final PartitionPartiesEditorList L = (PartitionPartiesEditorList) component;
                            final int index = L.getIndex(point);

                                final Partie partie = new Partie(getInstrument());
                                L.getHistoire().executer(new PartitionActionPartieAjouter(index, partie));

                                L.mettreAJour();

                    }
            };
              
              pictureAdapter.addGhostDropListener(listener);
              
              
              
              addMouseMotionListener(new GhostMotionAdapter(ghostGlassPane));
        }
        
        public ImageIcon getImageIcon()
        {
            return instrument.getImageIconSmall();
        }
        public Instrument getInstrument() {
            return instrument;
        }

        public void dragEnter(DragSourceDragEvent dsde) {
      //      throw new UnsupportedOperationException("Not supported yet.");
        }

        public void dragOver(DragSourceDragEvent dsde) {
   //         throw new UnsupportedOperationException("Not supported yet.");
        }

        public void dropActionChanged(DragSourceDragEvent dsde) {
      //      throw new UnsupportedOperationException("Not supported yet.");
        }

        public void dragExit(DragSourceEvent dse) {
        //    throw new UnsupportedOperationException("Not supported yet.");
        }

        public void dragDropEnd(DragSourceDropEvent dsde) {
        //    throw new UnsupportedOperationException("Not supported yet.");
        }

        public void dragGestureRecognized(DragGestureEvent dge) {
            // ds.startDrag(dge, DragSource.DefaultCopyDrop, instrument.getImageIcon().getImage(), new Point(0, 0), new InstrumentTransferable(instrument), this);
 
        }





    }