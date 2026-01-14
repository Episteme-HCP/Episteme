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
public class LEDElement extends DiodeElement {
    /**
     * DOCUMENT ME!
     */
    public double colorR;

    /**
     * DOCUMENT ME!
     */
    public double colorG;

    /**
     * DOCUMENT ME!
     */
    public double colorB;

    /**
     * Creates a new LEDElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public LEDElement(int xx, int yy) {
        super(xx, yy);
        leakage = 3e-37;
        setup();
        colorR = 1;
        colorG = colorB = 0;
    }

    /**
     * Creates a new LEDElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public LEDElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        leakage = 3e-37;
        setup();
        colorR = new Double(st.nextToken()).doubleValue();
        colorG = new Double(st.nextToken()).doubleValue();
        colorB = new Double(st.nextToken()).doubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 162;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "162 " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        colorR + " " + colorG + " " + colorB;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        if ((this == circuitFrame.mouseElement) || (this == circuitFrame.dragElement)) {
            drawDiode(g, x, y, x2, y2, volts[0], volts[1]);
            drawPosts(g);

            return;
        }

        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int cr = 12;
        int x1a = (int) (x + ((dx * ((dn / 2) - cr)) / dn));
        int y1a = (int) (y + ((dy * ((dn / 2) - cr)) / dn));
        int x2a = (int) (x + ((dx * ((dn / 2) + cr)) / dn));
        int y2a = (int) (y + ((dy * ((dn / 2) + cr)) / dn));
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x1a, y1a);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);
        g.setColor(Color.gray);

        int xm = (x + x2) / 2;
        int ym = (y + y2) / 2;
        circuitFrame.drawThickCircle(g, xm, ym, cr);
        cr -= 3;

        double w = (255 * current) / .01;

        if (w > 255) {
            w = 255;
        }

        Color cc = new Color((int) (colorR * w), (int) (colorG * w),
                (int) (colorB * w));
        g.setColor(cc);
        g.fillOval(xm - cr, ym - cr, cr * 2, cr * 2);
        setBbox(x, y, x2, y2);
        adjustBbox(xm - cr, ym - cr, xm + cr, ym + cr);
        updateDotCount();
        circuitFrame.drawDots(g, x, y, x1a, y1a, curcount);
        circuitFrame.drawDots(g, x2, y2, x2a, y2a, -curcount);
        drawPosts(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        super.getInfo(arr);
        arr[0] = "LED";
    }
}
