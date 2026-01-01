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

package org.jscience.mathematics.geometry.surfaces;

import org.jscience.mathematics.geometry.ParametricSurface;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.util.Arrays;

/**
 * Represents an ellipsoidal surface.
 * <p>
 * An ellipsoid is a quadric surface with three semi-axes.
 * Parametric form: S(ÃŽÂ¸, Ãâ€ ) = (aÃ‚Â·sin(ÃŽÂ¸)Ã‚Â·cos(Ãâ€ ), bÃ‚Â·sin(ÃŽÂ¸)Ã‚Â·sin(Ãâ€ ), cÃ‚Â·cos(ÃŽÂ¸))
 * where ÃŽÂ¸ Ã¢Ë†Ë† [0, Ãâ‚¬] (polar angle), Ãâ€  Ã¢Ë†Ë† [0, 2Ãâ‚¬] (azimuthal angle)
 * a, b, c are the semi-axes
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Ellipsoid implements ParametricSurface {

    private final PointND center;
    private final Real semiAxisA;
    private final Real semiAxisB;
    private final Real semiAxisC;

    /**
     * Creates an ellipsoid with the given semi-axes.
     * 
     * @param semiAxisA the semi-axis along x
     * @param semiAxisB the semi-axis along y
     * @param semiAxisC the semi-axis along z
     */
    public Ellipsoid(Real semiAxisA, Real semiAxisB, Real semiAxisC) {
        this(PointND.of(Real.ZERO, Real.ZERO, Real.ZERO), semiAxisA, semiAxisB, semiAxisC);
    }

    /**
     * Creates an ellipsoid with the given center and semi-axes.
     * 
     * @param center    the center point
     * @param semiAxisA the semi-axis along x
     * @param semiAxisB the semi-axis along y
     * @param semiAxisC the semi-axis along z
     */
    public Ellipsoid(PointND center, Real semiAxisA, Real semiAxisB, Real semiAxisC) {
        if (center.ambientDimension() != 3) {
            throw new IllegalArgumentException("Ellipsoid requires 3D center point");
        }
        this.center = center;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
        this.semiAxisC = semiAxisC;
    }

    @Override
    public PointND at(Real u, Real v) {
        // u = ÃŽÂ¸ Ã¢Ë†Ë† [0, Ãâ‚¬], v = Ãâ€  Ã¢Ë†Ë† [0, 2Ãâ‚¬]
        Real sinTheta = Real.of(Math.sin(u.doubleValue()));
        Real cosTheta = Real.of(Math.cos(u.doubleValue()));
        Real sinPhi = Real.of(Math.sin(v.doubleValue()));
        Real cosPhi = Real.of(Math.cos(v.doubleValue()));

        Real x = center.get(0).add(semiAxisA.multiply(sinTheta).multiply(cosPhi));
        Real y = center.get(1).add(semiAxisB.multiply(sinTheta).multiply(sinPhi));
        Real z = center.get(2).add(semiAxisC.multiply(cosTheta));

        return PointND.of(x, y, z);
    }

    @Override
    public Vector<Real> partialU(Real u, Real v, Real h) {
        // Ã¢Ë†â€šS/Ã¢Ë†â€šÃŽÂ¸ = (aÃ‚Â·cos(ÃŽÂ¸)Ã‚Â·cos(Ãâ€ ), bÃ‚Â·cos(ÃŽÂ¸)Ã‚Â·sin(Ãâ€ ), -cÃ‚Â·sin(ÃŽÂ¸))
        Real cosTheta = Real.of(Math.cos(u.doubleValue()));
        Real sinTheta = Real.of(Math.sin(u.doubleValue()));
        Real sinPhi = Real.of(Math.sin(v.doubleValue()));
        Real cosPhi = Real.of(Math.cos(v.doubleValue()));

        Real dx = semiAxisA.multiply(cosTheta).multiply(cosPhi);
        Real dy = semiAxisB.multiply(cosTheta).multiply(sinPhi);
        Real dz = semiAxisC.multiply(sinTheta).negate();

        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Vector<Real> partialV(Real u, Real v, Real h) {
        // Ã¢Ë†â€šS/Ã¢Ë†â€šÃâ€  = (-aÃ‚Â·sin(ÃŽÂ¸)Ã‚Â·sin(Ãâ€ ), bÃ‚Â·sin(ÃŽÂ¸)Ã‚Â·cos(Ãâ€ ), 0)
        Real sinTheta = Real.of(Math.sin(u.doubleValue()));
        Real sinPhi = Real.of(Math.sin(v.doubleValue()));
        Real cosPhi = Real.of(Math.cos(v.doubleValue()));

        Real dx = semiAxisA.multiply(sinTheta).multiply(sinPhi).negate();
        Real dy = semiAxisB.multiply(sinTheta).multiply(cosPhi);
        Real dz = Real.ZERO;

        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Returns the semi-axis along x.
     * 
     * @return the semi-axis a
     */
    public Real getSemiAxisA() {
        return semiAxisA;
    }

    /**
     * Returns the semi-axis along y.
     * 
     * @return the semi-axis b
     */
    public Real getSemiAxisB() {
        return semiAxisB;
    }

    /**
     * Returns the semi-axis along z.
     * 
     * @return the semi-axis c
     */
    public Real getSemiAxisC() {
        return semiAxisC;
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
     * Returns the approximate volume using the formula V = (4/3)Ãâ‚¬abc.
     * 
     * @return the volume
     */
    public Real volume() {
        return Real.of(4 * Math.PI / 3)
                .multiply(semiAxisA)
                .multiply(semiAxisB)
                .multiply(semiAxisC);
    }

    /**
     * Checks if a point is inside the ellipsoid.
     * 
     * @param point     the point to check
     * @param tolerance the tolerance
     * @return true if inside
     */
    public boolean contains(PointND point, Real tolerance) {
        if (point.ambientDimension() != 3) {
            return false;
        }

        Real dx = point.get(0).subtract(center.get(0));
        Real dy = point.get(1).subtract(center.get(1));
        Real dz = point.get(2).subtract(center.get(2));

        Real term = dx.divide(semiAxisA).pow(Real.of(2))
                .add(dy.divide(semiAxisB).pow(Real.of(2)))
                .add(dz.divide(semiAxisC).pow(Real.of(2)));

        return term.subtract(Real.ONE).abs().compareTo(tolerance) <= 0;
    }
}


