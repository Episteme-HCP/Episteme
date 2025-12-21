/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import org.jscience.mathematics.analysis.VectorField;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.geometry.PointND;
import java.util.ArrayList;
import java.util.List;

/**
 * Computes the gradient of a scalar field.
 * <p>
 * The gradient is a vector field that points in the direction of the greatest
 * rate of increase of the scalar field.
 * </p>
 * <p>
 * Definition: ∇f = (∂f/∂x₁, ∂f/∂x₂, ..., ∂f/∂xₙ)
 * </p>
 * <p>
 * Physical interpretation:
 * - Direction of steepest ascent
 * - Electric field: E = -∇φ (negative gradient of potential)
 * - Force from potential: F = -∇U
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Gradient {

    /**
     * Computes the gradient of a scalar field at a point using finite differences.
     * <p>
     * Uses central difference: ∂f/∂xᵢ ≈ (f(x + h*eᵢ) - f(x - h*eᵢ)) / (2h)
     * </p>
     * 
     * @param field the scalar field
     * @param point the point at which to compute the gradient
     * @param h     the step size for numerical differentiation
     * @return the gradient vector
     */
    public static Vector<Real> compute(ScalarField<PointND> field, PointND point, Real h) {
        int n = point.ambientDimension();
        List<Real> gradComponents = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            // Create points shifted by ±h in the i-th direction
            PointND pointPlus = shiftPoint(point, i, h);
            PointND pointMinus = shiftPoint(point, i, h.negate());

            // Compute partial derivative using central difference
            Real fPlus = field.evaluate(pointPlus);
            Real fMinus = field.evaluate(pointMinus);
            Real partialDerivative = fPlus.subtract(fMinus).divide(h.multiply(Real.of(2)));

            gradComponents.add(partialDerivative);
        }

        return new DenseVector<>(gradComponents, org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Returns a vector field representing the gradient of the scalar field.
     * 
     * @param field the scalar field
     * @param h     the step size for numerical differentiation
     * @return the gradient as a vector field
     */
    public static VectorField<PointND> asField(ScalarField<PointND> field, Real h) {
        return VectorField.of(
                point -> compute(field, point, h),
                field.dimension());
    }

    /**
     * Shifts a point by a given amount in a specific coordinate direction.
     * 
     * @param point           the original point
     * @param coordinateIndex the index of the coordinate to shift
     * @param delta           the amount to shift
     * @return the shifted point
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
