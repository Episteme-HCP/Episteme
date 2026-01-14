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

package org.jscience.mathematics.geometry.surfaces;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.geometry.ParametricSurface;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.Arrays;

/**
 * Represents a sphere as a parametric surface.
 * <p>
 * Spherical coordinates parametrization:
 * S(ÃŽÂ¸,Ãâ€ ) = center + (r*sin(ÃŽÂ¸)*cos(Ãâ€ ), r*sin(ÃŽÂ¸)*sin(Ãâ€ ), r*cos(ÃŽÂ¸))
 * where ÃŽÂ¸ Ã¢Ë†Ë† [0,Ãâ‚¬] is the polar angle and Ãâ€  Ã¢Ë†Ë† [0,2Ãâ‚¬] is the azimuthal angle.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Sphere implements ParametricSurface {

    private final PointND center;
    private final Real radius;

    /**
     * Creates a sphere.
     * 
     * @param center the center point (must be 3D)
     * @param radius the radius
     */
    public Sphere(PointND center, Real radius) {
        if (center.ambientDimension() != 3) {
            throw new IllegalArgumentException("Sphere requires 3D center");
        }
        if (radius.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        this.center = center;
        this.radius = radius;
    }

    @Override
    public PointND at(Real theta, Real phi) {
        // x = r*sin(ÃŽÂ¸)*cos(Ãâ€ )
        // y = r*sin(ÃŽÂ¸)*sin(Ãâ€ )
        // z = r*cos(ÃŽÂ¸)

        double t = theta.doubleValue();
        double p = phi.doubleValue();
        double r = radius.doubleValue();

        Real x = Real.of(r * Math.sin(t) * Math.cos(p));
        Real y = Real.of(r * Math.sin(t) * Math.sin(p));
        Real z = Real.of(r * Math.cos(t));

        Vector<Real> offset = new DenseVector<>(
                Arrays.asList(x, y, z),
                org.jscience.mathematics.sets.Reals.getInstance());

        return new PointND(center.toVector().add(offset));
    }

    @Override
    public Vector<Real> normal(Real theta, Real phi, Real h) {
        // For a sphere, the normal at any point is simply the radial direction
        // N = (point - center) / ||point - center||

        PointND point = at(theta, phi);
        Vector<Real> radial = point.toVector().subtract(center.toVector());
        return radial.normalize();
    }

    @Override
    public Real gaussianCurvature(Real theta, Real phi, Real h) {
        // Sphere has constant Gaussian curvature K = 1/rÃ‚Â²
        return Real.ONE.divide(radius.multiply(radius));
    }

    /**
     * Returns the surface area of the sphere.
     * 
     * @return 4Ãâ‚¬rÃ‚Â²
     */
    public Real surfaceArea() {
        return radius.multiply(radius).multiply(Real.of(4 * Math.PI));
    }

    /**
     * Returns the volume of the sphere.
     * 
     * @return (4/3)Ãâ‚¬rÃ‚Â³
     */
    public Real volume() {
        return radius.pow(3).multiply(Real.of(4.0 / 3.0 * Math.PI));
    }

    /**
     * Returns the center of the sphere.
     * 
     * @return the center point
     */
    public PointND getCenter() {
        return center;
    }

    /**
     * Returns the radius of the sphere.
     * 
     * @return the radius
     */
    public Real getRadius() {
        return radius;
    }

    /**
     * Checks if a point is on the surface of the sphere.
     * 
     * @param point     the point to check
     * @param tolerance the tolerance
     * @return true if on surface
     */
    public boolean contains(PointND point, Real tolerance) {
        Real distance = center.distanceTo(point);
        return distance.subtract(radius).abs().compareTo(tolerance) < 0;
    }

    @Override
    public String toString() {
        return "Sphere(center=" + center + ", radius=" + radius + ")";
    }
}


