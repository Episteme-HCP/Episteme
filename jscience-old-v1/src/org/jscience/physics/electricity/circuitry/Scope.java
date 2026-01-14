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
package org.jscience.physics.electricity.circuitry;

import org.jscience.physics.electricity.circuitry.elements.LogicOutputElement;
import org.jscience.physics.electricity.circuitry.elements.OutputElement;
import org.jscience.physics.electricity.circuitry.elements.ProbeElement;
import org.jscience.physics.electricity.circuitry.elements.TransistorElement;
import org.jscience.physics.electricity.circuitry.gui.CircuitFrame;

import java.awt.*;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Scope {

    public static final int SCOPEVAL_POWER = 1;
     public static final int SCOPEVAL_IB = 1;
     public static final int SCOPEVAL_IC = 2;
     public static final int SCOPEVAL_IE = 3;
     public static final int SCOPEVAL_VBE = 4;
     public static final int SCOPEVAL_VBC = 5;
    public static final int SCOPEVAL_VCE = 6;

    public CircuitFrame circuitFrame;

      /**
     * DOCUMENT ME!
     */
    public double[] minV;

    /**
     * DOCUMENT ME!
     */
    public double[] maxV;

    /**
     * DOCUMENT ME!
     */
    public double minMaxV;

    /**
     * DOCUMENT ME!
     */
    public double[] minI;

    /**
     * DOCUMENT ME!
     */
    public double[] maxI;

    /**
     * DOCUMENT ME!
     */
    public double minMaxI;

    /**
     * DOCUMENT ME!
     */
    public int scopePointCount = 128;

    /**
     * DOCUMENT ME!
     */
    public int ptr;

    /**
     * DOCUMENT ME!
     */
    public int ctr;

    /**
     * DOCUMENT ME!
     */
    public int speed;

    /**
     * DOCUMENT ME!
     */
    public int position;

    /**
     * DOCUMENT ME!
     */
    public int value;

    /**
     * DOCUMENT ME!
     */
    public String text;

    /**
     * DOCUMENT ME!
     */
    public Rectangle rect;

    /**
     * DOCUMENT ME!
     */
    public boolean showI;

    /**
     * DOCUMENT ME!
     */
    public boolean showV;

    /**
     * DOCUMENT ME!
     */
    public boolean showMax;

    /**
     * DOCUMENT ME!
     */
    public boolean showFreq;

    /**
     * DOCUMENT ME!
     */
    public boolean lockScale;

    /**
     * DOCUMENT ME!
     */
    public CircuitElement elm;

    /**
     * Creates a new Scope object.
     */
    public Scope() {
        rect = new Rectangle();
        reset();
    }

    public CircuitFrame getCircuitFrame() {
       return circuitFrame;
    }

    public void setCircuitFrame(CircuitFrame circuitFrame) {
      this.circuitFrame = circuitFrame;
    }

     /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void showCurrent(boolean b) {
        showI = b;
        value = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void showVoltage(boolean b) {
        showV = b;
        value = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void showMax(boolean b) {
        showMax = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void showFreq(boolean b) {
        showFreq = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setLockScale(boolean b) {
        lockScale = b;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetGraph() {
        scopePointCount = 1;

        while (scopePointCount < rect.width)
            scopePointCount *= 2;

        minV = new double[scopePointCount];
        maxV = new double[scopePointCount];
        minI = new double[scopePointCount];
        maxI = new double[scopePointCount];
        ptr = ctr = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean active() {
        return elm != null;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        resetGraph();
        minMaxV = 5;
        minMaxI = .1;
        speed = 64;
        showI = showV = showMax = true;
        showFreq = lockScale = false;

        // no showI for Output
        if ((elm != null) &&
                (elm instanceof OutputElement || elm instanceof LogicOutputElement ||
                elm instanceof ProbeElement)) {
            showI = false;
        }

        value = 0;

        if (elm instanceof TransistorElement) {
            value = SCOPEVAL_VCE;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setRect(Rectangle r) {
        rect = r;
        resetGraph();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return rect.width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int rightEdge() {
        return rect.x + rect.width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ce DOCUMENT ME!
     */
    public void setElement(CircuitElement ce) {
        elm = ce;
        reset();
    }

    /**
     * DOCUMENT ME!
     */
    public void timeStep() {
        if (elm == null) {
            return;
        }

        double v = elm.getScopeValue(value);

        if (v < minV[ptr]) {
            minV[ptr] = v;
        }

        if (v > maxV[ptr]) {
            maxV[ptr] = v;
        }

        double i = 0;

        if (value == 0) {
            i = elm.getCurrent();

            if (i < minI[ptr]) {
                minI[ptr] = i;
            }

            if (i > maxI[ptr]) {
                maxI[ptr] = i;
            }
        }

        ctr++;

        if (ctr >= speed) {
            ptr = (ptr + 1) & (scopePointCount - 1);
            minV[ptr] = maxV[ptr] = v;
            minI[ptr] = maxI[ptr] = i;
            ctr = 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void adjustScale(double x) {
        minMaxV *= x;
        minMaxI *= x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        //System.out.println(this + " " + g);
        if (elm == null) {
            return;
        }

        int i;
        int x = rect.x;
        int y = rect.y;
        int maxy = (rect.height - 1) / 2;
        y += maxy;
        g.setColor(Color.gray);
        g.drawLine(x, y, (x + rect.width) - 1, y);

        boolean gotI = false;
        boolean gotV = false;
        int minRange = 4;
        double realMaxV = -1e8;
        double realMaxI = -1e8;
        Color curColor = Color.yellow;
        Color voltColor = (value > 0) ? Color.white
                                      : circuitFrame.colorScale[circuitFrame.colorScaleCount - 1];

        if ((circuitFrame.scopeSelected == -1) && (elm == circuitFrame.mouseElement)) {
            if ((value == 0) && !showV) {
                curColor = Color.cyan;
            } else {
                voltColor = Color.cyan;
            }
        }

        int ipa = (ptr + scopePointCount) - rect.width;

        for (i = 0; i != rect.width; i++) {
            int ip = (i + ipa) & (scopePointCount - 1);

            while (maxV[ip] > minMaxV)
                minMaxV *= 2;

            while (minV[ip] < -minMaxV)
                minMaxV *= 2;

            while (maxI[ip] > minMaxI)
                minMaxI *= 2;

            while (minI[ip] < -minMaxI)
                minMaxI *= 2;
        }

        // these two loops are pretty much the same, and should be
        // combined!
        if ((value == 0) && showI) {
            g.setColor(curColor);

            int ox = -1;
            int oy = -1;

            for (i = 0; i != rect.width; i++) {
                int ip = (i + ipa) & (scopePointCount - 1);
                int miniy = (int) ((maxy / minMaxI) * minI[ip]);
                int maxiy = (int) ((maxy / minMaxI) * maxI[ip]);

                if (maxI[ip] > realMaxI) {
                    realMaxI = maxI[ip];
                }

                if (miniy <= maxy) {
                    if ((miniy < -minRange) || (maxiy > minRange)) {
                        gotI = true;
                    }

                    if (ox != -1) {
                        if ((miniy == oy) && (maxiy == oy)) {
                            continue;
                        }

                        g.drawLine(ox, y - oy, (x + i) - 1, y - oy);
                        ox = oy = -1;
                    }

                    if (miniy == maxiy) {
                        ox = x + i;
                        oy = miniy;

                        continue;
                    }

                    g.drawLine(x + i, y - miniy, x + i, y - maxiy);
                }
            }

            if (ox != -1) {
                g.drawLine(ox, y - oy, (x + i) - 1, y - oy);
            }
        }

        if ((value != 0) || showV) {
            g.setColor(voltColor);

            int ox = -1;
            int oy = -1;

            for (i = 0; i != rect.width; i++) {
                int ip = (i + ipa) & (scopePointCount - 1);
                int minvy = (int) ((maxy / minMaxV) * minV[ip]);
                int maxvy = (int) ((maxy / minMaxV) * maxV[ip]);

                if (maxV[ip] > realMaxV) {
                    realMaxV = maxV[ip];
                }

                if (((value != 0) || showV) && (minvy <= maxy)) {
                    if ((minvy < -minRange) || (maxvy > minRange)) {
                        gotV = true;
                    }

                    if (ox != -1) {
                        if ((minvy == oy) && (maxvy == oy)) {
                            continue;
                        }

                        g.drawLine(ox, y - oy, (x + i) - 1, y - oy);
                        ox = oy = -1;
                    }

                    if (minvy == maxvy) {
                        ox = x + i;
                        oy = minvy;

                        continue;
                    }

                    g.drawLine(x + i, y - minvy, x + i, y - maxvy);
                }
            }

            if (ox != -1) {
                g.drawLine(ox, y - oy, (x + i) - 1, y - oy);
            }
        }

        double freq = 0;

        if (showFreq) {
            // try to get frequency
            // get average
            double avg = 0;

            for (i = 0; i != rect.width; i++) {
                int ip = (i + ipa) & (scopePointCount - 1);
                avg += (minV[ip] + maxV[ip]);
            }

            avg /= (i * 2);

            int state = 0;
            double thresh = avg * .05;
            int oi = 0;
            double avperiod = 0;
            int periodct = -1;
            double avperiod2 = 0;

            // count period lengths
            for (i = 0; i != rect.width; i++) {
                int ip = (i + ipa) & (scopePointCount - 1);
                double q = maxV[ip] - avg;
                int os = state;

                if (q < thresh) {
                    state = 1;
                } else if (q > -thresh) {
                    state = 2;
                }

                if ((state == 2) && (os == 1)) {
                    int pd = i - oi;
                    oi = i;

                    // short periods can't be counted properly
                    if (pd < 12) {
                        continue;
                    }

                    // skip first period, it might be too short
                    if (periodct >= 0) {
                        avperiod += pd;
                        avperiod2 += (pd * pd);
                    }

                    periodct++;
                }
            }

            avperiod /= periodct;
            avperiod2 /= periodct;

            double periodstd = Math.sqrt(avperiod2 - (avperiod * avperiod));
            freq = 1 / (avperiod * circuitFrame.timeStep * speed);

            // don't show freq if standard deviation is too great
            if ((periodct < 1) || (periodstd > 2)) {
                freq = 0;
            }

            // System.out.println(freq + " " + periodstd + " " + periodct);
        }

        g.setColor(Color.white);

        int yt = rect.y + 10;

        if (showMax) {
            if (value != 0) {
                g.drawString(circuitFrame.getUnitText(realMaxV, elm.getScopeUnits(value)),
                    x, yt);
            } else if (showV) {
                g.drawString(circuitFrame.getVoltageText(realMaxV), x, yt);
            } else if (showI) {
                g.drawString(circuitFrame.getCurrentText(realMaxI), x, yt);
            }

            yt += 15;
        }

        if ((text != null) && ((rect.y + rect.height) > (yt + 5))) {
            g.drawString(text, x, yt);
            yt += 15;
        }

        if (showFreq && (freq != 0) && ((rect.y + rect.height) > (yt + 5))) {
            g.drawString(circuitFrame.getUnitText(freq, "Hz"), x, yt);
        }

        if ((ptr > 5) && !lockScale) {
            if (!gotI && (minMaxI > 1e-4)) {
                minMaxI /= 2;
            }

            if (!gotV && (minMaxV > 1e-4)) {
                minMaxV /= 2;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void speedUp() {
        if (speed > 1) {
            speed /= 2;
            resetGraph();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void slowDown() {
        speed *= 2;
        resetGraph();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PopupMenu getMenu() {
        if (elm == null) {
            return null;
        }

        if (elm instanceof TransistorElement) {
            circuitFrame.scopeIbMenuItem.setState(value == SCOPEVAL_IB);
            circuitFrame.scopeIcMenuItem.setState(value == SCOPEVAL_IC);
            circuitFrame.scopeIeMenuItem.setState(value == SCOPEVAL_IE);
            circuitFrame.scopeVbeMenuItem.setState(value == SCOPEVAL_VBE);
            circuitFrame.scopeVbcMenuItem.setState(value == SCOPEVAL_VBC);
            circuitFrame.scopeVceMenuItem.setState(value == SCOPEVAL_VCE);

            return circuitFrame.transScopeMenu;
        } else {
            circuitFrame.scopeVMenuItem.setState(showV && (value == 0));
            circuitFrame.scopeIMenuItem.setState(showI && (value == 0));
            circuitFrame.scopeMaxMenuItem.setState(showMax);
            circuitFrame.scopeFreqMenuItem.setState(showFreq);
            circuitFrame.scopePowerMenuItem.setState(value == SCOPEVAL_POWER);

            return circuitFrame.scopeMenu;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void setValue(int x) {
        reset();
        value = x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        if (elm == null) {
            return null;
        }

        int flags = (showI ? 1 : 0) | (showV ? 2 : 0) | (showMax ? 0 : 4) |
            (showFreq ? 8 : 0) | (lockScale ? 16 : 0);
        int eno = circuitFrame.locateElement(elm);

        if (eno < 0) {
            return null;
        }

        String x = "o " + eno + " " + speed + " " + value + " " + flags + " " +
            minMaxV + " " + minMaxI + " " + position;

        if (text != null) {
            x += (" " + text);
        }

        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param st DOCUMENT ME!
     */
    public void undump(StringTokenizer st) {
        reset();

        int e = new Integer(st.nextToken()).intValue();

        if (e == -1) {
            return;
        }

        elm = circuitFrame.getElement(e);
        speed = new Integer(st.nextToken()).intValue();
        value = new Integer(st.nextToken()).intValue();

        int flags = new Integer(st.nextToken()).intValue();
        minMaxV = new Double(st.nextToken()).doubleValue();
        minMaxI = new Double(st.nextToken()).doubleValue();

        if (minMaxV == 0) {
            minMaxV = .5;
        }

        if (minMaxI == 0) {
            minMaxI = 1;
        }

        text = null;

        try {
            position = new Integer(st.nextToken()).intValue();

            while (st.hasMoreTokens()) {
                if (text == null) {
                    text = st.nextToken();
                } else {
                    text += (" " + st.nextToken());
                }
            }
        } catch (Exception ee) {
        }

        showI = (flags & 1) != 0;
        showV = (flags & 2) != 0;
        showMax = (flags & 4) == 0;
        showFreq = (flags & 8) != 0;
        lockScale = (flags & 16) != 0;
    }
}
