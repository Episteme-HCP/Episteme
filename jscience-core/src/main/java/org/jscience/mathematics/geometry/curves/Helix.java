/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.geometry.ParametricCurve;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a 3D helix (spiral) curve.
 * <p>
 * A helix is a curve that winds around a cylinder at a constant angle.
 * Parametric form: C(t) = (rÃ‚Â·cos(t), rÃ‚Â·sin(t), hÃ‚Â·t)
 * where r is the radius and h is the pitch (height per radian).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Helix implements ParametricCurve {

    private final Real radius;
    private final Real pitch;
    private final PointND center;

    /**
     * Creates a helix with the given radius and pitch.
     * 
     * @param radius the radius of the helix
     * @param pitch  the pitch (height per radian)
     */
    public Helix(Real radius, Real pitch) {
        this(PointND.of(Real.ZERO, Real.ZERO, Real.ZERO), radius, pitch);
    }

    /**
     * Creates a helix with the given center, radius, and pitch.
     * 
     * @param center the center point (axis passes through this)
     * @param radius the radius of the helix
     * @param pitch  the pitch (height per radian)
     */
    public Helix(PointND center, Real radius, Real pitch) {
        if (center.ambientDimension() != 3) {
            throw new IllegalArgumentException("Helix requires 3D center point");
        }
        this.center = center;
        this.radius = radius;
        this.pitch = pitch;
    }

    /**
     * Creates a helix with the given 3D center, radius, and pitch.
     * 
     * @param center the center point
     * @param radius the radius of the helix
     * @param pitch  the pitch (height per radian)
     */
    public Helix(org.jscience.mathematics.geometry.Point3D center, Real radius, Real pitch) {
        this.radius = radius;
        this.pitch = pitch;
        this.center = org.jscience.mathematics.geometry.PointND.of(center.getX(), center.getY(), center.getZ());
    }

    @Override
    public PointND at(Real t) {
        Real x = center.get(0).add(radius.multiply(Real.of(Math.cos(t.doubleValue()))));
        Real y = center.get(1).add(radius.multiply(Real.of(Math.sin(t.doubleValue()))));
        Real z = center.get(2).add(pitch.multiply(t));
        return PointND.of(x, y, z);
    }

    @Override
    public int dimension() {
        return 3;
    }

    @Override
    public org.jscience.mathematics.geometry.Vector3D tangent(Real t, Real h) {
        // Analytical tangent: C'(t) = (-rÃ‚Â·sin(t), rÃ‚Â·cos(t), h)
        Real dx = radius.multiply(Real.of(Math.sin(t.doubleValue()))).negate();
        Real dy = radius.multiply(Real.of(Math.cos(t.doubleValue())));
        Real dz = pitch;
        return new org.jscience.mathematics.geometry.Vector3D(dx, dy, dz);
    }

    @Override
    public Real curvature(Real t, Real h) {
        // Analytical curvature: ÃŽÂº = r / (rÃ‚Â² + hÃ‚Â²)
        Real r2 = radius.multiply(radius);
        Real h2 = pitch.multiply(pitch);
        return radius.divide(r2.add(h2));
    }

    /**
     * Returns the radius of the helix.
     * 
     * @return the radius
     */
    public Real getRadius() {
        return radius;
    }

    /**
     * Returns the pitch of the helix.
     * 
     * @return the pitch
     */
    public Real getPitch() {
        return pitch;
    }

    /**
     * Returns the center point.
     * 
     * @return the center
     */
    public PointND getCenter() {
        return center;
    }

    /**
     * Returns the total height for one complete turn (2Ãâ‚¬ radians).
     * 
     * @return the height per turn
     */
    public Real heightPerTurn() {
        return pitch.multiply(Real.of(2 * Math.PI));
    }
}


