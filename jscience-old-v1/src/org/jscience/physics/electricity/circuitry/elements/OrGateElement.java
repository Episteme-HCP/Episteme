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

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class OrGateElement extends GateElement {
    /**
     * Creates a new OrGateElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public OrGateElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new OrGateElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public OrGateElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getGateName() {
        return "OR gate";
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));

        if (dn > 150) {
            setSize(2);
        }

        int hs = gheight;
        int dpx = (int) (Math.abs((hs * dy) / dn));
        int dpy = (int) (-Math.abs((hs * dx) / dn));
        int i;
        outpx = x2;
        outpy = y2;

        int ww = gwidth2; // was 24

        if (ww > (dn / 2)) {
            ww = (int) (dn / 2);
        }

        if (isInverting() && ((ww + 8) > (dn / 2))) {
            ww = (int) ((dn / 2) - 8);
        }

        int x1a = (int) (x + ((dx * ((dn / 2) - ww)) / dn));
        int y1a = (int) (y + ((dy * ((dn / 2) - ww)) / dn));
        int x2a = (int) (x + ((dx * ((dn / 2) + ww)) / dn));
        int y2a = (int) (y + ((dy * ((dn / 2) + ww)) / dn));
        int i0 = -inputCount / 2;
        inPosts = new Point[inputCount];
        inGates = new Point[inputCount];
        nodes = new int[getPostCount()];
        volts = new double[getPostCount()];

        for (i = 0; i != inputCount; i++, i0++) {
            if ((i0 == 0) && ((inputCount & 1) == 0)) {
                i0++;
            }

            inPosts[i] = new Point(x + (dpx * i0), y + (dpy * i0));
            inGates[i] = new Point(x1a + (dpx * i0), y1a + (dpy * i0));
            volts[i] = (lastOutput ^ isInverting()) ? 5 : 0;
        }

        if (isInverting()) {
            pcirclex = (int) (x + ((dx * ((dn / 2) + ww + 4)) / dn));
            pcircley = (int) (y + ((dy * ((dn / 2) + ww + 4)) / dn));
            outnx = (int) (x + ((dx * ((dn / 2) + ww + 6)) / dn));
            outny = (int) (y + ((dy * ((dn / 2) + ww + 6)) / dn));
        } else {
            outnx = x2a;
            outny = y2a;
        }

        // 0-15 = top curve, 16 = right, 17-32=bottom curve,
        // 33-37 = left curve
        int hs2 = gwidth * ((inputCount / 2) + 1);
        dpx = (int) (Math.abs((hs2 * dy) / dn));
        dpy = (int) (-Math.abs((hs2 * dx) / dn));
        triPointsX = new int[38];
        triPointsY = new int[38];

        if (this instanceof XorGateElement) {
            linePointsX = new int[5];
            linePointsY = new int[5];
        }

        for (i = 0; i != 16; i++) {
            double a = i / 16.;
            double b = 1 - (a * a);
            int xa = (int) (x + ((dx * ((dn / 2) + (ww * a))) / dn) +
                (dpx * b));
            int ya = (int) (y + ((dy * ((dn / 2) + (ww * a))) / dn) +
                (dpy * b));
            triPointsX[i] = xa;
            triPointsY[i] = ya;
            xa = (int) ((x + ((dx * ((dn / 2) + (ww * a))) / dn)) - (dpx * b));
            ya = (int) ((y + ((dy * ((dn / 2) + (ww * a))) / dn)) - (dpy * b));
            triPointsX[32 - i] = xa;
            triPointsY[32 - i] = ya;
        }

        for (i = 0; i != 5; i++) {
            double a = (i - 2) / 2.;
            double b = (4 * (1 - (a * a))) - 2;
            int xa = x1a + (int) (((dx * b) / dn) + (dpx * a));
            int ya = y1a + (int) (((dy * b) / dn) + (dpy * a));
            triPointsX[33 + i] = xa;
            triPointsY[33 + i] = ya;

            if (this instanceof XorGateElement) {
                linePointsX[i] = xa - (int) ((dx * 5) / dn);
                linePointsY[i] = ya - (int) ((dy * 5) / dn);
            }
        }

        triPointsX[16] = x2a;
        triPointsY[16] = y2a;
        setBbox(triPointsX[37], triPointsY[37], triPointsX[16], triPointsY[16]);
        adjustBbox(triPointsX[33], triPointsY[33], triPointsX[16],
            triPointsY[16]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean calcFunction() {
        int i;
        boolean f = false;

        for (i = 0; i != inputCount; i++)
            f |= getInput(i);

        return f;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 152;
    }
}
