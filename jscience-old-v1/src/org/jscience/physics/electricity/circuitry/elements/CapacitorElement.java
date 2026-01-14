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
 * @version $Revision: 1.3 $
  */
public class CapacitorElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public double capacitance;

    /**
     * DOCUMENT ME!
     */
    public double compResistance;

    /**
     * DOCUMENT ME!
     */
    public double voltdiff;

    /**
     * DOCUMENT ME!
     */
    public double voltSourceValue;

    /**
     * Creates a new CapacitorElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public CapacitorElement(int xx, int yy) {
        super(xx, yy);
        capacitance = 1e-5;
    }

    /**
     * Creates a new CapacitorElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public CapacitorElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        capacitance = new Double(st.nextToken()).doubleValue();
        voltdiff = new Double(st.nextToken()).doubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void setNodeVoltage(int n, double c) {
        super.setNodeVoltage(n, c);
        voltdiff = volts[0] - volts[1];
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        voltdiff = current = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'c';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "c " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        capacitance + " " + voltdiff;
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
        int x1a = (int) (x + ((dx * ((dn / 2) - 4)) / dn));
        int y1a = (int) (y + ((dy * ((dn / 2) - 4)) / dn));
        int x2a = (int) (x + ((dx * ((dn / 2) + 4)) / dn));
        int y2a = (int) (y + ((dy * ((dn / 2) + 4)) / dn));
        int hs = 12;
        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        setBbox(x, y, x2, y2);
        adjustBbox(x - dpx, y - dpy, x2 + dpx, y2 + dpy);
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x1a, y1a);
        setPowerColor(g, false);
        circuitFrame.drawThickLine(g, x1a - dpx, y1a - dpy, x1a + dpx, y1a + dpy);

        if (circuitFrame.powerCheckItem.getState()) {
            g.setColor(Color.gray);
        }

        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);
        setPowerColor(g, false);
        circuitFrame.drawThickLine(g, x2a - dpx, y2a - dpy, x2a + dpx, y2a + dpy);
        updateDotCount();

        if (circuitFrame.dragElement != this) {
            circuitFrame.drawDots(g, x, y, x1a, y1a, curcount);
            circuitFrame.drawDots(g, x2, y2, x2a, y2a, -curcount);
        }

        drawPosts(g);

        if (circuitFrame.showValuesCheckItem.getState()) {
            String s = circuitFrame.getShortUnitText(capacitance, "F");
            drawValues(g, s, (x2 + x) / 2, (y2 + y) / 2, dpx, dpy);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        // capacitor companion model using trapezoidal approximation
        // (Thevenin equivalent) consists of a voltage source in
        // series with a resistor
        circuitFrame.stampVoltageSource(nodes[0], nodes[2], voltSource);
        compResistance = circuitFrame.timeStep / (2 * capacitance);
        circuitFrame.stampResistor(nodes[2], nodes[1], compResistance);
    }

    /**
     * DOCUMENT ME!
     */
    public void startIteration() {
        voltSourceValue = -voltdiff - (current * compResistance);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        circuitFrame.updateVoltageSource(nodes[0], nodes[2], voltSource, voltSourceValue);
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
     * @return DOCUMENT ME!
     */
    public int getInternalNodeCount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "capacitor";
        getBasicInfo(arr);
        arr[3] = "C = " + circuitFrame.getUnitText(capacitance, "F");
        arr[4] = "P = " + circuitFrame.getUnitText(getPower(), "W");

        //double v = getVoltageDiff();
        //arr[4] = "U = " + getUnitText(.5*capacitance*v*v, "J");
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
            return new EditInfo("Capacitance (uF)", capacitance * 1e6, 0, 0);
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
        capacitance = ei.value * 1e-6;
    }
}
