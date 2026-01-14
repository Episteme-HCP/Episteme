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

// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import org.jscience.physics.electricity.circuitry.CircuitElement;

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ProbeElement extends CircuitElement {
    /**
     * Creates a new ProbeElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public ProbeElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new ProbeElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public ProbeElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'p';
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        int segments = 16;
        int i;
        int ox = 0;
        int dx = x2 - x;
        int dy = y2 - y;
        int hs = 8;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        setBbox(x, y, x2, y2);

        double len = ((circuitFrame.mouseElement == this) || (circuitFrame.dragElement == this)) ? ((dn / 2) - 8)
                                                               : 16;

        if (len > (dn / 2)) {
            len = dn / 2;
        }

        int x1a = (int) (x + ((dx * (len)) / dn));
        int y1a = (int) (y + ((dy * (len)) / dn));
        int x2a = (int) (x + ((dx * (dn - len)) / dn));
        int y2a = (int) (y + ((dy * (dn - len)) / dn));
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x1a, y1a);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);

        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        adjustBbox(x - dpx, y - dpy, x2 + dpx, y2 + dpy);
        drawPosts(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "scope probe";
        arr[1] = "Vd = " + circuitFrame.getVoltageText(getVoltageDiff());
    }

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     * @param n2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
   public boolean getConnection(int n1, int n2) {
        return false;
    }
}
