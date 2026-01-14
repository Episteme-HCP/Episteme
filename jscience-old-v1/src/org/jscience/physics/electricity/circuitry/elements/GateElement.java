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
public abstract class GateElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public final int FLAG_SMALL = 1;

    /**
     * DOCUMENT ME!
     */
    public int inputCount = 2;

    /**
     * DOCUMENT ME!
     */
    public boolean lastOutput;

    /**
     * DOCUMENT ME!
     */
    public int gsize;

    /**
     * DOCUMENT ME!
     */
    public int gwidth;

    /**
     * DOCUMENT ME!
     */
    public int gwidth2;

    /**
     * DOCUMENT ME!
     */
    public int gheight;

    /**
     * DOCUMENT ME!
     */
    public Point[] inPosts;

    /**
     * DOCUMENT ME!
     */
    public Point[] inGates;

    /**
     * DOCUMENT ME!
     */
   public int outpx;

    /**
     * DOCUMENT ME!
     */
    public int outpy;

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
    public int[] triPointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] triPointsY;

    /**
     * DOCUMENT ME!
     */
    public int[] linePointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] linePointsY;

    /**
     * DOCUMENT ME!
     */
    public int pcirclex;

    /**
     * DOCUMENT ME!
     */
    public int pcircley;

    /**
     * Creates a new GateElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public GateElement(int xx, int yy) {
        super(xx, yy);
        noDiagonal = true;
        inputCount = 2;
        setSize(circuitFrame.smallGridCheckItem.getState() ? 1 : 2);
    }

    /**
     * Creates a new GateElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public GateElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        inputCount = new Integer(st.nextToken()).intValue();
        lastOutput = new Double(st.nextToken()).doubleValue() > 2.5;
        noDiagonal = true;
        setSize(((f & FLAG_SMALL) != 0) ? 1 : 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInverting() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setSize(int s) {
        gsize = s;
        gwidth = 7 * s;
        gwidth2 = 14 * s;
        gheight = 8 * s;
        flags = (s == 1) ? FLAG_SMALL : 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return getDumpType() + " " + x + " " + y + " " + x2 + " " + y2 + " " +
        flags + " " + inputCount + " " + volts[inputCount];
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        int i;

        for (i = 0; i != inputCount; i++) {
            setVoltageColor(g, volts[i]);
            circuitFrame.drawThickLine(g, inPosts[i].x, inPosts[i].y, inGates[i].x,
                inGates[i].y);
        }

        setVoltageColor(g, volts[inputCount]);
        circuitFrame.drawThickLine(g, outnx, outny, outpx, outpy);
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.lightGray);
        circuitFrame.drawThickPolygon(g, triPointsX, triPointsY, triPointsX.length);

        if (linePointsX != null) {
            for (i = 0; i != (linePointsX.length - 1); i++)
                circuitFrame.drawThickLine(g, linePointsX[i], linePointsY[i],
                    linePointsX[i + 1], linePointsY[i + 1]);
        }

        if (isInverting()) {
            circuitFrame.drawThickCircle(g, pcirclex, pcircley, 3);
        }

        curcount = updateDotCount(current, curcount);
        circuitFrame.drawDots(g, outnx, outny, outpx, outpy, curcount);

        for (i = 0; i != inputCount; i++)
            drawPost(g, inPosts[i].x, inPosts[i].y, nodes[i]);

        drawPost(g, outpx, outpy, nodes[inputCount]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return inputCount + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getPost(int n) {
        if (n == inputCount) {
            return new Point(outpx, outpy);
        }

        return inPosts[n];
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
    public abstract String getGateName();

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = getGateName();
        arr[1] = "Vout = " + circuitFrame.getVoltageText(volts[inputCount]);
        arr[2] = "Iout = " + circuitFrame.getCurrentText(getCurrent());
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampVoltageSource(0, nodes[inputCount], voltSource);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getInput(int x) {
        return volts[x] > 2.5;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean calcFunction();

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        int i;
        boolean f = calcFunction();

        if (isInverting()) {
            f = !f;
        }

        lastOutput = f;

        double res = f ? 5 : 0;
        circuitFrame.updateVoltageSource(0, nodes[inputCount], voltSource, res);
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
            return new EditInfo("Inputs", inputCount, 1, 8);
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
        inputCount = (int) ei.value;
        setPoints();
    }

    // there is no current path through the gate inputs, but there
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
        return (n1 == inputCount);
    }
}
