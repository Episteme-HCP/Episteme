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

import org.jscience.mathematics.chaos.*;

import java.applet.Applet;

import java.awt.*;


/**
 * Cat map transforms.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class CatTransform extends Applet implements Runnable {
    /**
     * DOCUMENT ME!
     */
    private CatMap cm;

    /**
     * DOCUMENT ME!
     */
    private double[] p1;

    /**
     * DOCUMENT ME!
     */
    private double[] p2;

    /**
     * DOCUMENT ME!
     */
    private double[] p3;

    /**
     * DOCUMENT ME!
     */
    private double[] p4;

    /**
     * DOCUMENT ME!
     */
    private Thread thr;

    /**
     * DOCUMENT ME!
     */
    private int xScale;

    /**
     * DOCUMENT ME!
     */
    private int yScale;

    /**
     * DOCUMENT ME!
     */
    private int p1x;

    /**
     * DOCUMENT ME!
     */
    private int p1y;

    /**
     * DOCUMENT ME!
     */
    private int p2x;

    /**
     * DOCUMENT ME!
     */
    private int p2y;

    /**
     * DOCUMENT ME!
     */
    private int p3x;

    /**
     * DOCUMENT ME!
     */
    private int p3y;

    /**
     * DOCUMENT ME!
     */
    private int p4x;

    /**
     * DOCUMENT ME!
     */
    private int p4y;

    /**
     * DOCUMENT ME!
     */
    private int iteration;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        cm = new CatMap();
        p1 = new double[2];
        p2 = new double[2];
        p3 = new double[2];
        p4 = new double[2];
        p1[0] = 0.1;
        p1[1] = 0.1;
        p2[0] = 0.9;
        p2[1] = 0.1;
        p3[0] = 0.9;
        p3[1] = 0.9;
        p4[0] = 0.1;
        p4[1] = 0.9;
        xScale = getSize().width;
        yScale = getSize().height;
        iteration = 0;
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        if (thr == null) {
            thr = new Thread(this);
        }

        thr.start();
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        thr = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int dataToScreenX(double x) {
        return (int) Math.round(x * xScale);
    }

    /**
     * DOCUMENT ME!
     *
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int dataToScreenY(double y) {
        return (int) Math.round(y * yScale);
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        while (thr == Thread.currentThread()) {
            p1x = dataToScreenX(p1[0]);
            p1y = dataToScreenY(p1[1]);
            p2x = dataToScreenX(p2[0]);
            p2y = dataToScreenY(p2[1]);
            p3x = dataToScreenX(p3[0]);
            p3y = dataToScreenY(p3[1]);
            p4x = dataToScreenX(p4[0]);
            p4y = dataToScreenY(p4[1]);
            repaint();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }

            p1 = cm.map(p1);
            p2 = cm.map(p2);
            p3 = cm.map(p3);
            p4 = cm.map(p4);
            iteration++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        showStatus("Iteration = " + iteration);
        g.drawLine(p1x, p1y, p2x, p2y);
        g.drawLine(p2x, p2y, p3x, p3y);
        g.drawLine(p3x, p3y, p4x, p4y);
        g.drawLine(p4x, p4y, p1x, p1y);
    }
}
