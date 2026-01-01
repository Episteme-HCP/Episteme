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

package org.jscience.mathematics.analysis.vectorcalculus;

import org.jscience.mathematics.analysis.ScalarField;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.PointND;
import java.util.ArrayList;
import java.util.List;

/**
 * Computes the Laplacian of a scalar field.
 * <p>
 * The Laplacian is the divergence of the gradient: Ã¢Ë†â€¡Ã‚Â²f = Ã¢Ë†â€¡Ã‚Â·(Ã¢Ë†â€¡f)
 * </p>
 * <p>
 * Definition: Ã¢Ë†â€¡Ã‚Â²f = Ã¢Ë†â€šÃ‚Â²f/Ã¢Ë†â€šxÃ¢â€šÂÃ‚Â² + Ã¢Ë†â€šÃ‚Â²f/Ã¢Ë†â€šxÃ¢â€šâ€šÃ‚Â² + ... + Ã¢Ë†â€šÃ‚Â²f/Ã¢Ë†â€šxÃ¢â€šâ„¢Ã‚Â²
 * </p>
 * <p>
 * Physical interpretation and applications:
 * - Heat equation: Ã¢Ë†â€šT/Ã¢Ë†â€št = ÃŽÂ±Ã¢Ë†â€¡Ã‚Â²T (heat diffusion)
 * - Wave equation: Ã¢Ë†â€šÃ‚Â²u/Ã¢Ë†â€štÃ‚Â² = cÃ‚Â²Ã¢Ë†â€¡Ã‚Â²u (wave propagation)
 * - Laplace's equation: Ã¢Ë†â€¡Ã‚Â²Ãâ€  = 0 (electrostatics, gravity)
 * - Poisson's equation: Ã¢Ë†â€¡Ã‚Â²Ãâ€  = -ÃÂ/ÃŽÂµÃ¢â€šâ‚¬ (electrostatics with charges)
 * - SchrÃƒÂ¶dinger equation: iÃ¢â€žÂÃ¢Ë†â€šÃË†/Ã¢Ë†â€št = -Ã¢â€žÂÃ‚Â²/(2m)Ã¢Ë†â€¡Ã‚Â²ÃË† + VÃË†
 * - Harmonic functions: functions where Ã¢Ë†â€¡Ã‚Â²f = 0
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Laplacian {

    /**
     * Computes the Laplacian of a scalar field at a point.
     * <p>
     * Uses second-order central difference:
     * Ã¢Ë†â€šÃ‚Â²f/Ã¢Ë†â€šxÃ¡ÂµÂ¢Ã‚Â² Ã¢â€°Ë† (f(x + h*eÃ¡ÂµÂ¢) - 2f(x) + f(x - h*eÃ¡ÂµÂ¢)) / hÃ‚Â²
     * </p>
     * 
     * @param field the scalar field
     * @param point the point at which to compute Laplacian
     * @param h     the step size for numerical differentiation
     * @return the Laplacian (scalar value)
     */
    public static Real compute(ScalarField<PointND> field, PointND point, Real h) {
        int n = point.ambientDimension();
        Real laplacian = Real.ZERO;

        Real fCenter = field.evaluate(point);
        Real hSquared = h.multiply(h);

        for (int i = 0; i < n; i++) {
            // Shift point in i-th direction
            PointND pointPlus = shiftPoint(point, i, h);
            PointND pointMinus = shiftPoint(point, i, h.negate());

            // Evaluate field at shifted points
            Real fPlus = field.evaluate(pointPlus);
            Real fMinus = field.evaluate(pointMinus);

            // Compute Ã¢Ë†â€šÃ‚Â²f/Ã¢Ë†â€šxÃ¡ÂµÂ¢Ã‚Â² using three-point stencil
            Real secondDerivative = fPlus.add(fMinus).subtract(fCenter.multiply(Real.of(2)))
                    .divide(hSquared);

            laplacian = laplacian.add(secondDerivative);
        }

        return laplacian;
    }

    /**
     * Returns a scalar field representing the Laplacian of the input field.
     * 
     * @param field the scalar field
     * @param h     the step size for numerical differentiation
     * @return the Laplacian as a scalar field
     */
    public static ScalarField<PointND> asField(ScalarField<PointND> field, Real h) {
        return ScalarField.of(
                point -> compute(field, point, h),
                field.dimension());
    }

    /**
     * Computes the vector Laplacian of a vector field.
     * <p>
     * For a vector field F, the vector Laplacian is:
     * Ã¢Ë†â€¡Ã‚Â²F = (Ã¢Ë†â€¡Ã‚Â²FÃ¢â€šÂ, Ã¢Ë†â€¡Ã‚Â²FÃ¢â€šâ€š, ..., Ã¢Ë†â€¡Ã‚Â²FÃ¢â€šâ„¢)
     * </p>
     * <p>
     * Note: In some contexts, Ã¢Ë†â€¡Ã‚Â²F = Ã¢Ë†â€¡(Ã¢Ë†â€¡Ã‚Â·F) - Ã¢Ë†â€¡Ãƒâ€”(Ã¢Ë†â€¡Ãƒâ€”F), but this is only
     * equivalent in 3D and for certain coordinate systems.
     * </p>
     * 
     * @param field the vector field
     * @param point the point
     * @param h     the step size
     * @return the vector Laplacian
     */
    public static org.jscience.mathematics.linearalgebra.Vector<Real> computeVector(
            org.jscience.mathematics.analysis.VectorField<PointND> field,
            PointND point,
            Real h) {

        int n = point.ambientDimension();
        List<Real> laplacianComponents = new ArrayList<>(n);

        // Compute Laplacian of each component
        for (int component = 0; component < n; component++) {
            final int comp = component;
            ScalarField<PointND> componentField = ScalarField.of(
                    p -> field.evaluate(p).get(comp),
                    n);
            laplacianComponents.add(compute(componentField, point, h));
        }

        return new org.jscience.mathematics.linearalgebra.vectors.DenseVector<>(
                laplacianComponents,
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Shifts a point by a given amount in a specific coordinate direction.
     */
    private static PointND shiftPoint(PointND point, int coordinateIndex, Real delta) {
        int n = point.ambientDimension();
        List<Real> newCoords = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            if (i == coordinateIndex) {
                newCoords.add(point.get(i).add(delta));
            } else {
                newCoords.add(point.get(i));
            }
        }

        return new PointND(newCoords);
    }
}


