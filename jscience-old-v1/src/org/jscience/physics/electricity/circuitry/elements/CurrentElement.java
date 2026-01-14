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
import org.jscience.physics.electricity.circuitry.gui.EditInfo;

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class CurrentElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public double currentValue;

    /**
     * Creates a new CurrentElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public CurrentElement(int xx, int yy) {
        super(xx, yy);
        currentValue = .01;
    }

    /**
     * Creates a new CurrentElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public CurrentElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);

        try {
            currentValue = new Double(st.nextToken()).doubleValue();
        } catch (Exception e) {
            currentValue = .01;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "i " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        currentValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'i';
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int cr = 12;
        int x1a = (int) (x + ((dx * ((dn / 2) - cr)) / dn));
        int y1a = (int) (y + ((dy * ((dn / 2) - cr)) / dn));
        int x2a = (int) (x + ((dx * ((dn / 2) + cr)) / dn));
        int y2a = (int) (y + ((dy * ((dn / 2) + cr)) / dn));
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x1a, y1a);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);
        setVoltageColor(g, (volts[0] + volts[1]) / 2);
        setPowerColor(g, false);

        int xm = (x + x2) / 2;
        int ym = (y + y2) / 2;
        circuitFrame.drawThickCircle(g, xm, ym, cr);

        int ar = cr / 2;
        int x1b = (int) (x + ((dx * ((dn / 2) + ar)) / dn));
        int y1b = (int) (y + ((dy * ((dn / 2) + ar)) / dn));
        int x2b = (int) (x + ((dx * ((dn / 2) - ar)) / dn));
        int y2b = (int) (y + ((dy * ((dn / 2) - ar)) / dn));
        circuitFrame.drawThickLine(g, x1b, y1b, x2b, y2b);

        int[] arrowPointsX = new int[3];
        int[] arrowPointsY = new int[3];
        double l = Math.sqrt(((xm - x1b) * (xm - x1b)) +
                ((ym - y1b) * (ym - y1b)));
        double hatx = (x1b - xm) / l;
        double haty = (y1b - ym) / l;
        int ax = x1b;
        int ay = y1b;
        final double as = 4;
        arrowPointsX[0] = ax;
        arrowPointsY[0] = ay;
        arrowPointsX[1] = (int) ((ax + (haty * as)) - (hatx * as));
        arrowPointsY[1] = (int) (ay - (hatx * as) - (haty * as));
        arrowPointsX[2] = (int) (ax - (haty * as) - (hatx * as));
        arrowPointsY[2] = (int) ((ay + (hatx * as)) - (haty * as));

        int i;
        int j;

        for (i = 0; i != 4; i++) {
            for (j = 0; j != 3; j++) {
                if (i == 1) {
                    arrowPointsX[j]++;
                } else if (i == 2) {
                    arrowPointsY[j]++;
                } else if (i == 3) {
                    arrowPointsX[j]--;
                }
            }

            g.fillPolygon(arrowPointsX, arrowPointsY, 3);
        }

        setBbox(x, y, x2, y2);
        adjustBbox(xm - cr, ym - cr, xm + cr, ym + cr);
        doDots(g);
        drawPosts(g);
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        current = currentValue;
        circuitFrame.stampCurrentSource(nodes[0], nodes[1], current);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Current (A)", currentValue, 0, .1);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param ei DOCUMENT ME!
     */
    public void setEditValue(int n, EditInfo ei) {
        currentValue = ei.value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "current source";
        getBasicInfo(arr);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[1] - volts[0];
    }
}
