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
public class ThreeRays extends RayCaster {
    /** DOCUMENT ME! */
    private double w;

    /** DOCUMENT ME! */
    private Double3Vector pos;

/**
     * Creates a new ThreeRays object.
     */
    public ThreeRays() {
        super();
    }

/**
     * Creates a new ThreeRays object.
     *
     * @param pos    DOCUMENT ME!
     * @param inter1 DOCUMENT ME!
     * @param inter2 DOCUMENT ME!
     * @param w      DOCUMENT ME!
     */
    public ThreeRays(Double3Vector pos, Double3Vector inter1,
        Double3Vector inter2, double w) {
        super();
        setIntersections(pos, inter1, inter2, w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     * @param inter1 DOCUMENT ME!
     * @param inter2 DOCUMENT ME!
     * @param w DOCUMENT ME!
     */
    public void setIntersections(Double3Vector pos, Double3Vector inter1,
        Double3Vector inter2, double w) {
        Double3Vector dir1 = new Double3Vector(inter1.x - pos.x,
                inter1.y - pos.y, inter1.z - pos.z);
        Double3Vector dir2 = new Double3Vector(inter2.x - pos.x,
                inter2.y - pos.y, inter2.z - pos.z);
        Double3Vector dir3 = new Double3Vector(1.0, 0.0, 0.0);

        this.pos = new Double3Vector(pos);
        this.w = w;

        RayPoint r;

        rays.setSize(0);
        r = new RayPoint(pos, dir1, w);

        if (dir1.x == 0) {
            r.invalidate();
        }

        rays.addElement(new Ray(r));
        r = new RayPoint(pos, dir2, w);

        if (dir2.x == 0) {
            r.invalidate();
        }

        rays.addElement(new Ray(r));
        r = new RayPoint(pos, dir3, w);
        rays.addElement(new Ray(r));
    }

    /**
     * DOCUMENT ME!
     *
     * @param inter1 DOCUMENT ME!
     * @param inter2 DOCUMENT ME!
     */
    public void SetIntersections(Double3Vector inter1, Double3Vector inter2) {
        Double3Vector dir1 = new Double3Vector(inter1.x - pos.x,
                inter1.y - pos.y, inter1.z - pos.z);
        Double3Vector dir2 = new Double3Vector(inter2.x - pos.x,
                inter2.y - pos.y, inter2.z - pos.z);
        Double3Vector dir3 = new Double3Vector(1.0, 0.0, 0.0);

        rays.setSize(0);
        rays.addElement(new Ray(new RayPoint(pos, dir1, w)));
        rays.addElement(new Ray(new RayPoint(pos, dir2, w)));
        rays.addElement(new Ray(new RayPoint(pos, dir3, w)));
    }
}
