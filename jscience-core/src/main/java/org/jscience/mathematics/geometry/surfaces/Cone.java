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

import org.jscience.mathematics.geometry.ParametricSurface;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.util.Arrays;

/**
 * Represents a conical surface.
 * <p>
 * A cone is a surface formed by lines connecting a point (apex) to a circular
 * base.
 * Parametric form: S(ÃŽÂ¸, t) = (tÃ‚Â·rÃ‚Â·cos(ÃŽÂ¸), tÃ‚Â·rÃ‚Â·sin(ÃŽÂ¸), tÃ‚Â·h)
 * where ÃŽÂ¸ Ã¢Ë†Ë† [0, 2Ãâ‚¬], t Ã¢Ë†Ë† [0, 1], r is base radius, h is height
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Cone implements ParametricSurface {

    private final PointND apex;
    private final Real baseRadius;
    private final Real height;

    /**
     * Creates a cone with the given base radius and height.
     * Apex at origin, base at z = -height.
     * 
     * @param baseRadius the radius of the base
     * @param height     the height
     */
    public Cone(Real baseRadius, Real height) {
        this(PointND.of(Real.ZERO, Real.ZERO, Real.ZERO), baseRadius, height);
    }

    /**
     * Creates a cone with the given apex, base radius, and height.
     * 
     * @param apex       the apex point
     * @param baseRadius the radius of the base
     * @param height     the height (base is at apex - height along z)
     */
    public Cone(PointND apex, Real baseRadius, Real height) {
        if (apex.ambientDimension() != 3) {
            throw new IllegalArgumentException("Cone requires 3D apex point");
        }
        this.apex = apex;
        this.baseRadius = baseRadius;
        this.height = height;
    }

    @Override
    public PointND at(Real u, Real v) {
        // u = ÃŽÂ¸ Ã¢Ë†Ë† [0, 2Ãâ‚¬], v = t Ã¢Ë†Ë† [0, 1] (0 = apex, 1 = base)
        Real x = apex.get(0).add(v.multiply(baseRadius).multiply(Real.of(Math.cos(u.doubleValue()))));
        Real y = apex.get(1).add(v.multiply(baseRadius).multiply(Real.of(Math.sin(u.doubleValue()))));
        Real z = apex.get(2).subtract(v.multiply(height));
        return PointND.of(x, y, z);
    }

    @Override
    public Vector<Real> partialU(Real u, Real v, Real h) {
        // Ã¢Ë†â€šS/Ã¢Ë†â€šÃŽÂ¸ = (-vrÃ‚Â·sin(ÃŽÂ¸), vrÃ‚Â·cos(ÃŽÂ¸), 0)
        Real dx = v.multiply(baseRadius).multiply(Real.of(Math.sin(u.doubleValue()))).negate();
        Real dy = v.multiply(baseRadius).multiply(Real.of(Math.cos(u.doubleValue())));
        Real dz = Real.ZERO;
        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Vector<Real> partialV(Real u, Real v, Real h) {
        // Ã¢Ë†â€šS/Ã¢Ë†â€št = (rÃ‚Â·cos(ÃŽÂ¸), rÃ‚Â·sin(ÃŽÂ¸), -h)
        Real dx = baseRadius.multiply(Real.of(Math.cos(u.doubleValue())));
        Real dy = baseRadius.multiply(Real.of(Math.sin(u.doubleValue())));
        Real dz = height.negate();
        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Real gaussianCurvature(Real u, Real v, Real h) {
        // Gaussian curvature of cone is 0 everywhere except at apex
        if (v.abs().compareTo(Real.of(1e-10)) < 0) {
            return Real.POSITIVE_INFINITY; // Singular at apex
        }
        return Real.ZERO;
    }

    /**
     * Returns the base radius.
     * 
     * @return the base radius
     */
    public Real getBaseRadius() {
        return baseRadius;
    }

    /**
     * Returns the height.
     * 
     * @return the height
     */
    public Real getHeight() {
        return height;
    }

    /**
     * Returns the apex point.
     * 
     * @return the apex
     */
    public PointND getApex() {
        return apex;
    }

    /**
     * Returns the slant height.
     * l = Ã¢Ë†Å¡(rÃ‚Â² + hÃ‚Â²)
     * 
     * @return the slant height
     */
    public Real slantHeight() {
        Real r2 = baseRadius.multiply(baseRadius);
        Real h2 = height.multiply(height);
        return r2.add(h2).sqrt();
    }

    /**
     * Returns the lateral surface area.
     * A = Ãâ‚¬rl where l is slant height
     * 
     * @return the lateral surface area
     */
    public Real lateralSurfaceArea() {
        return Real.of(Math.PI).multiply(baseRadius).multiply(slantHeight());
    }

    /**
     * Returns the total surface area (including base).
     * A = Ãâ‚¬r(r + l)
     * 
     * @return the total surface area
     */
    public Real totalSurfaceArea() {
        return Real.of(Math.PI).multiply(baseRadius).multiply(baseRadius.add(slantHeight()));
    }

    /**
     * Returns the volume of the cone.
     * V = (1/3)Ãâ‚¬rÃ‚Â²h
     * 
     * @return the volume
     */
    public Real volume() {
        return Real.of(Math.PI / 3).multiply(baseRadius).multiply(baseRadius).multiply(height);
    }
}


