package org.jscience.architecture.lift.gui;

import org.jscience.architecture.lift.InOutput;

import java.awt.*;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:54 $
 */
public class OutputsRenderer extends JPanel {
    /**
     * DOCUMENT ME!
     */
    final static int PreferredWidth = 36;

    /**
     * DOCUMENT ME!
     */
    final static int PreferredHeight = 48;

    /**
     * DOCUMENT ME!
     */
    final static Dimension PreferredSize = new Dimension(PreferredWidth,
            PreferredHeight);

    /**
     * DOCUMENT ME!
     */
    final Color[] Colors = new Color[] {
            Color.LIGHT_GRAY, Color.CYAN, Color.BLUE.darker().darker(),
            Color.RED.darker(), Color.GRAY
        };

    /**
     * DOCUMENT ME!
     */
    InOutput[] Os = null;

    /**
     * DOCUMENT ME!
     */
    boolean Initialized = false;

    /**
     * DOCUMENT ME!
     */
    JPanel JP;

    /**
     * Creates a new OutputsRenderer object.
     *
     * @param Outs DOCUMENT ME!
     */
    public OutputsRenderer(InOutput[] Outs) {
        Os = Outs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return (PreferredSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param Index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public InOutput getInOutput(int Index) {
        return (Os[Index]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOpaque() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param G DOCUMENT ME!
     */
    public void paintComponent(Graphics G) {
        if (!Initialized) {
            Initialized = true;

            int H = getSize().height;
            int W = getSize().width;
            int Size = InnerCarCanvas.getMaxSize(W, H, Os.length, 0.75);
            int HCount = W / (int) (Size * 0.75);
            int VCount = H / Size;

            setLayout(new GridLayout(HCount, VCount));

            for (int i = 0; i < (HCount * VCount); i++) {
                if (i < Os.length) {
                    JP = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

                    JP.setBackground(Colors[4]);
                    JP.add(new InputRenderer(Os[i], W / HCount, H / VCount,
                            Colors));
                    add(JP);
                }
            }

            revalidate();
        }
    }
}
