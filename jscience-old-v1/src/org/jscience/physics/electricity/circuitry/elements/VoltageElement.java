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
public class VoltageElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public static final int FLAG_COS = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int WF_DC = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int WF_AC = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int WF_SQUARE = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int WF_TRIANGLE = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int WF_SAWTOOTH = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int WF_PULSE = 5;

    /**
     * DOCUMENT ME!
     */
    public int waveform;

    /**
     * DOCUMENT ME!
     */
    public double frequency;

    /**
     * DOCUMENT ME!
     */
    public double maxVoltage;

    /**
     * DOCUMENT ME!
     */
    public double freqTimeZero;

    /**
     * DOCUMENT ME!
     */
   public double bias;

    /**
     * DOCUMENT ME!
     */
   public  final int circleSize = 17;

    /**
     * Creates a new VoltageElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     * @param wf DOCUMENT ME!
     */
    public VoltageElement(int xx, int yy, int wf) {
        super(xx, yy);
        waveform = wf;
        maxVoltage = 5;
        frequency = 40;
        reset();
    }

    /**
     * Creates a new VoltageElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public VoltageElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        maxVoltage = 5;
        frequency = 40;
        waveform = WF_DC;

        try {
            waveform = new Integer(st.nextToken()).intValue();
            frequency = new Double(st.nextToken()).doubleValue();
            maxVoltage = new Double(st.nextToken()).doubleValue();
            bias = new Double(st.nextToken()).doubleValue();
        } catch (Exception e) {
        }

        reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'v';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "v " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        waveform + " " + frequency + " " + maxVoltage + " " + bias;
    }

    /*void setCurrent(double c) {
        current = c;
        System.out.print("v current set to " + c + "\n");
        }*/
    public void reset() {
        freqTimeZero = 0;

        if ((flags & FLAG_COS) != 0) {
            freqTimeZero = 1 / (frequency * 4);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double triangleFunc(double x) {
        if (x < circuitFrame.pi) {
            return (x * (2 / circuitFrame.pi)) - 1;
        }

        return 1 - ((x - circuitFrame.pi) * (2 / circuitFrame.pi));
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        if (waveform == WF_DC) {
            circuitFrame.stampVoltageSource(nodes[0], nodes[1], voltSource, getVoltage());
        } else {
            circuitFrame.stampVoltageSource(nodes[0], nodes[1], voltSource);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        if (waveform != WF_DC) {
            circuitFrame.updateVoltageSource(nodes[0], nodes[1], voltSource, getVoltage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltage() {
        double w = 2 * circuitFrame.pi * (circuitFrame.t - freqTimeZero) * frequency;

        switch (waveform) {
        case WF_DC:
            return maxVoltage + bias;

        case WF_AC:
            return (Math.sin(w) * maxVoltage) + bias;

        case WF_SQUARE:
            return bias + (((w % (2 * circuitFrame.pi)) > circuitFrame.pi) ? (-maxVoltage) : maxVoltage);

        case WF_TRIANGLE:
            return bias + (triangleFunc(w % (2 * circuitFrame.pi)) * maxVoltage);

        case WF_SAWTOOTH:
            return (bias + ((w % (2 * circuitFrame.pi)) * (maxVoltage / circuitFrame.pi))) - maxVoltage;

        case WF_PULSE:
            return ((w % (2 * circuitFrame.pi)) < 1) ? (maxVoltage + bias) : bias;

        default:
            return 0;
        }
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
        int centerDist = (waveform == WF_DC) ? 4 : circleSize;
        int x1a = (int) (x + ((dx * ((dn / 2) - centerDist)) / dn));
        int y1a = (int) (y + ((dy * ((dn / 2) - centerDist)) / dn));
        int x2a = (int) (x + ((dx * ((dn / 2) + centerDist)) / dn));
        int y2a = (int) (y + ((dy * ((dn / 2) + centerDist)) / dn));
        int xc = (x + x2) / 2;
        int yc = (y + y2) / 2;
        setBbox(x, y, x2, y2);
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x1a, y1a);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);

        if (waveform == WF_DC) {
            int hs = 10;
            int dpx = (int) ((hs * dy) / dn);
            int dpy = (int) ((-hs * dx) / dn);
            setPowerColor(g, false);
            setVoltageColor(g, volts[0]);
            circuitFrame.drawThickLine(g, x1a - dpx, y1a - dpy, x1a + dpx, y1a + dpy);
            setVoltageColor(g, volts[1]);
            hs = 16;
            dpx = (int) ((hs * dy) / dn);
            dpy = (int) ((-hs * dx) / dn);
            circuitFrame.drawThickLine(g, x2a - dpx, y2a - dpy, x2a + dpx, y2a + dpy);
            adjustBbox(x - dpx, y - dpy, x2 + dpx, y2 + dpy);
        } else {
            drawWaveform(g, xc, yc);
        }

        updateDotCount();

        if (circuitFrame.dragElement != this) {
            if (waveform == WF_DC) {
                circuitFrame.drawDots(g, x, y, x2, y2, curcount);
            } else {
                circuitFrame.drawDots(g, x, y, x1a, y1a, curcount);
                circuitFrame.drawDots(g, x2, y2, x2a, y2a, -curcount);
            }
        }

        drawPosts(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param xc DOCUMENT ME!
     * @param yc DOCUMENT ME!
     */
    public void drawWaveform(Graphics g, int xc, int yc) {
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.gray);
        setPowerColor(g, false);
        circuitFrame.drawThickCircle(g, xc, yc, circleSize);
        adjustBbox(xc - circleSize, yc - circleSize, xc + circleSize,
            yc + circleSize);

        int wl = 8;

        switch (waveform) {
        case WF_DC:break;

        case WF_SQUARE:
            circuitFrame.drawThickLine(g, xc - wl, yc - wl, xc - wl, yc);
            circuitFrame.drawThickLine(g, xc - wl, yc - wl, xc, yc - wl);
            circuitFrame.drawThickLine(g, xc, yc - wl, xc, yc + wl);
            circuitFrame.drawThickLine(g, xc + wl, yc + wl, xc, yc + wl);
            circuitFrame.drawThickLine(g, xc + wl, yc, xc + wl, yc + wl);

            break;

        case WF_PULSE:
            yc += (wl / 2);
            circuitFrame.drawThickLine(g, xc - wl, yc - wl, xc - wl, yc);
            circuitFrame.drawThickLine(g, xc - wl, yc - wl, xc - (wl / 2), yc - wl);
            circuitFrame.drawThickLine(g, xc - (wl / 2), yc - wl, xc - (wl / 2), yc);
            circuitFrame.drawThickLine(g, xc - (wl / 2), yc, xc + wl, yc);

            break;

        case WF_SAWTOOTH:
            circuitFrame.drawThickLine(g, xc, yc - wl, xc - wl, yc);
            circuitFrame.drawThickLine(g, xc, yc - wl, xc, yc + wl);
            circuitFrame.drawThickLine(g, xc, yc + wl, xc + wl, yc);

            break;

        case WF_TRIANGLE: {
            int xl = 5;
            circuitFrame.drawThickLine(g, xc - (xl * 2), yc, xc - xl, yc - wl);
            circuitFrame.drawThickLine(g, xc - xl, yc - wl, xc, yc);
            circuitFrame.drawThickLine(g, xc, yc, xc + xl, yc + wl);
            circuitFrame.drawThickLine(g, xc + xl, yc + wl, xc + (xl * 2), yc);

            break;
        }

        case WF_AC: {
            int i;
            int xl = 10;
            int ox = -1;
            int oy = -1;

            for (i = -xl; i <= xl; i++) {
                int yy = yc + (int) (.95 * Math.sin((i * circuitFrame.pi) / xl) * wl);

                if (ox != -1) {
                    circuitFrame.drawThickLine(g, ox, oy, xc + i, yy);
                }

                ox = xc + i;
                oy = yy;
            }

            break;
        }
        }

        if (circuitFrame.showValuesCheckItem.getState()) {
            String s = circuitFrame.getShortUnitText(frequency, "Hz");
            int dx = x2 - x;
            int dy = y2 - y;

            if ((dx == 0) || (dy == 0)) {
                int dpx = (dx == 0) ? circleSize : 0;
                int dpy = (dy == 0) ? (-circleSize) : 0;
                drawValues(g, s, xc, yc, dpx, dpy);
            }
        }
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
    public double getPower() {
        return -getVoltageDiff() * current;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[1] - volts[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        switch (waveform) {
        case WF_DC:
            arr[0] = "voltage source";

            break;

        case WF_AC:
            arr[0] = "A/C source";

            break;

        case WF_SQUARE:
            arr[0] = "square wave gen";

            break;

        case WF_PULSE:
            arr[0] = "pulse gen";

            break;

        case WF_SAWTOOTH:
            arr[0] = "sawtooth gen";

            break;

        case WF_TRIANGLE:
            arr[0] = "triangle gen";

            break;
        }

        arr[1] = "I = " + circuitFrame.getCurrentText(getCurrent());
        arr[2] = "Vd = " + circuitFrame.getVoltageText(getVoltageDiff());

        if (waveform != WF_DC) {
            arr[3] = "f = " + circuitFrame.getUnitText(frequency, "Hz");
            arr[4] = "Vmax = " + circuitFrame.getVoltageText(maxVoltage);
            arr[5] = "Voff = " + circuitFrame.getVoltageText(bias);
            arr[6] = "P = " + circuitFrame.getUnitText(getPower(), "W");
        }
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
            return new EditInfo("Max Voltage", maxVoltage, -20, 20);
        }

        if (n == 1) {
            EditInfo ei = new EditInfo("Waveform", waveform, -1, -1);
            ei.choice = new Choice();
            ei.choice.add("D/C");
            ei.choice.add("A/C");
            ei.choice.add("Square Wave");
            ei.choice.add("Triangle");
            ei.choice.add("Sawtooth");
            ei.choice.add("Pulse");
            ei.choice.select(waveform);

            return ei;
        }

        if (n == 2) {
            return new EditInfo("Frequency (Hz)", frequency, 4, 500);
        }

        if (n == 3) {
            return new EditInfo("DC Offset (V)", bias, -20, 20);
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
            maxVoltage = ei.value;
        }

        if (n == 3) {
            bias = ei.value;
        }

        if (n == 2) {
            // adjust time zero to maintain continuity ind the waveform
            // even though the frequency has changed.
            double oldfreq = frequency;
            frequency = ei.value;

            double maxfreq = 1 / (8 * circuitFrame.timeStep);

            if (frequency > maxfreq) {
                frequency = maxfreq;
            }

            double adj = frequency - oldfreq;
            freqTimeZero = circuitFrame.t - ((oldfreq * (circuitFrame.t - freqTimeZero)) / frequency);
        }

        if (n == 1) {
            waveform = ei.choice.getSelectedIndex();
        }
    }
}
