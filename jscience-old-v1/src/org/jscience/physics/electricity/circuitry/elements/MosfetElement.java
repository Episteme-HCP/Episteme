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
public class MosfetElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public int pnp;

    /**
     * DOCUMENT ME!
     */
    public int FLAG_PNP = 1;

    // post coordinates
    /**
     * DOCUMENT ME!
     */
    public int src_px;

    // post coordinates
    /**
     * DOCUMENT ME!
     */
    public int src_py;

    // post coordinates
    /**
     * DOCUMENT ME!
     */
    public int drn_px;

    // post coordinates
    /**
     * DOCUMENT ME!
     */
    public int drn_py;

    // midpoint coordinates
    /**
     * DOCUMENT ME!
     */
    public int src_mx;

    // midpoint coordinates
    /**
     * DOCUMENT ME!
     */
    public int src_my;

    // midpoint coordinates
    /**
     * DOCUMENT ME!
     */
    public int drn_mx;

    // midpoint coordinates
    /**
     * DOCUMENT ME!
     */
    public int drn_my;

    // gate coordinates
    /**
     * DOCUMENT ME!
     */
    public int gate1x;

    // gate coordinates
    /**
     * DOCUMENT ME!
     */
    public int gate1y;

    // gate coordinates
    /**
     * DOCUMENT ME!
     */
    public int gate2x;

    // gate coordinates
    /**
     * DOCUMENT ME!
     */
    public int gate2y;

    // gate coordinates
    /**
     * DOCUMENT ME!
     */
    public int gate3x;

    // gate coordinates
    /**
     * DOCUMENT ME!
     */
    public int gate3y;

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
    public int pcircler;

    /**
     * DOCUMENT ME!
     */
    public double lastv1;

    /**
     * DOCUMENT ME!
     */
    public double lastv2;

    /**
     * DOCUMENT ME!
     */
    public double ids;

    /**
     * DOCUMENT ME!
     */
    public int mode = 0;

    /**
     * DOCUMENT ME!
     */
    public double gm = 0;

    /**
     * Creates a new MosfetElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     * @param pnpflag DOCUMENT ME!
     */
    public MosfetElement(int xx, int yy, boolean pnpflag) {
        super(xx, yy);
        pnp = (pnpflag) ? (-1) : 1;
        flags = (pnpflag) ? FLAG_PNP : 0;
        noDiagonal = true;
    }

    /**
     * Creates a new MosfetElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public MosfetElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        pnp = ((f & FLAG_PNP) != 0) ? (-1) : 1;
        noDiagonal = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getThreshold() {
        return 1.5;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getBeta() {
        return .02;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nonLinear() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        lastv1 = lastv2 = volts[0] = volts[1] = volts[2] = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'f';
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        setBbox(x, y, drn_px, drn_py);
        adjustBbox(x, y, src_px, src_py);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, src_px, src_py, src_mx, src_my);
        setVoltageColor(g, volts[2]);
        circuitFrame.drawThickLine(g, drn_mx, drn_my, drn_px, drn_py);

        int segments = 6;
        int i;
        setPowerColor(g, true);

        for (i = 0; i != segments; i++) {
            double v = volts[1] + (((volts[2] - volts[1]) * i) / segments);
            setVoltageColor(g, v);

            int xa = src_mx + (((drn_mx - src_mx) * i) / segments);
            int ya = src_my + (((drn_my - src_my) * i) / segments);
            int xb = src_mx + (((drn_mx - src_mx) * (i + 1)) / segments);
            int yb = src_my + (((drn_my - src_my) * (i + 1)) / segments);
            circuitFrame.drawThickLine(g, xa, ya, xb, yb);
        }

        if (circuitFrame.powerCheckItem.getState()) {
            g.setColor(Color.gray);
        }

        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, gate2x, gate2y);
        circuitFrame.drawThickLine(g, gate1x, gate1y, gate3x, gate3y);

        if (pnp == -1) {
            circuitFrame.drawThickCircle(g, pcirclex, pcircley, pcircler);
        }

        curcount = updateDotCount(-ids, curcount);
        circuitFrame.drawDots(g, src_px, src_py, src_mx, src_my, curcount);
        circuitFrame.drawDots(g, src_mx, src_my, drn_mx, drn_my, curcount);
        circuitFrame.drawDots(g, drn_mx, drn_my, drn_px, drn_py, curcount);
        drawPost(g, x, y, nodes[0]);
        drawPost(g, src_px, src_py, nodes[1]);
        drawPost(g, drn_px, drn_py, nodes[2]);
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
                        : ((n == 1) ? new Point(src_px, src_py)
                                    : new Point(drn_px, drn_py));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCurrent() {
        return ids;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPower() {
        return ids * (volts[2] - volts[1]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 3;
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        // find the coordinates of the various points we need to draw
        // the MOSFET.
        int hs = 16;
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) (Math.abs((hs * dy) / dn));
        int dpy = (int) (-Math.abs((hs * dx) / dn));
        src_px = x2 - dpx;
        src_py = y2 - dpy;
        drn_px = x2 + dpx;
        drn_py = y2 + dpy;

        int dax = (int) ((12 * dx) / dn);
        int day = (int) ((12 * dy) / dn);
        src_mx = src_px - dax;
        src_my = src_py - day;
        drn_mx = drn_px - dax;
        drn_my = drn_py - day;
        dax = (int) ((8 * dx) / dn);
        day = (int) ((8 * dy) / dn);
        gate1x = src_mx - dax;
        gate1y = src_my - day;
        gate3x = drn_mx - dax;
        gate3y = drn_my - day;
        gate2x = (gate1x + gate3x) / 2;
        gate2y = (gate1y + gate3y) / 2;

        if (pnp == -1) {
            gate2x -= dax;
            gate2y -= day;
            pcirclex = gate2x + (dax / 2) + (day / 8);
            pcircley = gate2y + (day / 2) + (dax / 8);
            pcircler = (int) Math.abs((dax / 2) + (day / 2)) - 1;

            if ((dx < 0) || (dy < 0)) {
                pcirclex++;
                pcircley++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampNonLinear(nodes[1]);
        circuitFrame.stampNonLinear(nodes[2]);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        double[] vs = new double[3];
        vs[0] = volts[0];
        vs[1] = volts[1];
        vs[2] = volts[2];

        if (vs[1] > (lastv1 + .5)) {
            vs[1] = lastv1 + .5;
        }

        if (vs[1] < (lastv1 - .5)) {
            vs[1] = lastv1 - .5;
        }

        if (vs[2] > (lastv2 + .5)) {
            vs[2] = lastv2 + .5;
        }

        if (vs[2] < (lastv2 - .5)) {
            vs[2] = lastv2 - .5;
        }

        int source = 1;
        int drain = 2;

        if ((pnp * vs[1]) > (pnp * vs[2])) {
            source = 2;
            drain = 1;
        }

        int gate = 0;
        double vgs = vs[gate] - vs[source];
        double vds = vs[drain] - vs[source];

        if ((Math.abs(lastv1 - vs[1]) > .01) ||
                (Math.abs(lastv2 - vs[2]) > .01)) {
            circuitFrame.converged = false;
        }

        lastv1 = vs[1];
        lastv2 = vs[2];

        double realvgs = vgs;
        double realvds = vds;
        vgs *= pnp;
        vds *= pnp;
        ids = 0;
        gm = 0;

        double Gds = 0;
        double vt = getThreshold();
        double beta = getBeta();

        if ((vgs > .5) && this instanceof JfetElement) {
            circuitFrame.stop("JFET is reverse biased!", this);

            return;
        }

        if (vgs < vt) {
            // should be all zero, but that causes a singular matrix,
            // so instead we treat it as a large resistor
            Gds = 1e-8;
            ids = vds * Gds;
            mode = 0;
        } else if (vds < (vgs - vt)) {
            // linear
            ids = beta * (((vgs - vt) * vds) - (vds * vds * .5));
            gm = beta * vds;
            Gds = beta * (vgs - vds - vt);
            mode = 1;
        } else {
            // saturation; Gds = 0
            gm = beta * (vgs - vt);
            // use very small Gds to avoid nonconvergence
            Gds = 1e-8;
            ids = (.5 * beta * (vgs - vt) * (vgs - vt)) +
                ((vds - (vgs - vt)) * Gds);
            mode = 2;
        }

        double rs = (-pnp * ids) + (Gds * realvds) + (gm * realvgs);
        //System.out.println("M " + vds + " " + vgs + " " + ids + " " + gm + " "+ Gds + " " + volts[0] + " " + volts[1] + " " + volts[2] + " " + source + " " + rs + " " + this);
        circuitFrame.stampMatrix(nodes[drain], nodes[drain], Gds);
        circuitFrame.stampMatrix(nodes[drain], nodes[source], -Gds - gm);
        circuitFrame.stampMatrix(nodes[drain], nodes[gate], gm);

        circuitFrame.stampMatrix(nodes[source], nodes[drain], -Gds);
        circuitFrame.stampMatrix(nodes[source], nodes[source], Gds + gm);
        circuitFrame.stampMatrix(nodes[source], nodes[gate], -gm);

        circuitFrame.stampRightSide(nodes[drain], rs);
        circuitFrame.stampRightSide(nodes[source], -rs);

        if (((source == 2) && (pnp == 1)) || ((source == 1) && (pnp == -1))) {
            ids = -ids;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    public void getFetInfo(String[] arr, String n) {
        arr[0] = ((pnp == -1) ? "p-" : "n-") + n;
        arr[1] = "Ids = " + circuitFrame.getCurrentText(ids);
        arr[2] = "Vgs = " + circuitFrame.getVoltageText(volts[0] - volts[1]);
        arr[3] = "Vds = " + circuitFrame.getVoltageText(volts[2] - volts[1]);
        arr[4] = (mode == 0) ? "off" : ((mode == 1) ? "linear" : "saturation");
        arr[5] = "gm = " + circuitFrame.getUnitText(gm, "mho");
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        getFetInfo(arr, "MOSFET");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canViewInScope() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[2] - volts[1];
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
        return !((n1 == 0) || (n2 == 0));
    }
}
