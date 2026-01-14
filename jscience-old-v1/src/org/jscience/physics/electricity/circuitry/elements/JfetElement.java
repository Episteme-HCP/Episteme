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
public class JfetElement extends MosfetElement {
    // post coordinates (gate post is x, y)
    /**
     * DOCUMENT ME!
     */
    public int src_gx;

    // post coordinates (gate post is x, y)
    /**
     * DOCUMENT ME!
     */
    public int src_gy;

    // post coordinates (gate post is x, y)
    /**
     * DOCUMENT ME!
     */
    public int drn_gx;

    // post coordinates (gate post is x, y)
    /**
     * DOCUMENT ME!
     */
    public int drn_gy;

    // post coordinates (gate post is x, y)
    /**
     * DOCUMENT ME!
     */
    public int gategx;

    // post coordinates (gate post is x, y)
    /**
     * DOCUMENT ME!
     */
    public int gategy;

    // gate rectangle
    /**
     * DOCUMENT ME!
     */
    public int gaterx;

    // gate rectangle
    /**
     * DOCUMENT ME!
     */
    public int gatery;

    // gate rectangle
    /**
     * DOCUMENT ME!
     */
    public int gaterw;

    // gate rectangle
    /**
     * DOCUMENT ME!
     */
    public int gaterh;

    /**
     * DOCUMENT ME!
     */
    public int[] arrowPointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] arrowPointsY;

    /**
     * Creates a new JfetElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     * @param pnpflag DOCUMENT ME!
     */
    public JfetElement(int xx, int yy, boolean pnpflag) {
        super(xx, yy, pnpflag);
        noDiagonal = true;
    }

    /**
     * Creates a new JfetElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public JfetElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        noDiagonal = true;
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
        circuitFrame.drawThickLine(g, src_mx, src_my, src_gx, src_gy);
        setVoltageColor(g, volts[2]);
        circuitFrame.drawThickLine(g, drn_mx, drn_my, drn_px, drn_py);
        circuitFrame.drawThickLine(g, drn_mx, drn_my, drn_gx, drn_gy);
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, gategx, gategy);
        g.fillPolygon(arrowPointsX, arrowPointsY, 3);
        setPowerColor(g, true);
        g.fillRect(gaterx, gatery, gaterw, gaterh);
        curcount = updateDotCount(-ids, curcount);

        if (curcount != 0) {
            circuitFrame.drawDots(g, src_px, src_py, src_mx, src_my, curcount);
            circuitFrame.drawDots(g, src_mx, src_my, src_gx, src_gy, curcount + 8);
            circuitFrame.drawDots(g, drn_px, drn_py, drn_mx, drn_my, -curcount);
            circuitFrame.drawDots(g, drn_mx, drn_my, drn_gx, drn_gy, -(curcount + 8));
        }

        drawPost(g, x, y, nodes[0]);
        drawPost(g, src_px, src_py, nodes[1]);
        drawPost(g, drn_px, drn_py, nodes[2]);
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        // find the coordinates of the various points we need to draw
        // the JFET.
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

        int dpx2 = dpx / 2;
        int dpy2 = dpy / 2;
        src_mx = x2 - dpx2;
        src_my = y2 - dpy2;
        drn_mx = x2 + dpx2;
        drn_my = y2 + dpy2;

        /*int dax1 = (int) ((dn/2-8)*dx/dn);
        int day1 = (int) ((dn/2-8)*dy/dn);
        int dax2 = (int) ((dn/2+3)*dx/dn);
        int day2 = (int) ((dn/2+3)*dy/dn);*/
        int dax1 = (int) ((10 * dx) / dn);
        int day1 = (int) ((10 * dy) / dn);
        int dax2 = (int) (((dn - 15) * dx) / dn);
        int day2 = (int) (((dn - 15) * dy) / dn);
        src_gx = src_mx - dax1;
        src_gy = src_my - day1;
        drn_gx = drn_mx - dax1;
        drn_gy = drn_my - day1;
        gategx = x + dax2;
        gategy = y + day2;

        if (dy == 0) {
            gaterx = circuitFrame.min(gategx, src_gx);
            gaterw = circuitFrame.max(gategx, src_gx) - gaterx;
            gatery = circuitFrame.min(src_py, drn_py);
            gaterh = circuitFrame.max(src_py, drn_py) - gatery;
        } else {
            gaterx = circuitFrame.min(src_px, drn_px);
            gaterw = circuitFrame.max(src_px, drn_px) - gaterx;
            gatery = circuitFrame.min(gategy, src_gy);
            gaterh = circuitFrame.max(gategy, src_gy) - gatery;
        }

        arrowPointsX = new int[3];
        arrowPointsY = new int[3];

        double l = Math.sqrt(((gategx - x) * (gategx - x)) +
                ((gategy - y) * (gategy - y)));
        double hatx = (gategx - x) / l;
        double haty = (gategy - y) / l;
        int ax = gategx;
        int ay = gategy;
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
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'j';
    }

    // these values are taken from Hayes+Horowitz p155
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getThreshold() {
        return -4;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getBeta() {
        return .00125;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        getFetInfo(arr, "JFET");
    }

    /*EditInfo getEditInfo(int n) {
        if (n == 0)
            return new EditInfo("threshold", th, .01, 5);
        if (n == 1)
            return new EditInfo("beta", bb, .01, 5);
        return null;
    }
    void setEditValue(int n, EditInfo ei) {
        if (n == 0)
            th = ei.value;
        else
            bb = ei.value;
            }*/
}
