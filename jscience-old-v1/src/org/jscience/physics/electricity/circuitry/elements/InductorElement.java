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
public class InductorElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public double inductance;

    /**
     * DOCUMENT ME!
     */
    public double compResistance;

    /**
     * DOCUMENT ME!
     */
    public double curSourceValue;

    /**
     * Creates a new InductorElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public InductorElement(int xx, int yy) {
        super(xx, yy);
        inductance = 1;
    }

    /**
     * Creates a new InductorElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public InductorElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        inductance = new Double(st.nextToken()).doubleValue();
        current = new Double(st.nextToken()).doubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'l';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "l " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        inductance + " " + current;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawInductor(g, x, y, x2, y2, volts[0], volts[1]);
        doDots(g);
        drawPosts(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     */
    public void drawInductor(Graphics g, int x1, int y1, int x2, int y2, double v1,
        double v2) {
        int segments = 30;
        int i;
        int dx = x2 - x1;
        int dy = y2 - y1;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        setBbox(x1, y1, x2, y2);

        if (dn > 40) {
            int x1a = (int) (x1 + ((dx * ((dn / 2) - 16)) / dn));
            int y1a = (int) (y1 + ((dy * ((dn / 2) - 16)) / dn));
            int x2a = (int) (x1 + ((dx * ((dn / 2) + 16)) / dn));
            int y2a = (int) (y1 + ((dy * ((dn / 2) + 16)) / dn));
            setVoltageColor(g, v1);
            circuitFrame.drawThickLine(g, x1, y1, x1a, y1a);
            setVoltageColor(g, v2);
            circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);
            dn = 32;
            x1 = x1a;
            y1 = y1a;
            x2 = x2a;
            y2 = y2a;
            dx = x2 - x1;
            dy = y2 - y1;
        }

        int xa = x1;
        int ya = y1;
        int dpx0 = (int) ((8 * dy) / dn);
        int dpy0 = (int) ((-8 * dx) / dn);
        adjustBbox(x1 - dpx0, y1 - dpy0, x2 + dpx0, y2 + dpy0);
        setPowerColor(g, false);

        for (i = 0; i != segments; i++) {
            double cx = ((((i + 1) * 6.) / segments) % 2) - 1;
            double hs = 8 * Math.sqrt(1 - (cx * cx));

            if (hs < 0) {
                hs = -hs;
            }

            int dpx = (int) ((hs * dy) / dn);
            int dpy = (int) ((-hs * dx) / dn);
            double v = v1 + (((v2 - v1) * i) / segments);
            setVoltageColor(g, v);

            int xb = x1 + ((dx * (i + 1)) / segments) + dpx;
            int yb = y1 + ((dy * (i + 1)) / segments) + dpy;
            circuitFrame.drawThickLine(g, xa, ya, xb, yb);
            xa = xb;
            ya = yb;
        }

        if (circuitFrame.showValuesCheckItem.getState()) {
            String s = circuitFrame.getShortUnitText(inductance, "H");
            drawValues(g, s, (x2 + x1) / 2, (y2 + y1) / 2, dpx0, dpy0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        current = volts[0] = volts[1] = 0;
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        // inductor companion model using trapezoidal approximation
        // (Norton equivalent) consists of a current source in
        // parallel with a resistor.
        compResistance = (2 * inductance) / circuitFrame.timeStep;
        circuitFrame.stampResistor(nodes[0], nodes[1], compResistance);
        circuitFrame.stampRightSide(nodes[0]);
        circuitFrame.stampRightSide(nodes[1]);
    }

    /**
     * DOCUMENT ME!
     */
    public void startIteration() {
        double voltdiff = volts[0] - volts[1];
        curSourceValue = (voltdiff / compResistance) + current;

        //System.out.println("ind " + this + " " + current + " " + voltdiff);
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        double voltdiff = volts[0] - volts[1];
        current = (voltdiff / compResistance) + curSourceValue;
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        circuitFrame.stampCurrentSource(nodes[0], nodes[1], curSourceValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "inductor";
        getBasicInfo(arr);
        arr[3] = "L = " + circuitFrame.getUnitText(inductance, "H");
        arr[4] = "P = " + circuitFrame.getUnitText(getPower(), "W");
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
            return new EditInfo("Inductance (H)", inductance, 0, 0);
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
        inductance = ei.value;
    }
}
