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
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.PointND;
import java.util.ArrayList;
import java.util.List;

/**
 * Computes the Laplacian of a scalar field.
 * <p>
 * The Laplacian is the divergence of the gradient: ∇²f = ∇·(∇f)
 * </p>
 * <p>
 * Definition: ∇²f = ∂²f/∂x₁² + ∂²f/∂x₂² + ... + ∂²f/∂xₙ²
 * </p>
 * <p>
 * Physical interpretation and applications:
 * - Heat equation: ∂T/∂t = α∇²T (heat diffusion)
 * - Wave equation: ∂²u/∂t² = c²∇²u (wave propagation)
 * - Laplace's equation: ∇²φ = 0 (electrostatics, gravity)
 * - Poisson's equation: ∇²φ = -ρ/ε₀ (electrostatics with charges)
 * - Schrödinger equation: iℏ∂ψ/∂t = -ℏ²/(2m)∇²ψ + Vψ
 * - Harmonic functions: functions where ∇²f = 0
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Laplacian {

    /**
     * Computes the Laplacian of a scalar field at a point.
     * <p>
     * Uses second-order central difference:
     * ∂²f/∂xᵢ² ≈ (f(x + h*eᵢ) - 2f(x) + f(x - h*eᵢ)) / h²
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

            // Compute ∂²f/∂xᵢ² using three-point stencil
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
     * ∇²F = (∇²F₁, ∇²F₂, ..., ∇²Fₙ)
     * </p>
     * <p>
     * Note: In some contexts, ∇²F = ∇(∇·F) - ∇×(∇×F), but this is only
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
