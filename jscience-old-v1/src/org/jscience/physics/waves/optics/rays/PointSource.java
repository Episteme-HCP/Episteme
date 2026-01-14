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

package org.jscience.physics.waves.optics.rays;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PointSource extends RayCaster {
    /** DOCUMENT ME! */
    private double n;

    /** DOCUMENT ME! */
    private double width;

    /** DOCUMENT ME! */
    private double w;

    /** DOCUMENT ME! */
    private Double3Vector s;

    /** DOCUMENT ME! */
    private Double3Vector d1;

    /** DOCUMENT ME! */
    private Double3Vector d2;

/**
     * Creates a new PointSource object.
     */
    public PointSource() {
        super();
    }

/**
     * Creates a new PointSource object.
     *
     * @param src   DOCUMENT ME!
     * @param dest1 DOCUMENT ME!
     * @param dest2 DOCUMENT ME!
     * @param n     DOCUMENT ME!
     * @param w     DOCUMENT ME!
     */
    public PointSource(Double3Vector src, Double3Vector dest1,
        Double3Vector dest2, int n, double w) {
        super();

        this.n = n;

        //this.width = width;
        this.w = w;

        Move(src, dest1, dest2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param dest1 DOCUMENT ME!
     * @param dest2 DOCUMENT ME!
     */
    public void Move(Double3Vector src, Double3Vector dest1, Double3Vector dest2) {
        s = new Double3Vector(src);

        if (dest1 != null) {
            d1 = new Double3Vector(dest1);
        }

        if (dest2 != null) {
            d2 = new Double3Vector(dest2);
        }

        rays.setSize(0);

        double diffX = (d2.x - d1.x);
        double diffY = (d2.y - d1.y);

        for (int i = 0; i < n; i++) {
            Double3Vector newdir = new Double3Vector(d1.x - s.x +
                    ((i * diffX) / (n - 1)),
                    d1.y - s.y + ((i * diffY) / (n - 1)), d1.z - s.z);
            rays.addElement(new Ray(new RayPoint(s, newdir, w)));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     */
    public void Move(Double3Vector src) {
        Move(src, null, null);
    }
}
