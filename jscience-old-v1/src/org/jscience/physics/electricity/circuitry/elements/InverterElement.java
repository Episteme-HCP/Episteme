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
public class InverterElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public double slewRate; // V/ns

    /**
     * DOCUMENT ME!
     */
    public int in_nx;

    /**
     * DOCUMENT ME!
     */
    public int in_ny;

    /**
     * DOCUMENT ME!
     */
    public int outnx;

    /**
     * DOCUMENT ME!
     */
    public int outny;

    /**
     * DOCUMENT ME!
     */
    public int pcirclex;

    /**
     * DOCUMENT ME!
     */
    public int pcircley;

    /**
     * DOCUMENT ME!
     */
    public int[] triPointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] triPointsY;

    /**
     * Creates a new InverterElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public InverterElement(int xx, int yy) {
        super(xx, yy);
        noDiagonal = true;
        slewRate = .5;
    }

    /**
     * Creates a new InverterElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public InverterElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        noDiagonal = true;

        try {
            slewRate = new Double(st.nextToken()).doubleValue();
        } catch (Exception e) {
            slewRate = .5;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "I " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        slewRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'I';
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawPosts(g);
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, in_nx, in_ny);
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.lightGray);
        circuitFrame.drawThickPolygon(g, triPointsX, triPointsY, 3);
        circuitFrame.drawThickCircle(g, pcirclex, pcircley, 3);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, outnx, outny, x2, y2);
        curcount = updateDotCount(current, curcount);
        circuitFrame.drawDots(g, outnx, outny, x2, y2, curcount);
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        int hs = 8;
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) (Math.abs((hs * dy) / dn));
        int dpy = (int) (-Math.abs((hs * dx) / dn));

        int ww = 16;

        if (ww > (dn / 2)) {
            ww = (int) (dn / 2);
        }

        int x1a = (int) (x + ((dx * ((dn / 2) - ww)) / dn));
        int y1a = (int) (y + ((dy * ((dn / 2) - ww)) / dn));
        int x2a = (int) (x + ((dx * (((dn / 2) + ww) - 5)) / dn));
        int y2a = (int) (y + ((dy * (((dn / 2) + ww) - 5)) / dn));
        in_nx = x1a;
        in_ny = y1a;
        outnx = (int) (x + ((dx * ((dn / 2) + ww + 2)) / dn));
        outny = (int) (y + ((dy * ((dn / 2) + ww + 2)) / dn));

        pcirclex = (int) (x + ((dx * (((dn / 2) + ww) - 2)) / dn));
        pcircley = (int) (y + ((dy * (((dn / 2) + ww) - 2)) / dn));

        triPointsX = new int[3];
        triPointsY = new int[3];
        triPointsX[0] = x1a + (dpx * 2);
        triPointsY[0] = y1a + (dpy * 2);
        triPointsX[1] = x1a - (dpx * 2);
        triPointsY[1] = y1a - (dpy * 2);
        triPointsX[2] = x2a;
        triPointsY[2] = y2a;
        setBbox(triPointsX[0], triPointsY[0], triPointsX[1], triPointsY[1]);
        adjustBbox(x, y, x2, y2);
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
     */
    public void stamp() {
        circuitFrame.stampVoltageSource(0, nodes[1], voltSource);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        double v0 = volts[1];
        double out = (volts[0] > 2.5) ? 0 : 5;
        double maxStep = slewRate * circuitFrame.timeStep * 1e9;
        out = Math.max(Math.min(v0 + maxStep, out), v0 - maxStep);
        circuitFrame.updateVoltageSource(0, nodes[1], voltSource, out);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "inverter";
        arr[1] = "Vi = " + circuitFrame.getVoltageText(volts[0]);
        arr[2] = "Vo = " + circuitFrame.getVoltageText(volts[1]);
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
            return new EditInfo("Slew Rate (V/ns)", slewRate, 0, 0);
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
        slewRate = ei.value;
    }

    // there is no current path through the inverter input, but there
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

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasGroundConnection(int n1) {
        return (n1 == 1);
    }
}
