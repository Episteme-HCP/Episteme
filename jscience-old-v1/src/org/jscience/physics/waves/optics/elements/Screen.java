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

package org.jscience.physics.waves.optics.elements;

import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Screen extends OpticalElement {
    /** DOCUMENT ME! */
    private double depth = 0.0;

    /** DOCUMENT ME! */
    private double height = 100.0;

/**
     * Creates a new Screen object.
     */
    public Screen() {
        super();
    }

/**
     * Creates a new Screen object.
     *
     * @param height DOCUMENT ME!
     * @param depth  DOCUMENT ME!
     */
    public Screen(double height, double depth) {
        super();
        this.depth = depth;
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param depth DOCUMENT ME!
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDepth() {
        return depth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void propagateRayPointSelf(RayPoint r) {
        double distance;
        distance = -r.getPosition().x;

        if (distance > 0) {
            r.goStraight(distance);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawSelf(Graphics g) {
        int r = (int) Math.round(height / 2);
        g.drawLine(0, -r, 0, r);
    }
}
