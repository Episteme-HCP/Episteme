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
import org.jscience.physics.electricity.circuitry.Scope;

import java.awt.*;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class TransistorElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public static final double leakage = 1e-13; // 1e-6;

    /**
     * DOCUMENT ME!
     */
   public  static final double vt = .025;

    /**
     * DOCUMENT ME!
     */
    public static final double vdcoef = 1 / vt;

    /**
     * DOCUMENT ME!
     */
    public static final double fgain = .99;

    /**
     * DOCUMENT ME!
     */
    public static final double rgain = .5;

    /**
     * DOCUMENT ME!
     */
    public int pnp;

    /**
     * DOCUMENT ME!
     */
    public int collx;

    /**
     * DOCUMENT ME!
     */
    public int colly;

    /**
     * DOCUMENT ME!
     */
    public int emitx;

    /**
     * DOCUMENT ME!
     */
    public int emity;

    /**
     * DOCUMENT ME!
     */
    public int midpx;

    /**
     * DOCUMENT ME!
     */
    public int midpy;

    /**
     * DOCUMENT ME!
     */
    public int col2x;

    /**
     * DOCUMENT ME!
     */
    public int col2y;

    /**
     * DOCUMENT ME!
     */
    public int emi2x;

    /**
     * DOCUMENT ME!
     */
    public int emi2y;

    /**
     * DOCUMENT ME!
     */
    public int[] arrowPointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] arrowPointsY;

    /**
     * DOCUMENT ME!
     */
    public int rectX;

    /**
     * DOCUMENT ME!
     */
    public int rectY;

    /**
     * DOCUMENT ME!
     */
    public int rectW;

    /**
     * DOCUMENT ME!
     */
    public int rectH;

    /**
     * DOCUMENT ME!
     */
    public double ic;

    /**
     * DOCUMENT ME!
     */
    public double ie;

    /**
     * DOCUMENT ME!
     */
    public double ib;

    /**
     * DOCUMENT ME!
     */
    public double curcount_c;

    /**
     * DOCUMENT ME!
     */
    public double curcount_e;

    /**
     * DOCUMENT ME!
     */
    public double curcount_b;

    /**
     * DOCUMENT ME!
     */
    public double vcrit;

    /**
     * DOCUMENT ME!
     */
    public double lastvbc;

    /**
     * DOCUMENT ME!
     */
    public double lastvbe;

    /**
     * Creates a new TransistorElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     * @param pnpflag DOCUMENT ME!
     */
    public TransistorElement(int xx, int yy, boolean pnpflag) {
        super(xx, yy);
        pnp = (pnpflag) ? (-1) : 1;
        setup();
    }

    /**
     * Creates a new TransistorElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public TransistorElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        pnp = new Integer(st.nextToken()).intValue();

        try {
            lastvbe = new Double(st.nextToken()).doubleValue();
            lastvbc = new Double(st.nextToken()).doubleValue();
            volts[0] = 0;
            volts[1] = -lastvbe;
            volts[2] = -lastvbc;
        } catch (Exception e) {
        }

        setup();
    }

    /**
     * DOCUMENT ME!
     */
    public void setup() {
        vcrit = vt * Math.log(vt / (Math.sqrt(2) * leakage));
        noDiagonal = true;
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
        volts[0] = volts[1] = volts[2] = 0;
        lastvbc = lastvbe = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 't';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "t " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        pnp + " " + (volts[0] - volts[1]) + " " + (volts[0] - volts[2]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        if (arrowPointsX == null) {
            return;
        }

        setBbox(x, y, collx, colly);
        adjustBbox(x, y, emitx, emity);
        setPowerColor(g, true);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, col2x, col2y, collx, colly);
        setVoltageColor(g, volts[2]);
        circuitFrame.drawThickLine(g, emi2x, emi2y, emitx, emity);
        g.setColor(Color.lightGray);
        g.fillPolygon(arrowPointsX, arrowPointsY, 3);
        setVoltageColor(g, volts[0]);

        if (circuitFrame.powerCheckItem.getState()) {
            g.setColor(Color.gray);
        }

        circuitFrame.drawThickLine(g, midpx, midpy, x, y);
        curcount_b = updateDotCount(-ib, curcount_b);
        circuitFrame.drawDots(g, midpx, midpy, x, y, curcount_b);
        curcount_c = updateDotCount(-ic, curcount_c);
        circuitFrame.drawDots(g, col2x, col2y, collx, colly, curcount_c);
        curcount_e = updateDotCount(-ie, curcount_e);
        circuitFrame.drawDots(g, emi2x, emi2y, emitx, emity, curcount_e);
        setVoltageColor(g, volts[0]);
        setPowerColor(g, true);
        g.fillRect(rectX, rectY, rectW, rectH);
        drawPost(g, x, y, nodes[0]);
        drawPost(g, collx, colly, nodes[1]);
        drawPost(g, emitx, emity, nodes[2]);
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
                        : ((n == 1) ? new Point(collx, colly)
                                    : new Point(emitx, emity));
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
     *
     * @return DOCUMENT ME!
     */
    public double getPower() {
        return ((volts[0] - volts[2]) * ib) + ((volts[1] - volts[2]) * ic);
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        int hs = 16;
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) (Math.abs((hs * dy) / dn));
        int dpy = pnp * (int) (-Math.abs((hs * dx) / dn));
        collx = x2 + dpx;
        colly = y2 + dpy;
        emitx = x2 - dpx;
        emity = y2 - dpy;

        midpx = x;
        midpy = y;

        if (dn > 16) {
            midpx = (int) (x + ((dx * (dn - 16)) / dn));
            midpy = (int) (y + ((dy * (dn - 16)) / dn));
        }

        int bar1x = midpx + dpx;
        int bar1y = midpy + dpy;
        int bar2x = midpx - dpx;
        int bar2y = midpy - dpy;

        if (dy == 0) {
            rectX = bar1x - 2;
            rectY = circuitFrame.min(bar1y, bar2y);
            rectW = bar2x - bar1x + 5;
            rectH = pnp * (bar2y - bar1y);
        } else {
            rectX = bar2x;
            rectY = bar1y - 2;
            rectW = bar1x - bar2x;
            rectH = bar2y - bar1y + 5;
        }

        col2x = ((bar1x * 2) + bar2x) / 3;
        col2y = ((bar1y * 2) + bar2y) / 3;
        emi2x = (bar1x + (bar2x * 2)) / 3;
        emi2y = (bar1y + (bar2y * 2)) / 3;
        arrowPointsX = new int[3];
        arrowPointsY = new int[3];

        double l = Math.sqrt(((emi2x - emitx) * (emi2x - emitx)) +
                ((emi2y - emity) * (emi2y - emity)));
        double hatx = (pnp * (emitx - emi2x)) / l;
        double haty = (pnp * (emity - emi2y)) / l;
        int ax = (pnp == -1) ? (((emi2x * 4) + emitx) / 5) : emitx;
        int ay = (pnp == -1) ? (((emi2y * 4) + emity) / 5) : emity;
        final double as = 7;
        arrowPointsX[0] = ax;
        arrowPointsY[0] = ay;
        arrowPointsX[1] = (int) ((ax + (haty * as)) - (hatx * as));
        arrowPointsY[1] = (int) (ay - (hatx * as) - (haty * as));
        arrowPointsX[2] = (int) (ax - (haty * as) - (hatx * as));
        arrowPointsY[2] = (int) ((ay + (hatx * as)) - (haty * as));
    }

    /**
     * DOCUMENT ME!
     *
     * @param vnew DOCUMENT ME!
     * @param vold DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double limitStep(double vnew, double vold) {
        // this is what spice does for limiting
        double arg;
        double oo = vnew;

        if ((vnew > vcrit) && (Math.abs(vnew - vold) > (vt + vt))) {
            if (vold > 0) {
                arg = 1 + ((vnew - vold) / vt);

                if (arg > 0) {
                    vnew = vold + (vt * Math.log(arg));
                } else {
                    vnew = vcrit;
                }
            } else {
                vnew = vt * Math.log(vnew / vt);
            }

            circuitFrame.converged = false;

            //System.out.println(vnew + " " + oo + " " + vold);
        }

        return (vnew);
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
        double vbc = volts[0] - volts[1]; // typically negative
        double vbe = volts[0] - volts[2]; // typically positive

        if ((Math.abs(vbc - lastvbc) > .01) || // .01
                (Math.abs(vbe - lastvbe) > .01)) {
            circuitFrame.converged = false;
        }

        //if (subIterations > 300 && subIterations < 330)
        //System.out.print("T " + vbc + " " + vbe + "\n");
        vbc = pnp * limitStep(pnp * vbc, pnp * lastvbc);
        vbe = pnp * limitStep(pnp * vbe, pnp * lastvbe);
        lastvbc = vbc;
        lastvbe = vbe;

        double pcoef = vdcoef * pnp;
        double expbc = Math.exp(vbc * pcoef);

        /*if (expbc > 1e13 || Double.isInfinite(expbc))
          expbc = 1e13;*/
        double expbe = Math.exp(vbe * pcoef);

        if (expbe < 1) {
            expbe = 1;
        }

        /*if (expbe > 1e13 || Double.isInfinite(expbe))
          expbe = 1e13;*/
        ie = pnp * leakage * (-(expbe - 1) + (rgain * (expbc - 1)));
        ic = pnp * leakage * ((fgain * (expbe - 1)) - (expbc - 1));
        ib = -(ie + ic);

        //System.out.println("gain " + ic/ib);
        //System.out.print("T " + vbc + " " + vbe + " " + ie + " " + ic + "\n");
        double gee = -leakage * vdcoef * expbe;
        double gec = rgain * leakage * vdcoef * expbc;
        double gce = -gee * fgain;
        double gcc = -gec * (1 / rgain);
        /*System.out.print("gee = " + gee + "\n");
        System.out.print("gec = " + gec + "\n");
        System.out.print("gce = " + gce + "\n");
        System.out.print("gcc = " + gcc + "\n");
        System.out.print("gce+gcc = " + (gce+gcc) + "\n");
        System.out.print("gee+gec = " + (gee+gec) + "\n");*/

        // stamps from page 302 of Pillage.  Node 0 is the base,
        // node 1 the collector, node 2 the emitter
        circuitFrame.stampMatrix(nodes[0], nodes[0], -gee - gec - gce - gcc);
        circuitFrame.stampMatrix(nodes[0], nodes[1], gec + gcc);
        circuitFrame.stampMatrix(nodes[0], nodes[2], gee + gce);
        circuitFrame.stampMatrix(nodes[1], nodes[0], gce + gcc);
        circuitFrame.stampMatrix(nodes[1], nodes[1], -gcc);
        circuitFrame.stampMatrix(nodes[1], nodes[2], -gce);
        circuitFrame.stampMatrix(nodes[2], nodes[0], gee + gec);
        circuitFrame.stampMatrix(nodes[2], nodes[1], -gec);
        circuitFrame.stampMatrix(nodes[2], nodes[2], -gee);

        // we are solving for v(k+1), not delta v, so we use formula
        // 10.5.13, multiplying J by v(k)
        circuitFrame.stampRightSide(nodes[0], -ib - ((gec + gcc) * vbc) -
            ((gee + gce) * vbe));
        circuitFrame.stampRightSide(nodes[1], -ic + (gce * vbe) + (gcc * vbc));
        circuitFrame.stampRightSide(nodes[2], -ie + (gee * vbe) + (gec * vbc));
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "transistor (" + ((pnp == -1) ? "PNP)" : "NPN)");

        double vbc = volts[0] - volts[1];
        double vbe = volts[0] - volts[2];
        double vce = volts[1] - volts[2];
        arr[1] = "Vbe = " + circuitFrame.getVoltageText(vbe);
        arr[2] = "Vbc = " + circuitFrame.getVoltageText(vbc);
        arr[3] = "Vce = " + circuitFrame.getVoltageText(vce);
        arr[4] = "Ic = " + circuitFrame.getCurrentText(ic);
        arr[5] = "Ib = " + circuitFrame.getCurrentText(ib);

        if ((vbc * pnp) > .2) {
            arr[6] = ((vbe * pnp) > .2) ? "saturation" : "reverse active";
        } else {
            arr[6] = ((vbe * pnp) > .2) ? "fwd active" : "cutoff";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getScopeValue(int x) {
        switch (x) {
        case Scope.SCOPEVAL_IB:
            return ib;

        case Scope.SCOPEVAL_IC:
            return ic;

        case Scope.SCOPEVAL_IE:
            return ie;

        case Scope.SCOPEVAL_VBE:
            return volts[0] - volts[2];

        case Scope.SCOPEVAL_VBC:
            return volts[0] - volts[1];

        case Scope.SCOPEVAL_VCE:
            return volts[1] - volts[2];
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getScopeUnits(int x) {
        switch (x) {
        case Scope.SCOPEVAL_IB:
        case Scope.SCOPEVAL_IC:
        case Scope.SCOPEVAL_IE:
            return "A";

        default:
            return "V";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canViewInScope() {
        return true;
    }
}
