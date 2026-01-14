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
public class GroundElement extends CircuitElement {
    /**
     * Creates a new GroundElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public GroundElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new GroundElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public GroundElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'g';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        setVoltageColor(g, 0);
        circuitFrame.drawThickLine(g, x, y, x2, y2);

        double dx = x2 - x;
        double dy = y2 - y;
        double dr = Math.sqrt((dx * dx) + (dy * dy));
        dx /= dr;
        dy /= dr;

        //drawThickLine(g, x, y, (int) (x2-dx*10), (int) (y2-dy*10));
        int i;

        for (i = 0; i != 3; i++) {
            int a = 10 - (i * 4);
            int b = i * 5; // -10;
            circuitFrame.drawThickLine(g, (int) (x2 + (dx * b) + (dy * a)),
                (int) ((y2 + (dy * b)) - (dx * a)),
                (int) ((x2 + (dx * b)) - (dy * a)),
                (int) (y2 + (dy * b) + (dx * a)));
        }

        doDots(g);
        setBbox(x, y, x2, y2);
        adjustBbox(x2 + (int) ((dx * 10) + (dy * 10)),
            y2 + (int) ((dy * 10) - (dx * 10)),
            x2 + (int) ((dx * 10) - (dy * 10)),
            y2 + (int) ((dy * 10) + (dx * 10)));
        drawPost(g, x, y, nodes[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void setCurrent(int x, double c) {
        current = -c;
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampVoltageSource(0, nodes[0], voltSource, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "ground";
        arr[1] = "I = " + circuitFrame.getCurrentText(getCurrent());
    }

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasGroundConnection(int n1) {
        return true;
    }
}
