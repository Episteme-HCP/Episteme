/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.architecture.lift.gui;

import org.jscience.architecture.lift.InOutput;

import java.awt.*;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public class InputRenderer extends JPanel {
    /**
     * DOCUMENT ME!
     */
    final static int DefaultWidth = 36;

    /**
     * DOCUMENT ME!
     */
    final static int DefaultHeight = 48;

    /**
     * DOCUMENT ME!
     */
    final InOutput I;

    /**
     * DOCUMENT ME!
     */
    final Dimension PreferredSize;

    /**
     * DOCUMENT ME!
     */
    final Color[] Colors;

    /**
     * Creates a new InputRenderer object.
     *
     * @param In DOCUMENT ME!
     * @param PreferredWidth DOCUMENT ME!
     * @param PreferredHeight DOCUMENT ME!
     * @param ColorTheme DOCUMENT ME!
     */
    public InputRenderer(InOutput In, int PreferredWidth, int PreferredHeight,
        Color[] ColorTheme) {
        PreferredSize = new Dimension(PreferredWidth, PreferredHeight);
        I = In;
        Colors = ColorTheme;
    }

    /**
     * Creates a new InputRenderer object.
     *
     * @param In DOCUMENT ME!
     */
    public InputRenderer(InOutput In) {
        PreferredSize = new Dimension(DefaultWidth, DefaultHeight);
        I = In;
        Colors = new Color[] {
                Color.LIGHT_GRAY, Color.GREEN.brighter().brighter(),
                Color.GREEN.darker().darker(), Color.RED.darker()
            };
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewColor DOCUMENT ME!
     * @param Index DOCUMENT ME!
     */
    public void setColor(Color NewColor, int Index) {
        Colors[Index] = NewColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public InOutput getInput() {
        return I;
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
     * @param G DOCUMENT ME!
     */
    public void paintComponent(Graphics G) {
        int H = getSize().height;
        int W = getSize().width;
        int[] UpX = new int[] {
                (int) (W * 0.15), (int) (W * 0.65), (int) (W * 0.4)
            };
        int[] UpY = new int[] {
                (int) (H * 0.35), (int) (H * 0.35), (int) (H * 0.1)
            };
        int[] DownX = new int[] {
                (int) (W * 0.15), (int) (W * 0.65), (int) (W * 0.4)
            };
        int[] DownY = new int[] {
                (int) (H * 0.65), (int) (H * 0.65), (int) (H * 0.9)
            };

        G.setColor(Colors[0]);
        G.fillRect(0, 0, W, H);
        G.setColor(I.getUp() ? Colors[1] : Colors[2]);
        G.drawPolygon(UpX, UpY, 3);
        G.fillPolygon(UpX, UpY, 3);

        G.setColor(I.getDown() ? Colors[1] : Colors[2]);
        G.fillPolygon(DownX, DownY, 3);
        G.drawPolygon(DownX, DownY, 3);

        int LH = H / (3 * I.getMaxFloor());

        if (LH >= 1) {
            int CH = (H - (LH * 3 * I.getMaxFloor())) / 2;
            int CW = (int) (0.75 * W);
            int LedW = (int) (0.2 * W);

            for (int i = I.getMaxFloor() - 1; i >= 0; i--) {
                G.setColor((I.getFloor() == i) ? Colors[3]
                                               : (I.getSignal(i) ? Colors[1]
                                                                 : Colors[2]));
                G.fillRect(CW, CH, LedW, LH * 2);
                CH += (3 * LH);
            }
        }
    }
}
