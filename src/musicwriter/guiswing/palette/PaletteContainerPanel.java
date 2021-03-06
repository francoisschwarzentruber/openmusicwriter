/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PaletteContainerPanel.java
 *
 * Created on 24 févr. 2011, 23:23:37
 */

package musicwriter.guiswing.palette;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

/**
 *
 * @author Ancmin
 */
public class PaletteContainerPanel extends javax.swing.JPanel {

    /** Creates new form PaletteContainerPanel */
    public PaletteContainerPanel() {
        initComponents();
        this.setLayout(new ModifiedFlowLayout(FlowLayout.LEADING, 0, 0));
        
    }


    public class ModifiedFlowLayout extends FlowLayout
{
    public ModifiedFlowLayout()
    {
        super();
    }

    public ModifiedFlowLayout(int align)
    {
        super(align);
    }

    public ModifiedFlowLayout(int align, int hgap, int vgap)
    {
        super(align, hgap, vgap);
    }

        @Override
    public Dimension minimumLayoutSize(Container target)
    {
        return computeSize(target, true);
    }

        @Override
    public Dimension preferredLayoutSize(Container target)
    {
        return computeSize(target, false);
    }

    private Dimension computeSize(Container target, boolean minimum)
    {
        synchronized (target.getTreeLock())
        {
            int hgap = getHgap();
            int vgap = getVgap();
            int w = target.getWidth();

       // Let this behave like a regular FlowLayout (single row)
       // if the container hasn't been assigned any size yet
            if (w == 0)
                w = Integer.MAX_VALUE;

            Insets insets = target.getInsets();
            if (insets == null)
                insets = new Insets(0, 0, 0, 0);
            int reqdWidth = 0;

            final int maxwidth = w - (insets.left + insets.right + hgap * 2);
            final int n = target.getComponentCount();
            int x = 0;
            int y = insets.top;
            int rowHeight = 0;

            for (int i = 0; i < n; i++)
            {
                Component c = target.getComponent(i);
                if (c.isVisible())
                {
                    Dimension d =
                        minimum ? c.getMinimumSize() :
                 c.getPreferredSize();
                    if ((x == 0) || ((x + d.width) <= maxwidth))
                    {
                        if (x > 0)
                        {
                            x += hgap;
                        }
                        x += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    } else
                    {
                        x = d.width;
                        y += vgap + rowHeight;
                        rowHeight = d.height;
                    }
                    reqdWidth = Math.max(reqdWidth, x);
                }
            }
            y += rowHeight;
            return new Dimension(reqdWidth+insets.left+insets.right, y);
        }
    }
}

  






    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(120, 32767));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(120, 1400));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 128, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
