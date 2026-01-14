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

package org.jscience.mathematics.analysis.chaos;

/**
 * The KochCurve class provides an object that encapsulates the Koch curve.
 *
 * @author Mark Hale
 * @version 0.1
 */
public abstract class KochCurve extends Object {
/**
     * Creates a new KochCurve object.
     */
    public KochCurve() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double hausdorffDimension() {
        return Math.log(4.0) / Math.log(3.0);
    }

    /**
     * The recursive algorithm for the Koch curve.
     *
     * @param startX the x-coordinate of the start of the line.
     * @param startY the x-coordinate of the start of the line.
     * @param endX the x-coordinate of the end of the line.
     * @param endY the x-coordinate of the end of the line.
     * @param n the number of recursions.
     */
    public void recurse(double startX, double startY, double endX, double endY,
        int n) {
        if (n == 0) {
            return;
        }

        final double l_3X = (endX - startX) / 3.0;
        final double l_3Y = (endY - startY) / 3.0;
        eraseLine(startX + l_3X, startY + l_3Y, endX - l_3X, endY - l_3Y);

        final double h = Math.sqrt(3.0) / 2.0;
        final double pX = ((startX + endX) / 2.0) - (l_3Y * h);
        final double pY = ((startY + endY) / 2.0) + (l_3X * h);
        drawLine(startX + l_3X, startY + l_3Y, pX, pY);
        drawLine(pX, pY, endX - l_3X, endY - l_3Y);
        recurse(startX, startY, startX + l_3X, startY + l_3Y, n - 1);
        recurse(startX + l_3X, startY + l_3Y, pX, pY, n - 1);
        recurse(pX, pY, endX - l_3X, endY - l_3Y, n - 1);
        recurse(endX - l_3X, endY - l_3Y, endX, endY, n - 1);
    }

    /**
     * Draws a line segment in a 2D plane. This need not be an actual
     * graphical operation, but some corresponding abstract operation.
     *
     * @param startX DOCUMENT ME!
     * @param startY DOCUMENT ME!
     * @param endX DOCUMENT ME!
     * @param endY DOCUMENT ME!
     */
    protected abstract void drawLine(double startX, double startY, double endX,
        double endY);

    /**
     * Erases a line segment in a 2D plane. This need not be an actual
     * graphical operation, but some corresponding abstract operation.
     *
     * @param startX DOCUMENT ME!
     * @param startY DOCUMENT ME!
     * @param endX DOCUMENT ME!
     * @param endY DOCUMENT ME!
     */
    protected abstract void eraseLine(double startX, double startY,
        double endX, double endY);
}
