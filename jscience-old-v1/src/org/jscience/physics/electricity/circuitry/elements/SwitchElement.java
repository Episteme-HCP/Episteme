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
public class SwitchElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public boolean open;

    /**
     * DOCUMENT ME!
     */
    public boolean momentary;

    /**
     * DOCUMENT ME!
     */
    public int switchx1;

    /**
     * DOCUMENT ME!
     */
    public int switchx2;

    /**
     * DOCUMENT ME!
     */
    public int switchy1;

    /**
     * DOCUMENT ME!
     */
    public int switchy2;

    /**
     * Creates a new SwitchElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public SwitchElement(int xx, int yy) {
        super(xx, yy);
        open = momentary = false;
    }

    /**
     * Creates a new SwitchElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     * @param mm DOCUMENT ME!
     */
    public SwitchElement(int xx, int yy, boolean mm) {
        super(xx, yy);
        open = mm;
        momentary = mm;
    }

    /**
     * Creates a new SwitchElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public SwitchElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        open = new Boolean(st.nextToken()).booleanValue();
        momentary = new Boolean(st.nextToken()).booleanValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 's';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "s " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        open + " " + momentary;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawSwitch(g, x, y, x2, y2, volts[0], volts[1], open);

        if (!open) {
            doDots(g);
        }

        drawPosts(g);
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        if (open) {
            current = 0;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        if (!open) {
            circuitFrame.stampVoltageSource(nodes[0], nodes[1], voltSource, 0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return (open) ? 0 : 1;
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
     * @param open DOCUMENT ME!
     */
    public void drawSwitch(Graphics g, int x1, int y1, int x2, int y2, double v1,
        double v2, boolean open) {
        int i;
        int ox = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;
        int openhs = 16;
        int hs = (open) ? openhs : 0;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        setBbox(x, y, x2, y2);

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

        switchx1 = x1;
        switchx2 = x2;
        switchy1 = y1;
        switchy2 = y2;

        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);

        if (circuitFrame.mouseElement != this) {
            g.setColor(Color.white);
        }

        circuitFrame.drawThickLine(g, x1, y1, x2 + dpx, y2 + dpy);

        int dpx0 = (int) ((openhs * dy) / dn);
        int dpy0 = (int) ((-openhs * dx) / dn);
        adjustBbox(x1, y1, x2 + dpx0, y2 + dpy0);
        adjustBbox(x1, y1, x2 - (dpx0 / 2), y2 - (dpy0 / 2));
    }

    /**
     * DOCUMENT ME!
     */
    public void mouseUp() {
        if (momentary) {
            toggle();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void toggle() {
        open = !open;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = (momentary) ? "push switch (SPST)" : "switch (SPST)";

        if (open) {
            arr[1] = "open";
            arr[2] = "Vd = " + circuitFrame.getVoltageDText(getVoltageDiff());
        } else {
            arr[1] = "closed";
            arr[2] = "I = " + circuitFrame.getCurrentDText(getCurrent());
        }
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
        return !open;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWire() {
        return true;
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
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Momentary Switch", momentary);

            return ei;
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
        if (n == 0) {
            momentary = ei.checkbox.getState();
        }
    }
}
