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
public class AnalogSwitch2Element extends AnalogSwitchElement {
    // posts
    /**
     * DOCUMENT ME!
     */
    public int x3a;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int y3a;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int x3b;

    // posts
    /**
     * DOCUMENT ME!
     */
    int y3b;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int xsw;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int ysw;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    int spxa;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    public int spya;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    public int spxb;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    public int spyb;

    /**
     * DOCUMENT ME!
     */
    public int spx1;

    /**
     * DOCUMENT ME!
     */
    public int spy1;

    /**
     * Creates a new AnalogSwitch2Element object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public AnalogSwitch2Element(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new AnalogSwitch2Element object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public AnalogSwitch2Element(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawSwitch(g, x, y, x2, y2, volts[0], volts[1], volts[2], open);
        updateDotCount();
        circuitFrame.drawDots(g, x, y, spx1, spy1, curcount);

        if (!open) {
            circuitFrame.drawDots(g, spxa, spya, x3a, y3a, curcount);
        } else {
            circuitFrame.drawDots(g, spxb, spyb, x3b, y3b, curcount);
        }

        drawPost(g, x, y, nodes[0]);
        drawPost(g, x3a, y3a, nodes[1]);
        drawPost(g, x3b, y3b, nodes[2]);
        drawPost(g, xsw, ysw, nodes[3]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getPost(int n) {
        return (n == 0) ? new Point(x, y)
                        : ((n == 1) ? new Point(x3a, y3a)
                                    : ((n == 2) ? new Point(x3b, y3b)
                                                : new Point(xsw, ysw)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 4;
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        int hs = 16;
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        x3a = x2 + dpx;
        y3a = y2 + dpy;
        x3b = x2 - dpx;
        y3b = y2 - dpy;
        xsw = ((x + x2) / 2) + dpx;
        ysw = ((y + y2) / 2) + dpy;
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
     * @param v3 DOCUMENT ME!
     * @param open DOCUMENT ME!
     */
    public void drawSwitch(Graphics g, int x1, int y1, int x2, int y2, double v1,
        double v2, double v3, boolean open) {
        int i;
        int ox = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;
        int hs = 16;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        setBbox(x1, y1, x2, y2);

        if (dn > 40) {
            int x1a = (int) (x1 + ((dx * ((dn / 2) - 16)) / dn));
            int y1a = (int) (y1 + ((dy * ((dn / 2) - 16)) / dn));
            int x2a = (int) (x1 + ((dx * ((dn / 2) + 16)) / dn));
            int y2a = (int) (y1 + ((dy * ((dn / 2) + 16)) / dn));
            setVoltageColor(g, v1);
            circuitFrame.drawThickLine(g, x1, y1, x1a, y1a);
            setVoltageColor(g, v2);
            spxa = x2a + dpx;
            spya = y2a + dpy;
            spxb = x2a - dpx;
            spyb = y2a - dpy;
            circuitFrame.drawThickLine(g, spxa, spya, x2 + dpx, y2 + dpy);
            setVoltageColor(g, v3);
            circuitFrame.drawThickLine(g, spxb, spyb, x2 - dpx, y2 - dpy);
            dn = 32;
            x1 = x1a;
            y1 = y1a;
            x2 = x2a;
            y2 = y2a;
            dx = x2 - x1;
            dy = y2 - y1;
        }

        spx1 = x1;
        spy1 = y1;
        g.setColor(Color.lightGray);

        int dir = (open) ? (-1) : 1;
        circuitFrame.drawThickLine(g, x1, y1, x2 + (dpx * dir), y2 + (dpy * dir));
        adjustBbox(x1, y1, x2 + dpx, y2 + dpy);
        adjustBbox(x1, y1, x2 - dpx, y2 - dpy);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 160;
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        if (open) {
            current = (volts[0] - volts[2]) / resistance;
        } else {
            current = (volts[0] - volts[1]) / resistance;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampNonLinear(nodes[0]);
        circuitFrame.stampNonLinear(nodes[1]);
        circuitFrame.stampNonLinear(nodes[2]);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        open = (volts[3] < 2.5);

        if (open) {
            circuitFrame.stampResistor(nodes[0], nodes[2], resistance);
        } else {
            circuitFrame.stampResistor(nodes[0], nodes[1], resistance);
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
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "analog switch (DPST)";
        arr[1] = "I = " + circuitFrame.getCurrentDText(getCurrent());
    }
}
