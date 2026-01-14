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
public abstract class ChipElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public int csize;

    /**
     * DOCUMENT ME!
     */
    public int cspc;

    /**
     * DOCUMENT ME!
     */
    public int cspc2;

    /**
     * DOCUMENT ME!
     */
    public int bits;

    /**
     * DOCUMENT ME!
     */
    public final int FLAG_SMALL = 1;

    /**
     * DOCUMENT ME!
     */
    public int[] rectPointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] rectPointsY;

    /**
     * DOCUMENT ME!
     */
    public int[] clockPointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] clockPointsY;

    /**
     * DOCUMENT ME!
     */
    public Pin[] pins;

    /**
     * DOCUMENT ME!
     */
    public int sizeX;

    /**
     * DOCUMENT ME!
     */
    public int sizeY;

    /**
     * DOCUMENT ME!
     */
    public boolean lastClock;

    /**
     * DOCUMENT ME!
     */
    public final int SIDE_N = 0;

    /**
     * DOCUMENT ME!
     */
    public final int SIDE_S = 1;

    /**
     * DOCUMENT ME!
     */
    public final int SIDE_W = 2;

    /**
     * DOCUMENT ME!
     */
    public final int SIDE_E = 3;

    /**
     * Creates a new ChipElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public ChipElement(int xx, int yy) {
        super(xx, yy);

        if (needsBits()) {
            bits = (this instanceof JohnsonElement) ? 10 : 4;
        }

        noDiagonal = true;
        setupPins();
        setSize(circuitFrame.smallGridCheckItem.getState() ? 1 : 2);
    }

    /**
     * Creates a new ChipElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public ChipElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);

        if (needsBits()) {
            bits = new Integer(st.nextToken()).intValue();
        }

        noDiagonal = true;
        setupPins();
        setSize(((f & FLAG_SMALL) != 0) ? 1 : 2);

        int i;

        for (i = 0; i != getPostCount(); i++) {
            if (pins[i].state) {
                volts[i] = new Double(st.nextToken()).doubleValue();
                pins[i].value = volts[i] > 2.5;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean needsBits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setSize(int s) {
        csize = s;
        cspc = 8 * s;
        cspc2 = cspc * 2;
        flags &= ~FLAG_SMALL;
        flags |= ((s == 1) ? FLAG_SMALL : 0);
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void setupPins();

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawChip(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawChip(Graphics g) {
        int i;
        Font f = new Font("SansSerif", 0, 10 * csize);
        g.setFont(f);

        FontMetrics fm = g.getFontMetrics();

        for (i = 0; i != getPostCount(); i++) {
            Pin p = pins[i];
            setVoltageColor(g, volts[i]);

            Point a = p.post;
            Point b = p.stub;
            circuitFrame.drawThickLine(g, a.x, a.y, b.x, b.y);
            p.curcount = updateDotCount(p.current, p.curcount);
            circuitFrame.drawDots(g, b.x, b.y, a.x, a.y, p.curcount);

            if (p.bubble) {
                g.setColor(Color.black);
                circuitFrame.drawThickCircle(g, p.bubbleX, p.bubbleY, 1);
                g.setColor(Color.lightGray);
                circuitFrame.drawThickCircle(g, p.bubbleX, p.bubbleY, 3);
            }

            g.setColor(Color.white);

            int sw = fm.stringWidth(p.text);
            g.drawString(p.text, p.textloc.x - (sw / 2),
                p.textloc.y + (fm.getAscent() / 2));

            if (p.lineOver) {
                int ya = p.textloc.y - (fm.getAscent() / 2);
                g.drawLine(p.textloc.x - (sw / 2), ya, p.textloc.x + (sw / 2),
                    ya);
            }
        }

        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.lightGray);
        circuitFrame.drawThickPolygon(g, rectPointsX, rectPointsY, 4);

        if (clockPointsX != null) {
            g.drawPolyline(clockPointsX, clockPointsY, 3);
        }

        for (i = 0; i != getPostCount(); i++)
            drawPost(g, pins[i].post.x, pins[i].post.y, nodes[i]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public void drag(int xx, int yy) {
        yy = circuitFrame.snapGrid(yy);

        if (xx < x) {
            xx = x;
            yy = y;
        } else {
            y = y2 = yy;
            x2 = circuitFrame.snapGrid(xx);
        }

        setPoints();
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        if ((x2 - x) > (sizeX * cspc2)) {
            setSize(2);
        }

        int hs = cspc;
        int dx = 1;
        int dy = 0;
        int dpx = -dy;
        int dpy = dx;
        rectPointsX = new int[4];
        rectPointsY = new int[4];

        int x0 = x + cspc2;
        int y0 = y;
        int xr = x0 - cspc;
        int yr = y0 - cspc;
        int xs = sizeX * cspc2;
        int ys = sizeY * cspc2;
        rectPointsX[0] = xr;
        rectPointsY[0] = yr;
        rectPointsX[1] = xr + (dx * xs);
        rectPointsY[1] = yr + (dy * xs);
        rectPointsX[2] = xr + (dx * xs) + (dpx * ys);
        rectPointsY[2] = yr + (dy * xs) + (dpy * ys);
        rectPointsX[3] = xr + (dpx * ys);
        rectPointsY[3] = yr + (dpy * ys);
        setBbox(xr, yr, rectPointsX[2], rectPointsY[2]);

        int i;

        for (i = 0; i != getPostCount(); i++) {
            Pin p = pins[i];

            switch (p.side) {
            case SIDE_N:
                p.setPoint(x0, y0, 1, 0, 0, -1, 0, 0);

                break;

            case SIDE_S:
                p.setPoint(x0, y0, 1, 0, 0, 1, 0, ys - cspc2);

                break;

            case SIDE_W:
                p.setPoint(x0, y0, 0, 1, -1, 0, 0, 0);

                break;

            case SIDE_E:
                p.setPoint(x0, y0, 0, 1, 1, 0, xs - cspc2, 0);

                break;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getPost(int n) {
        return pins[n].post;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getVoltageSourceCount(); // output count

    /**
     * DOCUMENT ME!
     *
     * @param j DOCUMENT ME!
     * @param vs DOCUMENT ME!
     */
    public void setVoltageSource(int j, int vs) {
        int i;

        for (i = 0; i != getPostCount(); i++) {
            Pin p = pins[i];

            if (p.output && (j-- == 0)) {
                p.voltSource = vs;

                return;
            }
        }

        System.out.println("setVoltageSource failed for " + this);
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        int i;

        for (i = 0; i != getPostCount(); i++) {
            Pin p = pins[i];

            if (p.output) {
                circuitFrame.stampVoltageSource(0, nodes[i], p.voltSource);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void execute() {
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        int i;

        for (i = 0; i != getPostCount(); i++) {
            Pin p = pins[i];

            if (!p.output) {
                p.value = volts[i] > 2.5;
            }
        }

        execute();

        for (i = 0; i != getPostCount(); i++) {
            Pin p = pins[i];

            if (p.output) {
                circuitFrame.updateVoltageSource(0, nodes[i], p.voltSource, p.value ? 5 : 0);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        int i;

        for (i = 0; i != getPostCount(); i++) {
            pins[i].value = false;
            volts[i] = 0;
        }

        lastClock = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        int t = getDumpType();
        String s = t + " " + x + " " + y + " " + x2 + " " + y2 + " " + flags;

        if (needsBits()) {
            s += (" " + bits);
        }

        int i;

        for (i = 0; i != getPostCount(); i++) {
            if (pins[i].state) {
                s += (" " + volts[i]);
            }
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = getChipName();

        int i;
        int a = 1;

        for (i = 0; i != getPostCount(); i++) {
            Pin p = pins[i];

            if (arr[a] != null) {
                arr[a] += "; ";
            } else {
                arr[a] = "";
            }

            String t = p.text;

            if (p.lineOver) {
                t += '\'';
            }

            if (p.clock) {
                t = "Clk";
            }

            arr[a] += (t + " = " + circuitFrame.getVoltageText(volts[i]));

            if ((i % 2) == 1) {
                a++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void setCurrent(int x, double c) {
        int i;

        for (i = 0; i != getPostCount(); i++)
            if (pins[i].output && (pins[i].voltSource == x)) {
                pins[i].current = c;
            }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "chip";
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
     * @param n1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasGroundConnection(int n1) {
        return pins[n1].output;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.2 $
      */
    class Pin {
        /**
         * DOCUMENT ME!
         */
        Point post;

        /**
         * DOCUMENT ME!
         */
        Point stub;

        /**
         * DOCUMENT ME!
         */
        Point textloc;

        /**
         * DOCUMENT ME!
         */
        int pos;

        /**
         * DOCUMENT ME!
         */
        int side;

        /**
         * DOCUMENT ME!
         */
        int voltSource;

        /**
         * DOCUMENT ME!
         */
        int bubbleX;

        /**
         * DOCUMENT ME!
         */
        int bubbleY;

        /**
         * DOCUMENT ME!
         */
        String text;

        /**
         * DOCUMENT ME!
         */
        boolean lineOver;

        /**
         * DOCUMENT ME!
         */
        boolean bubble;

        /**
         * DOCUMENT ME!
         */
        boolean clock;

        /**
         * DOCUMENT ME!
         */
        boolean output;

        /**
         * DOCUMENT ME!
         */
        boolean value;

        /**
         * DOCUMENT ME!
         */
        boolean state;

        /**
         * DOCUMENT ME!
         */
        double curcount;

        /**
         * DOCUMENT ME!
         */
        double current;

        /**
         * Creates a new Pin object.
         *
         * @param p DOCUMENT ME!
         * @param s DOCUMENT ME!
         * @param t DOCUMENT ME!
         */
        Pin(int p, int s, String t) {
            pos = p;
            side = s;
            text = t;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param dx DOCUMENT ME!
         * @param dy DOCUMENT ME!
         * @param dax DOCUMENT ME!
         * @param day DOCUMENT ME!
         * @param sx DOCUMENT ME!
         * @param sy DOCUMENT ME!
         */
        void setPoint(int x, int y, int dx, int dy, int dax, int day, int sx,
            int sy) {
            int xa = x + (cspc2 * dx * pos) + sx;
            int ya = y + (cspc2 * dy * pos) + sy;
            post = new Point(xa + (dax * cspc2), ya + (day * cspc2));
            stub = new Point(xa + (dax * cspc), ya + (day * cspc));
            textloc = new Point(xa, ya);

            if (bubble) {
                bubbleX = xa + (dax * 10 * csize);
                bubbleY = ya + (day * 10 * csize);
            }

            if (clock) {
                clockPointsX = new int[3];
                clockPointsY = new int[3];
                clockPointsX[0] = (xa + (dax * cspc)) - ((dx * cspc) / 2);
                clockPointsY[0] = (ya + (day * cspc)) - ((dy * cspc) / 2);
                clockPointsX[1] = xa;
                clockPointsY[1] = ya;
                clockPointsX[2] = xa + (dax * cspc) + ((dx * cspc) / 2);
                clockPointsY[2] = ya + (day * cspc) + ((dy * cspc) / 2);
            }
        }
    }
}
