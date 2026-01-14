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
public class LogicOutputElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public double threshold;

    /**
     * Creates a new LogicOutputElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public LogicOutputElement(int xx, int yy) {
        super(xx, yy);
        threshold = 2.5;
    }

    /**
     * Creates a new LogicOutputElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public LogicOutputElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f);

        try {
            threshold = new Double(st.nextToken()).doubleValue();
        } catch (Exception e) {
            threshold = 2.5;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "M " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        threshold;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'M';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        Font f = new Font("SansSerif", Font.BOLD, 20);
        g.setFont(f);

        FontMetrics fm = g.getFontMetrics();
        //g.setColor(this == mouseElement ? Color.cyan : Color.lightGray);
        g.setColor(Color.lightGray);

        String s = (volts[0] < threshold) ? "L" : "H";
        double dx = x2 - x;
        double dy = y2 - y;
        double dr = Math.sqrt((dx * dx) + (dy * dy));
        dx /= dr;
        dy /= dr;

        int x3 = x2 - (int) (dx * 12);
        int y3 = y2 - (int) (dy * 12);
        setBbox(x, y, x2, y2);

        int w = fm.stringWidth(s) / 2;
        int yy = (y2 + (fm.getAscent() / 2)) - 1;
        g.drawString(s, x2 - w, yy);
        adjustBbox(x2 - w, yy - fm.getAscent(), x2 + w, yy);
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x3, y3);
        doDots(g);
        drawPost(g, x, y, nodes[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "logic output";
        arr[1] = (volts[0] < threshold) ? "low" : "high";
        arr[2] = "V = " + circuitFrame.getVoltageText(volts[0]);
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
            return new EditInfo("Threshold", threshold, 10, -10);
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
            threshold = ei.value;
        }
    }
}
