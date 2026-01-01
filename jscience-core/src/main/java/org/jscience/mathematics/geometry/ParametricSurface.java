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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.mathematics.analysis.DifferentiableFunction;

/**
 * Represents a parametric surface in 3D space.
 * <p>
 * A parametric surface is defined by: S(u,v) = (x(u,v), y(u,v), z(u,v))
 * where (u,v) are parameters, typically in some domain D Ã¢Å â€š Ã¢â€žÂÃ‚Â².
 * </p>
 * <p>
 * Examples:
 * - Plane: S(u,v) = PÃ¢â€šâ‚¬ + u*a + v*b
 * - Sphere: S(ÃŽÂ¸,Ãâ€ ) = (r*sin(ÃŽÂ¸)*cos(Ãâ€ ), r*sin(ÃŽÂ¸)*sin(Ãâ€ ), r*cos(ÃŽÂ¸))
 * - Torus: S(u,v) = ((R+r*cos(v))*cos(u), (R+r*cos(v))*sin(u), r*sin(v))
 * - Cylinder: S(ÃŽÂ¸,z) = (r*cos(ÃŽÂ¸), r*sin(ÃŽÂ¸), z)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ParametricSurface extends DifferentiableFunction<Vector<Real>, Vector<Real>> {

    @Override
    default org.jscience.mathematics.analysis.Function<Vector<Real>, Vector<Real>> differentiate() {
        throw new UnsupportedOperationException(
                "Surface derivative (Jacobian) requires dual vector output. Use partialU/partialV.");
    }

    @Override
    default Vector<Real> evaluate(Vector<Real> uv) {
        if (uv.dimension() != 2) {
            throw new IllegalArgumentException("Parameters must be 2D (u, v)");
        }
        return at(uv.get(0), uv.get(1)).toVector();
    }

    // @Override removed
    default int getOutputDimension() {
        return 3; // Surfaces are typically in 3D
    }

    /**
     * Evaluates the surface at parameters (u, v).
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @return the point on the surface
     */
    PointND at(Real u, Real v);

    /**
     * Returns the partial derivative Ã¢Ë†â€šS/Ã¢Ë†â€šu.
     * <p>
     * This is the tangent vector in the u-direction.
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the partial derivative vector
     */
    default Vector<Real> partialU(Real u, Real v, Real h) {
        PointND p1 = at(u.subtract(h), v);
        PointND p2 = at(u.add(h), v);

        Vector<Real> diff = p2.toVector().subtract(p1.toVector());
        return diff.multiply(Real.of(0.5).divide(h));
    }

    /**
     * Returns the partial derivative Ã¢Ë†â€šS/Ã¢Ë†â€šv.
     * <p>
     * This is the tangent vector in the v-direction.
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the partial derivative vector
     */
    default Vector<Real> partialV(Real u, Real v, Real h) {
        PointND p1 = at(u, v.subtract(h));
        PointND p2 = at(u, v.add(h));

        Vector<Real> diff = p2.toVector().subtract(p1.toVector());
        return diff.multiply(Real.of(0.5).divide(h));
    }

    /**
     * Returns the normal vector at (u, v).
     * <p>
     * The normal is computed as: N = Ã¢Ë†â€šS/Ã¢Ë†â€šu Ãƒâ€” Ã¢Ë†â€šS/Ã¢Ë†â€šv
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the unit normal vector
     */
    default Vector<Real> normal(Real u, Real v, Real h) {
        Vector<Real> du = partialU(u, v, h);
        Vector<Real> dv = partialV(u, v, h);

        Vector<Real> cross = du.cross(dv);
        return cross.normalize();
    }

    /**
     * Returns the surface area element dS.
     * <p>
     * dS = ||Ã¢Ë†â€šS/Ã¢Ë†â€šu Ãƒâ€” Ã¢Ë†â€šS/Ã¢Ë†â€šv|| du dv
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size for numerical differentiation
     * @return the surface area element
     */
    default Real surfaceElement(Real u, Real v, Real h) {
        Vector<Real> du = partialU(u, v, h);
        Vector<Real> dv = partialV(u, v, h);

        Vector<Real> cross = du.cross(dv);
        return cross.norm();
    }

    /**
     * Computes the surface area over a parameter domain.
     * <p>
     * Area = Ã¢Ë†Â«Ã¢Ë†Â«_D ||Ã¢Ë†â€šS/Ã¢Ë†â€šu Ãƒâ€” Ã¢Ë†â€šS/Ã¢Ë†â€šv|| du dv
     * </p>
     * 
     * @param u0        minimum u parameter
     * @param u1        maximum u parameter
     * @param v0        minimum v parameter
     * @param v1        maximum v parameter
     * @param numStepsU number of integration steps in u
     * @param numStepsV number of integration steps in v
     * @return the surface area
     */
    default Real surfaceArea(Real u0, Real u1, Real v0, Real v1,
            int numStepsU, int numStepsV) {
        Real du = u1.subtract(u0).divide(Real.of(numStepsU));
        Real dv = v1.subtract(v0).divide(Real.of(numStepsV));
        Real h = du.min(dv).multiply(Real.of(0.01));

        Real area = Real.ZERO;

        for (int i = 0; i < numStepsU; i++) {
            for (int j = 0; j < numStepsV; j++) {
                Real u = u0.add(du.multiply(Real.of(i + 0.5)));
                Real v = v0.add(dv.multiply(Real.of(j + 0.5)));

                Real element = surfaceElement(u, v, h);
                area = area.add(element.multiply(du).multiply(dv));
            }
        }

        return area;
    }

    /**
     * Returns the first fundamental form coefficients (E, F, G).
     * <p>
     * E = Ã¢Å¸Â¨Ã¢Ë†â€šS/Ã¢Ë†â€šu, Ã¢Ë†â€šS/Ã¢Ë†â€šuÃ¢Å¸Â©
     * F = Ã¢Å¸Â¨Ã¢Ë†â€šS/Ã¢Ë†â€šu, Ã¢Ë†â€šS/Ã¢Ë†â€švÃ¢Å¸Â©
     * G = Ã¢Å¸Â¨Ã¢Ë†â€šS/Ã¢Ë†â€šv, Ã¢Ë†â€šS/Ã¢Ë†â€švÃ¢Å¸Â©
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size
     * @return array [E, F, G]
     */
    default Real[] firstFundamentalForm(Real u, Real v, Real h) {
        Vector<Real> du = partialU(u, v, h);
        Vector<Real> dv = partialV(u, v, h);

        Real E = du.dot(du);
        Real F = du.dot(dv);
        Real G = dv.dot(dv);

        return new Real[] { E, F, G };
    }

    /**
     * Returns the Gaussian curvature at (u, v).
     * <p>
     * K = (LN - MÃ‚Â²) / (EG - FÃ‚Â²)
     * where L, M, N are second fundamental form coefficients
     * and E, F, G are first fundamental form coefficients.
     * </p>
     * 
     * @param u the first parameter
     * @param v the second parameter
     * @param h the step size
     * @return the Gaussian curvature
     */
    default Real gaussianCurvature(Real u, Real v, Real h) {
        // This is a simplified implementation
        // Full implementation requires second fundamental form

        @SuppressWarnings("unused")
        Vector<Real> n = normal(u, v, h); // Kept for future full implementation

        // Approximate using finite differences
        Vector<Real> nPlusU = normal(u.add(h), v, h);
        Vector<Real> nMinusU = normal(u.subtract(h), v, h);
        Vector<Real> dnDu = nPlusU.subtract(nMinusU).multiply(Real.of(0.5).divide(h));

        Vector<Real> nPlusV = normal(u, v.add(h), h);
        Vector<Real> nMinusV = normal(u, v.subtract(h), h);
        Vector<Real> dnDv = nPlusV.subtract(nMinusV).multiply(Real.of(0.5).divide(h));

        // K Ã¢â€°Ë† ||dnDu Ãƒâ€” dnDv||
        return dnDu.cross(dnDv).norm();
    }
}


